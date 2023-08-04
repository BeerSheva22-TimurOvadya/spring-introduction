package telran.spring.exceptions;

import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.fasterxml.jackson.core.JsonProcessingException;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class GlobalExceptionsHandler {

	@ExceptionHandler(NotFoundException.class)
	ResponseEntity<String> notFoundHandler(NotFoundException e) {
		String errorMessage = e.getMessage();
		log.error(errorMessage);
		return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler({ IllegalArgumentException.class, HttpMessageNotReadableException.class, IllegalStateException.class, JsonProcessingException.class })
	ResponseEntity<String> illegalArgumentHandler(RuntimeException e) {
		String errorMessage = e.getMessage();
		log.error(errorMessage);
		return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	ResponseEntity<String> methodArgumentHandler(MethodArgumentNotValidException e) {
		String errorMessage = e.getAllErrors().stream().map(er -> er.getCodes()[0] + ": " + er.getDefaultMessage())
				.collect(Collectors.joining(";"));
		log.error(errorMessage);
		return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	ResponseEntity<String> constraintViolation(ConstraintViolationException e) {
		String errorMessage = e.getConstraintViolations().stream()
				.map(er -> er.getPropertyPath() + ": " + er.getMessage()).collect(Collectors.joining(";"));
		log.error(errorMessage);
		return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(RuntimeException.class)
	ResponseEntity<String> exceptionsHandler(RuntimeException e) {
		String errorMessage = e.getMessage();
		log.error(errorMessage);
		return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(HttpMediaTypeNotSupportedException.class)
	ResponseEntity<String> mediaTypeNotSupportedHandler(HttpMediaTypeNotSupportedException e) {
	    String errorMessage = "Unsupported media type: " + e.getContentType();
	    log.error(errorMessage);
	    return new ResponseEntity<>(errorMessage, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
	}
	
	
}
