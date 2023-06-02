package atpiera.githubinspector.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ServiceException extends RuntimeException {

	private final Reason reason;

	private final String message;

	public ServiceException(Reason reason){
		this.reason = reason;
		this.message = reason.getDefaultMessage();
	}
}
