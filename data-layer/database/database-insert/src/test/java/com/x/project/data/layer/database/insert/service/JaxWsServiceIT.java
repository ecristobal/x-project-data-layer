package com.x.project.data.layer.database.insert.service;

import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.x.project.data.layer.database.insert.spring.DatabaseInsertTestBeanConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { DatabaseInsertTestBeanConfiguration.class })
@Transactional
public class JaxWsServiceIT {

    @Autowired
    private JaxWsService jaxWsService;

    @Test
    public void testInsertRowOk() {
        final String name = UUID.randomUUID().toString();
        final String value = UUID.randomUUID().toString();
        final TableRow row = new TableRow();
        row.setName(name);
        row.setValue(value);
        jaxWsService.insertRow(row);
    }

    @Test(expected = CustomRuntimeException.class)
    public void testInsertRowRollback() {
        final TableRow row = new TableRow();
        final String name = UUID.randomUUID().toString();
        final String value = UUID.randomUUID().toString();
        row.setName(name);
        row.setValue(value);
        jaxWsService.insertRow(row);
        throw new CustomRuntimeException();
    }

    private class CustomRuntimeException extends RuntimeException {

        private static final long serialVersionUID = -849912750341888090L;

    }

}
