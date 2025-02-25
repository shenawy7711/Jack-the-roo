package model.card;

public abstract class Card{
	private final String name;
	private final String description;
	protected BoardManager boardManager;
	protected GameManager gameManager;
	
	//SUBCLASSES ARE NOT DONE PLEASE READ THE CARD 
	//CLASS TO MAKE THE SUBCLASSES STANDARD AND WILD.
	
	public Card(String name, String description, BoardManager boardManager, GameManager gameManager) {
		super();
		this.name = name;
		this.description = description;
		this.boardManager = new Board();
		this.gameManager = new Game();
	}

	public BoardManager getBoardManager() {
		return boardManager;
	}

	public void setBoardManager(BoardManager boardManager) {
		this.boardManager = boardManager;
	}

	public GameManager getGameManager() {
		return gameManager;
	}

	public void setGameManager(GameManager gameManager) {
		this.gameManager = gameManager;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}
}
