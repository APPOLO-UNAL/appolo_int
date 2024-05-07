package com.analysis;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class GraphQLSOAPUserService {

    public String getAverageFollows() {
        try {
            // Build the GraphQL request to get the list of users
            double totalFollowerCount = 0;
            String usersQuery = "query{ users{ uid } }";
            String graphqlRequest = "{\"query\": \"" + usersQuery + "\"}";

            // Get the list of UIDs from the GraphQL response
            JSONArray usersArray = getUsersUIDs(graphqlRequest);
            for (int i = 0; i < usersArray.length(); i++) {
                JSONObject userObject = usersArray.getJSONObject(i);
                String uid = userObject.getString("uid");
                //System.out.println("Processing user with UID: " + uid);
                // Build and send the GraphQL request to get follower count for each UID
                String followersQuery = "query{    followersCount(id:\\\""+uid+"\\\"){       followers      }  }";
                //System.out.println("GraphQL Query for UID " + uid + ": " + followersQuery);
                String followersGraphqlRequest = "{\"query\": \"" + followersQuery + "\"}";
                // Send the request and process the response
                int responseFoll = getFollowersCount(followersGraphqlRequest);
                //System.out.println("GraphQL response for  " + uid + ": " + responseFoll);
                totalFollowerCount += responseFoll;
            }
            // Calculate the average follower count
            double averageFollowers = (totalFollowerCount / usersArray.length());
            return "Average number of follows in app: " + averageFollowers;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getAverageFollowing() {
        try {
            // Build the GraphQL request to get the list of users
            double totalFollowerCount = 0;
            String usersQuery = "query{ users{ uid } }";
            String graphqlRequest = "{\"query\": \"" + usersQuery + "\"}";

            // Get the list of UIDs from the GraphQL response
            JSONArray usersArray = getUsersUIDs(graphqlRequest);
            for (int i = 0; i < usersArray.length(); i++) {
                JSONObject userObject = usersArray.getJSONObject(i);
                String uid = userObject.getString("uid");
                //System.out.println("Processing user with UID: " + uid);
                // Build and send the GraphQL request to get follower count for each UID
                String followersQuery = "query{    followingCount(id:\\\""+uid+"\\\"){       following      }  }";
                //System.out.println("GraphQL Query for UID " + uid + ": " + followersQuery);
                String followersGraphqlRequest = "{\"query\": \"" + followersQuery + "\"}";
                // Send the request and process the response
                int responseFoll = getFollowingCount(followersGraphqlRequest);
                //System.out.println("GraphQL response for  " + uid + ": " + responseFoll);
                totalFollowerCount += responseFoll;
            }
            // Calculate the average follower count
            double averageFollowings = (totalFollowerCount / usersArray.length());
            return "Average number of following in app: " + averageFollowings;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getMostFollowed(){
        try {
            // Build the GraphQL request to get the list of users
            int maxFollowerCount = 0;
            String mostFollowedUID = "";
            String usersQuery = "query{ users{ uid } }";
            String graphqlRequest = "{\"query\": \"" + usersQuery + "\"}";

            // Get the list of UIDs from the GraphQL response
            JSONArray usersArray = getUsersUIDs(graphqlRequest);
            for (int i = 0; i < usersArray.length(); i++) {
                JSONObject userObject = usersArray.getJSONObject(i);
                String uid = userObject.getString("uid");
                //System.out.println("Processing user with UID: " + uid);
                // Build and send the GraphQL request to get follower count for each UID
                String followersQuery = "query{    followersCount(id:\\\""+uid+"\\\"){       followers      }  }";
                //System.out.println("GraphQL Query for UID " + uid + ": " + followersQuery);
                String followersGraphqlRequest = "{\"query\": \"" + followersQuery + "\"}";
                // Send the request and process the response
                int responseFoll = getFollowersCount(followersGraphqlRequest);
                //System.out.println("GraphQL response for  " + uid + ": " + responseFoll);
                if(responseFoll > maxFollowerCount){
                    maxFollowerCount = responseFoll;
                    mostFollowedUID = uid;
                }
            }
            
            String userQuery ="query{ user(id:\\\""+ mostFollowedUID +"\\\"){    userName  }}";
            String userGraphqlRequest = "{\"query\": \"" + userQuery + "\"}";
            String userName = getUserNames(userGraphqlRequest);

            return "Most followed user in app: " + userName + " with " + maxFollowerCount + " followers";

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getFollowingMost(){
        try {
            // Build the GraphQL request to get the list of users
            int maxFollowingCount = 0;
            String mostFollowingUID = "";
            String usersQuery = "query{ users{ uid } }";
            String graphqlRequest = "{\"query\": \"" + usersQuery + "\"}";

            // Get the list of UIDs from the GraphQL response
            JSONArray usersArray = getUsersUIDs(graphqlRequest);
            for (int i = 0; i < usersArray.length(); i++) {
                JSONObject userObject = usersArray.getJSONObject(i);
                String uid = userObject.getString("uid");
                //System.out.println("Processing user with UID: " + uid);
                // Build and send the GraphQL request to get follower count for each UID
                String followersQuery = "query{    followingCount(id:\\\""+uid+"\\\"){       following      }  }";
                //System.out.println("GraphQL Query for UID " + uid + ": " + followersQuery);
                String followersGraphqlRequest = "{\"query\": \"" + followersQuery + "\"}";
                // Send the request and process the response
                int responseFoll = getFollowingCount(followersGraphqlRequest);
                //System.out.println("GraphQL response for  " + uid + ": " + responseFoll);
                if(responseFoll > maxFollowingCount){
                    maxFollowingCount = responseFoll;
                    mostFollowingUID = uid;
                }
            }

            String userQuery ="query{ user(id:\\\""+ mostFollowingUID +"\\\"){    userName  }}";
            String userGraphqlRequest = "{\"query\": \"" + userQuery + "\"}";
            String userName = getUserNames(userGraphqlRequest);
            // Calculate the average follower count
            return "User following most people in app: " + userName + " following " + maxFollowingCount + " users";

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private JSONObject ProcessResponse(String queryString){
        String responseString = "";
        try {
            // Set up the connection to the GraphQL server
            URL url = new URL("http://localhost:80/graphql");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            // Send the GraphQL request
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(queryString.getBytes());
            outputStream.flush();

            // Read the response from the GraphQL server
            if (connection.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + connection.getResponseCode());
            }

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader((connection.getInputStream())));

            String output;
            //System.out.println("Output from Server .... \n");
            while ((output = bufferedReader.readLine()) != null) {
                responseString += output;
            }

            connection.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONObject responseObject = new JSONObject(responseString.toString());
        return responseObject;
    }

    private JSONArray getUsersUIDs(String graphqlRequest) throws IOException {
        // Parse the JSON response to extract the UIDs of the users
        JSONObject responseObject = ProcessResponse(graphqlRequest);
        JSONObject dataObject = responseObject.getJSONObject("data");
        JSONArray usersArray = dataObject.getJSONArray("users");
        return usersArray;
    }

    private int getFollowersCount(String graphqlRequest) throws IOException {
        // Parse the JSON response to extract the followers count
        JSONObject responseObject = ProcessResponse(graphqlRequest);
        JSONObject dataObject = responseObject.getJSONObject("data");
        JSONObject follCountObject = dataObject.getJSONObject("followersCount");
        int followersNum = follCountObject.getInt("followers");
        return followersNum;
    }
    private int getFollowingCount(String graphqlRequest) throws IOException {
        // Parse the JSON response to extract the followers count
        JSONObject responseObject = ProcessResponse(graphqlRequest);
        JSONObject dataObject = responseObject.getJSONObject("data");
        JSONObject follCountObject = dataObject.getJSONObject("followingCount");
        int followersNum = follCountObject.getInt("following");
        return followersNum;
    }

    private String getUserNames(String userGraphqlRequest) {
        // Parse the JSON response to extract the UIDs of the users
        JSONObject responseObject = ProcessResponse(userGraphqlRequest);
        JSONObject dataObject = responseObject.getJSONObject("data");
        JSONObject userObject = dataObject.getJSONObject("user");
        String userName = userObject.getString("userName");
        return userName;
    }


    // public static void main(String[] args) throws IOException {
    //     GraphQLSOAPService graphQLService = new GraphQLSOAPService();
    //     String followersCountResponse = graphQLService.invokeGraphQLService();
    //     System.out.println("Response from GraphQL server:");
    //     System.out.println(followersCountResponse);
    // }
}