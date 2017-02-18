package model;

public class BasketballCourt {
  private double positionX;
  private double positionY;
  private double width;
  private double height;

  public BasketballCourt(double positionX, double positionY)
  {
	  this.positionX = positionX;
	  this.positionY = positionY;
	  width = 0;
	  height = 0;
  }

  public double getPositionX() {
	  return positionX;
  }

  public void setPositionX(double positionX) {
	  this.positionX = positionX;
  }

  public double getPositionY() {
	  return positionY;
  }

  public void setPositionY(double positionY) {
	  this.positionY = positionY;
  }

  @Override
  public String toString() {
	  return "BasketballCourt [positionX=" + positionX + ", positionY=" + positionY + ", width="
			  + width + ", height=" + height + "]";
  }
}
