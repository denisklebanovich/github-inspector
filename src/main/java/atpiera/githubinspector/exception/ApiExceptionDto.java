package atpiera.githubinspector.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiExceptionDto {

	private int status;

	private String message;
}
