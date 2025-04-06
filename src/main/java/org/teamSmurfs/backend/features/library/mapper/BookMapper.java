package org.teamSmurfs.backend.features.library.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.teamSmurfs.backend.features.library.dto.LibraryRequest;
import org.teamSmurfs.backend.features.library.model.Book;
import org.teamSmurfs.backend.features.user.model.User;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BookMapper {

    /**
     * Maps a LibraryRequest to a Book entity.
     *
     * @param request the incoming request data
     * @param uploaderId the authenticated user ID uploading the book
     * @return Book entity ready for persistence
     */
    public static Book toEntity(final LibraryRequest request, final Long uploaderId) {
        Book book = new Book();
        book.setName(request.getBookName());
        book.setUrl(request.getBookUrl());
        book.setCategoryId(request.getCategoryId());
        book.setDifficultyLevel(request.getDifficultyLevel());
        book.setRating(request.getRating());
        book.setOrganizationName(request.getOrganizationName());
        book.setOrganizationUrl(request.getOrganizationUrl());
        book.setDescription(request.getDescription());
        book.setUploaderId(uploaderId);
        return book;
    }
}
