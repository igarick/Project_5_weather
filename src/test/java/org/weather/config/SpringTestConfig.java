package org.weather.config;

import jakarta.persistence.EntityManagerFactory;
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

//@Profile("test")
//@Configuration
//@ComponentScan({
//        "org.weather.service",
//        "org.weather.repository",
////        "org.weather.model"
//})
//@EnableTransactionManagement
//@EnableJpaRepositories("org.weather.repository")
//@PropertySource("classpath:hibernate-test.properties")

@Configuration
@Profile("test")
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "org.weather.repository")
@ComponentScan(basePackages = {
        "org.weather.service",
        "org.weather.repository",
        "org.weather.utils",

})
@PropertySource("classpath:hibernate-test.properties")
public class SpringTestConfig {

    private final Environment env;

    @Autowired
    public SpringTestConfig(Environment env) {
        this.env = env;
    }

    // ---------- TEST DATASOURCE (H2) ----------
//    @Bean
//    public DataSource dataSource() {
//        DriverManagerDataSource ds = new DriverManagerDataSource();
//        ds.setDriverClassName(env.getProperty("hibernate.driver_class"));
//        ds.setUrl(env.getProperty("hibernate.connection.url"));
//        ds.setUsername(env.getProperty("hibernate.connection.username"));
//        ds.setPassword(env.getProperty("hibernate.connection.password"));
//        return ds;
//    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName(env.getRequiredProperty("hibernate.driver_class"));
//        String url = env.getRequiredProperty("hibernate.connection.url");
//        String username = env.getProperty("hibernate.connection.username");
//        String password = env.getProperty("hibernate.connection.password");
//
//        System.out.println("=== TEST DATASOURCE CONFIG ===");
//        System.out.println("URL: " + url);
//        System.out.println("USERNAME: " + username);
//        System.out.println("PASSWORD: '" + password + "'");
//        System.out.println("==============================");
        System.out.println("=== TEST DATASOURCE CONFIG ===");
        System.out.println("DS URL: " + env.getRequiredProperty("hibernate.connection.url"));
        System.out.println("DS USER: " + env.getRequiredProperty("hibernate.connection.username"));
        System.out.println("DS PASS: '" + env.getProperty("hibernate.connection.password", "") + "'");
        System.out.println("==============================");

        ds.setUrl(env.getRequiredProperty("hibernate.connection.url"));
        ds.setUsername(env.getRequiredProperty("hibernate.connection.username"));
        ds.setPassword(env.getProperty("hibernate.connection.password", ""));
        return ds;
    }

    // ---------- HIBERNATE PROPERTIES ----------
    private Properties hibernateProps() {
        Properties props = new Properties();
        props.put("hibernate.dialect", env.getRequiredProperty("hibernate.dialect"));
        props.put("hibernate.hbm2ddl.auto", "create-drop"); // ВАЖНО! Для тестов
        props.put("hibernate.show_sql", env.getRequiredProperty("hibernate.show_sql"));
        props.put("hibernate.format_sql", env.getRequiredProperty("hibernate.format_sql"));
        return props;
    }

//    private Properties hibernateProperties() {
//        Properties props = new Properties();
//        props.put("hibernate.dialect", env.getProperty("hibernate.dialect"));
//        props.put("hibernate.hbm2ddl.auto", "create-drop"); // пересоздание схемы
//        props.put("hibernate.show_sql", "false");
//        return props;
//    }

    // ---------- ENTITY MANAGER ----------
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource());
        em.setPackagesToScan("org.weather.model");
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        em.setJpaProperties(hibernateProps());
        return em;
    }

//    @Bean
//    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
//        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
//        em.setDataSource(dataSource());
//        em.setPackagesToScan("org.weather.model");
//
//        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
//        em.setJpaVendorAdapter(vendorAdapter);
//
//        em.setJpaProperties(hibernateProperties());
//        return em;
//    }

    // ---------- TRANSACTION MANAGER ----------
    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }

//    @Bean
//    public PlatformTransactionManager transactionManager(LocalContainerEntityManagerFactoryBean emf) {
//        JpaTransactionManager tx = new JpaTransactionManager();
//        tx.setEntityManagerFactory(emf.getObject());
//        return tx;
//    }

    // ---------- DISABLE FLYWAY FOR TESTS ----------
//    @Bean
//    @Primary
//    public Flyway flywayDisabled() {
//        return null; // Flyway не применяется в тестовой среде
//    }



    // ---------- DISABLE FLYWAY ----------
//    @Bean
//    @Primary
//    public Object disableFlyway() {
//        return new Object();
//    }
}
