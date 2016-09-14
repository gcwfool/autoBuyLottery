import java.awt.Container;  
 
import java.awt.event.ActionEvent;  
import java.awt.event.ActionListener;  
import java.util.Vector;  
  


import javax.swing.JFrame;  
import javax.swing.JLabel;  
import javax.swing.JPanel;  
import javax.swing.JScrollPane;  
import javax.swing.JTable;  
import javax.swing.JTextField;  
import javax.swing.table.DefaultTableModel;  

import java.util.Date;      

import javax.swing.Timer;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.concurrent.atomic.AtomicLong;
   
public class GrabXYNCwindow extends JFrame {  
	private static final long serialVersionUID = -8581492063632813033L;
    final private JTable table1;
    final private JTable table2;
    final private JTable table3;
    final private JTable table4;
    final private JTable table5;
    final private JTable table6;
    final private JTable table7;
    final private JTable table8;
    final private JTable table9;
    
    private JLabel labelA;
    final private JTextField textFieldA = new JTextField(15);  
    final private JTextField textFieldB = new JTextField(15);
    final private JTextField textFieldC = new JTextField(15);
    final private JTextField textFieldD = new JTextField(15);
    private AtomicLong remainTime = new AtomicLong(0);

  //  private long remainTime = 0;
  
    public GrabXYNCwindow() {  
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
        paneBall1.setSize(400, 25);
        paneBall1.setLocation(0, 50);
        container.add(paneBall1);
        
        Vector<String> tableColumnNames1 = new Vector<String>();  
        tableColumnNames1.add("种类");  
        tableColumnNames1.add("赔率");
        tableColumnNames1.add("金额");
        tableColumnNames1.add("差值");
        Vector<Vector<String>> tableValues1 = new Vector<Vector<String>>();
        for(int i = 0; i < 10; i++) {
        	tableValues1.add(new Vector<String>());
        }
        
        tableValues1.get(0).add("大");
        tableValues1.get(1).add("小");
        tableValues1.get(2).add("单");
        tableValues1.get(3).add("双");
        tableValues1.get(4).add("尾大");
        tableValues1.get(5).add("尾小");
        tableValues1.get(6).add("合单");
        tableValues1.get(7).add("合双");
        tableValues1.get(8).add("龙");
        tableValues1.get(9).add("虎");
        
        final DefaultTableModel defaultTableModel1 = new DefaultTableModel(  
                tableValues1, tableColumnNames1);  
        table1 = new JTable(defaultTableModel1);  
        JScrollPane scrollPane1 = new JScrollPane();  
        scrollPane1.setViewportView(table1);  
        //scrollPane1.setLocation(600, 75);
        scrollPane1.setSize(400, 89 + 32 + 64);
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
        //paneBall2.setLocation(0, 75 + 89);
        paneBall2.setSize(400, 25);
        paneBall2.setLocation(400, 50);
        container.add(paneBall2);
        
        Vector<String> tableColumnNames2 = new Vector<String>();  
        tableColumnNames2.add("种类");  
        tableColumnNames2.add("赔率");
        tableColumnNames2.add("金额");
        tableColumnNames2.add("差值");
        Vector<Vector<String>> tableValues2 = new Vector<Vector<String>>();
        for(int i = 0; i < 10; i++) {
        	tableValues2.add(new Vector<String>());
        }
        
        tableValues2.get(0).add("大");
        tableValues2.get(1).add("小");
        tableValues2.get(2).add("单");
        tableValues2.get(3).add("双");
        tableValues2.get(4).add("尾大");
        tableValues2.get(5).add("尾小");
        tableValues2.get(6).add("合单");
        tableValues2.get(7).add("合双");
        tableValues2.get(8).add("龙");
        tableValues2.get(9).add("虎");
        
        
        final DefaultTableModel defaultTableModel2 = new DefaultTableModel(  
                tableValues2, tableColumnNames2);  
        table2 = new JTable(defaultTableModel2);  
        //table2.setRowHeight(24);
        JScrollPane scrollPane2 = new JScrollPane();  
        scrollPane2.setViewportView(table2);  
        scrollPane2.setSize(400, 89 + 32 + 64);
        scrollPane2.setLocation(400, 75);
        
        container.add(scrollPane2);  
        
        
        //第三球
        Vector<String> ball3 = new Vector<String>();
        ball3.add("第三球");
        final DefaultTableModel ball3Model = new DefaultTableModel(  
        		null, ball3);  
        final JTable tableBall3 = new JTable(ball3Model);
        JScrollPane paneBall3 = new JScrollPane();  
        paneBall3.setViewportView(tableBall3);  
        paneBall3.setSize(400, 25);
        //paneBall3.setLocation(600, 75 + 89);
        paneBall3.setLocation(800, 50);
        container.add(paneBall3);
        
        Vector<String> tableColumnNames3 = new Vector<String>();  
        tableColumnNames3.add("种类");  
        tableColumnNames3.add("赔率");
        tableColumnNames3.add("金额");
        tableColumnNames3.add("差值");
        Vector<Vector<String>> tableValues3 = new Vector<Vector<String>>();
        for(int i = 0; i < 10; i++) {
        	tableValues3.add(new Vector<String>());
        }

        tableValues3.get(0).add("大");
        tableValues3.get(1).add("小");
        tableValues3.get(2).add("单");
        tableValues3.get(3).add("双");
        tableValues3.get(4).add("尾大");
        tableValues3.get(5).add("尾小");
        tableValues3.get(6).add("合单");
        tableValues3.get(7).add("合双");
        tableValues3.get(8).add("龙");
        tableValues3.get(9).add("虎");
        
        final DefaultTableModel defaultTableModel3 = new DefaultTableModel(  
                tableValues3, tableColumnNames3);  
        table3 = new JTable(defaultTableModel3);  
        JScrollPane scrollPane3 = new JScrollPane();  
        scrollPane3.setViewportView(table3);  
        scrollPane3.setSize(400, 89 + 32 + 64);
        //scrollPane3.setLocation(600, 75 + 89 + 25);
        scrollPane3.setLocation(800, 75);
        container.add(scrollPane3);  
        
        
        //第四球
        Vector<String> ball4 = new Vector<String>();
        ball4.add("第四球");
        final DefaultTableModel ball4Model = new DefaultTableModel(  
        		null, ball4);  
        final JTable tableBall4 = new JTable(ball4Model);
        JScrollPane paneBall4 = new JScrollPane();  
        paneBall4.setViewportView(tableBall4);  
        paneBall4.setSize(400, 25);
        //paneBall4.setLocation(0, 75 + 89 + 25 + 89);
        paneBall4.setLocation(0, 89 + 32 + 75 + 64);
        container.add(paneBall4);
        
        Vector<String> tableColumnNames4 = new Vector<String>();  
        tableColumnNames4.add("种类");  
        tableColumnNames4.add("赔率");
        tableColumnNames4.add("金额");
        tableColumnNames4.add("差值");
        Vector<Vector<String>> tableValues4 = new Vector<Vector<String>>();
        for(int i = 0; i < 10; i++) {
        	tableValues4.add(new Vector<String>());
        }

        tableValues4.get(0).add("大");
        tableValues4.get(1).add("小");
        tableValues4.get(2).add("单");
        tableValues4.get(3).add("双");
        tableValues4.get(4).add("尾大");
        tableValues4.get(5).add("尾小");
        tableValues4.get(6).add("合单");
        tableValues4.get(7).add("合双");
        tableValues4.get(8).add("龙");
        tableValues4.get(9).add("虎");
        
        final DefaultTableModel defaultTableModel4 = new DefaultTableModel(  
                tableValues4, tableColumnNames4);
        table4 = new JTable(defaultTableModel4);
        JScrollPane scrollPane4 = new JScrollPane();  
        scrollPane4.setViewportView(table4);  
        scrollPane4.setSize(400, 89 + 32 + 64);
        //scrollPane4.setLocation(0, 75 + 89 + 25 + 89 + 25);
        scrollPane4.setLocation(0, 89 + 32 + 100 + 64);
        container.add(scrollPane4);  
        
        
        //第五球
        Vector<String> ball5 = new Vector<String>();
        ball5.add("第五球");
        final DefaultTableModel ball5Model = new DefaultTableModel(  
        		null, ball5);  
        final JTable tableBall5 = new JTable(ball5Model);
        JScrollPane paneBall5 = new JScrollPane();  
        paneBall5.setViewportView(tableBall5);  
        paneBall5.setSize(400, 25);
        paneBall5.setLocation(400, 89 + 32 + 75 + 64);
        container.add(paneBall5);
        
        Vector<String> tableColumnNames5 = new Vector<String>();  
        tableColumnNames5.add("种类");  
        tableColumnNames5.add("赔率");
        tableColumnNames5.add("金额");
        tableColumnNames5.add("差值");
        Vector<Vector<String>> tableValues5 = new Vector<Vector<String>>();
        for(int i = 0; i < 8; i++) {
        	tableValues5.add(new Vector<String>());
        }

        tableValues5.get(0).add("大");
        tableValues5.get(1).add("小");
        tableValues5.get(2).add("单");
        tableValues5.get(3).add("双");
        tableValues5.get(4).add("尾大");
        tableValues5.get(5).add("尾小");
        tableValues5.get(6).add("合单");
        tableValues5.get(7).add("合双");
        
        final DefaultTableModel defaultTableModel5 = new DefaultTableModel(  
                tableValues5, tableColumnNames5);  
        table5 = new JTable(defaultTableModel5);  
        table5.setRowHeight(20);
        JScrollPane scrollPane5 = new JScrollPane();  
        scrollPane5.setViewportView(table5);  
        scrollPane5.setSize(400, 89 + 32 + 64);
        scrollPane5.setLocation(400, 89 + 32 + 100 + 64);
        container.add(scrollPane5);  
        
        //第六球
        Vector<String> ball6 = new Vector<String>();
        ball6.add("第六球");
        final DefaultTableModel ball6Model = new DefaultTableModel(  
        		null, ball6);  
        final JTable tableBall6 = new JTable(ball6Model);
        JScrollPane paneBall6 = new JScrollPane();  
        paneBall6.setViewportView(tableBall6);  
        paneBall6.setSize(400, 25);
        paneBall6.setLocation(800, 89 + 32 + 75 + 64);
        container.add(paneBall6);
        
        Vector<String> tableColumnNames6 = new Vector<String>();  
        tableColumnNames6.add("种类");  
        tableColumnNames6.add("赔率");
        tableColumnNames6.add("金额");
        tableColumnNames6.add("差值");
        Vector<Vector<String>> tableValues6 = new Vector<Vector<String>>();
        for(int i = 0; i < 8; i++) {
        	tableValues6.add(new Vector<String>());
        }
        
        tableValues6.get(0).add("大");
        tableValues6.get(1).add("小");
        tableValues6.get(2).add("单");
        tableValues6.get(3).add("双");
        tableValues6.get(4).add("尾大");
        tableValues6.get(5).add("尾小");
        tableValues6.get(6).add("合单");
        tableValues6.get(7).add("合双");
        
        final DefaultTableModel defaultTableModel6 = new DefaultTableModel(  
                tableValues6, tableColumnNames6);  
        table6 = new JTable(defaultTableModel6);
        table6.setRowHeight(20);
        JScrollPane scrollPane6 = new JScrollPane();  
        scrollPane6.setViewportView(table6);
        scrollPane6.setSize(400, 89 + 32 + 64);
        scrollPane6.setLocation(800, 89 + 32 + 100 + 64);
        container.add(scrollPane6); 
        
        //第七球
        Vector<String> ball7 = new Vector<String>();
        ball7.add("第七球");
        final DefaultTableModel ball7Model = new DefaultTableModel(  
        		null, ball7);  
        final JTable tableBall7 = new JTable(ball7Model);
        JScrollPane paneBall7 = new JScrollPane();  
        paneBall7.setViewportView(tableBall7);  
        paneBall7.setSize(400, 25);
        paneBall7.setLocation(0, 89 + 32 + 100 + 89 + 32 + 128);
        container.add(paneBall7);
        
        Vector<String> tableColumnNames7 = new Vector<String>();  
        tableColumnNames7.add("种类");  
        tableColumnNames7.add("赔率");
        tableColumnNames7.add("金额");
        tableColumnNames7.add("差值");
        Vector<Vector<String>> tableValues7 = new Vector<Vector<String>>();
        for(int i = 0; i < 8; i++) {
        	tableValues7.add(new Vector<String>());
        }
        
        tableValues7.get(0).add("大");
        tableValues7.get(1).add("小");
        tableValues7.get(2).add("单");
        tableValues7.get(3).add("双");
        tableValues7.get(4).add("尾大");
        tableValues7.get(5).add("尾小");
        tableValues7.get(6).add("合单");
        tableValues7.get(7).add("合双");

        
        final DefaultTableModel defaultTableModel7 = new DefaultTableModel(  
                tableValues7, tableColumnNames7);  
        table7 = new JTable(defaultTableModel7);  
        JScrollPane scrollPane7 = new JScrollPane();  
        scrollPane7.setViewportView(table7);  
        scrollPane7.setSize(400, 89 + 64);
        scrollPane7.setLocation(0, 89 + 32 + 100 + 89 + 32 + 25 + 128);
        container.add(scrollPane7);  
        
        //第八球
        Vector<String> ball8 = new Vector<String>();
        ball8.add("第八球");
        final DefaultTableModel ball8Model = new DefaultTableModel(  
        		null, ball8);  
        final JTable tableBall8 = new JTable(ball8Model);
        JScrollPane paneBall8 = new JScrollPane();  
        paneBall8.setViewportView(tableBall8);  
        paneBall8.setSize(400, 25);
        paneBall8.setLocation(400, 89 + 32 + 100 + 89 + 32 + 128);
        container.add(paneBall8);
        
        Vector<String> tableColumnNames8 = new Vector<String>();  
        tableColumnNames8.add("种类");  
        tableColumnNames8.add("赔率");
        tableColumnNames8.add("金额");
        tableColumnNames8.add("差值");
        Vector<Vector<String>> tableValues8 = new Vector<Vector<String>>();
        for(int i = 0; i < 8; i++) {
        	tableValues8.add(new Vector<String>());
        }

        tableValues8.get(0).add("大");
        tableValues8.get(1).add("小");
        tableValues8.get(2).add("单");
        tableValues8.get(3).add("双");
        tableValues8.get(4).add("尾大");
        tableValues8.get(5).add("尾小");
        tableValues8.get(6).add("合单");
        tableValues8.get(7).add("合双");
        
        final DefaultTableModel defaultTableModel8 = new DefaultTableModel(  
                tableValues8, tableColumnNames8);  
        table8 = new JTable(defaultTableModel8);  
        JScrollPane scrollPane8 = new JScrollPane();  
        scrollPane8.setViewportView(table8);  
        scrollPane8.setSize(400, 89 + 64);
        scrollPane8.setLocation(400, 89 + 32 + 100 + 89 + 32 + 25 + 128);
        container.add(scrollPane8);  
        
        //总和、正码
        Vector<String> ball9 = new Vector<String>();
        ball9.add("总和、正码");
        final DefaultTableModel ball9Model = new DefaultTableModel(  
        		null, ball9);  
        final JTable tableBall9 = new JTable(ball9Model);
        JScrollPane paneBall9 = new JScrollPane();  
        paneBall9.setViewportView(tableBall9);  
        paneBall9.setSize(400, 25);
        paneBall9.setLocation(800, 89 + 32 + 100 + 89 + 32 + 128);
        container.add(paneBall9);
        
        Vector<String> tableColumnNames9 = new Vector<String>();  
        tableColumnNames9.add("种类");  
        tableColumnNames9.add("赔率");
        tableColumnNames9.add("金额");
        tableColumnNames9.add("差值");
        Vector<Vector<String>> tableValues9 = new Vector<Vector<String>>();
        for(int i = 0; i < 6; i++) {
        	tableValues9.add(new Vector<String>());
        }

        tableValues9.get(0).add("大");
        tableValues9.get(1).add("小");
        tableValues9.get(2).add("单");
        tableValues9.get(3).add("双");
        tableValues9.get(4).add("尾大");
        tableValues9.get(5).add("尾小");

        final DefaultTableModel defaultTableModel9 = new DefaultTableModel(  
                tableValues9, tableColumnNames9);  
        table9 = new JTable(defaultTableModel9);  
        JScrollPane scrollPane9 = new JScrollPane();  
        scrollPane9.setViewportView(table9);  
        scrollPane9.setSize(400, 89 + 64);
        scrollPane9.setLocation(800, 89 + 32 + 100 + 89 + 32 + 25 + 128);
        container.add(scrollPane9);  
        
        ////////////////////////////////////////
        
        JPanel panel = new JPanel();
        panel.setSize(1000, 50);
        panel.setLocation(0, 0);
        container.add(panel);  
        labelA = new JLabel("距封盘: ");   
        textFieldA.setEditable(false);
        JLabel labelB = new JLabel("期数: "); 
        textFieldB.setEditable(false);
        JLabel labelC = new JLabel("在线状态: ");
        textFieldC.setEditable(false);
        textFieldC.setText("正常在线");
        JLabel labelD = new JLabel("差值总额: ");
        textFieldD.setEditable(false);
        panel.add(labelA);  
        panel.add(textFieldA);  
        panel.add(labelB);
        panel.add(textFieldB); 
        panel.add(labelC);
        panel.add(textFieldC);
        panel.add(labelD);
        panel.add(textFieldD); 
        
        setTitle("幸运农场");  
       //pack(); //Realize the components.  
        setBounds(100, 100, 1220, 700);  
//      textFieldA.requestFocus();  
        setLayout(null);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);  
        setVisible(false); //Display the window. 
        //table1.setEnabled(false);
        table2.setEnabled(false);
        table3.setEnabled(false);
        table4.setEnabled(false);
        table5.setEnabled(false);
        table6.setEnabled(false);
        table7.setEnabled(false);
        table8.setEnabled(false);
        table9.setEnabled(false);
        resetData();
        setTimer(textFieldA);
    }  
    
    private void setTimer(JTextField time){   
        final JTextField varTime = time;   
        Timer timeAction = new Timer(1000, new ActionListener() {          
            public void actionPerformed(ActionEvent e) {       
                SimpleDateFormat df = new SimpleDateFormat("mm:ss");   
                if(remainTime.get() < 0) {
                	remainTime.set(0);;
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
    
    public void setOnlineStatus(boolean online) {
    	if(online) {
    		textFieldC.setText("正常在线");
    	} else {
    		textFieldC.setText("网络连接断开");
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
	    				case "WDX1_D":
	    					table1.setValueAt(odds, 4, 1);
	    					table1.setValueAt(amount,4, 2);
	    					break;
	    				case "WDX1_X":
	    					table1.setValueAt(odds, 5, 1);
	    					table1.setValueAt(amount, 5, 2);
	    					break;
	    				case "HDS1_D":
	    					table1.setValueAt(odds, 6, 1);
	    					table1.setValueAt(amount, 6, 2);
	    					break;
	    				case "HDS1_S":
	    					table1.setValueAt(odds, 7, 1);
	    					table1.setValueAt(amount, 7, 2);
	    					break;
	    				case "LH1_L":
	    					table1.setValueAt(odds, 8, 1);
	    					table1.setValueAt(amount, 8, 2);
	    					break;
	    				case "LH1_H":
	    					table1.setValueAt(odds, 9, 1);
	    					table1.setValueAt(amount, 9, 2);
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
	    				case "WDX2_D":
	    					table2.setValueAt(odds, 4, 1);
	    					table2.setValueAt(amount, 4, 2);
	    					break;
	    				case "WDX2_X":
	    					table2.setValueAt(odds, 5, 1);
	    					table2.setValueAt(amount, 5, 2);
	    					break;
	    				case "HDS2_D":
	    					table2.setValueAt(odds, 6, 1);
	    					table2.setValueAt(amount, 6, 2);
	    					break;
	    				case "HDS2_S":
	    					table2.setValueAt(odds, 7, 1);
	    					table2.setValueAt(amount, 7, 2);
	    					break;
	    				case "LH2_L":
	    					table2.setValueAt(odds, 8, 1);
	    					table2.setValueAt(amount, 8, 2);
	    					break;
	    				case "LH2_H":
	    					table2.setValueAt(odds, 9, 1);
	    					table2.setValueAt(amount, 9, 2);
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
	    				case "WDX3_D":
	    					table3.setValueAt(odds, 4, 1);
	    					table3.setValueAt(amount, 4, 2);
	    					break;
	    				case "WDX3_X":
	    					table3.setValueAt(odds, 5, 1);
	    					table3.setValueAt(amount, 5, 2);
	    					break;
	    				case "HDS3_D":
	    					table3.setValueAt(odds, 6, 1);
	    					table3.setValueAt(amount, 6, 2);
	    					break;
	    				case "HDS3_S":
	    					table3.setValueAt(odds, 7, 1);
	    					table3.setValueAt(amount, 7, 2);
	    					break;
	    				case "LH3_L":
	    					table3.setValueAt(odds, 8, 1);
	    					table3.setValueAt(amount, 8, 2);
	    					break;
	    				case "LH3_H":
	    					table3.setValueAt(odds, 9, 1);
	    					table3.setValueAt(amount, 9, 2);
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
	    				case "WDX4_D":
	    					table4.setValueAt(odds, 4, 1);
	    					table4.setValueAt(amount, 4, 2);
	    					break;
	    				case "WDX4_X":
	    					table4.setValueAt(odds, 5, 1);
	    					table4.setValueAt(amount, 5, 2);
	    					break;
	    				case "HDS4_D":
	    					table4.setValueAt(odds, 6, 1);
	    					table4.setValueAt(amount, 6, 2);
	    					break;
	    				case "HDS4_S":
	    					table4.setValueAt(odds, 7, 1);
	    					table4.setValueAt(amount, 7, 2);
	    					break;
	    				case "LH4_L":
	    					table4.setValueAt(odds, 8, 1);
	    					table4.setValueAt(amount, 8, 2);
	    					break;
	    				case "LH4_H":
	    					table4.setValueAt(odds, 9, 1);
	    					table4.setValueAt(amount, 9, 2);
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
	    				case "WDX5_D":
	    					table5.setValueAt(odds, 4, 1);
	    					table5.setValueAt(amount, 4, 2);
	    					break;
	    				case "WDX5_X":
	    					table5.setValueAt(odds, 5, 1);
	    					table5.setValueAt(amount, 5, 2);
	    					break;
	    				case "HDS5_D":
	    					table5.setValueAt(odds, 6, 1);
	    					table5.setValueAt(amount, 6, 2);
	    					break;
	    				case "HDS5_S":
	    					table5.setValueAt(odds, 7, 1);
	    					table5.setValueAt(amount, 7, 2);
	    					break;
	    				case "DX6_D":
	    					table6.setValueAt(odds, 0, 1);
	    					table6.setValueAt(amount, 0, 2);
	    					break;
	    				case "DX6_X":
	    					table6.setValueAt(odds, 1, 1);
	    					table6.setValueAt(amount, 1, 2);
	    					break;
	    				case "DS6_D":
	    					table6.setValueAt(odds, 2, 1);
	    					table6.setValueAt(amount, 2, 2);
	    					break;
	    				case "DS6_S":
	    					table6.setValueAt(odds, 3, 1);
	    					table6.setValueAt(amount, 3, 2);
	    					break;
	    				case "WDX6_D":
	    					table6.setValueAt(odds, 4, 1);
	    					table6.setValueAt(amount, 4, 2);
	    					break;
	    				case "WDX6_X":
	    					table6.setValueAt(odds, 5, 1);
	    					table6.setValueAt(amount, 5, 2);
	    					break;
	    				case "HDS6_D":
	    					table6.setValueAt(odds, 6, 1);
	    					table6.setValueAt(amount, 6, 2);
	    					break;
	    				case "HDS6_S":
	    					table6.setValueAt(odds, 7, 1);
	    					table6.setValueAt(amount, 7, 2);
	    					break;
	    				case "DX7_D":
	    					table7.setValueAt(odds, 0, 1);
	    					table7.setValueAt(amount, 0, 2);
	    					break;
	    				case "DX7_X":
	    					table7.setValueAt(odds, 1, 1);
	    					table7.setValueAt(amount, 1, 2);
	    					break;
	    				case "DS7_D":
	    					table7.setValueAt(odds, 2, 1);
	    					table7.setValueAt(amount, 2, 2);
	    					break;
	    				case "DS7_S":
	    					table7.setValueAt(odds, 3, 1);
	    					table7.setValueAt(amount, 3, 2);
	    					break;
	    				case "WDX7_D":
	    					table7.setValueAt(odds, 4, 1);
	    					table7.setValueAt(amount, 4, 2);
	    					break;
	    				case "WDX7_X":
	    					table7.setValueAt(odds, 5, 1);
	    					table7.setValueAt(amount, 5, 2);
	    					break;
	    				case "HDS7_D":
	    					table7.setValueAt(odds, 6, 1);
	    					table7.setValueAt(amount, 6, 2);
	    					break;
	    				case "HDS7_S":
	    					table7.setValueAt(odds, 7, 1);
	    					table7.setValueAt(amount, 7, 2);
	    					break;
	    				case "DX8_D":
	    					table8.setValueAt(odds, 0, 1);
	    					table8.setValueAt(amount, 0, 2);
	    					break;
	    				case "DX8_X":
	    					table8.setValueAt(odds, 1, 1);
	    					table8.setValueAt(amount, 1, 2);
	    					break;
	    				case "DS8_D":
	    					table8.setValueAt(odds, 2, 1);
	    					table8.setValueAt(amount, 2, 2);
	    					break;
	    				case "DS8_S":
	    					table8.setValueAt(odds, 3, 1);
	    					table8.setValueAt(amount, 3, 2);
	    					break;
	    				case "WDX8_D":
	    					table8.setValueAt(odds, 4, 1);
	    					table8.setValueAt(amount, 4, 2);
	    					break;
	    				case "WDX8_X":
	    					table8.setValueAt(odds, 5, 1);
	    					table8.setValueAt(amount, 5, 2);
	    					break;
	    				case "HDS8_D":
	    					table8.setValueAt(odds, 6, 1);
	    					table8.setValueAt(amount, 6, 2);
	    					break;
	    				case "HDS8_S":
	    					table8.setValueAt(odds, 7, 1);
	    					table8.setValueAt(amount, 7, 2);
	    					break;
	    				case "ZDX_D":
	    					table9.setValueAt(odds, 0, 1);
	    					table9.setValueAt(amount, 0, 2);
	    					break;
	    				case "ZDX_X":
	    					table9.setValueAt(odds, 1, 1);
	    					table9.setValueAt(amount, 1, 2);
	    					break;
	    				case "ZDS_D":
	    					table9.setValueAt(odds, 2, 1);
	    					table9.setValueAt(amount, 2, 2);
	    					break;
	    				case "ZDS_S":
	    					table9.setValueAt(odds, 3, 1);
	    					table9.setValueAt(amount, 3, 2);
	    					break;
	    				case "ZWDX_D":
	    					table9.setValueAt(odds, 4, 1);
	    					table9.setValueAt(amount, 4, 2);
	    					break;
	    				case "ZWDX_X":
	    					table9.setValueAt(odds, 5, 1);
	    					table9.setValueAt(amount, 5, 2);
	    					break;
	    			}	
	        	}
	    	}
    	}catch(Exception e) {
    		System.out.println("重庆农场设置界面数据错误");
    	}
    	
    	long totalValue = 0;
    	long tmpValue = 0;
    	table1.setValueAt(Long.parseLong(table1.getValueAt(0, 2).toString()) - Long.parseLong(table1.getValueAt(1, 2).toString()), 0, 3);
    	table1.setValueAt(-Long.parseLong(table1.getValueAt(0, 3).toString()), 1, 3);
    	table1.setValueAt(Long.parseLong(table1.getValueAt(2, 2).toString()) - Long.parseLong(table1.getValueAt(3, 2).toString()), 2, 3);
    	table1.setValueAt(-Long.parseLong(table1.getValueAt(2, 3).toString()), 3, 3);
    	table1.setValueAt(Long.parseLong(table1.getValueAt(4, 2).toString()) - Long.parseLong(table1.getValueAt(5, 2).toString()), 4, 3);
    	table1.setValueAt(-Long.parseLong(table1.getValueAt(4, 3).toString()), 5, 3);
    	table1.setValueAt(Long.parseLong(table1.getValueAt(6, 2).toString()) - Long.parseLong(table1.getValueAt(7, 2).toString()), 6, 3);
    	table1.setValueAt(-Long.parseLong(table1.getValueAt(6, 3).toString()), 7, 3);
    	table1.setValueAt(Long.parseLong(table1.getValueAt(8, 2).toString()) - Long.parseLong(table1.getValueAt(9, 2).toString()), 8, 3);
    	table1.setValueAt(-Long.parseLong(table1.getValueAt(8, 3).toString()), 9, 3);
    	
    	tmpValue = Long.parseLong(table1.getValueAt(0, 3).toString());
    	totalValue += tmpValue >= 0 ? tmpValue : -tmpValue;
    	tmpValue = Long.parseLong(table1.getValueAt(2, 3).toString());
    	totalValue += tmpValue >= 0 ? tmpValue : -tmpValue;
    	tmpValue = Long.parseLong(table1.getValueAt(4, 3).toString());
    	totalValue += tmpValue >= 0 ? tmpValue : -tmpValue;
    	tmpValue = Long.parseLong(table1.getValueAt(6, 3).toString());
    	totalValue += tmpValue >= 0 ? tmpValue : -tmpValue;
    	tmpValue = Long.parseLong(table1.getValueAt(8, 3).toString());
    	totalValue += tmpValue >= 0 ? tmpValue : -tmpValue;
    	
    	
    	table2.setValueAt(Long.parseLong(table2.getValueAt(0, 2).toString()) - Long.parseLong(table2.getValueAt(1, 2).toString()), 0, 3);
    	table2.setValueAt(-Long.parseLong(table2.getValueAt(0, 3).toString()), 1, 3);
    	table2.setValueAt(Long.parseLong(table2.getValueAt(2, 2).toString()) - Long.parseLong(table2.getValueAt(3, 2).toString()), 2, 3);
    	table2.setValueAt(-Long.parseLong(table2.getValueAt(2, 3).toString()), 3, 3);
    	table2.setValueAt(Long.parseLong(table2.getValueAt(4, 2).toString()) - Long.parseLong(table2.getValueAt(5, 2).toString()), 4, 3);
    	table2.setValueAt(-Long.parseLong(table2.getValueAt(4, 3).toString()), 5, 3);
    	table2.setValueAt(Long.parseLong(table2.getValueAt(6, 2).toString()) - Long.parseLong(table2.getValueAt(7, 2).toString()), 6, 3);
    	table2.setValueAt(-Long.parseLong(table2.getValueAt(6, 3).toString()), 7, 3);
    	table2.setValueAt(Long.parseLong(table2.getValueAt(8, 2).toString()) - Long.parseLong(table2.getValueAt(9, 2).toString()), 8, 3);
    	table2.setValueAt(-Long.parseLong(table2.getValueAt(8, 3).toString()), 9, 3);
    	
    	tmpValue = Long.parseLong(table2.getValueAt(0, 3).toString());
    	totalValue += tmpValue >= 0 ? tmpValue : -tmpValue;
    	tmpValue = Long.parseLong(table2.getValueAt(2, 3).toString());
    	totalValue += tmpValue >= 0 ? tmpValue : -tmpValue;
    	tmpValue = Long.parseLong(table2.getValueAt(4, 3).toString());
    	totalValue += tmpValue >= 0 ? tmpValue : -tmpValue;
    	tmpValue = Long.parseLong(table2.getValueAt(6, 3).toString());
    	totalValue += tmpValue >= 0 ? tmpValue : -tmpValue;
    	tmpValue = Long.parseLong(table2.getValueAt(8, 3).toString());
    	totalValue += tmpValue >= 0 ? tmpValue : -tmpValue;
    	
    	
    	table3.setValueAt(Long.parseLong(table3.getValueAt(0, 2).toString()) - Long.parseLong(table3.getValueAt(1, 2).toString()), 0, 3);
    	table3.setValueAt(-Long.parseLong(table3.getValueAt(0, 3).toString()), 1, 3);
    	table3.setValueAt(Long.parseLong(table3.getValueAt(2, 2).toString()) - Long.parseLong(table3.getValueAt(3, 2).toString()), 2, 3);
    	table3.setValueAt(-Long.parseLong(table3.getValueAt(2, 3).toString()), 3, 3);
    	table3.setValueAt(Long.parseLong(table3.getValueAt(4, 2).toString()) - Long.parseLong(table3.getValueAt(5, 2).toString()), 4, 3);
    	table3.setValueAt(-Long.parseLong(table3.getValueAt(4, 3).toString()), 5, 3);
    	table3.setValueAt(Long.parseLong(table3.getValueAt(6, 2).toString()) - Long.parseLong(table3.getValueAt(7, 2).toString()), 6, 3);
    	table3.setValueAt(-Long.parseLong(table3.getValueAt(6, 3).toString()), 7, 3);
    	table3.setValueAt(Long.parseLong(table3.getValueAt(8, 2).toString()) - Long.parseLong(table3.getValueAt(9, 2).toString()), 8, 3);
    	table3.setValueAt(-Long.parseLong(table3.getValueAt(8, 3).toString()), 9, 3);
    	
    	tmpValue = Long.parseLong(table3.getValueAt(0, 3).toString());
    	totalValue += tmpValue >= 0 ? tmpValue : -tmpValue;
    	tmpValue = Long.parseLong(table3.getValueAt(2, 3).toString());
    	totalValue += tmpValue >= 0 ? tmpValue : -tmpValue;
    	tmpValue = Long.parseLong(table3.getValueAt(4, 3).toString());
    	totalValue += tmpValue >= 0 ? tmpValue : -tmpValue;
    	tmpValue = Long.parseLong(table3.getValueAt(6, 3).toString());
    	totalValue += tmpValue >= 0 ? tmpValue : -tmpValue;
    	tmpValue = Long.parseLong(table3.getValueAt(8, 3).toString());
    	totalValue += tmpValue >= 0 ? tmpValue : -tmpValue;
    	
    	
    	table4.setValueAt(Long.parseLong(table4.getValueAt(0, 2).toString()) - Long.parseLong(table4.getValueAt(1, 2).toString()), 0, 3);
    	table4.setValueAt(-Long.parseLong(table4.getValueAt(0, 3).toString()), 1, 3);
    	table4.setValueAt(Long.parseLong(table4.getValueAt(2, 2).toString()) - Long.parseLong(table4.getValueAt(3, 2).toString()), 2, 3);
    	table4.setValueAt(-Long.parseLong(table4.getValueAt(2, 3).toString()), 3, 3);
    	table4.setValueAt(Long.parseLong(table4.getValueAt(4, 2).toString()) - Long.parseLong(table4.getValueAt(5, 2).toString()), 4, 3);
    	table4.setValueAt(-Long.parseLong(table4.getValueAt(4, 3).toString()), 5, 3);
    	table4.setValueAt(Long.parseLong(table4.getValueAt(6, 2).toString()) - Long.parseLong(table4.getValueAt(7, 2).toString()), 6, 3);
    	table4.setValueAt(-Long.parseLong(table4.getValueAt(6, 3).toString()), 7, 3);
    	table4.setValueAt(Long.parseLong(table4.getValueAt(8, 2).toString()) - Long.parseLong(table4.getValueAt(9, 2).toString()), 8, 3);
    	table4.setValueAt(-Long.parseLong(table4.getValueAt(8, 3).toString()), 9, 3);
    	
    	tmpValue = Long.parseLong(table4.getValueAt(0, 3).toString());
    	totalValue += tmpValue >= 0 ? tmpValue : -tmpValue;
    	tmpValue = Long.parseLong(table4.getValueAt(2, 3).toString());
    	totalValue += tmpValue >= 0 ? tmpValue : -tmpValue;
    	tmpValue = Long.parseLong(table4.getValueAt(4, 3).toString());
    	totalValue += tmpValue >= 0 ? tmpValue : -tmpValue;
    	tmpValue = Long.parseLong(table4.getValueAt(6, 3).toString());
    	totalValue += tmpValue >= 0 ? tmpValue : -tmpValue;
    	tmpValue = Long.parseLong(table4.getValueAt(8, 3).toString());
    	totalValue += tmpValue >= 0 ? tmpValue : -tmpValue;
    	
    	table5.setValueAt(Long.parseLong(table5.getValueAt(0, 2).toString()) - Long.parseLong(table5.getValueAt(1, 2).toString()), 0, 3);
    	table5.setValueAt(-Long.parseLong(table5.getValueAt(0, 3).toString()), 1, 3);
    	table5.setValueAt(Long.parseLong(table5.getValueAt(2, 2).toString()) - Long.parseLong(table5.getValueAt(3, 2).toString()), 2, 3);
    	table5.setValueAt(-Long.parseLong(table5.getValueAt(2, 3).toString()), 3, 3);
    	table5.setValueAt(Long.parseLong(table5.getValueAt(4, 2).toString()) - Long.parseLong(table5.getValueAt(5, 2).toString()), 4, 3);
    	table5.setValueAt(-Long.parseLong(table5.getValueAt(4, 3).toString()), 5, 3);
    	table5.setValueAt(Long.parseLong(table5.getValueAt(6, 2).toString()) - Long.parseLong(table5.getValueAt(7, 2).toString()), 6, 3);
    	table5.setValueAt(-Long.parseLong(table5.getValueAt(6, 3).toString()), 7, 3);
    	
    	tmpValue = Long.parseLong(table5.getValueAt(0, 3).toString());
    	totalValue += tmpValue >= 0 ? tmpValue : -tmpValue;
    	tmpValue = Long.parseLong(table5.getValueAt(2, 3).toString());
    	totalValue += tmpValue >= 0 ? tmpValue : -tmpValue;
    	tmpValue = Long.parseLong(table5.getValueAt(4, 3).toString());
    	totalValue += tmpValue >= 0 ? tmpValue : -tmpValue;
    	tmpValue = Long.parseLong(table5.getValueAt(6, 3).toString());
    	totalValue += tmpValue >= 0 ? tmpValue : -tmpValue;
    	
    	table6.setValueAt(Long.parseLong(table6.getValueAt(0, 2).toString()) - Long.parseLong(table6.getValueAt(1, 2).toString()), 0, 3);
    	table6.setValueAt(-Long.parseLong(table6.getValueAt(0, 3).toString()), 1, 3);
    	table6.setValueAt(Long.parseLong(table6.getValueAt(2, 2).toString()) - Long.parseLong(table6.getValueAt(3, 2).toString()), 2, 3);
    	table6.setValueAt(-Long.parseLong(table6.getValueAt(2, 3).toString()), 3, 3);
    	table6.setValueAt(Long.parseLong(table6.getValueAt(4, 2).toString()) - Long.parseLong(table6.getValueAt(5, 2).toString()), 4, 3);
    	table6.setValueAt(-Long.parseLong(table6.getValueAt(4, 3).toString()), 5, 3);
    	table6.setValueAt(Long.parseLong(table6.getValueAt(6, 2).toString()) - Long.parseLong(table6.getValueAt(7, 2).toString()), 6, 3);
    	table6.setValueAt(-Long.parseLong(table6.getValueAt(6, 3).toString()), 7, 3);
    	
    	tmpValue = Long.parseLong(table6.getValueAt(0, 3).toString());
    	totalValue += tmpValue >= 0 ? tmpValue : -tmpValue;
    	tmpValue = Long.parseLong(table6.getValueAt(2, 3).toString());
    	totalValue += tmpValue >= 0 ? tmpValue : -tmpValue;
    	tmpValue = Long.parseLong(table6.getValueAt(4, 3).toString());
    	totalValue += tmpValue >= 0 ? tmpValue : -tmpValue;
    	tmpValue = Long.parseLong(table6.getValueAt(6, 3).toString());
    	totalValue += tmpValue >= 0 ? tmpValue : -tmpValue;
    	
    	table7.setValueAt(Long.parseLong(table7.getValueAt(0, 2).toString()) - Long.parseLong(table7.getValueAt(1, 2).toString()), 0, 3);
    	table7.setValueAt(-Long.parseLong(table7.getValueAt(0, 3).toString()), 1, 3);
    	table7.setValueAt(Long.parseLong(table7.getValueAt(2, 2).toString()) - Long.parseLong(table7.getValueAt(3, 2).toString()), 2, 3);
    	table7.setValueAt(-Long.parseLong(table7.getValueAt(2, 3).toString()), 3, 3);
    	table7.setValueAt(Long.parseLong(table7.getValueAt(4, 2).toString()) - Long.parseLong(table7.getValueAt(5, 2).toString()), 4, 3);
    	table7.setValueAt(-Long.parseLong(table7.getValueAt(4, 3).toString()), 5, 3);
    	table7.setValueAt(Long.parseLong(table7.getValueAt(6, 2).toString()) - Long.parseLong(table7.getValueAt(7, 2).toString()), 6, 3);
    	table7.setValueAt(-Long.parseLong(table7.getValueAt(6, 3).toString()), 7, 3);
    	
    	tmpValue = Long.parseLong(table7.getValueAt(0, 3).toString());
    	totalValue += tmpValue >= 0 ? tmpValue : -tmpValue;
    	tmpValue = Long.parseLong(table7.getValueAt(2, 3).toString());
    	totalValue += tmpValue >= 0 ? tmpValue : -tmpValue;
    	tmpValue = Long.parseLong(table7.getValueAt(4, 3).toString());
    	totalValue += tmpValue >= 0 ? tmpValue : -tmpValue;
    	tmpValue = Long.parseLong(table7.getValueAt(6, 3).toString());
    	totalValue += tmpValue >= 0 ? tmpValue : -tmpValue;
    	
    	
    	table8.setValueAt(Long.parseLong(table8.getValueAt(0, 2).toString()) - Long.parseLong(table8.getValueAt(1, 2).toString()), 0, 3);
    	table8.setValueAt(-Long.parseLong(table8.getValueAt(0, 3).toString()), 1, 3);
    	table8.setValueAt(Long.parseLong(table8.getValueAt(2, 2).toString()) - Long.parseLong(table8.getValueAt(3, 2).toString()), 2, 3);
    	table8.setValueAt(-Long.parseLong(table8.getValueAt(2, 3).toString()), 3, 3);
    	table8.setValueAt(Long.parseLong(table8.getValueAt(4, 2).toString()) - Long.parseLong(table8.getValueAt(5, 2).toString()), 4, 3);
    	table8.setValueAt(-Long.parseLong(table8.getValueAt(4, 3).toString()), 5, 3);
    	table8.setValueAt(Long.parseLong(table8.getValueAt(6, 2).toString()) - Long.parseLong(table8.getValueAt(7, 2).toString()), 6, 3);
    	table8.setValueAt(-Long.parseLong(table8.getValueAt(6, 3).toString()), 7, 3);
    	
    	tmpValue = Long.parseLong(table8.getValueAt(0, 3).toString());
    	totalValue += tmpValue >= 0 ? tmpValue : -tmpValue;
    	tmpValue = Long.parseLong(table8.getValueAt(2, 3).toString());
    	totalValue += tmpValue >= 0 ? tmpValue : -tmpValue;
    	tmpValue = Long.parseLong(table8.getValueAt(4, 3).toString());
    	totalValue += tmpValue >= 0 ? tmpValue : -tmpValue;
    	tmpValue = Long.parseLong(table8.getValueAt(6, 3).toString());
    	totalValue += tmpValue >= 0 ? tmpValue : -tmpValue;
    	
    	
    	table9.setValueAt(Long.parseLong(table9.getValueAt(0, 2).toString()) - Long.parseLong(table9.getValueAt(1, 2).toString()), 0, 3);
    	table9.setValueAt(-Long.parseLong(table9.getValueAt(0, 3).toString()), 1, 3);
    	table9.setValueAt(Long.parseLong(table9.getValueAt(2, 2).toString()) - Long.parseLong(table9.getValueAt(3, 2).toString()), 2, 3);
    	table9.setValueAt(-Long.parseLong(table9.getValueAt(2, 3).toString()), 3, 3);
    	table9.setValueAt(Long.parseLong(table9.getValueAt(4, 2).toString()) - Long.parseLong(table9.getValueAt(5, 2).toString()), 4, 3);
    	table9.setValueAt(-Long.parseLong(table9.getValueAt(4, 3).toString()), 5, 3);
    	
    	tmpValue = Long.parseLong(table9.getValueAt(0, 3).toString());
    	totalValue += tmpValue >= 0 ? tmpValue : -tmpValue;
    	tmpValue = Long.parseLong(table9.getValueAt(2, 3).toString());
    	totalValue += tmpValue >= 0 ? tmpValue : -tmpValue;
    	tmpValue = Long.parseLong(table9.getValueAt(4, 3).toString());
    	totalValue += tmpValue >= 0 ? tmpValue : -tmpValue;
    	
    	textFieldD.setText(Long.toString(totalValue));
    }
    
    public void resetData() {
    	for(int i = 0; i < 10; i++) {
    		for(int j = 2; j < 4; j++) {
    			if( i < 6) {
	    			table1.setValueAt(0, i, j);
	    			table2.setValueAt(0, i, j);
	    			table3.setValueAt(0, i, j);
	    			table4.setValueAt(0, i, j);
	    			table5.setValueAt(0, i, j);
	    			table6.setValueAt(0, i, j);
	    			table7.setValueAt(0, i, j);
	    			table8.setValueAt(0, i, j);
	    			table9.setValueAt(0, i, j);
    			} else if(i < 8) {
    				table1.setValueAt(0, i, j);
	    			table2.setValueAt(0, i, j);
	    			table3.setValueAt(0, i, j);
	    			table4.setValueAt(0, i, j);
	    			table5.setValueAt(0, i, j);
	    			table6.setValueAt(0, i, j);
	    			table7.setValueAt(0, i, j);
	    			table8.setValueAt(0, i, j);
    			} else{
    				table1.setValueAt(0, i, j);
	    			table2.setValueAt(0, i, j);
	    			table3.setValueAt(0, i, j);
	    			table4.setValueAt(0, i, j);
    			}
    		}
    	}
    	textFieldD.setText("0");
    }
}