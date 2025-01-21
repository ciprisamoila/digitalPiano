package com.example.pianoproject.Controllers;

import com.example.pianoproject.Recording;
import com.example.pianoproject.RecordingManager;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class RecordingsController {
    @FXML
    public ScrollPane recordingScroll;

    RecordingManager recordingManager = new RecordingManager();

    public void initialize() {
        if (recordingManager.recordings.isEmpty() || recordingManager.nrDeleted() == recordingManager.recordings.size()){
            Label label = new Label("No recordings found");
            label.setStyle("-fx-font-weight: bold");
            HBox hbox = new HBox(label);
            hbox.setAlignment(Pos.CENTER);
            hbox.setStyle("-fx-padding: 20;");
            hbox.setMinWidth(265);
            recordingScroll.setContent(hbox);
        } else {
            FlowPane flowPane = new FlowPane();
            for (Recording recording : recordingManager.recordings) {
                if(recording.isToBeDeleted()) continue;

                Button playButton = new Button("Play");
                playButton.setOnAction(_ -> recording.playRecording());
                Button stopButton = new Button("Stop");
                stopButton.setOnAction(_ -> recording.stopRecording());
                Button deleteButton = new Button("Delete");
                deleteButton.setOnAction(_ -> {
                   recording.delete();
                   initialize();
                });
                HBox hbox = new HBox(10, playButton, stopButton, deleteButton);
                hbox.setAlignment(Pos.CENTER);

                VBox vbox = new VBox(10, new Label(recording.getName()), hbox);
                vbox.setAlignment(Pos.CENTER);

                vbox.setMinWidth(265);
                vbox.setStyle("-fx-padding: 20;-fx-border-color: transparent;");
                vbox.setOnMouseEntered(_ -> vbox.setStyle("-fx-padding: 20;-fx-border-color: black;"));
                vbox.setOnMouseExited(_ -> vbox.setStyle("-fx-padding: 20;-fx-border-color: transparent;"));


                flowPane.getChildren().add(vbox);
                recordingScroll.setContent(flowPane);
            }
        }
    }

    public void onDiscardButtonPress() {
        recordingManager.markNotToBeDeleted();

        recordingScroll.getScene().getWindow().hide();
    }

    public void onApplyButtonPress() {
        recordingManager.deleteRecordings();

        recordingScroll.getScene().getWindow().hide();
    }
}
