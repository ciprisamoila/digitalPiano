package com.example.pianoproject;

import javax.sound.midi.*;
import java.io.File;

public class Recorder {
    private boolean isRecording;
    private long startTime;
    private Sequencer sequencer;
    private Sequence sequence;
    private Track track;

    private long getTick() {
        return (long) ((System.currentTimeMillis() - startTime) * 0.12);
    }

    public boolean isRecording() {
        return isRecording;
    }

    public void startRecording(int instrument) {
        isRecording = true;
        try {
            sequencer = MidiSystem.getSequencer();
            sequencer.open();

            sequence = new Sequence(Sequence.SMPTE_30, 4);
            track = sequence.createTrack();

            startTime = System.currentTimeMillis();

            ShortMessage programChange = new ShortMessage(ShortMessage.PROGRAM_CHANGE, instrument, 0);
            MidiEvent event = new MidiEvent(programChange, 0);
            track.add(event);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void startKey(Key key) {
        try {
            ShortMessage noteOnMessage = new ShortMessage();
            noteOnMessage.setMessage(ShortMessage.NOTE_ON, 0, key.getPitch(), key.getVolume());
            MidiEvent event = new MidiEvent(noteOnMessage, getTick());
            track.add(event);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void stopKey(Key key) {
        try {
            ShortMessage noteOffMessage = new ShortMessage();
            noteOffMessage.setMessage(ShortMessage.NOTE_OFF, 0, key.getPitch(), key.getVolume());
            MidiEvent event = new MidiEvent(noteOffMessage, getTick());
            track.add(event);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void startSustain() {
        try {
            ShortMessage controlChangeMessage = new ShortMessage();
            controlChangeMessage.setMessage(ShortMessage.CONTROL_CHANGE, 0, 64, 127);
            MidiEvent event = new MidiEvent(controlChangeMessage, getTick());
            track.add(event);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void stopSustain() {
        try {
            ShortMessage controlChangeMessage = new ShortMessage();
            controlChangeMessage.setMessage(ShortMessage.CONTROL_CHANGE, 0, 64, 0);
            MidiEvent event = new MidiEvent(controlChangeMessage, getTick());
            track.add(event);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void stopRecording() {
        isRecording = false;
        try {
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

    public void stopPlayRecording() {
        sequencer.stop();
    }

    public void writeFile(File file) {
        try {
            MidiSystem.write(sequence, 1, file);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
