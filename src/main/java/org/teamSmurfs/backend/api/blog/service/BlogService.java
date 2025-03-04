package org.teamSmurfs.backend.api.blog.service;

import org.teamSmurfs.backend.api.blog.dto.BlogDto;
import org.teamSmurfs.backend.api.blog.dto.BlogRequest;

import java.util.List;

public interface BlogService {
    void createBlog(final BlogRequest blogRequest);
    List<BlogDto> retrieveBlogsForThisUserId(final Long userId);
    List<BlogDto> retrieveBlogsByThisUser(final String authHeader);
    void updateBlog(final Long blogId, final BlogRequest blogRequest);
    void deleteBlog(final Long blogId);
    BlogDto retrieveOne(final Long id);
}
