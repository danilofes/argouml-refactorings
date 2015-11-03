/* $Id: ActionDeployProfile.java 19907 2012-12-30 13:06:01Z closettop_nightlybuild $
 *****************************************************************************
 * Copyright (c) 2009-2012 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    thn
 *    euluis
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 2009 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason. IN NO EVENT SHALL THE
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

package org.argouml.ui.explorer;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Collection;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;

import org.argouml.application.api.Argo;
import org.argouml.configuration.Configuration;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.persistence.PersistenceManager;
import org.argouml.persistence.ProjectFileView;
import org.argouml.profile.ProfileException;
import org.argouml.profile.ProfileFacade;
import org.argouml.profile.ProfileManager;
import org.argouml.profile.UserDefinedProfile;
import org.argouml.ui.ProjectBrowser;
import org.argouml.ui.explorer.rules.PerspectiveRule;
import org.argouml.util.ArgoFrame;

/**
 * Explorer action for deploying a user editable profile. Deploying means: save
 * it as an XMI file, immediately load it as a user profile, but don't activate
 * it (pointless, because a profile cannot be applied to itself), and also don't
 * change the configuration (so it will only be loaded on every startup when it
 * is deployed into a default profile directory).
 *
 * @author Thomas Neustupny
 */
public class ActionDeployProfile extends AbstractAction {

    private static final Logger LOG =
        Logger.getLogger(ActionDeployProfile.class.getName());

    private Object undeployedProfile;

    /**
     * Default Constructor
     *
     * @param profile the selected profile
     */
    public ActionDeployProfile(Object profile) {
        super(Translator.localize("action.deploy-profile") + "...");
        undeployedProfile = profile;
    }

    public void actionPerformed(ActionEvent arg0) {
        // get one of the default profile dirs, if available
        // (as a default value for the following save dialog)
        ProfileManager profileManager = ProfileFacade.getManager();
        StringBuffer path = new StringBuffer();
        Collection dirs = profileManager.getSearchPathDirectories();
        if (dirs != null && !dirs.isEmpty()) {
            String s = (String) dirs.iterator().next();
            path.append(s);
            if (!(s.endsWith("/") || s.endsWith("\\"))) {
                path.append('/');
            }
        }
        // save profile
        path.append(Model.getFacade().getName(undeployedProfile));
        File f = saveProfile(path.toString());
        if (f == null) {
            return;
        }
        // register it as a user profile
        try {
            profileManager.registerProfile(new UserDefinedProfile(f,
                profileManager));
        } catch (ProfileException e) {
            LOG.log(Level.WARNING, "failed to load profile from file " + f.getPath(), e);
        }
    }

    /**
     * Offers the saving of the profile by presenting a file dialog.
     *
     * @param fn the suggested default path for the file dialog
     * @return the File instance where the profile is saved, null on abort
     */
    private File saveProfile(String fn) {
        File theFile = null;
        PersistenceManager pm = PersistenceManager.getInstance();
        // show a chooser dialog for the file name, only xmi is allowed
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle(Translator.localize("action.deploy-profile"));
        chooser.setFileView(ProjectFileView.getInstance());
        chooser.setApproveButtonText(Translator.localize("filechooser.export"));
        chooser.setAcceptAllFileFilterUsed(true);
        pm.setXmiFileChooserFilter(chooser);
        if (fn.length() > 0) {
            fn = PersistenceManager.getInstance().getBaseName(fn);
            chooser.setSelectedFile(new File(fn));
        }
        int result = chooser.showSaveDialog(ArgoFrame.getFrame());
        if (result == JFileChooser.APPROVE_OPTION) {
            theFile = chooser.getSelectedFile();
            if (theFile != null) {
                Model.getExtensionMechanismsHelper().makeProfileApplicable(
                    undeployedProfile);
                String name = theFile.getName();
                name = pm.fixXmiExtension(name);
                theFile = new File(theFile.getParent(), name);
                ProjectBrowser.getInstance().trySaveWithProgressMonitor(true,
                        theFile, false);
            }
        }
        return theFile;
    }
    
    /**
     * Tries to load user defined perspectives, if it can't it loads the
     * (predefined) default perspectives.
     * @param perspectiveManager 
     */
    public void loadUserPerspectives(PerspectiveManager perspectiveManager) {

        String userPerspectives =
            Configuration.getString(
                    Argo.KEY_USER_EXPLORER_PERSPECTIVES, "");

        StringTokenizer pst = new StringTokenizer(userPerspectives, ";");

        if (pst.hasMoreTokens()) {

            // load user perspectives
            while (pst.hasMoreTokens()) {
                String perspective = pst.nextToken();
                StringTokenizer perspectiveDetails =
                    new StringTokenizer(perspective, ",");

                // get the perspective name
                String perspectiveName = perspectiveDetails.nextToken();

                ExplorerPerspective userDefinedPerspective =
                    new ExplorerPerspective(perspectiveName);

                // make sure there are some rules...
                if (perspectiveDetails.hasMoreTokens()) {

                    // get the rules
                    while (perspectiveDetails.hasMoreTokens()) {

                        // get the rule name
                        String ruleName = perspectiveDetails.nextToken();

                        // create the rule
                        try {
                            Class ruleClass = Class.forName(ruleName);

                            PerspectiveRule rule =
                                (PerspectiveRule) ruleClass.newInstance();

                            userDefinedPerspective.addRule(rule);
                        } catch (ClassNotFoundException e) {
                            LOG.log(Level.SEVERE,
                                    "could not create rule " + ruleName
                                    + " you can try to "
                                    + "refresh the perspectives to the "
                                    + "default settings.",
                                    e);
                        } catch (InstantiationException e) {
                            LOG.log(Level.SEVERE,
                                    "could not create rule " + ruleName
                                    + " you can try to "
                                    + "refresh the perspectives to the "
                                    + "default settings.",
                                    e);
                        } catch (IllegalAccessException e) {
                            LOG.log(Level.SEVERE,
                                    "could not create rule " + ruleName
                                    + " you can try to "
                                    + "refresh the perspectives to the "
                                    + "default settings.",
                                    e);
                        }
                    }
                } else {
                    // rule name but no rules
                    continue;
                }

                // add the perspective
                perspectiveManager.addPerspective(userDefinedPerspective);
            }
        } else {
            // no user defined perspectives
            perspectiveManager.loadDefaultPerspectives();
        }

        // one last check that some loaded.
        if (perspectiveManager.getPerspectives().size() == 0) {
            perspectiveManager.loadDefaultPerspectives();
        }
    }
}
