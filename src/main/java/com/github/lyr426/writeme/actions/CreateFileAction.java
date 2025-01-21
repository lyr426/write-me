package com.github.lyr426.writeme.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import org.jetbrains.annotations.NotNull;

public class CreateFileAction extends AnAction {

  @Override
  public void actionPerformed(@NotNull AnActionEvent event) {
    Project project = event.getProject();
    if(project == null) {
      return;
    }

    String basePath = project.getBasePath();
    if(basePath == null) {
      return;
    }

    Editor editor = event.getRequiredData(com.intellij.openapi.actionSystem.CommonDataKeys.EDITOR);
    if(editor == null) {
      Messages.showErrorDialog("No editor found!", "Error");
      return;
    }

    String newFilePath = basePath + "/README.md";

    ApplicationManager.getApplication().runWriteAction(() -> {
      try {
        File createFile = new File(newFilePath);
        String newContent = editor.getSelectionModel().getSelectedText();

        if(createFile.exists()) {
          String existingContent = Files.readString(createFile.toPath(), StandardCharsets.UTF_8);
          String updatedContent = existingContent + "\n" + newContent;
          Files.writeString(createFile.toPath(), updatedContent, StandardCharsets.UTF_8);
        } else {
          boolean created = createFile.createNewFile();
          if(!created) {
            return;
          }
          String initialContent = "EEEE HELLO WORLD!";
          Files.writeString(createFile.toPath(), initialContent + "\n" +newContent, StandardCharsets.UTF_8);
        }

        VirtualFile virtualFile = VirtualFileManager.getInstance().refreshAndFindFileByUrl("file://" + newFilePath);
        if(virtualFile != null) {
          FileEditorManager.getInstance(project).openFile(virtualFile, true);
        }
      }catch (IOException e){
        e.printStackTrace();
      }
    });
  }
}
