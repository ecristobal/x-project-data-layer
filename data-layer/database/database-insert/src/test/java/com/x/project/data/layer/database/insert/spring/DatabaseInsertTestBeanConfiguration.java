package com.x.project.data.layer.database.insert.spring;

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQXAConnectionFactory;
import org.apache.cxf.binding.soap.SoapBindingConfiguration;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.transport.jms.JMSConfigFeature;
import org.apache.cxf.transport.jms.JMSConfiguration;
import org.apache.cxf.transport.jms.spec.JMSSpecConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.jta.JtaTransactionManager;

import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.icatch.jta.UserTransactionManager;
import com.x.project.data.layer.database.insert.service.JaxWsService;

@Configuration
public class DatabaseInsertTestBeanConfiguration {

    @Bean
    public JaxWsService jaxWsService(final JtaTransactionManager jtaTransactionManager) {
        final JaxWsProxyFactoryBean factoryBean = new JaxWsProxyFactoryBean();
        factoryBean.setTransportId(JMSSpecConstants.SOAP_JMS_SPECIFICATION_TRANSPORTID);
        factoryBean.setAddress("jms://");
        factoryBean.setServiceClass(JaxWsService.class);
        final JMSConfiguration jmsConfiguration = new JMSConfiguration();
        final ConnectionFactory connectionFactory = new ActiveMQXAConnectionFactory("tcp://localhost:61616");
        jmsConfiguration.setConnectionFactory(connectionFactory);
        jmsConfiguration.setTargetDestination("test-queue");
        jmsConfiguration.setReceiveTimeout(10000L);
        jmsConfiguration.setTransactionManager(jtaTransactionManager.getTransactionManager());
        final JMSConfigFeature jmsConfigFeature = new JMSConfigFeature();
        jmsConfigFeature.setJmsConfig(jmsConfiguration);
        factoryBean.getFeatures().add(jmsConfigFeature);
        SoapBindingConfiguration sbc = new SoapBindingConfiguration();
        factoryBean.setBindingConfig(sbc);
        return (JaxWsService) factoryBean.create();
    }

    @Bean
    public JtaTransactionManager transactionManager() {
        final JtaTransactionManager jtaTransactionManager = new JtaTransactionManager();
        jtaTransactionManager.setUserTransaction(new UserTransactionImp());
        jtaTransactionManager.setTransactionManager(new UserTransactionManager());
        return jtaTransactionManager;
    }

}
