package org.pdf.forms;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Map;
import java.util.Optional;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import javax.swing.*;

import org.pdf.forms.gui.VLFrame;
import org.pdf.forms.gui.windows.SplashWindow;

public class Application {

    private static final String DEFAULT_VERSION = "0.8b05";

    public static void main(final String[] args) {
        final Application application = new Application();
        application.start();
    }

    private Application() {
    }

    private void start() {
        final String version = readManifest().map(this::getVersion).orElse(DEFAULT_VERSION);

        splashScreen(version);
    }

    private void splashScreen(final String version) {
        SplashWindow splashWindow = new SplashWindow(version);
        splashWindow.setStatusMaximum(4);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        splashWindow.setProgress(1, "Initializing window");
        final VLFrame frame = new VLFrame(splashWindow, version);

        // get local graphics environment
        GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();

        // get maximum window bounds
        Rectangle maximumWindowBounds = graphicsEnvironment.getMaximumWindowBounds();
        frame.setSize(maximumWindowBounds.width, maximumWindowBounds.height);
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);

        frame.validate();
        SwingUtilities.invokeLater(() -> frame.setVisible(true));

        splashWindow.setVisible(false);
    }

    private Optional<Manifest> readManifest() {
        try {
            final Enumeration<URL> resources = getClass().getClassLoader().getResources("META-INF/MANIFEST.MF");
            if (resources.hasMoreElements()) {
                return Optional.of(new Manifest(resources.nextElement().openStream()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private String getVersion(final Manifest manifest) {
        final Map<String, Attributes> manifestEntries = manifest.getEntries();
        return Optional.ofNullable(manifest.getMainAttributes().getValue("Implementation-Version")).orElse(DEFAULT_VERSION);
    }
}
