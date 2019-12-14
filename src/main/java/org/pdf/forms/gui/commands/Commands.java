package org.pdf.forms.gui.commands;

import static java.util.Map.entry;

import java.util.Map;

import org.pdf.forms.fonts.FontHandler;
import org.pdf.forms.gui.IMainFrame;
import org.pdf.forms.widgets.utils.WidgetFactory;

public class Commands {

    public static final int NEW = 77184; //"NEW".hashCode();
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
    public static final int ADD_SELECTION_TO_LIBRARY = 1778177090;
    public static final int BUG_REPORT = 1888177090;
    public static final int NONE = 0;

    private final Map<Integer, Command> commandMap;

    public Commands(
            final IMainFrame mainFrame,
            final String version,
            final FontHandler fontHandler,
            final WidgetFactory widgetFactory) {
        commandMap = Map.ofEntries(
                entry(NEW, new NewPdfCommand(mainFrame, version)),
                entry(OPEN, new OpenDesignerFileCommand(mainFrame, version, widgetFactory)),
                entry(CLOSE, new ClosePdfCommand(mainFrame, version)),
                entry(IMPORT, new ImportPdfCommand(mainFrame, version, widgetFactory)),
                entry(SAVE_FILE, new SaveDesignerFileCommand(mainFrame, version)),
                entry(SAVE_FILE_AS, new SaveDesignerFileAsCommand(mainFrame, version)),
                entry(PUBLISH, new PublishPdfCommand(mainFrame, fontHandler)),
                entry(FONT_MANAGEMENT, new FontManagementCommand(mainFrame, fontHandler)),
                entry(EXIT, () -> System.exit(0)),
                entry(ZOOM_IN, new ZoomInCommand(mainFrame)),
                entry(ZOOM_OUT, new ZoomOutCommand(mainFrame)),
                entry(ZOOM, new ZoomCommand(mainFrame)),
                entry(INSERT_PAGE, new InsertPageCommand(mainFrame)),
                entry(REMOVE_PAGE, new RemovePageCommand(mainFrame)),
                entry(ADD_SELECTION_TO_LIBRARY, new AddSelectionToLibraryCommand(mainFrame)),
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
