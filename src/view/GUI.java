package view;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;

import java.io.IOException;
import java.util.ArrayList;

import engine.Game;
import engine.board.Cell;
import exception.GameException;
import exception.InvalidCardException;
import exception.InvalidMarbleException;
import javafx.animation.PauseTransition;
import javafx.util.Duration;
import javafx.application.Application;
import javafx.application.Platform;
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
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.Colour;
import model.card.Card;
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
		htrack1left.getChildren().clear();
		for(int i=10;i>=0;i--) {
			Cell x = game.getBoard().getTrack().get(i);
			Marble m = x.getMarble();
			ImageView colourimage = null;
			if (m==null) {
				colourimage= new ImageView("Empty.png");
			}
			else if(m.getColour()==Colour.BLUE) {
				colourimage= new ImageView("Blue.png");
			}
			else if(m.getColour()==Colour.GREEN) {
				colourimage= new ImageView("Green.png");
			}
			else if(m.getColour()==Colour.RED) {
				colourimage= new ImageView("Red.png");
			}
			else if(m.getColour()==Colour.YELLOW) {
				colourimage= new ImageView("Yellow.png");
			}
			colourimage.setFitWidth(22);
			colourimage.setFitHeight(22);
			colourimage.setPreserveRatio(false);
			fulltrack.get(i).setGraphic(colourimage);
			fulltrack.get(i).setPadding(Insets.EMPTY);
			htrack1left.getChildren().add(fulltrack.get(i));
		}
		htrack1right.getChildren().clear();
		for(int i=99;i>=86;i--) {
			Cell x = game.getBoard().getTrack().get(i);
			Marble m = x.getMarble();
			ImageView colourimage = null;
			if (m==null) {
				colourimage= new ImageView("Empty.png");
			}
			else if(m.getColour()==Colour.BLUE) {
				colourimage= new ImageView("Blue.png");
			}
			else if(m.getColour()==Colour.GREEN) {
				colourimage= new ImageView("Green.png");
			}
			else if(m.getColour()==Colour.RED) {
				colourimage= new ImageView("Red.png");
			}
			else if(m.getColour()==Colour.YELLOW) {
				colourimage= new ImageView("Yellow.png");
			}
			colourimage.setPreserveRatio(false);
			fulltrack.get(i).setGraphic(colourimage);
			fulltrack.get(i).setPadding(Insets.EMPTY);
			htrack1right.getChildren().add(fulltrack.get(i));
		}
		vtrack1.getChildren().clear();
		for(int i=35;i>=11;i--) {
			Cell x = game.getBoard().getTrack().get(i);
			Marble m = x.getMarble();
			ImageView colourimage = null;
			if (m==null) {
				colourimage= new ImageView("Empty.png");
			}
			else if(m.getColour()==Colour.BLUE) {
				colourimage= new ImageView("Blue.png");
			}
			else if(m.getColour()==Colour.GREEN) {
				colourimage= new ImageView("Green.png");
			}
			else if(m.getColour()==Colour.RED) {
				colourimage= new ImageView("Red.png");
			}
			else if(m.getColour()==Colour.YELLOW) {
				colourimage= new ImageView("Yellow.png");
			}
			colourimage.setFitWidth(22);
			colourimage.setFitHeight(22);
			colourimage.setPreserveRatio(false);
			fulltrack.get(i).setGraphic(colourimage);
			fulltrack.get(i).setPadding(Insets.EMPTY);
			vtrack1.getChildren().add(fulltrack.get(i));
		}
		htrack2.getChildren().clear();
		for(int i=36;i<=60;i++) {
			Cell x = game.getBoard().getTrack().get(i);
			Marble m = x.getMarble();
			ImageView colourimage = null;
			if (m==null) {
				colourimage= new ImageView("Empty.png");
			}
			else if(m.getColour()==Colour.BLUE) {
				colourimage= new ImageView("Blue.png");
			}
			else if(m.getColour()==Colour.GREEN) {
				colourimage= new ImageView("Green.png");
			}
			else if(m.getColour()==Colour.RED) {
				colourimage= new ImageView("Red.png");
			}
			else if(m.getColour()==Colour.YELLOW) {
				colourimage= new ImageView("Yellow.png");
			}
			colourimage.setFitWidth(22);
			colourimage.setFitHeight(22);
			colourimage.setPreserveRatio(false);
			fulltrack.get(i).setGraphic(colourimage);
			fulltrack.get(i).setPadding(Insets.EMPTY);
			htrack2.getChildren().add(fulltrack.get(i));
		}
		vtrack2.getChildren().clear();
		for(int i=61;i<=85;i++) {
			Cell x = game.getBoard().getTrack().get(i);
			Marble m = x.getMarble();
			ImageView colourimage = null;
			if (m==null) {
				colourimage= new ImageView("Empty.png");
			}
			else if(m.getColour()==Colour.BLUE) {
				colourimage= new ImageView("Blue.png");
			}
			else if(m.getColour()==Colour.GREEN) {
				colourimage= new ImageView("Green.png");
			}
			else if(m.getColour()==Colour.RED) {
				colourimage= new ImageView("Red.png");
			}
			else if(m.getColour()==Colour.YELLOW) {
				colourimage= new ImageView("Yellow.png");
			}
			colourimage.setFitWidth(22);
			colourimage.setFitHeight(22);
			colourimage.setPreserveRatio(false);
			fulltrack.get(i).setGraphic(colourimage);
			fulltrack.get(i).setPadding(Insets.EMPTY);
			vtrack2.getChildren().add(fulltrack.get(i));
		}
		vsafezone1.getChildren().clear();
		for(int i=0;i<4;i++) {
			Marble m =game.getBoard().getSafeZones().get(0).getCells().get(i).getMarble();
			ImageView colourimage = null;
			if (m==null) {
				colourimage= new ImageView("Empty.png");
			}
			else if(m.getColour()==Colour.BLUE) {
				colourimage= new ImageView("Blue.png");
			}
			else if(m.getColour()==Colour.GREEN) {
				colourimage= new ImageView("Green.png");
			}
			else if(m.getColour()==Colour.RED) {
				colourimage= new ImageView("Red.png");
			}
			else if(m.getColour()==Colour.YELLOW) {
				colourimage= new ImageView("Yellow.png");
			}
			colourimage.setFitWidth(22);
			colourimage.setFitHeight(22);
			vsafezone1.getChildren().add(colourimage);
		}
		hsafezone1.getChildren().clear();
		for(int i=0;i<4;i++) {
			Marble m =game.getBoard().getSafeZones().get(1).getCells().get(i).getMarble();
			ImageView colourimage = null;
			if (m==null) {
				colourimage= new ImageView("Empty.png");
			}
			else if(m.getColour()==Colour.BLUE) {
				colourimage= new ImageView("Blue.png");
			}
			else if(m.getColour()==Colour.GREEN) {
				colourimage= new ImageView("Green.png");
			}
			else if(m.getColour()==Colour.RED) {
				colourimage= new ImageView("Red.png");
			}
			else if(m.getColour()==Colour.YELLOW) {
				colourimage= new ImageView("Yellow.png");
			}
			colourimage.setFitWidth(22);
			colourimage.setFitHeight(22);
			hsafezone1.getChildren().add(colourimage);
		}
		vsafezone2.getChildren().clear();
		for(int i=0;i<4;i++) {
			Marble m =game.getBoard().getSafeZones().get(2).getCells().get(i).getMarble();
			ImageView colourimage = null;
			if (m==null) {
				colourimage= new ImageView("Empty.png");
			}
			else if(m.getColour()==Colour.BLUE) {
				colourimage= new ImageView("Blue.png");
			}
			else if(m.getColour()==Colour.GREEN) {
				colourimage= new ImageView("Green.png");
			}
			else if(m.getColour()==Colour.RED) {
				colourimage= new ImageView("Red.png");
			}
			else if(m.getColour()==Colour.YELLOW) {
				colourimage= new ImageView("Yellow.png");
			}
			colourimage.setFitWidth(22);
			colourimage.setFitHeight(22);
			vsafezone2.getChildren().add(colourimage);
		}
		hsafezone2.getChildren().clear();
		for(int i=0;i<4;i++) {
			Marble m =game.getBoard().getSafeZones().get(3).getCells().get(i).getMarble();
			ImageView colourimage = null;
			if (m==null) {
				colourimage= new ImageView("Empty.png");
			}
			else if(m.getColour()==Colour.BLUE) {
				colourimage= new ImageView("Blue.png");
			}
			else if(m.getColour()==Colour.GREEN) {
				colourimage= new ImageView("Green.png");
			}
			else if(m.getColour()==Colour.RED) {
				colourimage= new ImageView("Red.png");
			}
			else if(m.getColour()==Colour.YELLOW) {
				colourimage= new ImageView("Yellow.png");
			}
			colourimage.setFitWidth(22);
			colourimage.setFitHeight(22);
			hsafezone2.getChildren().add(colourimage);
		}
		homezone0.getChildren().clear();
		for (int i = 0; i <game.getPlayers().get(0).getMarbles().size(); i++) {
			Marble m = game.getPlayers().get(0).getMarbles().get(i);
			ImageView colourimage = null;
			if (m==null) {
				colourimage= new ImageView("Empty.png");
			}
			else if(m.getColour()==Colour.BLUE) {
				colourimage= new ImageView("Blue.png");
			}
			else if(m.getColour()==Colour.GREEN) {
				colourimage= new ImageView("Green.png");
			}
			else if(m.getColour()==Colour.RED) {
				colourimage= new ImageView("Red.png");
			}
			else if(m.getColour()==Colour.YELLOW) {
				colourimage= new ImageView("Yellow.png");
			}
			colourimage.setFitWidth(22);
			colourimage.setFitHeight(22);
			homezone0.getChildren().add(colourimage);
		}
		homezone1.getChildren().clear();
		for (int i = 0; i <game.getPlayers().get(1).getMarbles().size(); i++) {
			Marble m = game.getPlayers().get(1).getMarbles().get(i);
			ImageView colourimage = null;
			if (m==null) {
				colourimage= new ImageView("Empty.png");
			}
			else if(m.getColour()==Colour.BLUE) {
				colourimage= new ImageView("Blue.png");
			}
			else if(m.getColour()==Colour.GREEN) {
				colourimage= new ImageView("Green.png");
			}
			else if(m.getColour()==Colour.RED) {
				colourimage= new ImageView("Red.png");
			}
			else if(m.getColour()==Colour.YELLOW) {
				colourimage= new ImageView("Yellow.png");
			}
			colourimage.setFitWidth(22);
			colourimage.setFitHeight(22);
			homezone1.getChildren().add(colourimage);
		}
		homezone2.getChildren().clear();
		for (int i = 0; i <game.getPlayers().get(2).getMarbles().size(); i++) {
			Marble m = game.getPlayers().get(2).getMarbles().get(i);
			ImageView colourimage = null;
			if (m==null) {
				colourimage= new ImageView("Empty.png");
			}
			else if(m.getColour()==Colour.BLUE) {
				colourimage= new ImageView("Blue.png");
			}
			else if(m.getColour()==Colour.GREEN) {
				colourimage= new ImageView("Green.png");
			}
			else if(m.getColour()==Colour.RED) {
				colourimage= new ImageView("Red.png");
			}
			else if(m.getColour()==Colour.YELLOW) {
				colourimage= new ImageView("Yellow.png");
			}
			colourimage.setFitWidth(22);
			colourimage.setFitHeight(22);
			homezone2.getChildren().add(colourimage);
		}
		homezone3.getChildren().clear();
		for (int i = 0; i <game.getPlayers().get(3).getMarbles().size(); i++) {
			Marble m = game.getPlayers().get(3).getMarbles().get(i);
			ImageView colourimage = null;
			if (m==null) {
				colourimage= new ImageView("Empty.png");
			}
			else if(m.getColour()==Colour.BLUE) {
				colourimage= new ImageView("Blue.png");
			}
			else if(m.getColour()==Colour.GREEN) {
				colourimage= new ImageView("Green.png");
			}
			else if(m.getColour()==Colour.RED) {
				colourimage= new ImageView("Red.png");
			}
			else if(m.getColour()==Colour.YELLOW) {
				colourimage= new ImageView("Yellow.png");
			}
			colourimage.setFitWidth(22);
			colourimage.setFitHeight(22);
			homezone3.getChildren().add(colourimage);
		}
		String r=game.getPlayers().get(0).getName()+" / Cards:"+game.getPlayers().get(0).getHand().size();
		realplayer.setText(r);
		String a1=game.getPlayers().get(1).getName()+" / Cards:"+game.getPlayers().get(1).getHand().size();
		ai1.setText(a1);
		String a2=game.getPlayers().get(2).getName()+" / Cards:"+game.getPlayers().get(2).getHand().size();
		ai2.setText(a2);
		String a3=game.getPlayers().get(3).getName()+" / Cards:"+game.getPlayers().get(3).getHand().size();
		ai3.setText(a3);
		String curptext ="Current Player Turn: "+ game.getActivePlayerColour();
		curplayer.setText(curptext);
		cardsbox.getChildren().clear();
		cardsbuttons = new ArrayList<Button>();
		for (int i = 0; i < game.getPlayers().get(0).getHand().size(); i++) {
			Card c = game.getPlayers().get(0).getHand().get(i);
			String card = "";
			if (c instanceof Standard) {
					Standard x = (Standard) c;
					Suit s = x.getSuit();
					if (s==Suit.SPADE) {
						card = x.getName() + " / ♠"  ;
					}
					if (s==Suit.DIAMOND) {
						card = x.getName() + " / ♦"  ;
					}if (s==Suit.HEART) {
						card = x.getName() + " / ♥"  ;
					}
					if (s==Suit.CLUB) {
						card = x.getName() + " / ♣"  ;
					}
			}else {
					card = c.getName();
			}
			Button b = new Button(card);
			b.setPrefSize(150, 50);
			b.setStyle("-fx-background-color: orange; -fx-text-fill: black;");
			b.setOnMouseEntered(e ->b.setStyle("-fx-background-color: black; -fx-text-fill: orange;"));
			b.setOnMouseExited(e ->b.setStyle("-fx-background-color: orange; -fx-text-fill: black;"));
			b.setOnAction(this);
			cardsbuttons.add(b);
			cardsbox.getChildren().add(b);
			if(game.getFirePit().size()>0) {
				int j = game.getFirePit().size()-1;
				firepit.setText(game.getFirePit().get(j).getName());
			}
		}
		if (game.checkWin()!=null) {
			winscene(game.checkWin());
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

	public void handle(ActionEvent e) {
		if(e.getSource()==startbutton) {
			if(namefield.getText().equals("")) {
				errormsg("Please enter your name.");
			}
			else {
				gamescene();
				gameengine();
			}
		}

		for(int i=0;i<fulltrack.size();i++) {
			if (e.getSource()==fulltrack.get(i)) {
				Player player = game.getPlayers().get(0);
				try {
					player.selectMarble(game.getBoard().getTrack().get(i).getMarble());
				}catch(InvalidMarbleException x) {
					errormsg(x.getMessage());
				}
			}
		}
		if(e.getSource()==clear) {
			game.deselectAll();
		}
		if (e.getSource() == endturn) {
		    Player x = game.getPlayers().get(0);

		    if (x.getSelectedCard() == null) {
		        int r = (int) (Math.random() * x.getHand().size());
		        try {
		            x.selectCard(x.getHand().get(r));
		        } catch (InvalidCardException e1) {
		            errormsg(e1.getMessage());
		            return;
		        }
		    }

		    game.endPlayerTurn();
		    endturn.setDisable(true); 

		    PauseTransition pause = new PauseTransition(Duration.seconds(1));
		    pause.setOnFinished(event -> {
		        gameengine();
		        runCpuTurn(1);
		    });
		    pause.play();

		}

		for (int i = 0; i < cardsbuttons.size(); i++) {
			if (e.getSource() == cardsbuttons.get(i)) {
				Card c = game.getPlayers().get(0).getHand().get(i);
				try {
					game.getPlayers().get(0).selectCard(c);

					if (game.canPlayTurn()) {
						game.playPlayerTurn();
						game.endPlayerTurn();
						gameengine();

						PauseTransition pause = new PauseTransition(Duration.seconds(1));
						pause.setOnFinished(ev -> runCpuTurn(1));
						pause.play();
					} else {
						errormsg("You cannot play this card now.");
						game.deselectAll();
						gameengine();
					}
				} catch (Exception ex) {
					errormsg(ex.getMessage());
					game.deselectAll();
					gameengine();
				}
				break;
			}
		}

		gameengine();
	}
	private void runCpuTurn(int index) {
	    if (index > 3) {
	        gameengine();
	        Platform.runLater(() -> endturn.setDisable(false)); // Always re-enable
	        return;
	    }

	    PauseTransition pause = new PauseTransition(Duration.seconds(1));
	    pause.setOnFinished(event -> {
	        try {
	            if (game.canPlayTurn()) {
	                game.playPlayerTurn();
	            }
	        } catch (GameException g) {
	            errormsg(g.getMessage());
	        }

	        game.endPlayerTurn();
	        gameengine();
	        runCpuTurn(index + 1); // Keep progressing CPUs
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

