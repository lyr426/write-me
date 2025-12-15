package com.github.lyr426.writeme.services;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import git4idea.commands.GitCommand;
import git4idea.commands.GitSimpleHandler;
import git4idea.repo.GitRepository;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class DiffAnalyzer {
  public static void getDiff(@NotNull Project project, @NotNull GitRepository gitRepository, @NotNull DiffCallback callback) {
    new Task.Backgroundable(project, "Fetching Git Diff", true) {
      @Override
      public void run(@NotNull ProgressIndicator indicator) {
        try {
          GitSimpleHandler handler = new GitSimpleHandler(project, gitRepository.getRoot(), GitCommand.DIFF);
          handler.setSilent(false);
          handler.addParameters();

          String rawOutput = handler.run();
          List<String> output = Collections.singletonList(rawOutput);

          ApplicationManager.getApplication().invokeLater(() -> {
            callback.onDiffFetched(output);
          });

        } catch (Exception e) {
          e.printStackTrace(); // 로컬 디버깅용 로그

          ApplicationManager.getApplication().invokeLater(() ->
            Messages.showErrorDialog(project, e.getMessage(), "Error")
          );
        }
      }
    }.queue();
  }

  public interface DiffCallback {
    void onDiffFetched(List<String> diffOutput);
  }
}
