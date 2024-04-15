package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class dictionaryManagement {
       private static dictionaryManagement instance = null;
       private Map<String, word> VNtoEnDic;
       private Map<String, word> EntoVNDic;
       private Map<String, word>  VNtoEnFavWords;
       private Map<String, word> EntoVNFavWords;
       private final String filePathEntoVn = "src/main/java/assets/Anh_Viet.xml";
       private final String filePathVntoEn = "src/main/java/assets/Viet_Anh.xml";
       private final String filePathENtoVNFavDic = "src/main/java/assets/ENtoVNFavDic.dat";
       private final String filePathVNtoENFavDic = "src/main/java/assets/VNtoENFavDic.dat";
       private final String filePathStatisticWords = "src/main/java/assets/statisticWords.dat";
       private List<statisticWord> listHistoryWords;
       private Map<String, Integer> listStatisticWords;
       
       public static boolean isVNtoEnMode;
       public static boolean isVNtoEnFavMode;
       
       
       private dictionaryManagement() 
       {
    	   this.VNtoEnDic = new TreeMap<>();
    	   this.EntoVNDic  = new TreeMap<>();
    	   try {
			loadVNtoENFavDic();
			 loadEntoVNFavDic();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	   this.listStatisticWords = new TreeMap<>();
    	   try {
			this.listHistoryWords = (List<statisticWord>) readFile(filePathStatisticWords);
			if(this.listHistoryWords == null)
			{
				this.listHistoryWords = new ArrayList<>();
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	   
    	   this.isVNtoEnMode = false;
    	   this.isVNtoEnFavMode = false;
       }
       
       public static dictionaryManagement getInstance()
       {
    	   if(instance == null)
    	   {
    		   instance = new dictionaryManagement();
    	   }
    	   return instance;
       }
      

	public Map<String, word> getVNtoEnDic() {
		return VNtoEnDic;
	}

	public Map<String, word> getEntoVNDic() {
		return EntoVNDic;
	}

	public Map<String, word> getVNtoEnFavWords() {
		return VNtoEnFavWords;
	}

	public Map<String, word> getEntoVNFavWords() {
		return EntoVNFavWords;
	}
	
	public Map<String, Integer> getListStatisticWords()
	{
		return this.listStatisticWords;
	}
	
	

	public List<statisticWord> GetListHistoryWords()
	{
		return this.listHistoryWords;
	}
    
    public void loadDictionaryFromXML(Map<String, word> dic, String path)
    {
    	 try {
             DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
             DocumentBuilder builder = factory.newDocumentBuilder();
             Document document = builder.parse(new File(path));

             NodeList recordList = document.getElementsByTagName("record");

             for (int i = 0; i < recordList.getLength(); i++) {
                 Node recordNode = recordList.item(i);

                 if (recordNode.getNodeType() == Node.ELEMENT_NODE) {
                     Element recordElement = (Element) recordNode;

                     String w = recordElement.getElementsByTagName("word").item(0).getTextContent();

                     NodeList meaningList = recordElement.getElementsByTagName("meaning");

                     StringBuilder meanings = new StringBuilder();
                     for (int j = 0; j < meaningList.getLength(); j++) {
                         Node meaningNode = meaningList.item(j);

                         if (meaningNode.getNodeType() == Node.ELEMENT_NODE) {
                             Element meaningElement = (Element) meaningNode;
                             meanings.append(meaningElement.getTextContent()).append("\n");
                         }
                     }
                     try {
                         dic.put(w, word.parseFromString(meanings.toString()));
                     } catch (Exception e) {
                         e.printStackTrace();
                         System.out.println("Error while parsing word: " + w);
                     }
                 }
             }
         } catch (Exception e) {
             e.printStackTrace();
         }	
    }
    
    public void loadEntoVNDicfromXML(String path)
    {
    	loadDictionaryFromXML(EntoVNDic, path);
    }
    
    public void loadVNtoEnDicfromXML(String path) 
    {
    	loadDictionaryFromXML(VNtoEnDic, path);
    }   
    private void saveDictionaryToXML(Map<String, word> dictionary, String filePath) {
        // Ghi dữ liệu từ HashMap vào file XML
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();

            Element rootElement = document.createElement("dictionary");
            document.appendChild(rootElement);

            for (String word : dictionary.keySet()) {
                Element recordElement = document.createElement("record");
                rootElement.appendChild(recordElement);

                Element wordElement = document.createElement("word");
                wordElement.appendChild(document.createTextNode(word));
                recordElement.appendChild(wordElement);

                Element meaningElement = document.createElement("meaning");
                meaningElement.appendChild(document.createTextNode(dictionary.get(word).toString()));
                recordElement.appendChild(meaningElement);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new File(filePath));
            transformer.transform(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public boolean checkWordExist(String word)
    {
    	if(isVNtoEnMode)
    	{
    		//if(VNtoEnDic.containsKey())
    	     for(String key : VNtoEnDic.keySet())
    	     {
    	    	 if(key.toLowerCase().equals(word.toLowerCase()))
    	    	 {
    	    		 return true;
    	    	 }
    	     }
    	}
    	else if(!isVNtoEnMode)
    	{
    		for(String key : EntoVNDic.keySet())
   	         {
   	    	 if(key.toLowerCase().equals(word.toLowerCase()))
   	    	 {
   	    		 return true;
   	    	 }
   	         }
    	}
    	return false;
    }
    
    
    public boolean insertWord(String word, String meaning)
    {
    	word w = new word(word);
    	wordType wt = new wordType("");
    	wt.getMeanings().add(new meaning(meaning));
    	w.getLsWordType().add(wt);
          
    	if(checkWordExist(word))
    	{
    		return false;
    	}
    	
    	if(isVNtoEnMode)
    	{
    	    VNtoEnDic.put(word, w);
    	    saveDictionaryToXML(VNtoEnDic, filePathVntoEn);
    	    
    	}
    	else
    	{
    		EntoVNDic.put(word, w);
    		saveDictionaryToXML(EntoVNDic, filePathEntoVn);
    	}
    	return true;
    }
    public boolean deleteWord(String word)
    {
        if(!checkWordExist(word))
        {
        	return false;
        }
        if(isVNtoEnMode)
        {
           VNtoEnDic.remove(word);
           saveDictionaryToXML(VNtoEnDic, filePathVntoEn);
        }
        else
        {
           EntoVNDic.remove(word);	
           saveDictionaryToXML(EntoVNDic, filePathEntoVn);
        }
        
        // delete in fav if exists
        deleteWordFav(word);
     
        return true;
    }
    
    // check if exist in list favorite
    public boolean checkExistInFavList(String word)
    {
        if(isVNtoEnMode)
        {
        	if(VNtoEnFavWords.containsKey(word))
        	{
        		return true;
        	}
        }
        else
        {
        	if(EntoVNFavWords.containsKey(word))
        	{
        		return true;
        	}
        }
    	return false;
    }
   
    /// insert into favorite list
    public boolean insertIntoFavoriteList(String favWord, word w)
    {
        if(checkExistInFavList(favWord))
        {
        	return false;
        }
    	if(isVNtoEnMode)
    	{
    	    VNtoEnFavWords.put(favWord, w);
    	    writeFileDat(filePathVNtoENFavDic, VNtoEnFavWords);	
    	}
    	else
    	{
    	   EntoVNFavWords.put(favWord, w);
    	   writeFileDat(filePathENtoVNFavDic, EntoVNFavWords);	
    	}
    	
    	return true;
    }
     
    /// Delete a word in favoriteList
    public void deleteWordFav(String word)
    {
    	if(isVNtoEnFavMode)
    	{
    		if(VNtoEnFavWords.containsKey(word))
    		{
    			VNtoEnFavWords.remove(word);
    			writeFileDat(filePathVNtoENFavDic, VNtoEnFavWords);	
    		}
    	}
    	else
    	{
    		if(EntoVNFavWords.containsKey(word))
    		{
    			EntoVNFavWords.remove(word);
    			writeFileDat(filePathENtoVNFavDic, EntoVNFavWords);
    		}
    	}
    }
    
    
   
    
    private void writeFileDat(String pathname, Object dic)
    {
    	  try {
  		  ObjectOutputStream objectOutput = new ObjectOutputStream(new FileOutputStream(pathname));
  		  objectOutput.writeObject(dic);
  		  objectOutput.close();
  		} catch (FileNotFoundException e) {
  			// TODO Auto-generated catch block
  			e.printStackTrace();
  		} catch (IOException e) {
  			// TODO Auto-generated catch block
  			e.printStackTrace();
  		}
    }
    
    public Object readFile(String pathname) throws ClassNotFoundException 
    {
    	try (ObjectInputStream objectInput = new ObjectInputStream(new FileInputStream(pathname));) {
			return objectInput.readObject();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			
		} catch (IOException e) {
		}
    	return null;
    }
    
  
    public void loadVNtoENFavDic() throws ClassNotFoundException
    {
    	
    	VNtoEnFavWords =  (Map<String, word>) readFile(filePathVNtoENFavDic);
    	if(VNtoEnFavWords == null)
    	{
    		VNtoEnFavWords = new TreeMap<>();
    	}
    }
    public void loadEntoVNFavDic() throws ClassNotFoundException
    {
    	 EntoVNFavWords =  (Map<String, word>) readFile(filePathENtoVNFavDic);	
    	 if(EntoVNFavWords == null)
    	 {
    		 EntoVNFavWords = new TreeMap<>(); 
    	 }
    }
    
    public void LoadStatisticWords() throws ClassNotFoundException
    {
    	listHistoryWords = (ArrayList<statisticWord>)readFile(filePathStatisticWords);   
    	if(listHistoryWords == null)
    	{
    	//	System.out.println("ok");
    		listHistoryWords = new ArrayList<statisticWord>();
    	}
    }
    
    public void StatisticWord(List<statisticWord> arr)
    {
    	listStatisticWords.clear();
    	for(statisticWord word : arr)
    	{
    		if(listStatisticWords.containsKey(word.get_word()))
    		{
    		  int times = listStatisticWords.get(word.get_word()) + 1;
    		  listStatisticWords.remove(word.get_word());
    		  listStatisticWords.put(word.get_word(), times);
    		  
    		}
    		else
    		{
    			listStatisticWords.put(word.get_word(), 1);
    		}
    	}
    }
    
    public void addToHistory(String word)
    {
    	//System.out.println(word);
        statisticWord st = new statisticWord(word, LocalDate.now());
        listHistoryWords.add(st);
        // write to file statistic 
        writeFileDat(filePathStatisticWords, listHistoryWords);   
    }
}
