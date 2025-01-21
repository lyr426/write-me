package com.github.lyr426.writeme.actions;


import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;

public class PopupAction extends AnAction {

  @Override
  public void actionPerformed(@NotNull AnActionEvent event) {
    Editor editor = event.getRequiredData(com.intellij.openapi.actionSystem.CommonDataKeys.EDITOR);
    if(editor == null) {
      Messages.showErrorDialog("No editor found!", "Error");
      return;
    }

    String selectedText = editor.getSelectionModel().getSelectedText();
    if(selectedText == null) {
      Messages.showInfoMessage("No text selected!", "Info");
      return;
    }

    Messages.showMessageDialog(
      "Selected text:\n" + selectedText,
      "Text Selection", Messages.getInformationIcon()
    );
  }
}
