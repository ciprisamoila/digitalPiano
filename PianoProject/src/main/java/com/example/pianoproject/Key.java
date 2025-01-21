package com.example.pianoproject;

import javax.sound.midi.*;

public class Key {
    private final int pitch;
    private Receiver receiver;
    private ShortMessage noteOnMessage;
    private ShortMessage noteOffMessage;
    private ShortMessage programChangeMessage;
    private ShortMessage controlChangeMessage;
    private final KeyColor color;
    private int volume;
    private int instrument;
    private boolean sustainOn;
    private final Keyboard keyboard;

    public Key(int pitch, KeyColor color, int instrument, int volume, boolean sustainOn, Keyboard keyboard) {
        this.pitch = pitch;
        this.color = color;
        this.instrument = instrument;
        this.volume = volume;
        this.sustainOn = sustainOn;
        this.keyboard = keyboard;

        try {
            receiver = MidiSystem.getReceiver();
            noteOnMessage = new ShortMessage();
            noteOffMessage = new ShortMessage();
            programChangeMessage = new ShortMessage();
            controlChangeMessage = new ShortMessage();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public int getPitch() {
        return pitch;
    }

    public int getVolume() {
        return volume;
    }

    public boolean isWhite() {
        return color == KeyColor.WHITE;
    }

    public boolean isBlack() {
        return !isWhite();
    }

    public void updateKey() {
        volume = keyboard.getVolume();
        if(instrument != keyboard.getInstrument()) {
            instrument = keyboard.getInstrument();
            try {
                programChangeMessage.setMessage(ShortMessage.PROGRAM_CHANGE, 0, instrument, volume);
                receiver.send(programChangeMessage, -1);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        if(sustainOn != keyboard.isSustainOn()) {
            sustainOn = keyboard.isSustainOn();
            try {
                controlChangeMessage.setMessage(ShortMessage.CONTROL_CHANGE, 0, 64, sustainOn ? 127 : 0);
                receiver.send(controlChangeMessage, -1);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void startKey() {
        updateKey();
        try {
            noteOnMessage.setMessage(ShortMessage.NOTE_ON, 0, pitch, volume);
            receiver.send(noteOnMessage, -1);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void stopKey() {
        try {
            noteOffMessage.setMessage(ShortMessage.NOTE_OFF, 0, pitch, volume);
            receiver.send(noteOffMessage, -1);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static int indexToPitch(int index) {
        return 21 + index;
    }

    public void stopSustain() {
        try {
            controlChangeMessage.setMessage(ShortMessage.CONTROL_CHANGE, 0, 64, 0);
            receiver.send(controlChangeMessage, -1);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void setNoteOnMessage(ShortMessage noteOnMessage) {
        this.noteOnMessage = noteOnMessage;
    }

    public void setNoteOffMessage(ShortMessage noteOffMessage) {
        this.noteOffMessage = noteOffMessage;
    }

    public void setProgramChangeMessage(ShortMessage programChangeMessage) {
        this.programChangeMessage = programChangeMessage;
    }

    public void setControlChangeMessage(ShortMessage controlChangeMessage) {
        this.controlChangeMessage = controlChangeMessage;
    }
}
