package com.contentmunch.blog.query;

import com.contentmunch.blog.data.Blog;
import com.contentmunch.blog.data.Blogs;
import com.contentmunch.blog.engine.BloggerEngine;
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
    private final BloggerEngine bloggerEngine;

    @QueryMapping
    public Blogs blogs(@Argument Long pageSize, @Argument String nextPageToken, @Argument String label, DataFetchingEnvironment dfe) {

        DataFetchingFieldSelectionSet selectionSet = dfe.getSelectionSet();

        var fields = selectionSet.getFields().stream()
                .filter(field -> field.getFullyQualifiedName().contains("Blogs.blogs/"))
                .map(SelectedField::getName).toList();
        var blogs = bloggerEngine.getBlogs(fields, pageSize, nextPageToken, label);

        return blogs;
    }

    @QueryMapping
    public Blogs searchBlogs(@Argument String query, @Argument String nextPageToken, DataFetchingEnvironment dfe) {

        DataFetchingFieldSelectionSet selectionSet = dfe.getSelectionSet();

        var fields = selectionSet.getFields().stream()
                .filter(field -> field.getFullyQualifiedName().contains("Blogs.blogs/"))
                .map(SelectedField::getName).toList();
        var blogs = bloggerEngine.searchBlogs(query, fields, nextPageToken);
        return blogs;
    }

    @QueryMapping
    public Blog blog(@Argument String id, DataFetchingEnvironment dfe) {

        DataFetchingFieldSelectionSet selectionSet = dfe.getSelectionSet();
        var blog = bloggerEngine.getBlog(id, selectionSet.getFields().stream()
                .map(SelectedField::getName).collect(Collectors.toList()));
        return blog;
    }
}
