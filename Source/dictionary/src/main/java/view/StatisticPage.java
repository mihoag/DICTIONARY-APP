package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.SystemColor;
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import controller.actionController;
import model.dictionaryManagement;
import model.statisticWord;
import model.word;

import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.JTable;

public class StatisticPage extends JFrame {

	private JPanel contentPane;
	
	private com.toedter.calendar.JDateChooser fromDateInput;
	private com.toedter.calendar.JDateChooser toDateInput;
	private JTable table;
	private DefaultTableModel tableModel;
	private dictionaryManagement dicManage;
	private JButton btnSearch;
	private JButton btnShowAll;
	
	
	
	public void setData()
	{
	   	tableModel.setRowCount(0);
	   	int i = 0;
	   	for(String str : dicManage.getListStatisticWords().keySet())
	   	{
	   		//System.out.println(str);
	   		i++;
	   	   	tableModel.addRow(new Object[] {i, str, dicManage.getListStatisticWords().get(str)});
	   	}	
	}
	
	 private void FilterActionPerformed() {//GEN-FIRST:event_searchBtnActionPerformed
	        // TODO add your handling code here:

	        Calendar fromDate = (Calendar) fromDateInput.getCalendar();
	        Calendar toDate = (Calendar) toDateInput.getCalendar();

	        if (fromDate != null && toDate != null) {
	            LocalDate startDate = LocalDate.of(fromDate.get(Calendar.YEAR),
	                    fromDate.get(Calendar.MONTH) + 1,
	                    fromDate.get(Calendar.DAY_OF_MONTH));
	            LocalDate endDate = LocalDate.of(toDate.get(Calendar.YEAR),
	                    toDate.get(Calendar.MONTH) + 1,
	                    toDate.get(Calendar.DAY_OF_MONTH));

	            if (startDate.isBefore(endDate) || startDate.isEqual(endDate)) {
	                List<statisticWord> currentList = new ArrayList<>();
	                for (int i = 0; i < dicManage.GetListHistoryWords().size(); i++) {
	                    statisticWord word = dicManage.GetListHistoryWords().get(i);
	                    LocalDate currentDate = word.get_date();
	                    if ((currentDate.isAfter(startDate) && currentDate.isBefore(endDate))
	                            || currentDate.equals(startDate) || currentDate.equals(endDate)) {
	                        currentList.add(word);
	                    }
	                }
	                
	                dicManage.StatisticWord(currentList);
	                setData();
	            } else {
	                JOptionPane.showMessageDialog(this,
	                        "The start date and the end date must be valid",
	                        "Warning",
	                        JOptionPane.WARNING_MESSAGE);
	            }
	        }
	    }
	 
	 private void processShowAll() throws ClassNotFoundException
	 {
		 dicManage.LoadStatisticWords();
		 dicManage.StatisticWord(dicManage.GetListHistoryWords());
		 //fromDateInput.cleanup();
		 //toDateInput.cleanup();
		 fromDateInput.setDate(null);
		 toDateInput.setDate(null);
		 setData();
	 }
	
	public void initComponent()
	{
		///
		this.setTitle("Statistic");
		setIconImage(Toolkit.getDefaultToolkit().getImage(HomePage.class.getResource("/assets/stats.png")));
		getContentPane().setBackground(new Color(235, 216, 148));
		getContentPane().setLayout(null);
		
		
		///
		setBounds(100, 100, 798, 637);
		setResizable(false);
		getContentPane().setLayout(null);
		Border border1 = BorderFactory.createLineBorder(Color.RED, 3);
		
	    // 
		JLabel lblListFavoriteWords = new JLabel("STATISTIC");
		lblListFavoriteWords.setForeground(new Color(184, 68, 29));
		lblListFavoriteWords.setFont(new Font("Tahoma", Font.BOLD, 30));
		lblListFavoriteWords.setBackground(SystemColor.menu);
		lblListFavoriteWords.setBounds(285, 10, 171, 67);
		getContentPane().add(lblListFavoriteWords);
		
		JLabel lblFromDate = new JLabel("From date:");
		lblFromDate.setForeground(Color.BLACK);
		lblFromDate.setFont(new Font("Tahoma", Font.PLAIN, 23));
		lblFromDate.setBackground(SystemColor.menu);
		lblFromDate.setBounds(44, 87, 134, 44);
		getContentPane().add(lblFromDate);
		
		JLabel lblToDate = new JLabel("To date:");
		lblToDate.setForeground(Color.BLACK);
		lblToDate.setFont(new Font("Tahoma", Font.PLAIN, 23));
		lblToDate.setBackground(SystemColor.menu);
		lblToDate.setBounds(430, 87, 134, 44);
		getContentPane().add(lblToDate);
		
		btnSearch = new JButton("Filter");
		btnSearch.setOpaque(true);
		btnSearch.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnSearch.setBackground(new Color(211, 103, 84));
		btnSearch.setBounds(620, 134, 117, 38);
		getContentPane().add(btnSearch);
		this.setBackground(new Color(235, 216, 148));
		
		
		ImageIcon img = new ImageIcon("src/main/java/assets/stats.png");
	        // Set the desired width and height
	    int width = 60;  // Width in pixels
	    int height = 60; // Height in pixels
	        // Create a scaled version of the ImageIcon
	    Image scaledImage = img.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
	    ImageIcon scaledIcon = new ImageIcon(scaledImage);
	    
	    ///
	    fromDateInput = new com.toedter.calendar.JDateChooser();
	    fromDateInput.getCalendarButton().setFont(new Font("Tempus Sans ITC", Font.PLAIN, 20));
	    fromDateInput.getCalendarButton().addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    	}
	    });
	    fromDateInput.setSize(187, 37);
	    fromDateInput.setLocation(161, 87);
	    toDateInput = new com.toedter.calendar.JDateChooser();
	    toDateInput.setLocation(533, 87);
	    toDateInput.setSize(201, 37);
	    
	    //fromDateInput.setEnabled(false);
	    //toDateInput.setEnabled(false);
	    
	    //
	    
	    
	    this.getContentPane().add(fromDateInput);
	    this.getContentPane().add(toDateInput);
	    
	    
	    // Initialize tableModel
	    tableModel = new DefaultTableModel();
	    tableModel.addColumn("No.");
	    tableModel.addColumn("Word");
	    tableModel.addColumn("Times");
	    //tableModel.addRow(new Object[] {1,"Name",3});
	    //
	    table = new JTable(tableModel);
	    table.setBounds(44, 207, 690, 367);
	    JScrollPane sc = new JScrollPane(table);
	    setData();
	 
	    sc.setLocation(44, 236);
	    sc.setSize(690, 338);
	    sc.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	    getContentPane().add(sc);
	    
	    btnShowAll = new JButton("Show all");
	    btnShowAll.setOpaque(true);
	    btnShowAll.setFont(new Font("Tahoma", Font.PLAIN, 20));
	    btnShowAll.setBackground(new Color(211, 103, 84));
	    btnShowAll.setBounds(44, 180, 117, 38);
	    getContentPane().add(btnShowAll);
	    //
		setVisible(true);	
	}
	public void initEvent()
	{
	    btnSearch.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				FilterActionPerformed();
				
			}
		});
	    
	    btnShowAll.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try {
					processShowAll();
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
	}
	
	public StatisticPage() throws ClassNotFoundException {
       dicManage = dictionaryManagement.getInstance();
       dicManage.LoadStatisticWords();
       dicManage.StatisticWord(dicManage.GetListHistoryWords());
	   initComponent();
	   initEvent();   
	}
}
