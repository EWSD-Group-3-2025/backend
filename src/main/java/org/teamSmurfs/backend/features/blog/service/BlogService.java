package org.teamSmurfs.backend.features.blog.service;

import org.teamSmurfs.backend.features.blog.dto.BlogDto;
import org.teamSmurfs.backend.features.blog.dto.BlogRequest;

import java.util.List;

public interface BlogService {
    void createBlog(final String authHeader, final BlogRequest blogRequest);
    List<BlogDto> retrieveBlogsForThisUser(final String authHeader);
    List<BlogDto> retrieveBlogsByThisUser(final String authHeader);
    void updateBlog(final String authHeader, final Long blogId, final BlogRequest blogRequest);
    void deleteBlog(final Long blogId);
    BlogDto retrieveOne(final Long id);
}
