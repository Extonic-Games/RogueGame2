package me.extain.game.gameObject.Player;

public class PlayerStats {

    private float xp;
    private float xpNeeded;
    private int level, lastLevel;
    private int attack;

    public PlayerStats(float xp, int level, int attack) {
        this.xp = xp;
        this.level = level;
        this.attack = attack;

        this.xpNeeded = this.level * 1000;
    }

    public PlayerStats() {
        this(0, 1, 1);

        this.xpNeeded = this.level * 1000;
    }

    public void calculateLevel() {
        if (this.xp >= this.xpNeeded) {
            this.lastLevel = level;

            this.level +=1;

            this.xp = 0;

            this.xpNeeded = this.level * 1000;
        }
    }


    public float getXp() {
        return xp;
    }

    public int getAttack() {
        return attack;
    }

    public int getLevel() {
        return level;
    }

    public int getLastLevel() {
        return lastLevel;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public void setXp(float xp) {
        this.xp = xp;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
