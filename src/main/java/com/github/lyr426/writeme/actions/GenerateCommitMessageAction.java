package com.github.lyr426.writeme.actions;

import com.github.lyr426.writeme.services.AiCommitSuggester;
import com.github.lyr426.writeme.services.DiffAnalyzer;
import com.github.lyr426.writeme.toolWindow.CommitMessageDialog;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import git4idea.repo.GitRepository;
import git4idea.repo.GitRepositoryManager;
import java.util.List;
import javax.swing.SwingUtilities;
import org.jetbrains.annotations.NotNull;

public class GenerateCommitMessageAction extends AnAction {

  @Override
  public void actionPerformed(@NotNull AnActionEvent event) {
    Project project = event.getProject();

    if(project == null) {
      return;
    }
    GitRepositoryManager repositoryManager = project.getService(GitRepositoryManager.class);
    if (repositoryManager == null) {
      System.err.println("Could not initialize GitRepositoryManager.");
      return;
    }
    List<GitRepository> repositories = repositoryManager.getRepositories();
    if (repositories.isEmpty()) {
      System.err.println("No Git repositories found in the project.");
      return;
    }
    GitRepository repository = repositories.get(0);

    System.out.println("getDiff() 호출 시작");
    try {
      DiffAnalyzer.getDiff(project, repository, diff -> {
        try {
          String commitMessage = AiCommitSuggester.generate(String.join("\n", diff));
          SwingUtilities.invokeLater(() -> {
            CommitMessageDialog dialog = new CommitMessageDialog(project, commitMessage);
            dialog.show();
          });
        } catch (Exception e) {
          e.printStackTrace();
          System.out.println("❌ 콜백 내부에서 예외 발생: " + e.getMessage());
        }
      });
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("❌ getDiff() 호출 중 예외 발생: " + e.getMessage());
    }
  }
}
