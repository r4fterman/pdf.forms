package org.pdf.forms.gui.properties.customcomponents.tridstatecheckbox;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.SwingUtilities;
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
 * 6. You have to make a TristateDecorator as a button model that
 * wraps the original button model and does state management.
 */
public class TristateCheckBox extends JCheckBox {

    /**
     * This is a type-safe enumerated type.
     */
    public static class State {
    }

    public static final State NOT_SELECTED = new State() {
        @Override
        public String toString() {
            return "NOT_SELECTED";
        }
    };
    public static final State SELECTED = new State() {
        @Override
        public String toString() {
            return "SELECTED";
        }
    };
    public static final State DONT_CARE = new State() {
        @Override
        public String toString() {
            return "DONT_CARE";
        }
    };

    private final TristateDecorator model;

    public TristateCheckBox(
            final String text,
            final Icon icon,
            final State initial,
            final TristateCheckBoxParent parent) {
        super(text, icon);
        super.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(final MouseEvent e) {
                grabFocus();
                model.nextState();

                parent.checkboxClicked(e);
            }
        });

        final ActionMap map = new ActionMapUIResource();
        map.put("pressed", new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                grabFocus();
                model.nextState();
            }
        });
        map.put("released", null);
        SwingUtilities.replaceUIActionMap(this, map);

        // set the model to the adapted model
        model = new TristateDecorator(getModel());
        setModel(model);
        setState(initial);
    }

    public TristateCheckBox(
            final String text,
            final State initial,
            final TristateCheckBoxParent parent) {
        this(text, null, initial, parent);
    }

    public TristateCheckBox(
            final String text,
            final TristateCheckBoxParent parent) {
        this(text, DONT_CARE, parent);
    }

    public TristateCheckBox(final TristateCheckBoxParent parent) {
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
        model.setState(state);
    }

    /**
     * Return the current state, which is determined by the
     * selection status of the model.
     */
    public State getState() {
        return model.getState();
    }

    @Override
    public void setSelected(final boolean selected) {
        if (selected) {
            setState(SELECTED);
        } else {
            setState(NOT_SELECTED);
        }
    }

    /**
     * Exactly which Design Pattern is this?  Is it an Adapter,
     * a Proxy or a Decorator?  In this case, my vote lies with the
     * Decorator, because we are extending functionality and
     * "decorating" the original model with a more powerful model.
     */
    private final class TristateDecorator implements ButtonModel {
        private final ButtonModel other;

        private TristateDecorator(final ButtonModel other) {
            this.other = other;
        }

        private void setState(final State state) {
            if (state == NOT_SELECTED) {
                other.setArmed(false);
                setPressed(false);
                setSelected(false);
            } else if (state == SELECTED) {
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
                return SELECTED;
            }

            if (isSelected() && isArmed()) {
                // don't care grey tick
                return DONT_CARE;
            }

            // normal deselected
            return NOT_SELECTED;
        }

        /**
         * We rotate between NOT_SELECTED, SELECTED and DONT_CARE.
         */
        private void nextState() {
            final State current = getState();
            if (current == NOT_SELECTED) {
                setState(SELECTED);
            } else if (current == SELECTED) {
                setState(NOT_SELECTED);
            } else if (current == DONT_CARE) {
                setState(NOT_SELECTED);
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

