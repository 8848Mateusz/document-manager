package individual.p_n_2.AppConfig;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.*;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "individual.p_n_2.Repository.Symfonia",
        entityManagerFactoryRef = "symfoniaEntityManagerFactory",
        transactionManagerRef = "symfoniaTransactionManager"
)
public class SymfoniaDbConfig {

    @Bean
    @ConfigurationProperties("spring.symfonia.datasource")
    public DataSourceProperties symfoniaDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "symfoniaDataSource")
    public DataSource symfoniaDataSource(
            @Qualifier("symfoniaDataSourceProperties") DataSourceProperties props) {
        return props
                .initializeDataSourceBuilder()
                .type(com.zaxxer.hikari.HikariDataSource.class)
                .build();
    }

    @Bean(name = "symfoniaEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean symfoniaEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("symfoniaDataSource") DataSource ds) {
        // hbm2ddl.auto wyłączone, bo nie wolno modyfikować produkcyjnej bazy Symfonii
        return builder
                .dataSource(ds)
                .packages("individual.p_n_2.Domain.Symfonia")
                .persistenceUnit("symfonia")
                .build();
    }

    @Bean(name = "symfoniaTransactionManager")
    public PlatformTransactionManager symfoniaTransactionManager(
            @Qualifier("symfoniaEntityManagerFactory") EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }
}