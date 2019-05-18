package com.adnd.iomoney.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "accounts", indices = {@Index(value = "name", unique = true)})
public class Account {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name = "";

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
