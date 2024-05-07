package com.analysis;

import jakarta.jws.WebMethod;
import jakarta.jws.WebService;

//TODO change methods to match with the GraphQL service
@WebService(serviceName = "AlbumWS")
public class albumService {
    GraphQLSOAPAlbumService graphQLSOAPService = new GraphQLSOAPAlbumService();
    // @WebMethod(operationName = "AverageLikes")
    // public int getAverageLikes(){
    //     return 0;
    // }
    @WebMethod(operationName = "AverageRating")
    public String getAverageRating(){
        String response = graphQLSOAPService.getAverageRating();
        return response;
    }
    @WebMethod(operationName = "MostRatedItem")
    public String getMostRatedItem(){
        String response = graphQLSOAPService.getItemMusicMostComm();
        return response;
    }
    @WebMethod(operationName = "MostRepliedComm")
    public String getMostRepliedComm(){
        String response = graphQLSOAPService.getMostRepliedComm();
        return response;
    }
    
}