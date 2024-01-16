package com.diary.fitness_diary.home;

import java.util.List;

public class MainItem {
    String routineName;
    List<SubItem> subItemList;

    public MainItem(String routineName, List<SubItem> subItemList) {
        this.routineName = routineName;
        this.subItemList = subItemList;
    }

    public String getRoutineName() {
        return routineName;
    }

    public void setRoutineName(String routineName) {
        this.routineName = routineName;
    }

    public List<SubItem> getSubItemList() {
        return subItemList;
    }

    public void setSubItemList(List<SubItem> subItemList) {
        this.subItemList = subItemList;
    }
}
