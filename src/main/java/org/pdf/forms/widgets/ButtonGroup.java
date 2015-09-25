/**
* ===========================================
* PDF Forms Designer
* ===========================================
*
* Project Info:  http://pdfformsdesigne.sourceforge.net
* (C) Copyright 2006-2008..
* Lead Developer: Simon Barnett (n6vale@googlemail.com)
*
* 	This file is part of the PDF Forms Designer
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
* ButtonGroup.java
* ---------------
*/
package org.pdf.forms.widgets;

public class ButtonGroup {
    private static int nextRadioButtonGroupNumber = 1;
    private static int nextCheckBoxGroupNumber = 1;

    private String name;

    public ButtonGroup(int type) {
        String name;
        if(type == IWidget.RADIO_BUTTON){
            name = "Radio Button Group" + nextRadioButtonGroupNumber;
            nextRadioButtonGroupNumber++;
        }else{
            name = "CheckBox Group" + nextCheckBoxGroupNumber;
            nextCheckBoxGroupNumber++;
        }
        this.name = name;
    }

    public ButtonGroup(int type, String groupName) {
        this.name = groupName;
	}

	public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
