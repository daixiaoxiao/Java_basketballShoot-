package model;

public class Basketball extends Circle{
    private double centerX;
    private double centerY;
    private double velocityX;
    private double velocityY;
    private double radius;

    public Basketball(double centerX,double centerY,double radius)
    {
        super(centerX, centerY, radius);
        velocityX = 0;
        velocityY = 0;
    }

    public void setVelocity(double x, double y)
    {
        velocityX = x;
        velocityY = y;
    }

    public void addVelocity(double x, double y)
    {
        velocityX += x;
        velocityY += y;
    }

    public double getVelocityX() {
		return velocityX;
	}

	public double getVelocityY() {
		return velocityY;
	}

    public String toString()
    {
        return " Position: [" + centerX + "," + centerY + "]"
        + " Velocity: [" + velocityX + "," + velocityY + "]";
    }
}
