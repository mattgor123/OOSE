package edu.jhu.cs.mgoreli5.oose.MyBrickusUI;

import javax.swing.JFrame;
import edu.jhu.cs.oose.fall2013.brickus.iface.BrickusModel;
/**
 * this is the main class of my custom Brickus view, it'll use my custom 
 * game model if the arg of the main methods is empty.To run another model,
 * simply type the class path of the model in the arg 1.
 * 
 * @author Matt Gorelik
 */
public class MyBrickusMain {
	public static void main(String[] arg) {
		BrickusModel model = new edu.jhu.cs.oose.fall2013.brickus.model.StandardBrickusModel();
		MyBrickusFrame gui = new MyBrickusFrame(model);
		gui.setLocationRelativeTo(null);
		gui.setVisible(true);
		gui.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
}
