package com.x.project.data.layer.database.insert.service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.jta.JtaTransactionManager;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import com.x.project.data.layer.database.insert.spring.DatabaseInsertTestBeanConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { DatabaseInsertTestBeanConfiguration.class })
public class JaxWsServiceIT {

    @Autowired
    private JaxWsService jaxWsService;

    @Autowired
    private JtaTransactionManager transactionManager;

    @Autowired
    private JpaTransactionManager jpaTransactionManager;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    public void testInsertRowOk() {
        final String name = "name";
        final String value = "value";
        final TableRow row = new TableRow();
        row.setName(name);
        row.setValue(value);
        final TransactionTemplate transactionTemplate = new TransactionTemplate(this.transactionManager);
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {

            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                jaxWsService.insertRow(row);
            }
        });
        final TransactionTemplate jpaTransactionTemplate = new TransactionTemplate(this.jpaTransactionManager);
        jpaTransactionTemplate.execute(new TransactionCallbackWithoutResult() {

            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                final TableRowEntity entity = entityManager.find(TableRowEntity.class, name);
                entityManager.remove(entity);
                Assert.assertNotNull(entity);
                Assert.assertEquals(name, entity.getName());
                Assert.assertEquals(value, entity.getValue());
            }
        });
    }

    @Test
    public void testInsertRowRollback() {
        final TableRow row = new TableRow();
        final String name = "name2";
        final String value = "value2";
        row.setName(name);
        row.setValue(value);
        final TransactionTemplate transactionTemplate = new TransactionTemplate(this.transactionManager);
        try {
            transactionTemplate.execute(new TransactionCallbackWithoutResult() {

                @Override
                protected void doInTransactionWithoutResult(TransactionStatus status) {
                    jaxWsService.insertRow(row);
                    throw new CustomRuntimeException();
                }
            });
        } catch (CustomRuntimeException e) {
            // Nothing to do
        }
        final TransactionTemplate jpaTransactionTemplate = new TransactionTemplate(this.jpaTransactionManager);
        jpaTransactionTemplate.execute(new TransactionCallbackWithoutResult() {

            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                final TableRowEntity entity = entityManager.find(TableRowEntity.class, name);
                Assert.assertNull(entity);
            }
        });
    }

    private class CustomRuntimeException extends RuntimeException {

        private static final long serialVersionUID = -6607507636266903120L;

    }

}
