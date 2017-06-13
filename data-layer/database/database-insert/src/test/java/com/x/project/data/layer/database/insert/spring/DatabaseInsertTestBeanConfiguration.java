package com.x.project.data.layer.database.insert.spring;

import java.util.Properties;

import javax.jms.ConnectionFactory;
import javax.persistence.EntityManagerFactory;
import javax.transaction.TransactionManager;

import org.apache.activemq.ActiveMQXAConnectionFactory;
import org.apache.activemq.jms.pool.JcaPooledConnectionFactory;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.transport.jms.ConnectionFactoryFeature;
import org.apache.cxf.transport.jms.spec.JMSSpecConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.jta.JtaTransactionManager;

import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.icatch.jta.UserTransactionManager;
import com.mysql.cj.jdbc.MysqlDataSource;
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
        connectionFactory.setTransactionManager(transactionManager);
        return connectionFactory;
    }

    @Bean
    public JtaTransactionManager transactionManager(UserTransactionImp userTransaction,
            UserTransactionManager transactionManager) {
        return new JtaTransactionManager(userTransaction, transactionManager);
    }

    @Bean
    public JpaTransactionManager jpaTransactionManager(final EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
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

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        final LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        final MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setURL("jdbc:mysql://sonarqube-mysql.cbpq2cxhkzxc.eu-central-1.rds.amazonaws.com:3306/test");
        dataSource.setUser("tester");
        dataSource.setPassword("tester");
        factoryBean.setDataSource(dataSource);
        final Properties properties = new Properties();
        properties.put("hibernate.ddl-auto", "none");
        properties.put("hibernate.current_session_context_class", "jta");
        properties.put("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
        properties.put("hibernate.transaction.jpa.platform", "com.atomikos.icatch.jta.hibernate4.AtomikosJ2eePlatform");
        factoryBean.setJpaProperties(properties);
        factoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        factoryBean.setJpaDialect(new HibernateJpaDialect());
        factoryBean.setPackagesToScan("com.x.project.data.layer.database.insert.service");
        return factoryBean;
    }

}
