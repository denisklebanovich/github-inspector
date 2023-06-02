package atpiera.githubinspector.exception;


import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

	/**
	 * Handles all exceptions thrown by the service layer.
	 */
	@ExceptionHandler(ServiceException.class)
	public ResponseEntity<ApiExceptionDto> handleServiceException(ServiceException e) {
		var body = new ApiExceptionDto(e.getReason().getStatus().value(), e.getMessage());
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		return new ResponseEntity<>(body, headers, e.getReason().getStatus());
	}

	/**
	 * Handles all unexpected exceptions.
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiExceptionDto> handleUnexpectedException(Exception e) {
		var body = new ApiExceptionDto(INTERNAL_SERVER_ERROR.value(), e.getMessage());
		return new ResponseEntity<>(body, INTERNAL_SERVER_ERROR);
	}
}
