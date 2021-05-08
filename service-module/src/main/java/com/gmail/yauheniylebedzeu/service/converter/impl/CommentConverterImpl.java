package com.gmail.yauheniylebedzeu.service.converter.impl;

import com.gmail.yauheniylebedzeu.repository.model.Comment;
import com.gmail.yauheniylebedzeu.repository.model.User;
import com.gmail.yauheniylebedzeu.service.converter.CommentConverter;
import com.gmail.yauheniylebedzeu.service.exception.UserNotReceivedException;
import com.gmail.yauheniylebedzeu.service.model.CommentDTO;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class CommentConverterImpl implements CommentConverter {

    @Override
    public Comment convertCommentDTOToComment(CommentDTO commentDTO) {
        Comment comment = new Comment();
        comment.setContent(commentDTO.getContent());
        return comment;
    }

    @Override
    public CommentDTO convertCommentToCommentDTO(Comment comment) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(comment.getId());
        commentDTO.setUuid(comment.getUuid());
        commentDTO.setContent(comment.getContent());
        commentDTO.setAdditionDate(comment.getAdditionDate());
        User user = comment.getUser();
        if (Objects.isNull(user)) {
            throw new UserNotReceivedException(String.format("Couldn't get the user from the database for the comment" +
                    " with id = %d", comment.getId()));
        }
        commentDTO.setUserFirstName(user.getFirstName());
        commentDTO.setUserLastName(user.getLastName());
        return commentDTO;
    }
}
