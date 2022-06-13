package com.contentmunch.blog.configuration;

import com.contentmunch.blog.query.BlogsQuery;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@ComponentScan(basePackageClasses = {BlogsQuery.class, BlogConfig.class})
@Configuration
public class BlogsApiConfiguration {
}
