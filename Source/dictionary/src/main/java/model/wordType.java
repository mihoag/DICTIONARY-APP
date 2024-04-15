package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class wordType implements Serializable {
    private String type;
    private List<meaning> meanings;
    
    public wordType(String type) {
        this.type = type;
        meanings = new ArrayList<>();
    }

    public void addMeaning(meaning meaning) {
        meanings.add(meaning);
    }

    public List<meaning> getMeanings() {
        return meanings;
    }

    public String getType() {
        return type; 
    }
}
