package com.example.pianoproject.Controllers;

import com.example.pianoproject.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class PianoController {
    @FXML
    private ChoiceBox<String> instrumentChoice;

    @FXML
    private ToggleButton sustainPedal;

    @FXML
    private ScrollPane scrollBox;

    @FXML
    private HBox keyboardBox;

    @FXML
    private Slider slider;

    @FXML
    private ToggleButton recordButton;

    private final Keyboard keyboard = new Keyboard();
    private boolean isMousePressed = false;
    private final Set<Integer> playedKeys = new HashSet<>();
    private final Set<Integer> playingKeys = new HashSet<>(); // only for stopping sounds when sustain turned off
    private Recorder recorder = new Recorder();
    private RecordingManager recordingManager = new RecordingManager();

    private void keyPressed(int index, Button key) {
        isMousePressed = true;
        keyboard.getKey(index).startKey();
        if (keyboard.getKey(index).isWhite()) {
            key.setStyle("-fx-background-color: #c3c3c3; -fx-border-color: black;");
        } else {
            key.setStyle("-fx-background-color: #3e3e3e;");
        }

        if(recorder.isRecording()) {
            recorder.startKey(keyboard.getKey(index));
        }
    }

    private void keyReleased(int index, Button key) {
        isMousePressed = false;
        keyboard.getKey(index).stopKey();
        if (keyboard.getKey(index).isWhite()) {
            key.setStyle("-fx-background-color: white; -fx-border-color: black;");
        } else {
            key.setStyle("-fx-background-color: black;");
        }

        if(recorder.isRecording()) {
            recorder.stopKey(keyboard.getKey(index));
        }
    }

    public void setKeyEvents(int index, Button key) {
        key.setOnMousePressed(_ -> {
            keyPressed(index, key);
            playedKeys.add(index);
            playingKeys.add(index);
        });
        key.setOnDragDetected(_ -> keyboardBox.startFullDrag());
        key.setOnMouseDragEntered(_ -> {
            if (isMousePressed && !playedKeys.contains(index)) {
                keyPressed(index, key);
                playedKeys.clear();
                playedKeys.add(index);
                playingKeys.add(index);
            }
        });
        key.setOnMouseDragExited(_ -> {
            if (isMousePressed) {
                keyReleased(index, key);
                isMousePressed = true;
            }
        });
        key.setOnMouseDragReleased(_ -> {
            keyReleased(index, key);
            playedKeys.clear();
        });
        key.setOnMouseReleased(_ -> {
            keyReleased(index, key);
            playedKeys.clear();
        });
    }

    public void initialize() {
        for(int i = 0; i < keyboard.getNrOfKeys(); i++) {
            if(keyboard.getKey(i).isWhite()) {
                Button whiteKey = new Button();
                whiteKey.setId("key-" + i);
                whiteKey.setPrefSize(80, 300);
                whiteKey.setMinSize(80, 300);
                whiteKey.setStyle("-fx-background-color: white; -fx-border-color: black;");
                setKeyEvents(i, whiteKey);
                if(keyboard.getKey(i - 1) != null && keyboard.getKey(i - 1).isBlack()) {
                    StackPane stackPane = new StackPane();
                    stackPane.setAlignment(Pos.TOP_CENTER);
                    stackPane.getChildren().add(whiteKey);

                    Button blackKey = new Button();
                    blackKey.setId("key-" + (i - 1));
                    blackKey.setPrefSize(60, 180);
                    blackKey.setStyle("-fx-background-color: black;");
                    blackKey.setTranslateX(-40);
                    setKeyEvents(i - 1, blackKey);

                    stackPane.getChildren().add(blackKey);

                    keyboardBox.getChildren().add(stackPane);
                } else {
                    keyboardBox.getChildren().add(whiteKey);
                }
            }
        }
        scrollBox.setHvalue(0.5);

        instrumentChoice.getItems().addAll("Piano", "Violin", "Guitar", "Trumpet");
        instrumentChoice.getSelectionModel().select(0);
        instrumentChoice.setOnAction(_ -> {
            String s = instrumentChoice.getSelectionModel().getSelectedItem();
            keyboard.setInstrument(MusicalInstrument.nameToInt(s));
        });
    }

    public void sliderController() {
        keyboard.setVolume((int)slider.getValue());
    }

    public void sustainController() {
        if(sustainPedal.isSelected()) {
            sustainPedal.setText("OFF");
            if(recorder.isRecording()) {
                recorder.startSustain();
            }
        } else {
            sustainPedal.setText("ON");
            if(recorder.isRecording()) {
                recorder.stopSustain();
            }
        }

        keyboard.setSustain(sustainPedal.isSelected());
        if(!sustainPedal.isSelected()) {
            playingKeys.forEach(index -> keyboard.getKey(index).stopSustain());
            playingKeys.clear();
        }
    }

    private void saveRecording(String name) {
        File file = new File("Recordings\\" + name + ".midi");

        recorder.writeFile(file);
    }

    private boolean isAlphanumeric(String str) {
        for(int i = 0; i < str.length(); i++) {
            if(!Character.isLetter(str.charAt(i)) && !Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    private void showPopUp() {
        Stage popUp = new Stage();
        popUp.initModality(Modality.APPLICATION_MODAL);
        popUp.setTitle("Your recording is ready!");

        TextField nameField = new TextField("");

        Button discardButton = new Button("Discard");
        discardButton.setOnMouseClicked(_ -> {
            recorder.stopPlayRecording();
            popUp.close();
        });
        Button saveButton = new Button("Save");
        saveButton.setOnMouseClicked(_ -> {
            recorder.stopPlayRecording();
            if(nameField.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Please enter a name for the recording!");
                alert.showAndWait();
            } else if(nameField.getText().length() > 20) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Please enter a name with maximum 20 characters!");
                alert.showAndWait();
            } else if(!isAlphanumeric(nameField.getText())) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Please enter a valid name (only alphanumeric characters) for the recording!");
                alert.showAndWait();
            } else if(!recordingManager.nameIsOk(nameField.getText())) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Name is already taken!");
                alert.showAndWait();
            } else {
                saveRecording(nameField.getText());
                popUp.close();
            }
        });
        Button playButton = new Button("Play");
        playButton.setOnMouseClicked(_ -> recorder.playRecording());

        HBox hBox = new HBox(10, playButton, discardButton, saveButton);
        hBox.setAlignment(Pos.CENTER);

        HBox hBoxName = new HBox(10, new Label("Name:"), nameField);
        hBoxName.setAlignment(Pos.CENTER);

        VBox vBox = new VBox(10, hBoxName, hBox);
        vBox.setStyle("-fx-padding: 20;");
        Scene scene = new Scene(vBox, 300, 100);

        popUp.setResizable(false);
        popUp.setScene(scene);
        popUp.showAndWait();
    }

    public void recordController() {
        if (recordButton.isSelected()) {
            instrumentChoice.setDisable(true);
            recordButton.setText("Stop");
            recorder = new Recorder();
            recorder.startRecording(keyboard.getInstrument());

            if(sustainPedal.isSelected()) {
                sustainPedal.fire();
                sustainController();
            }
        } else {
            instrumentChoice.setDisable(false);
            recordButton.setText("Start");
            recorder.stopRecording();
            recordingManager = new RecordingManager();
            showPopUp();
        }
    }

    public void showRecordingList() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("recordings-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 280, 400);
            Stage stage = new Stage();
            stage.setTitle("Recordings");
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}
