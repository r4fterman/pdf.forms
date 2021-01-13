package org.pdf.forms.model.des;

import javax.xml.bind.annotation.XmlType;

@XmlType(name = "layout")
public class LayoutProperties {

    private SizeAndPosition sizeAndPosition;
    private Margins margins;
    private Caption caption;
}
