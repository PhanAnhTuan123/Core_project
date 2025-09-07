package com.tuanPhan.plugin.flyway.task.exec;

import com.tuanPhan.plugin.flyway.FlywayPlugin;
import com.tuanPhan.plugin.flyway.model.FlywayConfig;
import org.gradle.api.tasks.*;
import org.gradle.work.DisableCachingByDefault;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@DisableCachingByDefault(because = "Runs external Flyway CLI; DB side effects should not be cached")
public abstract class FlywayExecTask extends Exec {

    private static final String FAKE_JAR_FLAG = "-jarDirs=/tmp";
    private static final String ENV_VAR_FAKE_JAR_DIRS = "FAKE_JAR_DIRS";

    @Input
    private final List<String> commands;

    private FlywayConfig config;

    @Input
    public List<String> getCommands() {
        return commands;
    }

    @Nested
    public void setConfig(FlywayConfig config) {
        this.config = config;
    }

    public FlywayExecTask(List<String> commands) {
        this.commands = commands;
    }

    @Input
    public FlywayConfig getConfig() {
        return config;
    }

    @Override
    public void exec() {
        File path = Paths.get(getWorkingDir().getPath(), FlywayPlugin.WORKING_DIR).toFile();
        setWorkingDir(path);
        setCommandLine(buildCommand());
        super.exec();
    }

    private List<String> buildCommand() {
        boolean addFakeJarDirs = "true".equals(System.getenv(ENV_VAR_FAKE_JAR_DIRS));
        String url = Objects.requireNonNull(config).url();
        String flywayBinary = System.getenv("FLYWAY_DIR");
        if (flywayBinary == null) {
            flywayBinary = "flyway";
        }

        List<String> command = new ArrayList<>();
        command.add(flywayBinary);
        command.add("-url=" + url);

        if (config.user() != null && !config.user().isEmpty()) {
            command.add("-user=" + config.user());
        }
        if (config.password() != null && !config.password().isEmpty()) {
            command.add("-password=" + config.password());
        }
        if (addFakeJarDirs) {
            command.add(FAKE_JAR_FLAG);
        }

        command.addAll(commands);
        return command;
    }
}
