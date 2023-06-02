package atpiera.githubinspector.github;

import atpiera.githubinspector.exception.ServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.when;

@SpringBootTest
class GithubServiceTest {

	@Autowired
	private GithubService githubService;

	@MockBean
	RestTemplate restTemplate;

	@Autowired
	GithubApiErrorHandler githubApiErrorHandler;

	@BeforeEach
	public void setup() throws IOException {
		String mockedUserReposResponse = readFileFromResources("mockedUserReposResponse.json");
		String mockedBranchesResponse = readFileFromResources("mockedBranchesResponse.json");

		when(restTemplate.getForEntity(contains("mockedUser/repos"), any()))
				.thenReturn(new ResponseEntity<>(mockedUserReposResponse, HttpStatus.OK));

		when(restTemplate.getForEntity(contains("/branches"), any()))
				.thenReturn(new ResponseEntity<>(mockedBranchesResponse, HttpStatus.OK));
	}

	private String readFileFromResources(String filename) throws IOException {
		URL resource = getClass().getClassLoader().getResource(filename);
		return Files.readString(new File(resource.getFile()).toPath());
	}

	@Test
	void shouldThrowExceptionWhenInvalidHeader() {
		String invalidHeader = "text/plain";
		String username = "mockUser";
		assertThrows(ServiceException.class, () -> githubService.getUserRepositories(invalidHeader, username));
	}

	@Test
	void shouldReturnRepositories() {
		String validHeader = "application/json";
		String username = "mockedUser";

		List<RepositoryDto> expected = List.of(
				new RepositoryDto("mockedProject", "mockedUser",
						List.of(
								new BranchDto("master", "322013e11f1df8899c9025a592c07c852f873a55"),
								new BranchDto("develop", "322013e11f1df8899c9025a592c07c852f873a55"))));

		List<RepositoryDto> actual = githubService.getUserRepositories(validHeader, username);

		assertEquals(expected, actual);
	}
}
