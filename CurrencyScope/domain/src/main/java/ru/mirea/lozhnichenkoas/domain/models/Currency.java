package ru.mirea.lozhnichenkoas.domain.models;

import java.io.Serializable;

public class Currency implements Serializable {
    private String charCode;
    private String name;
    private int nominal;
    private double value;
    private double previous;
    private int iconId;

    public Currency(String charCode, String name, int nominal, double value, double previous) {
        this.charCode = charCode;
        this.name = name;
        this.nominal = nominal;
        this.value = value;
        this.previous = previous;
    }

    public String getCharCode(){
        return charCode;
    }

    public String getName(){
        return name;
    }

    public int getNominal(){
        return nominal;
    }

    public double getValue(){
        return value;
    }

    public double getPrevious(){
        return previous;
    }

    public int getIconId(){
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }
}
