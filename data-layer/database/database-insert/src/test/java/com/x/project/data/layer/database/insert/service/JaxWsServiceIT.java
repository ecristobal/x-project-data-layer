package com.x.project.data.layer.database.insert.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import com.x.project.data.layer.database.insert.spring.DatabaseInsertTestBeanConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { DatabaseInsertTestBeanConfiguration.class })
public class JaxWsServiceIT {

    @Autowired
    private JaxWsService jaxWsService;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Test
    public void testInsertRowOk() {
        final TableRow row = new TableRow();
        row.setName("name");
        row.setValue("value");
        final TransactionTemplate transactionTemplate = new TransactionTemplate(this.transactionManager);
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {

            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                jaxWsService.insertRow(row);
            }
        });
    }

    @Test
    public void testInsertRowRollback() {
        final TableRow row = new TableRow();
        row.setName("name2");
        row.setValue("value2");
        try {
            final TransactionTemplate transactionTemplate = new TransactionTemplate(this.transactionManager);
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
    }

    private class CustomRuntimeException extends RuntimeException {

        private static final long serialVersionUID = -6607507636266903120L;

    }

}
