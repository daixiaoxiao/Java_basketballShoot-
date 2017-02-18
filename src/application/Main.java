package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Optional;

import controller.BallController;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {

	private BallController engine = new BallController();
    private AnimationTimer animatedImage;
    private Timeline gameLoop, countdown;
    private static Scene theScene;
    private static Pane root;
    private static ArrayList<String> input;
    private static AnimatedShoot stephen_curry, ball_rotate;
	private static final Integer STARTTIME = 15;
	private static IntegerProperty timeSeconds = new SimpleIntegerProperty(STARTTIME);
	private final static ProgressBar pb = new ProgressBar();
	private final static VerticalProgressBar Vpb = new VerticalProgressBar(15, 100);
	private final static Label timerLabel = new Label();
	private final static Rectangle rect = new Rectangle();
	private final static StackPane stack = new StackPane();
    private Button button, restart;
    private HBox hbox;
    private String player;

    @Override
	public void start(Stage primaryStage) {
		try {
			primaryStage.setTitle( "Basketball shooting!" );

			VBox Vpane = new VBox();
			Pane borderRoot = new Pane();
			
		    MenuBar menuBar = new MenuBar();
		   	
		    Menu menuFile = new Menu("File");
	        MenuItem exitMenuItem = new MenuItem("Exit");
	        MenuItem saveMenuItem = new MenuItem("Save");
	        exitMenuItem.setOnAction(actionEvent -> Platform.exit());
	        
	        saveMenuItem.setOnAction(new EventHandler<ActionEvent>() {
	            public void handle(ActionEvent t) {
	            	TextInputDialog dialog = new TextInputDialog("dai");
	            	dialog.setTitle("User Iput");
	            	dialog.setHeaderText("A User Input Dialog");
	            	dialog.setContentText("Please enter your name:");
	            	
	            	Optional<String> result = dialog.showAndWait();
	                result.ifPresent(name -> enterPlayer(name));
	            }
	        });
	        
	        menuFile.getItems().add(saveMenuItem);
	        menuFile.getItems().addAll(exitMenuItem);

	        menuBar.prefWidthProperty().bind(primaryStage.widthProperty());
	        menuBar.getMenus().addAll(menuFile);
		    
	        root = new Pane();
	        
	        Image BackImage = new Image("file:resources/shooterbackground.png", 768, 512, false, false);
	        ImageView BackView = new ImageView(BackImage);
	        root.getChildren().add(BackView);
	        
	        borderRoot.getChildren().add(menuBar);
		    
		    Vpane.getChildren().addAll(borderRoot, root);
		    theScene = new Scene( Vpane );

			primaryStage.setScene( theScene );
	 
			Canvas canvas = new Canvas( 768, 512 );
			root.getChildren().add( canvas );

			GraphicsContext gc = canvas.getGraphicsContext2D();
			
			prepareWidgets();

			KeyPressed pressed = new KeyPressed();

			prepareActionHandlers();

			prepareAnimatedImages();

			final long startNanoTime = System.nanoTime();
			//You can add a specific action when each frame is started.
	        animatedImage = new AnimationTimer() {
	            @Override
	            public void handle(long currentNanoTime) {
	            	double t = (currentNanoTime - startNanoTime) / 1000000000.0;
	            	if(pressed.keyPressed)
	            	{
	            		engine.getRender().gesture(gc, stephen_curry, t);
	            		engine.getRender().rotate(gc, ball_rotate, t);
                    }
	            }
            };

			gameLoop = new Timeline();
			gameLoop.setCycleCount( Timeline.INDEFINITE );

            countdown = new Timeline();

            //one can add a specific action when the keyframe is reached
            EventHandler<ActionEvent> onFinished = new EventHandler<ActionEvent>() {
                public void handle(ActionEvent t) {
					engine.getRender().init_ball(gc, pressed);
                	gameLoop.stop();
                	animatedImage.stop();
                	updateScore();
                	}
            };

            countdown.getKeyFrames().add(
                    new KeyFrame(Duration.seconds(STARTTIME+1), onFinished,
                    new KeyValue(timeSeconds, 0)));

			KeyFrame kf = new KeyFrame(
					Duration.seconds(0.009),                // 120 FPS
					new EventHandler<ActionEvent>()
					{
						public void handle(ActionEvent ae)
						{

							// game logic

							if (input.contains("UP"))
							{
								if(engine.getBasketball().getVelocityY() <= 42)
								{
									engine.getBasketball().addVelocity(0, 1);
									Vpb.getProgressBar().setProgress(engine.getBasketball().getVelocityY() / 40);
								}
							}

							if (input.contains("RIGHT"))
							{
								if(engine.getBasketball().getVelocityX() <= 38)
								engine.getBasketball().addVelocity(1, 0);
								pb.setProgress(engine.getBasketball().getVelocityX() / 30);
							}

							if (input.contains("S"))
							{
								pressed.setKeyPressed(true);
							}


							if(pressed.keyPressed)
								engine.update();

							// collision detection
							engine.hit(pressed);

							engine.render(gc);
							engine.getRender().init_ball(gc, pressed);
						}
					});

			gameLoop.getKeyFrames().add( kf );

			prepareButtons();

			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
	
	public void enterPlayer(String player_name){
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			
			e.printStackTrace();
		}
		String url = "jdbc:mysql://studev.groept.be:3306/a15_sd10";
		String sql = "SELECT name FROM player where name = '"+player_name+"'";
		String sql_0 = "INSERT INTO player (name, score) VALUES ('"+player_name+"', NULL)";
		
		player = player_name;
		
		try (Connection con = DriverManager.getConnection(url, "a15_sd10", "70xkcta4");
				Statement statement = con.createStatement();){
			ResultSet resultset = statement.executeQuery(sql);
			if(resultset.absolute(1)){
//				System.out.println("The player name already in the database...");
				
				Alert alert = new Alert(AlertType.INFORMATION);
	    	    alert.setTitle("Information");
	    	    alert.setHeaderText("Welcome back:  " + player);
	    	    alert.setContentText("     Let's start!");

	    	    alert.show();
			}
			else{
//				System.out.println("Inserted records into the table...");
				statement.executeUpdate(sql_0);
			}
	    }
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
public void updateScore() {
		
		// load driver
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			
			e.printStackTrace();
		}

		int score = engine.getRender().i;
		String url = "jdbc:mysql://studev.groept.be:3306/a15_sd10";
		String sql_0 = "SELECT * FROM player where name = '"+player+"'";
		String sql = "INSERT INTO player " + "VALUES ('"+player+"', '"+score+"')";
		String sql_1 = "UPDATE player set score = ? where name = '"+player+"'";
		
		
		try (Connection con = DriverManager.getConnection(url, "a15_sd10", "70xkcta4");
			Statement statement = con.createStatement();){
		    ResultSet rs = statement.executeQuery(sql_0);
		    if(rs.absolute(1)){
//				System.out.println("The player name already in the database, please update the score if nessecary...");
				
				int old_score = rs.getInt(2);
//				System.out.println("**********************************");
				if(old_score < score){
					PreparedStatement preparedStmt = con.prepareStatement(sql_1);
		    	    preparedStmt.setInt(1, score);
		    	    preparedStmt.executeUpdate();
//		    	    System.out.println("updated the highest score");
		    	    
		    	    Alert alert = new Alert(AlertType.INFORMATION);
		    	    alert.setTitle("Score");
		    	    alert.setHeaderText("Congratulations! It's your highest score!");
		    	    alert.setContentText("Your score is: " + score);

		    	    alert.show();
				}
				else{
//					System.out.println("The score isn't highest, no need to update!");
					
		    	    Alert alert = new Alert(AlertType.INFORMATION);
		    	    alert.setTitle("Score");
		    	    alert.setHeaderText("Unfortunately you didn't beat yourself~");
		    	    alert.setContentText("Your score is: " + score);

		    	    alert.show();
				}
								
			}
			else{
//				System.out.println("Inserted records into the table...");
				statement.executeUpdate(sql);
			}
		}
			
			// ps.setInt(1, userId);
		 catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void prepareButtons()
	{
	    button = new Button();
        button.setText("ST");
        button.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent event) {
            	root.getChildren().add(stack);
    			root.getChildren().addAll(pb, Vpb.getProgressBar());
				countdown.play();
				gameLoop.play();
				animatedImage.start();
            }
        });
        button.setStyle("-fx-background-radius: 10em; " +
                "-fx-min-width: 50px; " +
                "-fx-min-height: 50px; " +
                "-fx-max-width: 50px; " +
                "-fx-max-height: 50px;");

        restart = new Button();
        restart.setText("RE");
        restart.setOnAction(new EventHandler<ActionEvent>(){
        	public void handle(ActionEvent event){
        		if (countdown != null && gameLoop != null){
        			countdown.stop();
        			gameLoop.stop();
        			animatedImage.stop();
        		}
				timeSeconds.setValue(STARTTIME);
				engine.getRender().i = 0;
				countdown.playFromStart();
				gameLoop.playFromStart();
				animatedImage.start();
        	}
        });
        restart.setStyle( "-fx-background-radius: 5em; " +
                "-fx-min-width: 50px; " +
                "-fx-min-height: 50px; " +
                "-fx-max-width: 50px; " +
                "-fx-max-height: 50px;");

        hbox = new HBox(8); 
        hbox.setLayoutX(310);
        hbox.setLayoutY(0);
        hbox.getChildren().addAll(button, restart);
        root.getChildren().add(hbox);
	}
	
	private static void prepareWidgets()
	{
	    pb.setLayoutX(100);
		pb.setLayoutY(400);
        pb.setMinSize(StackPane.USE_PREF_SIZE, StackPane.USE_PREF_SIZE);
        pb.setPrefSize(100, 15);
        pb.setMaxSize(StackPane.USE_PREF_SIZE, StackPane.USE_PREF_SIZE);

		Vpb.getProgressBar().setLayoutX(15);
		Vpb.getProgressBar().setLayoutY(180);
		
		timerLabel.textProperty().bind(timeSeconds.asString());
        timerLabel.setTextFill(Color.WHITE);
        timerLabel.setStyle("-fx-font-size: 4em;");

		rect.setWidth(80);
		rect.setHeight(80);
		rect.setArcWidth(10);
		rect.setArcHeight(10);

        stack.getChildren().addAll(rect, timerLabel);
        stack.setLayoutX(0);
        stack.setLayoutY(0);
	}

	private static void prepareActionHandlers()
	{
        input = new ArrayList<String>();

		theScene.setOnKeyPressed(
				new EventHandler<KeyEvent>()
				{
					public void handle(KeyEvent e)
					{
						String code = e.getCode().toString();
						if ( !input.contains(code) )
							input.add( code );
					}
				});

		theScene.setOnKeyReleased(
				new EventHandler<KeyEvent>()
				{
					public void handle(KeyEvent e)
					{
						String code = e.getCode().toString();
						input.remove( code );
					}
				});
	}

	private static void prepareAnimatedImages()
	{
		stephen_curry = new AnimatedShoot();
		Image[] imageArray = new Image[6];
		for (int i = 0; i < 6; i++)
			imageArray[i] = new Image( "file:resources/curry_" + i + ".png" );
		stephen_curry.frames = imageArray;
		stephen_curry.duration = 0.200;

		ball_rotate = new AnimatedShoot();
		Image[] ballArray = new Image[4];
		for (int i = 0; i < 4; i++)
			ballArray[i] = new Image( "file:resources/newbasketball_" + i + ".png" );
		ball_rotate.frames = ballArray;
		ball_rotate.duration = 0.100;
	}
}
