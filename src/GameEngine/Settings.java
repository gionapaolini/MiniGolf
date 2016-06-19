package GameEngine;

/**
 * Created by giogio on 19/06/16.
 */
public class Settings {
    int nHuman = 1;
    int nBot = 0;
    int lvlBot = 0;
    int round =30;

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
}
