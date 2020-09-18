import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class App extends Application {

    // Enter number of rows and columns
    GameOfLife game = new GameOfLife(45, 75);
    // Enter cell width in pixels
    final int cellWidth = 20;

    Canvas canvas = new Canvas(game.width * cellWidth, game.height * cellWidth);
    boolean isGameOn = false;
    
    @Override
    public void start(Stage primaryStage){
        GridPane root = new GridPane();
        root.setAlignment(Pos.CENTER);
        root.add(canvas, 1, 1);

        root.setStyle("-fx-background-color: #121212");

        Scene scene = new Scene(root, canvas.getWidth()+50, canvas.getHeight()+ 80);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Game of Life");
        primaryStage.show();
        primaryStage.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);
        });


        AnimationTimer timer = new AnimationTimer(){
            @Override
            public void handle(long arg0) {
                paint();
            }
        };

        Thread gameThread = new Thread(() -> {
            while (true){
                System.out.print(String.format("\r %s...", isGameOn? "Running": "Stopped" ));
                if(isGameOn){
                    game.tick();
                }
            }
        });

        Label statusLbl = new Label("Stopped");

        Button startBtn = new Button("Start");
        startBtn.setOnAction(e -> {
            isGameOn = !isGameOn;
            startBtn.setText(isGameOn? "Stop": "Start");
            statusLbl.setText(isGameOn? "Running": "Stopped");
        });
        
        startBtn.setStyle("-fx-background-color: gray; -fx-text-fill: white");
        statusLbl.setStyle("-fx-text-fill: white");

        BorderPane topPane = new BorderPane();
        topPane.setCenter(startBtn);
        topPane.setRight(statusLbl);

        root.add(topPane, 1, 0);

        // User drawing handler
        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, 
            new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent e) {
                    int x = (int) e.getX(), y = (int) e.getY();
                    boolean state = e.getButton() == MouseButton.PRIMARY;
                    mousePaint(state, x,y);
                }
        });

        timer.start();
        gameThread.start();
    }

    void mousePaint(boolean state, int x, int y){
        int cellX = x / cellWidth;
        int cellY = y / cellWidth;

        try {
            game.setCell(state, cellX, cellY);
            paintCell(cellY, cellX);
        } catch (Exception e) {
            return;
        }
    }

    // paint whole canvas
    void paint(){
        var gc = canvas.getGraphicsContext2D();

        gc.setStroke(Color.GRAY);

        for (int i = 0; i < game.getCells().length; i++) {
        for (int j = 0; j < game.getCells()[i].length; j++) {
            paintCell(i, j);   
        }}
    }

    // paint specific cell
    void paintCell(int i, int j){
        var gc = canvas.getGraphicsContext2D();
        int startX = j * cellWidth;
        int startY = i * cellWidth;

        gc.strokeRect(startX, startY, cellWidth, cellWidth);

        if(game.getCells()[i][j]){
            gc.setFill(Color.LIGHTGRAY);
            gc.fillRect(startX, startY, cellWidth, cellWidth);
        }else{
            gc.setFill(Color.web("#202020"));
            gc.fillRect(startX, startY, cellWidth, cellWidth);
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
