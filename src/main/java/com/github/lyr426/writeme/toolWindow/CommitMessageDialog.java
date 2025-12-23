package com.github.lyr426.writeme.toolWindow;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vcs.CommitMessageI;
import com.intellij.openapi.vcs.changes.ChangeListManager;
import com.intellij.openapi.vcs.changes.LocalChangeList;
import com.intellij.openapi.vcs.ui.CommitMessage;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import org.jetbrains.annotations.Nullable;

public class CommitMessageDialog extends DialogWrapper {

  private final Project project;
  private final String message;
  private final CommitMessageI commitPanel;
  private JTextArea textArea;

  public CommitMessageDialog(Project project, String message, CommitMessageI commitPanel) {
    super(true);
    this.project = project;
    this.message = message;
    this.commitPanel = commitPanel;
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
      // 1. 내부 데이터(ChangeList) 업데이트 (기존 코드 유지)
      ChangeListManager changeListManager = ChangeListManager.getInstance(project);
      List<LocalChangeList> changeLists = changeListManager.getChangeLists();

      if (!changeLists.isEmpty()) {
        LocalChangeList changeList = changeLists.get(0);
        changeListManager.editComment(changeList.getName(), commitMessage);
      }

      if (commitPanel instanceof CommitMessage) {
        ((CommitMessage) commitPanel).setCommitMessage(commitMessage);
        System.out.println("Updated commit message: " + commitMessage);
      } else {
        // 혹시 패널을 못 받아왔을 경우 로그 정도만 남김 (ChangeListManager가 백업 역할)
        System.out.println("Warning: Commit UI panel is null, only ChangeList updated.");
      }

      return true;

    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }
}
