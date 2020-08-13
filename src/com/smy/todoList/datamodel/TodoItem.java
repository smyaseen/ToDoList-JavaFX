package com.smy.todoList.datamodel;

import java.time.LocalDate;

public class TodoItem {

    private String shortDescription;
    private String longDescription;
    private LocalDate dueDate;

    public TodoItem(String shortDescription, String longDescription, LocalDate dueDate) {
        this.shortDescription = shortDescription;
        this.longDescription = longDescription;
        this.dueDate = dueDate;
    }

    @Override
    public String toString() {
        return shortDescription;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
}
