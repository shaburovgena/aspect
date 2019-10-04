package com.example.controller;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

class ControllerUtils {
    static Map<String, String> getErrors(BindingResult bindingResult) {
        //Получаем мапу с ошибками
        Collector<FieldError, ?, Map<String, String>> collector = Collectors.toMap(
                //Вычленяем имя поля и прибаляем "Error"
                fieldError -> fieldError.getField() + "Error",
                FieldError::getDefaultMessage
        );
        System.out.println("Validation error " + bindingResult.getFieldErrors().stream().collect(collector));
        //Возвращаем коллекцию ошибок
        return bindingResult.getFieldErrors().stream().collect(collector);
    }
}
