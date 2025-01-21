package com.github.lyr426.writeme.services;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.github.lyr426.writeme.MyBundle;

public class MyProjectService {

    private static final Logger LOG = Logger.getInstance(MyProjectService.class);

    public MyProjectService(Project project) {
        LOG.info(MyBundle.message("projectService", project.getName()));
    }

    public int getRandomNumber() {
        return (int) (Math.random() * 100) + 1;
    }



}
