package citra;

import java.sql.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.regex.Pattern;

import citra.client.*;
import citra.exception.*;
import citra.util.Pair;
import citra.util.Source;

public class Database {

    private Source source;
    private String database;

    private Connection connection = null;
    private Statement statement = null;

    private static final ArrayList<Source> CheckForPort;
    static {
        CheckForPort = new ArrayList<>(1);
        CheckForPort.add(Source.MYSQL);
    }

    private static final ArrayList<Source> CheckByOwner;
    static {
        CheckByOwner = new ArrayList<>(1);
        CheckByOwner.add(Source.ORACLE);
    }

    private Database(){
        System.out.println("Connection Started !");
    }

    private static void initialize(Database db) throws SQLException {
        if(!db.searchTable("user_address_code_country")){
            db.statement.executeUpdate(
                    "create table user_address_code_country (" +
                            "cID int primary key," +
                            "Country varchar(100)" +
                            ");"
            );
        }

        if(!db.searchTable("user_address_code_state")){
            db.statement.executeUpdate(
                    "create table user_address_code_state (" +
                            "sID int primary key," +
                            "State varchar(100), " +
                            "Country int, " +
                            "foreign key (Country) references user_address_code_country(cID)" +
                            ");"
            );
        }

        if(!db.searchTable("user_address_code_postal")){
            db.statement.executeUpdate(
                    "create table user_address_code_postal (" +
                            "pID int primary key," +
                            "City varchar(100)," +
                            "PostalCode varchar(6), " +
                            "State int, " +
                            "foreign key (State) references user_address_code_state(sID)" +
                            ");"
            );
        }

        if(!db.searchTable("user_address")){
            db.statement.executeUpdate(
                    "create table user_address (" +
                            "uID int primary key, " +
                            "AddressLine1 varchar(100), " +
                            "AddressLine2 varchar(100), " +
                            "Location int, " +
                            "foreign key (Location) references user_address_code_postal(pID) " +
                            ");"
            );
        }

        if(!db.searchTable("user_master")){
            db.statement.executeUpdate(
                    "create table user_master (" +
                            "uID int primary key, " +
                            "Username varchar(128) unique, " +
                            "Name varchar(100), " +
                            "DOB date" +
                            ");"
            );
        }

        if(!db.searchTable("token_master")){
            db.statement.executeUpdate(
                    "create table token_master (" +
                            "uID int primary key, " +
                            "Token varchar(128), " +
                            "Code varchar(7)" +
                            ");"
            );
        }

        if(!db.searchTable("comm_master")){
            db.statement.executeUpdate(
                    "create table comm_master (" +
                            "uID int primary key, " +
                            "Email varchar(150) unique, " +
                            "Mobile varchar(10) unique" +
                            ");"
            );
        }

        if(!db.searchTable("reuse_master")){
            db.statement.executeUpdate(
                    "create table reuse_master (" +
                            "rID int primary key" +
                            ");"
            );
        }

        if(!db.searchTable("revoke_master")){
            db.statement.executeUpdate(
                    "create table revoke_master (" +
                            "rvID int primary key" +
                            ");"
            );
        }

        db.statement.executeUpdate("set autocommit = 0;");
    }

    public boolean searchTable(String name) throws SQLException {
        ResultSet resultSet =
                CheckByOwner.contains(source)
                        ? statement.executeQuery(source.getTables(database))
                        : statement.executeQuery(source.getTables()) ;

        while(resultSet.next())
            if( resultSet.getString(1).equals(name) ) return true;

        return false;
    }

    public static Database connect(Source source, String hostname, String port, String database, String user, String token) throws DBInvalidException {
        if(CheckForPort.contains(source)) throw new DBInvalidException();
        else {
            try {
                Class.forName(source.getDriver());
                Database db = new Database();
                db.source = source;
                db.database = database;

                db.connection = DriverManager.getConnection(source.getPath(hostname, port, database), user, token);
                System.out.println("Connection Established !");

                db.statement = db.connection.createStatement();
                initialize(db);

                return db;

            } catch (ClassNotFoundException | SQLException e) {
                System.out.println("Connection Failed !");
                return null;
            }
        }
    }

    public static Database connect(Source source, String hostname, String database, String user, String token) throws DBInvalidException {
        if(!CheckForPort.contains(source)) throw new DBInvalidException();
        else {
            try {
                Class.forName(source.getDriver());
                Database db = new Database();
                db.source = source;
                db.database = database;

                db.connection = DriverManager.getConnection(source.getPath(hostname, database), user, token);
                System.out.println("Connection Established !");

                db.statement = db.connection.createStatement();
                initialize(db);

                return db;

            } catch (ClassNotFoundException | SQLException e) {
                System.out.println("Connection Failed !");
                return null;
            }
        }
    }

    private int checkForUser(String user) throws SQLException {
        ResultSet resultSet = statement.executeQuery(
                "select uID from user_master where Username = '"+user+"';"
        );
        return resultSet.next() ? resultSet.getInt(1) : -1;
    }

    private boolean checkForEmail(String email) throws SQLException {
        ResultSet resultSet = statement.executeQuery(
                "select * from comm_master where Email = '"+email+"';"
        );
        return resultSet.next();
    }

    private boolean checkForMobile(String mobile) throws SQLException {
        ResultSet resultSet = statement.executeQuery(
                "select * from comm_master where Mobile = '"+mobile+"';"
        );
        return resultSet.next();
    }

    private boolean validateWithToken(String username, String token) throws SQLException{
        if(checkForUser(username) != -1){
            ResultSet resultSet = statement.executeQuery(
                    "select token from token_master where uID = any(select uID from user_master where Username = '"+username+"');"
            );
            return resultSet.next() && resultSet.getString("Token").equals(token);
        }
        return false;
    }

    private boolean validateWithCode(String username, String code) throws SQLException{
        if(checkForUser(username) != -1) {
            ResultSet resultSet = statement.executeQuery(
                    "select code from token_master where uID = any(select uID from user_master where Username = '"+username+"');"
            );
            return resultSet.next() && resultSet.getString("Code").equals(code);
        }
        return false;
    }

    private int getCountryCode(Address address) throws SQLException {
        ResultSet resultSet = statement.executeQuery(
                "select cID from user_address_code_country where Country = '"+address.country()+"';"
        );

        if(!resultSet.next()){
            resultSet = statement.executeQuery(
                    "select max(cID)+1 from user_address_code_country;"
            );

            int cID = resultSet.next() ? resultSet.getInt(1) : 1;
            statement.executeUpdate(
                    "insert into user_address_code_country values (" +
                            cID + "," +
                            "'" + address.country() + "'" +
                            ");"
            );
            return cID;

        } else return resultSet.getInt(1);
    }

    private int getStateCode(Address address) throws SQLException {
        int cID = getCountryCode(address);

        ResultSet resultSet = statement.executeQuery(
                "select sID from user_address_code_state where State = '"+address.state()+"';"
        );

        if(!resultSet.next()){
            resultSet = statement.executeQuery(
                    "select max(sID)+1 from user_address_code_state;"
            );

            int sID = resultSet.next() ? resultSet.getInt(1) : 1;
            statement.executeUpdate(
                    "insert into user_address_code_state values (" +
                            sID + "," +
                            "'" + address.state() + "'," +
                            cID +
                            ");"
            );
            return sID;

        } else return resultSet.getInt(1);
    }

    private int getPostalCode(Address address) throws SQLException {
        int sID = getStateCode(address);

        ResultSet resultSet = statement.executeQuery(
                "select pID from user_address_code_postal where PostalCode = '"+address.postal()+"';"
        );

        if(!resultSet.next()){
            resultSet = statement.executeQuery(
                    "select max(pID)+1 from user_address_code_postal;"
            );

            int pID = resultSet.next() ? resultSet.getInt(1) : 1;
            statement.executeUpdate(
                    "insert into user_address_code_postal values (" +
                            pID + "," +
                            "'" + address.city() + "'," +
                            "'" + address.postal() + "'," +
                            sID +
                            ");"
            );
            return pID;

        } else return resultSet.getInt(1);
    }

    private String getCountry(int cID) throws SQLException {
        ResultSet resultSet = statement.executeQuery(
                "select Country from user_address_code_country where cID = "+cID+";"
        );
        return resultSet.next() ? resultSet.getString(1) : "";
    }

    private String getState(int sID) throws SQLException {
        ResultSet resultSet = statement.executeQuery(
                "select State from user_address_code_state where sID = "+sID+";"
        );
        return resultSet.next() ? resultSet.getString(1) : "";
    }


    private String getCity(int pID) throws SQLException {
        ResultSet resultSet = statement.executeQuery(
                "select City from user_address_code_postal where pID = "+pID+";"
        );
        return resultSet.next() ? resultSet.getString(1) : "";
    }

    private Client getUser(String username, String token){
        try {
            if(validateWithToken(username, token)){
                ResultSet resultSet = statement.executeQuery(
                        "select " +
                                "user_master.Name, user_master.DOB, comm_master.Email, comm_master.Mobile, user_address.AddressLine1, user_address.AddressLine2, " +
                                "user_address_code_postal.PostalCode, user_address_code_postal.City, user_address_code_state.State, user_address_code_country.Country " +
                                "from user_master " +
                                "join user_address on user_master.uID = user_address.uID " +
                                "join comm_master on user_address.uID = comm_master.uID " +
                                "join user_address_code_postal on user_address.Location = user_address_code_postal.pID " +
                                "join user_address_code_state on user_address_code_postal.State = user_address_code_state.sID " +
                                "join user_address_code_country on user_address_code_state.Country = user_address_code_country.cID " +
                                "where Username = '" + username + "'" +
                                ";"
                );

                if (resultSet.next()) {
                    User user = new User(resultSet.getString(1), resultSet.getDate(2).toLocalDate());
                    Comm comm = new Comm(resultSet.getString(3), resultSet.getString(4));
                    Address address = new Address(resultSet.getString(5), resultSet.getString(6), resultSet.getString(7), resultSet.getString(8), resultSet.getString(9), resultSet.getString(10));

                    return new Client(user, comm, address);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error in Fetching Data!");
        }

        return null;
    }

    public boolean searchUser(String user) {
        try {
            return checkForUser(user) != -1;
        } catch (SQLException e) {
            System.out.println("Error in Search!");
        }
        return false;
    }

    public Pair<Integer, Client> validateUser(String username, String token){
        try {
            int id = checkForUser(username);

            if(id != -1){
                ResultSet resultSet = statement.executeQuery(
                        "select rvID from revoke_master where rvID = "+id+";"
                );

                if(!resultSet.next()) return new Pair<>(id, getUser(username,token));
            }
        } catch (SQLException e) {
            System.out.println("Validation Error!");
        }

        System.out.println("User not found!");
        return null;
    }

    public int addNewUser(Client client){
        try{
            if( checkForUser(client.user().username()) != -1 )
                System.out.println("User already Exist!");
            else if( checkForEmail(client.comm().email()) )
                System.out.println("Email already Exist!");
            else if( checkForMobile(client.comm().mobile()) )
                System.out.println("Mobile Number already Exist!");
            else if(!Pattern.compile("^.*(?=.{8,128})(?=..*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$").matcher(client.security().token()).matches())
                System.out.println("Password not valid!");
            else if(client.security().code().length() != 7)
                System.out.println("Security Code must exactly be of 7 digits!");
            else if(!Pattern.compile("^(?=.{7,150})[a-zA-Z0-9+._-]+@[a-zA-Z0-9.]+$").matcher(client.comm().email()).matches())
                System.out.println("Invalid E-mail ID!");
            else if(client.comm().mobile().length() != 10)
                System.out.println("Invalid Mobile Number!");
            else if(client.address().postal().length() != 6)
                System.out.println("Pin-code is of 6 digits!");
            else{
                statement.executeUpdate("start transaction ;");

                int id;
                ResultSet resultSet = statement.executeQuery("select rID from reuse_master;");
                if(resultSet.next()) {
                    id = resultSet.getInt("rID");
                    statement.executeUpdate("delete from reuse_master where rID =" + id + ";");
                }
                else{
                    resultSet.close();
                    resultSet = statement.executeQuery("select max(uid)+1 from user_master;");
                    id = resultSet.next() ? resultSet.getInt(1) : 1;
                }

                statement.executeUpdate(
                        "insert into token_master values (" +
                                id + "," +
                                "'" + client.security().token() + "'," +
                                "'" + client.security().code() + "'" +
                                ");"
                );

                statement.executeUpdate(
                        "insert into comm_master values (" +
                                id + "," +
                                "'" + client.comm().email() + "'," +
                                "'" + client.comm().mobile() + "'" +
                                ");"
                );

                statement.executeUpdate(
                        "insert into user_master values (" +
                                id + "," +
                                "'" + client.user().username() + "'," +
                                "'" + client.user().name() + "'," +
                                "'" + client.user().dob().format(DateTimeFormatter.ISO_LOCAL_DATE) + "'" +
                                ");"
                );

                statement.executeUpdate(
                        "insert into user_address values (" +
                                id + "," +
                                "'" + client.address().address1() + "'," +
                                "'" + client.address().address2() + "'," +
                                getPostalCode(client.address()) +
                                ");"
                );

                statement.executeUpdate("commit ;");

                System.out.println("User Registered!");
                return id;
            }

        } catch (SQLException e) {
            System.out.println("Registration Failed!");
        }

        try {
            statement.executeUpdate("rollback;");
        } catch (SQLException ex) {
            System.out.println("Rollback Error!");
        }
        return -1;
    }

    public Address getLocation(String postal){
        try {
            ResultSet resultSet = statement.executeQuery(
                    "select user_address_code_postal.pID, user_address_code_state.sID, user_address_code_country.cID " +
                            "from user_address " +
                            "join user_address_code_postal on user_address.Location = user_address_code_postal.pID " +
                            "join user_address_code_state on user_address_code_postal.State = user_address_code_state.sID " +
                            "join user_address_code_country on user_address_code_state.Country = user_address_code_country.cID " +
                            "where user_address_code_postal.PostalCode = '" + postal + "'" +
                            ";"
            );
            if(resultSet.next()) {
                int pID = resultSet.getInt(1), sID = resultSet.getInt(2), cID = resultSet.getInt(3);
                return new Address(getCity(pID), getState(sID), getCountry(cID));
            }
            else System.out.println("Unable to retrieve Location");
        } catch (SQLException e) {
            System.out.println("Error in Location!");
        }
        return null;
    }

    public boolean changePassword(String username, String code, String newToken){
        try {
            if(validateWithCode(username,code)) {
                if(Pattern.compile("^.*(?=.{8,128})(?=..*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$").matcher(newToken).matches()){
                    statement.executeUpdate(
                            "update token_master " +
                                    "set Token = " +
                                    "'" + newToken + "' " +
                                    "where code = " +
                                    "'" + code + "'" +
                                    ";"
                    );
                    return true;
                }
                else System.out.println("Password not valid!");
            }
            else System.out.println("Security Code Incorrect!");
        }
        catch (SQLException e) {
            System.out.println("Couldn't update password!");
        }
        return false;
    }

    public boolean deleteUser(String username, String code){
        try {
            if(validateWithCode(username, code)){
                int id = checkForUser(username);

                statement.executeUpdate("start transaction ;");

                statement.executeUpdate(
                        "delete from user_master where uID = "+id+";"
                );

                statement.executeUpdate(
                        "delete from token_master where uID = "+id+";"
                );

                statement.executeUpdate(
                        "delete from comm_master where uID = "+id+";"
                );

                statement.executeUpdate(
                        "delete from user_address where uID = "+id+";"
                );

                statement.executeUpdate(
                        "insert into reuse_master value("+id+");"
                );

                statement.executeUpdate("commit ;");

                System.out.println("User Record Deleted!");
                return true;
            }
            else System.out.println("User doesn't exists!");
        }
        catch (SQLException e) {
            System.out.println("Deletion Failed!");
            try {
                statement.executeUpdate("rollback ;");
            } catch (SQLException ex) {
                System.out.println("Rollback Error!");
            }
        }

        return false;
    }

    public boolean updateUserInfo(Client client, String token){
        try {
            if( !validateWithToken(client.user().username(), token) )
                System.out.println("UnAuthorized Request Declined!");
            else if (checkForEmail(client.comm().email()))
                System.out.println("Email already Exist!");
            else if (checkForMobile(client.comm().mobile()))
                System.out.println("Mobile Number already Exist!");
            else if (!Pattern.compile("^(?=.{7,150})[a-zA-Z0-9+._-]+@[a-zA-Z0-9.]+$").matcher(client.comm().email()).matches())
                System.out.println("Invalid E-mail ID!");
            else if (client.comm().mobile().length() != 10)
                System.out.println("Invalid Mobile Number!");
            else if (client.address().postal().length() != 6)
                System.out.println("Pin-code is of 6 digits!");
            else {
                statement.executeUpdate("start transaction ;");

                int id = checkForUser(client.user().username());

                statement.executeUpdate(
                        "update comm_master set " +
                                "Email = '" + client.comm().email() + "'," +
                                "Mobile = '" + client.comm().mobile() + "' " +
                                "where uID = " + id +
                                ";"
                );

                statement.executeUpdate(
                        "update user_master set " +
                                "Name = '" + client.user().name() + "'," +
                                "Dob = '" + client.user().dob().format(DateTimeFormatter.ISO_LOCAL_DATE) + "' " +
                                "where uID = " + id +
                                ";"
                );

                statement.executeUpdate(
                        "update user_address set " +
                                "AddressLine1 = '" + client.address().address1() + "'," +
                                "AddressLine2 = '" + client.address().address2() + "'," +
                                "Location = " + getPostalCode(client.address()) + " " +
                                "where uID = " + id +
                                ";"
                );

                statement.executeUpdate("commit ;");

                System.out.println("User details updated!");

                return true;
            }

        } catch (SQLException e) {
            System.out.println("Update Failed!");

            try {
                statement.executeUpdate("rollback;");
            } catch (SQLException ex) {
                System.out.println("Error!");
            }
        }
        return false;
    }

    public boolean revokeAccess(String username, String code){
        try {
            if(validateWithCode(username, code)) {
                int id = checkForUser(username);

                if (id != -1) {
                    statement.executeUpdate(
                            "insert into revoke_master values (" + id + ");"
                    );

                    System.out.println("Access Revoked!");
                    return true;
                }
            }

        } catch (SQLException e) {
            System.out.println("Revoke Error!");
        }

        return false;
    }

    public boolean grantAccess(String username, String code){
        try {
            if(validateWithCode(username, code)) {
                int id = checkForUser(username);

                if (id != -1) {
                    statement.executeUpdate(
                            "delete from revoke_master where rvID = " + id + ";"
                    );

                    System.out.println("Access Granted!");
                    return true;
                }
            }

        } catch (SQLException e) {
            System.out.println("Grant Error!");
        }

        return false;
    }

    public ResultSet executeQuery(String query) throws SQLException {
        if(query.contains("show")) System.out.println("Can't perform this action!");
        else return statement.executeQuery(query);
        return null;
    }

    public boolean executeUpdate(String query) throws SQLException {
        if(query.contains("modify") || query.contains("delete") ) System.out.println("Can't perform this action!");
        else {
            statement.executeUpdate(query);
            return true;
        }
        return false;
    }
}
