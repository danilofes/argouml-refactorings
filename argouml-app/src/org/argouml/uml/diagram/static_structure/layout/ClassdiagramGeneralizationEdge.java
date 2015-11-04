/* $Id: ClassdiagramGeneralizationEdge.java 17863 2010-01-12 20:07:22Z linus $
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

// Copyright (c) 1996-2006 The Regents of the University of California. All
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

package org.argouml.uml.diagram.static_structure.layout;

import java.util.logging.Level;

import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigEdge;

/**
 *
 * @author  mkl
 */
public class ClassdiagramGeneralizationEdge
    extends ClassdiagramInheritanceEdge
{

    /**
     * The constructor.
     *
     * @param edge the fig edge
     */
    public ClassdiagramGeneralizationEdge(FigEdge edge) {
        super(edge);
    }

    /**
     * @see org.argouml.uml.diagram.layout.LayoutedEdge#layout()
     *
     * Layout the edges in a way that they form a nice inheritance tree. Try to
     * implement these nice zigzag lines between classes and works well when the
     * row difference is one.
     *
     * @author Markus Klink
     * @since 0.9.6
     */
    public void layout() {
        // now we construct the zig zag inheritance line
        //getUnderlyingFig()
        Fig fig = getUnderlyingFig();
        int centerHigh = getCenterHigh();
        int centerLow = getCenterLow();
    
        // the amount of the "sidestep"
        int difference = centerHigh - centerLow;
    
        /*
         * If center points are "close enough" we just adjust the endpoints
         * of the line a little bit.  Otherwise we add a jog in the middle to
         * deal with the offset.
         *
         * TODO: Epsilon is currently fixed, but could probably be computed
         * dynamically as 10% of the width of the narrowest figure or some
         * other value which is visually not noticeable.
         */
        if (Math.abs(difference) < EPSILON) {
            fig.addPoint(centerLow + (difference / 2 + (difference % 2)),
                    (int) (low.getLocation().getY()));
            fig.addPoint(centerHigh - (difference / 2),
                    high.getLocation().y + high.getSize().height);
        } else {
            fig.addPoint(centerLow, (int) (low.getLocation().getY()));
            LOG.log(Level.FINE,
                    "Point: x: {0} y: {1}",
                    new Object[]{ centerLow, low.getLocation().y});
    
            LOG.log(Level.FINE,
                    "Point: x: {0} y: {1}",
                    new Object[]{(centerHigh - difference), getDownGap()});
            getUnderlyingFig().addPoint(centerHigh - difference, getDownGap());
    
            LOG.log(Level.FINE,
                    "Point: x: {0} y: {1}",
                    new Object[]{centerHigh, getDownGap()});
            getUnderlyingFig().addPoint(centerHigh, getDownGap());
            
    
            LOG.log(Level.FINE,
                    "Point x: {0} y: {1}",
                    new Object[] {
                        centerHigh,
                        (high.getLocation().y + high.getSize().height)
                    });
            fig.addPoint(centerHigh, high.getLocation().y + high.getSize().height);            
        }
        fig.setFilled(false);
        getCurrentEdge().setFig(getUnderlyingFig());
        // currentEdge.setBetweenNearestPoints(false);
    }
}

