package atpiera.githubinspector.github;

import atpiera.githubinspector.exception.Reason;
import atpiera.githubinspector.exception.ServiceException;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.DefaultResponseErrorHandler;

import java.io.IOException;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Component
public class GithubApiErrorHandler extends DefaultResponseErrorHandler {

	@Override
	public void handleError(ClientHttpResponse response) throws IOException {
		HttpStatusCode statusCode = response.getStatusCode();
		if (statusCode.equals(NOT_FOUND)) {
			throw new ServiceException(Reason.USER_NOT_FOUND);
		}
		throw new ServiceException(Reason.UNKNOWN, response.getStatusText());
	}
}
