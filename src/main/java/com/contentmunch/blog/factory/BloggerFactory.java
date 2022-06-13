package com.contentmunch.blog.factory;

import com.contentmunch.blog.configuration.BlogConfig;
import com.contentmunch.blog.engine.BloggerEngine;
import com.contentmunch.blog.engine.GoogleBloggerEngine;

public class BloggerFactory {
    public static BloggerEngine getDefaultBlogEngine(BlogConfig blogConfig) {
        return new GoogleBloggerEngine(blogConfig);
    }
}
