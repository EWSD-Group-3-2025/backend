package org.teamSmurfs.backend.api.blog.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.teamSmurfs.backend.api.blog.dto.BlogDto;
import org.teamSmurfs.backend.api.blog.dto.BlogRecord;
import org.teamSmurfs.backend.api.blog.dto.BlogRequest;
import org.teamSmurfs.backend.api.blog.model.Blog;
import org.teamSmurfs.backend.api.blog.repository.BlogJdbcRepository;
import org.teamSmurfs.backend.api.blog.repository.wrapper.BlogJpaRepositoryWrapper;
import org.teamSmurfs.backend.api.blog.service.BlogService;
import org.teamSmurfs.backend.api.user.dto.UserDto;
import org.teamSmurfs.backend.api.user.model.User;
import org.teamSmurfs.backend.api.user.repository.UserRepository;
import org.teamSmurfs.backend.api.user.utils.UserUtil;
import org.teamSmurfs.backend.config.utils.EntityUtil;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BlogServiceImpl implements BlogService {

    private final BlogJpaRepositoryWrapper jpaRepository;
    private final BlogJdbcRepository jdbcRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final UserUtil userUtil;

    @Override
    @Transactional
    public void createBlog(final BlogRequest blogRequest) {
        User author = EntityUtil.getEntityById(this.userRepository, blogRequest.getAuthorId());
        Blog newBlog = new Blog(author, blogRequest.getContent(), blogRequest.getTitle());
        this.jpaRepository.save(newBlog);
    }

    @Override
    public List<BlogDto> retrieveBlogsForThisUserId(final Long userId) {
        return this.jdbcRepository.findBlogsForThisUser(userId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BlogDto> retrieveBlogsByThisUser(String authHeader) {

        UserDto userDto = this.userUtil.getCurrentUserDto(authHeader);

        User user = EntityUtil.getEntityById(this.userRepository, userDto.getId());

        return this.jdbcRepository.findByAuthorId(user.getId()).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void updateBlog(final Long blogId, final BlogRequest blogRequest) {
        Blog existingBlog = this.jpaRepository.findOneWithNotFoundDetection(blogId);

        User author = EntityUtil.getEntityById(this.userRepository, blogRequest.getAuthorId());

        boolean isUpdated = false;

        if (!blogRequest.getAuthorId().equals(existingBlog.getAuthor().getId())) {
            existingBlog.setAuthor(author);
            isUpdated = true;
        }

        if (!blogRequest.getContent().equals(existingBlog.getContent())) {
            existingBlog.setContent(blogRequest.getContent());
            isUpdated = true;
        }

        if (!blogRequest.getTitle().equals(existingBlog.getTitle())) {
            existingBlog.setTitle(blogRequest.getTitle());
            isUpdated = true;
        }

        if (isUpdated)
            this.jpaRepository.save(existingBlog);
    }

    @Override
    @Transactional
    public void deleteBlog(final Long blogId) {
        this.jpaRepository.deleteById(blogId);
    }

    private BlogDto convertToDto(BlogRecord blogRecord) {
        return this.modelMapper.map(blogRecord, BlogDto.class);
    }
}
