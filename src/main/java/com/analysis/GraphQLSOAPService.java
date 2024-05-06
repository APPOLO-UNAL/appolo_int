package com.analysis;
import java.io.*;
import java.net.*;
import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;

import jakarta.xml.soap.*;

public class GraphQLSOAPService {

    public String invokeGraphQLService() {
        try {
            // Construir la solicitud GraphQL
            String graphqlQuery = "query {          comments {               Id               userId               picture               userName          }     }";
            String graphqlRequest = "{\"query\": \"" + graphqlQuery + "\"}";

            // Establecer la conexión con el servidor GraphQL
            URL url = new URL("http://localhost/graphql");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            // Enviar la solicitud GraphQL
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(graphqlRequest.getBytes());
            outputStream.flush();

            // Leer la respuesta del servidor GraphQL
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder responseBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                responseBuilder.append(line);
            }
            reader.close();

            // Procesar la respuesta GraphQL
            String graphqlResponse = responseBuilder.toString();

            // Construir y enviar la respuesta SOAP
            SOAPMessage soapResponse = buildSOAPResponse(graphqlResponse);
            return serializeSOAPMessage(soapResponse);
        } catch (IOException | SOAPException e) {
            e.printStackTrace();
            return null;
        }
    }

    private SOAPMessage buildSOAPResponse(String graphqlResponse) throws SOAPException {
        SOAPMessage soapMessage = MessageFactory.newInstance().createMessage();
        SOAPEnvelope envelope = soapMessage.getSOAPPart().getEnvelope();
        SOAPBody body = envelope.getBody();

        // Añadir el contenido GraphQL a la respuesta SOAP
        SOAPElement responseElement = body.addChildElement(envelope.createName("graphqlResponse"));
        responseElement.addTextNode(graphqlResponse);

        return soapMessage;
    }

    private String serializeSOAPMessage(SOAPMessage soapMessage) throws SOAPException, IOException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer;
        java.io.StringWriter sw;
        try {
            transformer = transformerFactory.newTransformer();
            sw = new java.io.StringWriter();
            transformer.transform(new javax.xml.transform.stream.StreamSource(), new StreamResult(sw));
        } catch (TransformerConfigurationException e) {
            // TODO Auto-generated catch block
            sw = null;
            e.printStackTrace();
        } catch (TransformerException e) {
            sw = null;
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        
        return sw.toString();
    }

    public static void main(String[] args) {
        GraphQLSOAPService soapService = new GraphQLSOAPService();
        String soapResponse = soapService.invokeGraphQLService();
        System.out.println("SOAP Response:");
        System.out.println(soapResponse);
    }
}