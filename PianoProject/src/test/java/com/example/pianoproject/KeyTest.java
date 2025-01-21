package com.example.pianoproject;

import org.junit.jupiter.api.Test;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.ShortMessage;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class KeyTest {

    Keyboard keyboard = new Keyboard();

    @Test
    void getPitch() {
        Key key = keyboard.getKey(39); // Middle C
        assertEquals(60, key.getPitch());
    }

    @Test
    void getVolume() {
        Key key = keyboard.getKey(39);
        assertEquals(100, key.getVolume());
    }

    @Test
    void getVolumeAfterUpdate() {
        Key key = keyboard.getKey(39);

        keyboard.setVolume(128);
        key.updateKey();
        assertEquals(128, key.getVolume());
    }

    @Test
    void isWhite() {
        Key key = keyboard.getKey(39);
        assertTrue(key.isWhite());
    }

    @Test
    void isBlack() {
        Key key = keyboard.getKey(40);
        assertTrue(key.isBlack());
    }

    @Test
    void indexToPitch() {
        int index = 33;
        int pitch = Key.indexToPitch(index);
        assertEquals(54, pitch);
    }

    @Test
    void startKey() {
        ShortMessage mockShortMessage = mock(ShortMessage.class);

        Key key = keyboard.getKey(39);
        key.setNoteOnMessage(mockShortMessage);

        key.startKey();

        try {
            verify(mockShortMessage).setMessage(ShortMessage.NOTE_ON, 0, 60, 100);
        } catch (InvalidMidiDataException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void startKeyAfterUpdate() {
        ShortMessage mockShortMessage = mock(ShortMessage.class);

        Key key = keyboard.getKey(39);
        key.setNoteOnMessage(mockShortMessage);

        keyboard.setVolume(128);

        key.startKey();

        try {
            verify(mockShortMessage).setMessage(ShortMessage.NOTE_ON, 0, 60, 128);
        } catch (InvalidMidiDataException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void stopKey() {
        ShortMessage mockShortMessage = mock(ShortMessage.class);

        Key key = keyboard.getKey(80);
        key.setNoteOffMessage(mockShortMessage);

        key.stopKey();

        try {
            verify(mockShortMessage).setMessage(ShortMessage.NOTE_OFF, 0, 101, 100);
        } catch (InvalidMidiDataException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void instrumentChange() {
        ShortMessage mockShortMessage = mock(ShortMessage.class);

        Key key = keyboard.getKey(80);
        key.setProgramChangeMessage(mockShortMessage);

        keyboard.setInstrument(MusicalInstrument.nameToInt("Trumpet"));

        key.updateKey();

        try {
            verify(mockShortMessage).setMessage(ShortMessage.PROGRAM_CHANGE, 0, 56, 100);
        } catch (InvalidMidiDataException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void pressSustain() {
        ShortMessage mockShortMessage = mock(ShortMessage.class);

        Key key = keyboard.getKey(80);
        key.setControlChangeMessage(mockShortMessage);

        keyboard.setSustain(true);

        key.updateKey();

        try {
            verify(mockShortMessage).setMessage(ShortMessage.CONTROL_CHANGE, 0, 64, 127);
        } catch (InvalidMidiDataException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void releaseSustain() {
        ShortMessage mockShortMessage = mock(ShortMessage.class);

        Key key = keyboard.getKey(80);
        key.setControlChangeMessage(mockShortMessage);

        keyboard.setSustain(true);
        key.updateKey();

        keyboard.setSustain(false);
        key.updateKey();

        try {
            verify(mockShortMessage).setMessage(ShortMessage.CONTROL_CHANGE, 0, 64, 0);
        } catch (InvalidMidiDataException e) {
            System.out.println(e.getMessage());
        }
    }
}