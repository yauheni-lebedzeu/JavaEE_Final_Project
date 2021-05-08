package com.gmail.yauheniylebedzeu.web.controller.exception.handler;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@ControllerAdvice
public class AppExceptionHandler {

    @ExceptionHandler(NoHandlerFoundException.class)
    public String handler404Exception(HttpServletRequest httpServletRequest, NoHandlerFoundException exception, HttpSession session) {
        session.setAttribute("url" , httpServletRequest.getRequestURL());
        session.setAttribute("exception", exception);
        return "error";
    }

}
