package com.analysis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class GraphQLSOAPAlbumService {
    
    public String getAverageRating(){
        //query{  comments{    rate  }}
        try {
            // Build the GraphQL request to get the list of users
            double totalRatingCount = 0;
            String commentsQuery = "query{  comments{    rate  }}";
            String graphqlRequest = "{\"query\": \"" + commentsQuery + "\"}";

            // Get the list of UIDs from the GraphQL response
            JSONArray ratingsArray = getCommentsRate(graphqlRequest);
            for (int i = 0; i < ratingsArray.length(); i++) {
                JSONObject userObject = ratingsArray.getJSONObject(i);
                int rating = userObject.getInt("rate");
                totalRatingCount += rating;
            }
            // Calculate the average follower count
            double averageRating = (totalRatingCount / ratingsArray.length());
            return "Average rating of music reviewed in app: " + averageRating;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //el item de musica con más comentarios 
    //query{  tracksByIdTrack(id:"3mH6qwIy9crq0I9YQbOuDf"){    name    artists{      name    }  }}
    public String getItemMusicMostComm(){
        try {
            // Build the GraphQL request to get the list of users
            double totalFollowerCount = 0;
            Map<String, Integer> idCounts = new HashMap<>();
            String commentsQuery = "query{  comments{    itemMusicId  }}";
            String graphqlRequest = "{\"query\": \"" + commentsQuery + "\"}";

            // Get the list of UIDs from the GraphQL response
            JSONArray commentsArray = getCommentsUIDs(graphqlRequest);
            for (int i = 0; i < commentsArray.length(); i++) {
                JSONObject userObject = commentsArray.getJSONObject(i);
                String Id = userObject.getString("itemMusicId");
                idCounts.put(Id, idCounts.getOrDefault(Id, 0) + 1);
            }
            String mostCommonId = null;
            int maxCount = 0;

            for (Map.Entry<String, Integer> entry : idCounts.entrySet()) {
                if (mostCommonId == null || entry.getValue() > maxCount) {
                    mostCommonId = entry.getKey();
                    maxCount = entry.getValue();
                }
            }

            // Calculate the average follower count
            String tracksQuery = "query{    tracksByIdTrack(id:\\\""+mostCommonId+"\\\"){    name    artists{      name    }  }  }";
            //System.out.println("GraphQL Query for UID " + uid + ": " + followersQuery);
            String tracksGraphqlRequest = "{\"query\": \"" + tracksQuery + "\"}";
            //TODO: get track name and artist name
            String[] arrName  = getTrackArtName(tracksGraphqlRequest);
            return "Most reviewed track is " + arrName[0] + " by " + arrName[1] + " with " + maxCount + " reviews";

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //el comentario con más respuestas/replys
    //query{  comments{    Id  }}
    //query{  replies(parentId:"663991cc161df6084bcf08c1"){    Id  }}

    public String getMostRepliedComm(){
        try {
            // Build the GraphQL request to get the list of users
            double maxReplyCount = 0;
            String mostRepliedUID = "";
            String commentsQuery = "query{  comments{    Id  }}";
            String graphqlRequest = "{\"query\": \"" + commentsQuery + "\"}";

            // Get the list of UIDs from the GraphQL response
            JSONArray commentsArray = getCommentsUIDs(graphqlRequest);
            for (int i = 0; i < commentsArray.length(); i++) {
                JSONObject commentsObject = commentsArray.getJSONObject(i);
                String Id = commentsObject.getString("Id");
                //System.out.println("Processing comment with ID: " + Id);
                // Build and send the GraphQL request to get follower count for each UID
                String tracksQuery = "query{    replies(parentId:\\\""+Id+"\\\"){    Id  }}";
                //System.out.println("GraphQL Query for UID " + uid + ": " + followersQuery);
                String tracksGraphqlRequest = "{\"query\": \"" + tracksQuery + "\"}";
                int responseRepl = getRepliesNum(tracksGraphqlRequest);
                //System.out.println("GraphQL response for  " + uid + ": " + responseFoll);
                if(responseRepl > maxReplyCount){
                    maxReplyCount = responseRepl;
                    mostRepliedUID = Id;
                }

            }
            // Calculate the average follower count
            return "Most replied comment id is: " + mostRepliedUID + " with " + maxReplyCount + " replies";

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private int getRepliesNum(String tracksGraphqlRequest) throws IOException{
        JSONObject responseObject = ProcessResponse(tracksGraphqlRequest);
        System.out.println(responseObject);
        JSONObject dataObject = responseObject.getJSONObject("data");
        JSONArray repliesArray = dataObject.getJSONArray("replies");
        return repliesArray.length();
    }

    private String[] getTrackArtName(String followersGraphqlRequest) {
        JSONObject responseObject = ProcessResponse(followersGraphqlRequest);
        System.out.println(responseObject);
        JSONObject dataObject = responseObject.getJSONObject("data");
        JSONObject tracksObject = dataObject.getJSONObject("tracksByIdTrack");
        String trackName = tracksObject.getString("name");
        JSONArray artistsArray = tracksObject.getJSONArray("artists");
        String artistName = artistsArray.getJSONObject(0).getString("name");
        return new String[] { trackName, artistName };
    }

    private JSONArray getCommentsUIDs(String graphqlRequest) throws IOException{
        JSONObject responseObject = ProcessResponse(graphqlRequest);
        JSONObject dataObject = responseObject.getJSONObject("data");
        JSONArray commentsArray = dataObject.getJSONArray("comments");
        return commentsArray;
    }

    private JSONArray getCommentsRate(String graphqlRequest) throws IOException{
        JSONObject responseObject = ProcessResponse(graphqlRequest);
        JSONObject dataObject = responseObject.getJSONObject("data");
        JSONArray commentsArray = dataObject.getJSONArray("comments");
        return commentsArray;
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

    public static void main(String[] args) throws IOException {
        GraphQLSOAPAlbumService graphQLService = new GraphQLSOAPAlbumService();
        String followersCountResponse = graphQLService.getItemMusicMostComm();
        System.out.println("Response from GraphQL server:");
        System.out.println(followersCountResponse);
    }
}
