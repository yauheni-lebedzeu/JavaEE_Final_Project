package com.gmail.yauheniylebedzeu.service.converter;

import com.gmail.yauheniylebedzeu.repository.model.Comment;
import com.gmail.yauheniylebedzeu.service.model.CommentDTO;

public interface CommentConverter {

    Comment convertCommentDTOToComment(CommentDTO commentDTO);

    CommentDTO convertCommentToCommentDTO(Comment comment);

}