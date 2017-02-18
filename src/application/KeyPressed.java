package application;

public class KeyPressed {
    public boolean keyPressed;
    public int loopcount;

    public KeyPressed()
    {
    	keyPressed = false;
    	loopcount = 0;
    }

	public boolean isKeyPressed() {
		return keyPressed;
	}

	public void setKeyPressed(boolean keyPressed) {
		this.keyPressed = keyPressed;
	}
}
