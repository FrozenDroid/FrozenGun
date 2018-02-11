package com.frozendroid.frozengun.models;

import com.xxmicloxx.NoteBlockAPI.NBSDecoder;
import com.xxmicloxx.NoteBlockAPI.RadioSongPlayer;
import com.xxmicloxx.NoteBlockAPI.Song;

import java.io.File;

public class NoteBlockSound extends Sound {

    private RadioSongPlayer player;

    NoteBlockSound(File file, SoundType type) {
        super(file, type);
    }

    @Override
    public void load() {
        Song song = NBSDecoder.parse(this.file);
        player = new RadioSongPlayer(song);
        player.setAutoDestroy(false);
    }

    @Override
    void play() {
        this.listeners.forEach(player::addPlayer);
        this.player.setPlaying(true);
    }

    @Override
    void stop() {
        this.player.setPlaying(false);
    }

}
