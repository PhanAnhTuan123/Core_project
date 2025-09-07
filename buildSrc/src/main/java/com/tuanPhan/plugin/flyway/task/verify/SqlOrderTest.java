package com.tuanPhan.plugin.flyway.task.verify;

import com.tuanPhan.plugin.flyway.model.SqlMigration;
import org.gradle.api.GradleException;
import org.gradle.api.file.ConfigurableFileTree;
import org.gradle.api.file.FileVisitDetails;
import org.gradle.api.file.FileVisitor;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class SqlOrderTest {

    private static final String UNSET_MIGRATION_MARKER = "REORDER";

    private SqlOrderTest() {
        // Utility class, prevent instantiation
    }

    /**
     * Verify all sql migrations from a given directory tree.
     *
     * @param tree The directory which contains all migration files
     */
    public static void invoke(ConfigurableFileTree tree) {
        List<SqlMigration> migrations = collect(tree);
        verify(migrations.toArray(new SqlMigration[0]));
    }

    /**
     * Walk through a file directory and collect all migration files.
     * This method assumes all files must be in the following format:
     * <pre>
     *   [order]-[file-name].sql
     * </pre>
     * where {@code order} is a number.
     * <p>
     * Note: it will not attempt to verify the file name format.
     *
     * @param tree The file tree that contains all SQL migration files
     */
    private static List<SqlMigration> collect(ConfigurableFileTree tree) {
        List<SqlMigration> migrations = new ArrayList<>();
        tree.visit(new FileVisitor() {
            @Override
            public void visitFile(@NotNull FileVisitDetails fileDetails) {
                File file = fileDetails.getFile();
                if (file.isFile()) {
                    String name = file.getName();
                    if (!name.isEmpty()
                            && Character.isDigit(name.charAt(0))
                            && !name.contains(UNSET_MIGRATION_MARKER)) {
                        migrations.add(SqlMigration.from(file));
                    }
                }
            }

            @Override
            public void visitDir(@NotNull FileVisitDetails dirDetails) {
                // Ignore directories
            }
        });
        return migrations;
    }

    /**
     * Rule for all sql files:
     * - All SQL migration files are from [0, n]
     * - There is no order duplication
     * Out of order or unsorted order is valid.
     */
    private static void verify(SqlMigration[] migrations) {
        int size = migrations.length;
        if (size == 0) {
            throw new GradleException("No SQL migrations found!");
        }

        for (SqlMigration migration : migrations) {
            if (migration.getOrder() >= size) {
                throw new GradleException("Invalid SQL migration: " + migration.getFileName());
            }
        }

        Set<Integer> travelledSqlMigrationOrder = new HashSet<>();
        Set<SqlMigration> duplicateOrders = new HashSet<>();

        for (SqlMigration migration : migrations) {
            if (!travelledSqlMigrationOrder.add(migration.getOrder())) {
                duplicateOrders.add(migration);
            }
        }

        if (!duplicateOrders.isEmpty()) {
            List<String> duplicateFileNames = new ArrayList<>();
            for (SqlMigration m : duplicateOrders) {
                duplicateFileNames.add(m.getFileName());
            }
            throw new GradleException("Duplicate SQL migrations found: " + duplicateFileNames);
        }

        System.out.println("SQL migration looks great!");
    }
}
