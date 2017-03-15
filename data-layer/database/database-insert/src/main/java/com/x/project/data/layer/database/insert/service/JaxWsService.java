package com.x.project.data.layer.database.insert.service;

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
    public boolean insertRow(final @WebParam TableRow row);

}
