package org.teamSmurfs.backend.api.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.teamSmurfs.backend.api.blog.model.Blog;

public interface BlogRepository extends JpaRepository<Blog, Long> {
}
