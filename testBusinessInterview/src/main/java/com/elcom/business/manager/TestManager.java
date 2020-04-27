/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.business.manager;

import com.elcom.model.dto.interview.ResponseData;
import com.elcom.sharedbiz.manager.BaseManager;
import java.util.concurrent.Callable;
import javax.ws.rs.core.Response;

/**
 *
 * @author Admin
 */
public class TestManager extends BaseManager {

    public ResponseData doProcessData(String req) throws Exception {
        return tryCatch(new Callable<ResponseData>() {
            @Override
            public ResponseData call() throws Exception {
                return new ResponseData(Response.Status.OK.getStatusCode(), Response.Status.OK.toString(), "Hello");
            }
        });

//        return this.tryCatch(() -> {
//            return new ResponseData(
//                    Response.Status.OK.getStatusCode(),
//                    Response.Status.OK.toString(),
//                    "Hello"
//            );
//        });
    }
}
