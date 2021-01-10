package org.pdf.forms;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.jar.Manifest;

import javax.swing.*;

import org.pdf.forms.fonts.FontHandler;
import org.pdf.forms.gui.VLFrame;
import org.pdf.forms.gui.windows.SplashWindow;
import org.pdf.forms.utils.DesignerPropertiesFile;
import org.pdf.forms.widgets.utils.WidgetFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Application {

    private static final String DEFAULT_VERSION = "0.8b05";
    private static final String MANIFEST_MF = "META-INF/MANIFEST.MF";

    public static void main(final String[] args) {
        final Application application = new Application();
        application.start();
    }

    private final Logger logger = LoggerFactory.getLogger(Application.class);

    private Application() {
    }

    private void start() {
        final String version = readManifest().map(this::getVersion).orElse(DEFAULT_VERSION);

        splashScreen(version);
    }

    private void splashScreen(final String version) {
        final SplashWindow splashWindow = new SplashWindow(version);
        splashWindow.setStatusMaximum(7);
        splashWindow.setVisible(true);

        splashWindow.setProgress(1, "Initialize UI");
        try {
            configureMacOSXSupport();
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (final Exception e) {
            logger.error("Error on application startup", e);
        }

        try {
            initializeUI(version, splashWindow);
        } finally {
            splashWindow.setVisible(false);
        }
    }

    private void initializeUI(
            final String version,
            final SplashWindow splashWindow) {
        final Configuration configuration = new Configuration();
        final DesignerPropertiesFile designerPropertiesFile = new DesignerPropertiesFile(configuration
                .getConfigDirectory());
        final FontHandler fontHandler = new FontHandler(designerPropertiesFile);
        final WidgetFactory widgetFactory = new WidgetFactory(fontHandler);
        final VLFrame frame = new VLFrame(splashWindow,
                version,
                fontHandler,
                widgetFactory,
                configuration,
                designerPropertiesFile);

        // get local graphics environment
        final GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();

        // get maximum window bounds
        final Rectangle maximumWindowBounds = graphicsEnvironment.getMaximumWindowBounds();
        frame.setSize(maximumWindowBounds.width, maximumWindowBounds.height);
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);

        frame.validate();
        SwingUtilities.invokeLater(() -> frame.setVisible(true));
    }

    private void configureMacOSXSupport() {
        final String lcOSName = System.getProperty("os.name").toLowerCase();
        if (lcOSName.startsWith("mac os x")) {
            System.setProperty("apple.laf.useScreenMenuBar", "true");
        }
    }

    private Optional<Manifest> readManifest() {
        try {
            final Optional<InputStream> stream = Optional.ofNullable(getClass().getClassLoader()
                    .getResourceAsStream(MANIFEST_MF));
            if (stream.isPresent()) {
                return Optional.of(new Manifest(stream.get()));
            } else {
                logger.warn("File {} not found. Use default application version.", MANIFEST_MF);
            }
        } catch (final IOException e) {
            logger.error("Error reading {}", MANIFEST_MF, e);
        }
        return Optional.empty();
    }

    private String getVersion(final Manifest manifest) {
        return Optional
                .ofNullable(manifest.getMainAttributes().getValue("Implementation-Version"))
                .orElse(DEFAULT_VERSION);
    }
}
