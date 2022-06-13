package com.contentmunch.blog.data;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Blogs {
    private List<Blog> blogs;
    private String nextPageToken;
}
