package org.pdf.forms.gui.commands;

public class FileUtil {

    public static String getShortenedFileName(
            final String fileNameToAdd,
            final String fileSeparator) {
        final int maxChars = 30;

        if (fileNameToAdd.length() <= maxChars) {
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

            if (builder.toString().length() <= maxChars) {
                break;
            }
        }

        return arrayedFilePath[0] + builder.toString() + arrayedFilePath[numberOfTokens - 1];
    }
}
