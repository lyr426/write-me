package com.github.lyr426.writeme.toolWindow;

import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vcs.CommitMessageI;
import com.intellij.openapi.vcs.VcsDataKeys;
import com.intellij.openapi.vcs.changes.ChangeListManager;
import com.intellij.openapi.vcs.changes.LocalChangeList;
import com.intellij.openapi.vcs.changes.ui.CommitChangeListDialog;
import com.intellij.openapi.vcs.ui.CommitMessage;
import com.intellij.vcs.commit.ChangeListCommitState;
import git4idea.commands.Git;
import git4idea.commands.GitLineHandler;
import git4idea.commands.GitTextHandler;
import git4idea.commit.GitCommitCompletionContributor;
import git4idea.commit.GitTemplateCommitMessageProvider;
import git4idea.commit.signature.GitCommitSignature;
import git4idea.commit.signature.GitCommitSignature.Verified;
import git4idea.performanceTesting.GitCommitCommand;
import git4idea.stash.GitStashCache.StashData.Changes;
import java.awt.Color;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import org.eclipse.jgit.internal.JGitText;
import org.jetbrains.annotations.Nullable;

public class CommitMessageDialog extends DialogWrapper {

  private final Project project; // 프로젝트 객체
  private final String message;
  private JTextArea textArea; // 텍스트 입력 필드

  public CommitMessageDialog(Project project, String message) {
    super(true);
    this.project = project;
    this.message = message;
    init();
    setTitle("Generated Commit Message");
  }

  @Override
  protected @Nullable JComponent createCenterPanel() {
    JPanel panel = new JPanel();
    textArea = new JTextArea(message, 10, 50);
    panel.add(textArea);

    return panel;
  }

  @Override
  protected void doOKAction() {
    String updatedMessage = textArea.getText().trim();

    setCommitMessage(updatedMessage);

    super.doOKAction();
  }

  private boolean setCommitMessage(String commitMessage) {
    if (project == null) {
      Messages.showErrorDialog("Project is unavailable.", "Error");
      return false;
    }

    try {
      // ChangeListManager를 통해 현재 변경 목록 가져오기
      ChangeListManager changeListManager = ChangeListManager.getInstance(project);
      List<LocalChangeList> changeLists = changeListManager.getChangeLists();

      if (changeLists.isEmpty()) {
        Messages.showErrorDialog("No changelists found.", "Error");
        return false;
      }

      // 첫 번째 변경 목록에 커밋 메시지 설정
      LocalChangeList changeList = changeLists.get(0);
      changeListManager.editComment(changeList.getName(), commitMessage);
      changeListManager.setDefaultChangeList(changeList);

      SwingUtilities.invokeLater(() -> {
        DataManager.getInstance().getDataContextFromFocusAsync().onSuccess(dataContext -> {
          CommitMessageI commitMessageComponent = VcsDataKeys.COMMIT_MESSAGE_CONTROL.getData(dataContext);
          if (commitMessageComponent instanceof CommitMessage) {
            ((CommitMessage) commitMessageComponent).setText(commitMessage);
            System.out.println("Updated commit message: " + commitMessage);
          } else {
            Messages.showErrorDialog("Commit Message UI is not available.", "Error");
          }
        }).onError(throwable -> {
          Messages.showErrorDialog("Failed to retrieve commit message component: " + throwable.getMessage(), "Error");
        });
      });

      return true;

    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }
}
