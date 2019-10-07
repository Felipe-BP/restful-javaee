/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.alunos.felipe.restful;

import br.edu.utfpr.alunos.felipe.restful.auth.CORSFilter;
import br.edu.utfpr.alunos.felipe.restful.auth.JwtInterceptor;
import org.glassfish.jersey.server.ResourceConfig;

/**
 *
 * @author felipe
 */

@javax.ws.rs.ApplicationPath("/")
public class ApplicationConfig extends ResourceConfig {

    public ApplicationConfig() {
        System.out.println("FELIPE STARTED");
        packages("br.edu.utfpr.alunos.felipe.restful");
        register(JwtInterceptor.class);
        register(CORSFilter.class);
        
        System.out.println(isRegistered(JwtInterceptor.class));
    }
    
}
