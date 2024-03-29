package address.models;


import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.concurrent.Task;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * 
 * @author Kacper Ratajczak 
 * 
 * 
 * 
 */

public class Slider extends Task {
    private Stage primaryStage;
    private Timeline timeline;
    private double width;
    private boolean direction;
    private double pw;


    public Slider(Stage primaryStage, boolean direction) {
        this.primaryStage = primaryStage;
        this.direction = direction;
        this.pw = 625;
        if(direction) {
            this.width = 0;
        }
        else {
            this.width = 200;
        }

        timeline = new Timeline(new KeyFrame(
                Duration.millis(15),
                event-> {
                    if(direction)
                        slideOut();
                    else slideIn();
                }
        ));
    }



    private void slideOut() {
        if(width < 200) {
            width += 20;
            primaryStage.setWidth(pw + width);
        }
        else timeline.stop();
    }

    private void slideIn() {
        if(width > 0) {
            width -= 20;
            primaryStage.setWidth(pw + width);
        }
        else timeline.stop();
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public Timeline getTimeline() {
        return timeline;
    }

    public double getWidth() {
        return width;
    }

    public boolean isDirection() {
        return direction;
    }

    public double getPw() {
        return pw;
    }

    @Override
    protected Object call() throws Exception {
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
        return null;
    }
}
