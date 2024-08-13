package com.example.malls.global.advice;

import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
@Log4j2
public class CustomRestAdvice {

    @ExceptionHandler(BindException.class)
    public ResponseEntity<Map<String, String>> handleBindException(BindException e) {
        log.error(e);

        Map<String, String> errorMap = new HashMap<>();

        if (e.hasErrors()) {
            BindingResult bindingResult = e.getBindingResult();

            bindingResult.getFieldErrors().forEach(fieldError -> {
                errorMap.put(fieldError.getField(), fieldError.getCode());
            });
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMap);
    }

    // Client Error - Board bno(FK) error
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> handleFKException(DataIntegrityViolationException e) {
        log.error(e);

        Map<String, String> errorMap = new HashMap<>();

        errorMap.put("time", "" + System.currentTimeMillis());
        errorMap.put("msg", "constraint fails");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMap);
    }

    // Reply - 데이터 없을 경우 예외 처리
    @ExceptionHandler({
            NoSuchElementException.class,
            EmptyResultDataAccessException.class})
    public ResponseEntity<Map<String, String>> handleNoSuchElements(Exception e){
        log.error(e);

        Map<String, String> errorMap = new HashMap<>();

        errorMap.put("time", ""+System.currentTimeMillis());
        errorMap.put("msg", "No Such Element Exception");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMap);
    }
}
