package com.gmail.yauheniylebedzeu.web.controller.exception.handler;

import lombok.extern.log4j.Log4j2;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
@Log4j2
public class ApplicationExceptionHandler {

    @ExceptionHandler(SizeLimitExceededException.class)
    public String SizeLimitExceededExceptionHandler(SizeLimitExceededException e, HttpServletRequest request, Model model) {
        log.error(e.getMessage(), e);
        String requestURI = request.getRequestURI();
        model.addAttribute("uri", requestURI);
        model.addAttribute("errorMessage", e.getMessage());
        return "mvcError";
    }

    @ExceptionHandler(RuntimeException.class)
    public String RuntimeExceptionsHandler(RuntimeException e, HttpServletRequest request, Model model) {
        log.error(e.getMessage(), e);
        String requestURI = request.getRequestURI();
        model.addAttribute("uri", requestURI);
        model.addAttribute("errorMessage", e.getMessage());
        return "mvcError";
    }
}
