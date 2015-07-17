package edu.jhu.cs.mgoreli5.oose.MyBrickusUI;

import java.awt.*;
import java.awt.event.*;
import java.util.Random;

import javax.swing.*;

import edu.jhu.cs.oose.fall2013.brickus.iface.BrickusModel;
import edu.jhu.cs.oose.fall2013.brickus.iface.BrickusPiece;
import edu.jhu.cs.oose.fall2013.brickus.iface.Player;
import edu.jhu.cs.oose.fall2013.brickus.iface.BrickusListener;
import edu.jhu.cs.oose.fall2013.brickus.iface.BrickusEvent;
import edu.jhu.cs.oose.fall2013.brickus.iface.BrickusIllegalMoveEvent;

/**
 * This is my implementation of the board. It will control all the components, 
 * as well as contain all of the listeners for the project
 * @author Matt Gorelik
 */
public class MyBrickusBoard extends JPanel{
	/**
	 * The current model for the game being played
	 */
	private BrickusModel model;
	/**
	 * This will be the panel used for displaying pieces
	 */
	private MyBrickusTray piecePanel;	
	/**
	 * For a bit of fun, I decided to create arrays of victory messages for each player which will be randomly selected on game end.
	 */
	private String[] p1victories = {"Game over, player 1 has won!", "Game over, player 1 is the champion!", "Game over, better luck next time player 2!", "Game over. Player 2, how does it feel to be a LOSER?", "Game over. Player 1, I hope you have a celebratory crown"};
	private String[] p2victories = {"Game over, player 2 has won!", "Game over, player 2 is the champion!", "Game over, better luck next time player 1!", "Game over. Player 1, how does it feel to be a LOSER?", "Game over. Player 2, I hope you have a celebratory crown"};
	/**
	 * This Label will be used to display the appropriate error messages, as well as other text at the bottom (such as victory)
	 */
	private JLabel textToDisplay;
	/**
	 * This Label will be used to display Player 1's Points
	 */
	private JLabel p1Points;
	/**
	 * This Label will be used to display Player 2's Points
	 */
	private JLabel p2Points;
	/**
	 * This is the Custom implementation of the game's Grid
	 */
	private MyBrickusGrid theGrid;
	/**
	 * This is the button which will be used to pass a turn
	 */
	private JButton passingButton;
	/**
	 * constructor of MyBrickusBoard, it create all the components of the view and set all the 
	 * relative listener to them
	 * 
	 * @param MODEL The current model for the game being played; final used because we DO NOT want to change the model reference once it has been createed
	 */
	public MyBrickusBoard (final BrickusModel MODEL){
		super();
		this.model = MODEL;
		//Add the event listener to the model
		this.model.addBrickusListener(new BrickusListener(){
			@Override
			/*
			 * Here, we will create the necessary events for the BrickusListener as presented by the interface
			 * The first is modelChanged, in which we will use to determine if the game should be ended or if the player has changed
			 */
			public void modelChanged(BrickusEvent event) {
				if(event.isPlayerChanged()){
					//Make sure nothing is selected to draw
					theGrid.drawPiece(-1, -1, null);
					if(piecePanel.getCurrentSelectPiece()!=null){
						//We should make sure we don't have a selected piece just in case
						piecePanel.getCurrentSelectPiece().setSelected(false);
						piecePanel.setCurrentSelectPiece(null);
					}
					textToDisplay.setText("");
					if(event.isGameOver()){						
						//Select the random game over message
						Random generator = new Random();
						int i = generator.nextInt(5);
						if (model.calculateScore(Player.PLAYER1) == model.calculateScore(Player.PLAYER2))
						{
							textToDisplay.setText("Game over, the game is tied");
						}
						else if (model.calculateScore(Player.PLAYER1) > model.calculateScore(Player.PLAYER2))
						{
							textToDisplay.setText(p1victories[i]);
						}
						else 
						{
							textToDisplay.setText(p2victories[i]);
						}
						//Remove the tray from the panel, it is no longer necessary
						JPanel p = (JPanel)getComponent(1);
						p.remove(piecePanel);
						passingButton.setEnabled(false);
					}
				}
				p1Points.setText("<html><font color=black>Score:"+MODEL.calculateScore(Player.PLAYER1)+"</font></html>");
				p2Points.setText("<html><font color=#CCCC00>Score:"+MODEL.calculateScore(Player.PLAYER2)+"</font></html>");
				repaint();
			}
			
			@Override
			/**
			 * This method will put the appropriate text in the textToDisplay panel
			 * @param arg0 The illegal move event trigger
			 */
			public void illegalMove(BrickusIllegalMoveEvent arg0) {
				
				textToDisplay.setText(arg0.getMessage());
			}
		});//A correct listener has been added
		//Set size to something that looks good on my machine; can be resized so using Dimensions
		this.setPreferredSize(new Dimension(600,660));
		this.setLayout(new BorderLayout());
		JPanel lowPanel= new JPanel();
		lowPanel.setLayout(new BorderLayout());
		//Pass the model to the MyBrickusTray constructor to create the panel for the pieces
		this.piecePanel = new MyBrickusTray(this.model);
		//Choose an appropriate Tray size for my machine
		this.piecePanel.setPreferredSize(new Dimension(60,240));
		//Add the mouse listener
		this.piecePanel.addMouseListener(new MouseListener(){
			@Override
			/**
			 * We will be flipping the pieces when the right mouse is clicked
			 * Direction is depending on the SHIFT key
			 * None of the other mouse events impact the behavior
			 * And if we left click on a piece, we want it to become the selected piece (logic in MyBrickusTray)
			 */
			public void mouseClicked(MouseEvent arg0) {
				if(arg0.getModifiers()==InputEvent.BUTTON1_MASK){
					piecePanel.setSelectPiece(arg0.getX(), arg0.getY());
					piecePanel.repaint();
				} else if(arg0.getModifiers()==InputEvent.BUTTON3_MASK){
					if(piecePanel.getCurrentSelectPiece()!=null){
						BrickusPiece p = piecePanel.getCurrentSelectPiece().getPiece();
						p.flipHorizontally();
					}
				} else if(arg0.getModifiers()==(InputEvent.BUTTON3_MASK | InputEvent.SHIFT_MASK)){
					if(piecePanel.getCurrentSelectPiece()!=null){
						BrickusPiece p = piecePanel.getCurrentSelectPiece().getPiece();
						p.flipVertically();
					}
				}
			}
			@Override
			public void mouseEntered(MouseEvent arg0) {
				// Auto-generated method stub

			}
			@Override
			public void mouseExited(MouseEvent arg0) {
				// Auto-generated method stub

			}
			@Override
			public void mousePressed(MouseEvent arg0) {
				// Auto-generated method stub

			}
			@Override
			public void mouseReleased(MouseEvent arg0) {
				// Auto-generated method stub

			}
		});
		//Add the mouse wheel listener
		this.piecePanel.addMouseWheelListener(new MouseWheelListener(){
			/**
			 * The mouse wheel scrolling upwards should rotate it clockwise, otherwise rotate counterclockwise
			 */
			@Override
			public void mouseWheelMoved(MouseWheelEvent arg0) {
				if(piecePanel.getCurrentSelectPiece()!=null){
					BrickusPiece p = piecePanel.getCurrentSelectPiece().getPiece();
					if(arg0.getWheelRotation()>0)
						p.rotateClockwise();
					else p.rotateCounterClockwise();
				}
			}
		});
		this.passingButton = new JButton("Pass");
		this.passingButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
					model.pass(model.getActivePlayer());
				}
		});

		JPanel scoreWithMsgPanel = new JPanel();
		scoreWithMsgPanel.setLayout(new BorderLayout());

		this.textToDisplay = new JLabel("");
		this.textToDisplay.setBorder(BorderFactory.createLoweredSoftBevelBorder());

		JPanel scorePanel = new JPanel();
		this.p1Points = new JLabel("              ");
		this.p1Points.setBorder(BorderFactory.createLoweredSoftBevelBorder());
		this.p2Points = new JLabel("              ");
		this.p2Points.setBorder(BorderFactory.createLoweredSoftBevelBorder());
		scorePanel.setLayout(new BoxLayout(scorePanel,BoxLayout.X_AXIS));
		scorePanel.add(this.p1Points);
		scorePanel.add(this.p2Points);

		scoreWithMsgPanel.add(this.textToDisplay,BorderLayout.CENTER);
		scoreWithMsgPanel.add(scorePanel,BorderLayout.EAST);

		lowPanel.add(this.piecePanel,BorderLayout.CENTER);
		lowPanel.add(passingButton,BorderLayout.EAST);
		lowPanel.add(scoreWithMsgPanel,BorderLayout.SOUTH);
		//We have finished constructing the lower panel; now we must make the grid panels
		
		this.theGrid = new MyBrickusGrid(this.model);
		this.theGrid.addMouseMotionListener(new MouseMotionListener(){

			@Override
			public void mouseDragged(MouseEvent arg0) {
				// Auto-generated method stub

			}

			/**
			 * This is where we listen to redraw the piece if there is a piece selected (calling theGrid's drawPiece)
			 */
			@Override
			public void mouseMoved(MouseEvent arg0) {
				if(piecePanel.getCurrentSelectPiece()!=null)
					theGrid.drawPiece(arg0.getX(), arg0.getY(), piecePanel.getCurrentSelectPiece().getPiece());
			}

		});
		this.theGrid.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent arg0) {
				//If it's left mouse click, we are trying to place the piece
				if(arg0.getModifiers()==InputEvent.BUTTON1_MASK){
					if(piecePanel.getCurrentSelectPiece()!=null){						
							model.placePiece(model.getActivePlayer(), theGrid.getXGrid(arg0.getX()), theGrid.getYGrid(arg0.getY()), piecePanel.getCurrentSelectPiece().getPiece());
					}
				}
				//If it's a right click, we want to rotate the piece on the grid itself (same logic as before with the direction)
				else if(arg0.getModifiers()==InputEvent.BUTTON3_MASK){
					if(piecePanel.getCurrentSelectPiece()!=null){
						BrickusPiece p = piecePanel.getCurrentSelectPiece().getPiece();
						p.flipHorizontally();
					}
				} else if(arg0.getModifiers()==(InputEvent.BUTTON3_MASK | InputEvent.SHIFT_MASK)){
					if(piecePanel.getCurrentSelectPiece()!=null){
						BrickusPiece p = piecePanel.getCurrentSelectPiece().getPiece();
						p.flipVertically();
					}
				}
			}
			@Override
			public void mouseEntered(MouseEvent arg0) {
				// Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				theGrid.drawPiece(-1, -1, null);
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// Auto-generated method stub

			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// Auto-generated method stub

			}

		});
		this.theGrid.addMouseWheelListener(new MouseWheelListener(){

			/**
			 * We also need to rotate the piece on the grid itself, so we perform the same mouse wheel listener logic.
			 */
			@Override
			public void mouseWheelMoved(MouseWheelEvent arg0) {
				if(piecePanel.getCurrentSelectPiece()!=null){
					BrickusPiece p = piecePanel.getCurrentSelectPiece().getPiece();
					if(arg0.getWheelRotation()>0)p.rotateClockwise();
					else p.rotateCounterClockwise();
				}
			}
		});
		this.add(this.theGrid,BorderLayout.CENTER);
		this.add(lowPanel,BorderLayout.SOUTH);
	}
}