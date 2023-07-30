

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
        primaryStage.setTitle("ConvexSym Viewer");
        primaryStage.setHeight(850);
        primaryStage.setWidth(1490);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}