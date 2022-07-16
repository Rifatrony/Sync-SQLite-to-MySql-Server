package com.binaryit.syncusingretrofit;

public class Model {

    String id, name, number, age;

    public Model() {
    }

    public Model(String id, String name, String number, String age) {
        this.id = id;
        this.name = name;
        this.number = number;
        this.age = age;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}
