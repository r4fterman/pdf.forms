package org.pdf.forms.readers.custom;

import static java.util.stream.Collectors.toUnmodifiableList;

import java.io.File;
import java.util.Collection;
import java.util.List;

import org.pdf.forms.model.components.CustomComponent;
import org.pdf.forms.model.components.CustomComponents;
import org.pdf.forms.model.des.Property;
import org.pdf.forms.model.des.Widget;
import org.pdf.forms.widgets.IWidget;

/**
 * holds values stored in XML file on disk.
 */
public final class CustomWidgetsFile {

    private final CustomComponents customComponents;

    public CustomWidgetsFile(final File directory) {
        final File customComponentsFile = getFile(directory);
        this.customComponents = new CustomComponentsFileReader(customComponentsFile).getCustomComponents();
    }

    private File getFile(final File directory) {
        return new File(directory, ".custom_components.xml");
    }

    public CustomComponents getCustomComponents() {
        return customComponents;
    }

    public boolean isNameTaken(final String name) {
        return customComponents.getCustomComponentList().stream()
                .map(CustomComponent::getProperty)
                .flatMap(List::stream)
                .filter(property -> property.getName().equals("name"))
                .anyMatch(property -> {
                    final String propertyName = property.getValue();
                    return propertyName.equals(name);
                });
    }

    public void addCustomWidget(
            final String name,
            final Collection<IWidget> customWidgets) {
        final CustomComponent customComponent = getCustomComponent(name);

        final List<Widget> widgets = customWidgets.stream()
                .map(this::convertToModelWidget)
                .collect(toUnmodifiableList());

        customComponent.getWidget().addAll(widgets);
    }

    private CustomComponent getCustomComponent(final String name) {
        final List<CustomComponent> customComponentList = customComponents.getCustomComponentList();

        return customComponentList.stream()
                .filter(customComponent -> customComponent.getProperty().stream()
                        .filter(property -> property.getName().equals("name"))
                        .anyMatch(property -> property.getValue().equals(name)))
                .findFirst()
                .orElseGet(() -> {
                    final CustomComponent customComponent = createCustomComponent(name);
                    customComponentList.add(customComponent);
                    return customComponent;
                });
    }

    private CustomComponent createCustomComponent(final String name) {
        final CustomComponent customComponent = new CustomComponent();
        customComponent.getProperty().add(new Property("name", name));
        return customComponent;
    }

    private Widget convertToModelWidget(final IWidget iWidget) {
        //todo: convert UI class into model class
        //todo: or work only on model class
        final Widget widget = new Widget();
        widget.getProperty().add(new Property("name", iWidget.getWidgetName()));
        return widget;
    }
}
