package ru.yandex.practicum.mymarket.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class BlogExceptionHandler {

    @ExceptionHandler(Exception.class)
    public String handleException(Exception ex, Model model) {
        System.out.println("Произошла ошибка: " + ex);
        model.addAttribute("message", "Сервис временно недоступен");
        return "error";
    }
}
