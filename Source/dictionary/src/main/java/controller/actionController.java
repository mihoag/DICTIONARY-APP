package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import model.dictionaryManagement;
import view.HomePage;

public class actionController implements ActionListener{

    private HomePage homepage;
    public dictionaryManagement dicmanage;
    public actionController(HomePage page)
    {
    	this.homepage = page ;
    	dicmanage = dictionaryManagement.getInstance();
    }
    
    public void deleteWord()
    {
    	String word = (String) homepage.listWord.getSelectedValue();
    	if(word != null)
    	{
    		boolean check = dicmanage.deleteWord(word);
    		if(check)
    		{
    			homepage.setData();
    		    homepage.TextShowMeaning.setText("");
    			JOptionPane.showMessageDialog(homepage, "Delete successfully");
    		}
    		else
    		{
    			
    			JOptionPane.showMessageDialog(homepage, "Delete fail");
    		}
    	}
    }
    
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
	    String strCommand = e.getActionCommand();
	    switch (strCommand) {
		case "ADD NEW WORD"  : {
	        
			String word = homepage.textNewWord.getText();
			String meaning = homepage.textMeaning.getText();
			
			if(word.equals("") || meaning.equals(""))
			{
				JOptionPane.showMessageDialog(this.homepage, "The field is not empty!");
				return;
			}
			
			boolean check = dicmanage.insertWord(word, meaning);
			if(check)
			{
			    homepage.setData();
			    homepage.textMeaning.setText("");
			    homepage.textNewWord.setText("");
			    JOptionPane.showMessageDialog(homepage, "Insert successfully!!!");
			}
			else
			{
				JOptionPane.showMessageDialog(homepage, "The word is existing");
			}
			break;
		}
		
		case "DELETE":
		{
		    deleteWord();
		    break;
		}
		
		case "delete a word":
		{
		  deleteWord();
		  break;	
		}
		
		
		case "ADD TO FAVORITE LIST":
		{
		    String word = (String) homepage.listWord.getSelectedValue();
		    Boolean check =   homepage.addToFavList(word); 
		    if(check)
		    {
		    	JOptionPane.showMessageDialog(homepage, "Add to favorite word list successfully!!!");
		    }
		    else
		    {
		    	JOptionPane.showMessageDialog(homepage, "This word is existed in the favorite word");
		    }
			break;
		}
		
		case "Add to list favorite":
		{
			  String word = (String) homepage.listWord.getSelectedValue();
			    Boolean check =   homepage.addToFavList(word); 
			    if(check)
			    {
			    	JOptionPane.showMessageDialog(homepage, "Add to favorite word list successfully!!!");
			    }
			    else
			    {
			    	JOptionPane.showMessageDialog(homepage, "This word is existed in the favorite word");
			    }
				break;
		}
	}
	}
}
