package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class word  implements Serializable{
	private static final long serialVersionUID = 1L;
    private String word;
    private String pronun;
    private String meaning;
    private List<wordType> lsWordTypes;
    
    public word(String word)
    {
    	this.word = word;
    	lsWordTypes = new ArrayList<wordType>();
    }
    
    public void setMeaning(String meaning)
    {
    	this.meaning = meaning;
    }
    
    public void setPronun(String pronun) {
        this.pronun = pronun;
    }

    public void setWord(String word) {
        this.word = word;
    }
    public void addWordType(wordType wordType) {
        lsWordTypes.add(wordType);
    }

    public String getWord() {
        return word;
    }
    public List<wordType>  getLsWordType()
    {
    	return this.lsWordTypes;
    }

    public String getMeaning() {
        return meaning;
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (pronun != null) {
            sb.append("@").append(word);
            sb.append(" ").append(pronun).append("\n");
        } else { 
            sb.append("@").append(word).append("\n");
        }
        for (wordType wordType : lsWordTypes) {
            sb.append("*").append(wordType.getType()).append("\n");
            for (meaning meaning : wordType.getMeanings()) {
                sb.append("-").append(meaning.getMeaning()).append("\n");
                for (subMeaning subMeaning : meaning.getSubMeanings()) {
                    sb.append("=").append(subMeaning.getSubMeaning()).append("+").append(subMeaning.getDefinition()).append("\n");
                }
            }
        }
        return sb.toString();
    }
    
    public static word parseFromString(String input) {
        String[] lines = input.split("\n");

        String wordLine = (lines[0].substring(1)).trim();
        String wordLineParts[] = wordLine.split("/");
        word newWord = new word(wordLine);

        if (wordLineParts.length == 2) {
            newWord.setWord(wordLineParts[0].trim());
            newWord.setPronun("/" + wordLineParts[1].trim() + "/");
        } else {
            newWord.setWord(wordLineParts[0].trim());
        }

        wordType wordType = null;
        meaning meaning = null;

        for (int i = 1; i < lines.length; i++) {
            String line = lines[i].trim();
            if (line.startsWith("*")) {
                wordType = new wordType(line.substring(1).trim());
                newWord.addWordType(wordType);
            } else if (line.startsWith("-")) {
                if (wordType == null) {
                    wordType = new wordType("DEFAULT_TYPE");
                    newWord.addWordType(wordType);
                } 
                meaning = new meaning(line.substring(1).trim());
                wordType.addMeaning(meaning);
            } else if (line.startsWith("=")) {
                if (meaning == null) {
                    meaning = new meaning("EMPTY_MEANING");
                    // Nếu wordtype rỗng, tạo một wordtype với giá trị mặc định
                    if (wordType == null) {
                        wordType = new wordType("DEFAULT_TYPE");
                        newWord.addWordType(wordType);
                    }
                    wordType.addMeaning(meaning);
                }
                String[] parts = line.substring(1).trim().split("\\+");
                if (parts.length != 2 && parts.length > 0) {
                    subMeaning subMeaning = new subMeaning(parts[0], "");
                    meaning.addSubMeaning(subMeaning);
                } else if(parts.length > 0) {
                    subMeaning subMeaning = new subMeaning(parts[0], parts[1]);
                    meaning.addSubMeaning(subMeaning);
                }
            }
        }
        return newWord;
    }

    public String toHtmlString() {
        StringBuilder html = new StringBuilder("<html><body>");

        // Define font size, margin, and font family values
        int fontSizeHeading = 16;
        int fontSizeParagraph = 14;
        int marginLeft = 20;
        String fontFamily = "Arial, sans-serif";

        if (pronun != null) {
            html.append("<p style='font-size:" + fontSizeParagraph + "px; font-family: " + fontFamily + ";'>").append(pronun).append("</p>");
        }

        for (wordType wordType : lsWordTypes) {

            if (!wordType.getType().equals("DEFAULT_TYPE")) {
                html.append("<h2 style='font-size:" + fontSizeHeading + "px; font-family: " + fontFamily + ";'>").append(wordType.getType()).append("</h2>");
            } else {
                html.append("<h2 style='font-size:" + fontSizeHeading + "px; font-family: " + fontFamily + ";'>").append("</h2>");
            }
            for (meaning meaning : wordType.getMeanings()) {
                html.append("<p style='margin-left:" + marginLeft + "px;font-size:" + fontSizeParagraph + "px; font-family: " + fontFamily + "; color:red'>").append("<strong>").append(meaning.getMeaning()).append("</strong>").append("</p>");

                for (subMeaning subMeaning : meaning.getSubMeanings()) {
                    html.append("<p style='margin-left:" + marginLeft + "px;font-size:" + (fontSizeParagraph - 2) + "px; font-family: " + fontFamily + ";'>").append(subMeaning.getSubMeaning())
                            .append(": ")
                            .append("<i>")
                            .append(subMeaning.getDefinition()).append("</i>")
                            .append("</p>");
                }
            }
        }
        html.append("</body></html>");
        return html.toString();
    }
    
    
}
