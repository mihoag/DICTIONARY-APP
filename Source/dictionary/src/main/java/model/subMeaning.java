package model;

import java.io.Serializable;

public class subMeaning implements Serializable{
	    private String subMeaning;
	    private String definition;
	 
	    public subMeaning(String subMeaning, String definition) {
	        this.subMeaning = subMeaning;
	        this.definition = definition;
	    }

	    public String getSubMeaning() {
	        return subMeaning;
	    }

	    public String getDefinition() {
	        return definition;
	    }
}
 