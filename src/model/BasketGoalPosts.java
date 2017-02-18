package model;

public class BasketGoalPosts{
	private double positionX;
	private double positionY;

	public BasketGoalPosts()
	{
		positionX = 0;
		positionY = 0;
	}

	public void setPosition(double x, double y)
	{
		positionX = x;
		positionY = y;
	}

	public double getPositionX() {
		return positionX;
	}

	public double getPositionY() {
		return positionY;
	}

	@Override
	public String toString() {
		return "BasketGoalPosts [positionX=" + positionX + ", positionY=" + positionY + "]";
	}
}
