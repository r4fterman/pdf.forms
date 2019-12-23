package org.pdf.forms.widgets;

public class ButtonGroup {

    private static int nextRadioButtonGroupNumber = 1;
    private static int nextCheckBoxGroupNumber = 1;

    private String name;

    public ButtonGroup(final int type) {
        final String name;
        if (type == IWidget.RADIO_BUTTON) {
            name = "Radio Button Group" + nextRadioButtonGroupNumber;
            nextRadioButtonGroupNumber++;
        } else {
            name = "CheckBox Group" + nextCheckBoxGroupNumber;
            nextCheckBoxGroupNumber++;
        }
        this.name = name;
    }

    public ButtonGroup(final String groupName) {
        this.name = groupName;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }
}
