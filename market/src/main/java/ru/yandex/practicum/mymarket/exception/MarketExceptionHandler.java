package ru.yandex.practicum.mymarket.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import reactor.core.publisher.Mono;

@ControllerAdvice
public class MarketExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Mono<String> handleException(Exception ex, Model model) {
        System.out.println("Произошла ошибка: " + ex);
        model.addAttribute("message", "Сервис временно недоступен");
        return Mono.just("error");
    }

    @ExceptionHandler(PaymentException.class)
    public Mono<String> handleException(PaymentException ex, Model model) {
        System.out.println("Произошла payment ошибка: " + ex);
        model.addAttribute("message", ex.getErrorMessage());
        return Mono.just("error");
    }
}
