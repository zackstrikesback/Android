package com.zackstrikesback.poll;

public class DBPoll {
    private int id;
    private String name;
    private String question;
    private String image;
    private int yes;
    private int no;

    public DBPoll(){
    }
    public DBPoll(String name, String question, String image){
        this.name = name;
        this.question = question;
        this.image = image;
        this.yes = 0;
        this.no = 0;
    }

    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id = id;
    }

    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }

    public String getQuestion(){
        return question;
    }
    public void setQuestion(String question){
        this.question = question;
    }

    public String getImage(){
        return image;
    }
    public void setImage(String image){
        this.image = image;
    }

    public int getYes(){
        return yes;
    }
    public void setYes(int yes){
        this.yes = yes;
    }

    public int getNo(){
        return no;
    }
    public void setNo(int no){
        this.no = no;
    }


    @Override
    public String toString() {
        return name;
    }
}