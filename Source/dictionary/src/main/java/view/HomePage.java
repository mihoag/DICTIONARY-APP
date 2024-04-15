package view;

import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.KeyEventDispatcher;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import controller.actionController;
import helper.Helper;
import model.dictionaryManagement;
import model.word;

import java.awt.Color;
import java.awt.Event;
import java.awt.Toolkit;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.SystemColor;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;

public class HomePage extends JFrame {
     
	private JPanel contentPane;
	private JComboBox VnEnModecomboBox;
    public  JList listWord;
    private DefaultListModel<String> listModel;
    public  JTextArea textNewWord;
    private JButton addNewWordBtn; 
    private JPanel panelMeaning;
    private dictionaryManagement dicManage;
    public  JTextArea textMeaning;
    private JButton btnDelete;
    private JButton btnAddToFavorite;
    private JMenuBar menuBar;
    private JMenu utilityMenu;
    private JMenuItem statisticMenuItem ;
    private JMenuItem favMenuItem;
    private JScrollPane wordlistScrollPane;
    public JLabel TextShowMeaning;
    private JTextArea textSearchField;
    private JPopupMenu JListPopUp;
    private JMenuItem deleteMenutem;
    private JMenuItem addFavMenuItem;
    private FavoritePage favPage;
    private StatisticPage statisticPage;
    
    private final String filePathEntoVn = "src/main/java/assets/Anh_Viet.xml";
    private final String filePathVntoEn = "src/main/java/assets/Viet_Anh.xml";
    private final String[] mode = new String[]{"EN-VN", "VN-EN"};    
    
    

    public void setDataForJList(Map<String, word> listWords)
    {  	     
               //  listWord.removeSelectionInterval(0, 0);
                 listModel.clear();
                 listModel.addElement("");
            	 for(String w : listWords.keySet())
			       {
			    	   listModel.addElement(w);
			       }
    }
    
    public void searchInputKeyTyped(KeyEvent evt)
    {
    	TextShowMeaning.setText("");
    	String keyword = textSearchField.getText();
  
        keyword += evt.getKeyChar();
        keyword = keyword.trim().toLowerCase();
       
        if(keyword == "")
        {
           setData();
           return;
        }
        
        listModel.removeAllElements();
        
        if (!dicManage.isVNtoEnMode) {
        	for(String s : dicManage.getEntoVNDic().keySet())
        	{
        		 if (s.toLowerCase().startsWith(keyword)) {
                     //
        			 listModel.addElement("");
        			 listModel.addElement(s);
                     
                 }
        	}
        } else {
        	for(String s : dicManage.getVNtoEnDic().keySet())
        	{
        		 if (Helper.unicodeToASCII(s).toLowerCase().startsWith(
                         Helper.unicodeToASCII(keyword).toLowerCase()
                 )) {
        			 listModel.addElement("");
        			 listModel.addElement(s);  
                 }
        	}
        }
        listWord.clearSelection();     
    }
    
    private void wordListValueChanged(ListSelectionEvent evt) {
    	//GEN-FIRST:event_wordListValueChanged
        if (!listWord.isSelectionEmpty() && !evt.getValueIsAdjusting()) {
            String w = (String) listWord.getSelectedValue();
            if(w == "")
            {
            	 listWord.setSelectedIndex(1);
            	return;
            }
            if (!dicManage.isVNtoEnMode) {
                word word = dicManage.getEntoVNDic().get(w);
                TextShowMeaning.setText(word.toHtmlString());
                /// process statistic
                dicManage.addToHistory(w);   
            } else {
            	 word word = dicManage.getVNtoEnDic().get(w);
                 TextShowMeaning.setText(word.toHtmlString());
                 /// process statistic
                 dicManage.addToHistory(w);
            }
        }
    }
    
    private void processComboboxEvent(ActionEvent event)
    {
    	String strMode = (String) VnEnModecomboBox.getSelectedItem();
    	//System.out.println(strMode);
    	if(strMode.equals(mode[0]))
    	{
    		// EN to VN process
    		dicManage.isVNtoEnMode = false;
    	}
    	else 
    	{
    	
    		dicManage.isVNtoEnMode = true;
    	}
    	
    	setData();
    	textSearchField.setText("");
    	TextShowMeaning.setText("");
    }
    
    public void setData()
    {
       if(dicManage.isVNtoEnMode)
       {
    	  setDataForJList(dicManage.getVNtoEnDic());   
       }
       else
       {
    	   setDataForJList(dicManage.getEntoVNDic());
       }
    }
    
    private void processJListMouseClickEvent(MouseEvent event)
    {
    	if(SwingUtilities.isRightMouseButton(event))
    	{
    	   String selectedValue = (String) listWord.getSelectedValue();
    	   if(selectedValue == "")
    	   {
    		   listWord.setSelectedIndex(1);
    		   return;
    	   }
           listWord.setSelectedIndex(listWord.locationToIndex(event.getPoint()));
           // process Statistic
           dicManage.addToHistory((String) listWord.getSelectedValue());
           ///
           JListPopUp.show(listWord, event.getX(), event.getY());
    	}
    }
    
    public boolean addToFavList(String word)
    {
    	if(dicManage.isVNtoEnMode)
    	{
    	   if(dicManage.getVNtoEnDic().containsKey(word))
    	   {
    		   return dicManage.insertIntoFavoriteList(word, dicManage.getVNtoEnDic().get(word));
    	   }
    	}
    	else
    	{   
    		 if(dicManage.getEntoVNDic().containsKey(word))
      	     {
      		   return dicManage.insertIntoFavoriteList(word, dicManage.getEntoVNDic().get(word));
      	     }
    	}
    	return false;
    }
    
    public void processDeleteWordItem()
    {
    	String word = (String) listWord.getSelectedValue();
    	boolean check = dicManage.deleteWord(word);
		if(check)
		{
			setData();
			JOptionPane.showMessageDialog(this, "Delete successfully");
		}
		else
		{
			JOptionPane.showMessageDialog(this, "Delete fail");
		}
    }
   
	public void initComponent()
	{
		
		this.setLocationRelativeTo(null);
		this.setTitle("Dictionary App");
		setIconImage(Toolkit.getDefaultToolkit().getImage(HomePage.class.getResource("/assets/dictionary.png")));
		getContentPane().setBackground(new Color(235, 216, 148));
		getContentPane().setLayout(null);
		
		JLabel lblTitle = new JLabel("DICTIONARY APP");
		lblTitle.setFont(new Font("Tahoma", Font.BOLD, 30));
		lblTitle.setForeground(new Color(184, 68, 29));
		lblTitle.setBackground(new Color(240, 240, 240));
		lblTitle.setBounds(216, 10, 278, 67);
		getContentPane().add(lblTitle);
		
		// Text new word field
		textNewWord = new JTextArea();
		textNewWord.setFont(new Font("Monospaced", Font.PLAIN, 20));
		textNewWord.setBounds(133, 493, 278, 38);
		getContentPane().add(textNewWord);
		
		
		JLabel lblNewWord = new JLabel("New word");
		lblNewWord.setForeground(new Color(0, 0, 0));
		lblNewWord.setFont(new Font("Tahoma", Font.PLAIN, 23));
		lblNewWord.setBackground(SystemColor.menu);
		lblNewWord.setBounds(20, 493, 103, 44);
		lblNewWord.setLabelFor(textNewWord);
		lblNewWord.setDisplayedMnemonic('N');
		getContentPane().add(lblNewWord);
		
	
		// Initialize list model
		listModel = new DefaultListModel<>();
		// JList to show all vocab
		listWord = new JList();
		listWord.setFont(new Font("Tahoma", Font.PLAIN, 18));
		// add border for jlist
		
		listWord.setModel(listModel);
		// set data
		setData();
		/////
	        //Initialize wordlistScrollPane
			wordlistScrollPane = new JScrollPane(listWord);
			wordlistScrollPane.setLocation(20, 217);
			wordlistScrollPane.setSize(391, 247);
			wordlistScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
	    	this.getContentPane().add(wordlistScrollPane);
		
		
		// combox for english, VN mode
		VnEnModecomboBox = new JComboBox(mode);
		VnEnModecomboBox.setFont(new Font("Tahoma", Font.PLAIN, 18));
		VnEnModecomboBox.setBounds(308, 119, 103, 38);
		getContentPane().add(VnEnModecomboBox);
		
		
		
	    TextShowMeaning = new JLabel("");
	    TextShowMeaning.setBackground(new Color(255, 255, 255));
	    TextShowMeaning.setFont(new Font("Tahoma", Font.PLAIN, 18));
	    TextShowMeaning.setBounds(23, 24, 276, 202);
	    
	    JScrollPane showMeaningscroll = new JScrollPane(TextShowMeaning);
	    showMeaningscroll.setLocation(430, 217);
	    showMeaningscroll.setSize(391, 247);
	    showMeaningscroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	    showMeaningscroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
	    getContentPane().add(showMeaningscroll);
	   
	    // Set image icon
		JPanel panelImage = new JPanel();
		panelImage.setBackground(new Color(235, 216, 148));
		panelImage.setBounds(490, 10, 91, 67);
		ImageIcon img = new ImageIcon("src/main/java/assets/dictionary1.png");
	        // Set the desired width and height
	    int width = 60;  // Width in pixels
	    int height = 60; // Height in pixels
	        // Create a scaled version of the ImageIcon
	    Image scaledImage = img.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
	    ImageIcon scaledIcon = new ImageIcon(scaledImage);
		JLabel lblImage = new JLabel();
		lblImage.setIcon(scaledIcon);
		panelImage.add(lblImage, BorderLayout.CENTER);
		
	    
		/// add image
		getContentPane().add(panelImage);
		
		textSearchField = new JTextArea();
		textSearchField.setFont(new Font("Monospaced", Font.PLAIN, 20));
		textSearchField.setBounds(103, 119, 194, 38);
		getContentPane().add(textSearchField);
		
		
		textMeaning = new JTextArea();
		textMeaning.setFont(new Font("Monospaced", Font.PLAIN, 20));
		textMeaning.setBounds(133, 553, 278, 38);
		getContentPane().add(textMeaning);
		
	
		
		JLabel lblMeaning = new JLabel("Meaning");
		lblMeaning.setForeground(new Color(0, 0, 0));
		lblMeaning.setFont(new Font("Tahoma", Font.PLAIN, 23));
		lblMeaning.setBackground(SystemColor.menu);
		lblMeaning.setBounds(20, 547, 103, 44);
		lblMeaning.setLabelFor(textMeaning);
		lblMeaning.setDisplayedMnemonic('M');
		getContentPane().add(lblMeaning);
	
	
		JLabel lblSearch1 = new JLabel("Search");
		lblSearch1.setForeground(new Color(0, 0, 0));
		lblSearch1.setFont(new Font("Tahoma", Font.PLAIN, 23));
		lblSearch1.setBackground(SystemColor.menu);
		lblSearch1.setBounds(20, 113, 74, 44);
		 lblSearch1.setLabelFor(textSearchField);
		    lblSearch1.setDisplayedMnemonic('S');
		getContentPane().add(lblSearch1);
	   
		
		
		 // Create an ImageIcon with an image file (adjust the path accordingly)
        ImageIcon originalIconPlus = new ImageIcon(HomePage.class.getResource("/assets/plus.png"));
        // Get the image from the original icon
        Image originalImage = originalIconPlus.getImage();
        // Scale the image to the desired dimensions
        int w_plus = 30; // Desired width
        int h_plus = 30; // Desired height
        Image scaledImagePlus = originalImage.getScaledInstance(w_plus, h_plus, Image.SCALE_SMOOTH);
        // Create a new ImageIcon with the scaled image
        ImageIcon scaledIconPlus = new ImageIcon(scaledImagePlus);
		addNewWordBtn = new JButton("ADD NEW WORD");	
		addNewWordBtn.setForeground(new Color(0, 0, 0));
		addNewWordBtn.setIcon(scaledIconPlus);
		addNewWordBtn.setFont(new Font("Verdana", Font.PLAIN, 15));
		
		addNewWordBtn.setBackground(new Color(242, 149, 108));
		addNewWordBtn.setOpaque(true);
		addNewWordBtn.setBounds(133, 607, 210, 38);
		getContentPane().add(addNewWordBtn);
		
		 // Create an ImageIcon with an image file (adjust the path accordingly)
        ImageIcon originalIconDelete = new ImageIcon(HomePage.class.getResource("/assets/bin.png"));
        // Get the image from the original icon
        Image originalImageDelete = originalIconDelete.getImage();
        // Scale the image to the desired dimensions
        int w_bin = 30; // Desired width
        int h_bin = 30; // Desired height
        Image scaledImageBin = originalImageDelete.getScaledInstance(w_bin, h_bin, Image.SCALE_SMOOTH);
        // Create a new ImageIcon with the scaled image
        ImageIcon scaledIconBin = new ImageIcon(scaledImageBin);
		
		btnDelete = new JButton("DELETE");
		btnDelete.setOpaque(true);
		btnDelete.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnDelete.setBackground(new Color(242, 149, 108));
		btnDelete.setBounds(520, 551, 235, 38);
		btnDelete.setIcon(scaledIconBin);
		getContentPane().add(btnDelete);
		
		
		
		
		
		 // Create an ImageIcon with an image file (adjust the path accordingly)
        ImageIcon originalIconFav = new ImageIcon(HomePage.class.getResource("/assets/addFav.png"));
        // Get the image from the original icon
        Image originalImageFav = originalIconFav.getImage();
        // Scale the image to the desired dimensions
        int w_fav = 30; // Desired width
        int h_fav = 30; // Desired height
        Image scaledImageFav = originalImageFav.getScaledInstance(w_fav, h_fav, Image.SCALE_SMOOTH);
        // Create a new ImageIcon with the scaled image
        ImageIcon scaledIconFav = new ImageIcon(scaledImageFav);
		
		
		btnAddToFavorite = new JButton("ADD TO FAVORITE LIST");
		btnAddToFavorite.setOpaque(true);
		btnAddToFavorite.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnAddToFavorite.setBackground(new Color(242, 149, 108));
		btnAddToFavorite.setBounds(520, 491, 235, 38);
		btnAddToFavorite.setIcon(scaledIconFav);
		getContentPane().add(btnAddToFavorite);
		
		
		JLabel labelListwords = new JLabel("List words");
		labelListwords.setForeground(Color.BLACK);
		labelListwords.setFont(new Font("Tahoma", Font.BOLD, 20));
		labelListwords.setBackground(SystemColor.menu);
		labelListwords.setBounds(177, 176, 124, 44);
		getContentPane().add(labelListwords);
		
		JLabel lblMean = new JLabel("Meaning");
		lblMean.setForeground(Color.BLACK);
		lblMean.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblMean.setBackground(SystemColor.menu);
		lblMean.setBounds(592, 176, 124, 44);
		getContentPane().add(lblMean);
		
		
		/// set menu bar
		menuBar = new JMenuBar();
		utilityMenu = new JMenu("Utility");
		statisticMenuItem  = new JMenuItem("Statistic");
		favMenuItem = new JMenuItem("Favorite words");
		
		utilityMenu.add(statisticMenuItem);
		utilityMenu.addSeparator();
		utilityMenu.add(favMenuItem);
		menuBar.add(utilityMenu);
		setJMenuBar(menuBar);
		
		
		
		// set menu popup
        JListPopUp = new JPopupMenu();
        deleteMenutem = new JMenuItem("delete a word");
        addFavMenuItem = new JMenuItem("Add to list favorite");
        JListPopUp.add(deleteMenutem);
        JListPopUp.addSeparator();
        JListPopUp.add(addFavMenuItem);
        
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 855, 714);
		setResizable(false);
		setVisible(true);
		
	}
	public void initEvent()
	{
		  // Event for Jlist
		  listWord.addMouseListener(new java.awt.event.MouseAdapter() {
	            public void mouseClicked(MouseEvent evt) {
	            	processJListMouseClickEvent(evt);
	            }
	        });
	       listWord.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				// TODO Auto-generated method stub
				wordListValueChanged(e);
				
			}
	     	});
	       
	       // Set event for search field
	       textSearchField.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				searchInputKeyTyped(e);
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
	       
	       // Set event for combobox
	       VnEnModecomboBox.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				processComboboxEvent(e);
			}
		    });
	       
	       
	       //
	       actionController ac=  new actionController(this);
	       addNewWordBtn.addActionListener(ac);
	       btnDelete.addActionListener(ac);
	       deleteMenutem.addActionListener(ac);
	       btnAddToFavorite.addActionListener(ac);
	       addFavMenuItem.addActionListener(ac);
	       
	       
	       // set event for statisticMenuItem,  favMenuItem
	       statisticMenuItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
			    try {
					statisticPage = new StatisticPage();
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
	     	});
	     
	       favMenuItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try {
					favPage = new FavoritePage();
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
	       
	       deleteMenutem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				processDeleteWordItem();
			}
		});
	       
	}
	public HomePage() {
		dicManage = dictionaryManagement.getInstance();
		  //Đường dẫn tới tệp từ điển XML
          //Load từ điển từ tệp XML
	      dicManage.loadEntoVNDicfromXML(filePathEntoVn);
	      dicManage.loadVNtoEnDicfromXML(filePathVntoEn);
	      initComponent();
	      initEvent();	
	}
	public static void main(String[] args) {
		new HomePage();
	}
}
