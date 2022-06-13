package com.contentmunch.blog.data;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Blog {
    private String id;
    private String title;
    private List<String> images;
    private String published;
    private String content;
}
