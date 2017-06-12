package com.x.project.data.layer.database.insert.spring;

import javax.jms.ConnectionFactory;
import javax.transaction.TransactionManager;

import org.apache.activemq.ActiveMQXAConnectionFactory;
import org.apache.activemq.jms.pool.JcaPooledConnectionFactory;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.transport.jms.ConnectionFactoryFeature;
import org.apache.cxf.transport.jms.spec.JMSSpecConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.jta.JtaTransactionManager;

import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.icatch.jta.UserTransactionManager;
import com.x.project.data.layer.database.insert.service.JaxWsService;

@Configuration
@EnableTransactionManagement
public class DatabaseInsertTestBeanConfiguration {

    @Bean
    public JaxWsService jaxWsService(final ConnectionFactory connectionFactory) {
        final JaxWsProxyFactoryBean factoryBean = new JaxWsProxyFactoryBean();
        factoryBean.setTransportId(JMSSpecConstants.SOAP_JMS_SPECIFICATION_TRANSPORTID);
        factoryBean.setAddress("jms:queue:test-queue?jndiTransactionManager=atomikosTransactionManager");
        factoryBean.setServiceClass(JaxWsService.class);
        factoryBean.getFeatures().add(new ConnectionFactoryFeature(connectionFactory));
        return (JaxWsService) factoryBean.create();
    }

    @Bean
    public ConnectionFactory connectionFactory(final TransactionManager transactionManager) {
        final ActiveMQXAConnectionFactory xaConnectionFactory = new ActiveMQXAConnectionFactory();
        xaConnectionFactory.setBrokerURL("tcp://localhost:61616");
        final JcaPooledConnectionFactory connectionFactory = new JcaPooledConnectionFactory();
        connectionFactory.setConnectionFactory(xaConnectionFactory);
        return connectionFactory;
    }

    @Bean
    public JtaTransactionManager transactionManager(UserTransactionImp userTransaction,
            UserTransactionManager transactionManager) {
        return new JtaTransactionManager(userTransaction, transactionManager);
    }

    @Bean
    public UserTransactionImp userTransactionService() {
        return new UserTransactionImp();
    }

    @Bean(initMethod = "init", destroyMethod = "close")
    public UserTransactionManager atomikosTransactionManager() throws Exception {
        final UserTransactionManager manager = new UserTransactionManager();
        manager.setForceShutdown(false);
        return manager;
    }

}
