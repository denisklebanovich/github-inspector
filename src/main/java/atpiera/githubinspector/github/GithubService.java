package atpiera.githubinspector.github;

import atpiera.githubinspector.exception.Reason;
import atpiera.githubinspector.exception.ServiceException;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class GithubService {

	public static final String APPLICATION_JSON = "application/json";
	public static final String ANY_FORMAT = "*/*";

	private final RestTemplate restTemplate;

	@Value("${github.api.url}")
	private String githubApiUrl;

	@Value("${github.api.token}")
	private String githubApiToken;


	public GithubService(RestTemplate restTemplate, GithubApiErrorHandler errorHandler) {
		this.restTemplate = restTemplate;
		this.restTemplate.setErrorHandler(errorHandler);
		authenticateRequests();
	}

	private void authenticateRequests() {
		if (githubApiToken != null) {
			restTemplate.getInterceptors().add((request, body, execution) -> {
				request.getHeaders().setBearerAuth(githubApiToken);
				return execution.execute(request, body);
			});
		}
	}


	/**
	 * Returns a list of repositories for the given user that are not forks.
	 */
	public List<RepositoryDto> getUserRepositories(String acceptHeader, String username) {
		if (!headerIsAcceptable(acceptHeader)) throw new ServiceException(Reason.INVALID_ACCEPT_HEADER);
		String url = githubApiUrl + "/users/" + username + "/repos";
		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
		List<JsonElement> jsonArray = JsonParser.parseString(response.getBody()).getAsJsonArray().asList();
		return getRepositoriesDtos(jsonArray);
	}

	private static boolean headerIsAcceptable(String acceptHeader) {
		String[] mediaTypes = acceptHeader.split(",");
		return Arrays.stream(mediaTypes)
				.map(String::trim)
				.anyMatch(mediaType -> mediaType.equals(APPLICATION_JSON) ||
						mediaType.equals(ANY_FORMAT));
	}

	private List<RepositoryDto> getRepositoriesDtos(List<JsonElement> jsonArray) {
		return jsonArray.stream()
				.map(JsonElement::getAsJsonObject)
				.filter(jsonObject -> !jsonObject.get("fork").getAsBoolean())
				.map(this::getRepository)
				.toList();
	}

	private RepositoryDto getRepository(JsonObject jsonObject) {
		String name = jsonObject.get("name").getAsString();
		String owner = jsonObject.get("owner").getAsJsonObject().get("login").getAsString();
		List<BranchDto> branches = getBranches(owner, name);
		return new RepositoryDto(name, owner, branches);
	}

	private List<BranchDto> getBranches(String username, String repositoryName) {
		String url = githubApiUrl + "/repos/" + username + "/" + repositoryName + "/branches";
		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
		List<JsonElement> jsonArray = JsonParser.parseString(response.getBody()).getAsJsonArray().asList();
		return getBranchesDtos(jsonArray);
	}

	private List<BranchDto> getBranchesDtos(List<JsonElement> jsonArray) {
		return jsonArray.stream()
				.map(JsonElement::getAsJsonObject)
				.map(this::getBranch)
				.toList();
	}

	private BranchDto getBranch(JsonObject jsonObject) {
		String name = jsonObject.get("name").getAsString();
		JsonObject commit = jsonObject.get("commit").getAsJsonObject();
		String sha = commit.get("sha").getAsString();
		return new BranchDto(name, sha);
	}
}
