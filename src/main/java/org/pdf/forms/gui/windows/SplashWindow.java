/*
* ===========================================
* PDF Forms Designer
* ===========================================
*
* Project Info:  http://pdfformsdesigne.sourceforge.net
* (C) Copyright 2006-2008..
* Lead Developer: Simon Barnett (n6vale@googlemail.com)
*
* This file is part of the PDF Forms Designer
*
    This library is free software; you can redistribute it and/or
    modify it under the terms of the GNU General Public
    License as published by the Free Software Foundation; either
    version 2.1 of the License, or (at your option) any later version.

    This library is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    General Public License for more details.

    You should have received a copy of the GNU General Public
    License along with this library; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA


*
* ---------------
* SplashWindow.java
* ---------------
*/
package org.pdf.forms.gui.windows;

import java.awt.BorderLayout;

import javax.swing.JWindow;

import org.pdf.forms.gui.commands.Version;

public class SplashWindow extends JWindow {

    private final SplashPanel splashPanel;

    public SplashWindow(final Version version) {
        splashPanel = new SplashPanel(version);

        getContentPane().add(splashPanel, BorderLayout.CENTER);

        setSize(717, 275);
        setLocationRelativeTo(null);

        setVisible(true);
    }

    public void setStatusMaximum(final int max) {
        splashPanel.setStatusMaximum(max);
    }

    public void setProgress(
            final int progress,
            final String text) {
        splashPanel.setProgress(progress, text);
    }
}
