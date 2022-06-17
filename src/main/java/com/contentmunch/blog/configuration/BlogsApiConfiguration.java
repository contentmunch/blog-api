package com.contentmunch.blog.configuration;

import com.contentmunch.blog.engine.BloggerEngine;
import com.contentmunch.blog.factory.BloggerFactory;
import com.contentmunch.blog.query.BlogsQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@ComponentScan(basePackageClasses = {BlogsQuery.class, BlogConfig.class})
@Configuration
public class BlogsApiConfiguration {

    @Autowired
    private BlogConfig blogConfig;

    @Bean
    BloggerEngine bloggerEngine() {
        return BloggerFactory.getDefaultBlogEngine(blogConfig);
    }
}
