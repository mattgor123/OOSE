package edu.jhu.cs.mgoreli5.oose.MyBrickusUI;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JComponent;

import edu.jhu.cs.oose.fall2013.brickus.iface.BrickusPiece;
import edu.jhu.cs.oose.fall2013.brickus.iface.Player;
/**
 * This is the Custom component for each BrickusPiece
 * 
 * @author Matt Gorelik
 */
public class MyBrickusPieceComponent extends JComponent{
	/**
	 * The BrickusPiece which we are going to be draw as a MyBrickusPieceComponent
	 */
	private BrickusPiece thePiece;
	/**
	 * Also, it is important that we know the current Player and can attach a pieceComponent to a player
	 */
	private Player thePlayer;
	/**
	 * Determine if this component is the currently selected one (which we will use throughout the implementation to determine what to draw)
	 */
	private boolean isSelected;

	/**
	 * Constructs a new MyBrickusPieceComponent
	 */
	public MyBrickusPieceComponent(){
		super();
		this.isSelected = false;
		this.thePiece = null;
		this.thePlayer = null;
	}
	/**
	 * Setter for the currently active player.
	 * @param player The currently active player
	 */
	public void setPlayer(Player player) {
		this.thePlayer = player;
	}
	/**
	 * Setter for the piece to draw
	 * @param piece The piece to draw
	 */
	public void setPiece(BrickusPiece piece) {
		this.thePiece = piece;
	}
	/**
	 * Setter for whether the piece is the currently selected
	 * @param isChosen Whether the piece is the currently selected one
	 */
	public void setSelected(boolean isChosen) {
		this.isSelected = isChosen;
	}
	/**
	 * Public getter for the BrickusPiece which the component is representing.
	 * @return thePiece The represented piece
	 */
	public BrickusPiece getPiece() {
		return thePiece;
	}
	/**
	 * Overrides the graphics methods of the component, which will be used to draw the selected piece and give it a background
	 * See Java's paintComponent documentation for additional information
	 * Black & Yellow is clearly the best color scheme, so Player 1 = Black, Player 2 = Yellow.
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(this.thePiece == null)
			return;
		
		Color currColor;

		if(this.thePlayer == Player.PLAYER1)
			currColor= Color.BLACK;
		else currColor = Color.YELLOW;

		int pWidth = this.thePiece.getWidth();
		int pHeight = this.thePiece.getHeight();
		int cWidth = this.getWidth()/5;
		int cHeight = this.getHeight()/5;
		
		//So we avoid "collisions" or painting over neighboring pieces, we will paint the pieces from the center of the Component
		int xMid = (5-pWidth)/2;
		int yMid = (5-pHeight)/2;

		if(this.isSelected){
			g.setColor(Color.CYAN);
			g.fillRect(0, 0, this.getWidth(), this.getHeight());
		}
		//Iterate through the possible cells of the piece to determine which should be filled in and which are fine to leave as is.
		for(int x=xMid;x<xMid+pWidth;x++)
			for(int y=yMid;y<yMid+pHeight;y++){
				if(this.thePiece.isOccupied(x-xMid, y-yMid)){
					//If it's occupied, we will paint it the player's color, and make sure we have the cell borders visible to avoid confusion
					g.setColor(currColor);
					g.fillRect(x*cWidth, y*cHeight, cWidth, cHeight);
					g.setColor(Color.GRAY);
					g.drawRect(x*cWidth, y*cHeight, cWidth, cHeight);	
				}
			}
	}
}