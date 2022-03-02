package citra.client;

public record Address(String address1, String address2, String postal, String city, String state, String country) {
    public Address(String city, String state, String country){
        this(null, null, null, city, state, country);
    }
}
