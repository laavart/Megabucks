package com.psloba.megabucks;

import com.psloba.citra.*;
import com.psloba.citra.exception.*;
import com.psloba.megabucks.Application.*;

public class Main {

    static public Database database;
    static public Client client;

    public static void main(String[] args) throws DBInvalidException {
        database = Database.connect(Source.MYSQL, "localhost", "megabucks", "root", "1234");
        LogInApplication.launch(args);
    }
}
