/**
* ===========================================
* PDF Forms Designer
* ===========================================
*
* Project Info:  http://pdfformsdesigne.sourceforge.net
* (C) Copyright 2006-2008..
* Lead Developer: Simon Barnett (n6vale@googlemail.com)
*
* 	This file is part of the PDF Forms Designer
*
    This library is free software; you can redistribute it and/or
    modify it under the terms of the GNU General Public
    License as published by the Free Software Foundation; either
    version 2.1 of the License, or (at your option) any later version.

    This library is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    General Public License for more details.

    You should have received a copy of the GNU General Public
    License along with this library; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA


*
* ---------------
* Commands.java
* ---------------
*/
package org.pdf.forms.gui.commands;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.jpedal.PdfDecoder;
import org.jpedal.examples.simpleviewer.utils.FileFilterer;
import org.jpedal.exception.PdfException;
import org.jpedal.objects.PdfPageData;
import org.jpedal.utils.BrowserLauncher;
import org.jpedal.utils.SwingWorker;
import org.pdf.forms.document.FormsDocument;
import org.pdf.forms.document.Page;
import org.pdf.forms.fonts.FontSelector;
import org.pdf.forms.gui.IMainFrame;
import org.pdf.forms.gui.designer.IDesigner;
import org.pdf.forms.gui.designer.gui.DesignerCompound;
import org.pdf.forms.gui.windows.AboutPanel;
import org.pdf.forms.gui.windows.FileFinder;
import org.pdf.forms.gui.windows.PDFImportChooser;
import org.pdf.forms.utils.CustomWidgetsFile;
import org.pdf.forms.utils.DesignerPropertiesFile;
import org.pdf.forms.utils.XMLUtils;
import org.pdf.forms.widgets.ButtonGroup;
import org.pdf.forms.widgets.GroupWidget;
import org.pdf.forms.widgets.IWidget;
import org.pdf.forms.widgets.utils.WidgetFactory;
import org.pdf.forms.widgets.utils.WidgetParser;
import org.pdf.forms.writer.Writer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

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
	
    private final IMainFrame mainFrame;
    private final String version;

    private JMenuItem[] recentDesignerDocuments;
	private JMenuItem[] recentImportedDocuments;
	private int noOfRecentDocs;

    public Commands(
            final IMainFrame mainFrame,
            final String version) {
        this.mainFrame = mainFrame;
        this.version = version;

        noOfRecentDocs = DesignerPropertiesFile.getInstance().getNoRecentDocumentsToDisplay();
        recentDesignerDocuments = new JMenuItem[noOfRecentDocs];
        recentImportedDocuments = new JMenuItem[noOfRecentDocs];
    }

    public void executeCommand(int id) {
		switch (id) {
            case NEW:
            	newPDF(595, 842);
                break;
            case OPEN:
                openDesignerFile();
                break;
            case CLOSE:
                closePDF();
                break;
            case IMPORT:
                importPDF();
                break;
            case SAVE_FILE:
                saveDesignerFile();
                break;
            case SAVE_FILE_AS:
                saveDesignerFileAs();
                break;
            case PUBLISH:
                publishPDF();
                break;
            case FONT_MANAGEMENT:
                fontManagement();
                break;
            case EXIT:
                System.exit(0);
                break;

//            case ZOOM_IN:
//                zoom(mainFrame.getCurrentScaling() * (3d / 2d));
//                break;
//            case ZOOM:
//                zoom(mainFrame.getCurrentSelectedScaling() / 100d);
//                break;
//            case ZOOM_OUT:
//                zoom(mainFrame.getCurrentScaling() * (2d / 3d));
//                break;

            case INSERT_PAGE:
                insertPage(595, 842);
                break;
            case REMOVE_PAGE:
                removePage();
                break;
//            case ADDSELECTIONTOLIBRARY:
//                addSelectionToLibrary();
//                break;

            case GROUP:
                group();
                break;
            case UNGROUP:
                ungroup();
                break;
                
            case WEBSITE:
            	visitWebsite();
            	break;
            	
            case ABOUT:
            	about();
            	break;
//            case BUGREPORT:
//            	
//            	LinkedHashMap pdfFilesAndSizes = mainFrame.getFormsDocument().getPdfFilesUsed();
//            	
//            	LinkedHashMap filesAndSizes = new LinkedHashMap();
//            	Document documentProperties = mainFrame.getFormsDocument().getDocumentProperties();
//            	
//				try {
//					File designerFile = File.createTempFile("bugreport", ".des");
//					designerFile.deleteOnExit();
//					
//					writeXML(documentProperties, designerFile.getAbsolutePath());
//					
//					Double size = Double.valueOf(round((designerFile.length() / 1000d), 1));
//					System.out.println(designerFile.getAbsolutePath());
//					filesAndSizes.put("Designer File", size);
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//
//				filesAndSizes.putAll(pdfFilesAndSizes);
//				System.out.println(filesAndSizes);
//            	
//				JDialog dialog = new JDialog((Frame) mainFrame, "Bug report", true);
//
//				BugReportPanel bugReportPanel = new BugReportPanel(filesAndSizes, dialog);
//            	
//            	dialog.add(bugReportPanel);
//            	dialog.pack();
//            	dialog.setLocationRelativeTo((Component) mainFrame);
//            	dialog.setVisible(true);
//            	
//            	break;
        }
    }

    private void about() {
    	JOptionPane.showMessageDialog((Component) mainFrame, new AboutPanel(), "About", JOptionPane.PLAIN_MESSAGE);
	}

	private void visitWebsite() {
          try {
              BrowserLauncher.openURL("http://pdfformsdesigne.sourceforge.net");
          } catch (IOException e1) {
              JOptionPane.showMessageDialog(null, "Error loading webpage");
              //<start-full><start-demo>
              e1.printStackTrace();
              //<end-demo><end-full>
          }

//      JMenuItem about = new JMenuItem("About");
//      about.addActionListener(new ActionListener() {
//          public void actionPerformed(ActionEvent actionEvent) {
//              JOptionPane.showMessageDialog((Component) mainFrame, new AboutPanel(), "About", JOptionPane.PLAIN_MESSAGE);
//          }
//      });
//      help.add(visitWebSite);
//      help.add(about);
//
//      menubar.add(help);
		
	}

	private void addSelectionToLibrary() {
    	CustomWidgetsFile customWidgetsFile = CustomWidgetsFile.getInstance();
    	boolean finished = false;

    	String name = JOptionPane.showInputDialog((Component) mainFrame, "Enter a name for the new component", "New component name",
    			JOptionPane.QUESTION_MESSAGE);

    	while (!finished) {
    		if (name == null)
    			return;

    		if (customWidgetsFile.isNameTaken(name)) {
    			name = JOptionPane.showInputDialog((Component) mainFrame, "The name you have entered is already taken, please enter another name", "New component name",
    					JOptionPane.WARNING_MESSAGE);
    		} else {
    			finished = true;
    		}
    	}

    	customWidgetsFile.addCustomWidget(name, mainFrame.getDesigner().getSelectedWidgets());
    }

    public void recentDocumentsOption(final String type, JMenu file) {
    	JMenuItem[] recentDocuments;
    	if (type.equals("recentdesfiles")) {
    		recentDocuments = recentDesignerDocuments;
		} else { // "recentpdffiles"
			recentDocuments = recentImportedDocuments;
		}
    	
		DesignerPropertiesFile properties = DesignerPropertiesFile.getInstance();

		String[] recentDocs = properties.getRecentDocuments(type);
		if (recentDocs == null)
			return;

		for (int i = 0; i < noOfRecentDocs; i++) {
			if (recentDocs[i] == null)
				recentDocs[i] = "";

			String fileNameToAdd = recentDocs[i];
			String shortenedFileName = getShortenedFileName(fileNameToAdd);
			
			recentDocuments[i] = new JMenuItem(i + 1 + ": " + shortenedFileName);
			
			if (recentDocuments[i].getText().equals(i + 1 + ": "))
				recentDocuments[i].setVisible(false);
			
			recentDocuments[i].setName(fileNameToAdd);
			recentDocuments[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JMenuItem item = (JMenuItem)e.getSource();
					String fileName = item.getName();

					if (type.equals("recentdesfiles")) {
						openDesignerFile(fileName);
					} else { // "recentpdffiles"
						int importType = aquirePDFImportType();
						
						importPDF(importType, fileName);
					}
				}
			});
			
			file.add(recentDocuments[i]);
		}
	}
    
    private void updateRecentDocuments(String[] recentDocs, String type) {
		if(recentDocs == null)
			return;

		JMenuItem[] recentDocuments;
    	if (type.equals("recentdesfiles")) {
    		recentDocuments = recentDesignerDocuments;
		} else { // "recentpdffiles"
			recentDocuments = recentImportedDocuments;
		}
		
		for (int i = 0; i < recentDocs.length; i++) {
			if (recentDocs[i] != null) {
				String shortenedFileName = getShortenedFileName(recentDocs[i]);

				recentDocuments[i].setText(i + 1 + ": " + shortenedFileName);
				if (recentDocuments[i].getText().equals(i + 1 + ": "))
					recentDocuments[i].setVisible(false);
				else
					recentDocuments[i].setVisible(true);

				recentDocuments[i].setName(recentDocs[i]);
			}
		}
	}
    
    private String getShortenedFileName(String fileNameToAdd) {
		final int maxChars = 30;
		
		if (fileNameToAdd.length() <= maxChars)
			return fileNameToAdd;
		
		StringTokenizer st = new StringTokenizer(fileNameToAdd,"\\/");
		
		int noOfTokens = st.countTokens();
		String[] arrayedFile = new String[noOfTokens];
		for (int i = 0; i < noOfTokens; i++)
			arrayedFile[i] = st.nextToken();
		
		String filePathBody = fileNameToAdd.substring(arrayedFile[0].length(),
				fileNameToAdd.length() - arrayedFile[noOfTokens - 1].length());
		
		StringBuffer sb = new StringBuffer(filePathBody);
		
		for (int i = noOfTokens - 2; i > 0; i--) {
			int start = sb.lastIndexOf(arrayedFile[i]);
			
			int end = start + arrayedFile[i].length();
			sb.replace(start, end, "...");

			if (sb.toString().length() <= maxChars)
				break;
		}
		
		return arrayedFile[0] + sb + arrayedFile[noOfTokens - 1];
	}
    
    private void fontManagement() {
        JDialog dialog = new JDialog((Frame) mainFrame, "Font Management", true);
        FontSelector fs = new FontSelector(mainFrame, dialog);

        dialog.add(fs);
        dialog.pack();
        dialog.setLocationRelativeTo((Frame) mainFrame);
        dialog.setVisible(true);
        
        mainFrame.updateAvailiableFonts();
    }

    private void ungroup() {
        IDesigner designerPanel = mainFrame.getDesigner();

        Set selectedWidgets = designerPanel.getSelectedWidgets();

        IWidget gw = (IWidget) selectedWidgets.iterator().next();

        designerPanel.removeSelectedWidgets();

        List widgetsInGroup = gw.getWidgetsInGroup();
        for (Iterator it = widgetsInGroup.iterator(); it.hasNext();) {
            IWidget widget = (IWidget) it.next();

            designerPanel.addWidget(widget);
        }

        HashSet widgets = new HashSet(widgetsInGroup);

        designerPanel.setSelectedWidgets(widgets);

        designerPanel.getMainFrame().setPropertiesCompound(widgets);
        designerPanel.getMainFrame().setPropertiesToolBar(widgets);

        designerPanel.repaint();
    }

    private void group() {
        IDesigner designerPanel = mainFrame.getDesigner();

        Set selectedWidgets = designerPanel.getSelectedWidgets();

        List widgetsInGroup = new ArrayList();
        GroupWidget gw = new GroupWidget();

        List allWidgets = designerPanel.getWidgets();

        for (Iterator it = allWidgets.iterator(); it.hasNext();) {
            IWidget widget = (IWidget) it.next();

            if (selectedWidgets.contains(widget)) {
                widgetsInGroup.add(widget);
            }
        }

        gw.setWidgetsInGroup(widgetsInGroup);
        designerPanel.addWidget(gw);

        designerPanel.removeSelectedWidgets();

        Set set = new HashSet();
        set.add(gw);
        designerPanel.setSelectedWidgets(set);

        designerPanel.repaint();
    }

    private double round(double number, int decPlaces) {
        double exponential = Math.pow(10, decPlaces);

        number *= exponential;
        number = Math.round(number);
        number /= exponential;

        return number;
    }

    private void zoom(double scaling) {
        if (mainFrame.getDesignerCompoundContent() == DesignerCompound.PREVIEW) {

            mainFrame.setCurrentSelectedScaling(round(scaling * 100, 2));

            DesignerCompound desgnerCompound = mainFrame.getDesignerCompound();
            desgnerCompound.previewZoom(scaling);
        }
        //mainFrame.setScaling(mainFrame.getScaling() * scaling); @scale
    }

    private void publishPDF() {

        File file;
        String fileToSave;
        boolean finished = false;

        while (!finished) {
            JFileChooser chooser = new JFileChooser();
            chooser.addChoosableFileFilter(new FileFilterer(new String[]{"pdf"}, "pdf (*.pdf)"));
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

            int approved = chooser.showSaveDialog(null);
            if (approved == JFileChooser.APPROVE_OPTION) {

                file = chooser.getSelectedFile();
                fileToSave = file.getAbsolutePath();

                if (!fileToSave.endsWith(".pdf")) {
                    fileToSave += ".pdf";
                    file = new File(fileToSave);
                }

                if (file.exists()) {
                    int n = JOptionPane.showConfirmDialog((Component) mainFrame, "The file already exists, are you sure you wish to overwrite?",
                            "File already exists", JOptionPane.YES_NO_OPTION);

                    if (n == 1)
                        continue;
                }

                Writer writer = new Writer(mainFrame);

                int noOfPages = mainFrame.getTotalNoOfPages();
                List[] widgets = new ArrayList[noOfPages];

                FormsDocument documentProperties = mainFrame.getFormsDocument();

                for (int i = 0; i < noOfPages; i++) {
                    widgets[i] = documentProperties.getPage(i + 1).getWidgets();
                }

                writer.write(file, widgets, documentProperties.getDocumentProperties());

                finished = true;
            } else {
                return;
            }
        }
    }

    private void saveDesignerFileAs() {
        Document documentProperties = mainFrame.getFormsDocument().getDocumentProperties();
        saveDesignerFileAs(documentProperties);
    }

    private void saveDesignerFile() {
        Document documentProperties = mainFrame.getFormsDocument().getDocumentProperties();

        String currentDesignerFileName = mainFrame.getCurrentDesignerFileName();
		if (currentDesignerFileName.equals("Untitled")) { // saving for the first time
            saveDesignerFileAs(documentProperties);
        } else { // saving an already saved file
            writeXML(documentProperties, currentDesignerFileName);
        }
    }

    private void saveDesignerFileAs(Document documentProperties) {
        File file;
        String fileToSave;
        boolean finished = false;

        while (!finished) {
            JFileChooser chooser = new JFileChooser();
            chooser.addChoosableFileFilter(new FileFilterer(new String[]{"des"}, "des (*.des)"));
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

            int approved = chooser.showSaveDialog(null);
            if (approved == JFileChooser.APPROVE_OPTION) {

                file = chooser.getSelectedFile();
                fileToSave = file.getAbsolutePath();

                if (!fileToSave.endsWith(".des")) {
                    fileToSave += ".des";
                    file = new File(fileToSave);
                }

                if (file.exists()) {
                    int n = JOptionPane.showConfirmDialog((Component) mainFrame, "The file already exists, are you sure you wish to overwrite?",
                            "File already exists", JOptionPane.YES_NO_OPTION);

                    if (n == 1)
                        continue;
                }

                mainFrame.setCurrentDesignerFileName(fileToSave);

                writeXML(documentProperties, mainFrame.getCurrentDesignerFileName());

                mainFrame.setTitle(fileToSave + " - PDF Forms Designer Version " + version);

                finished = true;
            } else {
                return;
            }
        }
    }

    private void writeXML(Document documentProperties, String fileName) {
//        try {
//            InputStream stylesheet = this.getClass().getResourceAsStream("/org/jpedal/examples/simpleviewer/res/xmlstyle.xslt");
//
//            TransformerFactory transformerFactory = TransformerFactory.newInstance();
//            Transformer transformer = transformerFactory.newTransformer(new StreamSource(stylesheet));
//            transformer.transform(new DOMSource(documentProperties), new StreamResult(mainFrame.getCurrentDesignerFileName()));
//        } catch (TransformerException e) {
//            e.printStackTrace();
//        }

        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
//            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            //initialize StreamResult with File object to save to file
            transformer.transform(new DOMSource(documentProperties), new StreamResult(fileName));
        } catch (TransformerException e) {
            e.printStackTrace();
        }

    }

    private void newPDF(int width, int height) {
        closePDF();

        mainFrame.setCurrentDesignerFileName("Untitled");
        mainFrame.setTitle("Untitled - PDF Forms Designer Version " + version);

        setPanelsState(true);

        mainFrame.setFormsDocument(new FormsDocument(version));

        insertPage(width, height);
    }

    private void setPanelsState(boolean state) {
        mainFrame.setPanelsState(state);
    }

    private void openDesignerFile() {
        final JFileChooser chooser = new JFileChooser();

        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        String[] des = new String[]{"des"};
        chooser.addChoosableFileFilter(new FileFilterer(des, "des (*.des)"));

        final int state = chooser.showOpenDialog((Component) mainFrame);

        final File fileToOpen = chooser.getSelectedFile();

        if (fileToOpen != null && state == JFileChooser.APPROVE_OPTION) {
            openDesignerFile(fileToOpen.getAbsolutePath());
        }
    }

	public void openDesignerFile(final String designerFileToOpen) {
		closePDF();

		setPanelsState(true);

		mainFrame.setCurrentDesignerFileName(designerFileToOpen);

		try {
		    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		    DocumentBuilder db = dbf.newDocumentBuilder();

            String fileName = mainFrame.getCurrentDesignerFileName();
            Document designerDocumentProperties;
            if (fileName.startsWith("http:") || fileName.startsWith("file:"))
                designerDocumentProperties = db.parse(fileName);
            else
                designerDocumentProperties = db.parse(new File(fileName));

            Element root = designerDocumentProperties.getDocumentElement();

		    mainFrame.setFormsDocument(new FormsDocument(root));

		    List pages = XMLUtils.getElementsFromNodeList(root.getElementsByTagName("page"));

		    Map changedFiles = getChangedPdfFileLocations(pages);

		    for (Iterator it = pages.iterator(); it.hasNext();) {
		        Element page = (Element) it.next();
		        
		        String pageType = XMLUtils.getAttributeFromChildElement(page, "pagetype");
		        String pageName = XMLUtils.getAttributeFromChildElement(page, "pagename");

		        Element pageData = (Element) page.getElementsByTagName("pagedata").item(0);

		        String value1 = XMLUtils.getAttributeByIndex(pageData, 0);
		        int value2 = Integer.parseInt(XMLUtils.getAttributeByIndex(pageData, 1));

		        Page newPage;
		        if (pageType.equals("pdfpage")) { // PDF page
		            String pdfFileToUse = (String) changedFiles.get(value1);
		            //todo check for skiped PDF files
		            newPage = new Page(pageName, pdfFileToUse, value2);
		        } else { // simple page
		            newPage = new Page(pageName, Integer.parseInt(value1), value2);
		        }

                /** add radio button groups to page */
                addButtonGroupsToPage(page, newPage, IWidget.RADIO_BUTTON);

                /** add check box groups to page */
                addButtonGroupsToPage(page, newPage, IWidget.CHECK_BOX);

				mainFrame.setCurrentPage(mainFrame.getCurrentPage() + 1);

				addPage(mainFrame.getCurrentPage(), newPage);

				List widgets = getWidgetsFromXMLElement(page);

		        for (Iterator iter = widgets.iterator(); iter.hasNext();) {
		            IWidget widget = (IWidget) iter.next();
		            mainFrame.addWidgetToHierarchy(widget);
		        }

		        newPage.setWidgets(widgets);
		    }

		} catch (Exception e) {
		    e.printStackTrace();
		}

		mainFrame.setCurrentPage(1);

		mainFrame.displayPage(mainFrame.getCurrentPage());

		setTotalPages();

		mainFrame.setTitle(mainFrame.getCurrentDesignerFileName() + " - PDF Forms Designer Version " + version);
		
		DesignerPropertiesFile properties = DesignerPropertiesFile.getInstance();
		properties.addRecentDocument(designerFileToOpen, "recentdesfiles");
		updateRecentDocuments(properties.getRecentDocuments("recentdesfiles"), "recentdesfiles");
	}

    public void importPDF(String file) {
        int importType = aquirePDFImportType();
        importPDF(importType, file);
    }

	//todo dont allow import of a pdf into a closed document
    private void importPDF() {
        int importType = aquirePDFImportType();

        final JFileChooser chooser = new JFileChooser();

        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        String[] pdf = new String[]{"pdf"};
        chooser.addChoosableFileFilter(new FileFilterer(pdf, "Pdf (*.pdf)"));

        final int state = chooser.showOpenDialog((Component) mainFrame);
        final File file = chooser.getSelectedFile();
        if (file != null && state == JFileChooser.APPROVE_OPTION) {
            importPDF(importType, file.getAbsolutePath());
        }
    }

	private int aquirePDFImportType() {
		PDFImportChooser pic = new PDFImportChooser((Component) mainFrame);
        pic.setVisible(true);
        
        return pic.getImportType();
    }

	private void importPDF(final int importType, final String pdfPath) {
		if (importType == PDFImportChooser.IMPORT_NEW) {
		    closePDF();

		    setPanelsState(true);

		    mainFrame.setFormsDocument(new FormsDocument(version));
		}

        try {
//            final JDialog dialog= new JDialog((Frame) mainFrame, true);
//            SwingUtilities.invokeLater(new Runnable() {
//                public void run() {
//                    JPanel panel = new JPanel();
//                    panel.setLayout(new BorderLayout());
//                    panel.add(new JLabel(("Calculating size of import")));
//                    //dialog.setUndecorated(true);
//                    dialog.add(panel);
//                    dialog.pack();
//                    dialog.setLocationRelativeTo((Component) mainFrame);
//                    dialog.setVisible(true);
//                }
//            });

            final PdfDecoder pdfDecoder = new PdfDecoder();

            if (pdfPath.startsWith("http:") || pdfPath.startsWith("file:")) {
                pdfDecoder.openPdfFileFromURL(pdfPath);
            }
            else {
                pdfDecoder.openPdfFile(pdfPath);
            }

            final int pageCount = pdfDecoder.getPageCount();
//            SwingUtilities.invokeLater(new Runnable() {
//                public void run() {
//                    System.out.println("setting visable = false");
//                    dialog.setVisible(false);
//                }
//            });

            final ProgressMonitor progressDialog = new ProgressMonitor((Component)mainFrame, "", "", 0, pageCount);
            progressDialog.setMillisToDecideToPopup(0);
            progressDialog.setMillisToPopup(0);
            progressDialog.setNote("Importing page "+1+" of "+progressDialog.getMaximum());
            progressDialog.setProgress(0);
            
            final SwingWorker worker = new SwingWorker() {
		    	public Object construct() {

		    		boolean isCancelled;
					
		    		final List pages = new ArrayList();
			    	int currentLastPage = mainFrame.getTotalNoOfPages();
		    		
					if (importType == PDFImportChooser.IMPORT_NEW) {
		    			for (int pdfPageNumber = 1; pdfPageNumber < pageCount + 1; pdfPageNumber++) {
		    				currentLastPage++;

		    				Page newPage = new Page("(page " + currentLastPage + ")", pdfPath, pdfPageNumber);

		    				List widgetsOnPage = new ArrayList();
		    				isCancelled = decodePDFPage(pdfPath, pdfDecoder, progressDialog, pdfPageNumber, newPage, widgetsOnPage);
		    				
		    				Object[] properties = { new Integer(pdfPageNumber), newPage, widgetsOnPage };
							pages.add(properties);
		    				
		    				if(isCancelled)
		    					break;
		    			}

		    			mainFrame.setCurrentDesignerFileName("Untitled");
		    			mainFrame.setTitle("Untitled - PDF Forms Designer Version " + version);

		    		} else if (importType == PDFImportChooser.IMPORT_EXISTING) {
		    			for (int pdfPageNumber = 1; pdfPageNumber < pageCount + 1; pdfPageNumber++) {
		    				currentLastPage++;
		    				
		    				Page newPage = new Page("(page " + currentLastPage + ")", pdfPath, pdfPageNumber);

		    				List widgetsOnPage = new ArrayList();
		    				isCancelled = decodePDFPage(pdfPath, pdfDecoder, progressDialog, pdfPageNumber, newPage, widgetsOnPage);
		    				
		    				Object[] properties = { new Integer(currentLastPage), newPage, widgetsOnPage };
							pages.add(properties);
		    				
		    				if(isCancelled)
		    					break;
		    			}
		    		}

					final int finalCurrentLastPage = currentLastPage;
					
					EventQueue.invokeLater(new Runnable(){
						public void run() {
							// TODO Auto-generated method stub
							for (Iterator it = pages.iterator(); it.hasNext();) {
								Object[] properties = (Object[]) it.next();
								
								int pageNumber = ((Integer) properties[0]).intValue();
								Page newPage = (Page) properties[1];
								List widgetsOnPage = (List) properties[2];
								
								mainFrame.setCurrentPage(pageNumber);

								addPage(pageNumber, newPage);
								
								for (Iterator iter = widgetsOnPage.iterator(); iter.hasNext();) {
									IWidget widget = (IWidget) iter.next();
									mainFrame.getDesigner().addWidget(widget);
								}
							}
							
							setTotalPages();
							
//							mainFrame.setCurrentPage(finalCurrentLastPage);
							mainFrame.displayPage(mainFrame.getCurrentPage());
							
							progressDialog.close();
							
						}
					});
					
					
		    		return null;
		    	}
		    };
			worker.start();
		    
			
		    DesignerPropertiesFile properties = DesignerPropertiesFile.getInstance();
			properties.addRecentDocument(pdfPath, "recentpdffiles");
			updateRecentDocuments(properties.getRecentDocuments("recentpdffiles"), "recentpdffiles");
		    
		} catch (PdfException e) {
		    e.printStackTrace();
		}
	}

    private void addButtonGroupsToPage(Element page, Page newPage, int type) {
        String groupName = type == IWidget.RADIO_BUTTON ? "radiobuttongroups" : "checkboxgroups";

        Element radioButtonGroupsElement = (Element) page.getElementsByTagName(groupName).item(0);

        List radioButtonGropusList = XMLUtils.getElementsFromNodeList(radioButtonGroupsElement.getChildNodes());

        for (Iterator iter = radioButtonGropusList.iterator(); iter.hasNext();) {
            Element buttonGroupElement = (Element) iter.next();

            String value = XMLUtils.getAttributeFromElement(buttonGroupElement, "buttongroupname");
            ButtonGroup buttonGroup = new ButtonGroup(type);

            System.out.println("value = " + value);

            buttonGroup.setName(value);

            if(type == IWidget.RADIO_BUTTON)
                newPage.getRadioButtonGroups().add(buttonGroup);
            else
                newPage.getCheckBoxGroups().add(buttonGroup);
        }
    }

    private List getWidgetsFromXMLElement(Element page) {

        List elementsInPage = XMLUtils.getElementsFromNodeList(page.getChildNodes());

        List widgetsInPageList = new ArrayList();

        for (Iterator it = elementsInPage.iterator(); it.hasNext();) {
            Element element = (Element) it.next();
            if (element.getNodeName().equals("widget"))
                widgetsInPageList.add(element);
        }

        List widgets = new ArrayList();

        for (Iterator iter = widgetsInPageList.iterator(); iter.hasNext();) {
            Element widgetElement = (Element) iter.next();

            String widgetType = XMLUtils.getAttributeFromChildElement(widgetElement, "type");

            try {
                Field field = IWidget.class.getDeclaredField(widgetType);

                int type = field.getInt(this);

                IWidget widget;
                if (type == IWidget.GROUP) {
                    widget = new GroupWidget();
                    List widgetsInGroup = getWidgetsFromXMLElement((Element) XMLUtils.getElementsFromNodeList(widgetElement.getElementsByTagName("widgets")).get(0));
                    widget.setWidgetsInGroup(widgetsInGroup);
                } else {
                    widget = WidgetFactory.createWidget(type, widgetElement);
                }

                widgets.add(widget);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return widgets;
    }

    private void decodePDFPage(PdfDecoder pdfDecoder, String pdfPath, int pdfPageNumber, Page newPage, List widgetsOnPage) {
    	
    	try {
            pdfDecoder = new PdfDecoder();

            if (pdfPath.startsWith("http:") || pdfPath.startsWith("file:"))
                pdfDecoder.openPdfFileFromURL(pdfPath);
            else
                pdfDecoder.openPdfFile(pdfPath);
            
            pdfDecoder.decodePage(pdfPageNumber);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        PdfPageData pdfPageData = pdfDecoder.getPdfPageData();
        
		int pageHeight = pdfPageData.getMediaBoxHeight(pdfPageNumber);
		int cropHeight = pdfPageData.getCropBoxHeight(pdfPageNumber);
        
		int cropX = pdfPageData.getCropBoxX(pdfPageNumber);
        int cropY = pdfPageData.getCropBoxY(pdfPageNumber);
        
        /**
         * when parsing a widget we don't want to be updating the display until all widgets are parsed, so add all the widgets to a list
         * so, after parsing, the list can be iterated and the widgets added to the page.
         */
        WidgetParser.parseWidgets(pdfDecoder.getFormRenderer(), newPage, pageHeight, cropHeight, cropX, cropY, mainFrame, widgetsOnPage);
        
        //newPage.setWidgets(widgetsList);
        
        pdfDecoder.closePdfFile();
	}

	private void closePDF() {
        mainFrame.setFormsDocument(null);

        mainFrame.getDesigner().close();

        mainFrame.setCurrentDesignerFileName("");
        mainFrame.setTitle("PDF Forms Designer Version " + version);

        mainFrame.setPropertiesCompound(new HashSet());
        mainFrame.setPropertiesToolBar(new HashSet());

        setPanelsState(false);

        mainFrame.setCurrentPage(0);
    }

    private void removePage() {

        int noOfPages = mainFrame.getTotalNoOfPages();
        if (noOfPages == 1) {
            JOptionPane.showMessageDialog((Component) mainFrame, "You cannot remove the last page", "Last Page",
                    JOptionPane.OK_OPTION);

            return;
        }

        mainFrame.getFormsDocument().removePage(mainFrame.getCurrentPage());
        mainFrame.removePageFromHierarchyPanel(mainFrame.getCurrentPage());

        //System.out.println(mainFrame.getCurrentPage() +" "+ mainFrame.getTotalNoOfPages());

        if (mainFrame.getCurrentPage() == noOfPages)
            mainFrame.setCurrentPage(mainFrame.getCurrentPage() - 1);

        mainFrame.displayPage(mainFrame.getCurrentPage());

        setTotalPages();
    }

    private void insertPage(int width, int height) {
        Page newPage = new Page("(page " + (mainFrame.getTotalNoOfPages() + 1) + ")", width, height);

        mainFrame.setCurrentPage(mainFrame.getCurrentPage() + 1);

        addPage(mainFrame.getCurrentPage(), newPage);

        mainFrame.displayPage(mainFrame.getCurrentPage());

        setTotalPages();
    }

    private Map getChangedPdfFileLocations(List pages) {
        Set pdfFiles = new HashSet();
        for (Iterator it = pages.iterator(); it.hasNext();) {
            Element page = (Element) it.next();

            Element fileLocationElement = XMLUtils.getPropertyElement(page, "pdffilelocation");
            if (fileLocationElement != null) {
                String fileLocation = fileLocationElement.getAttributeNode("value").getValue();
                pdfFiles.add(fileLocation);
            }
        }

        Map changedFiles = new HashMap();
        for (Iterator it = pdfFiles.iterator(); it.hasNext();) {
            String fileName = (String) it.next();

            String newFileName = fileName;

            if (!new File(fileName).exists()) {
                FileFinder fileFinder = new FileFinder((Component) mainFrame, fileName);
                fileFinder.setVisible(true);
                newFileName = fileFinder.getFileLocation();
            }

            changedFiles.put(fileName, newFileName);
        }

        return changedFiles;
    }


    private void addPage(int pdfPage, Page newPage) {
        mainFrame.getFormsDocument().addPage(pdfPage, newPage);
        mainFrame.addPageToHierarchyPanel(pdfPage, newPage);
    }

    private void setTotalPages() {
        mainFrame.setTotalNoOfDisplayedPages(mainFrame.getTotalNoOfPages());
    }

	private boolean decodePDFPage(final String pdfPath, final PdfDecoder pdfDecoder, final ProgressMonitor progressDialog, final int pdfPageNumber, Page newPage, List widgetsOnPage) {
		decodePDFPage(pdfDecoder, pdfPath, pdfPageNumber, newPage, widgetsOnPage);
		
		if(progressDialog.isCanceled()){
			return true;
		}
		
		progressDialog.setProgress(pdfPageNumber);
		progressDialog.setNote("Importing page "+pdfPageNumber+" of "+progressDialog.getMaximum());

		return false;
	}
	
	/*
	 final PdfImportProgressDialog progressDialog = new PdfImportProgressDialog();
		    progressDialog.setStatusMaximum(pageCount);
		    progressDialog.setProgress(0);
		    progressDialog.setLocationRelativeTo((Component) mainFrame);
		    progressDialog.setModal(true);
		    
		    SwingWorkerVariant aWorker = new SwingWorkerVariant((Component) mainFrame) {
		    	private List pages = new ArrayList();
		    	private int currentLastPage = mainFrame.getTotalNoOfPages();

		    	protected void doNonUILogic() {
					EventQueue.invokeLater(new Runnable() {
						public void run() {
							progressDialog.setVisible(true);
						}
					});
					
					boolean isCancelled;
					
					if (importType == PDFImportChooser.IMPORT_NEW) {
		    			for (int pdfPageNumber = 1; pdfPageNumber < pageCount + 1; pdfPageNumber++) {
		    				currentLastPage++;

		    				Page newPage = new Page("(page " + currentLastPage + ")", pdfPath, pdfPageNumber);

		    				List widgetsOnPage = new ArrayList();
		    				isCancelled = decodePDFPage(pdfPath, pdfDecoder, progressDialog, pdfPageNumber, newPage, widgetsOnPage);
		    				
		    				Object[] properties = { new Integer(pdfPageNumber), newPage, widgetsOnPage };
							pages.add(properties);
		    				
		    				if(isCancelled)
		    					break;
		    			}

		    			mainFrame.setCurrentDesignerFileName("Untitled");
		    			mainFrame.setTitle("Untitled - PDF Forms Designer Version " + version);

		    		} else if (importType == PDFImportChooser.IMPORT_EXISTING) {
		    			for (int pdfPageNumber = 1; pdfPageNumber < pageCount + 1; pdfPageNumber++) {
		    				currentLastPage++;
		    				
		    				Page newPage = new Page("(page " + currentLastPage + ")", pdfPath, pdfPageNumber);

		    				List widgetsOnPage = new ArrayList();
		    				isCancelled = decodePDFPage(pdfPath, pdfDecoder, progressDialog, pdfPageNumber, newPage, widgetsOnPage);
		    				
		    				Object[] properties = { new Integer(currentLastPage), newPage, widgetsOnPage };
							pages.add(properties);
		    				
		    				if(isCancelled)
		    					break;
		    			}
		    		}
				}	
				
				protected void doUIUpdateLogic() {
					for (Iterator it = pages.iterator(); it.hasNext();) {
						Object[] properties = (Object[]) it.next();
						
						int pdfPage = ((Integer) properties[0]).intValue();
						Page newPage = (Page) properties[1];
						List widgetsOnPage = (List) properties[2];
						
						addPage(pdfPage, newPage);
						
						for (Iterator iter = widgetsOnPage.iterator(); iter.hasNext();) {
							IWidget widget = (IWidget) iter.next();
							mainFrame.getDesigner().addWidget(widget);
						}
					}
					
					setTotalPages();
					
					mainFrame.setCurrentPage(currentLastPage);
					mainFrame.displayPage(mainFrame.getCurrentPage());
					
					progressDialog.setVisible(false);
				}
			};
			aWorker.start();
	 */
	
}
