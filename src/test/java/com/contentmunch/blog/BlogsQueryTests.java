/*
 * Copyright 2020-2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.contentmunch.blog;

import com.contentmunch.blog.data.Blog;
import com.contentmunch.blog.data.Blogs;
import com.contentmunch.blog.engine.BloggerEngine;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureGraphQlTester;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.graphql.test.tester.GraphQlTester;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@SpringBootTest
@AutoConfigureGraphQlTester
class BlogsQueryTests {

    @Autowired
    private GraphQlTester graphQlTester;
    private static Blog BLOG1 = Blog.builder()
            .id("1")
            .title("Employee Spotlight")
            .content("Content")
            .published("2021-12-15T10:24:00-08:00")
            .labels(List.of("Featured"))
            .images(List.of("ImageUrl1"))
            .build();
    private static Blog BLOG2 = Blog.builder()
            .id("2")
            .title("Employee Department")
            .content("Content 2")
            .published("2021-12-16T10:24:00-08:00")
            .labels(List.of("Featured", "Employee"))
            .images(List.of("ImageUrl2", "ImageUrl3"))
            .build();
    @MockBean
    private BloggerEngine bloggerEngine;


    @Test
    void getsBlogForId() {
        given(bloggerEngine.getBlog(eq("1"), anyList()))
                .willReturn(BLOG1);
        this.graphQlTester.documentName("blog")
                .variable("id", "1")
                .execute()
                .path("blog.title")
                .entity(String.class).isEqualTo("Employee Spotlight")
                .path("blog.labels")
                .entityList(String.class).containsExactly("Featured");


    }

    @Test
    void getsTitleForBlog() {
        given(bloggerEngine.getBlog(eq("1"), anyList()))
                .willReturn(BLOG1);

        this.graphQlTester.documentName("blogWithOnlyTitle")
                .variable("id", "1")
                .execute()
                .path("blog.title")
                .entity(String.class).isEqualTo("Employee Spotlight")
                .path("blog.id").pathDoesNotExist();
    }


}