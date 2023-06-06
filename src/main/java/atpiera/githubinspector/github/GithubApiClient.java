package atpiera.githubinspector.github;

import atpiera.githubinspector.exception.Reason;
import atpiera.githubinspector.exception.ServiceException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class GithubApiClient {

	private static final String USER_REPOS_ENDPOINT = "/users/{username}/repos";
	private static final String BRANCHES_ENDPOINT = "/repos/{username}/{repositoryName}/branches";
	public static final String GITHUB_API_URL = "https://api.github.com";

	private final WebClient webClient;

	public GithubApiClient() {
		this.webClient = WebClient.builder()
				.baseUrl(GITHUB_API_URL)
				.build();
	}

	public ResponseEntity<String> getUserRepositories(String username) {
		return webClient.get()
				.uri(USER_REPOS_ENDPOINT, username)
				.retrieve()
				.onStatus(status -> status.equals(NOT_FOUND),
						clientResponse -> {
							throw new ServiceException(Reason.USER_NOT_FOUND);
						})
				.toEntity(String.class)
				.block();
	}

	public ResponseEntity<String> getRepositoryBranches(String username, String repositoryName) {
		return webClient.get()
				.uri(BRANCHES_ENDPOINT, username, repositoryName)
				.retrieve()
				.toEntity(String.class)
				.block();
	}
}
