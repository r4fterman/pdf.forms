package org.pdf.forms.widgets;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Disabled
class GroupWidgetTest {

    private GroupWidget widget;

    @BeforeEach
    void setUp() {
        this.widget = new GroupWidget();
    }

    @Test
    void persist_widget_into_xml() throws Exception {
        final String serialize = new WidgetFileWriter(widget.getWidgetModel()).serialize();
        assertThat(serialize.length(), is(156));
        System.out.println(serialize);
    }
}
