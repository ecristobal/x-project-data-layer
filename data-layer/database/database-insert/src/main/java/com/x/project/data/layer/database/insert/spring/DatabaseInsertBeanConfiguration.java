package com.x.project.data.layer.database.insert.spring;

import javax.jms.XAConnectionFactory;
import javax.transaction.TransactionManager;
import javax.xml.ws.Endpoint;

import org.apache.activemq.ActiveMQXAConnectionFactory;
import org.apache.activemq.jms.pool.JcaPooledConnectionFactory;
import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.transport.jms.JMSConfigFeature;
import org.apache.cxf.transport.jms.JMSConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.x.project.data.layer.database.insert.service.JaxWsService;
import com.x.project.data.layer.database.insert.service.JaxWsServiceImpl;

/**
 * Database insert bean configuration file.
 * 
 * @author Esteban Cristóbal
 */
@Configuration
@EnableTransactionManagement
@EntityScan({ "com.x.project.data.layer.database.insert.service" })
public class DatabaseInsertBeanConfiguration {

    @Bean(initMethod = "publish", destroyMethod = "stop")
    public Endpoint endpoint(final Bus bus, final JcaPooledConnectionFactory connectionFactory,
            @Value("${jms.queue.name}") final String queueName, final JaxWsService jaxWsService,
            final TransactionManager transactionManager) {
        final EndpointImpl endpoint = new EndpointImpl(bus, jaxWsService);
        final JMSConfiguration jmsConfiguration = new JMSConfiguration();
        connectionFactory.setTransactionManager(transactionManager);
        jmsConfiguration.setConnectionFactory(connectionFactory);
        jmsConfiguration.setTargetDestination(queueName);
        jmsConfiguration.setReceiveTimeout(10000L);
        jmsConfiguration.setSessionTransacted(true);
        jmsConfiguration.setTransactionManager(transactionManager);
        final JMSConfigFeature jmsConfigFeature = new JMSConfigFeature();
        jmsConfigFeature.setJmsConfig(jmsConfiguration);
        endpoint.getFeatures().add(jmsConfigFeature);
        endpoint.setAddress("jms://");
        return endpoint;
    }

    @Bean
    public JaxWsService jaxWsService() {
        return new JaxWsServiceImpl();
    }

    @Bean
    public JcaPooledConnectionFactory connectionFactory(
            @Value("${jms.connection.factory.url}") final String queueManagerUrl) {
        final XAConnectionFactory xaConnectionFactory = new ActiveMQXAConnectionFactory(queueManagerUrl);
        final JcaPooledConnectionFactory connectionFactory = new JcaPooledConnectionFactory();
        connectionFactory.setConnectionFactory(xaConnectionFactory);
        return connectionFactory;
    }

}
