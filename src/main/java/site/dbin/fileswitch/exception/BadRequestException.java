package site.dbin.fileswitch.exception;

import lombok.Getter;

@Getter
public class BadRequestException extends RuntimeException{
    Integer status =  400;
    public BadRequestException(String message){
        super(message);
    }

    public BadRequestException(Integer status, String message){
        super(message);
        this.status = status;
    }
}
