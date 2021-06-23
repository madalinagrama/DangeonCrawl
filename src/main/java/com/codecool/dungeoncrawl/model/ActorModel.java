package com.codecool.dungeoncrawl.model;

import com.codecool.dungeoncrawl.logic.actors.Actor;

public class ActorModel extends BaseModel {

    private String actorType;
    private int hp;
    private int x;
    private int y;
    private int map_id;

    public ActorModel(int id, String playerName, int hp, int damage, int armor, int x, int y) {
        this.actorType = actorType;
        this.x = x;
        this.y = y;
        this.hp = hp;
        this.id = id;
    }

    public ActorModel(Actor actor) {
        this.actorType = actor.getClass().getTypeName();
        this.x = actor.getX();
        this.y = actor.getY();
        this.hp = actor.getHealth();
        this.id = actor.getId();

    }

    public String getActorType() {
        return actorType;
    }

    public void setActorType(String actorType) {
        this.actorType = actorType;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getMap_id() {
        return map_id;
    }

    public void setMap_id(int map_id) {
        this.map_id = map_id;
    }
}
