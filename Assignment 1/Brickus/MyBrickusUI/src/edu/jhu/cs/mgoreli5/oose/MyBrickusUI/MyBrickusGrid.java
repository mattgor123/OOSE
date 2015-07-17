package edu.jhu.cs.mgoreli5.oose.MyBrickusUI;

import javax.swing.*;

import java.awt.*;

import edu.jhu.cs.oose.fall2013.brickus.iface.BrickusModel;
import edu.jhu.cs.oose.fall2013.brickus.iface.BrickusPiece;
import edu.jhu.cs.oose.fall2013.brickus.iface.Player;
/**
 * This is the main Brickus Grid. It will represent the model and repaint whenever the model calls for it
 * Implementation note: Selected pieces will only display when moused over the Grid if the entire piece can be placed on the Grid.
 * @author Matt Gorelik
 */
public class MyBrickusGrid extends JComponent{
	/**
	 * The current model for the game being played
	 */
	private BrickusModel model;
	/**
	 * The currently selected piece
	 */
	private BrickusPiece thePiece;
	/**
	 * X-coordinate of the mouse upon dragging a piece onto the board
	 */
	private int xCoordPiece;
	/**
	 * Y-coordinate of the mouse upon dragging a piece onto the board
	 */
	private int yCoordPiece;
	/**
	 * MyBrickusGrid's Constructor
	 * @param model The current model for the game being played
	 */
	public MyBrickusGrid (final BrickusModel model){
		super();
		this.model = model;
		this.thePiece=null;
		this.xCoordPiece= -1;
		this.yCoordPiece = -1;
	}
	/**
	 * Used to Repaint the component whenever a piece is selected is moving on the board
	 * 
	 * @param x x-coordinate of the mouse.
	 * @param y y-coordinate of the mouse.
	 * @param piece The currently selected piece.
	 */
	public void drawPiece(int x,int y,BrickusPiece piece){
		this.xCoordPiece = x;
		this.yCoordPiece = y;
		this.thePiece= piece;	
		repaint();
	}
	/**
	 * Used to convert the given x-Coordinate into an appropriate spot on the Grid
	 * This is going to be completely necessary for resizability as well as ensuring the piece is placed on the Grid the way it is on the model
	 * @param x The x-coordinate of the mouse.
	 * @return The horizontal orientation of the mouse wrt the Grid.
	 */
	public int getXGrid(int x){
		int modelWidth = this.model.getWidth();
		int gridCellWidth = this.getWidth()/modelWidth;
		int boardTotalWidth = gridCellWidth*modelWidth;
		int horizSpace = (this.getWidth()-boardTotalWidth)/2;

		return (x - horizSpace)/gridCellWidth;
	}
	/**
	 * Used to convert the given y-Coordinate into an appropriate spot on the Grid
	 * This is going to be completely necessary for resizability as well as ensuring the piece is placed on the Grid the way it is on the model
	 * @param y The y-coordinate of the mouse.
	 * @return The vertical orientation of the mouse wrt the Grid.
	 */
	public int getYGrid(int y){
		int modelHeight = this.model.getHeight();
		int gridCellHeight = this.getHeight()/modelHeight;
		int boardTotalHeight = gridCellHeight*modelHeight;
		int vertSpace = (this.getHeight()-boardTotalHeight)/2;

		return (y - vertSpace)/gridCellHeight;
	}
	/**
	 * Used to graphically represent the model and the currently selected piece on the Grid
	 * For more information, please see Java's paintComponent documentation
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		int modelWidth = this.model.getWidth();
		int modelHeight = this.model.getHeight();
		int gridCellWidth = this.getWidth()/modelWidth;
		int gridCellHeight = this.getHeight()/modelHeight;
		int boardWidth = gridCellWidth*modelWidth;
		int boardHeight = gridCellHeight*modelHeight;
		
		int horizSpace = (this.getWidth()-boardWidth)/2;
		int vertSpace = (this.getHeight()-boardHeight)/2;

		//Draw on filled in spaces
		for(int x=0;x<modelWidth;x++)
			for(int y=0;y<modelHeight;y++){
				if(this.model.getContents(x, y)==Player.PLAYER1)
					g.setColor(Color.BLACK);
				else if(this.model.getContents(x, y)==Player.PLAYER2)
					g.setColor(Color.YELLOW);
				else g.setColor(Color.WHITE);
				//Once again, we fill in the rectangles with the color, then draw gray rectangles around them
				g.fillRect(horizSpace+x*gridCellWidth, vertSpace+y*gridCellHeight, gridCellWidth, gridCellHeight);
				g.setColor(Color.DARK_GRAY);
				g.drawRect(horizSpace+x*gridCellWidth, vertSpace+y*gridCellHeight, gridCellWidth, gridCellHeight);
			}
		
		//Now we draw our new piece on the grid if we have a piece selected
		if(this.thePiece!=null)
		{
			int xNormalized = (this.xCoordPiece - horizSpace)/gridCellWidth;
			int yNormalized = (this.yCoordPiece - vertSpace)/gridCellHeight;
			int pieceWidth = this.thePiece.getWidth();
			int pieceHeight = this.thePiece.getHeight();

			if(this.model.getActivePlayer()==Player.PLAYER1)
				g.setColor(new Color(0,0,0,150));
			else g.setColor(new Color(255,255,0,150));

			for(int i=0;i<pieceWidth;i++)
				for(int j=0;j<pieceHeight;j++)
					//Only paint the grid if this space is occupied; paint on the right grid square 
					if(this.thePiece.isOccupied(i, j))
							g.fillRect(horizSpace+(xNormalized+i)*gridCellWidth, vertSpace+(yNormalized+j)*gridCellHeight, gridCellWidth, gridCellHeight);
		}
	}
}