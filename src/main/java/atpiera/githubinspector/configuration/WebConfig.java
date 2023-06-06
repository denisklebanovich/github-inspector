package atpiera.githubinspector.configuration;

import atpiera.githubinspector.inteceptor.AcceptHeaderInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	private final AcceptHeaderInterceptor acceptHeaderInterceptor;

	public WebConfig(AcceptHeaderInterceptor acceptHeaderInterceptor) {
		this.acceptHeaderInterceptor = acceptHeaderInterceptor;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(acceptHeaderInterceptor);
	}
}
