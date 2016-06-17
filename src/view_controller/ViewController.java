package view_controller;
 
import java.util.Observable;
import java.util.Observer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import static javafx.scene.input.MouseButton.PRIMARY;
import static javafx.scene.input.MouseButton.SECONDARY;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.Game;
import model.Level;
import model.Score;
import model.Tile;

/**
 *
 * @author LAFAYE DE MICHEAUX Yoan - LHOPITAL Sacha
 */
public class ViewController extends Application {
  
    static int IMAGE_SIZE = 30;

    static Image MINE_RESSOURCE;
    static Image FLAG_RESSOURCE;
    static Image EMPTY_RESSOURCE;
    static Image[] VALUES_RESSOURCE;
    
    static{
        MINE_RESSOURCE = new Image("./ressources/mine.jpg");
        FLAG_RESSOURCE = new Image("./ressources/flag.jpg");
        EMPTY_RESSOURCE = new Image("./ressources/empty.jpg");
        
        VALUES_RESSOURCE = new Image[10];
        VALUES_RESSOURCE[0] = new Image("./ressources/zero.jpg");
        VALUES_RESSOURCE[1] = new Image("./ressources/one.jpg");
        VALUES_RESSOURCE[2] = new Image("./ressources/two.jpg");
        VALUES_RESSOURCE[3] = new Image("./ressources/three.jpg");
        VALUES_RESSOURCE[4] = new Image("./ressources/four.jpg");
        VALUES_RESSOURCE[5] = new Image("./ressources/five.jpg");
        VALUES_RESSOURCE[6] = new Image("./ressources/six.jpg");
        VALUES_RESSOURCE[7] = new Image("./ressources/seven.jpg");
        VALUES_RESSOURCE[8] = new Image("./ressources/eight.jpg");
        VALUES_RESSOURCE[9] = new Image("./ressources/nine.jpg");
    }
    
    Game game;
    GridPane grid;
    Text minesLeftText;
    Text endGameText;
    Text scoreText;
    Text bestScoreText;
    Text timer;
    HBox hBox_outter;
    BorderPane root;
    int currentGridSize;
    int currentMines;
    
    @Override
    public void start(Stage primaryStage) {
        
        root = new BorderPane();
        Scene scene = new Scene(root, 850, 950);
        scene.setFill(Color.OLDLACE);
       
        game = new Game(Level.MEDIUM);
        
        game.addObserver(new Observer() {
            
            @Override
            public void update(Observable o, Object arg) {
                
                if(game.isEndOfGame()) {
                    
                    scoreText.setText("Score: " + Score.getScore(game));
                    
                    if(game.playerWin()) {
                        endGameText.setText("BRAVO ! Vous avez gagné !");
                    } else {
                        endGameText.setText("Oops !");
                    }
                }
        
                for(Node tile : grid.getChildren()) {

                    //If the tile is not an ImageView
                    //happen somethimes with a gridpane
                    if(! (tile instanceof ImageView) ) {
                        continue;
                    }

                    int column = GridPane.getColumnIndex(tile);
                    int row = GridPane.getRowIndex(tile);
                    Tile currentTile = game.getBaseGrid().getTile(column, row);            
                    ImageView curentImageView = (ImageView)tile;

                    switch (currentTile.getState()) {

                        case Tile.STATE_HIDDEN : 
                            curentImageView.setImage(EMPTY_RESSOURCE);
                            break;

                        case Tile.STATE_FLAG :   
                            curentImageView.setImage(FLAG_RESSOURCE);
                            break;

                        case Tile.STATE_SHOWN :  
                            int tileValue = currentTile.getValue();
                            if(tileValue == -1) {
                                curentImageView.setImage(MINE_RESSOURCE);
                                break;
                            }
                            curentImageView.setImage(VALUES_RESSOURCE[tileValue]);
                            break;

                        default :
                    }
                }
                minesLeftText.setText(game.getMinesLeft() + "");
            }
        });
        
        game.timer.addObserver(new Observer(){
            @Override
            public void update(Observable o, Object arg) {
                timer.setText("Time: "+ game.timer.getTime());
            }
        });
        
        /* Menu */
        MenuBar menuBar = new MenuBar();
        Menu menuPartie = new Menu("Partie");
        Menu newGame = new Menu("Nouvelle Partie");
        MenuItem exitApp = new MenuItem("Quitter");
        MenuItem easy = new MenuItem("Facile (5x5)");
        MenuItem medium = new MenuItem("Intermédiaire (15x15)");
        MenuItem hard = new MenuItem("Difficile (25x25)");
        
        newGame.getItems().addAll(easy, medium, hard);
        menuPartie.getItems().addAll(newGame, exitApp);
        menuBar.getMenus().addAll(menuPartie);
        
        easy.setOnAction(new EventHandler<ActionEvent>() { 
            public void handle(ActionEvent t) {
                restartGame(Level.EASY);
            }
        });
        
        medium.setOnAction(new EventHandler<ActionEvent>() { 
            public void handle(ActionEvent t) {
                restartGame(Level.MEDIUM);
            }
        });
        
        hard.setOnAction(new EventHandler<ActionEvent>() { 
            public void handle(ActionEvent t) {
                restartGame(Level.HARD);
            }
        });
        
        exitApp.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                scene.getWindow().hide();
            }
        });
        
        
        /* Others text information */
        scoreText = new Text();
        scoreText.setFont(Font.font ("Verdana", 20));
        scoreText.setFill(Color.BLACK);
        
        bestScoreText = new Text("Best Score: "+ Score.getBestScore(game.level)+ " in " + Score.getBestTime(game.level) +" seconds.");
        bestScoreText.setFont(Font.font ("Verdana", 20));
        bestScoreText.setFill(Color.BLACK);
        
        endGameText = new Text();
        endGameText.setFont(Font.font ("Verdana", 20));
        endGameText.setFill(Color.RED);
        
        minesLeftText = new Text("");
        minesLeftText.setFont(Font.font ("Verdana", 20));
        minesLeftText.setFill(Color.RED);
        
        ImageView minesLeft = new ImageView();
        minesLeft.setImage(MINE_RESSOURCE);
        minesLeft.setFitHeight(IMAGE_SIZE);
        minesLeft.setFitWidth(IMAGE_SIZE);
        
        AnchorPane infoTop = new AnchorPane();
        AnchorPane.setLeftAnchor(minesLeft, new Double(80));
        AnchorPane.setTopAnchor(minesLeft, new Double(25));
        AnchorPane.setLeftAnchor(minesLeftText, new Double(85+ IMAGE_SIZE));
        AnchorPane.setTopAnchor(minesLeftText, new Double(25));
        AnchorPane.setRightAnchor(endGameText, new Double(80));
        AnchorPane.setTopAnchor(endGameText, new Double(25));
        AnchorPane.setLeftAnchor(scoreText, new Double(250));
        AnchorPane.setTopAnchor(scoreText, new Double(25));
        AnchorPane.setLeftAnchor(bestScoreText, new Double(250));
        AnchorPane.setTopAnchor(bestScoreText, new Double(50));
        
        infoTop.getChildren().addAll(minesLeft, minesLeftText, endGameText, scoreText, bestScoreText);
        
        /* Timer */
        timer = new Text("Time: 0");
        timer.setFont(Font.font ("Verdana", 20));
        timer.setFill(Color.BLACK);
        
        AnchorPane infoBot = new AnchorPane();
        AnchorPane.setLeftAnchor(timer, new Double(320));
        AnchorPane.setBottomAnchor(timer, new Double(25));
        
        infoBot.getChildren().add(timer);
   
        /* Grid */
        initScene();
        
        /* Bottom */
        VBox topVbox = new VBox();
        topVbox.getChildren().add(menuBar);
        topVbox.getChildren().add(infoTop);
        root.setTop(topVbox);
        root.setBottom(infoBot);
        
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
          public void handle(WindowEvent we) {
              Score.save();
              System.exit(0);
          }
        });     
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Démineur");
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    private void restartGame(Level level) {
        game.restart(level);
        endGameText.setText("");
        scoreText.setText("");
        bestScoreText.setText("Best Score: " + Score.getBestScore(game.level) + " in " + Score.getBestTime(game.level)+ " seconds.");
        timer.setText("Time: 0");
        initScene();
    }
    
    /***
     * Create the grid with the correct difficulty
     */
    private void initScene() {
        
        /* Grid */
        grid = new GridPane();
        grid.setGridLinesVisible(true);
        grid.setAlignment(Pos.CENTER);
        
        for(int column = 0; column < game.level.width; column++)  {
            for(int row = 0; row < game.level.height; row++) {
                
                ImageView tile = new ImageView();
                tile.setImage(EMPTY_RESSOURCE);
                grid.add(tile, column, row);
                
                tile.setFitHeight(IMAGE_SIZE);
                tile.setFitWidth(IMAGE_SIZE);
                
                GridPane.setValignment(tile, VPos.CENTER);
                GridPane.setHalignment(tile, HPos.CENTER);
                
                grid.setHgap(1);
                grid.setVgap(1);
                
                tile.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if(event.getButton().equals(SECONDARY)) {
                            game.rigthClick(
                                    GridPane.getColumnIndex((Node) event.getSource()), 
                                    GridPane.getRowIndex((Node) event.getSource()));
                        }
                        if(event.getButton().equals(PRIMARY)) {
                            game.leftClick(
                                    GridPane.getColumnIndex((Node) event.getSource()), 
                                    GridPane.getRowIndex((Node) event.getSource()));
                        }
                    }
                });
            }
        }
        minesLeftText.setText(game.getMinesLeft() + "");
        root.setCenter(grid);
    }
}
