package org.pdf.forms.utils.configuration;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.jpedal.examples.simpleviewer.gui.swing.SwingMenuItem;
import org.pdf.forms.gui.IMainFrame;
import org.pdf.forms.gui.commands.CommandListener;
import org.pdf.forms.gui.commands.Commands;
import org.pdf.forms.gui.designer.IDesigner;
import org.pdf.forms.widgets.IWidget;
import org.pdf.forms.widgets.utils.WidgetAlignmentAndOrder;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class MenuConfiguration extends ConfigurationFile{
	
	private CommandListener commandListener;
	
	private List closeItems = new ArrayList();
    private List previewItems = new ArrayList();

	private JMenu recentDesignerFiles, recentImportedFiles;
	
	private List alignAndOrderMenuItems = new ArrayList();
	private List groupMenuItems = new ArrayList();

	private Map windowNames = new HashMap();
	
	private IDesigner designer;

	private IMainFrame mainFrame;
	
	private JMenu[] menus;
		
	public MenuConfiguration(CommandListener commandListener, IDesigner designer, IMainFrame mainFrame) {
		super("menus.xml");
		
		this.commandListener = commandListener;
		this.designer = designer;
		this.mainFrame = mainFrame;
		
		menus = generateMenus();
	}

	public JMenu[] getMenus(){
		return menus;
	}
	
    protected void writeDefaultConfiguration() throws Exception {
    	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();

        doc = db.newDocument();

        Element menuConfigurationElement = doc.createElement("menu_configuration");
        doc.appendChild(menuConfigurationElement);
        
        writeDefaultFileMenuConfiguration(menuConfigurationElement);
        writeDefaultInsertMenuConfiguration(menuConfigurationElement);
        writeDefaultLayoutMenuConfiguration(menuConfigurationElement);
        writeDefaultWindowMenuConfiguration(menuConfigurationElement);
        writeDefaultHelpMenuConfiguration(menuConfigurationElement);
	}

    private void writeDefaultHelpMenuConfiguration(Element menuConfigurationElement) {
		Element windowElement = doc.createElement("menu");
        windowElement.setAttribute("name", "Help");
        windowElement.setAttribute("visible", "true");
        menuConfigurationElement.appendChild(windowElement);
		
		addItemToXMLTree(windowElement, "Visit Website", "WEBSITE", "true");
		addItemToXMLTree(windowElement, "About", "ABOUT", "true");
	}
    
    private void writeDefaultWindowMenuConfiguration(Element menuConfigurationElement) {
		Element windowElement = doc.createElement("menu");
        windowElement.setAttribute("name", "Window");
        windowElement.setAttribute("visible", "true");
        menuConfigurationElement.appendChild(windowElement);
		
		addItemToXMLTree(windowElement, "Toolbars", "TOOLBARS", "true");
		addItemToXMLTree(windowElement, "Script Editor", "SCRIPT_EDITOR", "true");
		addItemToXMLTree(windowElement, "Seperator", "SEPERATOR", "true");
		addItemToXMLTree(windowElement, "Hierarchy", "HIERARCHY", "true");
		addItemToXMLTree(windowElement, "Seperator", "SEPERATOR", "true");
		addItemToXMLTree(windowElement, "Library", "LIBRARY", "true");
		addItemToXMLTree(windowElement, "Seperator", "SEPERATOR", "true");
		addItemToXMLTree(windowElement, "Properties", "PROPERTIES", "true");
		addItemToXMLTree(windowElement, "Layout", "LAYOUT", "true");
		addItemToXMLTree(windowElement, "Border", "BORDER", "true");
		addItemToXMLTree(windowElement, "Object", "OBJECT", "true");
		addItemToXMLTree(windowElement, "Seperator", "SEPERATOR", "true");
		addItemToXMLTree(windowElement, "Font", "FONT", "true");
		addItemToXMLTree(windowElement, "Paragraph", "PARAGRAPH", "true");
	}
    
    private void writeDefaultLayoutMenuConfiguration(Element menuConfigurationElement) {
		Element layoutElement = doc.createElement("menu");
        layoutElement.setAttribute("name", "Layout");
        layoutElement.setAttribute("visible", "true");
        menuConfigurationElement.appendChild(layoutElement);
		
		addItemToXMLTree(layoutElement, "Align", "ALIGN", "true");
		addItemToXMLTree(layoutElement, "Seperator", "SEPERATOR", "true");
		addItemToXMLTree(layoutElement, "Group", "GROUP", "true");
		addItemToXMLTree(layoutElement, "Ungroup", "UNGROUP", "true");
		addItemToXMLTree(layoutElement, "Seperator", "SEPERATOR", "true");
		addItemToXMLTree(layoutElement, "Bring to Front", "BRING_TO_FRONT", "true");
		addItemToXMLTree(layoutElement, "Send to Back", "SEND_TO_BACK", "true");
		addItemToXMLTree(layoutElement, "Bring Forwards", "BRING_FORWARDS", "true");
		addItemToXMLTree(layoutElement, "Send Backwards", "SEND_BACKWARDS", "true");
	}
    
    private void writeDefaultInsertMenuConfiguration(Element menuConfigurationElement) {
		Element insertElement = doc.createElement("menu");
        insertElement.setAttribute("name", "Insert");
        insertElement.setAttribute("visible", "true");
        menuConfigurationElement.appendChild(insertElement);
		
		addItemToXMLTree(insertElement, "Insert Page", "INSERT_PAGE", "true");
		addItemToXMLTree(insertElement, "Remove Page", "REMOVE_PAGE", "true");
	}
    
	private void writeDefaultFileMenuConfiguration(Element menuConfigurationElement) {
		Element fileElement = doc.createElement("menu");
        fileElement.setAttribute("name", "File");
        fileElement.setAttribute("visible", "true");
        menuConfigurationElement.appendChild(fileElement);
		
		addItemToXMLTree(fileElement, "New", "NEW", "true");
		addItemToXMLTree(fileElement, "Open Designer File", "OPEN", "true");
		addItemToXMLTree(fileElement, "Recently Designer Files", "RECENT_OPEN", "true");
		addItemToXMLTree(fileElement, "Close", "CLOSE", "true");
		addItemToXMLTree(fileElement, "Seperator", "SEPERATOR", "true");
		addItemToXMLTree(fileElement, "Import PDF Document", "IMPORT", "true");
		addItemToXMLTree(fileElement, "Recently Imported PDF Documents", "RECENT_IMPORT", "true");
		addItemToXMLTree(fileElement, "Seperator", "SEPERATOR", "true");
		addItemToXMLTree(fileElement, "Save Designer File", "SAVE_FILE", "true");
		addItemToXMLTree(fileElement, "Save Designer File As", "SAVE_FILE_AS", "true");
		addItemToXMLTree(fileElement, "Seperator", "SEPERATOR", "true");
		addItemToXMLTree(fileElement, "Publish PDF Document", "PUBLISH", "true");
		addItemToXMLTree(fileElement, "Seperator", "SEPERATOR", "true");
		addItemToXMLTree(fileElement, "Font Management", "FONT_MANAGEMENT", "true");
		addItemToXMLTree(fileElement, "Seperator", "SEPERATOR", "true");
		addItemToXMLTree(fileElement, "Exit", "EXIT", "true");
	}

	private void addItemToXMLTree(Element element, String name, String command, String visible) {
		Element item = doc.createElement("item");
		item.setAttribute("name", name);
        item.setAttribute("visible", visible);
        item.setAttribute("command", command);
        element.appendChild(item);
	}

	private JMenu[] generateMenus() {
		NodeList nl = doc.getElementsByTagName("menu");
		
		List menus = new ArrayList();
		for (int i = 0; i < nl.getLength(); i++) {
			Element menuElement = (Element) nl.item(i);
			
			String visible = menuElement.getAttribute("visible");
			boolean isVisible = visible.toLowerCase().equals("true");
			if(!isVisible)
				continue;
			
			String menuName = menuElement.getAttribute("name");
			JMenu menu = new JMenu(menuName);
			
			NodeList menuItems = menuElement.getElementsByTagName("item");
			for (int j = 0; j < menuItems.getLength(); j++) {
				Element itemElement = (Element) menuItems.item(j);
				
				visible = itemElement.getAttribute("visible");
				isVisible = visible.toLowerCase().equals("true");
				if(!isVisible)
					continue;
				
				String itemName = itemElement.getAttribute("name");
				if(itemName.equals("Seperator"))
					menu.add(new JSeparator());
				else {
					String command = itemElement.getAttribute("command");
					//System.out.println("Adding "+itemName+" command ="+ command);
					addMenuItemMain(itemName, command.hashCode(), menu);
				}
			}
			menus.add(menu);
		}
		
		return (JMenu[]) menus.toArray(new JMenu[menus.size()]);
	}
	
	protected JMenuItem addBasicMenuItem(String text, int command, JMenu menu) {
        SwingMenuItem item = new SwingMenuItem(text);
        item.setID(command);
        item.addActionListener(commandListener);
        menu.add(item);
        
        return item;
    }
	
	private JMenu addRecentFilesMenu(String text, JMenu menu) {
    	JMenu item = new JMenu(text);
    	menu.add(item);
    	return item;
    }
	
	private JMenuItem addAlignAndOrderMenuItems(String text, final String type, String url, JMenu menu) {
		JMenuItem item = new JMenuItem(text);
	    item.setIcon(new ImageIcon(getClass().getResource(url)));
	    item.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				WidgetAlignmentAndOrder.alignAndOrder(designer, type);
			}
	    });
	    menu.add(item);
	    
	    return item;
	}
		
	private JMenuItem addGoupMenuItems(String text, String url, int command, JMenu menu) {
        SwingMenuItem item = new SwingMenuItem(text);
        item.setIcon(new ImageIcon(getClass().getResource(url)));
        item.setID(command);
        item.addActionListener(commandListener);
        menu.add(item);
        
        return item;
    }
	
	
	private JCheckBoxMenuItem addMenuItem(final String text) {
        final JCheckBoxMenuItem menuItem = new JCheckBoxMenuItem(text, true);
        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mainFrame.setDockableVisible(text, menuItem.isSelected());
            }
        });
        windowNames.put(text, menuItem);

        return menuItem;
    }
	
	private void addMenuItemMain(String name, int command, JMenu menu) {
		switch (command) {
		
		case Commands.NEW:
			previewItems.add(addBasicMenuItem(name, command, menu));
			break;
		case Commands.OPEN:
			previewItems.add(addBasicMenuItem(name, command, menu));
			break;
		case Commands.RECENT_OPEN:
			recentDesignerFiles = addRecentFilesMenu(name, menu);
			previewItems.add(recentDesignerFiles);
			break;
		case Commands.CLOSE:
			closeItems.add(addBasicMenuItem(name, command, menu));
			break;
		case Commands.IMPORT:
			closeItems.add(addBasicMenuItem(name, command, menu));
			break;
		case Commands.RECENT_IMPORT:
			recentImportedFiles = addRecentFilesMenu(name, menu);
			previewItems.add(recentImportedFiles);
			break;
		case Commands.SAVE_FILE:
			closeItems.add(addBasicMenuItem(name, command, menu));
			break;
		case Commands.SAVE_FILE_AS:
			closeItems.add(addBasicMenuItem(name, command, menu));
			break;
		case Commands.PUBLISH:
			closeItems.add(addBasicMenuItem(name, command, menu));
			break;
		case Commands.FONT_MANAGEMENT:
			closeItems.add(addBasicMenuItem(name, command, menu));
			break;
		case Commands.EXIT:
			addBasicMenuItem(name, command, menu);
			break;
		case Commands.INSERT_PAGE:
			previewItems.add(addBasicMenuItem(name, command, menu));
			break;
		case Commands.REMOVE_PAGE:
			previewItems.add(addBasicMenuItem(name, command, menu));
			break;
		case Commands.ALIGN:
			
			JMenu align = new JMenu(name);
			menu.add(align);

	        String[] alignButtons = WidgetAlignmentAndOrder.getAlignButtons();
	        for (int i = 0; i < alignButtons.length; i++) {
	            final String url = alignButtons[i];
	            if (url.equals("Seperator")) {

	            } else {
	                String[] splitFilename = url.split("/");
	                final String type = splitFilename[splitFilename.length - 1].split("\\.")[0];

	                alignAndOrderMenuItems.add(addAlignAndOrderMenuItems(type, type, url, align));
	            }
	        }
			
			break;
		case Commands.GROUP:
			groupMenuItems.add(addGoupMenuItems(name, "/org/pdf/forms/res/Grouped.gif", Commands.GROUP, menu));
			break;
		case Commands.UNGROUP:
			groupMenuItems.add(addGoupMenuItems(name, "/org/pdf/forms/res/Ungrouped.gif", Commands.UNGROUP, menu));
			break;
		case Commands.BRING_TO_FRONT:
			alignAndOrderMenuItems.add(addAlignAndOrderMenuItems(name, WidgetAlignmentAndOrder.BRING_TO_FRONT,
                    "/org/pdf/forms/res/Bring to Front.gif", menu));
			break;
		case Commands.SEND_TO_BACK:
			alignAndOrderMenuItems.add(addAlignAndOrderMenuItems(name, WidgetAlignmentAndOrder.SEND_TO_BACK,
                    "/org/pdf/forms/res/Send to Back.gif", menu));
			break;
		case Commands.BRING_FORWARDS:
			alignAndOrderMenuItems.add(addAlignAndOrderMenuItems(name, WidgetAlignmentAndOrder.BRING_FORWARDS,
                    "/org/pdf/forms/res/Bring Forwards.gif", menu));
			break;
		case Commands.SEND_BACKWARDS:
			alignAndOrderMenuItems.add(addAlignAndOrderMenuItems(name, WidgetAlignmentAndOrder.SEND_BACKWARDS,
                    "/org/pdf/forms/res/Send Backwards.gif", menu));
			break;
		case Commands.TOOLBARS:
			menu.add(addMenuItem(name));
			break;
		case Commands.SCRIPT_EDITOR:
			menu.add(addMenuItem(name));
			break;
		case Commands.HIERARCHY:
			menu.add(addMenuItem(name));
			break;
		case Commands.LIBRARY:
			menu.add(addMenuItem(name));
			break;
		case Commands.PROPERTIES:
			menu.add(addMenuItem(name));
			break;
		case Commands.LAYOUT:
			menu.add(addMenuItem(name));
			break;
		case Commands.BORDER:
			menu.add(addMenuItem(name));
			break;
		case Commands.OBJECT:
			menu.add(addMenuItem(name));
			break;
		case Commands.FONT:
			menu.add(addMenuItem(name));
			break;
		case Commands.PARAGRAPH:
			menu.add(addMenuItem(name));
			break;
		case Commands.WEBSITE:
			addBasicMenuItem(name, command, menu);
			break;
		case Commands.ABOUT:
			addBasicMenuItem(name, command, menu);
			break;
        }
    }
	
	public void setCloseState(boolean state) {
        for (Iterator it = closeItems.iterator(); it.hasNext();) {
            JMenuItem menuItem = (JMenuItem) it.next();
            menuItem.setEnabled(state);
        }
    }

    public void setState(boolean state) {
        setCloseState(state);

        for (Iterator it = previewItems.iterator(); it.hasNext();) {
            JMenuItem menuItem = (JMenuItem) it.next();
            menuItem.setEnabled(state);
        }
    }

	public JMenu getRecentDesignerFilesMenu() {
		return recentDesignerFiles;
	}

	public JMenu getRecentImportedFilesMenu() {
		return recentImportedFiles;
	}
	
	public void setProperties(Set widgets) {
		if(widgets.isEmpty()){
			setItemsEnabled(false);
			((JMenuItem)groupMenuItems.get(0)).setEnabled(false);
			((JMenuItem)groupMenuItems.get(1)).setEnabled(false);
		}else{
			setItemsEnabled(true);
			
			if (widgets.size() == 1 && ((IWidget) widgets.iterator().next()).getType() == IWidget.GROUP){
				((JMenuItem)groupMenuItems.get(0)).setEnabled(false);
				((JMenuItem)groupMenuItems.get(1)).setEnabled(true);
			}else if(widgets.size() > 1){
				((JMenuItem)groupMenuItems.get(0)).setEnabled(true);
				((JMenuItem)groupMenuItems.get(1)).setEnabled(false);
			}
		}
	}
	
	private void setItemsEnabled(boolean enabled) {
		for (Iterator it = alignAndOrderMenuItems.iterator(); it.hasNext();) {
			JMenuItem menu = (JMenuItem) it.next();
			menu.setEnabled(enabled);
		}
	}
	
	public void setDockableVisible(String dockableName, boolean visible) {
        JCheckBoxMenuItem menuItem = (JCheckBoxMenuItem) windowNames.get(dockableName);
        menuItem.setSelected(visible);
    }
}
