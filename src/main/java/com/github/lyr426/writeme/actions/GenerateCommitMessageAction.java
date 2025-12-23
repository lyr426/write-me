package com.github.lyr426.writeme.actions;

import com.github.lyr426.writeme.services.AiCommitSuggester;
import com.github.lyr426.writeme.services.DiffAnalyzer;
import com.github.lyr426.writeme.toolWindow.CommitMessageDialog;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vcs.CommitMessageI;
import com.intellij.openapi.vcs.VcsDataKeys;
import git4idea.repo.GitRepository;
import git4idea.repo.GitRepositoryManager;
import org.jetbrains.annotations.NotNull;

public class GenerateCommitMessageAction extends AnAction {

  @Override
  public void actionPerformed(@NotNull AnActionEvent event) {
    Project project = event.getProject();
    if (project == null) return;

    CommitMessageI commitPanel = VcsDataKeys.COMMIT_MESSAGE_CONTROL.getData(event.getDataContext());

    GitRepositoryManager repositoryManager = project.getService(GitRepositoryManager.class);
    if (repositoryManager == null || repositoryManager.getRepositories().isEmpty()) {
      return;
    }
    GitRepository repository = repositoryManager.getRepositories().get(0);

    DiffAnalyzer.getDiff(project, repository, diff -> {

      new Task.Backgroundable(project, "Generating Commit Message with AI", true) {
        @Override
        public void run(@NotNull ProgressIndicator indicator) {
          try {
            String diffText = String.join("\n", diff);
            String commitMessage = AiCommitSuggester.generate(diffText);

            ApplicationManager.getApplication().invokeLater(() -> {
              CommitMessageDialog dialog = new CommitMessageDialog(project, commitMessage, commitPanel);
              dialog.show();
            });

          } catch (Exception e) {
            e.printStackTrace();
            ApplicationManager.getApplication().invokeLater(() ->
              Messages.showErrorDialog(project, "AI Error: " + e.getMessage(), "Error")
            );
          }
        }
      }.queue();
    });
  }
}
