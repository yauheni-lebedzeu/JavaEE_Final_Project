package com.gmail.yauheniylebedzeu.service.converter.impl;

import com.gmail.yauheniylebedzeu.repository.model.Comment;
import com.gmail.yauheniylebedzeu.repository.model.User;
import com.gmail.yauheniylebedzeu.service.converter.CommentConverter;
import com.gmail.yauheniylebedzeu.service.model.CommentDTO;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.gmail.yauheniylebedzeu.service.util.ServiceUtil.formatDateTime;
import static com.gmail.yauheniylebedzeu.service.util.ServiceUtil.getFirstAndLastNameString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CommentConverterImplTest {

    private final CommentConverter commentConverter;

    public CommentConverterImplTest() {
        commentConverter = new CommentConverterImpl();
    }

    @Test
    void shouldConvertCommentDTOToCommentAndReturnNotNullObject() {
        CommentDTO commentDTO = new CommentDTO();
        Comment comment = commentConverter.convertCommentDTOToComment(commentDTO);
        assertNotNull(comment);
    }

    @Test
    void shouldConvertCommentDTOToCommentAndReturnRightContent() {
        CommentDTO commentDTO = new CommentDTO();
        String content = "content";
        commentDTO.setContent(content);
        Comment comment = commentConverter.convertCommentDTOToComment(commentDTO);
        assertEquals(content, comment.getContent());
    }

    @Test
    void shouldConvertCommentToCommentDTOAndGetNotNullObject() {
        Comment comment = new Comment();
        comment.setAdditionDateTime(LocalDateTime.now());
        comment.setUser(new User());
        CommentDTO commentDTO = commentConverter.convertCommentToCommentDTO(comment);
        assertNotNull(commentDTO);
    }

    @Test
    void shouldConvertCommentToCommentDTOAndGetRightAdditionDateTime() {
        Comment comment = new Comment();
        LocalDateTime dateTime = LocalDateTime.now();
        String formattedDateTime = formatDateTime(dateTime);
        comment.setAdditionDateTime(dateTime);
        comment.setUser(new User());
        CommentDTO commentDTO = commentConverter.convertCommentToCommentDTO(comment);
        assertEquals(formattedDateTime, commentDTO.getAdditionDateTime());
    }

    @Test
    void shouldConvertCommentToCommentDTOAndGetRightId() {
        Comment comment = new Comment();
        long id = 1L;
        comment.setId(id);
        comment.setAdditionDateTime(LocalDateTime.now());
        comment.setUser(new User());
        CommentDTO commentDTO = commentConverter.convertCommentToCommentDTO(comment);
        assertEquals(id, commentDTO.getId());
    }

    @Test
    void shouldConvertCommentToCommentDTOAndGetRightUuid() {
        Comment comment = new Comment();
        String uuid = UUID.randomUUID().toString();
        comment.setUuid(uuid);
        comment.setAdditionDateTime(LocalDateTime.now());
        comment.setUser(new User());
        CommentDTO commentDTO = commentConverter.convertCommentToCommentDTO(comment);
        assertEquals(uuid, commentDTO.getUuid());
    }

    @Test
    void shouldConvertCommentToCommentDTOAndGetRightContent() {
        Comment comment = new Comment();
        String content = "content";
        comment.setContent(content);
        comment.setAdditionDateTime(LocalDateTime.now());
        comment.setUser(new User());
        CommentDTO commentDTO = commentConverter.convertCommentToCommentDTO(comment);
        assertEquals(content, commentDTO.getContent());
    }

    @Test
    void shouldConvertCommentToCommentDTOAndGetRightRightAuthorFirstAndLastNameString() {
        Comment comment = new Comment();
        comment.setAdditionDateTime(LocalDateTime.now());
        User user = new User();
        user.setFirstName("FirstName");
        user.setLastName("LastName");
        comment.setUser(user);
        String firstAndLastNameString = getFirstAndLastNameString(user);
        CommentDTO commentDTO = commentConverter.convertCommentToCommentDTO(comment);
        assertEquals(firstAndLastNameString, commentDTO.getFirstAndLastName());
    }
}