package com.internshala.connect4;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {

    private Controller controller;

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader= new FXMLLoader(getClass().getResource("game.fxml"));
        GridPane rootGridPane=loader.load();


        controller = loader.getController();
        controller.createPlayground();



        MenuBar menuBar=createMenu();
        menuBar.prefWidthProperty().bind(primaryStage.widthProperty());
        Pane menupane = (Pane) rootGridPane.getChildren().get(0);
        menupane.getChildren().addAll(menuBar);

        Scene scene=new Scene(rootGridPane);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Connect Four");
        primaryStage.setResizable(false);
        primaryStage.show();
    }
    private MenuBar createMenu(){

        //Help Menu.......

        Menu helpmenu=new Menu("Help");
        MenuItem aboutgame=new MenuItem("About Connect4");
        aboutgame.setOnAction(event -> {
            aboutconnect4();
        });
        SeparatorMenuItem separatorMenu=new SeparatorMenuItem();
        MenuItem aboutme=new MenuItem("About Me");
        aboutme.setOnAction(event -> aboutme());
        helpmenu.getItems().addAll(aboutgame,separatorMenu,aboutme);

        //File Menu.........

        Menu filemenu=new Menu("File");
        MenuItem newgame=new MenuItem("New Game ");
        newgame.setOnAction(event ->controller.resetgame());
        MenuItem resetgame=new MenuItem("Reset Game ");
        resetgame.setOnAction(event -> controller.resetgame());
        SeparatorMenuItem separatorMenuItem=new SeparatorMenuItem();
        MenuItem exitgame=new MenuItem("Exit  Game ");
        exitgame.setOnAction(event -> exitgame());
        filemenu.getItems().addAll(newgame,resetgame,separatorMenuItem, exitgame);

        MenuBar menuBar=new MenuBar();
        menuBar.getMenus().addAll(filemenu,helpmenu);
        return menuBar;
    }

    private void aboutme() {
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About The Developer");
        alert.setHeaderText("C SOUMMITH");
        alert.setContentText("Video game development is the process of developing a video game. The effort is undertaken by a developer, ranging from a single person to an international team dispersed across the globe. Development of traditional commercial PC and console games is normally funded by a publisher, and can take several years to reach completion");
        alert.show();
    }

    private void aboutconnect4() {
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About Connect Four Game");
        alert.setHeaderText("How to Play?");
        alert.setContentText("Connect Four is a two-player connection game in which the players first choose a color and then take turns dropping colored discs from the top into a seven-column, six-row vertically suspended grid. The pieces fall straight down, occupying the next available space within the column. The objective of the game is to be the first to form a horizontal, vertical, or diagonal line of four of one's own discs. Connect Four is a solved game. The first player can always win by playing the right moves.");
        alert.show();
    }

    private void exitgame() {
        Platform.exit();
        System.exit(0);
    }




    public static void main(String[] args) {
        launch(args);
    }
}
