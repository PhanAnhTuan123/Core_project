package com.tuanPhan.config;

public interface ConfigKey<E extends ConfigKey<E>> {
    String getName();

    default Class<? extends Config> getType() {
        return null;
    }
}
