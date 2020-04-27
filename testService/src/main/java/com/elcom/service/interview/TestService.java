/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.service.interview;

import com.elcom.business.manager.CompanyManager;
import com.elcom.business.manager.TestManager;
import com.elcom.service.BasedService;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Admin
 */
@Path("/v1.0/test")
public class TestService extends BasedService {

    public TestService(@Context HttpHeaders headers, @Context HttpServletRequest request) {
        super(headers, request);
    }
    
    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response hello(String req) throws Exception {
        System.out.println("req: " + req);
        try (TestManager manager = new TestManager()) {
            return ok(manager.doProcessData(req));
        }
    }
}
