package com.tausifk.ecorys.common;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ModelAndView handleNotFound(NotFoundException e) {
        ModelAndView view = new ModelAndView("error", HttpStatus.NOT_FOUND);
        view.addObject("status", HttpStatus.NOT_FOUND.value());
        view.addObject("error", HttpStatus.NOT_FOUND.getReasonPhrase());
        view.addObject("message", e.getMessage());
        return view;
    }
}
