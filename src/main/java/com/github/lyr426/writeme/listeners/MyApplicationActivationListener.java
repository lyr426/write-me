package com.github.lyr426.writeme.listeners;

import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationActivationListener;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.wm.IdeFrame;
import org.jetbrains.annotations.NotNull;

public class MyApplicationActivationListener implements ApplicationActivationListener {

    private static final Logger LOG = Logger.getInstance(MyApplicationActivationListener.class);

    @Override
    public void applicationActivated(@NotNull IdeFrame ideFrame) {
        Application application = ApplicationManager.getApplication();
        application.invokeLater(() -> {
            System.out.println("🔧 Application is fully initialized, now executing invokeLater tasks...");
            // 필요한 UI 작업 실행
        });
    }

}
