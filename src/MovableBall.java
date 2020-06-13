import javafx.animation.KeyFrame; // Defines target values at a specified point in time for a set of variables that are interpolated along a Timeline .
import javafx.animation.Timeline; // A Timeline can be used to define a free form animation of any WritableValue.
import javafx.scene.layout.Pane; // Base class for layout panes which need to expose the children list as public so that users of the subclass can freely add/remove children.
import javafx.scene.paint.Color; // The Color class is used to encapsulate colors in the default sRGB color space.
import javafx.scene.shape.Circle; // The Circle class creates a new circle with the specified radius and center location measured in pixels.
import javafx.util.Duration; // A class that defines a duration of time.


// Inherited class from the Pane class, which acts as a wrapper for the circle ( Ball ).
public class MovableBall extends Pane {
    // Defining the properties of the MovableBall Class, such as timeline property, change value in both x and y directions and the base circle object that acts like a ball.
    private double changeInX = Double.POSITIVE_INFINITY, changeInY = Double.POSITIVE_INFINITY;
    private Circle circleBase;
    private Timeline animationTimeLine;
    // A constructor which take arguments related to the circle like the point of the center, radius, and color
    // it Inialize the circle with the passed values and adds it to the pane, also invoke the method which is responsible for creating the timeline for animation.
    public MovableBall( double centerX, double centerY, double radius, Color color){
        circleBase = new Circle(centerX,centerY , radius, color);
        getChildren().add(circleBase);
        createTimeLine();
    }

    // A Method For Changing the color of the circle based on the passed color object.
    public void changeBallColor(Color color){
        circleBase.setFill(color);
    }

    // A Method which is responsible for creating the timeline for animation
    // Setting the frame to 1 second and invoking a method every defined time to animate the ball.
    private void createTimeLine(){
        animationTimeLine = new Timeline(new KeyFrame(Duration.seconds(1), e-> animateBallPerTime()));
        animationTimeLine.setCycleCount(Timeline.INDEFINITE);
    }

    /*
        This is the main method for animating the circle and checking collisions with border.
     */
    private void animateBallPerTime(){
        // Checking the collision with the left or the right borders of the screen, if so it reverse the direction of movement
        if (circleBase.getCenterX()  < circleBase.getRadius() || circleBase.getCenterX() > getWidth() - circleBase.getRadius()) {
            changeInX *= -1;
        }
        // Checking the collision with the up or the down borders of the screen, if so it reverse the direction of movement
        if (circleBase.getCenterY()  < circleBase.getRadius() ||circleBase.getCenterY()> getHeight() - circleBase.getRadius() ) {
            changeInY *= -1;
        }
        // Sets the new value of the center of the circle based on the change in each x and y directions.
        circleBase.setCenterX(changeInX + circleBase.getCenterX());
        circleBase.setCenterY(changeInY + circleBase.getCenterY());
    }

    // Stopping the timeline which is responsible for stopping the animation.
    public void stop(){animationTimeLine.stop();}
    // Starting the timeline which is responsible for playing the animation.
    public void play(){animationTimeLine.play();}

    /*
        Some methods which is responsible for setting the values of the changeInX and changeInY
        according to the needed direction.
        So, For horizontal direction we have two scenarios, which are left and right
        for left the x values are decreasing and y values are still constant so we put a negative value
        for the x direction, and the right direction is vise verca.
        For the vertical directions, x is constant and y is changing, so y changes down with increase
        so it needs a positive value, and decreases up so it needs a negative value.
     */
    public void moveHorizontalToRight(){
        this.changeInX = 50 ;
        this.changeInY = 0;
    }
    public void moveVerticalToUp(){
        this.changeInX = 0;
        this.changeInY = -50;
    }
    public void moveVerticalToDown(){
        this.changeInX = 0;
        this.changeInY = 50;
    }
    public void moveHorizontalToLeft(){
        this.changeInX = -50;
        this.changeInY = 0;
    }

    // Some Methods checking the current direction of the ball.
    public boolean isLeft(){
        return (this.changeInX == -50 && this.changeInY == 0);
    }
    public boolean isRight(){
        return (this.changeInX == 50 && this.changeInY == 0);
    }
    public boolean isUp(){
        return (this.changeInX == 0 && this.changeInY == -50);
    }
    public boolean isDown(){
        return (this.changeInX == 0 && this.changeInY == 50);
    }

    // This Method check for the undirected state of the circle.
    // If the circle is not directed its changeInX and changeInY are infinity values as initial value.
    public boolean notDirected(){
        return (this.changeInX == Double.POSITIVE_INFINITY && this.changeInY == Double.POSITIVE_INFINITY);
    }
}
