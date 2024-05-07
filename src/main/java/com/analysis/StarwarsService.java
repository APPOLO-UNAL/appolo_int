package com.analysis;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
//POJO

//TODO change methods to match with the GraphQL service
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