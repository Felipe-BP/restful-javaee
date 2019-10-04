/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.alunos.felipe.restful;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import br.edu.utfpr.alunos.felipe.restful.auth.JwtAnnotation;
import br.edu.utfpr.alunos.felipe.restful.auth.JwtTools;
import br.edu.utfpr.alunos.felipe.restful.database.Mongo;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
/**
 * REST Web Service
 *
 * @author felipe
 */
@Path("user")
public class User {

    @Context
    private UriInfo context;
    
    private final Mongo database = new Mongo();

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
    public String getJson() {
        MongoCollection<Document> collection = database.mongoDatabase.getCollection("Joao Cesar");
        
        Document doc = new Document()
              .append("name", "Joe")
              .append("dept", "IT")
              .append("phone", "111-111-111");
        collection.insertOne(doc);
        
        return "Saved Successfully";
    }
    
    @POST
    @Path("authorization")
    @JwtAnnotation
    @Produces(MediaType.APPLICATION_JSON)
    public Response authorizationService(@QueryParam("username") String user, @QueryParam("password") String pass) {
        if (user.isEmpty())
            return Response.ok("username empty").build();
        if (pass.isEmpty())
            return Response.ok("password empty").build();
        String privateKey = JwtTools.getInstance().generateKey(user, pass);
        return Response.ok(privateKey).status(Response.Status.OK).build();
    }
    

    /**
     * PUT method for updating or creating an instance of User
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void putJson(String content) {
    }
}
