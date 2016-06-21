package GameEngine;

import GolfObjects.PutHole;

/**
 * Created by giogio on 19/06/16.
 */
public class Settings {
    int nHuman;
    int nBot;
    int lvlBot;
    int round;
    int phase;
    int level;
    PutHole putHole;

    public Settings(PutHole putHole){
         nHuman = 1;
         nBot = 0;
         lvlBot = 0;
         round =30;
         level = 1;
         phase = 2;
        this.putHole = putHole;
    }

    public int getnHuman() {
        return nHuman;
    }

    public void setnHuman(int nHuman) {
        this.nHuman = nHuman;
    }

    public int getnBot() {
        return nBot;
    }

    public void setnBot(int nBot) {
        this.nBot = nBot;
    }

    public int getLvlBot() {
        return lvlBot;
    }

    public void setLvlBot(int lvlBot) {
        this.lvlBot = lvlBot;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public int getPhase() {
        return phase;
    }

    public void setPhase(int phase) {
        this.phase = phase;
    }
    public void setLevel(int level) {
        this.level = level;
    }
    public int getLevel() {
        return level;
    }

    public PutHole getPutHole() {
        return putHole;
    }
}
