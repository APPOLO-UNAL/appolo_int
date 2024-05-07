package com.analysis;

public class GraphQLSOAPAlbumService {
    //query{  comments{    rate  }}
    public String getAverageRating(){
        return "Average Rating";
    }

    //el item de musica con más comentarios 
    //query{  comments{    itemMusicId  }}
    //query{  tracksByIdTrack(id:"3mH6qwIy9crq0I9YQbOuDf"){    name    artists{      name    }  }}

    //el comentario con más respuestas/replys
    //query{  comments{    Id  }}
    //query{  replies(parentId:"663991cc161df6084bcf08c1"){    Id  }}
}
