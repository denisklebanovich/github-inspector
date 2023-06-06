package atpiera.githubinspector.inteceptor;

import atpiera.githubinspector.exception.Reason;
import atpiera.githubinspector.exception.ServiceException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AcceptHeaderInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		String acceptHeader = request.getHeader(HttpHeaders.ACCEPT);
		if (!MediaType.APPLICATION_JSON_VALUE.equals(acceptHeader) && !MediaType.ALL_VALUE.equals(acceptHeader)) {
			throw new ServiceException(Reason.INVALID_ACCEPT_HEADER);
		}
		return true;
	}
}
