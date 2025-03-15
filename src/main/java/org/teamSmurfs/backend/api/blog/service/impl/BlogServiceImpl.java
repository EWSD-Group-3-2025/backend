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
import org.teamSmurfs.backend.api.comment.dto.CommentDto;
import org.teamSmurfs.backend.api.comment.dto.CommentRecord;
import org.teamSmurfs.backend.api.comment.repository.CommentJdbcRepository;
import org.teamSmurfs.backend.api.react.dto.ReactDto;
import org.teamSmurfs.backend.api.react.dto.ReactRecord;
import org.teamSmurfs.backend.api.react.model.ReactEntityType;
import org.teamSmurfs.backend.api.react.repository.ReactJdbcRepository;
import org.teamSmurfs.backend.api.user.dto.UserDto;
import org.teamSmurfs.backend.api.user.model.User;
import org.teamSmurfs.backend.api.user.utils.UserUtil;
import org.teamSmurfs.backend.config.exception.EntityNotFoundException;
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
    private final ReactJdbcRepository reactJdbcRepository;
    private final CommentJdbcRepository commentJdbcRepository;
    private final ModelMapper modelMapper;
    private final UserUtil userUtil;

    @Override
    @Transactional
    public void createBlog(final String authHeader, final BlogRequest blogRequest) {
        final UserDto userDto = this.userUtil.getCurrentUserDto(authHeader);

        User author = EntityUtil.getEntityById(this.userRepository, userDto.getId());
        Blog newBlog = new Blog(author, blogRequest.getContent(), blogRequest.getTitle());
        this.jpaRepository.save(newBlog);
    }

    @Override
    public List<BlogDto> retrieveBlogsForThisUser(final String authHeader) {
        UserDto userDto = this.userUtil.getCurrentUserDto(authHeader);

        User user = EntityUtil.getEntityById(this.userRepository, userDto.getId());

        return this.jdbcRepository.findBlogsForThisUser(user.getId()).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BlogDto> retrieveBlogsByThisUser(final String authHeader) {

        UserDto userDto = this.userUtil.getCurrentUserDto(authHeader);

        User user = EntityUtil.getEntityById(this.userRepository, userDto.getId());

        return this.jdbcRepository.findByAuthorId(user.getId()).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void updateBlog(final String authHeader, final Long blogId, final BlogRequest blogRequest) {
        Blog existingBlog = this.jpaRepository.findOneWithNotFoundDetection(blogId);

        final UserDto userDto = this.userUtil.getCurrentUserDto(authHeader);

        User author = EntityUtil.getEntityById(this.userRepository, userDto.getId());

        boolean isUpdated = false;

        if (!userDto.getId().equals(existingBlog.getAuthor().getId())) {
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

    @Override
    public BlogDto retrieveOne(final Long id) {
        return this.jdbcRepository.findById(id)
                .map(this::convertToDto)
                .orElseThrow(() -> new EntityNotFoundException("Blog with ID " + id + " not found"));
    }

    private BlogDto convertToDto(BlogRecord blogRecord) {
        BlogDto blogDto = this.modelMapper.map(blogRecord, BlogDto.class);

        List<ReactDto> reactList = this.reactJdbcRepository.findByEntityIdAndEntityType(blogDto.getId(), ReactEntityType.BLOG.getValue())
                .stream()
                .map(this::convertReactToDto)
                .collect(Collectors.toList());

        blogDto.setReactList(reactList);

        List<CommentDto> commentList = this.commentJdbcRepository.findByBlogId(blogDto.getId())
                .stream()
                .map(this::convertCommentToDo)
                .collect(Collectors.toList());

        blogDto.setCommentList(commentList);
        return blogDto;
    }

    private ReactDto convertReactToDto(ReactRecord react) {
        return new ReactDto(
            react.authorId(),
            EntityUtil.getEntityById(this.userRepository, react.authorId()).getName(),
            react.react(),
            react.entityId(),
            react.entityType(),
            react.createdAt(),
            react.updatedAt()
        );
    }

    private CommentDto convertCommentToDo(CommentRecord comment) {
        return new CommentDto(
            comment.id(),
            comment.commenterId(),
            comment.commenterName(),
            comment.commentText(),
            comment.createdAt(),
            comment.updatedAt()
        );
    }
}
