//import java.awt.BorderLayout;  
import java.awt.Container;  
//import java.awt.Point;  
import java.awt.event.ActionEvent;  
import java.awt.event.ActionListener;  
//import java.awt.event.InputEvent;  
//import java.awt.event.MouseAdapter;  
//import java.awt.event.MouseEvent;  
import java.awt.event.WindowAdapter;  
import java.awt.event.WindowEvent;  
//import java.util.ArrayList;
//import java.util.Arrays;  
//import java.util.List;
import java.util.Vector;  
  


//import javax.swing.JButton;  
import javax.swing.JFrame;  
import javax.swing.JLabel;  
//import javax.swing.JOptionPane;  
import javax.swing.JPanel;  
import javax.swing.JScrollPane;  
import javax.swing.JTable;  
import javax.swing.JTextField;  
//import javax.swing.ListSelectionModel;  
//import javax.swing.event.TableModelEvent;  
//import javax.swing.event.TableModelListener;  
import javax.swing.table.DefaultTableModel;  
//import javax.swing.table.JTableHeader;  
//import javax.swing.table.TableColumnModel;  

import java.util.Date;      

import javax.swing.Timer;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.concurrent.atomic.AtomicLong;
   
public class GrabCQSSCwindow extends JFrame {  
  

    private static final long serialVersionUID = -8581492063632813033L;
    final private JTable table1;
    final private JTable table2;
    final private JTable table3;
    final private JTable table4;
    final private JTable table5;
    final private JTable table;
    private JLabel labelA;
    final private JTextField textFieldA = new JTextField(15);  
    final private JTextField textFieldB = new JTextField(15);
    final private JTextField textFieldC = new JTextField(15);
    //private long remainTime = 0;
    private AtomicLong remainTime = new AtomicLong(0);
  
    public GrabCQSSCwindow() {  
        // TODO Auto-generated constructor stub  
        final Container container = getContentPane();
        
        //第一球
        Vector<String> ball1 = new Vector<String>();
        ball1.add("第一球");
        final DefaultTableModel ball1Model = new DefaultTableModel(  
        		null, ball1);  
        final JTable tableBall1 = new JTable(ball1Model);
        JScrollPane paneBall1 = new JScrollPane();  
        paneBall1.setViewportView(tableBall1);  
        paneBall1.setSize(600, 25);
        paneBall1.setLocation(0, 50);
        container.add(paneBall1);
        
        Vector<String> tableColumnNames1 = new Vector<String>();  
        tableColumnNames1.add("种类");  
        tableColumnNames1.add("赔率");
        tableColumnNames1.add("金额");
        tableColumnNames1.add("差值");
        Vector<Vector<String>> tableValues1 = new Vector<Vector<String>>();
        tableValues1.add(new Vector<String>());
        tableValues1.add(new Vector<String>());
        tableValues1.add(new Vector<String>());
        tableValues1.add(new Vector<String>());
        tableValues1.get(0).add("大");
        tableValues1.get(1).add("小");
        tableValues1.get(2).add("单");
        tableValues1.get(3).add("双");
        
        final DefaultTableModel defaultTableModel1 = new DefaultTableModel(  
                tableValues1, tableColumnNames1);  
        table1 = new JTable(defaultTableModel1);  
        JScrollPane scrollPane1 = new JScrollPane();  
        scrollPane1.setViewportView(table1);  
        scrollPane1.setSize(600, 89);
        scrollPane1.setLocation(0, 75);
        container.add(scrollPane1);  
        
        
        //第二球
        Vector<String> ball2 = new Vector<String>();
        ball2.add("第二球");
        final DefaultTableModel ball2Model = new DefaultTableModel(  
        		null, ball2);  
        final JTable tableBall2 = new JTable(ball2Model);
        JScrollPane paneBall2 = new JScrollPane();  
        paneBall2.setViewportView(tableBall2);  
        //paneBall2.setLocation(600, 50);
        paneBall2.setSize(600, 25);
        paneBall2.setLocation(0, 75 + 89);
        container.add(paneBall2);
        
        Vector<String> tableColumnNames2 = new Vector<String>();  
        tableColumnNames2.add("种类");  
        tableColumnNames2.add("赔率");
        tableColumnNames2.add("金额");
        tableColumnNames2.add("差值");
        Vector<Vector<String>> tableValues2 = new Vector<Vector<String>>();
        tableValues2.add(new Vector<String>());
        tableValues2.add(new Vector<String>());
        tableValues2.add(new Vector<String>());
        tableValues2.add(new Vector<String>());
        tableValues2.get(0).add("大");
        tableValues2.get(1).add("小");
        tableValues2.get(2).add("单");
        tableValues2.get(3).add("双");
        
        final DefaultTableModel defaultTableModel2 = new DefaultTableModel(  
                tableValues2, tableColumnNames2);  
        table2 = new JTable(defaultTableModel2);  
        JScrollPane scrollPane2 = new JScrollPane();  
        scrollPane2.setViewportView(table2);  
        //scrollPane2.setLocation(600, 75);
        scrollPane2.setSize(600, 89);
        scrollPane2.setLocation(0, 75 + 89 + 25);
        container.add(scrollPane2);  
        
        
        //第三球
        Vector<String> ball3 = new Vector<String>();
        ball3.add("第三球");
        final DefaultTableModel ball3Model = new DefaultTableModel(  
        		null, ball3);  
        final JTable tableBall3 = new JTable(ball3Model);
        JScrollPane paneBall3 = new JScrollPane();  
        paneBall3.setViewportView(tableBall3);  
        //paneBall3.setLocation(0, 75 + 89);
        paneBall3.setSize(600, 25);
        paneBall3.setLocation(0, 75 + 89 + 25 + 89);
        container.add(paneBall3);
        
        Vector<String> tableColumnNames3 = new Vector<String>();  
        tableColumnNames3.add("种类");  
        tableColumnNames3.add("赔率");
        tableColumnNames3.add("金额");
        tableColumnNames3.add("差值");
        Vector<Vector<String>> tableValues3 = new Vector<Vector<String>>();
        tableValues3.add(new Vector<String>());
        tableValues3.add(new Vector<String>());
        tableValues3.add(new Vector<String>());
        tableValues3.add(new Vector<String>());
        tableValues3.get(0).add("大");
        tableValues3.get(1).add("小");
        tableValues3.get(2).add("单");
        tableValues3.get(3).add("双");
        
        final DefaultTableModel defaultTableModel3 = new DefaultTableModel(  
                tableValues3, tableColumnNames3);  
        table3 = new JTable(defaultTableModel3);  
        table3.setRowHeight(24);
        JScrollPane scrollPane3 = new JScrollPane();  
        scrollPane3.setViewportView(table3);  
        scrollPane3.setSize(600, 89 + 32);
        scrollPane3.setLocation(0, 75 + 89 + 25 + 89 + 25);
        
        container.add(scrollPane3);  
        
        
        //第四球
        Vector<String> ball4 = new Vector<String>();
        ball4.add("第四球");
        final DefaultTableModel ball4Model = new DefaultTableModel(  
        		null, ball4);  
        final JTable tableBall4 = new JTable(ball4Model);
        JScrollPane paneBall4 = new JScrollPane();  
        paneBall4.setViewportView(tableBall4);  
        paneBall4.setSize(600, 25);
        //paneBall4.setLocation(600, 75 + 89);
        paneBall4.setLocation(600, 50);
        container.add(paneBall4);
        
        Vector<String> tableColumnNames4 = new Vector<String>();  
        tableColumnNames4.add("种类");  
        tableColumnNames4.add("赔率");
        tableColumnNames4.add("金额");
        tableColumnNames4.add("差值");
        Vector<Vector<String>> tableValues4 = new Vector<Vector<String>>();
        tableValues4.add(new Vector<String>());
        tableValues4.add(new Vector<String>());
        tableValues4.add(new Vector<String>());
        tableValues4.add(new Vector<String>());
        tableValues4.get(0).add("大");
        tableValues4.get(1).add("小");
        tableValues4.get(2).add("单");
        tableValues4.get(3).add("双");
        
        final DefaultTableModel defaultTableModel4 = new DefaultTableModel(  
                tableValues4, tableColumnNames4);  
        table4 = new JTable(defaultTableModel4);  
        JScrollPane scrollPane4 = new JScrollPane();  
        scrollPane4.setViewportView(table4);  
        scrollPane4.setSize(600, 89);
        //scrollPane4.setLocation(600, 75 + 89 + 25);
        scrollPane4.setLocation(600, 75);
        container.add(scrollPane4);  
        
        
        //第五球
        Vector<String> ball5 = new Vector<String>();
        ball5.add("第五球");
        final DefaultTableModel ball5Model = new DefaultTableModel(  
        		null, ball5);  
        final JTable tableBall5 = new JTable(ball5Model);
        JScrollPane paneBall5 = new JScrollPane();  
        paneBall5.setViewportView(tableBall5);  
        paneBall5.setSize(600, 25);
        //paneBall5.setLocation(0, 75 + 89 + 25 + 89);
        paneBall5.setLocation(600, 75 + 89);
        container.add(paneBall5);
        
        Vector<String> tableColumnNames5 = new Vector<String>();  
        tableColumnNames5.add("种类");  
        tableColumnNames5.add("赔率");
        tableColumnNames5.add("金额");
        tableColumnNames5.add("差值");
        Vector<Vector<String>> tableValues5 = new Vector<Vector<String>>();
        tableValues5.add(new Vector<String>());
        tableValues5.add(new Vector<String>());
        tableValues5.add(new Vector<String>());
        tableValues5.add(new Vector<String>());
        tableValues5.get(0).add("大");
        tableValues5.get(1).add("小");
        tableValues5.get(2).add("单");
        tableValues5.get(3).add("双");
        
        final DefaultTableModel defaultTableModel5 = new DefaultTableModel(  
                tableValues5, tableColumnNames5);
        table5 = new JTable(defaultTableModel5);
        JScrollPane scrollPane5 = new JScrollPane();  
        scrollPane5.setViewportView(table5);  
        scrollPane5.setSize(600, 89);
        //scrollPane5.setLocation(0, 75 + 89 + 25 + 89 + 25);
        scrollPane5.setLocation(600, 75 + 89 + 25);
        container.add(scrollPane5);  
        
        
        //总和
        Vector<String> ball = new Vector<String>();
        ball.add("总和");
        final DefaultTableModel ballModel = new DefaultTableModel(  
        		null, ball);  
        final JTable tableBall = new JTable(ballModel);
        JScrollPane paneBall = new JScrollPane();  
        paneBall.setViewportView(tableBall);  
        paneBall.setSize(600, 25);
        paneBall.setLocation(600, 75 + 89 + 25 + 89);
        container.add(paneBall);
        
        Vector<String> tableColumnNames = new Vector<String>();  
        tableColumnNames.add("种类");  
        tableColumnNames.add("赔率");
        tableColumnNames.add("金额");
        tableColumnNames.add("差值");
        Vector<Vector<String>> tableValues = new Vector<Vector<String>>();
        tableValues.add(new Vector<String>());
        tableValues.add(new Vector<String>());
        tableValues.add(new Vector<String>());
        tableValues.add(new Vector<String>());
        tableValues.add(new Vector<String>());
        tableValues.add(new Vector<String>());
        tableValues.get(0).add("总大");
        tableValues.get(1).add("总小");
        tableValues.get(2).add("总单");
        tableValues.get(3).add("总双");
        tableValues.get(4).add("龙");
        tableValues.get(5).add("虎");
        
        final DefaultTableModel defaultTableModel = new DefaultTableModel(  
                tableValues, tableColumnNames);  
        table = new JTable(defaultTableModel);  
        JScrollPane scrollPane = new JScrollPane();  
        scrollPane.setViewportView(table);  
        scrollPane.setSize(600, 89 + 32);
        scrollPane.setLocation(600, 75 + 89 + 25 + 89 + 25);
        container.add(scrollPane);  
        
        ////////////////////////////////////////
        
        JPanel panel = new JPanel();
        panel.setSize(900, 50);
        panel.setLocation(0, 0);
        container.add(panel);  
        labelA = new JLabel("距封盘: ");   
        textFieldA.setEditable(false);
        JLabel labelB = new JLabel("期数: "); 
        textFieldB.setEditable(false);
        JLabel labelC = new JLabel("数据是否可用: ");
        textFieldC.setEditable(false);
        panel.add(labelA);  
        panel.add(textFieldA);  
        panel.add(labelB);
        panel.add(textFieldB);   
        panel.add(labelC);
        panel.add(textFieldC); 
          
        // make the text field focused every time the window is activated  
        addWindowFocusListener(new WindowAdapter() {  
  
            @Override  
            public void windowGainedFocus(WindowEvent e) {  
                // TODO Auto-generated method stub  
                textFieldA.requestFocus();   
            }  
              
        });  
  
        // **************************************************************  
        // This is a standard snippet to realize the desired column selection as in  
        // Excel  
        // **************************************************************  
  
        // row selection mode  
        
        
        setTitle("重庆时时彩");  
       //pack(); //Realize the components.  
        setBounds(100, 100, 1220, 480);  
//      textFieldA.requestFocus();  
        setLayout(null);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);  
        setVisible(false); //Display the window.  
        table1.setEnabled(false);
        table2.setEnabled(false);
        table3.setEnabled(false);
        table4.setEnabled(false);
        table5.setEnabled(false);
        table.setEnabled(false);
        resetData();
        setTimer(textFieldA);
    }  
    
    private void setTimer(JTextField time) {   
        final JTextField varTime = time;   
        Timer timeAction = new Timer(1000, new ActionListener() {          
            public void actionPerformed(ActionEvent e) {       
                SimpleDateFormat df = new SimpleDateFormat("mm:ss");   
                if(remainTime.get() < 0) {
                	remainTime.set(0);
                }
                varTime.setText(df.format(new Date(remainTime.get())));
                remainTime.set(remainTime.get() - 1000);
            }      
        });            
        timeAction.start();        
    } 
    
    public void setRemainTime(long time) {
    	remainTime.set(time);
    }
    
    public long getRemainTime() {
    	return remainTime.get();
    }
    
    public void setCloseText(boolean close) {
    	if(close) {
    		labelA.setText("已封盘，距开奖:");
    	}
    	else {
    		labelA.setText("距封盘:");
    	}
    }
    
    public void setDrawNumber(String drawNumber) {
    	textFieldB.setText(drawNumber);
    }
    
    public void setSleepTime(long time) {
    	//textFieldC.setText((time/1000 + 3) + "秒");
    }
    
    public void setDataOk(boolean ok) {
    	if(ok) {
    		textFieldC.setText("可用,距封盘" + remainTime.get()/1000 + "秒");
    	} else {
    		textFieldC.setText("不可用");
    	}
    }
    
    public void setData(String[] data) {
    	
    	try{
	    	for(int i = 0; i < data.length; i++) {	
            	JSONArray cqsscLMGrabData = new JSONArray(data[i]);        	
            	JSONArray gamesGrabData = cqsscLMGrabData.getJSONArray(0);        	
            	JSONObject oddsGrabData = cqsscLMGrabData.getJSONObject(1);
  	       	
	        	for(int j = 0; j < gamesGrabData.length(); j++) {
	        		JSONObject gameGrabData = gamesGrabData.getJSONObject(j);
	        		
	    			String game = gameGrabData.getString("k");
	    			String contents = gameGrabData.getString("i");
	    			int amount = gameGrabData.getInt("a");
	    			String oddsKey = game + "_" + contents;
	    			double odds = oddsGrabData.getDouble(oddsKey);
	    			
	    			switch(oddsKey) {
	    				case "DX1_D":
	    					table1.setValueAt(odds, 0, 1);
	    					table1.setValueAt(amount, 0, 2);
	    					break;
	    				case "DX1_X":
	    					table1.setValueAt(odds, 1, 1);
	    					table1.setValueAt(amount, 1, 2);
	    					break;
	    				case "DS1_D":
	    					table1.setValueAt(odds, 2, 1);
	    					table1.setValueAt(amount, 2, 2);
	    					break;
	    				case "DS1_S":
	    					table1.setValueAt(odds, 3, 1);
	    					table1.setValueAt(amount, 3, 2);
	    					break;
	    				case "DX2_D":
	    					table2.setValueAt(odds, 0, 1);
	    					table2.setValueAt(amount, 0, 2);
	    					break;
	    				case "DX2_X":
	    					table2.setValueAt(odds, 1, 1);
	    					table2.setValueAt(amount, 1, 2);
	    					break;
	    				case "DS2_D":
	    					table2.setValueAt(odds, 2, 1);
	    					table2.setValueAt(amount, 2, 2);
	    					break;
	    				case "DS2_S":
	    					table2.setValueAt(odds, 3, 1);
	    					table2.setValueAt(amount, 3, 2);
	    					break;
	    				case "DX3_D":
	    					table3.setValueAt(odds, 0, 1);
	    					table3.setValueAt(amount, 0, 2);
	    					break;
	    				case "DX3_X":
	    					table3.setValueAt(odds, 1, 1);
	    					table3.setValueAt(amount, 1, 2);
	    					break;
	    				case "DS3_D":
	    					table3.setValueAt(odds, 2, 1);
	    					table3.setValueAt(amount, 2, 2);
	    					break;
	    				case "DS3_S":
	    					table3.setValueAt(odds, 3, 1);
	    					table3.setValueAt(amount, 3, 2);
	    					break;
	    				case "DX4_D":
	    					table4.setValueAt(odds, 0, 1);
	    					table4.setValueAt(amount, 0, 2);
	    					break;
	    				case "DX4_X":
	    					table4.setValueAt(odds, 1, 1);
	    					table4.setValueAt(amount, 1, 2);
	    					break;
	    				case "DS4_D":
	    					table4.setValueAt(odds, 2, 1);
	    					table4.setValueAt(amount, 2, 2);
	    					break;
	    				case "DS4_S":
	    					table4.setValueAt(odds, 3, 1);
	    					table4.setValueAt(amount, 3, 2);
	    					break;
	    				case "DX5_D":
	    					table5.setValueAt(odds, 0, 1);
	    					table5.setValueAt(amount, 0, 2);
	    					break;
	    				case "DX5_X":
	    					table5.setValueAt(odds, 1, 1);
	    					table5.setValueAt(amount, 1, 2);
	    					break;
	    				case "DS5_D":
	    					table5.setValueAt(odds, 2, 1);
	    					table5.setValueAt(amount, 2, 2);
	    					break;
	    				case "DS5_S":
	    					table5.setValueAt(odds, 3, 1);
	    					table5.setValueAt(amount, 3, 2);
	    					break;
	    				case "ZDX_D":
	    					table.setValueAt(odds, 0, 1);
	    					table.setValueAt(amount, 0, 2);
	    					break;
	    				case "ZDX_X":
	    					table.setValueAt(odds, 1, 1);
	    					table.setValueAt(amount, 1, 2);
	    					break;
	    				case "ZDS_D":
	    					table.setValueAt(odds, 2, 1);
	    					table.setValueAt(amount, 2, 2);
	    					break;
	    				case "ZDS_S":
	    					table.setValueAt(odds, 3, 1);
	    					table.setValueAt(amount, 3, 2);
	    					break;
	    				case "LH_L":
	    					table.setValueAt(odds, 4, 1);
	    					table.setValueAt(amount, 4, 2);
	    					break;
	    				case "LH_H":
	    					table.setValueAt(odds, 5, 1);
	    					table.setValueAt(amount, 5, 2);
	    					break;
	    			}	
	        	}
	    	}
    	}catch(Exception e) {
    	}
    	
    	//计算差值
    	table1.setValueAt(Long.parseLong(table1.getValueAt(0, 2).toString()) - Long.parseLong(table1.getValueAt(1, 2).toString()), 0, 3);
    	table1.setValueAt(-Long.parseLong(table1.getValueAt(0, 3).toString()), 1, 3);
    	table1.setValueAt(Long.parseLong(table1.getValueAt(2, 2).toString()) - Long.parseLong(table1.getValueAt(3, 2).toString()), 2, 3);
    	table1.setValueAt(-Long.parseLong(table1.getValueAt(2, 3).toString()), 3, 3);
    	
    	table2.setValueAt(Long.parseLong(table2.getValueAt(0, 2).toString()) - Long.parseLong(table2.getValueAt(1, 2).toString()), 0, 3);
    	table2.setValueAt(-Long.parseLong(table2.getValueAt(0, 3).toString()), 1, 3);
    	table2.setValueAt(Long.parseLong(table2.getValueAt(2, 2).toString()) - Long.parseLong(table2.getValueAt(3, 2).toString()), 2, 3);
    	table2.setValueAt(-Long.parseLong(table2.getValueAt(2, 3).toString()), 3, 3);
    	
    	table3.setValueAt(Long.parseLong(table3.getValueAt(0, 2).toString()) - Long.parseLong(table3.getValueAt(1, 2).toString()), 0, 3);
    	table3.setValueAt(-Long.parseLong(table3.getValueAt(0, 3).toString()), 1, 3);
    	table3.setValueAt(Long.parseLong(table3.getValueAt(2, 2).toString()) - Long.parseLong(table3.getValueAt(3, 2).toString()), 2, 3);
    	table3.setValueAt(-Long.parseLong(table3.getValueAt(2, 3).toString()), 3, 3);
    	
    	table4.setValueAt(Long.parseLong(table4.getValueAt(0, 2).toString()) - Long.parseLong(table4.getValueAt(1, 2).toString()), 0, 3);
    	table4.setValueAt(-Long.parseLong(table4.getValueAt(0, 3).toString()), 1, 3);
    	table4.setValueAt(Long.parseLong(table4.getValueAt(2, 2).toString()) - Long.parseLong(table4.getValueAt(3, 2).toString()), 2, 3);
    	table4.setValueAt(-Long.parseLong(table4.getValueAt(2, 3).toString()), 3, 3);
    	
    	table5.setValueAt(Long.parseLong(table5.getValueAt(0, 2).toString()) - Long.parseLong(table5.getValueAt(1, 2).toString()), 0, 3);
    	table5.setValueAt(-Long.parseLong(table5.getValueAt(0, 3).toString()), 1, 3);
    	table5.setValueAt(Long.parseLong(table5.getValueAt(2, 2).toString()) - Long.parseLong(table5.getValueAt(3, 2).toString()), 2, 3);
    	table5.setValueAt(-Long.parseLong(table5.getValueAt(2, 3).toString()), 3, 3);
    	
    	table.setValueAt(Long.parseLong(table.getValueAt(0, 2).toString()) - Long.parseLong(table.getValueAt(1, 2).toString()), 0, 3);
    	table.setValueAt(-Long.parseLong(table.getValueAt(0, 3).toString()), 1, 3);
    	table.setValueAt(Long.parseLong(table.getValueAt(2, 2).toString()) - Long.parseLong(table.getValueAt(3, 2).toString()), 2, 3);
    	table.setValueAt(-Long.parseLong(table.getValueAt(2, 3).toString()), 3, 3);
    	table.setValueAt(Long.parseLong(table.getValueAt(4, 2).toString()) - Long.parseLong(table.getValueAt(5, 2).toString()), 4, 3);
    	table.setValueAt(-Long.parseLong(table.getValueAt(4, 3).toString()), 5, 3);
    }
    
    public void resetData() {
    	for(int i = 0; i < 4; i++) {
    		for(int j = 2; j < 4; j++) {
    			table.setValueAt(0, i, j);
    			table1.setValueAt(0, i, j);
    			table2.setValueAt(0, i, j);
    			table3.setValueAt(0, i, j);
    			table4.setValueAt(0, i, j);
    			table5.setValueAt(0, i, j);
    		}
    	}
    	table.setValueAt(0, 4, 2);
    	table.setValueAt(0, 4, 3);
    	table.setValueAt(0, 5, 2);
    	table.setValueAt(0, 5, 3);
    }
}