package com.tuanPhan.plugin.flyway.model;

import java.io.File;

public class SqlMigration {
    private final int order;
    private final String fileName;

    public SqlMigration(int order, String fileName) {
        this.order = order;
        this.fileName = fileName;
    }

    public int getOrder() {
        return order;
    }

    public String getFileName() {
        return fileName;
    }

    public static SqlMigration from(File file) {
        char[] fileNameArray = file.getName().toCharArray();
        int firstOfNonDigits = -1;
        for (int i = 0; i < fileNameArray.length; i++) {
            if (!Character.isDigit(fileNameArray[i])) {
                firstOfNonDigits = i;
                break;
            }
        }
        String orderSubstring = file.getName().substring(0, firstOfNonDigits);
        int asInt;
        try {
            asInt = Integer.parseInt(orderSubstring);
        } catch (NumberFormatException e) {
            asInt = 0;
        }
        return new SqlMigration(asInt, file.getName());
    }
}
