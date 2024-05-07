package com.analysis;
// import java.io.*;
// import java.net.HttpURLConnection;
// import java.net.URL;
// import java.util.List;
// import org.json.JSONObject;

// public class GraphQLSOAPService {

//     public String invokeGraphQLService() {
//         String responseString = "";
//         try {
//             // Build the GraphQL request
//             String graphqlQuery = "query{  users{    uid  }}";
//             String graphqlRequest = "{\"query\": \"" + graphqlQuery + "\"}";

//             // Set up the connection to the GraphQL server
//             URL url = new URL("http://localhost:80/graphql");
//             HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//             connection.setRequestMethod("POST");
//             connection.setRequestProperty("Content-Type", "application/json");
//             connection.setDoOutput(true);

//             // Send the GraphQL request
//             OutputStream outputStream = connection.getOutputStream();
//             outputStream.write(graphqlRequest.getBytes());
//             outputStream.flush();

//             // Read the response from the GraphQL server
//             if (connection.getResponseCode() != 200) {
//                 throw new RuntimeException("Failed : HTTP error code : " + connection.getResponseCode());
//             }

//             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader((connection.getInputStream())));

//             String output;
//             System.out.println("Output from Server .... \n");
//             while ((output = bufferedReader.readLine()) != null) {
//                 responseString += output;
//             }

//             connection.disconnect();

//         } catch (IOException e) {
//             e.printStackTrace();
//         }


//         return responseString;
//     }

//     private int getUserFollowerCount(String uid) throws IOException {
//         // Build the GraphQL request to get the followers count for the user
//         String followersQuery = "query{ followers(id: \"" + uid + "\"){ followers } }";
//         String graphqlRequest = "{\"query\": \"" + followersQuery + "\"}";

//         // Set up the connection to the GraphQL server
//         URL url = new URL("http://localhost:80/graphql");
//         HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//         connection.setRequestMethod("POST");
//         connection.setRequestProperty("Content-Type", "application/json");
//         connection.setDoOutput(true);

//         // Send the GraphQL request to get the followers for the user
//         OutputStream outputStream = connection.getOutputStream();
//         outputStream.write(graphqlRequest.getBytes());
//         outputStream.flush();

//         // Read the response from the GraphQL server
//         if (connection.getResponseCode() != 200) {
//             throw new RuntimeException("Failed : HTTP error code : " + connection.getResponseCode());
//         }

//         BufferedReader bufferedReader = new BufferedReader(new InputStreamReader((connection.getInputStream())));
//         StringBuilder followersResponseBuilder = new StringBuilder();
//         String output;
//         while ((output = bufferedReader.readLine()) != null) {
//             followersResponseBuilder.append(output);
//         }
//         bufferedReader.close();

//         // Parse the response to extract the follower count
//         // Implement parsing logic here
//         // int followerCount = parseFollowerCount(followersResponseBuilder.toString());
//         // return followerCount;

//         return 0; // Placeholder return value
//     }

//     private double calculateAverageFollowerCount(List<Integer> followerCounts) {
//         double totalFollowerCount = 0;
//         for (int count : followerCounts) {
//             totalFollowerCount += count;
//         }
//         return totalFollowerCount / followerCounts.size();
//     }

//     // public static void main(String[] args) {
//     //     GraphQLSOAPService graphQLSOAPService = new GraphQLSOAPService();
//     //     String response = graphQLSOAPService.invokeGraphQLService();
//     //     System.out.println(response);
//     // }
// }

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class GraphQLSOAPService {

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


    // public static void main(String[] args) throws IOException {
    //     GraphQLSOAPService graphQLService = new GraphQLSOAPService();
    //     String followersCountResponse = graphQLService.invokeGraphQLService();
    //     System.out.println("Response from GraphQL server:");
    //     System.out.println(followersCountResponse);
    // }
}