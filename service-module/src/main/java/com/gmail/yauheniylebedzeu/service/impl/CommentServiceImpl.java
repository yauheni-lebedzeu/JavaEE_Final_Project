package com.gmail.yauheniylebedzeu.service.impl;

import com.gmail.yauheniylebedzeu.repository.CommentRepository;
import com.gmail.yauheniylebedzeu.repository.model.Article;
import com.gmail.yauheniylebedzeu.repository.model.Comment;
import com.gmail.yauheniylebedzeu.repository.model.User;
import com.gmail.yauheniylebedzeu.service.ArticleService;
import com.gmail.yauheniylebedzeu.service.CommentService;
import com.gmail.yauheniylebedzeu.service.UserService;
import com.gmail.yauheniylebedzeu.service.converter.CommentConverter;
import com.gmail.yauheniylebedzeu.service.exception.CommentNotFoundModuleException;
import com.gmail.yauheniylebedzeu.service.model.CommentDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final CommentConverter commentConverter;
    private final UserService userService;
    private final ArticleService articleService;

    @Override
    @Transactional
    public CommentDTO addCommentToArticle(String userUuid, String articleUuid, CommentDTO commentDTO) {
        User user = userService.getSafeUser(userUuid);
        Article article = articleService.getSafeArticle(articleUuid);
        Comment comment = commentConverter.convertCommentDTOToComment(commentDTO);
        comment.setUser(user);
        comment.setArticle(article);
        commentRepository.merge(comment);
        return commentConverter.convertCommentToCommentDTO(comment);
    }

    @Override
    @Transactional
    public boolean deleteComment(String commentUuid) {
        Optional<Comment> optionalComment = commentRepository.findByUuid(commentUuid);
        if (optionalComment.isPresent()) {
            Comment comment = optionalComment.get();
            commentRepository.remove(comment);
            return true;
        }
        throw new CommentNotFoundModuleException(String.format("A comment with uuid %s was not found!", commentUuid));
    }
}