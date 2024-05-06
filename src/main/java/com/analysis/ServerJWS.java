package com.analysis;

import jakarta.xml.ws.Endpoint;

public class ServerJWS {
    public static void main(String[] args) {
        String address="http://0.0.0.0:8081/";
        Endpoint.publish(address, new StarwarsService());
        System.out.println("Address : "+address);
    }
}