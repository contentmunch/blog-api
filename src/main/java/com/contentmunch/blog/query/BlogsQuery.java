package com.contentmunch.blog.query;

import com.contentmunch.blog.configuration.BlogConfig;
import com.contentmunch.blog.data.Blog;
import com.contentmunch.blog.data.Blogs;
import com.contentmunch.blog.factory.BloggerFactory;
import graphql.schema.DataFetchingEnvironment;
import graphql.schema.DataFetchingFieldSelectionSet;
import graphql.schema.SelectedField;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class BlogsQuery {
    private final BlogConfig blogConfig;

    @QueryMapping
    public Blogs blogs(@Argument Long pageSize, @Argument String nextPageToken, @Argument String label, DataFetchingEnvironment dfe) {
        var engine = BloggerFactory.getDefaultBlogEngine(blogConfig);
        DataFetchingFieldSelectionSet selectionSet = dfe.getSelectionSet();

        var fields = selectionSet.getFields().stream()
                .filter(field -> field.getFullyQualifiedName().contains("Blogs.blogs/"))
                .map(SelectedField::getName).toList();

        return engine.getBlogs(fields, pageSize, nextPageToken, label);
    }

    @QueryMapping
    public Blogs searchBlogs(@Argument String query, @Argument String nextPageToken, DataFetchingEnvironment dfe) {
        var engine = BloggerFactory.getDefaultBlogEngine(blogConfig);
        DataFetchingFieldSelectionSet selectionSet = dfe.getSelectionSet();

        var fields = selectionSet.getFields().stream()
                .filter(field -> field.getFullyQualifiedName().contains("Blogs.blogs/"))
                .map(SelectedField::getName).toList();

        return engine.searchBlogs(query, fields, nextPageToken);
    }

    @QueryMapping
    public Blog blog(@Argument String blogId, DataFetchingEnvironment dfe) {
        var engine = BloggerFactory.getDefaultBlogEngine(blogConfig);
        DataFetchingFieldSelectionSet selectionSet = dfe.getSelectionSet();

        return engine.getBlog(blogId, selectionSet.getFields().stream()
                .map(SelectedField::getName).collect(Collectors.toList()));
    }
}
