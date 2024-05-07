package com.analysis;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
//POJO

import org.json.JSONObject;

//TODO change methods to match with the GraphQL service
@WebService(serviceName = "UserWS")
public class userService {
    GraphQLSOAPService graphQLSOAPService = new GraphQLSOAPService();
    //     
    @WebMethod(operationName = "StatsFollowers")
    public String getStatsFollowers(){
        String response = graphQLSOAPService.getAverageFollows();
        System.out.println(response);
        return response;
    }
    @WebMethod(operationName = "StatsFollowing")
    public String getStatsFollowing(){
        String response = graphQLSOAPService.getAverageFollowing();
        System.out.println(response);
        return response;
    }
    @WebMethod(operationName = "MostFollowed")
    public String getMostFollowed(){
        String response = graphQLSOAPService.getMostFollowed();
        System.out.println(response);
        return response;
    }
    @WebMethod(operationName = "FollowingMost")
    public String getFollowingMost(){
        String response = graphQLSOAPService.getFollowingMost();
        System.out.println(response);
        return response;
    }

}