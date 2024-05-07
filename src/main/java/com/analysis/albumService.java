package com.analysis;

import jakarta.jws.WebMethod;
import jakarta.jws.WebService;

//TODO change methods to match with the GraphQL service
@WebService(serviceName = "AlbumWS")
public class albumService {
    // @WebMethod(operationName = "AverageLikes")
    // public int getAverageLikes(){
    //     return 0;
    // }
    @WebMethod(operationName = "AverageRating")
    public void getAverageRating(){
        return;
    }
    
}