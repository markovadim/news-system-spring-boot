package ru.clevertec.newssystemmanagement.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.clevertec.newssystemmanagement.entities.Comment;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    /**
     * @param id       - news id
     * @param pageable - pagination argument
     * @return - comment list
     */
    List<Comment> findAllByNewsId(long id, Pageable pageable);

    /**
     * Search comments by filter
     *
     * @param text     - word(-s) which containing in comment
     * @param username - author of comment
     * @return - comment list
     */
    List<Comment> findAllByTextContainsIgnoreCaseOrUsernameContainsIgnoreCase(String text, String username);
}
