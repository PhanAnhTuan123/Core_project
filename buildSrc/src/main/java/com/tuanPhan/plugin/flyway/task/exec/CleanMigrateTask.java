package com.tuanPhan.plugin.flyway.task.exec;

import com.tuanPhan.plugin.flyway.FlywayPlugin;
import com.tuanPhan.plugin.flyway.model.FlywayConfig;
import org.gradle.api.Project;
import org.gradle.api.Task;

import java.util.Arrays;

public abstract class CleanMigrateTask extends FlywayExecTask {

    public static final String TASK_NAME = "cleanMigrate";

    public CleanMigrateTask() {
        super(Arrays.asList("clean", "migrate"));
    }

    public static CleanMigrateTask configure(Project project, FlywayConfig config, Task... dependsOn) {
        CleanMigrateTask task = project.getTasks().create(TASK_NAME, CleanMigrateTask.class);

        task.setDescription("Run flyway cleanMigrate on " + config.dbName());
        task.setGroup(FlywayPlugin.PLUGIN_NAME);
        task.setConfig(config);
        task.dependsOn((Object[]) dependsOn);

        return task;
    }

}