package view;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class GUI extends Application{

	private AnchorPane root;
	private Scene startscene;
	private Label playernamelabel;
	private TextField namefield;

	@Override
	public void start(Stage primarystage) throws Exception {
		root = new AnchorPane();
		startscene = new Scene(root,800,600);
		primarystage.setScene(startscene);
		primarystage.show();
		primarystage.setResizable(false);
		playernamelabel = new Label("Enter player name");
		playernamelabel.setFont(new Font("Times new roman",24));
		createcomp(playernamelabel, 100,300, 200, 50);
		namefield = new TextField();
		createcomp(namefield,400,300,300,50);
		namefield.setPromptText("please enter a name");
		namefield.getParent().requestFocus();
	}
	public void createcomp(Region comp, int x, int y, int w, int h) {
		comp.setLayoutX(x);
		comp.setLayoutY(y);
		comp.setPrefWidth(w);
		comp.setPrefHeight(h);
		root.getChildren().add(comp);
	}
	public static void main(String[] args) {
		launch(args);
	}
}

