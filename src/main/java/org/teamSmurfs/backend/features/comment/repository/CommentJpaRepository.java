package org.teamSmurfs.backend.features.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.teamSmurfs.backend.features.comment.model.Comment;

public interface CommentJpaRepository extends JpaRepository<Comment, Long> {
}
