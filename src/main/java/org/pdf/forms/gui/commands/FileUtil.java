package org.pdf.forms.gui.commands;

public final class FileUtil {

    private static final int MAX_CHARS = 30;

    public static String getShortenedFileName(
            final String fileNameToAdd,
            final String fileSeparator) {

        if (fileNameToAdd.length() <= MAX_CHARS) {
            return fileNameToAdd;
        }

        final String[] arrayedFilePath = fileNameToAdd.split(fileSeparator);
        final int numberOfTokens = arrayedFilePath.length;

        final String filePathBody = fileNameToAdd.substring(arrayedFilePath[0].length(),
                fileNameToAdd.length() - arrayedFilePath[numberOfTokens - 1].length());

        final StringBuilder builder = new StringBuilder(filePathBody);

        for (int i = numberOfTokens - 2; i > 0; i--) {
            final int start = builder.lastIndexOf(arrayedFilePath[i]);

            final int end = start + arrayedFilePath[i].length();
            builder.replace(start, end, "...");

            if (builder.toString().length() <= MAX_CHARS) {
                break;
            }
        }

        return arrayedFilePath[0] + builder.toString() + arrayedFilePath[numberOfTokens - 1];
    }
}
