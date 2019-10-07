/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.alunos.felipe.restful;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import br.edu.utfpr.alunos.felipe.restful.auth.JwtAnnotation;
import br.edu.utfpr.alunos.felipe.restful.auth.JwtTools;
import br.edu.utfpr.alunos.felipe.restful.database.Mongo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import static com.mongodb.client.model.Filters.eq;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.ws.rs.Consumes;
import org.bson.Document;
/**
 * REST Web Service
 *
 * @author felipe
 */
@Path("user")
@JwtAnnotation
public class User {

    @Context
    private UriInfo context;
    
    private final Mongo database = new Mongo();
    private MongoCollection<Document> collection = database.mongoDatabase.getCollection("restful");

    /**
     * Creates a new instance of User
     */
    public User() {
    }

    /**
     * Retrieves representation of an instance of br.edu.utfpr.alunos.felipe.restful.User
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllUsers() {
        List<UserEntity> users = new ArrayList<>();
        MongoCursor<Document> cursor = collection.find().iterator();
        while(cursor.hasNext()) {
            users.add(new UserEntity(cursor.next()));
        }
        try {
            String json = new ObjectMapper().writeValueAsString(users);
            return Response.ok(json, MediaType.APPLICATION_JSON).build();
        } catch (JsonProcessingException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response.ok().status(Response.Status.NOT_FOUND).build();
    }
    
    @POST
    @Path("login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response authorizationService(String inputStream) {
        JsonReader reader = Json.createReader(new StringReader(inputStream));
        JsonObject jsonObject = reader.readObject();
        
        UserEntity user = new UserEntity(jsonObject.getString("username"), jsonObject.getString("password"));
     
        if (user == null) {
            return Response.ok().status(Response.Status.BAD_REQUEST).build();
        }
        
        if (verifyExists(user)) {
           String privateKey = JwtTools.getInstance().generateKey(user.getUserName(), user.getPassword());
           return Response.ok(privateKey).status(Response.Status.OK).build();
        }
        
        return Response.ok().status(Response.Status.NOT_FOUND).build();
    }
    
    @POST
    @Path("register")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response registerUser(String inputStream) {
        JsonReader reader = Json.createReader(new StringReader(inputStream));
        JsonObject jsonObject = reader.readObject();
        
        UserEntity user = new UserEntity(jsonObject.getString("username"), jsonObject.getString("password"));
        
        if (user == null) {
            return Response.ok().status(Response.Status.BAD_REQUEST).build();
        }
        
        createUser(user);
        return Response.ok().status(Response.Status.CREATED).build();
    }
    
    private void createUser(UserEntity user) {
        Document doc = new Document()
                .append("username", user.getUserName())
                .append("password", user.getPassword());
        
        collection.insertOne(doc);
    }
    
    private boolean verifyExists(UserEntity user) {
        Document document = collection.find(eq("username", user.getUserName())).first();
        
        return document != null;
    }
}
