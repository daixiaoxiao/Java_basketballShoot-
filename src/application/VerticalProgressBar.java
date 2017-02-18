package application;

import javafx.scene.control.ProgressBar;
import javafx.scene.layout.StackPane;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

public class VerticalProgressBar {
    private ProgressBar progressBar    = new ProgressBar();

    public VerticalProgressBar(double width, double height) {
        progressBar.setMinSize(StackPane.USE_PREF_SIZE, StackPane.USE_PREF_SIZE);
        progressBar.setPrefSize(height, width);
        progressBar.setMaxSize(StackPane.USE_PREF_SIZE, StackPane.USE_PREF_SIZE);
        progressBar.getTransforms().setAll(
                new Translate(0, height),
                new Rotate(-90, 0, 0)
        );
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }
}
