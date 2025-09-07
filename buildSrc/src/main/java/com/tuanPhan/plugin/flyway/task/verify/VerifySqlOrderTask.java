package com.tuanPhan.plugin.flyway.task.verify;

import com.tuanPhan.plugin.flyway.FlywayPlugin;
import org.gradle.api.DefaultTask;
import org.gradle.api.GradleException;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.file.ConfigurableFileTree;
import org.gradle.api.file.FileTree;
import org.gradle.api.tasks.*;

@CacheableTask
public class VerifySqlOrderTask extends DefaultTask {

    private String workingDir;

    // hold the tree built at configuration time
    private ConfigurableFileTree sqlFiles;

    public void setWorkingDir(String workingDir) {
        this.workingDir = workingDir;
    }

    @Input
    public String getWorkingDir() {
        return workingDir;
    }

    /** Configure-time file set (no project access in @TaskAction) */
    public void setSqlFiles(ConfigurableFileTree files) {
        this.sqlFiles = files;
    }

    @InputFiles
    @PathSensitive(PathSensitivity.RELATIVE)
    public FileTree getSqlFiles() {
        return this.sqlFiles;
    }

    @TaskAction
    public void executeTask() {
        if (sqlFiles == null) {
            throw new GradleException("verifySqlOrder: 'sqlFiles' was not configured");
        }
        SqlOrderTest.invoke(sqlFiles);
    }

    private static final String TASK_NAME = "verifySqlOrder";
    private static final String TASK_DESCRIPTION = "Validate the order of sql migration files.";

    public static VerifySqlOrderTask configure(Project project, String workingDir) {
        Task existing = project.getTasks().findByName(TASK_NAME);
        if (existing != null) {
            VerifySqlOrderTask t = (VerifySqlOrderTask) existing;
            t.setWorkingDir(workingDir);
            t.setSqlFiles(project.fileTree(workingDir));
            t.setGroup(FlywayPlugin.PLUGIN_NAME);
            t.setDescription(TASK_DESCRIPTION);
            return t;
        } else {
            VerifySqlOrderTask task = project.getTasks().create(TASK_NAME, VerifySqlOrderTask.class);
            task.setWorkingDir(workingDir);
            task.setSqlFiles(project.fileTree(workingDir));
            task.setGroup(FlywayPlugin.PLUGIN_NAME);
            task.setDescription(TASK_DESCRIPTION);
            return task;
        }
    }
}

