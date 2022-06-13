package com.contentmunch.blog.engine;

import com.contentmunch.blog.configuration.BlogConfig;
import com.contentmunch.blog.data.Blog;
import com.contentmunch.blog.data.Blogs;
import com.contentmunch.blog.exception.BlogException;
import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.http.BasicAuthentication;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.blogger.Blogger;
import com.google.api.services.blogger.model.Post;
import com.google.api.services.blogger.model.PostList;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.google.api.client.googleapis.javanet.GoogleNetHttpTransport.newTrustedTransport;

@Slf4j
public class GoogleBloggerEngine implements BloggerEngine {
    private final BlogConfig blogConfig;
    private final Blogger blogger;
    private static final long DEFAULT_PAGE_SIZE = 15;

    public GoogleBloggerEngine(BlogConfig blogConfig) {
        this.blogConfig = blogConfig;
        this.blogger = buildBlogger(blogConfig.getClientId(), blogConfig.getClientSecret(), blogConfig.getRefreshToken());
    }

    private Blogger buildBlogger(String clientId, String clientSecret, String refreshToken) {
        TokenResponse response = new TokenResponse();
        response.setRefreshToken(refreshToken);
        try {
            return new Blogger.Builder(newTrustedTransport(), JacksonFactory.getDefaultInstance(),
                    new Credential.Builder(BearerToken.authorizationHeaderAccessMethod()).setTransport(
                                    newTrustedTransport())
                            .setJsonFactory(JacksonFactory.getDefaultInstance())
                            .setTokenServerUrl(
                                    new GenericUrl(blogConfig.getTokenServer()))
                            .setClientAuthentication(new BasicAuthentication(
                                    clientId, clientSecret))
                            .build()
                            .setFromTokenResponse(response))
                    .setApplicationName("muncher-blog-api")
                    .build();
        } catch (GeneralSecurityException | IOException e) {
            throw new BlogException("Error creating Blogger Object", e);
        }
    }

    @Override
    public Blogs getBlogs(List<String> fields, Long pageSize, String nextPageToken, String label) {
        try {
            Blogger.Posts.List listAction = blogger.posts().list(blogConfig.getBlogId());
            var actionFields = MessageFormat.format("items({0}),nextPageToken", String.join(",", fields));
            listAction.setFields(actionFields);
            if (label != null)
                listAction.setLabels(label);
            listAction.setFetchImages(fields.contains("images"));
            listAction.setMaxResults(pageSize != null ? pageSize : DEFAULT_PAGE_SIZE);
            if (nextPageToken != null)
                listAction.setPageToken(nextPageToken);

            PostList posts = listAction.execute();
            List<Blog> blogs = new ArrayList<>();
            if (posts.getItems() != null) {
                posts.getItems().forEach(item -> blogs.add(
                        Blog.builder()
                                .id(item.getId())
                                .title(item.getTitle())
                                .content(item.getContent())
                                .published(item.getPublished())
                                .images(item.getImages() != null ? item.getImages().stream().map(Post.Images::getUrl).collect(Collectors.toList()) : null)
                                .build()));
            }

            return Blogs.builder()
                    .blogs(blogs)
                    .nextPageToken(posts.getNextPageToken())
                    .build();
        } catch (IOException e) {
            log.error("Error Retrieving Blogs", e);
            throw new BlogException("Error Retrieving Blogs", e);
        }
    }

    @Override
    public Blog getBlog(String blogId, List<String> fields) {
        try {
            Blogger.Posts.Get getAction = blogger.posts().get(blogConfig.getBlogId(), blogId);
            getAction.setFields(String.join(",", fields));
            getAction.setFetchImages(fields.contains("images"));

            Post post = getAction.execute();

            return Blog.builder()
                    .id(post.getId())
                    .title(post.getTitle())
                    .content(post.getContent())
                    .published(post.getPublished())
                    .images(post.getImages() != null ? post.getImages().stream().map(Post.Images::getUrl).collect(Collectors.toList()) : null)
                    .build();
        } catch (IOException e) {
            log.error("Error Retrieving Blog", e);
            throw new BlogException("Error Retrieving Blog", e);
        }
    }

    @Override
    public Blogs searchBlogs(String query, List<String> fields, String nextPageToken) {
        try {
            Blogger.Posts.Search search = blogger.posts().search(blogConfig.getBlogId(), query);
            var actionFields = MessageFormat.format("items({0}),nextPageToken", String.join(",", fields));
            search.setFields(actionFields);
            search.setFetchBodies(true);
            PostList posts = search.execute();
            List<Blog> blogs = new ArrayList<>();
            if (posts.getItems() != null) {
                posts.getItems().forEach(item -> blogs.add(
                        Blog.builder()
                                .id(item.getId())
                                .title(item.getTitle())
                                .content(item.getContent())
                                .published(item.getPublished())
                                .images(item.getImages() != null ? item.getImages().stream().map(Post.Images::getUrl).collect(Collectors.toList()) : null)
                                .build()));
            }

            return Blogs.builder()
                    .blogs(blogs)
                    .nextPageToken(posts.getNextPageToken())
                    .build();
        } catch (IOException e) {
            log.error("Error Retrieving Blogs", e);
            throw new BlogException("Error Retrieving Blogs", e);
        }
    }
}
