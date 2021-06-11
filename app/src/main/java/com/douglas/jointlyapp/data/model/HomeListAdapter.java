package com.douglas.jointlyapp.data.model;

/**
 * Assistant entity for home list initiative
 */
public class HomeListAdapter {

    private Initiative initiative;
    private User userOwner;
    private long countUserJoined;

    public HomeListAdapter() {
    }

    public HomeListAdapter(Initiative initiative, User userOwner, long countUserJoined) {
        this.initiative = initiative;
        this.userOwner = userOwner;
        this.countUserJoined = countUserJoined;
    }

    public Initiative getInitiative() {
        return initiative;
    }

    public void setInitiative(Initiative initiative) {
        this.initiative = initiative;
    }

    public User getUserOwner() {
        return userOwner;
    }

    public void setUserOwner(User userOwner) {
        this.userOwner = userOwner;
    }

    public long getCountUserJoined() {
        return countUserJoined;
    }

    public void setCountUserJoined(long countUserJoined) {
        this.countUserJoined = countUserJoined;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HomeListAdapter homeListAdapter = (HomeListAdapter) o;

        return initiative != null ? initiative.equals(homeListAdapter.initiative) : homeListAdapter.initiative == null;
    }

    @Override
    public int hashCode() {
        return initiative != null ? initiative.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "HomeList{" +
                "initiative=" + initiative +
                ", userOwner=" + userOwner +
                ", countUserJoined=" + countUserJoined +
                '}';
    }
}
