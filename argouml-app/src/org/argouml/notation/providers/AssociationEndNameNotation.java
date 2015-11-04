/* $Id: AssociationEndNameNotation.java 19421 2011-05-13 17:56:38Z bobtarling $
 *****************************************************************************
 * Copyright (c) 2009-2011 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Michiel van der Wulp
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 2006-2007 The Regents of the University of California. All
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

package org.argouml.notation.providers;

import java.beans.PropertyChangeEvent;
import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Level;

import org.argouml.model.AddAssociationEvent;
import org.argouml.model.DeleteInstanceEvent;
import org.argouml.model.Model;
import org.argouml.model.RemoveAssociationEvent;
import org.argouml.notation.NotationProvider;

/**
 * This abstract class forms the basis of all Notation providers
 * for the text shown next to the end of an association.
 * Subclass this for all languages.
 * 
 * @author Michiel van der Wulp
 */
public abstract class AssociationEndNameNotation extends NotationProvider {

    /**
     * The constructor. 
     *
     * @param associationEnd the uml element
     */
    public AssociationEndNameNotation(Object associationEnd) {
        if (!Model.getFacade().isAAssociationEnd(associationEnd)
                && !Model.getFacade().isAConnectorEnd(associationEnd)) {
            throw new IllegalArgumentException("This is not an AssociationEnd.");
        }
    }

    @Override
    public void initialiseListener(Object modelElement) {
        addElementListener(
                modelElement, 
                new String[] {"name", "visibility", "stereotype", "taggedValue"});
        Collection stereotypes =
                Model.getFacade().getStereotypes(modelElement);
        Iterator iter = stereotypes.iterator();
        while (iter.hasNext()) {
            Object o = iter.next();
            addElementListener(
                    o, 
                    new String[] {"name", "remove"});
        }
        // We also show tagged values (the / for derived)
        for (Object uml : Model.getFacade().getTaggedValuesCollection(modelElement)) {
            addElementListener(uml);
        }
    }

    @Override
    public void updateListener(Object modelElement,
            PropertyChangeEvent pce) {
        Object obj = pce.getSource();
        if ((obj == modelElement) 
                && ("stereotype".equals(pce.getPropertyName())
                || "taggedValue".equals(pce.getPropertyName()))) {
            if (pce instanceof AddAssociationEvent 
                    && Model.getFacade().isAStereotype(pce.getNewValue())) {
                // new stereotype
                addElementListener(
                        pce.getNewValue(), 
                        new String[] {"name", "remove"});
            }
            if (pce instanceof RemoveAssociationEvent 
                    && Model.getFacade().isAStereotype(pce.getOldValue())) {
                // removed stereotype
                removeElementListener(
                        pce.getOldValue());
            }
            if (pce instanceof AddAssociationEvent
                    && Model.getFacade().isATaggedValue(pce.getNewValue())) {
                addElementListener(pce.getNewValue());
            }
            if (pce instanceof RemoveAssociationEvent
                    && Model.getFacade().isATaggedValue(pce.getOldValue())) {
                removeElementListener(pce.getOldValue());
            }
        }
    }

    public void propertyChange(PropertyChangeEvent evt) {
        if (renderer != null) {
            Object owner = renderer.getOwner(this);
            if ((owner == evt.getSource())
                    && (evt instanceof DeleteInstanceEvent)) {
                return;
            }
            if (owner != null) {
                if (Model.getUmlFactory().isRemoved(owner)) {
                    LOG.log(Level.WARNING, "Encountered deleted object during delete of "
                            + owner);
                    return;
                }
                renderer.notationRenderingChanged(this,
                        toString(owner, renderer.getNotationSettings(this)));
                if (evt instanceof AddAssociationEvent
                        || evt instanceof RemoveAssociationEvent) {
                    initialiseListener(owner);
                }
            }
        }
    }

}
