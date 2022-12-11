package root.conf;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolationException;

@Configuration
class WebConf {

    @ControllerAdvice
    static class GlobalControllerExceptionHandler {

        @ExceptionHandler(ConstraintViolationException.class)
        ResponseEntity<String> handle(Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
