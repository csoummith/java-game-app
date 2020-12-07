package com.internshala.connect4;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;


import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Controller implements Initializable {
    private static final int columns =7;
    private static final int rows=6;
    private static final int circle_dia=80;
    private static final String disccolor1="#ff5050";
    private static final String disccolor2="#ffbb33";

    private static String player_one="Player One";
    private static String player_two="Player Two";

    private boolean isplayerone=true;

    private Disc[][] insertedDiscArray =new Disc[rows][columns];

    @FXML
    public  Pane filepane;

    @FXML
    public TextField playerone;

    @FXML
    public VBox vbox;

    @FXML
    public TextField playertwo;

    @FXML
    public Button setnames;

    @FXML
    public GridPane rootGridPane;

    @FXML
    public Pane pane1;

    @FXML
    public Label player1;

    @FXML
    public Label turn1;

    private boolean isAllowedToInsert=true;

    public void createPlayground() {
        Shape rectangleWithHoles=createGameStructuralGrid();
        rootGridPane.add(rectangleWithHoles,0,1);
        List<Rectangle> rectangleList=createClickableColoumns();
        for (Rectangle rectangle:rectangleList) {
            rootGridPane.add(rectangle,0,1);
        }

        setnames.setOnAction(event -> setname());



    }

    private void setname() {

         player_one=playerone.getText();
         player_two=playertwo.getText();
         player1.setText(player_one);

    }

    private Shape createGameStructuralGrid(){
        Shape rectangleWithHoles=new Rectangle((columns+1)*circle_dia,(rows+1)*circle_dia);
        for(int i=0;i<rows;i++){
            for(int j=0;j<columns;j++){
                Circle circle=new Circle();
                circle.setRadius(circle_dia/2);
                circle.setCenterX(circle_dia/2);
                circle.setCenterY(circle_dia/2);

                circle.setSmooth(true);

                circle.setTranslateX(j*(circle_dia+5)+circle_dia/4);
                circle.setTranslateY(i*(circle_dia+5)+circle_dia/4);

                rectangleWithHoles=Shape.subtract(rectangleWithHoles,circle);
            }

        }
        rectangleWithHoles.setFill(Color.WHITE);
        return rectangleWithHoles;
    }
    private List<Rectangle> createClickableColoumns(){
        List<Rectangle> rectangleList=new ArrayList<>();
        for(int col=0;col<columns;col++) {
            Rectangle rectangle = new Rectangle(circle_dia, (rows + 1) * circle_dia);
            rectangle.setFill(Color.TRANSPARENT);
            rectangle.setTranslateX(col*(circle_dia+5)+circle_dia / 4);
            rectangle.setOnMouseEntered(event -> rectangle.setFill(Color.valueOf("#eeeeee26")));
            rectangle.setOnMouseExited(event -> rectangle.setFill(Color.TRANSPARENT));
            int finalCol = col;
            rectangle.setOnMouseClicked(event -> {
                if(isAllowedToInsert) {

                    isAllowedToInsert=false;
                    final int column = finalCol;
                    insertDisc(new Disc(isplayerone), column);
                }
            });
            rectangleList.add(rectangle);
        }
        return rectangleList;
    }
    private void insertDisc(Disc disc,int column){
        int row=rows-1;
        while(row>=0){
            if(getdiscifpresent(row,column) == null)
                break;
            row--;
        }
        if(row<0)
            return;

        insertedDiscArray[row][column]=disc;
        pane1.getChildren().add(disc);

        disc.setTranslateX(column*(circle_dia+5)+circle_dia/4);
        int  currentrow=row;
        TranslateTransition translateTransition=new TranslateTransition(Duration.seconds(0.5),disc);
        translateTransition.setToY(row*(circle_dia+5)+circle_dia/4);

        translateTransition.setOnFinished(event -> {

            isAllowedToInsert=true;
            if(gameENded(currentrow,column)){
               gameOver();
               return;

            }
            isplayerone=!isplayerone;
            player1.setText(isplayerone? player_one : player_two);
        });
        translateTransition.play();
    }
    private boolean gameENded(int row,int colomn){
        List<Point2D> vertialPoints=IntStream.rangeClosed(row-3,row+3).mapToObj(r ->new Point2D(r,colomn)).collect(Collectors.toList());
        List<Point2D> horizontalPoints=IntStream.rangeClosed(colomn-3,colomn+3).mapToObj(c ->new Point2D(row,c)).collect(Collectors.toList());
        Point2D startpoint1=new Point2D(row-3,colomn+3);
        List<Point2D> diagnal1Points=IntStream.rangeClosed(0,6).mapToObj(i ->startpoint1.add(i,-i)).collect(Collectors.toList());
        Point2D startpoint2=new Point2D(row-3,colomn-3);
        List<Point2D> diagnal2Points=IntStream.rangeClosed(0,6).mapToObj(i ->startpoint2.add(i,i)).collect(Collectors.toList());
        boolean isEnded=checkcombinations(vertialPoints) || checkcombinations(horizontalPoints) || checkcombinations(diagnal1Points) || checkcombinations(diagnal2Points);
        return isEnded;


        
    }

    private boolean checkcombinations(List<Point2D> Points) {
        int chain=0;
        for (Point2D point: Points) {

            int rowuindexforArray=(int) point.getX();
            int colomnindexforArray= (int) point.getY();
            Disc disc=getdiscifpresent(rowuindexforArray,colomnindexforArray);
            if(disc != null && disc.isplayeronemove==isplayerone){
                chain++;
                if(chain==4){
                    return true;
                }
            }else{
                chain=0;
            }

        }
        return false;
    }
    private Disc getdiscifpresent(int row,int column){
        if(row>=rows || row<0 || column>=columns || column<0)
            return null;
        return insertedDiscArray[row][column];

    }

    private void gameOver(){
        String winner=isplayerone?player_one:player_two;
        System.out.println("Winner is: "+winner);
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Connect Four");
        alert.setHeaderText("The Winner is "+winner);
        alert.setContentText("Do You Want To Play Again?");
        ButtonType yes=new ButtonType("yes");
        ButtonType no=new ButtonType("No,Exit");
        alert.getButtonTypes().setAll(yes,no);
        Platform.runLater(()->{
            Optional<ButtonType> btnclicked=alert.showAndWait();
            if(btnclicked.isPresent() && btnclicked.get()==yes){
                resetgame();

            }else{
                Platform.exit();
                System.exit(0);
            }
        });

    }

    public void resetgame() {
        pane1.getChildren().clear();

        for (int row = 0; row<insertedDiscArray.length ;row++) {
            for(int col=0;col<insertedDiscArray[row].length;col++){
                insertedDiscArray[row][col]=null;
            }
        }

        isplayerone=true;
        player_one="Player One";
        player_two="Player Two";
        player1.setText(player_one);
        playerone.clear();
        playertwo.clear();

        createPlayground();
    }

    private static class Disc extends Circle{
         private final boolean isplayeronemove;
         public Disc(boolean isplayeronemove){
             this.isplayeronemove=isplayeronemove;
             setRadius(circle_dia/2);
             setFill(isplayeronemove? Color.valueOf(disccolor1):Color.valueOf(disccolor2));
             setCenterX(circle_dia/2);
             setCenterY(circle_dia/2);
         }
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }


}
