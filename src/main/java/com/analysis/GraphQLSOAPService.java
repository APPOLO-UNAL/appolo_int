package com.analysis;


// import java.io.*;
// import java.net.*;
// import javax.xml.transform.*;
// import javax.xml.transform.stream.StreamResult;

// import jakarta.xml.soap.*;

// public class GraphQLSOAPService {

//     public String invokeGraphQLService() {
//         try {
//             // Construir la solicitud GraphQL
//             String graphqlQuery = "query{  userComments(userId:\"d7e624ef472342d4b1f634a816ca0bb3\"){    content  }}";
//             String graphqlRequest = "{\"query\": \"" + graphqlQuery + "\"}";

//             // Establecer la conexión con el servidor GraphQL
//             URL url = new URL("http://localhost/graphql");
//             HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//             connection.setRequestMethod("POST");
//             connection.setRequestProperty("Content-Type", "application/json");
//             connection.setDoOutput(true);

//             // Enviar la solicitud GraphQL
//             OutputStream outputStream = connection.getOutputStream();
//             outputStream.write(graphqlRequest.getBytes());
//             outputStream.flush();

//             // Leer la respuesta del servidor GraphQL
//             BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//             StringBuilder responseBuilder = new StringBuilder();
//             String line;
//             while ((line = reader.readLine()) != null) {
//                 responseBuilder.append(line);
//             }
//             reader.close();

//             // Procesar la respuesta GraphQL
//             String graphqlResponse = responseBuilder.toString();

//             // Construir y enviar la respuesta SOAP
//             SOAPMessage soapResponse = buildSOAPResponse(graphqlResponse);
//             return serializeSOAPMessage(soapResponse);
//         } catch (IOException | SOAPException e) {
//             e.printStackTrace();
//             return null;
//         }
//     }

//     private SOAPMessage buildSOAPResponse(String graphqlResponse) throws SOAPException {
//         SOAPMessage soapMessage = MessageFactory.newInstance().createMessage();
//         SOAPEnvelope envelope = soapMessage.getSOAPPart().getEnvelope();
//         SOAPBody body = envelope.getBody();

//         // Añadir el contenido GraphQL a la respuesta SOAP
//         SOAPElement responseElement = body.addChildElement(envelope.createName("graphqlResponse"));
//         responseElement.addTextNode(graphqlResponse);

//         return soapMessage;
//     }

//     private String serializeSOAPMessage(SOAPMessage soapMessage) throws SOAPException, IOException {
//         TransformerFactory transformerFactory = TransformerFactory.newInstance();
//         Transformer transformer;
//         java.io.StringWriter sw;
//         try {
//             transformer = transformerFactory.newTransformer();
//             sw = new java.io.StringWriter();
//             transformer.transform(new javax.xml.transform.stream.StreamSource(), new StreamResult(sw));
//         } catch (TransformerConfigurationException e) {
//             // TODO Auto-generated catch block
//             sw = null;
//             e.printStackTrace();
//         } catch (TransformerException e) {
//             sw = null;
//             // TODO Auto-generated catch block
//             e.printStackTrace();
//         }
        
        
//         return sw.toString();
//     }

//     public static void main(String[] args) {
//         GraphQLSOAPService soapService = new GraphQLSOAPService();
//         String soapResponse = soapService.invokeGraphQLService();
//         System.out.println("SOAP Response:");
//         System.out.println(soapResponse);
//     }
// }

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class GraphQLSOAPService {

    public String invokeGraphQLService() {
        String responseString = "";
        try {
            // Build the GraphQL request
            String graphqlQuery = "query{  users{    uid  }}";
            String graphqlRequest = "{\"query\": \"" + graphqlQuery + "\"}";

            // Set up the connection to the GraphQL server
            URL url = new URL("http://localhost:80/graphql");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            // Send the GraphQL request
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(graphqlRequest.getBytes());
            outputStream.flush();

            // Read the response from the GraphQL server
            if (connection.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + connection.getResponseCode());
            }

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader((connection.getInputStream())));

            String output;
            System.out.println("Output from Server .... \n");
            while ((output = bufferedReader.readLine()) != null) {
                responseString += output;
            }

            connection.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return responseString;
    }

    public static void main(String[] args) {
        GraphQLSOAPService graphQLSOAPService = new GraphQLSOAPService();
        String response = graphQLSOAPService.invokeGraphQLService();
        System.out.println(response);
    }
}