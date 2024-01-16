package com.diary.fitness_diary;

public class ExListIntentVO {
    int id;
    String name;
    boolean star;

    public ExListIntentVO(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public boolean isStar() {
        return star;
    }

    public void setStar(boolean star) {
        this.star = star;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
