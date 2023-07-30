
import java.text.DecimalFormat;

import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public final class Progress {
    private final VBox root = new VBox();
    private final HBox progbox = new HBox();
    private final Label info = new Label();
    
    private final Button cancelButton = new Button();
    private final ProgressBar progressBar = new ProgressBar();
    private final Scene scene = new Scene(root);
    private final Stage stage = new Stage();
    
    final String nameo;
    final long inittime;
    final long endit;

    // We don't care what task return type is
    public Progress(final Task<?> task, final String name, final int enditers) {
    	
    	endit = enditers;
    	nameo = name;
    	inittime = System.currentTimeMillis();
    	
    	DecimalFormat df = new DecimalFormat("#.##");
    	
    	progressBar.setPrefWidth(200);
    	info.setText("Running " + nameo);
    	
    	progbox.setSpacing(10);
    	progbox.setAlignment(Pos.CENTER);
    	progbox.getChildren().addAll(progressBar, cancelButton);

    	root.setSpacing(10);
    	root.setPadding(new Insets(10));
    	root.setAlignment(Pos.CENTER_LEFT);
    	root.getChildren().addAll(progbox, info);
    	
        stage.setScene(scene);
        stage.setOnCloseRequest(event -> {
            task.cancel();
            stage.close();
        });

        // Block from accessing the main window until this stage is closed
        stage.initModality(Modality.APPLICATION_MODAL);

        progressBar.progressProperty().bind(task.progressProperty());
        
        progressBar.progressProperty().addListener((o, oldVal, newVal) -> {
        	String msg = df.format( 100 * ((double) newVal)) + "%";
        	
    		double intval = ((double) newVal) * endit;
        	
    		final long newttime = System.currentTimeMillis();
    		final long timedif = newttime - inittime;
    		// (time passed)/(iters so far) = total time/500
    		
    		final long projectedtime = Math.round((enditers/intval) * timedif);
    		final long remaining = projectedtime - timedif;
    		
    		msg = Utils.formattimemsg(0, remaining, " est. time remaining:") + " (" + msg + " done)";
    		
        	info.setText(msg);
        });

        cancelButton.setText("Cancel");
        cancelButton.setOnAction(event -> {
            task.cancel();
            stage.close();
        });
    }
    
    public void close() {
        stage.close();
    }

    public void show() {
        stage.show();
    }
}
