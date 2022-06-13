package com.contentmunch.blog.engine;

import com.contentmunch.blog.data.Blog;
import com.contentmunch.blog.data.Blogs;

import java.util.List;

public interface BloggerEngine {

    Blogs getBlogs(List<String> fields, Long pageSize, String nextPageToken, String label);

    Blog getBlog(String blogId, List<String> fields);

    Blogs searchBlogs(String query, List<String> fields, String nextPageToken);
}
