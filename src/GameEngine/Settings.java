package GameEngine;

/**
 * Created by giogio on 19/06/16.
 */
public class Settings {
    int nHuman;
    int nBot;
    int mapLevel;
    int round;

    int phase;

    public Settings(){
         nHuman = 1;
         nBot = 0;
        mapLevel = 1;
         round =30;

         phase = 2;
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

    public int getmapLevel() {
        return mapLevel;
    }

    public void setmapLevel(int mapLevel) {
        this.mapLevel = mapLevel;
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
}
