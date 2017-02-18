package controller;

import application.KeyPressed;
import javafx.scene.canvas.GraphicsContext;
import model.BasketGoalPosts;
import model.Basketball;
import model.BasketballCourt;
import view.Renderer;

public class BallController {
	private Basketball basketball;
	private BasketballCourt court;
	private BasketGoalPosts post;
	private Renderer render;

    private double g = 4;
    private double deltaT = 0.25;

	public BallController() {
	    basketball = new Basketball(40, 300, 30);
		court = new BasketballCourt(0, 450);
		post = new BasketGoalPosts();

		render = new Renderer(basketball, court, post);
	}

	/** this will render the whole world **/
	public void render(GraphicsContext gc) {
		render.accept(gc);
	}

	public void update(){

	    double deltaS = basketball.getVelocityY() * deltaT + g * deltaT * deltaT / 2;
		basketball.setCenter(basketball.getX() + basketball.getVelocityX() * deltaT, basketball.getY() - deltaS);
        basketball.setVelocity(basketball.getVelocityX(), basketball.getVelocityY() - g * deltaT);
	}

	public void hit(KeyPressed pressed)
	{
		if(basketball.containsPoint(basketball.getX(), 450) || basketball.containsPoint(768, basketball.getY())
				|| (basketball.getX() > 660 && basketball.getY() > 180))
		{
			basketball.setVelocity(0, 0);
			basketball.setCenter(40, 300);
			pressed.setKeyPressed(false);
		}
		if(basketball.getX() > 550 && basketball.getX() < 570 && basketball.getY() > 150 && basketball.getY() < 165)
		{
            basketball.setVelocity(4, 20);
		}
		if((basketball.getX() >= 570 && basketball.getX() <= 630) && (basketball.getY() >= 178 && basketball.getY() <= 184))
		{
            //add points
			render.i++;
		}
		if(basketball.getX() > 630 && basketball.getX() < 640 && basketball.getY() > 150 && basketball.getY() < 170)
		{
			basketball.setVelocity(-4, 20);
		}
		if(basketball.getX() >= 640 && basketball.getX() <= 660 && basketball.getY() >= 70 && basketball.getY() <= 250)
		{
            basketball.setVelocity(-15, basketball.getVelocityY());;
		}
    }

	public Basketball getBasketball() {
		return basketball;
	}

	public BasketballCourt getCourt() {
		return court;
	}

	public BasketGoalPosts getPost() {
		return post;
	}

	public Renderer getRender() {
		return render;
	}



}
