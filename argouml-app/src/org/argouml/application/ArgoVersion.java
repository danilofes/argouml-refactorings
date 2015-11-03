/* $Id: ArgoVersion.java 19906 2012-12-28 18:30:46Z linus $
 *******************************************************************************
 * Copyright (c) 2009 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Tom Morris
 *******************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */
 
// Copyright (c) 1996-2008 The Regents of the University of California. All
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

package org.argouml.application;

import org.argouml.application.helpers.ApplicationVersion;

/**
 * This class <strong>only</strong> encapsulates the ArgoUML version string.<p>
 *
 * It should <strong>not</strong> be edited manually because it is
 * build from the file ArgoVersion.template when necessary.<p>
 * 
 * Since this class resides in the top-level package, 
 * none of its functionality should be used
 * outside this package.
 *
 * @author Thierry Lach
 * @since  Argo0.11.1
 */
final class ArgoVersion {
    /**
     * Version number.
     */
    private static final String VERSION = "PRE-0.35.1";
    private static final String STABLE_VERSION = "0.32";

    /**
     * Make the version of ArgoUML public. 
     */
    static void init() {
        ApplicationVersion.init(VERSION, STABLE_VERSION);
    }

    /**
     * Don't allow instantiation.
     */
    private ArgoVersion() {
    }
}
