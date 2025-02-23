package org.teamSmurfs.backend.api.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.teamSmurfs.backend.api.comment.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
