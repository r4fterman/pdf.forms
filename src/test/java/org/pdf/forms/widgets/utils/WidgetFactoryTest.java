package org.pdf.forms.widgets.utils;

import static org.easymock.EasyMock.expect;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.ArrayList;

import javax.swing.JLabel;

import org.easymock.EasyMockSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.pdf.forms.Configuration;
import org.pdf.forms.document.Page;
import org.pdf.forms.fonts.FontHandler;
import org.pdf.forms.widgets.CheckBoxWidget;
import org.pdf.forms.widgets.ComboBoxWidget;
import org.pdf.forms.widgets.IWidget;
import org.pdf.forms.widgets.ImageWidget;
import org.pdf.forms.widgets.ListBoxWidget;
import org.pdf.forms.widgets.RadioButtonWidget;
import org.pdf.forms.widgets.TextFieldWidget;
import org.pdf.forms.widgets.TextWidget;

class WidgetFactoryTest extends EasyMockSupport {

    private final Rectangle bounds = new Rectangle(300, 400);

    private WidgetFactory widgetFactory;

    @BeforeEach
    void setUp() {
        final Configuration configuration = new Configuration();
        final FontHandler fontHandler = new FontHandler(configuration);
        this.widgetFactory = new WidgetFactory(fontHandler);
    }

    @Test
    void createTextFieldWidget() {
        final IWidget widget = widgetFactory.createWidget(IWidget.TEXT_FIELD, bounds);

        assertThat(widget, is(instanceOf(TextFieldWidget.class)));
    }

    @Test
    void createTextWidget() {
        final IWidget widget = widgetFactory.createWidget(IWidget.TEXT, bounds);

        assertThat(widget, is(instanceOf(TextWidget.class)));
    }

    @Test
    void createRadioButtonWidget() {
        final IWidget widget = widgetFactory.createWidget(IWidget.RADIO_BUTTON, bounds);

        assertThat(widget, is(instanceOf(RadioButtonWidget.class)));
    }

    @Test
    void createCheckBoxWidget() {
        final IWidget widget = widgetFactory.createWidget(IWidget.CHECK_BOX, bounds);

        assertThat(widget, is(instanceOf(CheckBoxWidget.class)));
    }

    @Test
    void createComboBoxWidget() {
        final IWidget widget = widgetFactory.createWidget(IWidget.COMBO_BOX, bounds);

        assertThat(widget, is(instanceOf(ComboBoxWidget.class)));
    }

    @Test
    void createListBoxWidget() {
        final IWidget widget = widgetFactory.createWidget(IWidget.LIST_BOX, bounds);

        assertThat(widget, is(instanceOf(ListBoxWidget.class)));
    }

    @Test
    void createImageWidget() {
        final IWidget widget = widgetFactory.createWidget(IWidget.IMAGE, bounds);

        assertThat(widget, is(instanceOf(ImageWidget.class)));
    }

    @Test
    void createCheckBoxWidget_directly() {
        final Page page = createMock(Page.class);
        expect(page.getCheckBoxGroups()).andReturn(new ArrayList<>());

        replayAll();
        final IWidget widget = widgetFactory.createCheckBoxWidget(page, bounds);
        verifyAll();

        assertThat(widget, is(instanceOf(CheckBoxWidget.class)));
    }

    @Test
    void createRadioButtonWidget_directly() {
        final Page page = createMock(Page.class);
        expect(page.getRadioButtonGroups()).andReturn(new ArrayList<>());

        replayAll();
        final IWidget widget = widgetFactory.createRadioButtonWidget(page, bounds);
        verifyAll();

        assertThat(widget, is(instanceOf(RadioButtonWidget.class)));
    }

    @Test
    void name() {
        final JLabel label = WidgetFactory.createResizedComponent(new JLabel(), 300, 400);

        assertThat(label.getSize(), is(new Dimension(300, 400)));
    }
}
