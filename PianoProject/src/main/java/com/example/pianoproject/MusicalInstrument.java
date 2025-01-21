package com.example.pianoproject;

public class MusicalInstrument {
    public static int nameToInt(String n){
        return switch (n) {
            case "Guitar" -> 25;
            case "Violin" -> 40;
            case "Trumpet" -> 56;
            default -> 0; // Piano
        };
    }
}
