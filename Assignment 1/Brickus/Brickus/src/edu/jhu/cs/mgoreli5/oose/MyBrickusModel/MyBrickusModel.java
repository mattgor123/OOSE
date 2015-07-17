package edu.jhu.cs.mgoreli5.oose.MyBrickusModel;
import java.util.*;
import edu.jhu.cs.oose.fall2013.brickus.iface.BrickusEvent;
import edu.jhu.cs.oose.fall2013.brickus.iface.BrickusIllegalMoveEvent;
import edu.jhu.cs.oose.fall2013.brickus.iface.BrickusListener;
import edu.jhu.cs.oose.fall2013.brickus.iface.BrickusModel;
import edu.jhu.cs.oose.fall2013.brickus.iface.BrickusPiece;
import edu.jhu.cs.oose.fall2013.brickus.iface.Player;
/**
 * This class implements the BrickusModel interface to play the game
 * @author Matt Gorelik
 */
public class MyBrickusModel implements BrickusModel {

	/* 
	 * Constants to represent the size of the board
	 */
	private final int BOARD_HEIGHT=14;
	private final int BOARD_WIDTH=14;

	/* 
	 * Pieces will be stored as separate lists for each player
	 */
	private List<BrickusPiece> player1Pieces;
	private List<BrickusPiece> player2Pieces;
	
	/*
	 * The active player
	 */
	private Player currentPlayer; 
	
	/*
	 * The listeners which will be used to handle events
	 */
	private List<BrickusListener> listeners;
		
	/*
	 * 2-way array representation of the board
	 */
	private Player[][] boardGrid;
	
	/*
	 * Used to keep track of whether the players have already made their first move, or must go in the corner
	 *
	 *Note: Could have done without these, and checked if it's the first move based on the size of player1(/2)Pieces, but this could change with alternate versions of the game
	 */
	private boolean p1HasMoved;
	private boolean p2HasMoved;
	
	/*
	 * Used to determine if the game should end if another pass occurs
	 */
	private boolean lastMoveWasPass;
	
	/**
	 * Default, parameterless constructor
	 */
	public MyBrickusModel()
	{
		this.boardGrid = new Player[BOARD_WIDTH][BOARD_HEIGHT];
		this.listeners = new ArrayList<BrickusListener>();
		this.startGame();
		this.makePieces();
	}
	
	/**
	 * Method used to initialize the variables and represent a fresh start
	 */
	private void startGame()
	{
		this.player1Pieces = new ArrayList<BrickusPiece>();
		this.player2Pieces = new ArrayList<BrickusPiece>();
		this.currentPlayer = Player.PLAYER1;
		this.p1HasMoved = false;
		this.p2HasMoved = false;
		this.lastMoveWasPass = false;		
	}
	
	/**
	 * Used to construct the grids for the 21 pieces we will use for the game
	 */
	private void makePieces()
	{
		boolean[][] pieceToAdd = new boolean[5][5];
		/*1x1 piece will have grid marked in top left
		 *Pieces marked numerically from bottom right to top left, across then up, from http://pl.cs.jhu.edu/oose/assignments/brickus-pieces.png 
		 *Pieces may not necessarily correspond to image, but they will all be equivalent through transformations
		 */
		//Piece 1
		pieceToAdd[0][0]=true;
		this.addPieces(new MyBrickusPiece(1,1, pieceToAdd, this.listeners, this));
		//Piece 2
		pieceToAdd[1][0]=true;
		this.addPieces(new MyBrickusPiece(2,1, pieceToAdd, this.listeners, this));
		//Piece 3
		pieceToAdd[1][0]=false;
		pieceToAdd[0][1]=true;
		pieceToAdd[1][1]=true;
		this.addPieces(new MyBrickusPiece(2,2, pieceToAdd, this.listeners, this));
		//Piece 4
		pieceToAdd[0][1]=false;
		pieceToAdd[1][1]=false;
		pieceToAdd[1][0]=true;
		pieceToAdd[2][0]=true;
		this.addPieces(new MyBrickusPiece(3,1, pieceToAdd, this.listeners, this));
		//Piece 5
		pieceToAdd[1][0]=true;
		pieceToAdd[2][0]=false;
		pieceToAdd[0][1]=true;
		pieceToAdd[1][1]=true;
		this.addPieces(new MyBrickusPiece(2,2,pieceToAdd,this.listeners, this));
		//Piece 6
		pieceToAdd[0][1]=false;
		pieceToAdd[2][1]=true;
		this.addPieces(new MyBrickusPiece(3,2,pieceToAdd,this.listeners, this));
		//Piece 7
		pieceToAdd[0][1]=true;
		pieceToAdd[0][0]=false;
		this.addPieces(new MyBrickusPiece(3,2,pieceToAdd,this.listeners,this));
		//Piece 8
		pieceToAdd[1][0]=false;
		pieceToAdd[0][0]=true;
		this.addPieces(new MyBrickusPiece(3,2,pieceToAdd,this.listeners,this));
		//Piece 9
		pieceToAdd[0][1]=false;
		pieceToAdd[1][1]=false;
		pieceToAdd[2][1]=false;
		pieceToAdd[1][0]=true;
		pieceToAdd[2][0]=true;
		pieceToAdd[3][0]=true;
		this.addPieces(new MyBrickusPiece(4,1,pieceToAdd,this.listeners,this));
		//Piece 10
		pieceToAdd[3][0]=false;
		pieceToAdd[0][1]=true;
		pieceToAdd[0][2]=true;
		this.addPieces(new MyBrickusPiece(3,3,pieceToAdd,this.listeners,this));
		//Piece 11
		pieceToAdd[0][1]=false;
		pieceToAdd[0][2]=false;
		pieceToAdd[3][0]=true;
		pieceToAdd[2][1]=true;
		this.addPieces(new MyBrickusPiece(4,2,pieceToAdd,this.listeners,this));
		//Piece 12
		pieceToAdd[3][0]=false;
		pieceToAdd[3][1]=true;
		this.addPieces(new MyBrickusPiece(4,2,pieceToAdd,this.listeners,this));
		//Piece 13
		pieceToAdd[3][1]=false;
		pieceToAdd[2][0]=false;
		pieceToAdd[1][1]=true;
		pieceToAdd[2][2]=true;
		this.addPieces(new MyBrickusPiece(3,3,pieceToAdd,this.listeners,this));
		//Piece 14
		pieceToAdd[2][2]=false;
		pieceToAdd[0][1]=true;
		this.addPieces(new MyBrickusPiece(3,2,pieceToAdd,this.listeners,this));
		//Piece 15
		pieceToAdd[2][0]=true;
		pieceToAdd[1][1]=false;
		this.addPieces(new MyBrickusPiece(3,2,pieceToAdd,this.listeners,this));
		//Piece 16
		pieceToAdd[1][0]=false;
		pieceToAdd[2][0]=false;
		pieceToAdd[1][1]=true;
		pieceToAdd[2][2]=true;
		this.addPieces(new MyBrickusPiece(3,3,pieceToAdd,this.listeners,this));
		//Piece 17
		pieceToAdd[1][1]=false;
		pieceToAdd[0][1]=false;
		pieceToAdd[2][2]=false;
		pieceToAdd[2][1]=false;
		pieceToAdd[1][0]=true;
		pieceToAdd[2][0]=true;
		pieceToAdd[3][0]=true;
		pieceToAdd[4][0]=true;
		this.addPieces(new MyBrickusPiece(5,1,pieceToAdd,this.listeners,this));
		//Piece 18
		pieceToAdd[4][0]=false;
		pieceToAdd[3][1]=true;
		this.addPieces(new MyBrickusPiece(4,2,pieceToAdd,this.listeners, this));
		//Piece 19
		pieceToAdd[3][0]=false;
		pieceToAdd[3][1]=false;
		pieceToAdd[1][1]=true;
		pieceToAdd[1][2]=true;
		this.addPieces(new MyBrickusPiece(3,3,pieceToAdd,this.listeners, this));
		//Piece 20
		pieceToAdd[1][0]=false;
		pieceToAdd[2][0]=false;
		pieceToAdd[0][1]=true;
		pieceToAdd[2][1]=true;
		this.addPieces(new MyBrickusPiece(3,3,pieceToAdd,this.listeners,this));
		//Piece 21
		pieceToAdd[0][0]=false;
		pieceToAdd[1][0]=true;
		this.addPieces(new MyBrickusPiece(3,3,pieceToAdd,this.listeners,this));
	}
	
	/**
	 * Simple helper method used to reduce clutter in the makePieces method
	 * @param thePiece The piece to add to the players' lists
	 */
	private void addPieces(MyBrickusPiece thePiece)
	{
		this.player1Pieces.add(thePiece);
		this.player2Pieces.add(thePiece);
	}
	
	/**
	 * Implementation of addBrickusListener from the interface
	 * @param listener The listener to add
	 */
	public void addBrickusListener(BrickusListener listener)
	{
		this.listeners.add(listener);
	}
	
	/**
	 * Implementation of calculateScore from the interface
	 * @param player The player whose score is being calculated
	 * @return theScore The player's calculated score
	 */
	public int calculateScore(Player player)
	{
		int theScore = 0;
		for (int i = 0; i < BOARD_WIDTH; i++)
			for (int j = 0; j < BOARD_HEIGHT; j++)
				if (boardGrid[i][j]==player)
					theScore++;
		return theScore;		
	}
	
	/**
	 * Implementation of the interface's getActivePlayer
	 */
	public Player getActivePlayer()
	{
		return this.currentPlayer;
	}
	
	/**
	 * Implementation of the interface's getContents
	 * @param	x	The x coordinate
	 * @param	y	The y coordinate
	 * @return 	player The player occupying said coordinate, or null if empty
	 */
	public Player getContents(int x, int y)
	{
		try {
			return this.boardGrid[x][y];
		}
		//I hope this is the way you guys wanted these exceptions handled?
		catch (IndexOutOfBoundsException e) {
			System.out.println("Please make sure you are trying to get contents of a valid square");
			throw e;
		}
	}
	
	/**
	 * Implementation of the interface's method to return the height of the board
	 */
	public int getHeight()
	{
		return this.BOARD_HEIGHT;
	}
	
	/**
	 * Implementation of the interface's method to return the width of the board
	 */
	public int getWidth()
	{
		return this.BOARD_WIDTH;
	}
	
	/**
	 * Implementation of the interface's method to return the pieces for a given Player
	 */
	public List<BrickusPiece> getPieces(Player player)
	{
		return player==Player.PLAYER1 ? this.player1Pieces : this.player2Pieces;
	}
	
	/**
	 * Implementation of the interface's method to represent the "pass" game action
	 */
	public void pass(Player player)
	{
		//Check if game should be over
		if (this.lastMoveWasPass)
		{
			this.currentPlayer=null;
			for (BrickusListener listener : listeners)
				listener.modelChanged(new BrickusEvent(this,true,true));
		}
		else
		{
			this.currentPlayer = (player == Player.PLAYER1) ? Player.PLAYER2 : Player.PLAYER1;
			this.lastMoveWasPass = true;
			for (BrickusListener listener : this.listeners)
				listener.modelChanged(new BrickusEvent(this,true,false));
		}
			
	}
	
	/**
	 * Implementation of the interface's method to represent the action of placing a piece on the board
	 */
	public void placePiece(Player player, int x, int y, BrickusPiece piece)
	{
		boolean validMove = true;
		int pieceWidth = piece.getWidth();
		int pieceHeight = piece.getHeight();		
		//Test for being on the board
		if (x + pieceWidth > this.BOARD_WIDTH || y + pieceHeight > this.BOARD_HEIGHT)
		{
			validMove = false;
			for (BrickusListener listener : this.listeners)
				listener.illegalMove(new BrickusIllegalMoveEvent("Please make sure your piece is entirely on the board"));
			return;
		}		
		//Test for overlap
		if (validMove)
		{
			for (int i = 0; i < pieceWidth; i++)
				for (int j = 0; j < pieceHeight; j++)
					if (piece.isOccupied(i, j) && boardGrid[x+i][y+j]!=null)
					{
						//Pieces are overlapping
						validMove = false;
						for (BrickusListener listener : this.listeners)
							listener.illegalMove(new BrickusIllegalMoveEvent("Please make sure your piece is not placed on top of any existing pieces"));
						return;
					}
		}
		//Test for valid first move
		if (validMove && ((player == Player.PLAYER1 && !p1HasMoved) || (player== Player.PLAYER2 && !p2HasMoved)))
		{
			//We are making the first move; ensure validity
			if ( x== 0 && y == 0 && piece.isOccupied(0,0) || //Top Left
				 x== 0 && y + pieceHeight == this.BOARD_HEIGHT && piece.isOccupied(0, pieceHeight - 1) || // Bottom Left
				 x + pieceWidth == this.BOARD_WIDTH && y + pieceHeight == this.BOARD_HEIGHT && piece.isOccupied(pieceWidth-1, pieceHeight-1) || // Bottom right
				 x + pieceWidth == this.BOARD_WIDTH && y == 0 && piece.isOccupied(pieceWidth - 1, 0)) //Top Right
			{
				validMove = true; //Just for debugging purposes, wanted to see hit count
				this.placeValidPiece(player, x, y, piece);
				return;
			}			
			//Piece should be in corner, but it's not
			else
			{
				validMove = false;
				for (BrickusListener listener : this.listeners)
					listener.illegalMove(new BrickusIllegalMoveEvent("Please ensure that your first move touches a corner"));
				return;
			}
		}
		//If good so far, we will validate that the pieces are diagonally touching, but not orthogonally touching
		if (validMove)
		{
			validMove = false; //We will set it to true only if diagonal
			for (int i = 0; i < pieceWidth ; i++)
				for (int j = 0; j < pieceHeight ; j++)
					if (piece.isOccupied(i,j))
					{	
						int xTest = x + i;
						int yTest = y + j;
						//Ensure no out of bounds errors, and validate that there is in fact a piece diagonally touching the piece we are trying to place
						if(	xTest > 0 && yTest > 0 && this.boardGrid[xTest-1][yTest-1]==player || //Top Left
							xTest > 0 && yTest + 1 < this.BOARD_HEIGHT && this.boardGrid[xTest-1][yTest+1]==player || //Bottom Left
							xTest + 1 < BOARD_WIDTH && yTest > 0 && this.boardGrid[xTest+1][yTest-1]==player || //Top Right
							xTest + 1 < BOARD_WIDTH && yTest + 1 < BOARD_HEIGHT && this.boardGrid[xTest+1][yTest+1]==player) //Bottom Right
							{
							validMove = true;
							//If we are diagonally touching, next we make sure we are not orthogonally touching.
							if(		xTest > 0 && this.boardGrid[xTest-1][yTest]==player || //Left
									yTest > 0 && this.boardGrid[xTest][yTest-1]==player || //Above
									xTest + 1 < BOARD_WIDTH && this.boardGrid[xTest+1][yTest]==player || //Right
									yTest + 1 < BOARD_HEIGHT&& this.boardGrid[xTest][yTest+1]==player) //Below
									{
										validMove = false;
										for (BrickusListener listener : this.listeners)
										listener.illegalMove(new BrickusIllegalMoveEvent("Please make sure no pieces of the same color are touching orthogonally"));
										return;
									}
							}						
					}						
		}
		if (validMove)
		{
			this.placeValidPiece(player, x, y, piece);
		}
		else
		{
			for (BrickusListener listener : this.listeners)
				listener.illegalMove(new BrickusIllegalMoveEvent("Please make sure your piece is diagonally touching one of your existing pieces"));
			return;
		}
	}
	
	/**
	 * Method to actually place a piece with the requested coordinates - will only be called if we are certain it's a valid move
	 * @param player	The player whose piece to play
	 * @param x	The x coordinate to place the origin of the piece
	 * @param y	The y coordinate to place the origin of the piece
	 * @param piece	The piece we are placing
	 */
	public void placeValidPiece(Player player, int x, int y, BrickusPiece piece)
	{
		//Iterate through the piece's grid, marking the board's grid with the right player
		for (int i=0; i < piece.getWidth(); i++)
			for (int j=0; j < piece.getHeight();j++)
				if (piece.isOccupied(i,j))
					this.boardGrid[x+i][y+j]=player;
		//Switch the active player, ensure his HasMoved flag is true, and remove the piece
		if (this.currentPlayer==Player.PLAYER1)
		{
			player1Pieces.remove(piece);
			p1HasMoved=true;
			this.currentPlayer = Player.PLAYER2;
		}
		else
		{
			player2Pieces.remove(piece);
			p2HasMoved=true;
			this.currentPlayer = Player.PLAYER1;
		}
		//Ensure this flag gets reset so the next pass doesn't end it
		this.lastMoveWasPass = false;
		for (BrickusListener listener : this.listeners)
			listener.modelChanged(new BrickusEvent(this,true,false));
		return;
	}
	
	/**
	 * Implementation of the interface's method to remove a BrickusListener from the model
	 */
	public void removeBrickusListener(BrickusListener listener)
	{
		this.listeners.remove(listener);
	}	
}
