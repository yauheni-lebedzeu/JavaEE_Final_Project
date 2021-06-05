package com.gmail.yauheniylebedzeu.service;

import com.gmail.yauheniylebedzeu.service.model.CommentDTO;

public interface CommentService {

    CommentDTO addCommentToArticle(String userUuid, String articleUuid, CommentDTO commentDTO);

    boolean deleteComment(String commentUuid);

}
