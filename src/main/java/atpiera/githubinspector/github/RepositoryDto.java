package atpiera.githubinspector.github;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class RepositoryDto {
	private String name;
	private String owner;
	private List<BranchDto> branches;
}
