package com.tuanPhan.plugin.flyway;

import com.tuanPhan.plugin.flyway.model.FlywayConfig;
import com.tuanPhan.plugin.flyway.task.exec.CleanMigrateTask;
import com.tuanPhan.plugin.flyway.task.exec.MigrateTask;
import com.tuanPhan.plugin.flyway.task.verify.VerifySqlOrderTask;
import org.gradle.api.GradleException;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.tasks.testing.Test;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;


public class FlywayPlugin implements Plugin<Project> {

    public static final String WORKING_DIR = "sql";
    public static final String PLUGIN_NAME = "flyway";

    @Override
    public void apply(Project target) {
        target.afterEvaluate(project -> {
            String fileName;
            if ("local".equals(System.getenv("MICRONAUT_ENVIRONMENTS"))) {
                fileName = WORKING_DIR + "/flyway-local.conf";
            } else {
                fileName = null;
            }

            if (fileName == null || !new File(fileName).exists()) {
                fileName = WORKING_DIR + "/flyway.conf";
            }

            File configFile = new File(target.getProjectDir(), fileName);

            Properties properties = new Properties();
            try (FileInputStream fis = new FileInputStream(configFile)) {
                properties.load(fis);
            } catch (Exception e) {
                throw new GradleException(
                        "Please make sure that the flyway.conf file exists under: "
                                + configFile.getAbsolutePath()
                                + " and is loadable"
                );
            }

            createTasks(target, properties);
        });
    }

    private void createTasks(Project project, Properties properties) {
        FlywayConfig config = FlywayConfig.from(properties);

        Task verifyOrderTask = VerifySqlOrderTask.configure(
                project,
                WORKING_DIR
        );

        Task migrate = MigrateTask.configure(
                project,
                config,
                verifyOrderTask
        );

        Task cleanMigrate = CleanMigrateTask.configure(
                project,
                config,
                verifyOrderTask
        );

        project.getTasks().withType(Test.class).configureEach(test ->
                test.dependsOn(cleanMigrate)
        );
    }
}
