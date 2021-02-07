package org.pdf.forms.model.des;

import static java.util.stream.Collectors.toUnmodifiableList;

import java.util.List;

public interface ButtonGroups {
    default List<String> getButtonGroupNames() {
        return getProperty().stream()
                .filter(p -> p.getName().equals("buttongroupname"))
                .map(Property::getValue)
                .collect(toUnmodifiableList());
    }

    List<Property> getProperty();
}
