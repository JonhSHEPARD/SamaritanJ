package io.github.jonhshepard.samaritanj.frames;

/**
 * @author JonhSHEPARD
 */

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SamaritanMainApp extends Application {

    private static SamaritanMainApp instance;

    public static SamaritanMainApp getInstance() {
        if(instance == null) System.exit(1);
        return instance;
    }

    private Stage stage;

    private Label textLbl;
    private Label triangleLbl;
    private Line lineLbl;

    private Thread wait;

    private final double DEFAULT_LINE_SCALE = 7.0D;

    private String title = "Samaritan";
    private List<String> text = Arrays.asList("Welcome", "To", "Samaritan");

    public static void launch(boolean maximized, String title) {
        launch(maximized, title, new ArrayList<String>());
    }

    private static void launch(boolean maximized, String title, List<String> text) {
        if (title.length() < 3) title = "Samaritan";
        if (text == null) text = Arrays.asList("Welcome", "To", "Samaritan");

        JSONObject o = new JSONObject();
        JSONArray a = new JSONArray();
        for (String s : text) a.put(s);
        try {
            o.put("text", a);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Application.launch(SamaritanMainApp.class, title, o.toString(), maximized + "");
    }

    @Override
    public void start(Stage primaryStage) {
        if (instance != null) {
            try {
                instance.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        instance = this;

        boolean maximized = false;
        if (this.getParameters().getRaw().size() == 3) {
            this.title = this.getParameters().getRaw().get(0);
            try {
                JSONArray a = (JSONArray) new JSONObject(this.getParameters().getRaw().get(1)).get("text");
                text = new ArrayList<String>();
                for (int i = 0; i < a.length(); i++) {
                    text.add(a.getString(i));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(this.getParameters().getRaw().get(2).equalsIgnoreCase("true")) maximized = true;
        }
        stage = primaryStage;
        stage.setTitle(this.title);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/samaritan.png")));

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        textLbl = new Label();
        textLbl.setStyle("-fx-font-family: magdacleanmono-bold; -fx-font-size: 30px;");
        grid.add(textLbl, 0, 0);
        GridPane.setHalignment(textLbl, HPos.CENTER);

        lineLbl = new Line(0, 0, 1, 0);
        lineLbl.setScaleX(DEFAULT_LINE_SCALE);
        lineLbl.setTranslateY(17);
        lineLbl.setStrokeWidth(3);
        grid.add(lineLbl, 0, 0);
        GridPane.setHalignment(lineLbl, HPos.CENTER);

        triangleLbl = new Label("▲");
        triangleLbl.setStyle("-fx-font-size: 30px;");
        triangleLbl.setTextFill(Color.RED);
        triangleLbl.setContentDisplay(ContentDisplay.CENTER);
        triangleLbl.setPadding(new Insets(65, 0, 0, 0));
        grid.add(triangleLbl, 0, 0);
        GridPane.setHalignment(triangleLbl, HPos.CENTER);

        Scene scene = (maximized) ? new Scene(grid) : new Scene(grid, 300, 300);
        grid.setStyle("-fx-background-color: radial-gradient(focus-distance 0% , center 50% 50% , radius 55% , #ffffff, #7f7f7f);");

        stage.setScene(scene);

        stage.setMaximized(maximized);

        stage.show();
        startWait();

        if(text.size() > 0) {
            PauseTransition pause = new PauseTransition(Duration.millis(1500));
            pause.setOnFinished(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent event) {
                    executeText(text);
                }
            });
            pause.play();
        }
    }

    private void startWait() {
        wait = new Thread(new Runnable() {
            public void run() {
                while (stage.isShowing() && wait != null && wait.isAlive()) {
                    lineLbl.setStroke(Color.BLACK);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException ignored) {
                    }
                    lineLbl.setStroke(Color.rgb(126, 126, 127));
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException ignored) {
                    }
                }
            }
        });
        wait.start();
    }

    public void executeText(String s) {
        executeText(s, false);
    }

    private void executeText(String s, boolean closeOnFinish) {
        s = s.replace(".", "");
        s = s.replace(",", "");
        executeText(Arrays.asList(s.split(" ")), closeOnFinish);
    }

    private void executeText(List<String> text) {
        executeText(text, false);
    }

    public void executeText(final List<String> text, final boolean closeOnFinish) {
        if (wait != null) {
            if (wait.isAlive()) {
                wait.interrupt();
            }
            wait = null;
        }

        lineLbl.setStroke(Color.BLACK);

        FadeTransition tglF = new FadeTransition(Duration.millis(500), triangleLbl);
        tglF.setFromValue(1);
        tglF.setToValue(0.1);
        tglF.setOnFinished(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                triangleLbl.setVisible(false);
                textLbl.setVisible(true);
                lineLbl.setStroke(Color.BLACK);
            }
        });

        SequentialTransition transition = new SequentialTransition(tglF);

        for (final String s : text) {
            PauseTransition txtT = new PauseTransition(Duration.millis(0));
            txtT.setOnFinished(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent event) {
                    textLbl.setText(s);
                }
            });
            transition.getChildren().add(txtT);

            ScaleTransition lnF = new ScaleTransition(Duration.millis(500), lineLbl);
            lnF.setToX(s.length() * 4);
            transition.getChildren().add(lnF);

            PauseTransition pause = new PauseTransition(Duration.millis(300 + s.length() * 20));
            transition.getChildren().add(pause);
        }

        FadeTransition txtF = new FadeTransition(Duration.millis(500), textLbl);
        txtF.setFromValue(1);
        txtF.setToValue(0.1);
        txtF.setOnFinished(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                triangleLbl.setVisible(true);
                textLbl.setVisible(false);
            }
        });
        transition.getChildren().add(txtF);

        FadeTransition tglF1 = new FadeTransition(Duration.millis(500), triangleLbl);
        tglF1.setFromValue(0.1);
        tglF1.setToValue(1);
        transition.getChildren().add(tglF1);

        ScaleTransition lnF = new ScaleTransition(Duration.millis(500), lineLbl);
        lnF.setToX(DEFAULT_LINE_SCALE);
        transition.getChildren().add(lnF);

        transition.setOnFinished(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                if(closeOnFinish) {
                    stage.close();
                } else {
                    startWait();
                }
            }
        });
        transition.play();
    }
}
