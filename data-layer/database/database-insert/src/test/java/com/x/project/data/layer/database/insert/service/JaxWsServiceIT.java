package com.x.project.data.layer.database.insert.service;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.x.project.data.layer.database.insert.spring.DatabaseInsertTestBeanConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { DatabaseInsertTestBeanConfiguration.class })
@Transactional
public class JaxWsServiceIT {

    @Autowired
    private JaxWsService jaxWsService;

    @Test
    public void testInsertRow() {
        final TableRow row = new TableRow();
        row.setName("name");
        row.setValue("value");
        this.jaxWsService.insertRow(row);
    }

}
