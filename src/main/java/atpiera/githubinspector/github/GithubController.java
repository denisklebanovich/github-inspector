package atpiera.githubinspector.github;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/github")
public class GithubController {

	private final GithubService githubService;

	public GithubController(GithubService githubService) {
		this.githubService = githubService;
	}

	@GetMapping("/users/{username}/repos")
	public List<RepositoryDto> getUserRepositories(@RequestHeader(value = "Accept") String acceptHeader,
	                                               @PathVariable String username) {
		return githubService.getUserRepositories(acceptHeader, username);
	}
}
