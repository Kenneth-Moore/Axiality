

import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class Axiality extends Application {

    @Override
    public void start(Stage primaryStage) {
    	
    	final Button butt = new Button();
    	
    	final TitleScreen titleScene = new TitleScreen(butt);
        final GameScreen gamescreen = new GameScreen();
        
        butt.setOnAction(event -> primaryStage.setScene(gamescreen));
        
        primaryStage.setScene(titleScene);
        primaryStage.setTitle("Convex Symmetry Viewer");
        primaryStage.setHeight(800);
        primaryStage.setWidth(1440);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}