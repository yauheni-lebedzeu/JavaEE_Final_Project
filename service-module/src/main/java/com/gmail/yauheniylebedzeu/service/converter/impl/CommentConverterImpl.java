package com.gmail.yauheniylebedzeu.service.converter.impl;

import com.gmail.yauheniylebedzeu.repository.model.Comment;
import com.gmail.yauheniylebedzeu.repository.model.User;
import com.gmail.yauheniylebedzeu.service.converter.CommentConverter;
import com.gmail.yauheniylebedzeu.service.model.CommentDTO;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.gmail.yauheniylebedzeu.service.util.EntitiesServiceUtil.getUser;
import static com.gmail.yauheniylebedzeu.service.util.ServiceUtil.formatDateTime;
import static com.gmail.yauheniylebedzeu.service.util.ServiceUtil.getFirstAndLastNameString;

@Component
public class CommentConverterImpl implements CommentConverter {

    @Override
    public Comment convertCommentDTOToComment(CommentDTO commentDTO) {
        Comment comment = new Comment();
        comment.setUuid(UUID.randomUUID().toString());
        comment.setContent(commentDTO.getContent());
        comment.setAdditionDateTime(LocalDateTime.now());
        return comment;
    }

    @Override
    public CommentDTO convertCommentToCommentDTO(Comment comment) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(comment.getId());
        commentDTO.setUuid(comment.getUuid());
        commentDTO.setContent(comment.getContent());
        LocalDateTime additionDateTime = comment.getAdditionDateTime();
        String formattedDateTime = formatDateTime(additionDateTime);
        commentDTO.setAdditionDateTime(formattedDateTime);
        User user = getUser(comment);
        String firstAndLastName = getFirstAndLastNameString(user);
        commentDTO.setFirstAndLastName(firstAndLastName);
        commentDTO.setIsAuthorDeleted(user.getIsDeleted());
        return commentDTO;
    }
}