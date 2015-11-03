/* $Id: NavigateTargetForwardAction.java 17842 2010-01-12 19:21:22Z linus $
 *****************************************************************************
 * Copyright (c) 2009 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    tfmorris
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 2002-2006 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies.  This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason.  IN NO EVENT SHALL THE
// UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT,
// SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS,
// ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
// THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF
// SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY
// WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
// MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
// PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT,
// UPDATES, ENHANCEMENTS, OR MODIFICATIONS.

package org.argouml.ui.cmd;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.i18n.Translator;
import org.argouml.ui.targetmanager.TargetManager;
import org.tigris.gef.base.DistributeAction;

/**
 * Navigates the target one target forward in history.
 *
 * @author jaap.branderhorst@xs4all.nl
 */
class NavigateTargetForwardAction extends AbstractAction {

    /**
     * Constructor.
     */
    public NavigateTargetForwardAction() {
        super(Translator.localize("action.navigate-forward"),
                ResourceLoaderWrapper.lookupIcon("action.navigate-forward"));
        // Set the tooltip string:
        putValue(Action.SHORT_DESCRIPTION,
                Translator.localize("action.navigate-forward"));
    }

    /*
     * @see java.awt.event.ActionListener#actionPerformed(
     *         java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        TargetManager.getInstance().navigateForward();
    }

    /**
     * Action is possible only if navigateForwardPossible on targetManager
     * returns true.
     *
     * @see javax.swing.Action#isEnabled()
     */
    public boolean isEnabled() {
        return TargetManager.getInstance().navigateForwardPossible();
    }

    /**
     * Initialize submenus of the Distribute menu.
     *
     * @param distribute
     *            the Distribute menu
     */
    static void initDistributeMenu(JMenu distribute) {
        DistributeAction a = new DistributeAction(
                DistributeAction.H_SPACING);
        a.putValue(Action.SMALL_ICON,
                ResourceLoaderWrapper.lookupIcon(
                        "DistributeHorizontalSpacing"));
        JMenuItem distributeHSpacing = distribute.add(a);
        GenericArgoMenuBar.setMnemonic(distributeHSpacing,
                "distribute horizontal spacing");
        ShortcutMgr.assignAccelerator(distributeHSpacing,
                ShortcutMgr.ACTION_DISTRIBUTE_H_SPACING);
    
        a = new DistributeAction(
                DistributeAction.H_CENTERS);
        a.putValue(Action.SMALL_ICON,
                ResourceLoaderWrapper.lookupIcon(
                        "DistributeHorizontalCenters"));
        JMenuItem distributeHCenters = distribute.add(a);
        GenericArgoMenuBar.setMnemonic(distributeHCenters,
                "distribute horizontal centers");
        ShortcutMgr.assignAccelerator(distributeHCenters,
                ShortcutMgr.ACTION_DISTRIBUTE_H_CENTERS);
    
        a = new DistributeAction(
                DistributeAction.V_SPACING);
        a.putValue(Action.SMALL_ICON,
                ResourceLoaderWrapper.lookupIcon("DistributeVerticalSpacing"));
        JMenuItem distributeVSpacing = distribute.add(a);
        GenericArgoMenuBar.setMnemonic(distributeVSpacing,
                "distribute vertical spacing");
        ShortcutMgr.assignAccelerator(distributeVSpacing,
                ShortcutMgr.ACTION_DISTRIBUTE_V_SPACING);
    
        a = new DistributeAction(
                DistributeAction.V_CENTERS);
        a.putValue(Action.SMALL_ICON,
                ResourceLoaderWrapper.lookupIcon("DistributeVerticalCenters"));
        JMenuItem distributeVCenters = distribute.add(a);
        GenericArgoMenuBar.setMnemonic(distributeVCenters,
                "distribute vertical centers");
        ShortcutMgr.assignAccelerator(distributeVCenters,
                ShortcutMgr.ACTION_DISTRIBUTE_V_CENTERS);
    }

    /**
     * The UID.
     */
    private static final long serialVersionUID = -3426889296160732468L;
}
