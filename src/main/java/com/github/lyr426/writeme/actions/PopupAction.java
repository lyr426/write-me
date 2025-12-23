package com.github.lyr426.writeme.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys; // Import 추가 필요
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;

public class PopupAction extends AnAction {

  @Override
  public void actionPerformed(@NotNull AnActionEvent event) {
    Editor editor = event.getData(CommonDataKeys.EDITOR);

    if (editor == null) {
      Messages.showErrorDialog("No editor found!", "Error");
      return;
    }

    String selectedText = editor.getSelectionModel().getSelectedText();
    if (selectedText == null || selectedText.isEmpty()) {
      Messages.showInfoMessage("No text selected!", "Info");
      return;
    }

    Messages.showMessageDialog(
      "Selected text:\n" + selectedText,
      "Text Selection", Messages.getInformationIcon()
    );
  }

  @Override
  public void update(@NotNull AnActionEvent event) {
    Editor editor = event.getData(CommonDataKeys.EDITOR);
    boolean hasSelection = editor != null && editor.getSelectionModel().hasSelection();

    event.getPresentation().setEnabledAndVisible(hasSelection);
  }
}
