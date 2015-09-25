package org.pdf.forms.utils.configuration;

import java.io.File;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.jpedal.utils.LogWriter;
import org.w3c.dom.Document;

public abstract class ConfigurationFile {
	
	protected String separator=System.getProperty( "file.separator" );
	protected String userDir=System.getProperty("user.dir");

	protected String configDir = userDir + separator + "configuration";
	protected String configFile = configDir + separator;

	protected Document doc;
	
	protected ConfigurationFile(String fileName) {
		configFile += fileName;
		
		try{
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();

			boolean needNewFile = false;
			
			if(new File(configFile).exists()){
				try{
                    doc = db.parse(new File(configFile));
				}catch(Exception e){
					doc = db.newDocument();
					needNewFile = true;
					//<start-full><start-demo>
					e.printStackTrace();
					//<end-demo><end-full>
				}
			}else {
				new File(configDir).mkdirs();
				doc =  db.newDocument();
				needNewFile = true;
			}
			
			if(needNewFile){
				writeDefaultConfiguration();
				writeDoc();
			}
		}catch(Exception e){
			LogWriter.writeLog("Exception " + e + " generating menu configuration file " + configFile);
        	//<start-full><start-demo>
        	e.printStackTrace();
        	//<end-demo><end-full>
		}
	}

	protected void writeDoc() throws Exception{
		InputStream stylesheet = this.getClass().getResourceAsStream("/org/jpedal/examples/simpleviewer/res/xmlstyle.xslt");

		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer(new StreamSource(stylesheet));
		transformer.transform(new DOMSource(doc), new StreamResult(configFile));
	}
    
    protected abstract void writeDefaultConfiguration() throws Exception;
}
