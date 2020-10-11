package org.pdf.forms.widgets;

public class RadioButtonGroup {

    private static int nextNumber = 1;

    private String name;

    public RadioButtonGroup() {
        String name = "Radio Button Group" + nextNumber;
        nextNumber++;

        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }
}
