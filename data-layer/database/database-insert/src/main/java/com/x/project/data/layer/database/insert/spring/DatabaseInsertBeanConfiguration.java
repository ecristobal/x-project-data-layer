package com.x.project.data.layer.database.insert.spring;

import javax.jms.ConnectionFactory;
import javax.transaction.TransactionManager;
import javax.xml.ws.Endpoint;

import org.apache.activemq.ActiveMQXAConnectionFactory;
import org.apache.activemq.jms.pool.XaPooledConnectionFactory;
import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.transport.jms.JMSConfigFeature;
import org.apache.cxf.transport.jms.JMSConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.atomikos.icatch.jta.J2eeTransactionManager;
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

    @Bean
    public Endpoint endpoint(@Autowired final Bus bus, @Autowired final ConnectionFactory connectionFactory,
            @Value("${jms.queue.name}") final String queueName, @Autowired final JaxWsService jaxWsService) {
        final EndpointImpl endpoint = new EndpointImpl(bus, jaxWsService);
        endpoint.publish("/Insert");
        final JMSConfiguration jmsConfiguration = new JMSConfiguration();
        jmsConfiguration.setConnectionFactory(connectionFactory);
        jmsConfiguration.setTargetDestination(queueName);
        jmsConfiguration.setReceiveTimeout(10000L);
        jmsConfiguration.setSessionTransacted(true);
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
    public ConnectionFactory connectionFactory(@Autowired final TransactionManager transactionManager,
            @Value("${jms.connection.factory.url}") final String queueManagerUrl) {
        final XaPooledConnectionFactory connectionFactory = new XaPooledConnectionFactory();
        connectionFactory.setConnectionFactory(new ActiveMQXAConnectionFactory(queueManagerUrl));
        connectionFactory.setTransactionManager(transactionManager);
        return connectionFactory;
    }

    @Bean
    public TransactionManager transactionManager() {
        return new J2eeTransactionManager();
    }

}
