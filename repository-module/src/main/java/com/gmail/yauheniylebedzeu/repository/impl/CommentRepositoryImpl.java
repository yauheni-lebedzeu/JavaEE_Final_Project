package com.gmail.yauheniylebedzeu.repository.impl;

import com.gmail.yauheniylebedzeu.repository.CommentRepository;
import com.gmail.yauheniylebedzeu.repository.model.Comment;
import org.springframework.stereotype.Repository;

@Repository
public class CommentRepositoryImpl extends GenericRepositoryImpl<Long, Comment> implements CommentRepository {
}