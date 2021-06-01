package com.gmail.yauheniylebedzeu.web.controller.exception.handler;

import com.gmail.yauheniylebedzeu.service.exception.ArticleContentNotReceivedException;
import com.gmail.yauheniylebedzeu.service.exception.ArticleNotFoundException;
import com.gmail.yauheniylebedzeu.service.exception.CartDetailNotReceivedException;
import com.gmail.yauheniylebedzeu.service.exception.CommentNotFoundException;
import com.gmail.yauheniylebedzeu.service.exception.CommentNotReceivedException;
import com.gmail.yauheniylebedzeu.service.exception.EmptyCartException;
import com.gmail.yauheniylebedzeu.service.exception.ItemDescriptionNotReceivedException;
import com.gmail.yauheniylebedzeu.service.exception.ItemNotFoundException;
import com.gmail.yauheniylebedzeu.service.exception.ItemNotReceivedException;
import com.gmail.yauheniylebedzeu.service.exception.MailSendingException;
import com.gmail.yauheniylebedzeu.service.exception.OrderDetailNotReceivedException;
import com.gmail.yauheniylebedzeu.service.exception.OrderNotFoundException;
import com.gmail.yauheniylebedzeu.service.exception.ReviewNotFoundException;
import com.gmail.yauheniylebedzeu.service.exception.RoleNotFoundException;
import com.gmail.yauheniylebedzeu.service.exception.RoleNotReceivedException;
import com.gmail.yauheniylebedzeu.service.exception.ServiceModuleException;
import com.gmail.yauheniylebedzeu.service.exception.UserContactsNotReceivedException;
import com.gmail.yauheniylebedzeu.service.exception.UserDeletedException;
import com.gmail.yauheniylebedzeu.service.exception.UserNotFoundException;
import com.gmail.yauheniylebedzeu.service.exception.UserNotReceivedException;
import com.gmail.yauheniylebedzeu.service.exception.WrongItemQuantityException;
import com.gmail.yauheniylebedzeu.service.exception.WrongOrderStatusException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(basePackages = {"com.gmail.yauheniylebedzeu.web.controller.api"})
@Log4j2
public class ApiExceptionHandler {

    @ExceptionHandler({ArticleContentNotReceivedException.class, ArticleNotFoundException.class,
            CartDetailNotReceivedException.class, CommentNotFoundException.class, CommentNotReceivedException.class,
            EmptyCartException.class, ItemDescriptionNotReceivedException.class, ItemNotFoundException.class,
            ItemNotReceivedException.class, MailSendingException.class, OrderDetailNotReceivedException.class, OrderNotFoundException.class,
            ReviewNotFoundException.class, RoleNotFoundException.class, RoleNotReceivedException.class,
            UserContactsNotReceivedException.class, UserDeletedException.class, UserNotFoundException.class,
            UserNotReceivedException.class, WrongItemQuantityException.class, WrongOrderStatusException.class})
    public ResponseEntity<?> ServiceExceptionsHandler(ServiceModuleException e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> RuntimeExceptionsHandler(RuntimeException e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}