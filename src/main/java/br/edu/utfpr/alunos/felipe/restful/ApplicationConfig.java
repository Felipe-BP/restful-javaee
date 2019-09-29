/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.alunos.felipe.restful;

import br.edu.utfpr.alunos.felipe.restful.auth.CORSFilter;
import java.util.HashMap;
import java.util.Map;
import br.edu.utfpr.alunos.felipe.restful.auth.JwtInterceptor;
import com.sun.jersey.api.core.DefaultResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;

/**
 *
 * @author felipe
 */
@javax.ws.rs.ApplicationPath("/")
public class ApplicationConfig extends DefaultResourceConfig {

    public ApplicationConfig() {
        super(User.class);
        Map<String, Object> maps = new HashMap<>();
        maps.put(ResourceConfig.PROPERTY_CONTAINER_RESPONSE_FILTERS, CORSFilter.class);
        maps.put(ResourceConfig.PROPERTY_CONTAINER_REQUEST_FILTERS, JwtInterceptor.class);
        setPropertiesAndFeatures(maps);
    }
    
}
