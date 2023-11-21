package com.merseongsanghoe.sooljarisearchengine.config;

import org.neo4j.cypherdsl.core.renderer.Configuration;
import org.neo4j.cypherdsl.core.renderer.Dialect;
import org.springframework.context.annotation.Bean;

public class Neo4jConfig {
    /**
     * 실행중인 Neo4j 버전에 맞추어 Cyper-DSL dialect 버전을 정의
     * 정확한 버전을 명시하면 더욱 최적화된 쿼리가 동작됨
     */
    @Bean
    Configuration cypherDslConfiguration() {
        return Configuration.newConfig()
                .withDialect(Dialect.NEO4J_5).build();
    }
}
