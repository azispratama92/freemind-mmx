/*FreeMind - A Program for creating and viewing Mindmaps
*Copyright (C) 2000-2006 Joerg Mueller, Daniel Polansky, Christian Foltin, Dimitri Polivaev and others.
*
*See COPYING for Details
*
*This program is free software; you can redistribute it and/or
*modify it under the terms of the GNU General Public License
*as published by the Free Software Foundation; either version 2
*of the License, or (at your option) any later version.
*
*This program is distributed in the hope that it will be useful,
*but WITHOUT ANY WARRANTY; without even the implied warranty of
*MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*GNU General Public License for more details.
*
*You should have received a copy of the GNU General Public License
*along with this program; if not, write to the Free Software
*Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
*/
/*
 * Created on 31.10.2005
 * Copyright (C) 2005 Dimitri Polivaev
 */
package freemind.modes;

import java.util.EventListener;

/**
 * @author Dimitri Polivaev
 * 31.10.2005
 */
public interface NodeViewEventListener extends EventListener{

    /**
     * @param nodeViewEvent
     */
    public void nodeViewCreated(NodeViewEvent nodeViewEvent);
    /**
     * @param nodeViewEvent
     */
    public void nodeViewRemoved(NodeViewEvent nodeViewEvent);
    
}
