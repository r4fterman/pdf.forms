/*
 * ===========================================
 * PDF Forms Designer
 * ===========================================
 * <p>
 * Project Info:  http://pdfformsdesigne.sourceforge.net
 * (C) Copyright 2006-2008..
 * Lead Developer: Simon Barnett (n6vale@googlemail.com)
 * <p>
 * This file is part of the PDF Forms Designer
 * <p>
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * <p>
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * <p>
 * <p>
 * <p>
 * ---------------
 * Commands.java
 * ---------------
 */
package org.pdf.forms.gui.commands;

import java.util.Map;
import java.util.StringTokenizer;

import javax.swing.JMenu;

import org.pdf.forms.gui.IMainFrame;

public class Commands {

    public static final int NEW = 77184;
    public static final int OPEN = 2432586;
    public static final int RECENT_OPEN = 159636910;
    public static final int CLOSE = 64218584;
    public static final int IMPORT = -2131466331;
    public static final int RECENT_IMPORT = -1381966327;
    public static final int SAVE_FILE = -1295101186;
    public static final int SAVE_FILE_AS = -668118765;
    public static final int PUBLISH = 482617583;
    public static final int FONT_MANAGEMENT = 1780825651;
    public static final int EXIT = 2142494;
    public static final int INSERT_PAGE = 2079238229;
    public static final int REMOVE_PAGE = 1888835946;
    public static final int ALIGN = 62365413;
    public static final int GROUP = 68091487;
    public static final int UNGROUP = 429566822;
    public static final int BRING_TO_FRONT = 88902514;
    public static final int SEND_TO_BACK = -452041100;
    public static final int BRING_FORWARDS = 1102378619;
    public static final int SEND_BACKWARDS = 122730713;
    public static final int TOOLBARS = -200551336;
    public static final int SCRIPT_EDITOR = 947938145;
    public static final int HIERARCHY = 606773781;
    public static final int LIBRARY = 884191387;
    public static final int PROPERTIES = -440960717;
    public static final int LAYOUT = -2056392918;
    public static final int BORDER = 1964992556;
    public static final int OBJECT = -1970038977;
    public static final int FONT = 2163791;
    public static final int PARAGRAPH = 440916302;
    public static final int WEBSITE = 1942318203;
    public static final int ABOUT = 62073709;
    public static final int ZOOM_IN = 2759635;
    public static final int ZOOM = 608001297;
    public static final int ZOOM_OUT = 1668177090;

    // currently unused commands
    private static final int ADD_SELECTION_TO_LIBRARY = 73709620;
    private static final int BUG_REPORT = 96207370;

    private final RecentDocumentFileList recentDocumentFileList;
    private final Map<Integer, Command> commandMap;

    public Commands(
            final IMainFrame mainFrame,
            final Version version) {
        recentDocumentFileList = new RecentDocumentFileList();

        commandMap = Map.ofEntries(
                Map.entry(NEW, new NewCommand(mainFrame, version)),
                Map.entry(OPEN, new OpenCommand(mainFrame, recentDocumentFileList, version)),
                Map.entry(CLOSE, new CloseCommand(mainFrame, version)),
                Map.entry(IMPORT, new ImportCommand(mainFrame, recentDocumentFileList, version)),
                Map.entry(SAVE_FILE, new SaveCommand(mainFrame, version)),
                Map.entry(SAVE_FILE_AS, new SaveAsCommand(mainFrame, version)),
                Map.entry(PUBLISH, new PublishCommand(mainFrame)),
                Map.entry(FONT_MANAGEMENT, new ManageFontsCommand(mainFrame)),
                Map.entry(EXIT, new ExitCommand()),
                Map.entry(ZOOM, new ZoomCommand(mainFrame)),
                Map.entry(ZOOM_IN, new ZoomInCommand(mainFrame)),
                Map.entry(ZOOM_OUT, new ZoomOutCommand(mainFrame)),
                Map.entry(INSERT_PAGE, new InsertPageCommand(mainFrame)),
                Map.entry(REMOVE_PAGE, new RemovePageCommand(mainFrame)),
                Map.entry(ADD_SELECTION_TO_LIBRARY, new AddSelectionToLibraryCommand(mainFrame)),
                Map.entry(GROUP, new GroupElementsCommand(mainFrame)),
                Map.entry(UNGROUP, new UnGroupElementsCommand(mainFrame)),
                Map.entry(WEBSITE, new VisitWebsiteCommand(mainFrame)),
                Map.entry(ABOUT, new ShowAboutCommand(mainFrame)),
                Map.entry(BUG_REPORT, new FileBugReportCommand(mainFrame))
        );
    }

    public void executeCommand(final int id) {
        commandMap.getOrDefault(id, new NoopCommand()).execute();
    }

    public void recentDocumentsOption(
            final RecentDocumentType type,
            final JMenu menu) {
        recentDocumentFileList.addToMenu(type, menu);
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

}
