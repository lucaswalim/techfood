package br.com.fiap.techfood.infrastructure.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan(basePackages = "br.com.fiap.techfood.infrastructure.persistence.entity")
@EnableJpaRepositories(basePackages = "br.com.fiap.techfood.infrastructure.persistence.repository")
public class PersistenceConfig {
}
