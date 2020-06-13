import javafx.application.Application; // Application class from which JavaFX applications extend.
import javafx.beans.value.ChangeListener; // A ChangeListener is notified whenever the value of an ObservableValue changes.
import javafx.beans.value.ObservableValue; // An ObservableValue is an entity that wraps a value and allows to observe the value for changes.
import javafx.geometry.Insets; // A set of inside offsets for the 4 side of a rectangular area.
import javafx.geometry.Pos; // A set of values for describing vertical and horizontal positioning and alignment.
import javafx.scene.Scene; // The JavaFX Scene class is the container for all content in a scene graph.
import javafx.scene.control.*; // Base class for all user interface controls.
import javafx.scene.image.Image; // The Image class represents graphical images and is used for loading images.
import javafx.scene.layout.*; // Provides classes to support user interface layout.
import javafx.scene.paint.Color; // The Color class is used to encapsulate colors in the default sRGB color space.
import javafx.scene.text.Font; // The Font class represents fonts, which are used to render text on screen.
import javafx.scene.text.FontWeight; //Specifies different font weights which can be used when searching for a font on the system.
import javafx.stage.Stage; // JavaFX Stage class is the top level JavaFX container.

public class Main extends Application {
    private BorderPane pane;  // Reference Variable to the main Pane in the program which is of type borderpane.
    private HBox hBox; // Reference variable for the hbox which is a container for the buttons, color picker, and radio buttons.
    private Button btnStart, btnStop, btnReverse; // Reference Variables for the start, stop, and reverse buttons respectively.
    private Scene scene; // Reference variable for the main scene in the project.
    private MovableBall movableBall; // Reference variable for the movbleball class, which is the main player in the program.
    private RadioButton rdBtnHorizontal, rdBtnVertical; // Reference variable for the horizontal and vertical radiobuttons which are responsible for the direction.
    private ToggleGroup tg; // A reference variable for the toggle group which ensure one radio button to be selected at a time and the other is not selected.
    private ColorPicker picker; // Reference variable for the color picker, which is responsible for choosing and changing the color of the ball.
    private Image backgroundImage; // Reference variable for the background image of the scene.
    private BackgroundSize backgroundSize; // Reference variable for the background size of the image on the scene

    // Start Method Which is the first method to call in the lifecycle of the JavaFX Program.
    @Override
    public void start(Stage primaryStage){
        initDrawing(); // Invoking the method which is responsible for drawing the UI.
        ballController(); // Invoking the method which is responsible for controlling the buttons and setting the click listeners to them and checking radio buttons selection.
        primaryStage.setTitle("Ball Movement"); // Setting the title of the program.
        primaryStage.setScene(scene); // setting the scene of the stage.
        primaryStage.setResizable(false); // the stage is not resizable, you cannot change the size of the screen dynamically.
        primaryStage.getScene().getStylesheets().add(Main.class.getResource("styles.css").toExternalForm()); // Setting a reference to the stylesheet which is responsible to style some components in the project using JavaFXCSS.
        primaryStage.show(); // Showing the stage on the screen.
    }


    // Main Method which is mandatory to launch the program.
    public static void main(String[] args) {
        launch(args);
    }


    // This Method is responsible for drawing the user interface.
    private void initDrawing(){
        buttonsInit(); // Invoking the method which is responsible for the buttons.
        pane = new BorderPane(); // Initializing the border pane.
        picker = new ColorPicker(Color.BLUE); // Initializing the color picker with blue as a default color.
        picker.getStyleClass().add("color-picker"); // Setting the style of the color picker to a custom style in the style sheet which we linked it in the primary stage.
        // Setting the preferred size (width and height) for the border pane.
        pane.setPrefHeight(600);
        pane.setPrefWidth(600);
        scene = new Scene(pane); // Initializing the scene.
        // Setting the background image of the border_pane.
        backgroundImage = new Image("images/background.png");
        backgroundSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false);
        pane.setBackground(new Background(new BackgroundImage(backgroundImage,
                BackgroundRepeat.ROUND,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                backgroundSize)));

        /*
            Initializing the hbox with inside spacing 10, and padding 10 from the right and left directions, 0 top and  bottom
            Setting the alignment to the center so its children will be centered.
         */
        hBox = new HBox(10);
        hBox.setPadding(new Insets(0, 10, 5 , 10));
        hBox.setAlignment(Pos.CENTER);

        // Initializing the movableball variable with a new instance from the MovaBall class.
        // Which is responsible for the drawing of the ball and animating it on the screen.
        // This will initialize the ball in the center of the pane with a circle radius 20.
        // and a color equals to the color picker default color which is blue.
        movableBall = new MovableBall(pane.getPrefWidth()/2,pane.getPrefHeight()/2 , 20,picker.getValue());

        // Setting the center of the borderPane to the MovaBall and the bottom to the hbox.
        // Border Pane ensures that every component will stick in its defined place.
        pane.setCenter(movableBall);
        pane.setBottom(hBox);

        /*
            Initializing the vertical and horizontal radio buttons, giving them the defined name
            and also some other properties related to the style like preferred width and text color,
            text font and text style.
            Also setting the toggle group which is responsible for controlling which radio button should
            be selected and the other shouldn't.
         */
        rdBtnHorizontal = new RadioButton("Horizontal");
        rdBtnVertical = new RadioButton("Vertical");
        rdBtnHorizontal.setPrefWidth(110);
        rdBtnVertical.setPrefWidth(100);
        tg = new ToggleGroup();
        rdBtnVertical.setToggleGroup(tg);
        rdBtnHorizontal.setToggleGroup(tg);
        rdBtnHorizontal.setTextFill(Color.valueOf("#F54A00"));
        rdBtnVertical.setTextFill(Color.valueOf("#F54A00"));
        rdBtnVertical.setFont(Font.font("System", FontWeight.BOLD, 13.0));
        rdBtnHorizontal.setFont(Font.font("System", FontWeight.BOLD, 13.0));
        rdBtnHorizontal.getStyleClass().add("selected-radio-button");
        rdBtnVertical.getStyleClass().add("selected-radio-button");

        // Adding Start, Stop, Reverse, radio buttons, and the color picker to the hbox as children.
        hBox.getChildren().addAll(btnStart, btnStop, btnReverse, rdBtnVertical, rdBtnHorizontal, picker);
    }

    // This is the main controller for the ball, which defines the click listeners to the different buttons,
    // And ensure the logic of the overall program.
    private void ballController(){
        // This adds a listener to the selections property of the radio button, to get any change in the selection while the program is working.
        rdBtnHorizontal.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
               // While program is working and the horizontal radio button is selected and before that it isn't,
                // Then immediately change the direction of the ball to the right.
                if(t1){
                    movableBall.moveHorizontalToRight();
                }
            }
        });
        // This adds a listener to the selections property of the radio button, to get any change in the selection while the program is working.

        rdBtnVertical.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                // While program is working and the vertical radio button is selected and before that it isn't,
                // Then immediately change the direction of the ball to the up.
                if(t1){
                    movableBall.moveVerticalToUp();
                }
            }
        });
        // This adds a listener to the value property of the color picker, to get any change in the color value while the program is working.
        picker.valueProperty().addListener(new ChangeListener<Color>() {
            @Override
            public void changed(ObservableValue<? extends Color> observableValue, Color color, Color t1) {
                // While program is working and the user changed the color, the ball will be assigned to the new color.
                movableBall.changeBallColor(t1);
            }
        });
        // Setting the click listener to the start button.
        btnStart.setOnAction(e -> {
            /*
                If initially the ball has no direction, which means that there is no selected radio
                buttons ( Vertical Or Horizontal ). This will alert the user for an error.
                else
                The start button will be disabled, the stop button will be enabled instead
                and the ball will start animating.
             */
            if (movableBall.notDirected()){
                Alert a = new Alert(Alert.AlertType.WARNING);
                a.setContentText("Select a direction");
                a.setTitle("Error in direction");
                a.showAndWait();
            } else {
                btnStart.setDisable(true);
                btnStop.setDisable(false);
                movableBall.play();
            }

        });

        // Setting the click listener to the stop button.
        btnStop.setOnAction(e -> {
            // This will make the start button to be enabled again, the stop button will be disabled , and the ball will stop animating.
            btnStart.setDisable(false);
            btnStop.setDisable(true);
            movableBall.stop();
        });

        // Setting the click listener to the reverse button.
        btnReverse.setOnAction(e -> {
            // Checking the direction of the ball, and reversing it depending on its initial direction.
            if (movableBall.isLeft())
                movableBall.moveHorizontalToRight();
            else if (movableBall.isRight())
                movableBall.moveHorizontalToLeft();
            else if (movableBall.isDown())
                movableBall.moveVerticalToUp();
            else if (movableBall.isUp())
                movableBall.moveVerticalToDown();
        });

    }

    // This Method Controls Buttons: Start, Stop, and Reverse UI.
    private void buttonsInit(){
        /*
        Initialize Start, Stop, and Reverse Button respectively.
         */
        btnStart = new Button();
        btnStop = new Button();
        btnReverse = new Button();
        /*
            Disable Mnemonic Parsing for the three buttons, which is responsible for shortcut keys.
            Example:
                btn.setText("_Say 'Hello World'");
                btn.setMnemonicParsing(true);
            If we set a click listener to the button it will print hello world on click , also
            on hitting Alt+S from the keyboard.
         */
        btnStart.setMnemonicParsing(false);
        btnReverse.setMnemonicParsing(false);
        btnStop.setMnemonicParsing(false);

        btnStop.setDisable(true); // Sets Stop bottom to disable initially.
        /*
            Setting the preferred size (width and height) attributes to the Stop, Start, and Reverse Button.
         */
        btnStart.setPrefWidth(75);
        btnStart.setPrefHeight(75);
        btnStop.setPrefWidth(75);
        btnStop.setPrefHeight(75);
        btnReverse.setPrefWidth(75);
        btnReverse.setPrefHeight(75);
        /*
            Setting the background Image for the three buttons.
         */
        btnStart.setBackground(backgroundGetter("images/startBtn.png"));
        btnStop.setBackground(backgroundGetter("images/stopBtn.png"));
        btnReverse.setBackground(backgroundGetter("images/reverseBtn.png"));

    }
    // A Method to generate a Background object from path of the image, which
    // is used in setting the background of the three Buttons.
    private Background backgroundGetter(String imagePath){
        return (new Background(new BackgroundImage(new Image(imagePath),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(75, 75, false, false, true, false))));
    }
}
