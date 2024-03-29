package org.pdf.forms.gui.properties.customcomponents.tridstatecheckbox;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.ActionMapUIResource;

/**
 * Maintenance tip - There were some tricks to getting this code
 * working:
 * <p/>
 * 1. You have to overwrite addMouseListener() to do nothing
 * 2. You have to add a mouse event on mousePressed by calling
 * super.addMouseListener()
 * 3. You have to replace the UIActionMap for the keyboard event
 * "pressed" with your own one.
 * 4. You have to remove the UIActionMap for the keyboard event
 * "released".
 * 5. You have to grab focus when the next state is entered,
 * otherwise clicking on the component won't get the focus.
 * 6. You have to make a #TristateDecorator as a button model that
 * wraps the original button model and does state management.
 */
public class TriStateCheckBox extends JCheckBox {

    public enum State {
        NOT_SELECTED,
        SELECTED,
        DONT_CARE;
    }

    private final TriStateDecorator decorator;

    public TriStateCheckBox(
            final String text,
            final Icon icon,
            final State initial,
            final TriStateCheckBoxParent parent) {
        super(text, icon);
        super.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(final MouseEvent e) {
                grabFocus();
                decorator.nextState();

                parent.checkBoxClicked(e);
            }
        });

        final ActionMap map = new ActionMapUIResource();
        map.put("pressed", new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                grabFocus();
                decorator.nextState();
            }
        });
        map.put("released", null);
        SwingUtilities.replaceUIActionMap(this, map);

        // set the model to the adapted model
        decorator = new TriStateDecorator(getModel());
        setModel(decorator);
        setState(initial);
    }

    public TriStateCheckBox(
            final String text,
            final State initial,
            final TriStateCheckBoxParent parent) {
        this(text, null, initial, parent);
    }

    public TriStateCheckBox(
            final String text,
            final TriStateCheckBoxParent parent) {
        this(text, State.DONT_CARE, parent);
    }

    public TriStateCheckBox(final TriStateCheckBoxParent parent) {
        this(null, parent);
    }

    /**
     * No one may add mouse listeners, not even Swing!
     */
    @Override
    public void addMouseListener(final MouseListener l) {
    }

    /**
     * Set the new state to either SELECTED, NOT_SELECTED or
     * DONT_CARE.  If state == null, it is treated as DONT_CARE.
     */
    public void setState(final State state) {
        decorator.setState(state);
    }

    /**
     * Return the current state, which is determined by the
     * selection status of the model.
     */
    public State getState() {
        return decorator.getState();
    }

    @Override
    public void setSelected(final boolean selected) {
        if (selected) {
            setState(State.SELECTED);
        } else {
            setState(State.NOT_SELECTED);
        }
    }

    /**
     * Exactly which Design Pattern is this?  Is it an Adapter,
     * a Proxy or a Decorator?  In this case, my vote lies with the
     * Decorator, because we are extending functionality and
     * "decorating" the original model with a more powerful model.
     */
    private final class TriStateDecorator implements ButtonModel {
        private final ButtonModel other;

        private TriStateDecorator(final ButtonModel other) {
            this.other = other;
        }

        private void setState(final State state) {
            if (state == State.NOT_SELECTED) {
                other.setArmed(false);
                setPressed(false);
                setSelected(false);
            } else if (state == State.SELECTED) {
                other.setArmed(false);
                setPressed(false);
                setSelected(true);
            } else {
                // either "null" or DONT_CARE
                other.setArmed(true);
                setPressed(true);
                setSelected(true);
            }
        }

        /**
         * The current state is embedded in the selection / armed
         * state of the model.
         * <p/>
         * We return the SELECTED state when the checkbox is selected
         * but not armed, DONT_CARE state when the checkbox is
         * selected and armed (grey) and NOT_SELECTED when the
         * checkbox is deselected.
         */
        private State getState() {
            if (isSelected() && !isArmed()) {
                // normal black tick
                return State.SELECTED;
            }

            if (isSelected() && isArmed()) {
                // don't care grey tick
                return State.DONT_CARE;
            }

            // normal deselected
            return State.NOT_SELECTED;
        }

        /**
         * We rotate between NOT_SELECTED, SELECTED and DONT_CARE.
         */
        private void nextState() {
            final State current = getState();
            if (current == State.NOT_SELECTED) {
                setState(State.SELECTED);
            } else if (current == State.SELECTED) {
                setState(State.NOT_SELECTED);
            } else if (current == State.DONT_CARE) {
                setState(State.NOT_SELECTED);
            }
        }

        @Override
        public void setArmed(final boolean armed) {
            // Filter: No one may change the armed status except us.
        }

        @Override
        public void setEnabled(final boolean enabled) {
            // We disable focusing on the component when it is not
            // enabled.
            setFocusable(enabled);
            other.setEnabled(enabled);
        }

        @Override
        public boolean isArmed() {
            return other.isArmed();
        }

        @Override
        public boolean isSelected() {
            return other.isSelected();
        }

        @Override
        public boolean isEnabled() {
            return other.isEnabled();
        }

        @Override
        public boolean isPressed() {
            return other.isPressed();
        }

        @Override
        public boolean isRollover() {
            return other.isRollover();
        }

        @Override
        public void setSelected(final boolean selected) {
            other.setSelected(selected);
        }

        @Override
        public void setPressed(final boolean pressed) {
            other.setPressed(pressed);
        }

        @Override
        public void setRollover(final boolean rollover) {
            other.setRollover(rollover);
        }

        @Override
        public void setMnemonic(final int key) {
            other.setMnemonic(key);
        }

        @Override
        public int getMnemonic() {
            return other.getMnemonic();
        }

        @Override
        public void setActionCommand(final String actionCommand) {
            other.setActionCommand(actionCommand);
        }

        @Override
        public String getActionCommand() {
            return other.getActionCommand();
        }

        @Override
        public void setGroup(final ButtonGroup group) {
            other.setGroup(group);
        }

        @Override
        public void addActionListener(final ActionListener listener) {
            other.addActionListener(listener);
        }

        @Override
        public void removeActionListener(final ActionListener listener) {
            other.removeActionListener(listener);
        }

        @Override
        public void addItemListener(final ItemListener listener) {
            other.addItemListener(listener);
        }

        @Override
        public void removeItemListener(final ItemListener listener) {
            other.removeItemListener(listener);
        }

        @Override
        public void addChangeListener(final ChangeListener listener) {
            other.addChangeListener(listener);
        }

        @Override
        public void removeChangeListener(final ChangeListener listener) {
            other.removeChangeListener(listener);
        }

        @Override
        public Object[] getSelectedObjects() {
            return other.getSelectedObjects();
        }
    }
}

