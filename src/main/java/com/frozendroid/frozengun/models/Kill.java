package com.frozendroid.frozengun.models;

public class Kill {

    private Long time;
    private MinigamePlayer killed;
    private MinigamePlayer killer;
    private Integer spree = 0;

    public MinigamePlayer getKilled() {
        return killed;
    }

    /**
     * Sets the killed player
     *
     * @param killed
     */
    public void setKilled(MinigamePlayer killed) {
        this.killed = killed;
    }

    /**
     * Gets the time when this kill happened in milliseconds
     *
     * @return
     */
    public Long getTime() {
        return time;
    }

    /**
     * Sets the time when this kill happened in milliseconds
     *
     * @param time
     */
    public void setTime(Long time) {
        this.time = time;
    }


    /**
     * Gets the killing spree count.
     *
     * @return
     */
    public Integer getSpree() {
        return spree;
    }

    /**
     * Sets the killing spree count.
     *
     * @param spree
     */
    public void setSpree(Integer spree) {
        this.spree = spree;
    }

    /**
     * Gets the killer's player object.
     *
     * @return
     */
    public MinigamePlayer getKiller() {
        return killer;
    }

    /**
     * Sets the killer
     *
     * @param killer
     */
    public void setKiller(MinigamePlayer killer) {
        this.killer = killer;
    }
}
