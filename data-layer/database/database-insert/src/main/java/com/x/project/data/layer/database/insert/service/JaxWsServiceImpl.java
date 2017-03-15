package com.x.project.data.layer.database.insert.service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

/**
 * Implementation of {@link JaxWsService} interface
 * 
 * @author Esteban Cristóbal
 */
public class JaxWsServiceImpl implements JaxWsService {

    @PersistenceContext
    private EntityManager entityManager;

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.x.project.data.layer.database.insert.service.JaxWsService#insertRow(
     * com.x.project.data.layer.database.insert.service.TableRow)
     */
    @Transactional
    public boolean insertRow(TableRow row) {
        boolean isValid = false;
        final TableRowEntity entity = new TableRowEntity();
        entity.setName(row.getName());
        entity.setValue(row.getValue());
        try {
            this.entityManager.persist(entity);
            isValid = true;
        } catch (Exception e) {
            // Nothing to do
        }
        return isValid;
    }

}
