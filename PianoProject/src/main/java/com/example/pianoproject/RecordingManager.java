package com.example.pianoproject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RecordingManager {
    public List<Recording> recordings = new ArrayList<>();
    public RecordingManager() {
        File dir = new File("Recordings");
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                recordings.add(new Recording(file));
            }
        }
    }

    public int nrDeleted() {
        return (int) recordings.stream().filter(Recording::isToBeDeleted).count();
    }

    public void deleteRecordings() {
        File dir = new File("Recordings");
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if(getRecording(file.getName().substring(0, file.getName().lastIndexOf("."))).isToBeDeleted()) {
                    if(!file.delete()) {
                        System.out.println("Error deleting recording: " + file.getName());
                    }
                }
            }
        }

        recordings.removeIf(Recording::isToBeDeleted);
    }

    public Recording getRecording(String name) {
        for (Recording recording : recordings) {
            if (recording.getName().equals(name)) {
                return recording;
            }
        }
        return null;
    }

    public boolean nameIsOk(String name) {
        return getRecording(name) == null;
    }

    public void markNotToBeDeleted() {
        for (Recording recording : recordings) {
            recording.notDelete();
        }
    }
}
