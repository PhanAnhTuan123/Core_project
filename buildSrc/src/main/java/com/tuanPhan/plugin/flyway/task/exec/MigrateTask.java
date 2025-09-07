package com.tuanPhan.plugin.flyway.task.exec;

import com.tuanPhan.plugin.flyway.FlywayPlugin;
import com.tuanPhan.plugin.flyway.model.FlywayConfig;
import org.gradle.api.Project;
import org.gradle.api.Task;

import java.util.Arrays;

public abstract class MigrateTask extends FlywayExecTask {

    private static final String TASK_NAME = "migrate";

    public MigrateTask() {
        super(Arrays.asList("migrate"));
    }

    public static MigrateTask configure(Project project, FlywayConfig config, Task... dependsOn) {
        MigrateTask task = project.getTasks().create(TASK_NAME, MigrateTask.class);

        task.setDescription("Run flyway migrate on " + config.dbName());
        task.setGroup(FlywayPlugin.PLUGIN_NAME);
        task.setConfig(config);
        task.dependsOn((Object[]) dependsOn);

        return task;
    }
}