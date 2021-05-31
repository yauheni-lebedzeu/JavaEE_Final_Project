package com.gmail.yauheniylebedzeu.web.controller.exception.handler;

import com.gmail.yauheniylebedzeu.service.exception.ArticleContentNotReceivedModuleException;
import com.gmail.yauheniylebedzeu.service.exception.ArticleNotFoundModuleException;
import com.gmail.yauheniylebedzeu.service.exception.CartDetailNotReceivedModuleException;
import com.gmail.yauheniylebedzeu.service.exception.CommentNotFoundModuleException;
import com.gmail.yauheniylebedzeu.service.exception.CommentNotReceivedModuleException;
import com.gmail.yauheniylebedzeu.service.exception.EmptyCartModuleException;
import com.gmail.yauheniylebedzeu.service.exception.ItemDescriptionNotReceivedModuleException;
import com.gmail.yauheniylebedzeu.service.exception.ItemNotFoundModuleException;
import com.gmail.yauheniylebedzeu.service.exception.ItemNotReceivedModuleException;
import com.gmail.yauheniylebedzeu.service.exception.MailSendingModuleException;
import com.gmail.yauheniylebedzeu.service.exception.OrderDetailNotReceivedModuleException;
import com.gmail.yauheniylebedzeu.service.exception.OrderNotFoundModuleException;
import com.gmail.yauheniylebedzeu.service.exception.ReviewNotFoundModuleException;
import com.gmail.yauheniylebedzeu.service.exception.RoleNotFoundModuleException;
import com.gmail.yauheniylebedzeu.service.exception.RoleNotReceivedModuleException;
import com.gmail.yauheniylebedzeu.service.exception.ServiceModuleException;
import com.gmail.yauheniylebedzeu.service.exception.UserContactsNotReceivedModuleException;
import com.gmail.yauheniylebedzeu.service.exception.UserDeletedModuleException;
import com.gmail.yauheniylebedzeu.service.exception.UserNotFoundModuleException;
import com.gmail.yauheniylebedzeu.service.exception.UserNotReceivedModuleException;
import com.gmail.yauheniylebedzeu.service.exception.WrongItemQuantityModuleException;
import com.gmail.yauheniylebedzeu.service.exception.WrongOrderStatusModuleException;
import lombok.extern.log4j.Log4j2;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice(basePackages = {"com.gmail.yauheniylebedzeu.web.controller.mvc"})
@Log4j2
public class MvcExceptionHandler {

    @ExceptionHandler({ArticleContentNotReceivedModuleException.class, ArticleNotFoundModuleException.class,
            CartDetailNotReceivedModuleException.class, CommentNotFoundModuleException.class, CommentNotReceivedModuleException.class,
            EmptyCartModuleException.class, ItemDescriptionNotReceivedModuleException.class, ItemNotFoundModuleException.class,
            ItemNotReceivedModuleException.class, MailSendingModuleException.class, OrderDetailNotReceivedModuleException.class, OrderNotFoundModuleException.class,
            ReviewNotFoundModuleException.class, RoleNotFoundModuleException.class, RoleNotReceivedModuleException.class,
            UserContactsNotReceivedModuleException.class, UserDeletedModuleException.class, UserNotFoundModuleException.class,
            UserNotReceivedModuleException.class, WrongItemQuantityModuleException.class, WrongOrderStatusModuleException.class})
    public String ServiceExceptionsHandler(ServiceModuleException e, HttpServletRequest request, Model model) {
        log.error(e.getMessage(), e);
        String requestURI = request.getRequestURI();
        model.addAttribute("uri", requestURI);
        model.addAttribute("errorMessage", e.getMessage());
        return "mvcError";
    }
}