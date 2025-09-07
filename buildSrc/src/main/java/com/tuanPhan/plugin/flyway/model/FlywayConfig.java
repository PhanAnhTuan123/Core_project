package com.tuanPhan.plugin.flyway.model;

import java.util.Properties;

public record FlywayConfig(String url, String user, String password, String dbName) {

    public static FlywayConfig from(Properties props) {
        return new FlywayConfig(
                props.getProperty("flyway.url"),
                props.getProperty("flyway.user"),
                props.getProperty("flyway.password"),
                props.getProperty("flyway.database")
        );
    }
}
