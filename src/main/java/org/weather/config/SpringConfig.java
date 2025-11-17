package org.weather.config;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableWebMvc
@ComponentScan("org.weather")
@EnableTransactionManagement
@EnableJpaRepositories("org.weather.repositories")
@PropertySource("classpath:hibernate.properties")
public class SpringConfig implements WebMvcConfigurer {

    private final ApplicationContext applicationContext;
    private final Environment env;

    @Autowired
    public SpringConfig(ApplicationContext applicationContext, Environment env) {
        this.applicationContext = applicationContext;
        this.env = env;
    }

    // ---------- VIEW LAYER ----------
    @Bean
    public SpringResourceTemplateResolver templateResolver() {
        SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
        resolver.setApplicationContext(applicationContext);
        resolver.setPrefix("/WEB-INF/views/");
        resolver.setSuffix(".html");
        resolver.setCharacterEncoding("UTF-8");
        return resolver;
    }

    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine engine = new SpringTemplateEngine();
        engine.setTemplateResolver(templateResolver());
        engine.setEnableSpringELCompiler(true);
        return engine;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**")
                .addResourceLocations("/resources/");
    }

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        ThymeleafViewResolver r = new ThymeleafViewResolver();
        r.setTemplateEngine(templateEngine());
        r.setCharacterEncoding("UTF-8");
        registry.viewResolver(r);
    }
    // ---------- DATA SOURCE ----------

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName(env.getRequiredProperty("hibernate.driver_class"));
        ds.setUrl(env.getRequiredProperty("hibernate.connection.url"));
        ds.setUsername(env.getRequiredProperty("hibernate.connection.username"));
        ds.setPassword(env.getRequiredProperty("hibernate.connection.password"));
        return ds;
    }
//    @Bean
//    public DataSource dataSource() {
//        HikariDataSource ds = new HikariDataSource();
//        ds.setDriverClassName(env.getRequiredProperty("hibernate.driver_class"));
//        ds.setJdbcUrl(env.getRequiredProperty("hibernate.connection.url"));
//        ds.setUsername(env.getRequiredProperty("hibernate.connection.username"));
//        ds.setPassword(env.getRequiredProperty("hibernate.connection.password"));
//

//// Hikari-specific
//    ds.setMaximumPoolSize(Integer.parseInt(env.getRequiredProperty("hikari.maximumPoolSize")));
//    ds.setMinimumIdle(Integer.parseInt(env.getRequiredProperty("hikari.minimumIdle")));
//    ds.setIdleTimeout(Long.parseLong(env.getRequiredProperty("hikari.idleTimeout")));
//    ds.setConnectionTimeout(Long.parseLong(env.getRequiredProperty("hikari.connectionTimeout")));
//    ds.setMaxLifetime(Long.parseLong(env.getRequiredProperty("hikari.maxLifetime")));
//
//        return ds;
//    }

    // ---------- HIBERNATE PROPERTIES ----------
    private Properties hibernateProperties() {
        Properties props = new Properties();
        props.put("hibernate.dialect", env.getRequiredProperty("hibernate.dialect"));
        props.put("hibernate.show_sql", env.getRequiredProperty("hibernate.show_sql"));
        props.put("hibernate.format_sql", env.getRequiredProperty("hibernate.format_sql"));
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
    // ---------- FLYWAY MIGRATIONS ----------

    @Bean(initMethod = "migrate")
    public Flyway flyway(DataSource dataSource) {
        return Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:db/migration")
                .load();
    }
}