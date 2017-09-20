package com.example.user.db;

public class DBTask {
    public long id;
    public String name;
    public String description;

    public DBTask(String name, String description){
        this.name = name;
        this.description = description;
    }

    public long getId(){
        return id;
    }
    public void setId(long id){
        this.id = id;
    }

    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }

    public String getDescription(){
        return description;
    }
    public void setDescription(String description){
        this.description = description;
    }

    @Override
    public String toString() {
        return name;
    }
}