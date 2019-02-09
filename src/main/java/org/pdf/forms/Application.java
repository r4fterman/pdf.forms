package org.pdf.forms;

import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Optional;
import java.util.jar.Manifest;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.pdf.forms.gui.VLFrame;
import org.pdf.forms.gui.commands.Version;
import org.pdf.forms.gui.windows.SplashWindow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Application {

    private Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(final String[] args) {
        final Application application = new Application();
        application.start();
    }

    private Application() {
    }

    private void start() {
        final Version version = readManifest().map(this::getVersion).orElse(Version.CURRENT_VERSION);

        splashScreen(version);
    }

    private void splashScreen(final Version version) {
        final SplashWindow splashWindow = new SplashWindow(version);
        splashWindow.setStatusMaximum(4);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (final Exception e) {
            logger.error("Fail setting look&feel", e);
        }

        splashWindow.setProgress(1, "Initializing window");
        final VLFrame frame = new VLFrame(splashWindow, version);

        // get local graphics environment
        final GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();

        // get maximum window bounds
        final Rectangle maximumWindowBounds = graphicsEnvironment.getMaximumWindowBounds();
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
        } catch (final IOException e) {
            logger.error("Fail to read manifest file", e);
        }
        return Optional.empty();
    }

    private Version getVersion(final Manifest manifest) {
        return Optional.ofNullable(manifest.getMainAttributes().getValue("Implementation-Version"))
                .map(Version::of)
                .orElse(Version.CURRENT_VERSION);
    }
}
