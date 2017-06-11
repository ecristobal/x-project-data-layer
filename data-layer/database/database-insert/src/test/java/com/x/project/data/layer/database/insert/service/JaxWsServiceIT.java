package com.x.project.data.layer.database.insert.service;

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
        final TableRow row = new TableRow();
        row.setName("name");
        row.setValue("value");
        this.jaxWsService.insertRow(row);
    }

    @Test(expected = RuntimeException.class)
    public void testInsertRowRollback() {
        final TableRow row = new TableRow();
        row.setName("name2");
        row.setValue("value2");
        this.jaxWsService.insertRow(row);
        throw new RuntimeException();
    }

}
