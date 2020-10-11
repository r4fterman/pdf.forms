package org.pdf.forms.gui.designer.captionchanger;

import javax.swing.text.BoxView;
import javax.swing.text.Element;

class CenteredBoxView extends BoxView {

    CenteredBoxView(
            final Element element,
            final int axis) {
        super(element, axis);
    }

    @Override
    protected void layoutMajorAxis(
            final int targetSpan,
            final int axis,
            final int[] offsets,
            final int[] spans) {
        super.layoutMajorAxis(targetSpan, axis, offsets, spans);

        int textBlockHeight = 0;
        final int offset;

        for (final int span : spans) {
            textBlockHeight += span;
        }
        offset = (targetSpan - textBlockHeight) / 2;
        for (int i = 0; i < offsets.length; i++) {
            offsets[i] += offset;
        }
    }
}
