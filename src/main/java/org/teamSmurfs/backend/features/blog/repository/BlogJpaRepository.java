package org.teamSmurfs.backend.features.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.teamSmurfs.backend.features.blog.model.Blog;

public interface BlogJpaRepository extends JpaRepository<Blog, Long> {
}
