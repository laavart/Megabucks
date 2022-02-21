package com.psloba.citra;

import com.psloba.citra.client.*;

public record Client(User user, Security security, Comm comm, Address address) {

    public Client(User user, Comm comm, Address address){
        this(user, null, comm, address);
    }
}
