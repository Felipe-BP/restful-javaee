/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.alunos.felipe.restful;

import javax.xml.bind.annotation.XmlRootElement;
import org.bson.Document;

/**
 *
 * @author felipe
 */
@XmlRootElement
public class UserEntity {
    private String userName;
    private String hash;
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserEntity(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }
    
    public UserEntity(Document doc) {
        this.userName = doc.getString("username");
        this.password = doc.getString("password");
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
}
