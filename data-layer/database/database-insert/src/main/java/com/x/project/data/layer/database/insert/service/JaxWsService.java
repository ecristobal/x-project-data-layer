package com.x.project.data.layer.database.insert.service;

import javax.jws.Oneway;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * JAX-WS service interface.
 * 
 * @author Esteban Cristóbal
 */
@WebService
public interface JaxWsService {

    @WebMethod
    @Oneway
    public void insertRow(final @WebParam TableRow row);

}
