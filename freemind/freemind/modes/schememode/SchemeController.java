/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2001  Joerg Mueller <joergmueller@bigfoot.com>
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
/*$Id: SchemeController.java,v 1.6 2001-05-06 18:47:57 ponder Exp $*/

package freemind.modes.schememode;

import freemind.main.FreeMind;
import freemind.main.FreeMindMain;
import freemind.modes.Mode;
import freemind.modes.MindMap;
import freemind.modes.MapAdapter;
import freemind.modes.MindMapNode;
import freemind.modes.ControllerAdapter;
//import silk.SI;
import java.io.File;
import java.awt.event.ActionEvent;
import java.util.StringTokenizer;
import javax.swing.Action;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.JFileChooser;

public class SchemeController extends ControllerAdapter {

    Action newMap = new NewMapAction(this);
    Action open = new OpenAction(this);
    Action save = new SaveAction(this);
    Action saveAs = new SaveAsAction(this);
    Action evaluate = new EvaluateAction();
    Action edit = new EditAction();
    Action addNew = new AddNewAction();
    Action remove = new RemoveAction();
    Action toggleFolded = new ToggleFoldedAction();

    public SchemeController(Mode mode) {
	super(mode);
    }

    public MapAdapter newModel() {
	return new SchemeMapModel(getFrame());
    }

    public MindMapNode newNode() {
	return new SchemeNodeModel(getFrame());
    }




    //private
    private MindMap getModel() {
 	return (MindMap)getController().getModel();
    }

    private MindMapNode getSelected() {
	if (getView() != null) {
	    return (MindMapNode)getView().getSelected().getModel();
	} else {
	    return null;
	}
    }


    public void saveAs() {
	JFileChooser chooser = null;
	if ((getMap().getFile() != null) && (getMap().getFile().getParentFile() != null)) {
	    chooser = new JFileChooser(getMap().getFile().getParentFile());
	} else {
	    chooser = new JFileChooser();
	}
	//chooser.setLocale(currentLocale);
	if (getFileFilter() != null) {
	    chooser.addChoosableFileFilter(getFileFilter());
	}
	int returnVal = chooser.showSaveDialog(getView());
	if (returnVal==JFileChooser.APPROVE_OPTION) {//ok pressed
	    File f = chooser.getSelectedFile();
	    //Force the extension to be .mm
	    //	    String ext = Tools.getExtension(f.getName());
	    //	    if(!ext.equals("mm")) {
	    //		f = new File(f.getParent(),f.getName()+".mm");
	    //	    }
	    save(f);
	    //Update the name of the map
	    updateMapModuleName();
	}
    }


    private class EvaluateAction extends AbstractAction {
	EvaluateAction() {
	    super(getFrame().getResources().getString("scheme_evaluate"));
	}
	public void actionPerformed(ActionEvent e) {
	    String rawCode = ((SchemeMapModel)getMap()).getCode().trim();
	    System.out.println(rawCode);
	    StringTokenizer code = new StringTokenizer(rawCode,",");
	    String output = "Output: \n";
	    //	    while(code.hasMoreTokens()) {
	    //		output = output + (SI.eval(code.nextToken()).toString())+"\n";
	    //	    }
	    JOptionPane.showMessageDialog(getView(),output);
	}
    }
}
