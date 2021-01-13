package org.pdf.forms.model.des;

import javax.xml.bind.annotation.XmlType;

@XmlType(name = "object")
public class ObjectProperties {

    private FieldProperties fieldProperties;
    private ValueProperties valueProperties;
    private BindingProperties bindingProperties;

}
