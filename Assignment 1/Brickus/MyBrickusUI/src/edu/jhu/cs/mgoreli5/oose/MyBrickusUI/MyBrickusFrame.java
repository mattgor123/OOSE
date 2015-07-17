package edu.jhu.cs.mgoreli5.oose.MyBrickusUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import edu.jhu.cs.oose.fall2013.brickus.iface.BrickusModel;
import edu.jhu.cs.oose.fall2013.brickus.iface.Player;
/**
 * The MyBrickusFrame is the overarching frame for my custom UI implementation
 * 
 * @author Matt Gorelik
 */
public class MyBrickusFrame extends JFrame{
	/**
	 * The current model for the game being played
	 * 	
	 */
	private BrickusModel model;
	/**
	 * This constructs the MyBrickusFrame for the game
	 * @param model Reference to the currently used model
	 */
	public MyBrickusFrame(final BrickusModel model){
		super("Welcome to Matt's Black 'n Yellow Brickus. May the best player win!");
		this.model = model;
		this.setContentPane(new MyBrickusBoard(this.model));
		final JButton newGameButton = new JButton("New Game");
		newGameButton.setFont(new Font("Dialog",Font.PLAIN,10));
		newGameButton.setBorder(BorderFactory.createEmptyBorder());
		newGameButton.setPreferredSize(new Dimension(200,20));
		newGameButton.addActionListener(new ActionListener(){

			@Override
			/**
			 * This is the listener for clicking on the newGameButton, and it gives a confirmation dialog.
			 */
			public void actionPerformed(ActionEvent arg0) {
				int dialResult = JOptionPane.showConfirmDialog(null,"Are you sure you're done with this game?");
				if (dialResult == JOptionPane.YES_OPTION)
				{
					if (model.calculateScore(Player.PLAYER1) == model.calculateScore(Player.PLAYER2))
						JOptionPane.showMessageDialog(null, "Ending on a tie? How boring.");
					else if (model.calculateScore(Player.PLAYER1) > model.calculateScore(Player.PLAYER2))
						JOptionPane.showMessageDialog(null, "Congratulations to Player 1!");
					else JOptionPane.showMessageDialog(null,"Congratulations to Player 2!");		
					dispose();
					MyBrickusMain.main(null);				
				}
				else
					JOptionPane.showMessageDialog(null,"Enjoy the rest of your game!");
			}
		});
		this.getContentPane().add(newGameButton, BorderLayout.NORTH);
		this.pack();
	}
}
