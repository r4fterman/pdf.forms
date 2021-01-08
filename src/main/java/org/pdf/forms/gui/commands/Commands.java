package org.pdf.forms.gui.commands;

import static java.util.Map.entry;

import java.util.Map;

import org.pdf.forms.Configuration;
import org.pdf.forms.fonts.FontHandler;
import org.pdf.forms.gui.IMainFrame;
import org.pdf.forms.utils.DesignerPropertiesFile;
import org.pdf.forms.widgets.utils.WidgetFactory;

public class Commands {

    public static final int NONE = 0;

    public static final int NEW = 1;
    public static final int OPEN = 2;
    public static final int RECENT_OPEN = 3;
    public static final int CLOSE = 4;
    public static final int IMPORT = 5;
    public static final int RECENT_IMPORT = 6;
    public static final int SAVE_FILE = 7;
    public static final int SAVE_FILE_AS = 8;
    public static final int PUBLISH = 9;
    public static final int FONT_MANAGEMENT = 10;
    public static final int EXIT = 11;

    public static final int INSERT_PAGE = 12;
    public static final int REMOVE_PAGE = 13;

    public static final int ALIGN = 14;
    public static final int GROUP = 15;
    public static final int UNGROUP = 16;
    public static final int BRING_TO_FRONT = 17;
    public static final int SEND_TO_BACK = 18;
    public static final int BRING_FORWARDS = 19;
    public static final int SEND_BACKWARDS = 20;

    public static final int TOOLBARS = 21;
    public static final int SCRIPT_EDITOR = 22;
    public static final int HIERARCHY = 23;
    public static final int LIBRARY = 24;
    public static final int PROPERTIES = 25;
    public static final int LAYOUT = 26;
    public static final int BORDER = 27;
    public static final int OBJECT = 28;
    public static final int FONT = 29;
    public static final int PARAGRAPH = 30;

    public static final int WEBSITE = 31;
    public static final int ABOUT = 32;

    public static final int ZOOM_IN = 33;
    public static final int ZOOM = 34;
    public static final int ZOOM_OUT = 35;
    public static final int ADD_SELECTION_TO_LIBRARY = 36;
    public static final int BUG_REPORT = 37;

    private static final Map<String, Integer> COMMAND_LOOKUP = Map.ofEntries(
            entry("NEW", NEW),
            entry("OPEN", OPEN),
            entry("RECENT_OPEN", RECENT_OPEN),
            entry("CLOSE", CLOSE),
            entry("IMPORT", IMPORT),
            entry("RECENT_IMPORT", RECENT_IMPORT),
            entry("SAVE_FILE", SAVE_FILE),
            entry("SAVE_FILE_AS", SAVE_FILE_AS),
            entry("PUBLISH", PUBLISH),
            entry("FONT_MANAGEMENT", FONT_MANAGEMENT),
            entry("EXIT", EXIT),
            entry("INSERT_PAGE", INSERT_PAGE),
            entry("REMOVE_PAGE", REMOVE_PAGE),
            entry("ALIGN", ALIGN),
            entry("GROUP", GROUP),
            entry("UNGROUP", UNGROUP),
            entry("BRING_TO_FRONT", BRING_TO_FRONT),
            entry("SEND_TO_BACK", SEND_TO_BACK),
            entry("BRING_FORWARDS", BRING_FORWARDS),
            entry("SEND_BACKWARDS", SEND_BACKWARDS),
            entry("TOOLBARS", TOOLBARS),
            entry("SCRIPT_EDITOR", SCRIPT_EDITOR),
            entry("HIERARCHY", HIERARCHY),
            entry("LIBRARY", LIBRARY),
            entry("PROPERTIES", PROPERTIES),
            entry("LAYOUT", LAYOUT),
            entry("BORDER", BORDER),
            entry("OBJECT", OBJECT),
            entry("FONT", FONT),
            entry("PARAGRAPH", PARAGRAPH),
            entry("WEBSITE", WEBSITE),
            entry("ABOUT", ABOUT),
            entry("ZOOM_IN", ZOOM_IN),
            entry("ZOOM_OUT", ZOOM_OUT),
            entry("ZOOM", ZOOM),
            entry("ADD_SELECTION_TO_LIBRARY", ADD_SELECTION_TO_LIBRARY),
            entry("BUG_REPORT", BUG_REPORT)
    );

    public static int fromValue(final String command) {
        return COMMAND_LOOKUP.getOrDefault(command, NONE);
    }

    private final Map<Integer, Command> commandMap;

    public Commands(
            final IMainFrame mainFrame,
            final String version,
            final FontHandler fontHandler,
            final WidgetFactory widgetFactory,
            final Configuration configuration,
            final DesignerPropertiesFile designerPropertiesFile) {
        commandMap = Map.ofEntries(
                entry(NEW, new NewPdfCommand(mainFrame, version)),
                entry(OPEN, new OpenDesignerFileCommand(mainFrame, version, widgetFactory, designerPropertiesFile)),
                entry(CLOSE, new ClosePdfCommand(mainFrame, version)),
                entry(IMPORT, new ImportPdfCommand(mainFrame, version, widgetFactory, designerPropertiesFile)),
                entry(SAVE_FILE, new SaveDesignerFileCommand(mainFrame, version)),
                entry(SAVE_FILE_AS, new SaveDesignerFileAsCommand(mainFrame, version)),
                entry(PUBLISH, new PublishPdfCommand(mainFrame, fontHandler)),
                entry(FONT_MANAGEMENT, new FontManagementCommand(mainFrame, fontHandler, designerPropertiesFile)),
                entry(EXIT, new ExitCommand(mainFrame)),
                entry(ZOOM_IN, new ZoomInCommand(mainFrame)),
                entry(ZOOM_OUT, new ZoomOutCommand(mainFrame)),
                entry(ZOOM, new ZoomCommand(mainFrame)),
                entry(INSERT_PAGE, new InsertPageCommand(mainFrame)),
                entry(REMOVE_PAGE, new RemovePageCommand(mainFrame)),
                entry(ADD_SELECTION_TO_LIBRARY, new AddSelectionToLibraryCommand(mainFrame, configuration.getConfigDirectory())),
                entry(GROUP, new GroupCommand(mainFrame)),
                entry(UNGROUP, new UnGroupCommand(mainFrame)),
                entry(WEBSITE, new VisitWebsiteCommand()),
                entry(ABOUT, new ShowAboutPanelCommand(mainFrame)),
                entry(BUG_REPORT, new BugReportCommand(mainFrame))
        );
    }

    public void executeCommand(final int id) {
        commandMap.getOrDefault(id, new NoopCommand()).execute();
    }

}
