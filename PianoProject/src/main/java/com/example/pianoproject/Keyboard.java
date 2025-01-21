package com.example.pianoproject;

public class Keyboard {
    private final Key[] keys = new Key[88];
    private int instrument;
    private int volume;
    private boolean sustainOn;
    private final int nrOfKeys;

    public Keyboard() {
        sustainOn = false;
        volume = 100;
        instrument = 0;
        nrOfKeys = 88;

        for (int i = 0; i < keys.length; i++) {
            if (i % 12 == 1 || i % 12 == 4 || i % 12 == 6 || i % 12 == 9 || i % 12 == 11) {
                keys[i] = new Key(Key.indexToPitch(i), KeyColor.BLACK, instrument, volume, sustainOn, this);
            } else {
                keys[i] = new Key(Key.indexToPitch(i), KeyColor.WHITE, instrument, volume, sustainOn, this);
            }
        }
    }

    public int getVolume() {
        return volume;
    }

    public int getNrOfKeys() {
        return nrOfKeys;
    }

    public int getInstrument() {
        return instrument;
    }

    public boolean isSustainOn() {
        return sustainOn;
    }

    public Key getKey(int index) {
        if(index < 0 || index >= keys.length)
            return null;
        return keys[index];
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public void setInstrument(int instrument) {
        this.instrument = instrument;
    }

    public void setSustain(boolean sustain) {
        this.sustainOn = sustain;
    }
}
