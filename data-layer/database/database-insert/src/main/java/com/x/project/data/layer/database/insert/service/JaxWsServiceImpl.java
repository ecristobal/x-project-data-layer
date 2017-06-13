package com.x.project.data.layer.database.insert.service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of {@link JaxWsService} interface
 * 
 * @author Esteban Cristóbal
 */
public class JaxWsServiceImpl implements JaxWsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(JaxWsServiceImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

    /*
     * (non-Javadoc)
     * @see
     * com.x.project.data.layer.database.insert.service.JaxWsService#insertRow(
     * com.x.project.data.layer.database.insert.service.TableRow)
     */
    @Transactional
    public void insertRow(TableRow row) {
        LOGGER.info("Entering SOAP endpoint");
        final TableRowEntity entity = new TableRowEntity();
        entity.setName(row.getName());
        entity.setValue(row.getValue());
        try {
            this.entityManager.persist(entity);
        } catch (Exception e) {
            LOGGER.error("Exception thrown:\n", e);
        }
    }

}
