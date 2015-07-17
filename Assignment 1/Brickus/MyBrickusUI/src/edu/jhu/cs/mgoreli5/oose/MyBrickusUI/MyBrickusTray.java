package edu.jhu.cs.mgoreli5.oose.MyBrickusUI;

import java.awt.*;
import java.util.Iterator;
import java.util.List;

import javax.swing.*;

import edu.jhu.cs.oose.fall2013.brickus.iface.BrickusModel;
import edu.jhu.cs.oose.fall2013.brickus.iface.BrickusPiece;
import edu.jhu.cs.oose.fall2013.brickus.iface.Player;
/**
 * @author Matt Gorelik
 * The MyBrickusTray class will be used to display all of a player's pieces, as well as provide control of the piece components
 * 
 */
public class MyBrickusTray extends JPanel{
	/**
	 * The current model for the game being played
	 */
	private BrickusModel model;
	/**
	 * This is the currently selected MyBrickusPieceComponent; this will be used for all the controls
	 */
	private MyBrickusPieceComponent currentSelectedPiece;
	/**
	 * This array will be used to store all of the MyBrickusPieceComponents (essentially, the 21 pieces)
	 */
	private MyBrickusPieceComponent[] pieceComponents;
	/**
	 * Constructor for MyBrickusTray, which will create the 21 MyBrickusPieceComponents as well as the layout
	 * 
	 * @param model The current model for the game being played.
	 */
	public MyBrickusTray(final BrickusModel model){
		super();
		this.model = model;
		this.setSize(new Dimension(180,200));
		pieceComponents = new MyBrickusPieceComponent[21];
		this.setLayout(new GridLayout(3,7));
		this.currentSelectedPiece = null;
		//Loop to create the 21 MyBrickusPieceComponents and place them in the Tray's array
		for(int i=0;i<21;i++){
			pieceComponents[i] = new MyBrickusPieceComponent();
			this.add(pieceComponents[i]);
		}
	}
	/**
	 * Select the right piece component based on the mouse click on the Tray
	 * The compHeight and compWidth are used to determine where each component should be located
	 * 
	 * @param x x-coordinate of the mouse
	 * @param y y-coordinate of the mouse
	 */
	public void setSelectPiece(int x, int y){
		int compHeight = this.getHeight()/3;
		int compWidth = this.getWidth()/7;
		int chosen = y/compHeight*7+x/compWidth;
		//Make sure we have not selected a null piece, and then select the piece
		if(this.pieceComponents[chosen].getPiece()!=null)
		{
			if(currentSelectedPiece!=null)
				currentSelectedPiece.setSelected(false);
			this.pieceComponents[chosen].setSelected(true);
			currentSelectedPiece = this.pieceComponents[chosen];			
		}
	}
	/**
	 * Getter for the currently selected piece component.
	 * @return currentSelectedPiece The piece component to return.
	 */
	public MyBrickusPieceComponent getCurrentSelectPiece() {
		return currentSelectedPiece;
	}
	/**
	 * Setter for the currently selected piece component.
	 * @param currentSelectPiece The newly selected piece.
	 */
	public void setCurrentSelectPiece(MyBrickusPieceComponent currentSelectPiece) {
		this.currentSelectedPiece = currentSelectPiece;
	}
	/**
	 * Used to paint the component based on their respective list of pieces from the model
	 * Also, ensures that the pieceComponents is always synced with the player's current pieces (which is why we use the iterator)
	 * See the paintComponent documentation for more information about the paintComponent method
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Player currPlayer = this.model.getActivePlayer();
		List<BrickusPiece> pl = this.model.getPieces(currPlayer);
		Iterator<BrickusPiece> it= pl.iterator();
		for(int i=0;i<21;i++){
			this.pieceComponents[i].setPlayer(currPlayer);
			if(it.hasNext())
				this.pieceComponents[i].setPiece(it.next());
			else 
				this.pieceComponents[i].setPiece(null);
		}
	}
}