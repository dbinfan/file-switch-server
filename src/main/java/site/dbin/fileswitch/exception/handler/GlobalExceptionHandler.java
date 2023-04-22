package site.dbin.fileswitch.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import site.dbin.fileswitch.exception.BadRequestException;
import site.dbin.fileswitch.exception.EntityExistException;
import site.dbin.fileswitch.exception.EntityNotFoundException;
import site.dbin.fileswitch.vo.Result;

import javax.servlet.ServletException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Throwable.class)
    public Result handleException(Throwable e){
        log.error("运行错误",e);
        return Result.error("运行错误");
    }

    @ExceptionHandler(value = BadRequestException.class)
    public Result badRequestException(BadRequestException e) {
        // 打印堆栈信息
        log.error("bad request",e);
        return Result.error(e.getMessage());
    }

    @ExceptionHandler(value = EntityExistException.class)
    public Result entityExistException(EntityExistException e) {
        // 打印堆栈信息
        log.error("实体已存在:"+e.getMessage(),e);
        return Result.error("实体已存在:"+e.getMessage());
    }

    @ExceptionHandler(value = EntityNotFoundException.class)
    public Result entityNotFoundException(EntityNotFoundException e) {
        // 打印堆栈信息
        log.error("实体不存在:"+e.getMessage(),e);
        return Result.error("实体不存在:"+e.getMessage());
    }

    @ExceptionHandler(value = {ServletException.class})
    public Result SaTokenExpiredException(ServletException e) {
        // 打印堆栈信息
         log.error("servlet错误",e);
        return Result.error("请求错误");
    }

}
