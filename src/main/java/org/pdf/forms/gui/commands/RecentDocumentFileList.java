package org.pdf.forms.gui.commands;

import static java.util.stream.Collectors.toUnmodifiableList;
import static org.pdf.forms.gui.commands.RecentDocumentType.RECENT_DES_FILES;
import static org.pdf.forms.gui.commands.RecentDocumentType.RECENT_PDF_FILES;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.StringTokenizer;
import java.util.stream.IntStream;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import org.pdf.forms.utils.DesignerPropertiesFile;

class RecentDocumentFileList {

    private final DesignerPropertiesFile designerPropertiesFile;

    private final JMenuItem[] recentDesignerDocuments;
    private final JMenuItem[] recentImportedDocuments;

    RecentDocumentFileList() {
        final File configDir = new File(System.getProperty("user.dir"));
        designerPropertiesFile = DesignerPropertiesFile.getInstance(configDir);

        final int noOfRecentDocs = designerPropertiesFile.getNoRecentDocumentsToDisplay();
        recentDesignerDocuments = new JMenuItem[noOfRecentDocs];
        recentImportedDocuments = new JMenuItem[noOfRecentDocs];
    }

    void addToRecentImportedDocuments(final File pdfFile) {
        designerPropertiesFile.addRecentDocument(pdfFile, RECENT_PDF_FILES);
        final String[] recentDocuments = designerPropertiesFile.getRecentDocuments(RECENT_PDF_FILES);

        addToRecentDocuments(recentDocuments, recentImportedDocuments);
    }

    void addToRecentDesignerDocuments(final File designerFile) {
        designerPropertiesFile.addRecentDocument(designerFile, RECENT_DES_FILES);
        final String[] recentDocuments = designerPropertiesFile.getRecentDocuments(RECENT_DES_FILES);

        addToRecentDocuments(recentDocuments, recentDesignerDocuments);
    }

    private void addToRecentDocuments(
            final String[] recentDocs,
            final JMenuItem[] recentDocuments) {
        if (recentDocs == null) {
            return;
        }

        for (int i = 0; i < recentDocs.length; i++) {
            if (recentDocs[i] != null) {
                final String shortenedFileName = getShortenedFileName(recentDocs[i]);
                if (recentDocuments[i] == null) {
                    recentDocuments[i] = new JMenuItem();
                }
                recentDocuments[i].setText(i + 1 + ": " + shortenedFileName);
                if (recentDocuments[i].getText().equals(i + 1 + ": ")) {
                    recentDocuments[i].setVisible(false);
                } else {
                    recentDocuments[i].setVisible(true);
                }

                recentDocuments[i].setName(recentDocs[i]);
            }
        }
    }

    private String getShortenedFileName(final String fileNameToAdd) {
        final int maxChars = 30;

        if (fileNameToAdd.length() <= maxChars) {
            return fileNameToAdd;
        }

        final StringTokenizer st = new StringTokenizer(fileNameToAdd, "\\/");

        final int noOfTokens = st.countTokens();
        final String[] arrayedFile = new String[noOfTokens];
        for (int i = 0; i < noOfTokens; i++) {
            arrayedFile[i] = st.nextToken();
        }

        final String filePathBody = fileNameToAdd.substring(arrayedFile[0].length(),
                fileNameToAdd.length() - arrayedFile[noOfTokens - 1].length());

        final StringBuilder builder = new StringBuilder(filePathBody);

        for (int i = noOfTokens - 2; i > 0; i--) {
            final int start = builder.lastIndexOf(arrayedFile[i]);

            final int end = start + arrayedFile[i].length();
            builder.replace(start, end, "...");

            if (builder.toString().length() <= maxChars) {
                break;
            }
        }

        return arrayedFile[0] + builder.toString() + arrayedFile[noOfTokens - 1];
    }

    void addToMenu(
            final RecentDocumentType type,
            final JMenu menu) {
        final String[] recentDocs = designerPropertiesFile.getRecentDocuments(type);
        if (recentDocs == null) {
            return;
        }

        final List<String> recentDocuments = Arrays.asList(recentDocs);
        final List<JMenuItem> menuItems = IntStream.range(0, recentDocs.length)
                .mapToObj(i -> createRecentDocumentMenuItem(i, recentDocuments.get(i)))
                .peek(menu::add)
                .collect(toUnmodifiableList());

        fillRecentDocumentList(menuItems, type);

    }

    private JMenuItem createRecentDocumentMenuItem(
            final int position,
            final String recentDocument) {
        final String fileNameToAdd = Objects.requireNonNullElse(recentDocument, "");
        final String shortenedFileName = getShortenedFileName(fileNameToAdd);

        final JMenuItem menuItem = new JMenuItem(position + 1 + ": " + shortenedFileName);

        if (menuItem.getText().equals(position + 1 + ": ")) {
            menuItem.setVisible(false);
        }

        menuItem.setName(fileNameToAdd);
        menuItem.addActionListener(event -> {
            final JMenuItem item = (JMenuItem) event.getSource();
            final String fileName = item.getName();
            // Call OpenCommand or ImportCommand
            //            if (type == RecentDocumentType.RECENT_DES_FILES) {
            //                openDesignerFile(fileName);
            //            } else {
            //                final int importType = aquirePDFImportType();
            //                importPDF(importType, fileName);
            //            }
        });

        return menuItem;
    }

    private void fillRecentDocumentList(
            final List<JMenuItem> menuItems,
            final RecentDocumentType type) {
        final JMenuItem[] recentDocs;
        if (type == RecentDocumentType.RECENT_DES_FILES) {
            recentDocs = recentDesignerDocuments;
        } else {
            recentDocs = recentImportedDocuments;
        }

        IntStream.range(0, menuItems.size())
                .forEach(i -> recentDocs[i] = menuItems.get(i));
    }

}
