package org.pdf.forms.model.des;

import java.util.Objects;
import java.util.StringJoiner;

import javax.xml.bind.annotation.XmlType;

@XmlType(name = "javascript")
public class JavaScriptContent {

    private String initialize;
    private String mouseEnter;
    private String mouseExit;
    private String change;
    private String mouseUp;
    private String mouseDown;
    private String keystroke;

    public String getMouseEnter() {
        return mouseEnter;
    }

    public void setMouseEnter(final String mouseEnter) {
        this.mouseEnter = mouseEnter;
    }

    public String getMouseExit() {
        return mouseExit;
    }

    public void setMouseExit(final String mouseExit) {
        this.mouseExit = mouseExit;
    }

    public String getChange() {
        return change;
    }

    public void setChange(final String change) {
        this.change = change;
    }

    public String getMouseUp() {
        return mouseUp;
    }

    public void setMouseUp(final String mouseUp) {
        this.mouseUp = mouseUp;
    }

    public String getMouseDown() {
        return mouseDown;
    }

    public void setMouseDown(final String mouseDown) {
        this.mouseDown = mouseDown;
    }

    public String getKeystroke() {
        return keystroke;
    }

    public void setKeystroke(final String keystroke) {
        this.keystroke = keystroke;
    }

    public String getInitialize() {
        return initialize;
    }

    public void setInitialize(final String initialize) {
        this.initialize = initialize;
    }

    @Override
    public boolean equals(final Object o) {
        if (o instanceof JavaScriptContent) {
            final JavaScriptContent that = (JavaScriptContent) o;
            return Objects.equals(initialize, that.initialize)
                    && Objects.equals(mouseEnter, that.mouseEnter)
                    && Objects.equals(mouseExit, that.mouseExit)
                    && Objects.equals(change, that.change)
                    && Objects.equals(mouseUp, that.mouseUp)
                    && Objects.equals(mouseDown, that.mouseDown)
                    && Objects.equals(keystroke, that.keystroke);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(initialize, mouseEnter, mouseExit, change, mouseUp, mouseDown, keystroke);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", JavaScriptContent.class.getSimpleName() + "[", "]")
                .add("initialize='" + initialize + "'")
                .add("mouseEnter='" + mouseEnter + "'")
                .add("mouseExit='" + mouseExit + "'")
                .add("change='" + change + "'")
                .add("mouseUp='" + mouseUp + "'")
                .add("mouseDown='" + mouseDown + "'")
                .add("keystroke='" + keystroke + "'")
                .toString();
    }
}
