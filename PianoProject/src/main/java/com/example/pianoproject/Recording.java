package com.example.pianoproject;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import java.io.File;

public class Recording {
    private final String name;
    private boolean toBeDeleted;
    Sequencer sequencer;
    Sequence sequence;
    public Recording(File file) {
        name = file.getName().substring(0, file.getName().indexOf("."));
        toBeDeleted = false;
        try {
            sequence = MidiSystem.getSequence(file);
            sequencer = MidiSystem.getSequencer();
            sequencer.open();
            sequencer.setSequence(sequence);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void playRecording() {
        if(sequencer.isRunning()) {
            sequencer.stop();
        }
        sequencer.setTickPosition(0);
        sequencer.start();
    }

    public void pauseRecording() {
        if(sequencer.isRunning()) {
            sequencer.stop();
        }
    }

    public void stopRecording() {
        pauseRecording();
        sequencer.setTickPosition(0);
    }

    public void delete() {
        toBeDeleted = true;
    }

    public void notDelete() {
        toBeDeleted = false;
    }

    public boolean isToBeDeleted() {
        return toBeDeleted;
    }

    public String getName() {
        return name;
    }
}
