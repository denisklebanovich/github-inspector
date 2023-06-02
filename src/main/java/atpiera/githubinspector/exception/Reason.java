package atpiera.githubinspector.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum Reason {
	USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User not found"),
	INVALID_ACCEPT_HEADER(HttpStatus.NOT_ACCEPTABLE,
			"Invalid Accept header. Only 'application/json' is supported."),
	UNKNOWN(HttpStatus.INTERNAL_SERVER_ERROR, "Unknown error");

	final HttpStatus status;
	final String defaultMessage;
}
