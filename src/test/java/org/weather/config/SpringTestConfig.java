package org.weather.config;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@ComponentScan("org.weather")
@EnableTransactionManagement
@EnableJpaRepositories("org.weather.repository")
@PropertySource("classpath:hibernate-test.properties")
public class SpringTestConfig {

    private final ApplicationContext applicationContext;
    private final Environment env;

    @Autowired
    public SpringTestConfig(ApplicationContext applicationContext, Environment env) {
        this.applicationContext = applicationContext;
        this.env = env;
    }

    // ---------- TEST DATASOURCE (H2) ----------
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName(env.getProperty("hibernate.driver_class"));
        ds.setUrl(env.getProperty("hibernate.connection.url"));
        ds.setUsername(env.getProperty("hibernate.connection.username"));
        ds.setPassword(env.getProperty("hibernate.connection.password"));
        return ds;
    }

    // ---------- HIBERNATE PROPERTIES ----------
    private Properties hibernateProperties() {
        Properties props = new Properties();
        props.put("hibernate.dialect", env.getProperty("hibernate.dialect"));
        props.put("hibernate.hbm2ddl.auto", "create-drop"); // пересоздание схемы
        props.put("hibernate.show_sql", "false");
        return props;
    }

    // ---------- ENTITY MANAGER ----------
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource());
        em.setPackagesToScan("org.weather.model");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);

        em.setJpaProperties(hibernateProperties());
        return em;
    }

    // ---------- TRANSACTION MANAGER ----------
    @Bean
    public PlatformTransactionManager transactionManager(LocalContainerEntityManagerFactoryBean emf) {
        JpaTransactionManager tx = new JpaTransactionManager();
        tx.setEntityManagerFactory(emf.getObject());
        return tx;
    }

    // ---------- DISABLE FLYWAY FOR TESTS ----------
    @Bean
    @Primary
    public Flyway flywayDisabled() {
        return null; // Flyway не применяется в тестовой среде
    }
}
