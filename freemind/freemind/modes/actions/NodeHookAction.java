/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2004  Joerg Mueller, Daniel Polansky, Christian Foltin and others.
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
 *
 * Created on 26.07.2004
 */
/*$Id: NodeHookAction.java,v 1.1.2.1 2004-07-30 18:29:30 christianfoltin Exp $*/
package freemind.modes.actions;

import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.Action;
import javax.swing.JMenuItem;

import freemind.controller.MenuItemEnabledListener;
import freemind.controller.actions.FreemindAction;
import freemind.extensions.HookFactory;
import freemind.extensions.HookInstanciationMethod;
import freemind.extensions.NodeHook;
import freemind.extensions.PermanentNodeHook;
import freemind.modes.MindMapNode;
import freemind.modes.ModeController;


public class NodeHookAction extends FreemindAction implements MenuItemEnabledListener {
	String hookName;
	ModeController controller;
	private static Logger logger;
	public NodeHookAction(String hookName, ModeController controller) {
		super(hookName, null, null);
		this.hookName = hookName;
		this.controller = controller;
		if(logger == null)
			logger = controller.getFrame().getLogger(this.getClass().getName());

	}

	public void actionPerformed(ActionEvent arg0) {
		// check, which method of invocation:
		// 
		invoke(controller.getSelected(), controller.getSelecteds());							
	}

	
	public void invoke(MindMapNode focussed, List selecteds) {
		logger.finest("invoke(selecteds) called.");
		HookInstanciationMethod instMethod = getInstanciationMethod();
		// get destination nodes
		Collection destinationNodes = instMethod.getDestinationNodes(controller, focussed, selecteds);
		MindMapNode adaptedFocussedNode = instMethod.getCenterNode(controller, focussed, selecteds);
		// test if hook already present
		if(instMethod.isAlreadyPresent(controller, hookName, adaptedFocussedNode, destinationNodes)){
			// remove the hook:
			for (Iterator i = destinationNodes.iterator(); i.hasNext();) {
				MindMapNode currentDestinationNode = (MindMapNode) i.next();
				// find the hook ini the current node, if present:
				for (Iterator j = currentDestinationNode.getActivatedHooks().iterator(); j
						.hasNext();) {
					PermanentNodeHook hook = (PermanentNodeHook) j.next();
					if(hook.getName().equals(hookName)) {
						currentDestinationNode.removeHook(hook);
						/* fc, 30.7.2004:
						 * we have to break. otherwise the collection is modified
						 * at two points (i.e., the collection is not valid anymore after removing
						 * one element).
						 * But this is no problem, as there exist only "once" plugins currently.
						 */ 
						break;
					}
				}
			}
		} else {
			// add the hook
			for (Iterator it = destinationNodes.iterator(); it.hasNext();) {
				MindMapNode currentDestinationNode = (MindMapNode) it.next();
				NodeHook hook = controller
						.createNodeHook(hookName, currentDestinationNode, controller.getMap());
				logger.info("created hook "+hookName);
				// call invoke.
				currentDestinationNode.invokeHook(hook);
				if (hook instanceof PermanentNodeHook) {
					PermanentNodeHook permHook = (PermanentNodeHook) hook;
					logger.info("This is a permanent hook "+ hookName);
					// the focussed receives the focus:
					if (currentDestinationNode == adaptedFocussedNode) {
						permHook.onReceiveFocusHook();
					}
					// using this method, the map is dirty now. This is important to
					// guarantee, that the hooks are saved.
					controller.nodeChanged(currentDestinationNode);
				}
			}
		}
	}


	
	/**
	 * @return
	 */
	private HookInstanciationMethod getInstanciationMethod() {
		HookFactory factory = controller.getFrame().getHookFactory();
		// determine instanciation method
		HookInstanciationMethod instMethod = factory.getInstanciationMethod(hookName);
		return instMethod;
	}

	/* (non-Javadoc)
	 * @see freemind.controller.MenuItemEnabledListener#isEnabled(javax.swing.JMenuItem, javax.swing.Action)
	 */
	public boolean isEnabled(JMenuItem item, Action action) {
		MindMapNode focussed = controller.getSelected();
		List selecteds = controller.getSelecteds();
		HookInstanciationMethod instMethod = getInstanciationMethod();
		// get destination nodes
		Collection destinationNodes = instMethod.getDestinationNodes(controller, focussed, selecteds);
		MindMapNode adaptedFocussedNode = instMethod.getCenterNode(controller, focussed, selecteds);
		// test if hook already present
		boolean isActionSelected = instMethod.isAlreadyPresent(controller, hookName, adaptedFocussedNode, destinationNodes);
		setSelected(item, isActionSelected);
		
		return true;
	} 
		
}