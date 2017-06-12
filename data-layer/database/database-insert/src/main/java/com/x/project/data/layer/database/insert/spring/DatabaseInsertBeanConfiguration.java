package com.x.project.data.layer.database.insert.spring;

import javax.transaction.TransactionManager;
import javax.xml.ws.Endpoint;

import org.apache.activemq.ActiveMQXAConnectionFactory;
import org.apache.activemq.jms.pool.JcaPooledConnectionFactory;
import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.transport.jms.ConnectionFactoryFeature;
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
    public EndpointImpl endpoint(final Bus bus, final JcaPooledConnectionFactory connectionFactory,
            @Value("${jms.queue.name}") final String queueName, final JaxWsService jaxWsService,
            final TransactionManager transactionManager) {
        connectionFactory.setTransactionManager(transactionManager);
        final EndpointImpl endpointImpl = (EndpointImpl) Endpoint.create(jaxWsService);
        endpointImpl.getFeatures().add(new ConnectionFactoryFeature(connectionFactory));
        endpointImpl.setAddress("jms:queue:test-queue?jndiTransactionManager=atomikosTransactionManager");
        return endpointImpl;
    }

    @Bean
    public JaxWsService jaxWsService() {
        return new JaxWsServiceImpl();
    }

    @Bean
    public JcaPooledConnectionFactory connectionFactory(
            @Value("${jms.connection.factory.url}") final String queueManagerUrl) {
        final ActiveMQXAConnectionFactory xaConnectionFactory = new ActiveMQXAConnectionFactory();
        xaConnectionFactory.setBrokerURL(queueManagerUrl);
        final JcaPooledConnectionFactory connectionFactory = new JcaPooledConnectionFactory();
        connectionFactory.setConnectionFactory(xaConnectionFactory);
        return connectionFactory;
    }

}
