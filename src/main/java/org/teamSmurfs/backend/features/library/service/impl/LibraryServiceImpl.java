package org.teamSmurfs.backend.features.library.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.teamSmurfs.backend.config.exception.EntityNotFoundException;
import org.teamSmurfs.backend.config.utils.EntityUtil;
import org.teamSmurfs.backend.features.library.dto.BookDto;
import org.teamSmurfs.backend.features.library.dto.BookRecord;
import org.teamSmurfs.backend.features.library.dto.LibraryRequest;
import org.teamSmurfs.backend.features.library.mapper.BookMapper;
import org.teamSmurfs.backend.features.library.model.Book;
import org.teamSmurfs.backend.features.library.model.BookCategory;
import org.teamSmurfs.backend.features.library.repository.BookJdbcRepository;
import org.teamSmurfs.backend.features.library.service.LibraryService;
import org.teamSmurfs.backend.features.user.dto.UserDto;
import org.teamSmurfs.backend.features.user.model.User;
import org.teamSmurfs.backend.features.user.repository.UserRepository;
import org.teamSmurfs.backend.features.user.utils.UserUtil;

import java.util.List;

@Service
@AllArgsConstructor
public class LibraryServiceImpl implements LibraryService {

    private final BookJdbcRepository jdbcRepository;
    private final UserUtil userUtil;
    private final UserRepository userRepository;

    @Override
    public void createBook(final LibraryRequest libraryRequest, final String authHeader) {
        final UserDto userDto = this.userUtil.getCurrentUserDto(authHeader);

        final User uploader = EntityUtil.getEntityById(this.userRepository, userDto.getId());
        final Book book = BookMapper.toEntity(libraryRequest, uploader.getId());

        this.jdbcRepository.createBook(book);
    }

    @Override
    public void updateBook(final Long id, final LibraryRequest libraryRequest, final String authHeader) {

        final UserDto userDto = this.userUtil.getCurrentUserDto(authHeader);

        final User uploader = EntityUtil.getEntityById(this.userRepository, userDto.getId());
        final Book book = BookMapper.toEntity(libraryRequest, uploader.getId());

        this.jdbcRepository.updateBook(id, book);
    }

    @Override
    public List<BookDto> retrieveAllBooks() {
        return this.jdbcRepository.findAll()
                .stream().map(this::convertToDto)
                .toList();
    }

    @Override
    public BookDto retrieveBook(final Long id) {
        return this.jdbcRepository.findBookById(id)
                .map(this::convertToDto)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with ID: " + id));
    }

    @Override
    public void deleteBook(final Long id) {
        this.jdbcRepository.deleteBook(id);
    }

    private BookDto convertToDto(final BookRecord book) {
        return new BookDto(
                book.id(),
                book.name(),
                book.categoryId(),
                BookCategory.fromInt(book.categoryId()).getCode(),
                book.difficultyLevel(),
                book.rating(),
                book.organizationName(),
                book.organizationUrl(),
                book.description(),
                book.url(),
                book.uploaderId(),
                book.uploaderName(),
                book.createdAt(),
                book.updatedAt()
        );
    }
}
