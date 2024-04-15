package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class meaning implements Serializable {
	 private String meaning;
	 private List<subMeaning> subMeanings;
	 
	 public meaning(String meaning) {
	        this.meaning = meaning;
	        subMeanings = new ArrayList<>();
	    }

	    public void addSubMeaning(subMeaning subMeaning) {
	        subMeanings.add(subMeaning);
	    } 

	    public List<subMeaning> getSubMeanings() {
	        return subMeanings;
	    }
	    public String getMeaning() {
	        return meaning;
	    }

	    public void setMeaning(String meaning) {
	        this.meaning = meaning;
	    }
}
