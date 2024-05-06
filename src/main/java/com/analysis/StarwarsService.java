package com.analysis;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
//POJO
@WebService(serviceName = "StarwarsWS")
public class StarwarsService {
    @WebMethod(operationName = "StarwarsCharacter")
    public StarwarsCharacter getStarwarsCharacter(@WebParam int id,@WebParam String name){
        return new StarwarsCharacter(id,name,new Date());
    }
    @WebMethod(operationName = "StarwarsCharacterList")
    public List<StarwarsCharacter> starwarsCharacterList(){
        return Arrays.asList(new StarwarsCharacter(1,"Obi-One",new Date()),
                new StarwarsCharacter(1,"Anakin",new Date()),
                new StarwarsCharacter(1,"Padmé",new Date()));
    }
}

/*
-------------Modificar para obtener canciones más reseñadas, o más populares--------------------
-------------Y los usuarios más seguidos, o más activos-----------------------------------------

 * código para conectar con la API de GraphQL y enviar una solicitud de consulta GraphQL a un servidor GraphQL. (segun chatGPT)
 * import java.io*;
import java.net.*;
import javax.xml.soap.*;
import javax.xml.transform.*;

public class GraphQLSOAPService {

    public String invokeGraphQLService() {
        try {
            // Construir la solicitud GraphQL
            String graphqlQuery = "query { user(id: \"123\") { id name email } }";
            String graphqlRequest = "{\"query\": \"" + graphqlQuery + "\"}";

            // Establecer la conexión con el servidor GraphQL
            URL url = new URL("http://localhost:80/graphql");
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
        Transformer transformer = transformerFactory.newTransformer();
        java.io.StringWriter sw = new java.io.StringWriter();
        transformer.transform(new javax.xml.transform.stream.StreamSource(soapMessage.getSOAPPart().getContent()), new StreamResult(sw));
        return sw.toString();
    }

    public static void main(String[] args) {
        GraphQLSOAPService soapService = new GraphQLSOAPService();
        String soapResponse = soapService.invokeGraphQLService();
        System.out.println("SOAP Response:");
        System.out.println(soapResponse);
    }
}

 * 
 */