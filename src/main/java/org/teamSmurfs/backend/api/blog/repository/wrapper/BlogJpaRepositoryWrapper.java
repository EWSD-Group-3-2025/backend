package org.teamSmurfs.backend.api.blog.repository.wrapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.teamSmurfs.backend.api.blog.model.Blog;
import org.teamSmurfs.backend.api.blog.repository.BlogJpaRepository;
import org.teamSmurfs.backend.config.exception.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class BlogJpaRepositoryWrapper {

    private final BlogJpaRepository repository;

    public Blog save(final Blog entity) { return this.repository.save(entity); }

    @Transactional
    public void deleteById(final Long entityId) {
        this.findOneWithNotFoundDetection(entityId);
        this.repository.deleteById(entityId);
    }

    @Transactional(readOnly = true)
    public Blog findOneWithNotFoundDetection(final Long id) {
        return this.repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Blog not found with id " + id));
    }
}
