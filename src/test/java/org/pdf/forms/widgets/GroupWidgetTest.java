package org.pdf.forms.widgets;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.pdf.forms.utils.XMLUtils;
import org.w3c.dom.Document;

@Disabled
class GroupWidgetTest {

    private GroupWidget widget;

    @BeforeEach
    void setUp() {
        this.widget = new GroupWidget();
    }

    @Test
    void persist_widget_into_xml() {
        final Document document = widget.getProperties();
        assertThat(document, is(notNullValue()));

        final String serialize = XMLUtils.serialize(document);
        assertThat(serialize.length(), is(156));
        System.out.println(serialize);
    }
}
