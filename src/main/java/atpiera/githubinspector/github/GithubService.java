package atpiera.githubinspector.github;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class GithubService {


	private static final String NAME = "name";
	private static final String OWNER = "owner";
	private static final String LOGIN = "login";
	private static final String COMMIT = "commit";
	private static final String SHA = "sha";
	private static final String FORK = "fork";


	private final GithubApiClient githubApiClient;


	public GithubService(GithubApiClient githubApiClient) {
		this.githubApiClient = githubApiClient;
	}


	/**
	 * Returns a list of repositories for the given user that are not forks.
	 */
	public List<RepositoryDto> getUserRepositories(String username) {
		ResponseEntity<String> response = githubApiClient.getUserRepositories(username);
		List<JsonElement> jsonArray = JsonParser.parseString(response.getBody()).getAsJsonArray().asList();
		return getRepositoriesDtos(jsonArray);
	}

	private List<RepositoryDto> getRepositoriesDtos(List<JsonElement> jsonArray) {
		return jsonArray.stream()
				.map(JsonElement::getAsJsonObject)
				.filter(jsonObject -> !jsonObject.get(FORK).getAsBoolean())
				.map(this::getRepository)
				.toList();
	}

	private RepositoryDto getRepository(JsonObject jsonObject) {
		String name = jsonObject.get(NAME).getAsString();
		String owner = jsonObject.get(OWNER).getAsJsonObject().get(LOGIN).getAsString();
		List<BranchDto> branches = getBranches(owner, name);
		return new RepositoryDto(name, owner, branches);
	}

	private List<BranchDto> getBranches(String username, String repositoryName) {
		ResponseEntity<String> response = githubApiClient.getRepositoryBranches(username, repositoryName);
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
		String name = jsonObject.get(NAME).getAsString();
		JsonObject commit = jsonObject.get(COMMIT).getAsJsonObject();
		String sha = commit.get(SHA).getAsString();
		return new BranchDto(name, sha);
	}
}
