package org.pdf.forms;

import java.io.File;

public class Configuration {

    public File getConfigDirectory() {
        return new File(System.getProperty("user.dir"));
    }
}
