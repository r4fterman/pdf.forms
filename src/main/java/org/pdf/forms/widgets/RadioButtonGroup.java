package org.pdf.forms.widgets;

public class RadioButtonGroup {

    private static int nextNumber = 1;

    private String name;

    public RadioButtonGroup() {
        this.name = createUniqueName();
        nextNumber++;
    }

    private String createUniqueName() {
        return "Radio Button Group " + nextNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }
}
