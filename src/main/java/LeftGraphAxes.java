

import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class LeftGraphAxes extends StackPane {

	public static final int axiswid = 40;
	
	public static final double tickwid = 5;
	
	public static final int leftticks = 8;
	
	public static final int offset = 20;
	
	public LeftGraphAxes() {
		
		int innerwid = GameScreen.DATAWID;
		int innerhie = GameScreen.DATAHIE;
		
		final Line leftline = new Line(0, 0, 0, innerhie);
		final double ltransx = (axiswid-innerwid)/2;
		final double ltransy = -axiswid/2;
	
		final double btransx = axiswid/2;
		final double btransy =  (innerhie-axiswid)/2 ;
		
		this.getChildren().add(0, leftline);
		this.getChildren().get(0).setTranslateX(ltransx);
		this.getChildren().get(0).setTranslateY(ltransy);
		
		final Line bottline = new Line(0, 0, innerwid, 0);
		this.getChildren().add(0, bottline);
		this.getChildren().get(0).setTranslateX(btransx);
		this.getChildren().get(0).setTranslateY(btransy);

		for (int i = 0; i < leftticks+1; i++) {
			final double ind = (double) i;
			
			final Label lab = new Label("" + (1 - (ind / leftticks)));
			
			final Line tick = new Line(0,0,tickwid,0);
			
			final double ttransx = ltransx - tickwid/2;
			final double ttransy = ltransy + innerhie/2 - (innerhie * (1 - (ind / leftticks)));
			this.getChildren().add(0, tick);
			this.getChildren().get(0).setTranslateX(ttransx);
			this.getChildren().get(0).setTranslateY(ttransy);
			
			this.getChildren().add(0, lab);
			this.getChildren().get(0).setTranslateX(ttransx-24);
			this.getChildren().get(0).setTranslateY(ttransy);
		}
		
		for (int i = 0; i < 5; i++) {
			final double ind = (double) i;

			final Label lab;
			if      (i == 0) lab = new Label("0");
			else if (i == 1) lab = new Label("\u03C0/4");
			else if (i == 2) lab = new Label("\u03C0/2");
			else if (i == 3) lab = new Label("3\u03C0/4");
			else             lab = new Label("\u03C0"); // if (i == 4)
			
			final Line tick = new Line(0,0,0,tickwid);
			
			final double ttransx = btransx + innerwid/2 - (innerwid * (1 - (ind /4)));
			final double ttransy = btransy + tickwid/2;
			
			this.getChildren().add(0, tick);
			this.getChildren().get(0).setTranslateX(ttransx);
			this.getChildren().get(0).setTranslateY(ttransy);
			
			this.getChildren().add(0, lab);
			this.getChildren().get(0).setTranslateX(ttransx);
			this.getChildren().get(0).setTranslateY(ttransy+16);
		}
		
		final ImageView bg = GameScreen.renderColor(Color.WHITE, axiswid+innerwid+offset, axiswid+innerhie+offset);
		this.getChildren().add(0, bg);

	}
}
