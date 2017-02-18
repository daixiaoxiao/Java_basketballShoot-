package view;

import java.util.function.Consumer;

import application.AnimatedShoot;
import application.KeyPressed;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.ArcType;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import model.BasketGoalPosts;
import model.Basketball;
import model.BasketballCourt;

public class Renderer implements Consumer<GraphicsContext>{
    private Basketball basketball;
    private BasketballCourt court;
    private BasketGoalPosts post;
    private Image ballImage, courtImage, postImage;
    public Integer i = 0;

    public Renderer(Basketball basketball, BasketballCourt court, BasketGoalPosts post) {
		this.basketball = basketball;
		this.court = court;
		this.post = post;
	}

    @Override
	public void accept(GraphicsContext gc) {
    	gc.clearRect(0, 0, 768, 512);

        Font theFont = Font.font( "Helvetica", FontWeight.BOLD, 24 );
        gc.setFont( theFont );
        gc.setLineWidth(1);

        courtImage = new Image("file:resources/basketballcourt.jpg");
        gc.drawImage( courtImage, court.getPositionX(), court.getPositionY() );

        postImage = new Image("file:resources/basketwave1.png");
        double[] xPoints = {680.0, 750.0, 750.0, 725.0, 725.0, 680.0, 680.0, 585.0, 585.0, 680.0};
        double[] yPoints = {70.0,  175.0, 512.0, 512.0, 175.0, 250.0, 180.0, 180.0, 182.0, 182.0};

    	Stop[] stops = new Stop[]{new Stop(0, Color.LIGHTGREEN), new Stop(1, Color.DARKGREEN)};
    	LinearGradient lg1 = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, stops);
    	Color c2 = new Color(84 / 255.0, 56 / 255.0, 71 / 255.0, 1.0);

    	gc.setFill(lg1);
        gc.setStroke(c2);
        gc.fillPolygon(xPoints, yPoints, 10);
        gc.strokePolygon(xPoints, yPoints, 10);

        gc.drawImage(postImage, 585, 183);

        gc.fillArc(655, 165, 70, 100, 140, 60, ArcType.OPEN);
        gc.fillArc(525, 165, 70, 100, 340, 60, ArcType.OPEN);
        
        gc.setFill(Color.BLACK);
        gc.setStroke(Color.BLACK);
        gc.fillRoundRect(650, 0, 118, 40, 10, 10);
        
        gc.setFill(Color.WHITE);
        gc.setStroke(Color.WHITE);
        gc.fillText("score:" + i.toString(), 665, 30);
        
    }

    public void rotate(GraphicsContext gc, AnimatedShoot shoot, double t)
    {
        gc.drawImage(shoot.getFrame(t), basketball.getX(), basketball.getY());
    }

    public void gesture(GraphicsContext gc, AnimatedShoot gesture, double t)
    {
    	gc.drawImage(gesture.getFrame(t), 0, 200);
    }

    public void init_ball(GraphicsContext gc, KeyPressed pressed)
    {
    	if(!pressed.keyPressed)
    	{
          Image initCurry = new Image("file:resources/curry_0.png");
          gc.drawImage(initCurry, 0, 200);
          ballImage = new Image("file:resources/newbasketball.png");
          gc.drawImage( ballImage, 40, 315 );
    	}
    }

    public void reset_progressbar(ProgressBar pb, KeyPressed pressed)
    {
    	if(!pressed.keyPressed)
    	{
    		pb.setProgress(0);
    	}
    }
}
