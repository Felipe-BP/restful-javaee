
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.alunos.felipe.restful.database;


import com.mongodb.client.*;
/**
 *
 * @author Aluno
 */
public class Mongo {
    private final String DB_CONNECTION = "mongodb://localhost:27017";
    private final String DB_NAME = "restful";
    private MongoClient mongoClient;
    public MongoDatabase mongoDatabase;
    
    public Mongo() {
        mongoClient = MongoClients.create(DB_CONNECTION);
        mongoDatabase = mongoClient.getDatabase(DB_NAME);
    }
}