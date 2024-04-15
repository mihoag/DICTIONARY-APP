package view;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import helper.Helper;
import model.dictionaryManagement;
import model.word;

import javax.swing.JList;
import javax.swing.JLabel;
import java.awt.SystemColor;
import java.awt.Font;
import javax.swing.JComboBox;

public class FavoritePage extends JFrame {

	private JPanel contentPane;
    private final String[] mode = new String[]{"EN-VN", "VN-EN"};   
    private final String[] sort = new String[]{"A-Z", "Z-A"}; 
	private DefaultListModel<String> listModel;
	private JScrollPane wordlistScrollPane = new JScrollPane(); 
    private static boolean isENtoVNmode;
    private JComboBox EnVNModeCombobox;
    private JComboBox SortBycomboBox_1;
    private JPanel panelMeaning;
    private JLabel TextShowMeaning;
    private dictionaryManagement dicManage;
    private JList listFavWord ;
    
    public void setData()
    {
       if(dicManage.isVNtoEnFavMode)
       {
    	  setDataForJList(dicManage.getVNtoEnFavWords());
       }
       else
       {
    	  setDataForJList(dicManage.getEntoVNFavWords());
       }
    }
    
    private void processComboboxModeEvent(ActionEvent event)
    {
    	String strMode = (String) EnVNModeCombobox.getSelectedItem();
    	//System.out.println(strMode);
    	if(strMode.equals(mode[0]))
    	{
    		// EN to VN process
    		dicManage.isVNtoEnFavMode = false;
    	}
    	else 
    	{
    	
    		dicManage.isVNtoEnFavMode = true;
    	}
    	
    	setData();
    	processComboboxSort();
    	TextShowMeaning.setText("");
    }
   
    public void setDataForJList(Map<String, word> listWords)
    {  	
    	           
    	listModel.removeAllElements();
	    for(String w : listWords.keySet())
	    {
	      listModel.addElement(w);
		}
    }
    
    private void wordListValueChanged(ListSelectionEvent evt) {//GEN-FIRST:event_wordListValueChanged
        if (!listFavWord.isSelectionEmpty() && !evt.getValueIsAdjusting()) {
            String w = (String) listFavWord.getSelectedValue();
            if (!dicManage.isVNtoEnFavMode) {
                word word = dicManage.getEntoVNFavWords().get(w);
                TextShowMeaning.setText(word.toHtmlString());
            } else {
            	 word word = dicManage.getVNtoEnFavWords().get(w);
                 TextShowMeaning.setText(word.toHtmlString());
            }
        }
    }
	public void initComponent()
	{
		setBounds(100, 100, 798, 637);
		setResizable(false);
		getContentPane().setLayout(null);
		
	
		// Initialize list model
	    listModel = new DefaultListModel<>();	
		listFavWord = new JList();
		listFavWord.setFont(new Font("Tahoma", Font.PLAIN, 18));
		listFavWord.setModel(listModel);
		  //Initialize wordlistScrollPane
		wordlistScrollPane = new JScrollPane(listFavWord);
		wordlistScrollPane.setLocation(20, 195);
		wordlistScrollPane.setSize(350, 365);
		wordlistScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    	this.getContentPane().add(wordlistScrollPane);
		setData();
    	
    	///
		
		JLabel lblListFavoriteWords = new JLabel("FAVORITE WORDS");
		lblListFavoriteWords.setForeground(new Color(184, 68, 29));
		lblListFavoriteWords.setFont(new Font("Tahoma", Font.BOLD, 30));
		lblListFavoriteWords.setBackground(SystemColor.menu);
		lblListFavoriteWords.setBounds(247, 0, 395, 67);
		getContentPane().add(lblListFavoriteWords);
		
		JLabel lblListFavoriteWords_1 = new JLabel("List favorite words");
		lblListFavoriteWords_1.setForeground(Color.BLACK);
		lblListFavoriteWords_1.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblListFavoriteWords_1.setBackground(SystemColor.menu);
		lblListFavoriteWords_1.setBounds(103, 152, 205, 44);
		getContentPane().add(lblListFavoriteWords_1);
		
		

		panelMeaning = new JPanel();
		panelMeaning.setBounds(418, 195, 326, 365);
		getContentPane().add(panelMeaning);
		
		/// add border for panel meaning
	    panelMeaning.setLayout(null);
	    TextShowMeaning = new JLabel("");
	    TextShowMeaning.setFont(new Font("Tahoma", Font.PLAIN, 18));
	    TextShowMeaning.setBounds(23, 24, 276, 202);
	    
	    JScrollPane showMeaningscroll = new JScrollPane(TextShowMeaning);
	    showMeaningscroll.setLocation(10, 10);
	    showMeaningscroll.setSize(306, 345);
	    showMeaningscroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	    showMeaningscroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
	    panelMeaning.add(showMeaningscroll);
		
		
	
		
		JLabel lblSortBy = new JLabel("Sort by:");
		lblSortBy.setForeground(Color.BLACK);
		lblSortBy.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblSortBy.setBackground(SystemColor.menu);
		lblSortBy.setBounds(190, 77, 78, 44);
		getContentPane().add(lblSortBy);
		
		EnVNModeCombobox = new JComboBox(mode);
		EnVNModeCombobox.setFont(new Font("Tahoma", Font.PLAIN, 20));
		EnVNModeCombobox.setBounds(488, 77, 112, 38);
		if(dicManage.isVNtoEnFavMode)
		{
			EnVNModeCombobox.setSelectedIndex(1);
		}
		else
		{
			EnVNModeCombobox.setSelectedIndex(0);
		}
		getContentPane().add(EnVNModeCombobox);
		
		JLabel lblMode = new JLabel("Mode:");
		lblMode.setForeground(Color.BLACK);
		lblMode.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblMode.setBackground(SystemColor.menu);
		lblMode.setBounds(418, 77, 205, 44);
		getContentPane().add(lblMode);
		
		SortBycomboBox_1 = new JComboBox(sort);
		SortBycomboBox_1.setFont(new Font("Tahoma", Font.PLAIN, 20));
		SortBycomboBox_1.setBounds(273, 77, 112, 38);
		getContentPane().add(SortBycomboBox_1);
	
		
		
		this.setLocationRelativeTo(null);
		this.setTitle("List favorite words");
		setIconImage(Toolkit.getDefaultToolkit().getImage(HomePage.class.getResource("/assets/dictionary.png")));
		getContentPane().setBackground(new Color(235, 216, 148));
		getContentPane().setLayout(null);
		
		JLabel lblMeaning = new JLabel("Meaning");
		lblMeaning.setForeground(Color.BLACK);
		lblMeaning.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblMeaning.setBackground(SystemColor.menu);
		lblMeaning.setBounds(539, 152, 205, 44);
		getContentPane().add(lblMeaning);
		setVisible(true);	
	}
	public void initEvent()
	{ 
		// set event for combobox ENVN
		EnVNModeCombobox.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				processComboboxModeEvent(e);
				
			}
		});
		
		
		// Set event for combobox Sort
		SortBycomboBox_1.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
				
			}
		});
		
		// Set event for list fav words
		listFavWord.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				// TODO Auto-generated method stub
				wordListValueChanged(e);
			}
		});
		
		///
		SortBycomboBox_1.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				processComboboxSort();
			}
		});
	}
	
	
	public void setDataSort(List<String> data)
	{
		listModel.removeAllElements();
	    for(String w : data)
	    {
	      listModel.addElement(w);
		}
	}
	
	public void processComboboxSort()
	{
		String option = (String) SortBycomboBox_1.getSelectedItem();
		TextShowMeaning.setText("");
		List<String> listFavSort = new ArrayList<>();
		if(dicManage.isVNtoEnFavMode)
		{
			for(String str : dicManage.getVNtoEnFavWords().keySet())
			{
				listFavSort.add(str);
			}
		}
		else
		{
			for(String str : dicManage.getEntoVNFavWords().keySet())
			{
				listFavSort.add(str);
			}   	
		}
        switch (option) {
            case "A-Z":
            {
                if(dicManage.isVNtoEnFavMode)
                {
                	Collections.sort(listFavSort, Collator.getInstance(new Locale("vi", "VN")));
                }
                else
                {
                    Collections.sort(listFavSort, new Comparator<String>() {
    					@Override
    					public int compare(String o1, String o2) {
    						// TODO Auto-generated method stub
    						if(dicManage.isVNtoEnFavMode)
    						{
    							return Helper.unicodeToASCII(o1).compareToIgnoreCase(Helper.unicodeToASCII(o2));
    						}
    						return o1.compareToIgnoreCase(o2);
    					}
    				});  
                }
                break;	
            }
              
            case "Z-A" :
            {
            	System.out.println("ok");
            	if(dicManage.isVNtoEnFavMode)
                {
                	Collections.sort(listFavSort, Collator.getInstance(new Locale("vi", "VN")));
                	Collections.reverse(listFavSort);
                
                }
                else
                {
                	 Collections.sort(listFavSort, new Comparator<String>() {

      					@Override
      					public int compare(String o1, String o2) {
      						// TODO Auto-generated method stub
      		
      						return o2.compareToIgnoreCase(o1);
      					}
      				});  
                  
                }
            	break;
            }
        }
        setDataSort(listFavSort);
	}
	public FavoritePage() throws ClassNotFoundException {
		dicManage = dictionaryManagement.getInstance();
		initComponent();
		initEvent(); 
	}
}
