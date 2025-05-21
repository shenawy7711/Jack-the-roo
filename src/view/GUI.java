package view;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;

import javafx.animation.PauseTransition;
import javafx.util.Duration;


import java.io.IOException;
import java.util.ArrayList;

import engine.Game;
import engine.board.Cell;
import exception.GameException;
import exception.IllegalSwapException;
import exception.InvalidCardException;
import exception.InvalidMarbleException;
import exception.SplitOutOfRangeException;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.Colour;
import model.card.Card;
import model.card.standard.Jack;
import model.card.standard.Standard;
import model.card.standard.Suit;
import model.player.Marble;
import model.player.Player;

public class GUI extends Application implements EventHandler<ActionEvent>{

	private AnchorPane root;
	private Scene startscene;
	private Scene gamescene;
	private Stage primarystage;
	private Label playernamelabel;
	private TextField namefield;
	private Button startbutton;
	private HBox imagebox;
	private ImageView image;
	private Game game;
	private ArrayList<Button> fulltrack;
	private VBox vtrack1;
	private HBox htrack2;
	private VBox vtrack2;
	private VBox vsafezone1;
	private HBox hsafezone1;
	private VBox vsafezone2;
	private HBox hsafezone2;
	private Button firepit;
	private HBox homezone0;
	private HBox homezone1;
	private HBox homezone2;
	private HBox homezone3;
	private Label realplayer;
	private Label ai1;
	private Label ai2;
	private Label ai3;
	private HBox htrack1left;
	private HBox htrack1right;
	private Button clear;
	private Button endturn;
	private Label curplayer;

	private HBox cardsbox;
	private ArrayList<Button> cardsbuttons;
	private Label firepitlabel;
	private TextField splitdistance;
	private Label splitdistancelabel;
	private Label winner;
	private Scene endscene;

	@Override
	public void start(Stage primarystage) throws Exception {
		this.primarystage = primarystage;
		root = new AnchorPane();
		startscene = new Scene(root,800,600);
		primarystage.setScene(startscene);
		primarystage.show();
		primarystage.setResizable(false);
		playernamelabel = new Label("Player name");
		playernamelabel.setFont(new Font("Times new roman",24));
		createcomp(playernamelabel, 240,350, 200, 50);
		namefield = new TextField();
		createcomp(namefield,390,360,200,40);
		namefield.setPromptText("please enter a name");
		namefield.getParent().requestFocus();
		startbutton = new Button("Start Game");
		createcomp(startbutton,300,450,200,50);
		startbutton.setOnAction(this::handle);
		imagebox = new HBox();
		image = new ImageView("Logo.png");
		imagebox.getChildren().add(image);
		createcomp(imagebox, 195, 20, 410, 275);
		root.setBackground(new Background(new BackgroundFill(Color.ORANGE, CornerRadii.EMPTY, Insets.EMPTY)));
		startbutton.setStyle("-fx-background-color: #604110; -fx-text-fill: white;");
        startbutton.setOnMouseEntered(e ->startbutton.setStyle("-fx-background-color: black; -fx-text-fill: white;"));
        startbutton.setOnMouseExited(e ->startbutton.setStyle("-fx-background-color: #604110; -fx-text-fill: white;"));
	}
	
	public void gamescene() {
		try {
			game = new Game(namefield.getText());
		}catch(IOException e) {
			System.out.println(e);
		}
		root = new AnchorPane();
		Image bgImage = new Image("Background.png");
		BackgroundImage backgroundImage = new BackgroundImage(
		    bgImage,
		    BackgroundRepeat.NO_REPEAT,
		    BackgroundRepeat.NO_REPEAT,
		    BackgroundPosition.DEFAULT,
		    BackgroundSize.DEFAULT
		);
		root.setBackground(new Background(backgroundImage));
		gamescene = new Scene(root,1000,900);
		primarystage.setScene(gamescene);
		primarystage.show();
		primarystage.setResizable(false);
		primarystage.setX(460);
		primarystage.setY(40);
		fulltrack = new ArrayList<Button>();
		for(int i=0;i<100;i++) {
			Button x = new Button();
			x.setPrefSize(22, 22);
			x.setOnAction(this);
			fulltrack.add(x);
		}
		htrack1left= new HBox();
		createcomp(htrack1left,225,725,286,22);
		htrack1right= new HBox();
		createcomp(htrack1right,467,725,264,22);
		vtrack1= new VBox();
		createcomp(vtrack1,203,175,22,550);
		htrack2= new HBox();
		createcomp(htrack2,225,153,550,22);
		vtrack2= new VBox();
		createcomp(vtrack2,775,175,22,550);
		vsafezone1= new VBox();
		createcomp(vsafezone1,489,637,22,88);
		hsafezone1= new HBox();
		createcomp(hsafezone1,225,439,88,22);
		vsafezone2= new VBox();
		createcomp(vsafezone2,489,175,22,88);
		hsafezone2= new HBox();
		createcomp(hsafezone2,687,439,88,22);
		firepitlabel= new Label();
		ImageView fp = new ImageView("Fire Pit.png");
		firepitlabel.setGraphic(fp);
		createcomp(firepitlabel,430,300,150,49);
		firepit = new Button();
		firepit.setStyle("-fx-background-color: orange; -fx-text-fill: black;");
		firepit.setFont(new Font("Times New Roman",25));
		createcomp(firepit,405,350,200,200);
		homezone0= new HBox();
		createcomp(homezone0,550,775,88,22);
		homezone1= new HBox();
		createcomp(homezone1,20,250,88,22);
		homezone2= new HBox();
		createcomp(homezone2,550,110,88,22);
		homezone3= new HBox();
		createcomp(homezone3,825,250,88,22);
		Font F = new Font("Times New Roman",25);
		realplayer = new Label();
		realplayer.setFont(F);
		createcomp(realplayer,290,775,250,30);
		ai1 = new Label();
		ai1.setFont(F);
		createcomp(ai1,20,200,250,30);
		ai2 = new Label();
		ai2.setFont(F);
		createcomp(ai2,290,110,250,30);
		ai3 = new Label();
		ai3.setFont(F);
		createcomp(ai3,825,200,250,30);
		realplayer.setTextFill(Color.WHITE);
		ai1.setTextFill(Color.WHITE);
		ai2.setTextFill(Color.WHITE);
		ai3.setTextFill(Color.WHITE);
		htrack1left.setSpacing(0);
		htrack1right.setSpacing(0);
		vtrack1.setSpacing(0);
		htrack2.setSpacing(0);
		vtrack2.setSpacing(0);
		clear= new Button("Clear Selection");
		createcomp(clear,30,740,140,40);
		clear.setOnAction(this);
		clear.setStyle("-fx-background-color: orange; -fx-text-fill: black;");
		clear.setOnMouseEntered(e ->clear.setStyle("-fx-background-color: black; -fx-text-fill: orange;"));
		clear.setOnMouseExited(e ->clear.setStyle("-fx-background-color: orange; -fx-text-fill: black;"));
		endturn= new Button("End Turn");
		createcomp(endturn,30,780,140,40);
		endturn.setOnAction(this);
		endturn.setStyle("-fx-background-color: orange; -fx-text-fill: black;");
		endturn.setOnMouseEntered(e ->endturn.setStyle("-fx-background-color: black; -fx-text-fill: orange;"));
		endturn.setOnMouseExited(e ->endturn.setStyle("-fx-background-color: orange; -fx-text-fill: black;"));
		curplayer = new Label();
		curplayer.setFont(F);
		curplayer.setTextFill(Color.WHITE);
		createcomp(curplayer,10,10,350,30);
		cardsbox = new HBox();
		createcomp(cardsbox,250,830,500,150);
		splitdistancelabel = new Label("Split Distance");
		splitdistancelabel.setFont(F);
		splitdistancelabel.setTextFill(Color.WHITE);
		createcomp(splitdistancelabel,830,775,200,30);
		splitdistance= new TextField();
		createcomp(splitdistance,830,830,60,50);
		splitdistance.setPromptText("1-6");
		splitdistance.getParent().requestFocus();
	}
	public void gameengine() {
	    updateTrackSection(htrack1left, 10, 0);
	    updateTrackSection(htrack1right, 99, 86);
	    updateTrackSection(vtrack1, 35, 11);
	    updateTrackSection(htrack2, 36, 60);
	    updateTrackSection(vtrack2, 61, 85);

	    updateSafeZone(vsafezone1, 0);
	    updateSafeZone(hsafezone1, 1);
	    updateSafeZone(vsafezone2, 2);
	    updateSafeZone(hsafezone2, 3);

	    updateHomeZone(homezone0, 0);
	    updateHomeZone(homezone1, 1);
	    updateHomeZone(homezone2, 2);
	    updateHomeZone(homezone3, 3);

	    // Labels
	    updatePlayerLabels();
	    updateCurrentPlayerLabel();

	    // Fire pit and cards
	    firepit.setText(game.getFirePit().isEmpty() ? "" : game.getFirePit().get(game.getFirePit().size() - 1).getName());

	    cardsbox.getChildren().clear();
	    cardsbuttons = new ArrayList<>();

	    // Make sure player turn is correct before accessing hand
	    Player current = game.getPlayers().get(0);
	    ArrayList<Card> hand = current.getHand();
	    if (hand == null || hand.isEmpty()) return;

	    for (Card c : hand) {
	        String label = (c instanceof Standard) ? c.getName() + " / " + getSuitSymbol(((Standard) c).getSuit()) : c.getName();
	        Button b = new Button(label);
	        b.setPrefSize(150, 50);
	        b.setStyle("-fx-background-color: orange; -fx-text-fill: black;");
	        b.setOnMouseEntered(e -> b.setStyle("-fx-background-color: black; -fx-text-fill: orange;"));
	        b.setOnMouseExited(e -> b.setStyle("-fx-background-color: orange; -fx-text-fill: black;"));
	        b.setOnAction(this);
	        cardsbuttons.add(b);
	        cardsbox.getChildren().add(b);
	    }

	    if (game.checkWin() != null) {
	        winscene(game.checkWin());
	    }
	}


	// Helper methods

	private void updateTrackSection(Pane pane, int from, int to) {
	    pane.getChildren().clear();
	    int step = from < to ? 1 : -1;

	    for (int i = from; i != to + step; i += step) {
	        Cell x = game.getBoard().getTrack().get(i);
	        Marble m = x.getMarble();
	        ImageView img = getMarbleImage(m);
	        img.setFitWidth(22);
	        img.setFitHeight(22);

	        Button b = new Button();
	        b.setGraphic(img);
	        b.setPadding(Insets.EMPTY);
	        b.setPrefSize(22, 22);
	        final int index = i;
	        b.setOnAction(e -> handleTrackButton(index));
	        pane.getChildren().add(b);
	    }
	}
	
	private void handleTrackButton(int i) {
	    Marble m = game.getBoard().getTrack().get(i).getMarble();
	    if (m != null && isMarbleTrapped(m)) {
	        errormsg("This marble is in a trap cell and cannot move.");
	        return;
	    }
	    try {
	        game.getPlayers().get(0).selectMarble(m);
	        if (!selectedMarbles.contains(m))
	            selectedMarbles.add(m);
	    } catch (InvalidMarbleException ex) {
	        errormsg(ex.getMessage());
	        return;
	    }
	    gameengine();
	}


	private void updateSafeZone(Pane pane, int index) {
	    pane.getChildren().clear();
	    for (int i = 0; i < 4; i++) {
	        Marble m = game.getBoard().getSafeZones().get(index).getCells().get(i).getMarble();
	        ImageView img = getMarbleImage(m);
	        img.setFitWidth(22);
	        img.setFitHeight(22);
	        pane.getChildren().add(img);
	    }
	}

	private void updateHomeZone(Pane pane, int playerIndex) {
	    pane.getChildren().clear();
	    for (Marble m : game.getPlayers().get(playerIndex).getMarbles()) {
	        ImageView img = getMarbleImage(m);
	        img.setFitWidth(22);
	        img.setFitHeight(22);
	        pane.getChildren().add(img);
	    }
	}

	private ImageView getMarbleImage(Marble m) {
	    if (m == null) return new ImageView("Empty.png");

	    switch (m.getColour()) {
	        case BLUE: return new ImageView("Blue.png");
	        case GREEN: return new ImageView("Green.png");
	        case RED: return new ImageView("Red.png");
	        case YELLOW: return new ImageView("Yellow.png");
	        default: return new ImageView("Empty.png");
	    }
	}

	private void updatePlayerLabels() {
	    realplayer.setText(game.getPlayers().get(0).getName() + " / Cards:" + game.getPlayers().get(0).getHand().size());
	    ai1.setText(game.getPlayers().get(1).getName() + " / Cards:" + game.getPlayers().get(1).getHand().size());
	    ai2.setText(game.getPlayers().get(2).getName() + " / Cards:" + game.getPlayers().get(2).getHand().size());
	    ai3.setText(game.getPlayers().get(3).getName() + " / Cards:" + game.getPlayers().get(3).getHand().size());
	}

	private void updateCurrentPlayerLabel() {
	    curplayer.setText("Current Player Turn: " + game.getActivePlayerColour());
	}

	private String getSuitSymbol(Suit s) {
	    switch (s) {
	        case SPADE: return "♠";
	        case DIAMOND: return "♦";
	        case HEART: return "♥";
	        case CLUB: return "♣";
	        default: return "";
	    }
	}


	
	private void winscene(Colour c) {
		root = new AnchorPane();
		endscene=new Scene(root,400,400);
		primarystage.setScene(endscene);
		primarystage.show();
		primarystage.setResizable(false);
		primarystage.setX(760);
		primarystage.setY(340);
		winner = new Label("Wohoooo "+ c +" won!!!");
		winner.setFont(new Font("Times New Roman", 30));
		createcomp(winner,0,0,400,400);
	}


	private ArrayList<Marble> selectedMarbles = new ArrayList<>();

	public void handle(ActionEvent e) {
	    Object src = e.getSource();

	    if (src == startbutton) {
	        if (namefield.getText().isEmpty()) {
	            errormsg("Please enter your name.");
	            return;
	        }
	        gamescene();
	        gameengine();
	        return;
	    }

	    if (src == clear) {
	        game.deselectAll();
	        selectedMarbles.clear();
	        gameengine();
	        return;
	    }

	    if (src == endturn) {
	        Player player = game.getPlayers().get(0);

	        if (player.getHand().isEmpty()) {
	            game.endPlayerTurn();
	        } else {
	            try {
	                int rand = (int) (Math.random() * player.getHand().size());
	                player.selectCard(player.getHand().get(rand));
	                game.discardCard(player.getColour());
	                game.endPlayerTurn();
	            } catch (Exception ex) {
	                errormsg("Cannot discard card: " + ex.getMessage());
	                return;
	            }
	        }

	        try {
	            playCpuTurns();
	        } catch (Exception ex) {
	            errormsg("CPU error: " + ex.getMessage());
	        }

	        gameengine();
	        return;
	    }


	    for (int i = 0; i < fulltrack.size(); i++) {
	        if (src == fulltrack.get(i)) {
	            Marble m = game.getBoard().getTrack().get(i).getMarble();
	            if (m != null && isMarbleTrapped(m)) {
	                errormsg("This marble is in a trap cell and cannot move.");
	                return;
	            }
	            try {
	                game.getPlayers().get(0).selectMarble(m);
	                if (!selectedMarbles.contains(m))
	                    selectedMarbles.add(m);
	            } catch (InvalidMarbleException ex) {
	                errormsg(ex.getMessage());
	                return;
	            }
	            gameengine();
	            return;
	        }
	    }

	    for (int i = 0; i < cardsbuttons.size(); i++) {
	        if (src == cardsbuttons.get(i)) {
	            Card c = game.getPlayers().get(0).getHand().get(i);
	            try {
	                game.getPlayers().get(0).selectCard(c);

	                if (c.getName().equals("Seven")) {
	                    try {
	                        int split = Integer.parseInt(splitdistance.getText());
	                        game.editSplitDistance(split);
	                    } catch (NumberFormatException | SplitOutOfRangeException ex) {
	                        errormsg("Please enter a valid number between 1 and 6 for split distance.");
	                        game.deselectAll();
	                        selectedMarbles.clear();
	                        return;
	                    }
	                }

	                if (c instanceof Jack) {
	                    Jack jack = (Jack) c;
	                    if (!jack.validateMarbleSize(selectedMarbles)) {
	                        errormsg("You must select one or two marbles for the Jack card.");
	                        game.deselectAll();
	                        selectedMarbles.clear();
	                        return;
	                    }
	                    if (!jack.validateMarbleColours(selectedMarbles)) {
	                        throw new IllegalSwapException("You must select one of your marbles and one from another player.");
	                    }
	                    jack.act(selectedMarbles);
	                    game.endPlayerTurn();
	                    playCpuTurns();
	                } else {
	                    if (game.canPlayTurn()) {
	                        game.playPlayerTurn();
	                        game.endPlayerTurn();
	                        playCpuTurns();
	                    } else {
	                        throw new InvalidCardException("You cannot play this card now.");
	                    }
	                }

	            } catch (GameException ex) {
	                errormsg(ex.getMessage());
	            } finally {
	                game.deselectAll();
	                selectedMarbles.clear();
	                gameengine();
	            }

	            return;
	        }
	    }

	    gameengine();
	}




	private void playCpuTurns() {
	    playCpuTurn(1);
	}

	private void playCpuTurn(int aiIndex) {
	    if (aiIndex > 3) {
	        gameengine(); // Final update after all CPUs are done
	        return;
	    }

	    PauseTransition pause = new PauseTransition(Duration.seconds(3)); // 1-second delay
	    pause.setOnFinished(event -> {
	        try {
	            if (game.canPlayTurn()) {
	                game.playPlayerTurn();
	            }
	        } catch (Exception ex) {
	            errormsg("CPU " + aiIndex + " failed to play: " + ex.getMessage());
	        } finally {
	            try {
	                game.endPlayerTurn();
	            } catch (Exception ex) {
	                errormsg("Failed to end CPU " + aiIndex + "'s turn: " + ex.getMessage());
	            }
	            gameengine(); // Update UI after this AI's move
	            playCpuTurn(aiIndex + 1); // Move to the next AI
	        }
	    });

	    pause.play();
	}







	
	public void createcomp(Region comp, int x, int y, int w, int h) {
		comp.setLayoutX(x);
		comp.setLayoutY(y);
		comp.setPrefWidth(w);
		comp.setPrefHeight(h);
		root.getChildren().add(comp);
	}
	private boolean isMarbleTrapped(Marble marble) {
	    for (Cell cell : game.getBoard().getTrack()) {
	        if (cell.getMarble() == marble && cell.isTrap()) {
	            return true;
	        }
	    }
	    return false;
	}

	public void errormsg(String msg) {
		Alert a = new Alert(AlertType.ERROR);
		a.setHeaderText("Oops, something is balabizoo!");
		a.setContentText(msg);
		a.show();
	}
	public static void main(String[] args) {
		launch(args);
		
	}
}
