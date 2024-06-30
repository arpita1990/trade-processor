package com.cme.trading.config;
import com.cme.trading.cache.QuoteCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CacheConfig {

    @Bean
    public QuoteCache quoteCache() {
        return new QuoteCache();
    }
}

