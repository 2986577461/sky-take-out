package com.xiaoyan.globalException;


import com.xiaoyan.result.Result;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalException {
    /**
     * 处理 @RequestBody 参数校验失败的异常 (如 @Valid, @Validated)
     * 当请求体是JSON对象，且对象的属性校验失败时，会抛出 MethodArgumentNotValidException
     *
     * @param ex MethodArgumentNotValidException 异常对象
     * @return 统一的Result响应
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<String> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();

                String errorMsg = bindingResult.getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + " " + fieldError.getDefaultMessage())
                .collect(Collectors.joining(", "));

                return Result.error(errorMsg);
    }


    /**
     * 处理 @RequestParam, @PathVariable, @RequestHeader 等参数校验失败的异常
     * 当这些参数使用 @NotBlank, @NotNull, @Min 等注解校验失败时，会抛出 ConstraintViolationException
     * (注意：需要确保在 Controller 上或应用入口类上添加 @Validated 注解才能触发此类校验)
     *
     * @param e ConstraintViolationException 异常对象
     * @return 统一的Result响应
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public Result<String> handleConstraintViolationException(ConstraintViolationException e) {
        String defaultMessage = e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));

                return Result.error("参数校验失败: " + defaultMessage);
    }


    @ExceptionHandler
    public Result exceptionHandler(RuntimeException ex) {
        return Result.error(ex.getMessage());
    }

}
