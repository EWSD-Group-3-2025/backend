package org.teamSmurfs.backend.api.comment.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.teamSmurfs.backend.api.blog.dto.BlogDto;
import org.teamSmurfs.backend.api.blog.model.Blog;
import org.teamSmurfs.backend.api.blog.repository.BlogJpaRepository;
import org.teamSmurfs.backend.api.blog.service.BlogService;
import org.teamSmurfs.backend.api.comment.dto.CommentDto;
import org.teamSmurfs.backend.api.comment.dto.CommentRecord;
import org.teamSmurfs.backend.api.comment.dto.CommentRequest;
import org.teamSmurfs.backend.api.comment.model.Comment;
import org.teamSmurfs.backend.api.comment.repository.CommentJdbcRepository;
import org.teamSmurfs.backend.api.comment.repository.impl.CommentJpaRepositoryWrapper;
import org.teamSmurfs.backend.api.comment.service.CommentService;
import org.teamSmurfs.backend.api.user.dto.UserDto;
import org.teamSmurfs.backend.api.user.model.User;
import org.teamSmurfs.backend.api.user.repository.UserRepository;
import org.teamSmurfs.backend.api.user.utils.UserUtil;
import org.teamSmurfs.backend.config.exception.EntityNotFoundException;
import org.teamSmurfs.backend.config.exception.UnauthorizedException;
import org.teamSmurfs.backend.config.utils.EntityUtil;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService {

    private final CommentJpaRepositoryWrapper jpaRepository;
    private final CommentJdbcRepository jdbcRepository;
    private final BlogService blogService;
    private final BlogJpaRepository blogJpaRepository;
    private final UserRepository userRepository;
    private final UserUtil userUtil;

    @Override
    public void createComment(final String authHeader, final CommentRequest commentRequest) {

        final UserDto userDto = this.userUtil.getCurrentUserDto(authHeader);

        final Blog blog = EntityUtil.getEntityById(this.blogJpaRepository, commentRequest.getBlogId());

        final User commenter = EntityUtil.getEntityById(this.userRepository, userDto.getId());

        final Comment comment = new Comment(
            blog,
            commenter,
            commentRequest.getCommentText()
        );

        this.jpaRepository.save(comment);
    }

    @Override
    public void updateComment(final Long id, final String authHeader, final CommentRequest commentRequest) {

        final BlogDto blog = this.blogService.retrieveOne(commentRequest.getBlogId());

        final Comment existingComment = this.jpaRepository.findOneWithNotFoundDetection(id);

        if (!existingComment.getBlog().getId().equals(blog.getId())) {
            throw new UnauthorizedException("Comment with id " + id + " of Blog ID: " + commentRequest.getBlogId() +  " is not found");
        }

        if (!existingComment.getCommentText().equals(commentRequest.getCommentText())) {
            existingComment.setCommentText(commentRequest.getCommentText());
            this.jpaRepository.save(existingComment);
        }
    }

    @Override
    public void deleteComment(final Long id) { this.jpaRepository.deleteById(id); }

    @Override
    public CommentDto retrieveOne(final Long id) {
        return this.jdbcRepository.findById(id)
                .map(this::convertToDto)
                .orElseThrow(() -> new EntityNotFoundException("Comment with ID " + id + " not found"));
    }

    private CommentDto convertToDto(CommentRecord comment) {
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
