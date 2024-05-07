package com.analysis;

import jakarta.xml.ws.Endpoint;

public class ServerJWS {
    public static void main(String[] args) {
        String userServiceAddress = "http://0.0.0.0:8081/userService";
        String albumServiceAddress = "http://0.0.0.0:8081/albumService";
        Endpoint.publish(userServiceAddress, new userService());
        Endpoint.publish(albumServiceAddress, new albumService());
        System.out.println("User Service Address : " + userServiceAddress);
        System.out.println("Album Service Address : " + albumServiceAddress);
    }
}