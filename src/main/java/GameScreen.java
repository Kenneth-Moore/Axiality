import java.util.ArrayList;
import java.util.Random;

import geometry.*;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

// this is where you actually play the dot products.
public class GameScreen extends Scene {

	// this accuracy is used only to check if we need to change iteration
	// direction, it does not actually bound any numerical error.
	// There is another number, 'thresh', in ConvexSet.java which does that.
	final static double accuracy = 0.000000001;
	
	final static VBox root = new VBox();
    final HBox mainhbox = new HBox();

	private static final int WIDTH = 750;
	
	public static final int DATAWID = 600;
	public static final int DATAHIE = 325;
	
    final Color gridcol[] = { Color.RED, Color.BLUE, Color.RED, Color.BLUE };
    
    final VBox inputbox = new VBox();
    
    //layers field is where you can put a warning or something.
    final TextField volfield = new TextField();
    final TextField iters1field = new TextField();
    final TextField iters2field = new TextField();
    final TextField iters3field = new TextField();
    final Label iters1label = new Label("Iters1: ");
    final Label iters2label = new Label("Iters2: ");
    final Label iters3label = new Label("Iters3: ");
    
    final CheckBox presetsBox = new CheckBox("Use Preset Iters");
    final CheckBox preciseBox = new CheckBox("Precision Drag");
    final CheckBox infomodeBox = new CheckBox("Info Mode");
    
    final Label nlabel = new Label("refline:");
    final TextField nfield = new TextField();
    
    final Button calcbutton = new Button();
    final Button findshapebutton = new Button();
    final Button longfindshapebutton = new Button();

    final Button roundthreebutton = new Button();
    final Button roundthreebutton2 = new Button();
    
    final Button printbutton = new Button();
    final ComboBox<String> printOptions = new ComboBox<>();
    
    final Button loadbutton = new Button();
    
    final Label outputlabel = new Label("rat: ");
    final TextArea outputbox = new TextArea();
    
    final Button plusbutton = new Button("+");
    final Button minusbutton = new Button("-");
    
    final VBox outptbox = new VBox();
    final ImageView outbg = renderColor(Color.WHITE, WIDTH, WIDTH);
    final ImageView clickspace = renderColor(Color.TRANSPARENT, WIDTH, WIDTH);
    
    final ImageView databg = renderColor(Color.WHITE, DATAWID, DATAHIE);
    final ImageView dataclickspace = renderColor(Color.TRANSPARENT, DATAWID, DATAHIE);
    
    final StackPane linestack = new StackPane();
    final StackPane ballstack = new StackPane();
    
    final StackPane superstack = new StackPane();
    
    final VBox databox = new VBox();

    final StackPane datasuperstack = new StackPane();
    final StackPane databallstack = new StackPane();
    final StackPane datalinestack = new StackPane();

    
    final ConvexSet convexGuy = new ConvexSet(new ArrayList<>());
    final ConvexSetN convexGuyN = new ConvexSetN(new ArrayList<>());

    final ArrayList<Vector2> Data = new ArrayList<>();
    Vector2 DataPoint = Vector2.Zero;
    
    Vector2 direction = Vector2.normal(Vector2.create(1, 1));
    double translate = 13;
    int touched = 0;
    boolean iters2warning = false;
    
    public GameScreen() {
        super(root);
        
        
        
        mainhbox.getChildren().addAll(inputbox, outptbox);
        mainhbox.setPadding(new Insets(10));
        mainhbox.setSpacing(10);
    	
    	iters1field.setText("125");
    	iters1field.setPrefWidth(80);
    	iters1field.setOnMouseClicked(event -> {
    		if (infomodeBox.isSelected()) {
    			outputbox.setText (NodeExplanation.info(NodeExplanation.iters1));
        	}
    	});
    	
    	iters2field.setText("80");
    	iters2field.setPrefWidth(50);
    	iters2field.setOnMouseClicked(event -> {
    		if (infomodeBox.isSelected()) {
    			outputbox.setText (NodeExplanation.info(NodeExplanation.iters2));
        	}
    	});
    	
    	iters3field.setText("30");
    	iters3field.setPrefWidth(50);
    	iters3field.setOnMouseClicked(event -> {
    		if (infomodeBox.isSelected()) {
    			outputbox.setText (NodeExplanation.info(NodeExplanation.iters3));
        	}
    	});
    	
    	presetsBox.setSelected(true);
    	presetsBox.setOnAction(event -> {
    		if (infomodeBox.isSelected()) {
    			outputbox.setText (NodeExplanation.info(NodeExplanation.presets));
        	}
    	});
    	
    	preciseBox.setSelected(false);
    	preciseBox.setOnAction(event -> {
    		if (infomodeBox.isSelected()) {
    			outputbox.setText (NodeExplanation.info(NodeExplanation.precisiondrag));
        	}
    	});
    	
    	
    	infomodeBox.setSelected(false);
    	infomodeBox.setOnAction(event -> {
    		outputbox.setText (NodeExplanation.info(NodeExplanation.infocb));
    	});
    	
    	nfield.setEditable(false);
    	nfield.setText("13");
    	nfield.setPrefWidth(390);
    	nfield.setOnMouseClicked(event -> {
    		if (infomodeBox.isSelected()) {
    			outputbox.setText (NodeExplanation.info(NodeExplanation.reflinebox));
        	}
    	});
    	
    	volfield.setText("1");
    	volfield.setEditable(false);
    	volfield.setPrefWidth(150);
    	volfield.setOnMouseClicked(event -> {
    		if (infomodeBox.isSelected()) {
    			outputbox.setText (NodeExplanation.info(NodeExplanation.ratbox));
        	}
    	});
    	
    	outputbox.setPrefHeight(300);
    	outputbox.setPrefWidth(650);
    	outputbox.setWrapText(true);
    	outputbox.setOnMouseClicked(event -> {
    		if (infomodeBox.isSelected()) {
    			outputbox.setText (NodeExplanation.info(NodeExplanation.outputbox));
        	}
    	});
    	
    	Utils.colorButton(plusbutton, Color.LIGHTGREEN, Color.GREY);
    	Utils.colorButton(minusbutton, Color.LIGHTPINK, Color.GREY);
    	minusbutton.setPrefWidth(25);
    	plusbutton.setPrefWidth(25);
    	
    	plusbutton.setOnAction(event -> {
    		VectorN valid = setpresetsncheck("125", "60", "20", 10000, 200, 60, NodeExplanation.plus);
    		if (valid.get(0) < 0) return;
    		
    		int siz = convexGuy.getPlanes().size();
    		final Halfspace2 halfer = Halfspace2.create(Math.cos(siz), Math.sin(siz), 30);
    		convexGuy.addhalf(halfer);
    		
    		iters2warning = false;
    		calculate((int) valid.get(0), (int) valid.get(1), (int) valid.get(2));
    		renderScreen(false);
    	});
    	
    	minusbutton.setOnAction(event -> {
    		
    		VectorN valid = setpresetsncheck("125", "60", "20", 10000, 200, 60, NodeExplanation.minus);
    		if (valid.get(0) < 0) return;
    		
    		@SuppressWarnings("unchecked")
			final ArrayList<Halfspace2> spacers = (ArrayList<Halfspace2>) convexGuy.getPlanes().clone();
    		if (spacers.size() < 4) {
    			outputbox.setText("You need at least three planes!");
    			return;
    		}
    		
    		System.out.println(spacers.size());

    		spacers.remove(touched);
    		
    		convexGuy.refill(spacers);
    		System.out.println(spacers.size());
    		
    		touched = Math.min(touched, spacers.size());
    		
    		iters2warning = false;
    		
    		calculate((int) valid.get(0), (int) valid.get(1), (int) valid.get(2));
    		
    		renderScreen(false);
    	});
    	
    	final HBox pmHbox = new HBox();
    	pmHbox.getChildren().addAll(plusbutton, minusbutton);
    	
    	
    	loadbutton.setText("Load");
    	loadbutton.setOnAction(event -> {
    		VectorN valid = setpresetsncheck("700", "80", "30", 10000, 250, 60, NodeExplanation.loadbutton);
    		if (valid.get(0) < 0) return;
    		
    		final String input = outputbox.getText().strip();
    		
    		String[] linesplit = input.split("\n");
    		
    		ArrayList<Halfspace2> newvects = new ArrayList<>();
    		
    		for (String line : linesplit) {
    			String[] clipline = line.split(",");
    			
        		if (clipline.length < 2) {
        			String msg = "Improper syntax: no comma in line!\n\n" + NodeExplanation.syntax;
        			outputbox.setText(msg);
        			return;
        		}
    			
    			String str1 = clipline[0].replaceAll("[^\\d.-]", "");
    			String str2 = clipline[1].replaceAll("[^\\d.-]", "");
    			
    			if (str1 == "" || str2 == "") {
    				String msg = "Improper syntax, empty number! (comma misplaced?)\n\n" + NodeExplanation.syntax;
        			outputbox.setText(msg);
        			return;
    			}
    			
    			try {
    				double dub1 = Double.valueOf(str1);
    				double dub2 = Double.valueOf(str2);
    				
    				newvects.add(Halfspace2.fromRep(Vector2.create(dub1, dub2)));
    				
    			} catch (Exception e) {
    				String msg = "Improper syntax, too many periods, or unknown!\n\n" + NodeExplanation.syntax;
        			outputbox.setText(msg);
        			return;
    			}
    		}
    		
    		if (newvects.size() < 3) {
    			String msg = "Improper syntax: too few points!\n\n" + NodeExplanation.syntax;
    			outputbox.setText(msg);
    			return;
    		}
    		convexGuy.refill(newvects);
    		calculate((int) valid.get(0), (int) valid.get(1), (int) valid.get(2));
    		renderScreen(false);
    		
    	});
    	
    	printOptions.getItems().addAll(
    		    "Controls (Red)",
    		    "Vertices (Blue)",
    		    "Data (Graph)"
    		);
    	
    	printOptions.setValue("Controls (Red)");
    	
    	printOptions.setOnMouseClicked(event -> {
    		if (infomodeBox.isSelected()) {
    			outputbox.setText (NodeExplanation.info(NodeExplanation.printmenu));
        	}
    	});
    	
    	
    	printbutton.setText("Print:");
    	printbutton.setOnAction(event -> {
    		
    		if (infomodeBox.isSelected()) {
    			outputbox.setText (NodeExplanation.info(NodeExplanation.printbtn));
        		return;
        	}
    		
    		System.out.println("\n-print-\n");
    		
    		final double OGvol = convexGuy.vol();
    		
    		final ArrayList<Vector2> printers;
    		
    		if      (printOptions.getValue().equals("Controls (Red)"))   printers = convexGuy.getReps();
    		else if (printOptions.getValue().equals("Vertices (Blue)"))  printers = convexGuy.getvertices();
    		
    		else {
    			String s = "";
    			
    			if (Data.size() > 25000) {
    				s = "I can't print you that much data, sorry! (at most 25000 pts)";
    				outputbox.setText(s) ;
    				System.out.println(s);
    			}
    			
    			for (int i = 0; i < Data.size(); i++) s += "" + (Data.get(i).x/OGvol) + "\n";
    			
    			if (Data.size() > 3000) {
    				outputbox.setText("Sorry, I shouldn't print over 3000 data pts here."
    						+ "\n\nI have printed in the console though, I will do up to 25000 there.");
    			} else {
    				outputbox.setText(s);
    			}
				System.out.println(s);
    			
    			return;
    		}
    		
    		final String s = Utils.formatvertmsg(printers);
    		
    		outputbox.setText(s);
    		System.out.println(s);
    	});
    	
    	final HBox printhbox = new HBox();
    	printhbox.getChildren().addAll(printbutton, printOptions);
    	
    	calcbutton.setText("Calculate");
    	calcbutton.setOnAction(event -> {
    		
    		final long initTime = System.currentTimeMillis();
    		
    		VectorN valid = setpresetsncheck("700", "120", "30", 200000000, 300, 200, NodeExplanation.calcbutton);
    		if (valid.get(0) < 0) return;
    		
    		System.out.println("\n-Calc- 1: " + iters1field.getText().trim() + " 2: " + 
    						   iters2field.getText().trim() + " 3: " + iters3field.getText().trim() + " \n");
    		
    		iters2warning = false;
    		
    		if (valid.get(0) < 30001)
    			calculate((int) valid.get(0), (int) valid.get(1), (int) valid.get(2));
    		else  {
    			outputbox.setText("Calculating... ");
    			calculatetaskversion((int) valid.get(0), (int) valid.get(1), (int) valid.get(2));
    			return;
    		}
    			
    			
    		
    		//try the long pyramid!
    		
//    		final double len = 10000;
//    		final ArrayList<Vector2> vectsstart = convexGuy.getvertices();
//    		final ArrayList<Vector2> vects = new ArrayList<>();
//    		for (Vector2 vec : vectsstart) {
//    			boolean add = true;
//    			
//    			for (Vector2 test : vects) {
//    				if (vec.approxequals(test)) {
//    					add = false;
//    					break;
//    				}
//    			}
//    			
//    			if (add) vects.add(vec);
//    		}
//    		
//    		//System.out.println(vects.size());
//    		final VectorN topvect = VectorN.create(0,0,len/2);
//    		
//    		final ArrayList<HalfspaceN> pyramid = new ArrayList<>();
//    		
//    		final HalfspaceN base = HalfspaceN.create(topvect.scale(-1), len/2);
//    		pyramid.add(base);
//    		
//    		for (int i = 1; i < vects.size(); i ++) {
//    			final VectorN newv1 = VectorN.create(vects.get(i).x, vects.get(i).y, -len/2);
//    			final VectorN newv2 = VectorN.create(vects.get(i-1).x, vects.get(i-1).y, -len/2);
//    			
//    			final HalfspaceN halfer = HalfspaceN.fromVerts(newv1, newv2, topvect);
//    			
//    			pyramid.add(halfer);
//    		}
//    		
//    		convexGuyN.refill(pyramid);
    		
    		renderScreen(false);
    		
    		final long finalTime = System.currentTimeMillis();
    		
    		String msg = Utils.formattimemsg(initTime, finalTime, "Calculate done. Time:");
    		
    		if (Integer.valueOf(iters3field.getText().trim()) == 0)
    			msg += "\n\nTranslation tests (Iters3) all completed: " + !iters2warning;
    		
        	if (DataPoint.x > convexGuy.vol() + 100 * accuracy) {
        		msg += "\n\n(Unbounded)";
        	}
    		
    		outputbox.setText(msg);
    		System.out.println(msg);
    		
    	}); 
    	
    	longfindshapebutton.setText("Long Search");
    	longfindshapebutton.setOnAction(event -> {
    		VectorN valid = setpresetsncheck("800", "100", "30", 2000, 200, 100, NodeExplanation.R2longsearch);
    		if (valid.get(0) < 0) return;
    		
			gaussiterstage4((int) valid.get(0), (int) valid.get(1), (int) valid.get(2), true);
    	});
    	
    	findshapebutton.setText("Search");
    	findshapebutton.setOnAction(event -> {
    		
    		VectorN valid = setpresetsncheck("800", "100", "30", 2000, 200, 100, NodeExplanation.R2search);
    		if (valid.get(0) < 0) return;
    		
			gaussiterstage4((int) valid.get(0), (int) valid.get(1), (int) valid.get(2), false);
    	});
    	
    	roundthreebutton.setText("R3 Search");
    	Utils.colorButton(roundthreebutton, Color.PALEVIOLETRED, Color.GOLD);
    	roundthreebutton.setOnAction(event -> {
    		
    		VectorN valid = setpresetsncheck("40", "25", "25", 200, 60, 60, NodeExplanation.Def);
    		if (valid.get(0) < 0) return;
    		
    		double convvol = r3searchparallele();
    		
    		System.out.println("VOILA: " + convvol);
    		
    		renderScreen(false);
    	});
    	
    	roundthreebutton2.setText("R3 Calc");
    	Utils.colorButton(roundthreebutton2, Color.PALEVIOLETRED, Color.GOLD);

    	roundthreebutton2.setOnAction(event -> {
    		
    		final VectorN valid = setpresetsncheck("60", "50", "50", 180, 80, 80, NodeExplanation.Def);
    		if (valid.get(0) < 0) return;
    		
    		double convvol = r3calculate();
    		double OGvol = convexGuyN.vol(false);
    		System.out.println("VOILA: " + convvol/OGvol);
    		
    		renderScreen(false);
    		
    	});
    	
    	dataclickspace.setPickOnBounds(true);
    	dataclickspace.setOnMouseMoved(event -> {
    		if (infomodeBox.isSelected()) return;
    		renderScreen(false);
    		renderDataLine(event.getX(), event.getY(), false);
    	});
    	dataclickspace.setOnMouseDragged(event -> {
    		if (infomodeBox.isSelected()) return;
    		renderScreen(false);
    		renderDataLine(event.getX(), event.getY(), true);
    	});
    	//dataclickspace.setOnMouseExited(event -> {renderScreen();});
    	
    	dataclickspace.setOnMouseClicked(event -> {
    		if (infomodeBox.isSelected()) {
    			outputbox.setText (NodeExplanation.info(NodeExplanation.graph));
        	} else {
        		renderDataLine(event.getX(), event.getY(), true);
        	}
    	});
    	
    	clickspace.setPickOnBounds(true);
    	clickspace.setOnMousePressed(event -> {
    		//get the nearest point and drag it
    		
    		final VectorN valid = setpresetsncheck("125", "60", "20", 1000, 150, 60, NodeExplanation.clickspace);
    		if (valid.get(0) < 0) return;
    		
    		final double locx = event.getX() - (WIDTH/2);
    		final double locy = event.getY() - (WIDTH/2); 
    		
    		ArrayList<Vector2> halfsies = convexGuy.getReps();
    		
    		int moveind = -1;

    		double dist2 = 50; // minimum dist required to click
    		
    		for (int i = 0; i < halfsies.size(); i++) {
    			double newdist2 = (halfsies.get(i).x - locx)*(halfsies.get(i).x - locx) +
    					          (halfsies.get(i).y - locy)*(halfsies.get(i).y - locy);
    			if (newdist2 < dist2) {
    				moveind = i;
    				dist2 = newdist2;
    			}
    		}
    		
    		if (moveind == -1) return;
    		
    		touched = moveind;
    		
    		final int moveindfinal = moveind;
    		final double oglocxfinal = halfsies.get(moveind).x;
    		final double oglocyfinal = halfsies.get(moveind).y;

    		final double t;
    		if (preciseBox.isSelected()) t = 0.03;
    		else t = 1;
    		
    		clickspace.setOnMouseDragged(event2 -> {
    			
    			final double locx2 = event2.getX() - (WIDTH/2);
        		final double locy2 = event2.getY() - (WIDTH/2); 
    			
    			if (moveindfinal >= 0) {
        			convexGuy.replace(moveindfinal, Halfspace2.fromRep(
        					Vector2.create(t*locx2 + ((1-t)*oglocxfinal), t*locy2 + ((1-t)*oglocyfinal))));
        		}
        		
    			calculate((int) valid.get(0), (int) valid.get(1), (int) valid.get(2));
    			
    			renderScreen(false);
    		}) ;
    	});
    	
    	clickspace.setOnMouseReleased(event -> {
    		clickspace.setOnMouseDragged(event2 -> {});
    	});
    	
    	final HBox calcHBox = new HBox();
    	calcHBox.setSpacing(10);
    	calcHBox.setAlignment(Pos.CENTER);
    	calcHBox.getChildren().addAll(nlabel, nfield, outputlabel, volfield);
    	
    	final HBox calcHBox2 = new HBox();
    	calcHBox2.setSpacing(10);
    	calcHBox2.setAlignment(Pos.CENTER);
    	calcHBox2.getChildren().addAll(pmHbox, printhbox, calcbutton, findshapebutton, longfindshapebutton,
    								   loadbutton, preciseBox);
    	
    	final HBox ultraHBox = new HBox();
    	ultraHBox.setSpacing(10);
    	ultraHBox.setAlignment(Pos.CENTER);
    	ultraHBox.getChildren().addAll(iters1label, iters1field, iters2label, 
    								   iters2field, iters3label, iters3field, presetsBox, infomodeBox);
    	
    	final StackPane datasuperduperstack = new LeftGraphAxes();
    	
    	inputbox.setSpacing(10);
    	inputbox.getChildren().addAll(ultraHBox, outputbox, calcHBox, calcHBox2, datasuperduperstack);
    	
    	outptbox.setPrefSize(WIDTH, WIDTH);
    	outptbox.setSpacing(10);
    	   
    	superstack.getChildren().addAll(outbg, linestack, ballstack, clickspace);
    	outptbox.getChildren().addAll(superstack);
    	datasuperstack.getChildren().addAll(datalinestack, databallstack, dataclickspace);
    	
    	final Pane container = new Pane();
    	container.getChildren().add(datasuperstack);
    	
    	datasuperduperstack.getChildren().add(container);
    	final int datasuperlen = datasuperduperstack.getChildren().size();
    	datasuperduperstack.getChildren().get(datasuperlen-1).setTranslateY(LeftGraphAxes.offset/2);
    	datasuperduperstack.getChildren().get(datasuperlen-1).setTranslateX(LeftGraphAxes.axiswid+LeftGraphAxes.offset/2);
    	
    	root.getChildren().addAll(mainhbox);
    	
    	final ArrayList<Halfspace2> halfsies = new ArrayList<>();
    	
    	// This one is loaded to start:
		halfsies.add(Halfspace2.fromRep(Vector2.create(1.9251725197532472, 19.78331399341686)));
		halfsies.add(Halfspace2.fromRep(Vector2.create(0.485395950351331, -46.557334787897084)));
		halfsies.add(Halfspace2.fromRep(Vector2.create(-5.056706497397157, -24.085061354606264)));
		halfsies.add(Halfspace2.fromRep(Vector2.create(312.97017177145506, -13.054697984384223)));		
		
		convexGuy.refill(halfsies);
		
		calculate(125, 80, 30);
		renderScreen(false);
		
		double[] vec1 = {1.0,0,0};
		double[] vec2 = {-1.0,0,0};
		double[] vec3 = {0,0,1.0};
		double[] vec4 = {0,0,-1.0};
		double[] vec5 = {0,1.0,0};
		double[] vec6 = {0,-1.0,0};
		double[] vecS = {1,0,0.01};
		
		final HalfspaceN h1 = HalfspaceN.create(vec1, 1.0);
		final HalfspaceN h2 = HalfspaceN.create(vec2, 1.0);
		final HalfspaceN h3 = HalfspaceN.create(vec3, 1.0);
		final HalfspaceN h4 = HalfspaceN.create(vec4, 1.0);
		final HalfspaceN h5 = HalfspaceN.create(vec5, 1.0);
		final HalfspaceN h6 = HalfspaceN.create(vec6, 1.0);

		final ArrayList<HalfspaceN> list = new ArrayList<>(); 
		list.add(h1);
		list.add(h2);
		list.add(h3);
		list.add(h4);
		list.add(h5);
		list.add(h6);
		//list.add(hS);

		convexGuyN.refill(list);	
		
		outputbox.setText(NodeExplanation.info(NodeExplanation.infocb));

		//System.out.println(computeRatioN(VectorN.normal(VectorN.create(dirio)), 0) / convexGuyN.vol(false)); //
		
		/*
		HalfspaceN h1 = HalfspaceN.create(v1vec, 1);
		HalfspaceN reflN = HalfspaceN.create(refN, 4);
		
		Halfspace2 h21 = Halfspace2.create(3, 1, 1);
		Halfspace2 refl2 = Halfspace2.create(1, 1, 1);

		final ArrayList<HalfspaceN> halfsiesN = new ArrayList<>();
		final ArrayList<Halfspace2> halfsies2 = new ArrayList<>();

		halfsiesN.add(h1);
		halfsies2.add(h21);
		
		final ConvexSetN convexN = new ConvexSetN(halfsiesN);
		
		final ConvexSet convex2 = new ConvexSet(halfsies2);
		
		System.out.println("h1 = " + convexN.getPlanes().get(0).getRep());
		
		final ConvexSetN answer = convexN.reflect(reflN);
		final ConvexSet answer2 = convex2.reflect(refl2);
		
		System.out.println("h1 = " + answer.getPlanes().get(0).getRep());
		System.out.println("h2 = " + answer2.getPlanes().get(0).getRep());
		*/
    }
    
    public VectorN setpresetsncheck(final String iter1pre, final String iter2pre, final String iter3pre, 
    							    final int iter1max, final int iter2max, final int iter3max, 
    							    final NodeExplanation ex) {
    	
    	if (infomodeBox.isSelected()) {
    		
			outputbox.setText (NodeExplanation.info(ex));

    		return VectorN.create(-1, -1, -1);
    	}
    	
    	
 		if (presetsBox.isSelected()) {
			iters1field.setText(iter1pre);
			iters2field.setText(iter2pre);
			iters3field.setText(iter3pre);
		}
		
		final int iters1;
		final int iters2;
		final int iters3;
		
		try {
			iters1 = Integer.valueOf(iters1field.getText().trim());
			iters2 = Integer.valueOf(iters2field.getText().trim());
			iters3 = Integer.valueOf(iters3field.getText().trim());
		} catch (Exception e) {
			outputbox.setText ("Those iters are not numbers!");
			System.out.println("Those iters are not numbers!");
			return VectorN.create(-1, -1, -1);
		}
		
		if (iters1 > iter1max || iters2 > iter2max || iters3 > iter3max) {
			outputbox.setText ("Too high iters! Max iters for this function are: " 
							   + iter1max + ", " + iter2max + ", " + iter3max);
			
			System.out.println("Too high iters!");
			return VectorN.create(-1, -1, -1);
		} 	
		
		return VectorN.create(iters1, iters2, iters3);
    }
    
    public double r3searchparallele() {
    	
    	final int iters1 = Integer.valueOf(iters1field.getText());;
		
    	double currenttheta = 0.6922315153772051;
    	double currentt1 = 0.7949673750706697;
    	double currentalpha = 0.5451592620158696;
    	double currentbeta = 0.7130914576510023;
    	double currentt2 = 1.4470789184982942;
    	
    	double minsym = Parallelogram(currenttheta, currentt1, currentalpha, currentbeta, currentt2);
    	System.out.println(convexGuyN);
    	
    	final Random rando = new Random();
    	
    	for (int i=0; i < iters1; i++) {
    		
    		
        	double newtheta = currenttheta + 0.02*rando.nextGaussian() ;
        	double newt1 = currentt1 * Math.exp(0.05 * rando.nextGaussian());
        	double newalpha = currentalpha + 0.02*rando.nextGaussian();
        	double newbeta = currentbeta + 0.02*rando.nextGaussian();
        	double newt2 = currentt2 * Math.exp(0.05 * rando.nextGaussian());
        	
        	
        	final double newsym = Parallelogram(newtheta, newt1, newalpha, newbeta, newt2);
    		System.out.println(i + ", " + newsym);

    		
        	if (newsym < minsym) {
        		minsym = newsym;
        		
            	currenttheta = newtheta;
            	currentt1 = newt1;
            	currentalpha = newalpha;
            	currentbeta = newbeta;
            	currentt2 = newt2;
            	
            	// .out.println(minsym);
            	
//            	for (HalfspaceN half : convexGuyN.getPlanes()) {
//            		System.out.println(half);
//            	}
        	}
    	}
    	

    	Parallelogram(currenttheta, currentt1, currentalpha, currentbeta, currentt2);
    	
    	System.out.println(convexGuyN);
    	
    	System.out.println(currenttheta);
    	System.out.println(currentt1);
    	System.out.println(currentalpha);
    	System.out.println(currentbeta);
    	System.out.println(currentt2);

    	return minsym;
    }
    
    
    public void gaussiterstage4(final int iters1, final int iters2, final int iters3, 
    						    final boolean goonthen) {
    	System.out.println("Hill climbing to find you a better shape...");
    	outputbox.setText("Nothing better yet");
    	
    	final int iters4 = 400;
    	
		final long initTime = System.currentTimeMillis();
    	
    	final Task<Double> shapesearchtask = new Task<Double>() {

			@Override
			protected Double call() throws Exception {
				
		    	final int tweakers = 2*convexGuy.getPlanes().size();
		    	
		    	double[] currents = new double[tweakers];
		    	double[] BESTS    = new double[tweakers];
		    	
		    	final Random rando = new Random();
		    	for (int i = 0; i < tweakers; i += 2) {
		    		currents[i]   = convexGuy.getPlanes().get(i/2).getRep().x;
		    		currents[i+1] = convexGuy.getPlanes().get(i/2).getRep().y;
		    		BESTS[i]      = convexGuy.getPlanes().get(i/2).getRep().x;
		    		BESTS[i+1]    = convexGuy.getPlanes().get(i/2).getRep().y;
		    	}
		    	
		    	double currentnorm = gaussian(currents, true, iters1, iters2, iters3).x;
		    	double bestnorm = currentnorm;
		    	
		    	double step = 0.05; 
				
				for (int i=0; i < iters4; i++) {
					
					// I want the step to start at 0.5, and drop LINEARLY? to 0.
					step = 0.5 * (1 - (i/iters4));
					
					this.updateProgress(i, iters4);
					if (this.isCancelled()) break;
		    		
		    		System.out.println(i + ": " + currentnorm);
		    		
		    		for (int j = 0; j < tweakers; j+= 2) {
		        		
		        		final double[] news = new double[tweakers];
		        		
		        		for (int c = 0; c < tweakers; c++) {
		        			// we alternate between tweaking just one and tweaking all
		        			if (i % 100 < 50)
		        				news[c] = currents[c] + step * rando.nextGaussian();
		        			
		        			else {
		        				if (c == j || c == j+1)
		            				news[c] = currents[c] + step * rando.nextGaussian();
		            			else 
		            				news[c] = currents[c];
		        			}
		        		}
		        		final Vector2 newdata = gaussian(news, true, iters1, iters2, iters3);
		            	final double newnorm = newdata.x;
		            	
		            	final String msg;
		            	
		            	if (newnorm < 0.75 || newnorm > 1 || newnorm == Double.NaN) {
		            		
		            		
		            		if (newnorm < 0.75) {
		            			msg = " -- no changes (Improper Shape) (unexpectedly low: " + newdata.y + " ) \n\n" 
		            		     + Utils.formatvertmsg(convexGuy.getReps());;
		            		}
		            		else 
		            			msg = " -- no changes (Improper Shape)";
		            	}
		            	
		            	else if (newnorm < bestnorm) {
		            		
		            		msg = " ++ Improvement to best. newsym = " + newdata.y + "\n\n"
			            			  +	Utils.formatvertmsg(convexGuy.getReps());

	            			BESTS = news;
	            			bestnorm = newnorm;
	            			currents = news;
		            		currentnorm = newnorm;
	            		}

		            	
		            	else if (newnorm < currentnorm) {
		            		// if this happens, we always make the switch
		            		msg = " >> Improvement to current. newsym = " + newdata.y + "\n\n"
		            			  +	Utils.formatvertmsg(convexGuy.getReps());
		            		
		            		currents = news;
		            		currentnorm = newnorm;
		            	} 
		            	
		            	else if ((newnorm - currentnorm) < ConvexSet.thresh) {
		            		msg = " -- no changes";
		            	}
		            	
		            	else {
		            		
		            		final boolean goAnyway = (newnorm - currentnorm) <
		            				0.08*(50.0/(50.0+(i*i))) * Math.abs(rando.nextGaussian());
		            		
		            		if (goAnyway) {
		            			
		            			msg = " xx Annealing. newsym = " + newdata.y;
		            			currents = news;
			            		currentnorm = newnorm;
		            		} else {
		            			msg = " -- no changes";
		            		}
		            	}
		            	
		            	
		            	
		            	Platform.runLater( () -> { 
		            		renderScreen(true); 
		            		outputbox.setText(outputbox.getText() + "\n" + msg);
		            		outputbox.setScrollTop(Double.MAX_VALUE);
		            	});
		            	System.out.println(msg);
		            	
		            	//if (i % 100 < 50) {
		            		//we don't want to do too many additional allrandos, let's just move on
		            	//	break;
		            	//}
		    		}
		    	}
		    	gaussian(BESTS, true, iters1, iters2, iters3);
	    		Platform.runLater( () -> { renderScreen(false); });
				
		    	return null;
			}
    	};
    	
    	final Progress pr = new Progress(shapesearchtask, "Shape Search", iters4);
    	final Thread thread = new Thread(shapesearchtask);
    	
    	shapesearchtask.setOnSucceeded(success -> {
    		//put the end time in here
    		
    		final long finalTime = System.currentTimeMillis();
    		
    		final String msg = Utils.formattimemsg(initTime, finalTime, "shape search done. Time:");
    		
    		outputbox.setText(msg);
    		System.out.println(msg);
    		
    		pr.close();
    		
    		System.out.println("final: \n " + Utils.formatvertmsg(convexGuy.getReps()));
    		
    		if (goonthen) longfindshapebutton.fire();
    		else Platform.runLater( () -> { renderScreen(false); } );
    		
    	});
    	
    	shapesearchtask.setOnCancelled(cancelled -> {
    		outputbox.setText("Shape Search Cancelled");
        	System.out.println("// Shape Search Cancelled");
        	thread.interrupt();
        	pr.close();
        	
        });
    	shapesearchtask.setOnFailed(fail -> {
    		System.out.println(fail);
    		outputbox.setText("Shape Search Failed");
        	System.out.println("// Shape Search Failed");
        	pr.close();
        });

    	thread.start();

        pr.show();
    	
    	
    }
    
    public Vector2 gaussian(double[] newlist, boolean replace, final int iters1, final int iters2, final int iters3) {
    	
    	if (replace) {
	    	for (int c = 0; c < newlist.length; c += 2) {
	    		final Halfspace2 rep = Halfspace2.fromRep(Vector2.create(newlist[c], newlist[c+1]));

	    		convexGuy.replace(c/2, rep);
	    	}
    	}
    	final double sym = calculate(iters1, iters2, iters3);

    	final double vol = convexGuy.vol();
    	
    	return Vector2.create(normo(vol), sym/vol);
    }
    
    
    public double r3searchSimplex() {
    	
    	final int iters2 = Integer.valueOf(iters2field.getText());

    	double currentx1 = 1;
    	double currenty1 = 0;
    	double currentx2 = 0;
    	double currenty2 = 0;
    	double currentz2 = 1;
    	
    	double minsym =  Simplex(currentx1, currenty1, currentx2, currenty2, currentz2);
    			
    	System.out.println(convexGuyN);
    	
    	final Random rando = new Random();
    	
    	for (int i=0; i < iters2; i++) {
    		double newx1 = currentx1 * Math.exp(0.03 * rando.nextGaussian());
        	double newy1 = currenty1 + 0.03 * rando.nextGaussian();
        	double newx2 = currentx2 + 0.03 * rando.nextGaussian();
        	double newy2 = currenty2 + 0.03 * rando.nextGaussian();
        	double newz2 = currentz2 * Math.exp(0.03 * rando.nextGaussian());
        	
        	final double newsym = Simplex(newx1, newy1, newx2, newy2, newz2);
    		System.out.println(i + ", " + newsym);

    		
    		if (newsym < minsym) {
    			currentx1 = newx1;
    			currenty1 = newy1;
    			currentx2 = newx2;
    			currenty2 = newy2;
    			currentz2 = newz2;
    			
    			minsym = newsym;
    		}
        	
    	}
    	
    	Simplex(currentx1, currenty1, currentx2, currenty2, currentz2);
    		
    	System.out.println(convexGuyN);
    	
    	System.out.println(currentx1);
    	System.out.println(currenty1);
    	System.out.println(currentx2);
    	System.out.println(currenty2);
    	System.out.println(currentz2);
    	
    	return minsym;
    }
    
    public double r3search2() {
    	// search for shapes... 
    	
    	final int iters1;
    	final int iters2;
    	
		double[] vec1 = {0,0,1};
		double[] vec2 = {0,0,-1};
		
		final HalfspaceN h1 = HalfspaceN.create(vec1, 1);
		final HalfspaceN h2 = HalfspaceN.create(vec2, 1);
    	
    	try {
			iters1 = Integer.valueOf(iters1field.getText());
			iters2 = Integer.valueOf(iters2field.getText());
		} catch (Exception e) {
			System.out.println("Those iters are not numbers!");
			return -1;
		}
    	
    	double minsym = 1;
    	
    	ArrayList<HalfspaceN> best = new ArrayList<>();
    	
    	for (int i = -iters2; i < iters2; i++) {
    		// i is the iter for the second planes
    		
    		final double angl = (Math.PI*(iters2+2))/i;
    		final Vector2 angle = Vector2.fromangle(angl);
    		
    		double[] vec3 = {angle.y, 0, angle.x };
    		double[] vec4 = {-angle.y, 0, -angle.x };
    		
    		final HalfspaceN h3 = HalfspaceN.create(vec3, 1);
    		final HalfspaceN h4 = HalfspaceN.create(vec4, 1);
    		
        	for (int j = 1; j < iters2; j++) {
        		System.out.println((j + iters2*i + (iters2*iters2)) +  " of " + ((iters2*iters2) + (iters2*iters2)));

            	for (int k = 0; k < iters2; k++) {
            		
            		for (int t = 1; t < iters2; t++) {
            			
            			// j and k and t iterate over the third set of planes
            		
            			final double[] vec5 = {j, t, k};
            			final double[] vec6 = {-j, -t, -k};

                		final HalfspaceN h5 = HalfspaceN.create(vec5, 1);
                		final HalfspaceN h6 = HalfspaceN.create(vec6, 1);
                		
                		final ArrayList<HalfspaceN> list = new ArrayList<>();
                		
                		list.add(h1);
                		list.add(h2);
                		list.add(h3);
                		list.add(h4);
                		list.add(h5);
                		list.add(h6);
                		
                		convexGuyN.refill(list);
                		
                		double sym =  r3calculate()/convexGuyN.vol(false);
                		//System.out.println("\n sym: "+ sym + ", body: ");
                		
                		if (sym < minsym) {
                			// this a good shape!
                			best.clear();
                			best.addAll(list);
                			minsym = sym;
                		}
            		}
            	}
        	}
    	}
    	System.out.println(best.size());
    	
    	for (HalfspaceN half : best) {
    		System.out.println(half);
    	}
    	
    	return minsym;
    }
    
    //search this convex body for its ratio
    public double Parallelogram(final double theta, final double t1, 
    					 final double alpha, final double beta, final double t2) {
    	
		final ArrayList<HalfspaceN> list = new ArrayList<>(); 

		double[] vec1 = {0,0,1};
		double[] vec2 = {0,0,-1};
		final HalfspaceN h1 = HalfspaceN.create(vec1, 1);
		final HalfspaceN h2 = HalfspaceN.create(vec2, 1);
		
		list.add(h1);
		list.add(h2);

    	final Vector2 v3planar = Vector2.fromangle(alpha);

    	final double[] vec3 = {v3planar.x, 0, v3planar.y};
    	final double[] vec4 = {-v3planar.x, 0, -v3planar.y};

    	final double x5 = Math.cos(alpha) * Math.cos(beta);
    	final double z5 = Math.sin(alpha) * Math.cos(beta);
    	final double y5 = Math.sin(beta);
    	
    	final double[] vec5 = {x5,y5,z5};
    	final double[] vec6 = {-x5,-y5,-z5};
    	
    	final HalfspaceN h3 = HalfspaceN.create(vec3, t1);
		final HalfspaceN h4 = HalfspaceN.create(vec4, t1);
		
    	final HalfspaceN h5 = HalfspaceN.create(vec5, t2);
		final HalfspaceN h6 = HalfspaceN.create(vec6, t2);
		list.add(h3);
		list.add(h4);
		list.add(h5);
		list.add(h6);
		
		convexGuyN.refill(list);	
		
    	return r3calculate() / convexGuyN.vol(false);
    }
    
    public double Simplex(final double x1, final double y1, final double x2, final double y2, final double z2) {
    	
    	final ArrayList<HalfspaceN> list = new ArrayList<>(); 
    	
    	final VectorN vertex1 = VectorN.create(-1,-1,-1);
    	final VectorN vertex2 = VectorN.create(-1,1,-1);

    	final VectorN vertex3 = VectorN.create(x1,y1,-1);
    	final VectorN vertex4 = VectorN.create(x2,y2,z2);

		final HalfspaceN h1 = HalfspaceN.fromVerts(vertex2, vertex3, vertex4);
		final HalfspaceN h2 = HalfspaceN.fromVerts(vertex1, vertex3, vertex4);
		final HalfspaceN h3 = HalfspaceN.fromVerts(vertex1, vertex2, vertex4);
		final HalfspaceN h4 = HalfspaceN.fromVerts(vertex1, vertex2, vertex3);
		
		list.add(h1);
		list.add(h2);
		list.add(h3);
		list.add(h4);
		
		convexGuyN.refill(list);
		
		return r3calculate() / convexGuyN.vol(false);
    }
    
    public double r3calculate() {
    	
    	final int iters1;
    	final int iters2;
    	
    	double maxsym = 0;
		VectorN maxang = VectorN.Zero(3);
		double maxtranses = 0;
    	
		try {
			iters1 = Integer.valueOf(iters1field.getText());
			iters2 = Integer.valueOf(iters2field.getText());
		} catch (Exception e) {
			System.out.println("Those iters are not numbers!");
			return -1;
		}
		
		for (int i=-iters1; i<=iters1; i++) {
			for (int j=-iters1; j<=iters1; j++) {
				for (int k=0; k<=iters1; k++) {
					
					if ((Math.abs(i) != iters1) && (Math.abs(j) != iters1) && (Math.abs(k) != iters1)) continue;
					
					final double[] entries = {i, j, k};
					
					VectorN currentdir = VectorN.normal(VectorN.create(entries));	
					
					final Vector2 inner = fixedangleiterN(currentdir, iters2); 
					double newsym = inner.x;
					
					if (newsym > maxsym) {
						
						maxang = currentdir;
						maxsym = newsym;
						maxtranses = inner.y;
						break;
					}
				}
			}
		}
		
    	final ConvexSetN convrefl = (convexGuyN.reflect(new HalfspaceN(maxang, 0))).intersect(convexGuyN);

		System.out.println("--refl: \n" + convrefl);
		
    	return maxsym;
    }
    
    
    private void calculatetaskversion(final int iters1, final int iters2, final int iters3) {
    	Data.clear();
    	
		final long initTime = System.currentTimeMillis();
    	
    	final Task<Double> calctask = new Task<Double>() {

			@Override
			protected Double call() throws Exception {
				
				final int numlist = 15;
				
				// keep track 
				final double[] maxsyms = new double[numlist];
				final double[] maxangs = new double[numlist];
				
				for (int c = 0; c < numlist; c++) {
					maxsyms[c]=0;
					maxangs[c]=0;
				}
				
				
				final double anglesstep = Math.PI/iters1;
				
				double maxbasic = 0;
				
				for (int i=0; i<iters1; i++) {
					if (this.isCancelled()) return -1.0;
					this.updateProgress(i, iters1);
					
					//System.out.println("i = " + i + ", maxangs = " + maxsyms[0] + ", " + maxsyms[1] + ", " + maxsyms[2] + ", " + maxsyms[3]);
					
					Vector2 currentdir = Vector2.fromangle(anglesstep * i);
					
					final Vector2 inner = fixedangleiter(currentdir, iters2); 
					double newsym = inner.x;
					
					// try it aginst the top one, if it doesn't fit there we can save it if it is
					// not adjacent to the top one.
					restructurelist(numlist, i, anglesstep, newsym, maxangs, maxsyms);
					
					if (newsym > maxbasic) maxbasic = newsym;
				}
				
				// iter stage three is that we iterate over the top directions to try and find
				// an even better direction.
				
				final VectorN[] maxses = new VectorN[numlist];
				final double[] syms = new double[numlist];
				
				if (maxbasic > maxsyms[0])                   {
					final String msg = "Calc has goofed v1\n\n" 
							+ "maxsyms = " + maxsyms[0] + ", " + maxsyms[1] + ", " + maxsyms[2] + ", " + maxsyms[3]
									+ ", " + maxsyms[4] + ", " + maxsyms[5] + ", " + maxsyms[6]
								    + ", " + maxsyms[7] + ", " + maxsyms[8] + ", " + maxsyms[9];
					System.out.println(msg);
					
				}
				if (maxsyms[numlist-1] > maxsyms[numlist-2]) System.out.println("Calc has goofed v2");

				
				for (int i = 0; i < numlist; i++) {
					//System.out.println("i: " + i + " ang: " + maxangs[i] + " sym: " + maxsyms[i]);
					final Vector2 inner = fixedangleiter(Vector2.fromangle(maxangs[i]), iters2); 

					maxses[i] = iterstage3(maxangs[i],  2*anglesstep , maxsyms[i], iters2, iters3, inner.y);
					
					syms[i] = maxses[i].get(1);
					//System.out.println("i: " + i + " maxes: " + maxses[i]);
					
				}
				
				//System.out.println("basic: " + maxbasic/convexGuy.vol());
				
				int index = 0;
				double best = 0;
				
				for (int c = 0; c < numlist; c++) {
					if (syms[c] > best) {
						best = syms[c];
						index = c;
					}
				}
				
				if (maxsyms[index] > syms[index])                  System.out.println("Calc has goofed v3");
				
		 		direction = Vector2.fromangle(maxses[index].get(0));
	    		
	    		translate = maxses[index].get(2);
	    		
	    		double dataptangle = maxses[index].get(0);
	    		if      (dataptangle < 0)       dataptangle += Math.PI;
	    		else if (dataptangle > Math.PI) dataptangle -= Math.PI;
	    		
	    		DataPoint = Vector2.create(syms[index], dataptangle);
				
				return 1.0;
			}
		};
    	
		final Progress pr = new Progress(calctask, "Calculate", iters1);
    	final Thread thread = new Thread(calctask);
    	
    	calctask.setOnSucceeded(success -> {
    		//put the end time in here
    		
    		final long finalTime = System.currentTimeMillis();
    		
    		String msg = Utils.formattimemsg(initTime, finalTime, "Calculate done. Time:");
    		
    		if (Integer.valueOf(iters3field.getText().trim()) == 0)
    			msg += "\n\nTranslation tests (Iters3) all completed: " + !iters2warning;
    		
        	if (DataPoint.x > convexGuy.vol() + 100 * accuracy) {
        		msg += "\n\n(Unbounded)";
        	}
    		
    		outputbox.setText(msg);
    		System.out.println(msg);
    		
    		pr.close();
    		
    		Platform.runLater( () -> { renderScreen(false); } );
    	});
    	
    	calctask.setOnCancelled(cancelled -> {
    		outputbox.setText("Calculate Cancelled");
        	System.out.println("// Calculate Cancelled");
        	thread.interrupt();
        	pr.close();
        	
        });
    	calctask.setOnFailed(fail -> {
    		outputbox.setText("Calculate Failed!\n\n" + fail);
        	System.out.println("// Calculate Failed!");
    		System.out.println(fail);
        	pr.close();
        });

    	thread.start();

        pr.show();
    }
    
    
    public double calculate(final int iters1, final int iters2, final int iters3) {
    	Data.clear();
    	// We check many directions for the most symmetry possible.
		
		final int numlist = 10;
		
		// keep track 
		final double[] maxsyms = new double[numlist];
		final double[] maxangs = new double[numlist];
		
		for (int c = 0; c < numlist; c++) {
			maxsyms[c]=0;
			maxangs[c]=0;
		}
		
		
		final double anglesstep = Math.PI/iters1;
		
		double maxbasic = 0;
		
		for (int i=0; i<iters1; i++) {
			
			//System.out.println("i = " + i + ", maxangs = " + maxsyms[0] + ", " + maxsyms[1] + ", " + maxsyms[2] + ", " + maxsyms[3]);
			
			Vector2 currentdir = Vector2.fromangle(anglesstep * i);
			
			final Vector2 inner = fixedangleiter(currentdir, iters2); 
			double newsym = inner.x;
			
			// try it aginst the top one, if it doesn't fit there we can save it if it is
			// not adjacent to the top one.
			restructurelist(numlist, i, anglesstep, newsym, maxangs, maxsyms);
			
			if (newsym > maxbasic) maxbasic = newsym;
			if (iters1 < 30001) Data.add(Vector2.create(newsym, inner.y));
		}
		
		// iter stage three is that we iterate over the top directions to try and find
		// an even better direction.
		
		final VectorN[] maxses = new VectorN[numlist];
		final double[] syms = new double[numlist];
		
		if (maxbasic > maxsyms[0])                   {
			final String msg = "Calc has goofed v1\n\n" 
					+ "maxsyms = " + maxsyms[0] + ", " + maxsyms[1] + ", " + maxsyms[2] + ", " + maxsyms[3]
							+ ", " + maxsyms[4] + ", " + maxsyms[5] + ", " + maxsyms[6]
						    + ", " + maxsyms[7] + ", " + maxsyms[8] + ", " + maxsyms[9];
			System.out.println(msg);
			
		}
		if (maxsyms[numlist-1] > maxsyms[numlist-2]) System.out.println("Calc has goofed v2");

		
		for (int i = 0; i < numlist; i++) {
			//System.out.println("i: " + i + " ang: " + maxangs[i] + " sym: " + maxsyms[i]);
			final Vector2 inner = fixedangleiter(Vector2.fromangle(maxangs[i]), iters2); 

			maxses[i] = iterstage3(maxangs[i],  2*anglesstep , maxsyms[i], iters2, iters3, inner.y);
			
			syms[i] = maxses[i].get(1);
			//System.out.println("i: " + i + " maxes: " + maxses[i]);
			
		}
		
		//System.out.println("basic: " + maxbasic/convexGuy.vol());
		
		int index = 0;
		double best = 0;
		
		for (int c = 0; c < numlist; c++) {
			if (syms[c] > best) {
				best = syms[c];
				index = c;
			}
		}
		
		if (maxsyms[index] > syms[index])                  System.out.println("Calc has goofed v3");

		
		direction = Vector2.fromangle(maxses[index].get(0));
		
		translate = maxses[index].get(2);
		
		double dataptangle = maxses[index].get(0);
		if      (dataptangle < 0)       dataptangle += Math.PI;
		else if (dataptangle > Math.PI) dataptangle -= Math.PI;
		
		DataPoint = Vector2.create(syms[index], dataptangle);
		
		//System.out.println("returning " + syms[index] + ", " + syms[index]/convexGuy.vol());
		return syms[index];
    }

    private void restructurelist(final int numlist, final int i, final double anglesstep, final double newsym,
    		                     double[] maxangs, double[] maxsyms) {
    	
    	boolean close = false;
		for (int c = 0; c < numlist; c++) {
			
			if (Math.abs(maxangs[c] - (anglesstep * i)) < 2*anglesstep) {
				close = true;
				
				// we can only replace angle c with this one
				if (newsym > maxsyms[c]) {
					
					maxangs[c] = anglesstep * i;
					maxsyms[c] = newsym;
				} else continue;
				
				// now, it could be larger than other things on the list.
				for (int c2 = c; c2 > 0; c2--) {
					if (maxsyms[c2] > maxsyms[c2-1]) {
						
						//swap
						final double tempang = maxangs[c2];
						final double tempsym = maxsyms[c2];
						
						maxangs[c2] = maxangs[c2-1];
	    				maxsyms[c2] = maxsyms[c2-1];
	    				
	    				maxangs[c2-1] = tempang;
	    				maxsyms[c2-1] = tempsym;
					} else {
						//done swapping
						break;
					}
				}
				break;
			}
		}
		
		if (!close ) {
			
			// it's safe to replace any of the angles with this one.
			for (int c = 0; c < numlist; c++) {
				if (newsym > maxsyms[c]) {
					// shift all!
					for (int c2 = numlist-1; c2 > c; c2--) {
						
						//System.out.println("changing 2 c=" + c + ", " + (anglesstep * i));
						maxangs[c2] = maxangs[c2-1];
	    				maxsyms[c2] = maxsyms[c2-1];
					}

					// then replace...
					maxangs[c] = anglesstep * i;
    				maxsyms[c] = newsym;
    				
    				break;
				}
			}
		}
    }
    
    
    // this redraws the screen. It's also used to refresh things since it clears all lists.
    public void renderScreen(boolean onlydiagram) {
    	linestack.getChildren().clear();
    	ballstack.getChildren().clear();
    	
    	final Circle origin = new Circle();
    	origin.setFill(Color.BLACK);
    	origin.setRadius(3);
    	
    	ballstack.getChildren().add(origin);
    	ballstack.getChildren().get(0).setTranslateX(0);
		ballstack.getChildren().get(0).setTranslateY(0);
		
		final Line refline = new Line((WIDTH-100) * -direction.y, (WIDTH-100) * direction.x, 0, 0) ;
		refline.setStroke(Color.DARKCYAN);
		linestack.getChildren().add(0, refline);
		linestack.getChildren().get(0).setTranslateX(direction.x * translate);
		linestack.getChildren().get(0).setTranslateY(direction.y * translate);
		
		final ConvexSet convrefl = convexGuy.reflect(new Halfspace2(direction, translate)).intersect(convexGuy);
		
		final double OGvol = convexGuy.vol();
		final double refvol = convrefl.vol();
		
		drawConvSet(convexGuy, 1);
		drawConvSet(convrefl, 0.12);
		
		if (onlydiagram) return;
		
    	volfield.setText("" + refvol/OGvol);
    	nfield.setText("v=" + direction + ", t=" + translate);
		
    	databallstack.getChildren().clear();
    	datalinestack.getChildren().clear();
		
		renderData();
    }
    
    
    public void drawConvSet(final ConvexSet drawme, final double opacity) {
    	
    	for (Vector2 vect : drawme.getReps()) {
    		
    		final double metr = vect.norm();
    		
    		final Vector2 norm = vect.scale(1/metr);
    		
    		double len = WIDTH*Math.cos(Math.PI* metr/WIDTH)/2;
    		if (len < 0) len = 0;
    		
    		final Line l = new Line(-len * norm.y, len * norm.x, 
    								len * norm.y, -len * norm.x);
    		l.setOpacity(opacity);
    		
    		linestack.getChildren().add(0, l);
    		linestack.getChildren().get(0).setTranslateX(vect.x);
    		linestack.getChildren().get(0).setTranslateY(vect.y);
    		
    		// we don't need the control points for the non-OG convex set
    		if (opacity < 1) continue;
    		
       		if (vect.x > WIDTH/2 || vect.x < -WIDTH/2 || vect.y > WIDTH/2 || vect.y  < -WIDTH/2 ) continue;
    		
    		final Circle circ = new Circle();
    		circ.setRadius(4);
    		circ.setFill(Color.RED);
    		
    		ballstack.getChildren().add(0, circ);
    		ballstack.getChildren().get(0).setTranslateX(vect.x);
    		ballstack.getChildren().get(0).setTranslateY(vect.y);
    		
    	}
    	
    	for (Vector2 vect : drawme.getvertices()) {

    		if (vect.x > WIDTH/2 || vect.x < -WIDTH/2 || vect.y > WIDTH/2 || vect.y  < -WIDTH/2 ) continue;
    		
    		final Circle circ = new Circle();
    		circ.setRadius(4);
    		circ.setFill(Color.BLUE);
    		circ.setOpacity(opacity);

    		ballstack.getChildren().add(0, circ);
    		ballstack.getChildren().get(0).setTranslateX(vect.x);
    		ballstack.getChildren().get(0).setTranslateY(vect.y);
    		
    	}
    	
    }
    
    // returns true if it found that this data is from an unbounded set.
    
    public double normo(double vol) {
    	
    	/*
    	double sum = 0;
    	
    	for (int i=0; i < Data.size(); i++) {
    		sum += Math.pow((Data.get(i)/vol), 2);
    	}
    	
    	final double l2 = sum/Data.size();
    	
    	return ((l2 + (Math.pow(DataPoint.x/vol,2)*5))/6);
    	*/
    	return DataPoint.x/vol;
    }

    public boolean renderData() {

    	double vol = convexGuy.vol();
    	
    	if (DataPoint.x > vol + accuracy) {
    		System.out.println("Unbounded!");
    		return true;
    	}
    	
    	final double n = Data.size();

    	if (n > 30000) {
    		System.out.println("Too much data to draw!");
    		return false;
    	} 
    	

    	
    	final double[] xcoords = new double[(int) n];
    	final double[] ycoords = new double[(int) n];
    	
    	//System.out.println("normo: " + normo(vol));
    	
    	for (int i = 0; i < n; i++) {
    		
    		final double d = Data.get(i).x;
    		
    		final Circle dot = new Circle();
    		dot.setFill(Color.DARKRED);
    		dot.setRadius(2);
    		
    		final double transy = DATAHIE - (DATAHIE * d) / (vol);
    		final double transx = ((DATAWID / n) * (i));
    		
    		xcoords[i] = transx;
    		ycoords[i] = transy;
    		
    		databallstack.getChildren().add(0, dot);
    		databallstack.getChildren().get(0).setTranslateX( transx - DATAWID/2 );
    		databallstack.getChildren().get(0).setTranslateY( transy - DATAHIE/2 );
    	}
    	
		final Circle dot = new Circle();
		dot.setFill(Color.BLUE);
		dot.setRadius(3);
		
		final double transy = DATAHIE - (DATAHIE * (DataPoint.x)) / (vol);
		final double transx = ((DATAWID /  Math.PI) * (DataPoint.y));
    	
    	databallstack.getChildren().add(0, dot);
		databallstack.getChildren().get(0).setTranslateX( transx - DATAWID/2 );
		databallstack.getChildren().get(0).setTranslateY( transy - DATAHIE/2 );
    	
    	if (n > 1000) return false;
    	
    	for (int i=1; i < n; i++) {
    		//connect data[i] to data[i-1] !
    		
    		final double avgx = (xcoords[i] + xcoords[i-1])/2;
    		final double avgy = (ycoords[i] + ycoords[i-1])/2;
    		
    		final Line l = new Line(xcoords[i], ycoords[i], xcoords[i-1], ycoords[i-1]);
    		l.setStroke(Color.DARKRED);
    		
    		datalinestack.getChildren().add(0, l);
    		datalinestack.getChildren().get(0).setTranslateX(avgx - DATAWID/2);
    		datalinestack.getChildren().get(0).setTranslateY(avgy - DATAHIE/2);
    		
    				
    	}
    	return false;
    }
    
    private void renderDataLine(final double x, final double y, boolean justprint) {
    	
    	double vol = convexGuy.vol();

    	final double n = Data.size();

    	if (n > 30000 || n < 5) return;
    	
    	final int ind = (int) Math.round( (x * n) / DATAWID );
    	
    	try {
	    	if (ind == n && justprint) outputbox.setText("Symmetry in this direction: " + 
	    		           Data.get(0).x/vol   + " (translate: " + -Data.get(0).y   + ")");
			else if (justprint)        outputbox.setText("Symmetry in this direction: " + 
	    		           Data.get(ind).x/vol + " (translate: " + -Data.get(ind).y + ")");
    	} catch (IndexOutOfBoundsException e) {
    		return;
    	}
    	final Line holine = new Line(0,0,0,DATAHIE-1);
    	holine.setStroke(Color.GREY);
		datalinestack.getChildren().add(0, holine);
		datalinestack.getChildren().get(0).setTranslateX(-(DATAWID/2)+((DATAWID / n) * (ind)));
		datalinestack.getChildren().get(0).setTranslateY(0);
		
		final double ang = ( ind/n ) * Math.PI;
		final Vector2 tempdir = Vector2.fromangle(ang);
		final Vector2 datum;
		
		if (ind == n) datum = Vector2.create(Data.get(0).x, -Data.get(0).y);
		else          datum = Data.get(ind);
		
		//TODO
		final Line refline = new Line((WIDTH-100) * -tempdir.y, (WIDTH-100) * tempdir.x, 0, 0);
		refline.setStroke(Color.DARKRED);
		linestack.getChildren().add(0, refline);
		linestack.getChildren().get(0).setTranslateX(tempdir.x * datum.y);
		linestack.getChildren().get(0).setTranslateY(tempdir.y * datum.y);
		
    	final Line symline = new Line(0,0,DATAWID-1,0);
    	symline.setStroke(Color.GREY);
		datalinestack.getChildren().add(0, symline);
		datalinestack.getChildren().get(0).setTranslateX(0);
		datalinestack.getChildren().get(0).setTranslateY(-(datum.x/vol)*DATAHIE+(DATAHIE/2));
		
		
		
		
    }
    
    private double computeRatio(Vector2 dir, double trans) {
    	final ConvexSet convrefl = convexGuy.reflect(new Halfspace2(dir, trans)).intersect(convexGuy);
		
		return convrefl.vol();
    }
    
    private double computeRatioN(VectorN dir, double trans) {
    	final ConvexSetN convrefl = (convexGuyN.reflect(new HalfspaceN(dir, trans))).intersect(convexGuyN);
    	
//    	if (convrefl.degen) {
//    		
//    		System.out.println("degen ");
//    		return 0;
//    	} else System.out.println("not degen ");
		
		return convrefl.vol(false);
    }
    
    private Vector2 fixedangleiter(final Vector2 currentdir, final int iters2) {
    	double currenttrans;
		double currentsym = 0;
		
  		double stepsize = 4;
		int iterdirection = 1;
		
		double besttrans = 0;
		
		final double lower = computeRatio(currentdir, -1);
		final double upper = computeRatio(currentdir, 1);
		
		if (lower < upper) {
			currenttrans = -1;
			iterdirection = 1;
		}
		else {
			currenttrans = 1;
			iterdirection = -1;
		}
		
		for (int j = 0; j < iters2; j++) {
			
			if (stepsize < 0.0000000001) {
				return Vector2.create(currentsym, besttrans);
			}
			
			currenttrans += stepsize * iterdirection;
			final double newvol = computeRatio(currentdir, currenttrans);
			
			if (newvol > currentsym) {
				// things improve, so we move again
				currentsym = newvol;
				besttrans = currenttrans;
				
				// but first! Maybe need to change directions?
				final double checksym = computeRatio(currentdir, currenttrans + iterdirection * accuracy);
				
				if (checksym < newvol) {
					// we have passed the max, backup!
					stepsize = stepsize / 2;
					iterdirection *= -1;
				}
			}
			
			else {
				// we overstepped, so back up and try again.
				currenttrans -= stepsize * iterdirection;
				
				stepsize = stepsize / 2;
			}
		}
		
		// if it makes it here, the step size did not get small enough
		iters2warning = true;
		return Vector2.create(currentsym, besttrans);
    }
    
    private Vector2 fixedangleiterN(final VectorN currentdir, final int iters2) {
    	
    	return Vector2.create(computeRatioN(currentdir, 0), 0);
    	
    	/*
    	
    	double currenttrans = 0;
		double currentsym = 0;
		
  		double stepsize = 4;
		int iterdirection = 1;
		
		double besttrans = 0;
		
		final double lower = computeRatioN(currentdir, -accuracy);
		final double upper = computeRatioN(currentdir, accuracy);
		
		if (lower < upper) iterdirection =  1;
		else               iterdirection = -1;
		
		for (int j = 0; j < iters2; j++) {
			
			currenttrans += stepsize * iterdirection;
			final double newvol = computeRatioN(currentdir, currenttrans);
			
			if (newvol > currentsym) {
				// things improve, so we move again
				currentsym = newvol;
				besttrans = currenttrans;
				
				// but first! Maybe need to change directions?
				final double checksym = computeRatioN(currentdir, currenttrans + iterdirection * accuracy);
				
				if (checksym < newvol) {
					// we have passed the max, backup!
					stepsize = stepsize / 2;
					iterdirection *= -1;
				}
			}
			
			else {
				// we overstepped, so back up and try again.
				currenttrans -= stepsize * iterdirection;
				
				stepsize = stepsize / 2;
			}
		}
		
		return Vector2.create(currentsym, besttrans);
		
		*/
    }
    
    private VectorN iterstage3(final double initang, final double initstep, final double initsym, 
    						   final int iters2, final int iters3, final double inittrans) {
    	
    	double currentang = initang;
    	double currentsym = initsym;
    	
    	double stepsize = initstep;
    	int iterdirection = 1;
    	
    	//System.out.println("\n Iter3! " + currentang + "," + currentsym + "," + stepsize + "," + iterdirection);

    	
    	double bestang = initang;
    	Vector2 best = Vector2.create(initsym, inittrans);

    	final double lower = fixedangleiter(Vector2.fromangle(initang-(accuracy*100)), iters2).x;
		
		if (lower < initsym) iterdirection =  1;
		else                 iterdirection = -1;
    	
    	for (int i = 0; i < iters3; i++) {
    		
        	//System.out.println(currentang + "," + currentsym + "," + stepsize + "," + iterdirection);
    		
    		currentang += stepsize * iterdirection;
    		final Vector2 newinfo = fixedangleiter(Vector2.fromangle(currentang), iters2);
    		final double newvol = newinfo.x;
			if (newvol > currentsym) {
				// things improve, so we move again
				currentsym = newvol;
				best = newinfo;
				bestang = currentang;
				
				// but first! Maybe need to change directions?
				final double checksym = fixedangleiter(Vector2.fromangle(currentang + iterdirection * accuracy), iters2).x;
				
				if (checksym < newvol) {
					// we have passed the max, backup!
					stepsize = stepsize / 2;
					iterdirection *= -1;
				}
			}
			
			else {
				// we overstepped, so back up and try again.
				currentang -= stepsize * iterdirection;
				
				stepsize = stepsize / 2;
			}
    	}
    	double[] returner = {bestang, best.x, best.y};
    	return VectorN.create(returner);
    }
       
    @SuppressWarnings("exports")
	public static ImageView renderColor(final Color color, final int wid, final int hie) {
        final WritableImage image = new WritableImage(wid, hie);

        setImageColor(image, color, wid, hie);

        final ImageView imageView = new ImageView(image);

        return imageView;
    }
    
    private static void setImageColor(final WritableImage image, final Color color, final int wid, final int hie) {
        final PixelWriter pixelWriter = image.getPixelWriter();

        for (int pixelX = 0; pixelX < wid; pixelX += 1) {
            for (int pixelY = 0; pixelY < hie; pixelY += 1) {
                pixelWriter.setColor(pixelX, pixelY, color);
            }
        }
    }
 

}



