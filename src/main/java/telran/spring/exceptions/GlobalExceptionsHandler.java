package telran.spring.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionsHandler {
	@ExceptionHandler(NotFoundException.class)
	ResponseEntity<String> notFoundHandler() {
		
	}
}
