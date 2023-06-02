package atpiera.githubinspector.github;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class BranchDto {
	private String name;
	private String lastCommitSha;
}
