package com.vrumale.simpletodo;

/**
 * Created by vrumale on 1/13/2015.
 */
public class TodoItem {
    private int id;
    private String body;
    private int priority;

    public TodoItem(String body, int priority) {
        super();
        this.body = body;
        this.priority = priority;
    }
    @Override
    public String toString(){
       //return String.format("%d: %s", id, body);
        return body;
    }
    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
