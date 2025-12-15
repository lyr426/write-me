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
import git4idea.repo.GitRepository;
import git4idea.repo.GitRepositoryManager;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GenerateCommitMessageAction extends AnAction {

  @Override
  public void actionPerformed(@NotNull AnActionEvent event) {
    Project project = event.getProject();
    if (project == null) return;

    GitRepositoryManager repositoryManager = project.getService(GitRepositoryManager.class);
    if (repositoryManager == null || repositoryManager.getRepositories().isEmpty()) {
      return;
    }
    GitRepository repository = repositoryManager.getRepositories().get(0);

    // 1. Diff 가져오기 (DiffAnalyzer 내부는 백그라운드 -> UI 콜백 구조라고 가정)
    DiffAnalyzer.getDiff(project, repository, diff -> {
      // [중요] 콜백은 UI 스레드에서 실행되므로, 여기서 바로 AI를 호출하면 화면이 멈춥니다.
      // 따라서 다시 백그라운드 작업(Task.Backgroundable)을 시작해야 합니다.

      new Task.Backgroundable(project, "Generating Commit Message with AI", true) {
        @Override
        public void run(@NotNull ProgressIndicator indicator) {
          try {
            // 2. AI 요청 (이제 안전한 백그라운드 스레드에서 실행됨)
            String diffText = String.join("\n", diff);
            String commitMessage = AiCommitSuggester.generate(diffText);

            // 3. 결과 표시 (다시 UI 스레드로 복귀)
            ApplicationManager.getApplication().invokeLater(() -> {
              CommitMessageDialog dialog = new CommitMessageDialog(project, commitMessage);
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
