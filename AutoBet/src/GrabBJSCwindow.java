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
   
public class GrabBJSCwindow extends JFrame {  
  

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
    final private JTable table10;
    final private JTable table11;
    private JLabel labelA;
    final private JTextField textFieldA = new JTextField(15);  
    final private JTextField textFieldB = new JTextField(15);
    final private JTextField textFieldC = new JTextField(15);
    private long remainTime = 0;
  
    public GrabBJSCwindow() {  
        // TODO Auto-generated constructor stub  
        final Container container = getContentPane();
        
        //冠，亚军和两面
        Vector<String> ball1 = new Vector<String>();
        ball1.add("冠，亚军和两面");
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
        table1.setRowHeight(24);
        JScrollPane scrollPane1 = new JScrollPane();  
        scrollPane1.setViewportView(table1);  
        scrollPane1.setSize(400, 89 + 32);
        scrollPane1.setLocation(0, 75);
        container.add(scrollPane1);  
        
        
        //冠军
        Vector<String> ball2 = new Vector<String>();
        ball2.add("冠军");
        final DefaultTableModel ball2Model = new DefaultTableModel(  
        		null, ball2);  
        final JTable tableBall2 = new JTable(ball2Model);
        JScrollPane paneBall2 = new JScrollPane();  
        paneBall2.setViewportView(tableBall2);  
        paneBall2.setSize(400, 25);
        paneBall2.setLocation(400, 50);
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
        tableValues2.add(new Vector<String>());
        tableValues2.add(new Vector<String>());
        tableValues2.get(0).add("大");
        tableValues2.get(1).add("小");
        tableValues2.get(2).add("单");
        tableValues2.get(3).add("双");
        tableValues2.get(4).add("龙");
        tableValues2.get(5).add("虎");
        
        final DefaultTableModel defaultTableModel2 = new DefaultTableModel(  
                tableValues2, tableColumnNames2);  
        table2 = new JTable(defaultTableModel2);  
        JScrollPane scrollPane2 = new JScrollPane();  
        scrollPane2.setViewportView(table2);  
        //scrollPane2.setLocation(600, 75);
        scrollPane2.setSize(400, 89 + 32);
        scrollPane2.setLocation(400, 75);
        container.add(scrollPane2);  
        
        
        //亚军
        Vector<String> ball3 = new Vector<String>();
        ball3.add("亚军");
        final DefaultTableModel ball3Model = new DefaultTableModel(  
        		null, ball3);  
        final JTable tableBall3 = new JTable(ball3Model);
        JScrollPane paneBall3 = new JScrollPane();  
        paneBall3.setViewportView(tableBall3);  
        //paneBall3.setLocation(0, 75 + 89);
        paneBall3.setSize(400, 25);
        paneBall3.setLocation(800, 50);
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
        tableValues3.add(new Vector<String>());
        tableValues3.add(new Vector<String>());
        tableValues3.get(0).add("大");
        tableValues3.get(1).add("小");
        tableValues3.get(2).add("单");
        tableValues3.get(3).add("双");
        tableValues3.get(4).add("龙");
        tableValues3.get(5).add("虎");
        
        final DefaultTableModel defaultTableModel3 = new DefaultTableModel(  
                tableValues3, tableColumnNames3);  
        table3 = new JTable(defaultTableModel3);  
        //table3.setRowHeight(24);
        JScrollPane scrollPane3 = new JScrollPane();  
        scrollPane3.setViewportView(table3);  
        scrollPane3.setSize(400, 89+32);
        scrollPane3.setLocation(800, 75);
        
        container.add(scrollPane3);  
        
        
        //第三名
        Vector<String> ball4 = new Vector<String>();
        ball4.add("第三名");
        final DefaultTableModel ball4Model = new DefaultTableModel(  
        		null, ball4);  
        final JTable tableBall4 = new JTable(ball4Model);
        JScrollPane paneBall4 = new JScrollPane();  
        paneBall4.setViewportView(tableBall4);  
        paneBall4.setSize(400, 25);
        //paneBall4.setLocation(600, 75 + 89);
        paneBall4.setLocation(0, 89 + 32 + 75);
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
        tableValues4.add(new Vector<String>());
        tableValues4.add(new Vector<String>());
        tableValues4.get(0).add("大");
        tableValues4.get(1).add("小");
        tableValues4.get(2).add("单");
        tableValues4.get(3).add("双");
        tableValues4.get(4).add("龙");
        tableValues4.get(5).add("虎");
        
        final DefaultTableModel defaultTableModel4 = new DefaultTableModel(  
                tableValues4, tableColumnNames4);  
        table4 = new JTable(defaultTableModel4);  
        JScrollPane scrollPane4 = new JScrollPane();  
        scrollPane4.setViewportView(table4);  
        scrollPane4.setSize(400, 89 + 32);
        //scrollPane4.setLocation(600, 75 + 89 + 25);
        scrollPane4.setLocation(0, 89 + 32 + 100);
        container.add(scrollPane4);  
        
        
        //第四名
        Vector<String> ball5 = new Vector<String>();
        ball5.add("第四名");
        final DefaultTableModel ball5Model = new DefaultTableModel(  
        		null, ball5);  
        final JTable tableBall5 = new JTable(ball5Model);
        JScrollPane paneBall5 = new JScrollPane();  
        paneBall5.setViewportView(tableBall5);  
        paneBall5.setSize(400, 25);
        //paneBall5.setLocation(0, 75 + 89 + 25 + 89);
        paneBall5.setLocation(400, 89 + 32 + 75);
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
        tableValues5.add(new Vector<String>());
        tableValues5.add(new Vector<String>());
        tableValues5.get(0).add("大");
        tableValues5.get(1).add("小");
        tableValues5.get(2).add("单");
        tableValues5.get(3).add("双");
        tableValues5.get(4).add("龙");
        tableValues5.get(5).add("虎");
        
        final DefaultTableModel defaultTableModel5 = new DefaultTableModel(  
                tableValues5, tableColumnNames5);
        table5 = new JTable(defaultTableModel5);
        JScrollPane scrollPane5 = new JScrollPane();  
        scrollPane5.setViewportView(table5);  
        scrollPane5.setSize(400, 89 + 32);
        //scrollPane5.setLocation(0, 75 + 89 + 25 + 89 + 25);
        scrollPane5.setLocation(400, 89 + 32 + 100);
        container.add(scrollPane5);  
        
        
        //第五名
        Vector<String> ball6 = new Vector<String>();
        ball6.add("第五名");
        final DefaultTableModel ball6Model = new DefaultTableModel(  
        		null, ball6);  
        final JTable tableBall6 = new JTable(ball6Model);
        JScrollPane paneBall6 = new JScrollPane();  
        paneBall6.setViewportView(tableBall6);  
        paneBall6.setSize(400, 25);
        paneBall6.setLocation(800, 89 + 32 + 75);
        container.add(paneBall6);
        
        Vector<String> tableColumnNames6 = new Vector<String>();  
        tableColumnNames6.add("种类");  
        tableColumnNames6.add("赔率");
        tableColumnNames6.add("金额");
        tableColumnNames6.add("差值");
        Vector<Vector<String>> tableValues6 = new Vector<Vector<String>>();
        tableValues6.add(new Vector<String>());
        tableValues6.add(new Vector<String>());
        tableValues6.add(new Vector<String>());
        tableValues6.add(new Vector<String>());
        tableValues6.add(new Vector<String>());
        tableValues6.add(new Vector<String>());
        tableValues6.get(0).add("大");
        tableValues6.get(1).add("小");
        tableValues6.get(2).add("单");
        tableValues6.get(3).add("双");
        tableValues6.get(4).add("龙");
        tableValues6.get(5).add("虎");
        
        final DefaultTableModel defaultTableModel6 = new DefaultTableModel(  
                tableValues6, tableColumnNames6);  
        table6 = new JTable(defaultTableModel6);  
        JScrollPane scrollPane6 = new JScrollPane();  
        scrollPane6.setViewportView(table6);  
        scrollPane6.setSize(400, 89 + 32);
        scrollPane6.setLocation(800, 89 + 32 + 100);
        container.add(scrollPane6);  
        
        //第六名
        Vector<String> ball7 = new Vector<String>();
        ball7.add("第六名");
        final DefaultTableModel ball7Model = new DefaultTableModel(  
        		null, ball7);  
        final JTable tableBall7 = new JTable(ball7Model);
        JScrollPane paneBall7 = new JScrollPane();  
        paneBall7.setViewportView(tableBall7);  
        paneBall7.setSize(400, 25);
        paneBall7.setLocation(0, 89 + 32 + 100 + 89 + 32);
        container.add(paneBall7);
        
        Vector<String> tableColumnNames7 = new Vector<String>();  
        tableColumnNames7.add("种类");  
        tableColumnNames7.add("赔率");
        tableColumnNames7.add("金额");
        tableColumnNames7.add("差值");
        Vector<Vector<String>> tableValues7 = new Vector<Vector<String>>();
        tableValues7.add(new Vector<String>());
        tableValues7.add(new Vector<String>());
        tableValues7.add(new Vector<String>());
        tableValues7.add(new Vector<String>());
        tableValues7.get(0).add("大");
        tableValues7.get(1).add("小");
        tableValues7.get(2).add("单");
        tableValues7.get(3).add("双");;
        
        final DefaultTableModel defaultTableModel7 = new DefaultTableModel(  
                tableValues7, tableColumnNames7);  
        table7 = new JTable(defaultTableModel7);  
        JScrollPane scrollPane7 = new JScrollPane();  
        scrollPane7.setViewportView(table7);
        scrollPane7.setSize(400, 89);
        scrollPane7.setLocation(0, 89 + 32 + 100 + 89 + 32 + 25);
        container.add(scrollPane7); 
        
        //第七名
        Vector<String> ball8 = new Vector<String>();
        ball8.add("第七名");
        final DefaultTableModel ball8Model = new DefaultTableModel(  
        		null, ball8);  
        final JTable tableBall8 = new JTable(ball8Model);
        JScrollPane paneBall8 = new JScrollPane();  
        paneBall8.setViewportView(tableBall8);  
        paneBall8.setSize(400, 25);
        paneBall8.setLocation(400, 89 + 32 + 100 + 89 + 32);
        container.add(paneBall8);
        
        Vector<String> tableColumnNames8 = new Vector<String>();  
        tableColumnNames8.add("种类");  
        tableColumnNames8.add("赔率");
        tableColumnNames8.add("金额");
        tableColumnNames8.add("差值");
        Vector<Vector<String>> tableValues8 = new Vector<Vector<String>>();
        tableValues8.add(new Vector<String>());
        tableValues8.add(new Vector<String>());
        tableValues8.add(new Vector<String>());
        tableValues8.add(new Vector<String>());
        tableValues8.get(0).add("大");
        tableValues8.get(1).add("小");
        tableValues8.get(2).add("单");
        tableValues8.get(3).add("双");

        
        final DefaultTableModel defaultTableModel8 = new DefaultTableModel(  
                tableValues8, tableColumnNames8);  
        table8 = new JTable(defaultTableModel8);  
        JScrollPane scrollPane8 = new JScrollPane();  
        scrollPane8.setViewportView(table8);  
        scrollPane8.setSize(400, 89);
        scrollPane8.setLocation(400, 89 + 32 + 100 + 89 + 32 + 25);
        container.add(scrollPane8);  
        
        //第八名
        Vector<String> ball9 = new Vector<String>();
        ball9.add("第八名");
        final DefaultTableModel ball9Model = new DefaultTableModel(  
        		null, ball9);  
        final JTable tableBall9 = new JTable(ball9Model);
        JScrollPane paneBall9 = new JScrollPane();  
        paneBall9.setViewportView(tableBall9);  
        paneBall9.setSize(400, 25);
        paneBall9.setLocation(800, 89 + 32 + 100 + 89 + 32);
        container.add(paneBall9);
        
        Vector<String> tableColumnNames9 = new Vector<String>();  
        tableColumnNames9.add("种类");  
        tableColumnNames9.add("赔率");
        tableColumnNames9.add("金额");
        tableColumnNames9.add("差值");
        Vector<Vector<String>> tableValues9 = new Vector<Vector<String>>();
        tableValues9.add(new Vector<String>());
        tableValues9.add(new Vector<String>());
        tableValues9.add(new Vector<String>());
        tableValues9.add(new Vector<String>());
        tableValues9.get(0).add("大");
        tableValues9.get(1).add("小");
        tableValues9.get(2).add("单");
        tableValues9.get(3).add("双");
        
        final DefaultTableModel defaultTableModel9 = new DefaultTableModel(  
                tableValues9, tableColumnNames9);  
        table9 = new JTable(defaultTableModel9);  
        JScrollPane scrollPane9 = new JScrollPane();  
        scrollPane9.setViewportView(table9);  
        scrollPane9.setSize(400, 89);
        scrollPane9.setLocation(800, 89 + 32 + 100 + 89 + 32 + 25);
        container.add(scrollPane9);  
        
      //第九名
        Vector<String> ball10 = new Vector<String>();
        ball10.add("第九名");
        final DefaultTableModel ball10Model = new DefaultTableModel(  
        		null, ball10);  
        final JTable tableBall10 = new JTable(ball10Model);
        JScrollPane paneBall10 = new JScrollPane();  
        paneBall10.setViewportView(tableBall10);  
        paneBall10.setSize(400, 25);
        paneBall10.setLocation(0, 89 + 32 + 100 + 89 + 32 + 89 + 25);
        container.add(paneBall10);
        
        Vector<String> tableColumnNames10 = new Vector<String>();  
        tableColumnNames10.add("种类");  
        tableColumnNames10.add("赔率");
        tableColumnNames10.add("金额");
        tableColumnNames10.add("差值");
        Vector<Vector<String>> tableValues10 = new Vector<Vector<String>>();
        tableValues10.add(new Vector<String>());
        tableValues10.add(new Vector<String>());
        tableValues10.add(new Vector<String>());
        tableValues10.add(new Vector<String>());
        tableValues10.get(0).add("大");
        tableValues10.get(1).add("小");
        tableValues10.get(2).add("单");
        tableValues10.get(3).add("双");

        final DefaultTableModel defaultTableModel10 = new DefaultTableModel(  
                tableValues10, tableColumnNames10);  
        table10 = new JTable(defaultTableModel10);  
        JScrollPane scrollPane10 = new JScrollPane();  
        scrollPane10.setViewportView(table10);  
        scrollPane10.setSize(400, 89);
        scrollPane10.setLocation(0, 89 + 32 + 100 + 89 + 32 + 89 + 50);
        container.add(scrollPane10);  
        
        //第十名
        Vector<String> ball11 = new Vector<String>();
        ball11.add("第十名");
        final DefaultTableModel ball11Model = new DefaultTableModel(  
        		null, ball11);  
        final JTable tableBall11 = new JTable(ball11Model);
        JScrollPane paneBall11 = new JScrollPane();  
        paneBall11.setViewportView(tableBall11);  
        paneBall11.setSize(400, 25);
        paneBall11.setLocation(400, 89 + 32 + 100 + 89 + 32 + 89 + 25);
        container.add(paneBall11);
        
        Vector<String> tableColumnNames11 = new Vector<String>();  
        tableColumnNames11.add("种类");  
        tableColumnNames11.add("赔率");
        tableColumnNames11.add("金额");
        tableColumnNames11.add("差值");
        Vector<Vector<String>> tableValues11 = new Vector<Vector<String>>();
        tableValues11.add(new Vector<String>());
        tableValues11.add(new Vector<String>());
        tableValues11.add(new Vector<String>());
        tableValues11.add(new Vector<String>());
        tableValues11.get(0).add("总大");
        tableValues11.get(1).add("总小");
        tableValues11.get(2).add("总单");
        tableValues11.get(3).add("总双");

        final DefaultTableModel defaultTableModel11 = new DefaultTableModel(  
                tableValues11, tableColumnNames11);  
        table11 = new JTable(defaultTableModel11);  
        JScrollPane scrollPane11 = new JScrollPane();  
        scrollPane11.setViewportView(table11);  
        scrollPane11.setSize(400, 89);
        scrollPane11.setLocation(400, 89 + 32 + 100 + 89 + 32 + 89 + 50);
        container.add(scrollPane11); 
        
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
        
        setTitle("北京赛车");  
       //pack(); //Realize the components.  
        setBounds(100, 100, 1220, 900);  
//      textFieldA.requestFocus();  
        setLayout(null);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);  
        setVisible(false); //Display the window. 
        table1.setEnabled(false);
        table2.setEnabled(false);
        table3.setEnabled(false);
        table4.setEnabled(false);
        table5.setEnabled(false);
        table6.setEnabled(false);
        table7.setEnabled(false);
        table8.setEnabled(false);
        table9.setEnabled(false);
        table10.setEnabled(false);
        table11.setEnabled(false);
        resetData();
        setTimer(textFieldA);
    }  
    
    private void setTimer(JTextField time){   
        final JTextField varTime = time;   
        Timer timeAction = new Timer(1000, new ActionListener() {          
            public void actionPerformed(ActionEvent e) {       
                SimpleDateFormat df = new SimpleDateFormat("mm:ss");   
                if(remainTime < 0) {
                	remainTime = 0;
                }
                varTime.setText(df.format(new Date(remainTime)));
                remainTime -= 1000;
            }      
        });            
        timeAction.start();        
    } 
    
    public void setRemainTime(long time) {
    	remainTime = time;
    }
    
    public void setCloseText(boolean close) {
    	if(close) {
    		labelA.setText("距开奖:");
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
    
    public void setDataOk(boolean ok, long remainTime) {
    	if(ok) {
    		textFieldC.setText("可用,距封盘" + remainTime + "秒");
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
	    				case "GDX_D":
	    					table1.setValueAt(odds, 0, 1);
	    					table1.setValueAt(amount, 0, 2);
	    					break;
	    				case "GDX_X":
	    					table1.setValueAt(odds, 1, 1);
	    					table1.setValueAt(amount, 1, 2);
	    					break;
	    				case "GDS_D":
	    					table1.setValueAt(odds, 2, 1);
	    					table1.setValueAt(amount, 2, 2);
	    					break;
	    				case "GDS_S":
	    					table1.setValueAt(odds, 3, 1);
	    					table1.setValueAt(amount, 3, 2);
	    					break;
	    				case "DX1_D":
	    					table2.setValueAt(odds, 0, 1);
	    					table2.setValueAt(amount, 0, 2);
	    					break;
	    				case "DX1_X":
	    					table2.setValueAt(odds, 1, 1);
	    					table2.setValueAt(amount, 1, 2);
	    					break;
	    				case "DS1_D":
	    					table2.setValueAt(odds, 2, 1);
	    					table2.setValueAt(amount, 2, 2);
	    					break;
	    				case "DS1_S":
	    					table2.setValueAt(odds, 3, 1);
	    					table2.setValueAt(amount, 3, 2);
	    					break;
	    				case "LH1_L":
	    					table2.setValueAt(odds, 4, 1);
	    					table2.setValueAt(amount, 4, 2);
	    					break;
	    				case "LH1_H":
	    					table2.setValueAt(odds, 5, 1);
	    					table2.setValueAt(amount, 5, 2);
	    					break;
	    				case "DX2_D":
	    					table3.setValueAt(odds, 0, 1);
	    					table3.setValueAt(amount, 0, 2);
	    					break;
	    				case "DX2_X":
	    					table3.setValueAt(odds, 1, 1);
	    					table3.setValueAt(amount, 1, 2);
	    					break;
	    				case "DS2_D":
	    					table3.setValueAt(odds, 2, 1);
	    					table3.setValueAt(amount, 2, 2);
	    					break;
	    				case "DS2_S":
	    					table3.setValueAt(odds, 3, 1);
	    					table3.setValueAt(amount, 3, 2);
	    					break;
	    				case "LH2_L":
	    					table3.setValueAt(odds, 4, 1);
	    					table3.setValueAt(amount, 4, 2);
	    					break;
	    				case "LH2_H":
	    					table3.setValueAt(odds, 5, 1);
	    					table3.setValueAt(amount, 5, 2);
	    					break;
	    				case "DX3_D":
	    					table4.setValueAt(odds, 0, 1);
	    					table4.setValueAt(amount, 0, 2);
	    					break;
	    				case "DX3_X":
	    					table4.setValueAt(odds, 1, 1);
	    					table4.setValueAt(amount, 1, 2);
	    					break;
	    				case "DS3_D":
	    					table4.setValueAt(odds, 2, 1);
	    					table4.setValueAt(amount, 2, 2);
	    					break;
	    				case "DS3_S":
	    					table4.setValueAt(odds, 3, 1);
	    					table4.setValueAt(amount, 3, 2);
	    					break;
	    				case "LH3_L":
	    					table4.setValueAt(odds, 4, 1);
	    					table4.setValueAt(amount, 4, 2);
	    					break;
	    				case "LH3_H":
	    					table4.setValueAt(odds, 5, 1);
	    					table4.setValueAt(amount, 5, 2);
	    					break;
	    				case "DX4_D":
	    					table5.setValueAt(odds, 0, 1);
	    					table5.setValueAt(amount, 0, 2);
	    					break;
	    				case "DX4_X":
	    					table5.setValueAt(odds, 1, 1);
	    					table5.setValueAt(amount, 1, 2);
	    					break;
	    				case "DS4_D":
	    					table5.setValueAt(odds, 2, 1);
	    					table5.setValueAt(amount, 2, 2);
	    					break;
	    				case "DS4_S":
	    					table5.setValueAt(odds, 3, 1);
	    					table5.setValueAt(amount, 3, 2);
	    					break;
	    				case "LH4_L":
	    					table5.setValueAt(odds, 4, 1);
	    					table5.setValueAt(amount, 4, 2);
	    					break;
	    				case "LH4_H":
	    					table5.setValueAt(odds, 5, 1);
	    					table5.setValueAt(amount, 5, 2);
	    					break;
	    				case "DX5_D":
	    					table6.setValueAt(odds, 0, 1);
	    					table6.setValueAt(amount, 0, 2);
	    					break;
	    				case "DX5_X":
	    					table6.setValueAt(odds, 1, 1);
	    					table6.setValueAt(amount, 1, 2);
	    					break;
	    				case "DS5_D":
	    					table6.setValueAt(odds, 2, 1);
	    					table6.setValueAt(amount, 2, 2);
	    					break;
	    				case "DS5_S":
	    					table6.setValueAt(odds, 3, 1);
	    					table6.setValueAt(amount, 3, 2);
	    					break;
	    				case "LH5_L":
	    					table6.setValueAt(odds, 4, 1);
	    					table6.setValueAt(amount, 4, 2);
	    					break;
	    				case "LH5_H":
	    					table6.setValueAt(odds, 5, 1);
	    					table6.setValueAt(amount, 5, 2);
	    					break;
	    				case "DX6_D":
	    					table7.setValueAt(odds, 0, 1);
	    					table7.setValueAt(amount, 0, 2);
	    					break;
	    				case "DX6_X":
	    					table7.setValueAt(odds, 1, 1);
	    					table7.setValueAt(amount, 1, 2);
	    					break;
	    				case "DS6_D":
	    					table7.setValueAt(odds, 2, 1);
	    					table7.setValueAt(amount, 2, 2);
	    					break;
	    				case "DS6_S":
	    					table7.setValueAt(odds, 3, 1);
	    					table7.setValueAt(amount, 3, 2);
	    					break;
	    				case "DX7_D":
	    					table8.setValueAt(odds, 0, 1);
	    					table8.setValueAt(amount, 0, 2);
	    					break;
	    				case "DX7_X":
	    					table8.setValueAt(odds, 1, 1);
	    					table8.setValueAt(amount, 1, 2);
	    					break;
	    				case "DS7_D":
	    					table8.setValueAt(odds, 2, 1);
	    					table8.setValueAt(amount, 2, 2);
	    					break;
	    				case "DS7_S":
	    					table8.setValueAt(odds, 3, 1);
	    					table8.setValueAt(amount, 3, 2);
	    					break;
	    				case "DX8_D":
	    					table9.setValueAt(odds, 0, 1);
	    					table9.setValueAt(amount, 0, 2);
	    					break;
	    				case "DX8_X":
	    					table9.setValueAt(odds, 1, 1);
	    					table9.setValueAt(amount, 1, 2);
	    					break;
	    				case "DS8_D":
	    					table9.setValueAt(odds, 2, 1);
	    					table9.setValueAt(amount, 2, 2);
	    					break;
	    				case "DS8_S":
	    					table9.setValueAt(odds, 3, 1);
	    					table9.setValueAt(amount, 3, 2);
	    					break;
	    				case "DX9_D":
	    					table10.setValueAt(odds, 0, 1);
	    					table10.setValueAt(amount, 0, 2);
	    					break;
	    				case "DX9_X":
	    					table10.setValueAt(odds, 1, 1);
	    					table10.setValueAt(amount, 1, 2);
	    					break;
	    				case "DS9_D":
	    					table10.setValueAt(odds, 2, 1);
	    					table10.setValueAt(amount, 2, 2);
	    					break;
	    				case "DS9_S":
	    					table10.setValueAt(odds, 3, 1);
	    					table10.setValueAt(amount, 3, 2);
	    					break;
	    				case "DX10_D":
	    					table11.setValueAt(odds, 0, 1);
	    					table11.setValueAt(amount, 0, 2);
	    					break;
	    				case "DX10_X":
	    					table11.setValueAt(odds, 1, 1);
	    					table11.setValueAt(amount, 1, 2);
	    					break;
	    				case "DS10_D":
	    					table11.setValueAt(odds, 2, 1);
	    					table11.setValueAt(amount, 2, 2);
	    					break;
	    				case "DS10_S":
	    					table11.setValueAt(odds, 3, 1);
	    					table11.setValueAt(amount, 3, 2);
	    					break;
	    			}	
	        	}
	    	}
    	}catch(Exception e) {
    	}
    	table1.setValueAt(Long.parseLong(table1.getValueAt(0, 2).toString()) - Long.parseLong(table1.getValueAt(1, 2).toString()), 0, 3);
    	table1.setValueAt(-Long.parseLong(table1.getValueAt(0, 3).toString()), 1, 3);
    	table1.setValueAt(Long.parseLong(table1.getValueAt(2, 2).toString()) - Long.parseLong(table1.getValueAt(3, 2).toString()), 2, 3);
    	table1.setValueAt(-Long.parseLong(table1.getValueAt(2, 3).toString()), 3, 3);
    	
    	table2.setValueAt(Long.parseLong(table2.getValueAt(0, 2).toString()) - Long.parseLong(table2.getValueAt(1, 2).toString()), 0, 3);
    	table2.setValueAt(-Long.parseLong(table2.getValueAt(0, 3).toString()), 1, 3);
    	table2.setValueAt(Long.parseLong(table2.getValueAt(2, 2).toString()) - Long.parseLong(table2.getValueAt(3, 2).toString()), 2, 3);
    	table2.setValueAt(-Long.parseLong(table2.getValueAt(2, 3).toString()), 3, 3);
    	table2.setValueAt(Long.parseLong(table2.getValueAt(4, 2).toString()) - Long.parseLong(table2.getValueAt(5, 2).toString()), 4, 3);
    	table2.setValueAt(-Long.parseLong(table2.getValueAt(4, 3).toString()), 5, 3);
    	
    	table3.setValueAt(Long.parseLong(table3.getValueAt(0, 2).toString()) - Long.parseLong(table3.getValueAt(1, 2).toString()), 0, 3);
    	table3.setValueAt(-Long.parseLong(table3.getValueAt(0, 3).toString()), 1, 3);
    	table3.setValueAt(Long.parseLong(table3.getValueAt(2, 2).toString()) - Long.parseLong(table3.getValueAt(3, 2).toString()), 2, 3);
    	table3.setValueAt(-Long.parseLong(table3.getValueAt(2, 3).toString()), 3, 3);
    	table3.setValueAt(Long.parseLong(table3.getValueAt(4, 2).toString()) - Long.parseLong(table3.getValueAt(5, 2).toString()), 4, 3);
    	table3.setValueAt(-Long.parseLong(table3.getValueAt(4, 3).toString()), 5, 3);
    	
    	table4.setValueAt(Long.parseLong(table4.getValueAt(0, 2).toString()) - Long.parseLong(table4.getValueAt(1, 2).toString()), 0, 3);
    	table4.setValueAt(-Long.parseLong(table4.getValueAt(0, 3).toString()), 1, 3);
    	table4.setValueAt(Long.parseLong(table4.getValueAt(2, 2).toString()) - Long.parseLong(table4.getValueAt(3, 2).toString()), 2, 3);
    	table4.setValueAt(-Long.parseLong(table4.getValueAt(2, 3).toString()), 3, 3);
    	table4.setValueAt(Long.parseLong(table4.getValueAt(4, 2).toString()) - Long.parseLong(table4.getValueAt(5, 2).toString()), 4, 3);
    	table4.setValueAt(-Long.parseLong(table4.getValueAt(4, 3).toString()), 5, 3);
    	
    	table5.setValueAt(Long.parseLong(table5.getValueAt(0, 2).toString()) - Long.parseLong(table5.getValueAt(1, 2).toString()), 0, 3);
    	table5.setValueAt(-Long.parseLong(table5.getValueAt(0, 3).toString()), 1, 3);
    	table5.setValueAt(Long.parseLong(table5.getValueAt(2, 2).toString()) - Long.parseLong(table5.getValueAt(3, 2).toString()), 2, 3);
    	table5.setValueAt(-Long.parseLong(table5.getValueAt(2, 3).toString()), 3, 3);
    	table5.setValueAt(Long.parseLong(table5.getValueAt(4, 2).toString()) - Long.parseLong(table5.getValueAt(5, 2).toString()), 4, 3);
    	table5.setValueAt(-Long.parseLong(table5.getValueAt(4, 3).toString()), 5, 3);
    	
    	table6.setValueAt(Long.parseLong(table6.getValueAt(0, 2).toString()) - Long.parseLong(table6.getValueAt(1, 2).toString()), 0, 3);
    	table6.setValueAt(-Long.parseLong(table6.getValueAt(0, 3).toString()), 1, 3);
    	table6.setValueAt(Long.parseLong(table6.getValueAt(2, 2).toString()) - Long.parseLong(table6.getValueAt(3, 2).toString()), 2, 3);
    	table6.setValueAt(-Long.parseLong(table6.getValueAt(2, 3).toString()), 3, 3);
    	table6.setValueAt(Long.parseLong(table6.getValueAt(4, 2).toString()) - Long.parseLong(table6.getValueAt(5, 2).toString()), 4, 3);
    	table6.setValueAt(-Long.parseLong(table6.getValueAt(4, 3).toString()), 5, 3);
    	
    	table7.setValueAt(Long.parseLong(table7.getValueAt(0, 2).toString()) - Long.parseLong(table7.getValueAt(1, 2).toString()), 0, 3);
    	table7.setValueAt(-Long.parseLong(table7.getValueAt(0, 3).toString()), 1, 3);
    	table7.setValueAt(Long.parseLong(table7.getValueAt(2, 2).toString()) - Long.parseLong(table7.getValueAt(3, 2).toString()), 2, 3);
    	table7.setValueAt(-Long.parseLong(table7.getValueAt(2, 3).toString()), 3, 3);
    	
    	table8.setValueAt(Long.parseLong(table8.getValueAt(0, 2).toString()) - Long.parseLong(table8.getValueAt(1, 2).toString()), 0, 3);
    	table8.setValueAt(-Long.parseLong(table8.getValueAt(0, 3).toString()), 1, 3);
    	table8.setValueAt(Long.parseLong(table8.getValueAt(2, 2).toString()) - Long.parseLong(table8.getValueAt(3, 2).toString()), 2, 3);
    	table8.setValueAt(-Long.parseLong(table8.getValueAt(2, 3).toString()), 3, 3);
    	
    	table9.setValueAt(Long.parseLong(table9.getValueAt(0, 2).toString()) - Long.parseLong(table9.getValueAt(1, 2).toString()), 0, 3);
    	table9.setValueAt(-Long.parseLong(table9.getValueAt(0, 3).toString()), 1, 3);
    	table9.setValueAt(Long.parseLong(table9.getValueAt(2, 2).toString()) - Long.parseLong(table9.getValueAt(3, 2).toString()), 2, 3);
    	table9.setValueAt(-Long.parseLong(table9.getValueAt(2, 3).toString()), 3, 3);
    	
    	table10.setValueAt(Long.parseLong(table10.getValueAt(0, 2).toString()) - Long.parseLong(table10.getValueAt(1, 2).toString()), 0, 3);
    	table10.setValueAt(-Long.parseLong(table10.getValueAt(0, 3).toString()), 1, 3);
    	table10.setValueAt(Long.parseLong(table10.getValueAt(2, 2).toString()) - Long.parseLong(table10.getValueAt(3, 2).toString()), 2, 3);
    	table10.setValueAt(-Long.parseLong(table10.getValueAt(2, 3).toString()), 3, 3);
    	
    	table11.setValueAt(Long.parseLong(table11.getValueAt(0, 2).toString()) - Long.parseLong(table11.getValueAt(1, 2).toString()), 0, 3);
    	table11.setValueAt(-Long.parseLong(table11.getValueAt(0, 3).toString()), 1, 3);
    	table11.setValueAt(Long.parseLong(table11.getValueAt(2, 2).toString()) - Long.parseLong(table11.getValueAt(3, 2).toString()), 2, 3);
    	table11.setValueAt(-Long.parseLong(table11.getValueAt(2, 3).toString()), 3, 3);
    }
    
    public void resetData() {
    	for(int i = 0; i < 6; i++) {
    		for(int j = 2; j < 4; j++) {
    			if( i < 4) {
	    			table1.setValueAt(0, i, j);
	    			table2.setValueAt(0, i, j);
	    			table3.setValueAt(0, i, j);
	    			table4.setValueAt(0, i, j);
	    			table5.setValueAt(0, i, j);
	    			table6.setValueAt(0, i, j);
	    			table7.setValueAt(0, i, j);
	    			table8.setValueAt(0, i, j);
	    			table9.setValueAt(0, i, j);
	    			table10.setValueAt(0, i, j);
	    			table11.setValueAt(0, i, j);
    			}
    			else {
    				table2.setValueAt(0, i, j);
	    			table3.setValueAt(0, i, j);
	    			table4.setValueAt(0, i, j);
	    			table5.setValueAt(0, i, j);
	    			table6.setValueAt(0, i, j);
    			}
    		}
    	}
    }
}