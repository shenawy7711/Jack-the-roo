package view;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;

import javafx.application.Application;
import javafx.event.ActionEvent;
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
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class GUI extends Application{

	private AnchorPane root;
	private Scene startscene;
	private Label playernamelabel;
	private TextField namefield;
	private Button startbutton;
	private HBox imagebox;
	private ImageView image;
	private ImageView bg;

	@Override
	public void start(Stage primarystage) throws Exception {
		root = new AnchorPane();
		startscene = new Scene(root,800,600);
		primarystage.setScene(startscene);
		primarystage.show();
		primarystage.setResizable(false);
		playernamelabel = new Label("Enter player name");
		playernamelabel.setFont(new Font("Times new roman",24));
		createcomp(playernamelabel, 180,350, 200, 50);
		namefield = new TextField();
		createcomp(namefield,400,360,200,40);
		namefield.setPromptText("please enter a name");
		namefield.getParent().requestFocus();
		startbutton = new Button("Start Game");
		createcomp(startbutton,300,450,200,50);
		startbutton.setOnAction(this::handle);
		imagebox = new HBox();
		ImageView image = new ImageView("Logo.png");
		imagebox.getChildren().add(image);
		createcomp(imagebox, 195, 20, 410, 275);
		root.setBackground(new Background(new BackgroundFill(Color.ORANGE, CornerRadii.EMPTY, Insets.EMPTY)));
		startbutton.setStyle("-fx-background-color: #604110; -fx-text-fill: white;");
        startbutton.setOnMouseEntered(e ->startbutton.setStyle("-fx-background-color: black; -fx-text-fill: white;"));
        startbutton.setOnMouseExited(e ->startbutton.setStyle("-fx-background-color: #604110; -fx-text-fill: white;"));
	}
	public void handle(ActionEvent e) {
		if(e.getSource()==startbutton) {
			if(namefield.getText().equals("")) {
				errormsg("Please enter your name.");
			}
			else {
				gamescene();
			}
		}
	}	 
	public void gamescene() {
		
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

