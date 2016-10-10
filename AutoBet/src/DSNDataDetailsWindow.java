import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;  
 
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;  
import java.awt.event.ActionListener;  
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;  
  
import java.awt.Color;










import javax.swing.JFrame;  
import javax.swing.JLabel;  
import javax.swing.JPanel;  
import javax.swing.JScrollPane;  
import javax.swing.JTable;  
import javax.swing.JTextField;  
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel; 
import javax.swing.table.AbstractTableModel; 





import javax.swing.table.TableCellRenderer;

import java.util.Date;      

import javax.swing.Timer;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.concurrent.atomic.AtomicLong;
import java.util.Stack;


enum TYPEINDEX{
	DATA,
	DN,
	STATC,
	BETAMOUNT,
	PROFIT,
	STATS,
	COUNT,
	DVALUE
	
}


class ColorTableCellRenderer extends JLabel implements TableCellRenderer

{

private static final long serialVersionUID = 1L;

//定义构造器

public ColorTableCellRenderer ()

{

//设置标签为不透明状态

this.setOpaque(true);

//设置标签的文本对齐方式为居中

this.setHorizontalAlignment(JLabel.CENTER);

}

//实现获取呈现控件的getTableCellRendererComponent方法

public Component getTableCellRendererComponent(JTable table,Object value,

           boolean isSelected,boolean hasFocus,int row,int column)

{           

//获取要呈现的颜色

Color c=(Color)value;

//根据参数value设置背景色

this.setBackground(c);



return this;   

}

 }   



public class DSNDataDetailsWindow extends JFrame  
{  
  
   
	private static final long serialVersionUID = 508685938515369544L;
	
	private  Stack<Object[]> detailsData = new Stack<Object[]>();
	
	
	
	
    
    private JLabel labelzongqishu = new JLabel("总期数:");
    private JTextField textFieldzongqishu = new JTextField(15);  
    
    private JLabel labelzongshibai = new JLabel("总失败:");
    private JTextField textFieldzongshibai = new JTextField(15);  
    
    private JLabel labelzongyichang = new JLabel("总异常:");
    private JTextField textFieldzongyichang = new JTextField(15);  
    
    private JLabel labeljinriqishu = new JLabel("今日期数:");
    private JTextField textFieldjinriqishu = new JTextField(15);  
    
    private JLabel labeljinrishibai = new JLabel("今日失败:");
    private JTextField textFieldjinrishibai = new JTextField(15);  
    
    private JLabel labeljinriyichang = new JLabel("今日异常:");
    private JTextField textFieldjinriyichang = new JTextField(15);  

    private JLabel labelyue = new JLabel("余额:");
    private JTextField textFieldyue = new JTextField(15);  
    
    private JLabel labeljinrishuying = new JLabel("今日输赢:");
    private JTextField textFieldjinrishuying = new JTextField(15);  
    
    
    private JLabel labelzongchazhi = new JLabel("总差值:");
    private JTextField textFieldzongchazhi = new JTextField(15);  
    
    private JLabel labeljinrichazhi = new JLabel("今日差值:");
    private JTextField textFieldjinrichazhi = new JTextField(15);  
    
    
/*    private JLabel labeltime = new JLabel("距封盘:");
    private JTextField textFieldtime = new JTextField(15);  
    
    private AtomicLong remainTime = new AtomicLong(0);*/
    
    
    MyTableModel tableMode = new MyTableModel();
    
    
    
/*    private void setTimer(JTextField time) {   
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
    		labeltime.setText("已封盘，距开奖:");
    	}
    	else {
    		labeltime.setText("距封盘:");
    	}
    }*/
    
    
    
	

	public DSNDataDetailsWindow()  
    {  
		//setTitle("投注北京赛车详情");  
		
        intiComponent();  
        
    }  
	
	
	
	public void updateTextFieldzongqishu(String str){
		textFieldzongqishu.setText(str);
	}
	
	public void updateTextFieldzongshibai(String str){
		textFieldzongshibai.setText(str);
	}
	
	public void updateTextFieldzongyichang(String str){
		textFieldzongyichang.setText(str);
	}	
	
	public void updateTextFieldjinriqishu(String str){
		textFieldjinriqishu.setText(str);
	}
	
	public void updateTextFieldjinrishibai(String str){
		textFieldjinrishibai.setText(str);
	}
	
	public void updateTextFieldjinriyichang(String str){
		textFieldjinriyichang.setText(str);
	}
	
	public void updateTextFieldyue(String str){
		textFieldyue.setText(str);
	}
	
	
	public void updateTextFieldshuying(String str){
		textFieldjinrishuying.setText(str);
	}
	
	public void updateTextFieldzongchazhi(String str){
		textFieldzongchazhi.setText(str);
	}
	
	
	public void updateTextFieldjinrichazhi(String str){
		textFieldjinrichazhi.setText(str);
	}
	
	
	
	public void addData(String data, String drawNumber, int stat, String betAmount, String count){
		
		try{
			for(int i = 0; i<detailsData.size(); i++){ //todo 优化
				if(((String)detailsData.elementAt(i)[1]).equals(drawNumber)){
					return;
				}
			}
			
			Object[] newData = new Object[8];
			newData[TYPEINDEX.DATA.ordinal()] = data;
			newData[TYPEINDEX.DN.ordinal()] = drawNumber;
			if(stat == 0){
				newData[TYPEINDEX.STATC.ordinal()] = new Color(100, 255, 100);
				newData[TYPEINDEX.BETAMOUNT.ordinal()] = betAmount;
				newData[TYPEINDEX.PROFIT.ordinal()] = "0";
				newData[TYPEINDEX.STATS.ordinal()] = "成功";
				newData[TYPEINDEX.COUNT.ordinal()] = count;
				newData[TYPEINDEX.DVALUE.ordinal()] = "0";
			}else if(stat == 1){
				newData[TYPEINDEX.STATC.ordinal()] = new Color(255, 100, 100);
				newData[TYPEINDEX.BETAMOUNT.ordinal()] = betAmount;
				newData[TYPEINDEX.PROFIT.ordinal()] = "---";
				newData[TYPEINDEX.STATS.ordinal()] = "失败";
				newData[TYPEINDEX.COUNT.ordinal()] = count;
				newData[TYPEINDEX.DVALUE.ordinal()] = "---";
			}
			else if(stat == 2){
				newData[TYPEINDEX.STATC.ordinal()] = new Color(100, 100, 255);
				newData[TYPEINDEX.BETAMOUNT.ordinal()] = betAmount;
				newData[TYPEINDEX.PROFIT.ordinal()] = "---";
				newData[TYPEINDEX.STATS.ordinal()] = "未知";
				newData[TYPEINDEX.COUNT.ordinal()] = count;
				newData[TYPEINDEX.DVALUE.ordinal()] = "---";
			}
			else{//漏投
				newData[TYPEINDEX.STATC.ordinal()] = new Color(255, 100, 100);
				newData[TYPEINDEX.BETAMOUNT.ordinal()] = "---";
				newData[TYPEINDEX.PROFIT.ordinal()] = "---";
				newData[TYPEINDEX.STATS.ordinal()] = "漏投";
				newData[TYPEINDEX.COUNT.ordinal()] = "---";
				newData[TYPEINDEX.DVALUE.ordinal()] = "---";
			}
			
			addData(newData);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		

		
	}
	
	public  void addData(Object[] a){
		
		try{
			detailsData.push(a);
			
	    	Comparator ct = new CompareStr();
	    	
	    	Collections.sort(detailsData, ct);
			
			tableMode.updateTable();
		}catch(Exception e){
			e.printStackTrace();
		}
		

	}
	
	
	public void updateRowItem(String drawNumber, int index, String value){
		
		
		try{
			for(int i = 0; i < detailsData.size(); i++){
				if(detailsData.elementAt(i)[TYPEINDEX.DN.ordinal()].equals(drawNumber)){
					if(index == TYPEINDEX.STATC.ordinal()){
						int stat = Integer.parseInt(value);
						if(stat == 0){
							detailsData.elementAt(i)[TYPEINDEX.STATC.ordinal()] = new Color(100, 255, 100);
							detailsData.elementAt(i)[TYPEINDEX.STATS.ordinal()] = "成功";
						}else if(stat == 1){
							detailsData.elementAt(i)[TYPEINDEX.STATC.ordinal()] = new Color(255, 100, 100);
							detailsData.elementAt(i)[TYPEINDEX.STATS.ordinal()] = "失败";
						}
					}else if(index == TYPEINDEX.COUNT.ordinal()){
						int count = Integer.parseInt((String)detailsData.elementAt(i)[TYPEINDEX.COUNT.ordinal()]);
						int actualCount = Integer.parseInt(value);
						
						if(count != actualCount){
							detailsData.elementAt(i)[TYPEINDEX.STATC.ordinal()] = new Color(255, 100, 100);
							detailsData.elementAt(i)[TYPEINDEX.STATS.ordinal()] = "多投";
							detailsData.elementAt(i)[TYPEINDEX.COUNT.ordinal()] = value;
						}
						
					}
					else{
						detailsData.elementAt(i)[index] = value;
					}
					
					
					
				}
			}
			
			tableMode.updateTable();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		

	}
	
	

	
	
	
  
    /** 
     * 初始化窗体组件 
     */  
    private void intiComponent()  
    {  

		final Container container = getContentPane();
		
		container.setLayout(new BorderLayout());
		
		JPanel panelNorth = new JPanel(new GridLayout(5, 4));

        container.add(panelNorth, BorderLayout.NORTH);  
        
        
        panelNorth.add(labelzongqishu);
        panelNorth.add(textFieldzongqishu);
        textFieldzongqishu.setEditable(false);
        
        panelNorth.add(labelzongshibai);
        panelNorth.add(textFieldzongshibai);
        textFieldzongshibai.setEditable(false);


        panelNorth.add(labeljinriqishu);
        panelNorth.add(textFieldjinriqishu);
        textFieldjinriqishu.setEditable(false);
        
        panelNorth.add(labeljinrishibai);
        panelNorth.add(textFieldjinrishibai);
        textFieldjinrishibai.setEditable(false);
        
        panelNorth.add(labeljinriyichang);
        panelNorth.add(textFieldjinriyichang);
        textFieldjinriyichang.setEditable(false);
        
        
        panelNorth.add(labelyue);
        panelNorth.add(textFieldyue);
        textFieldyue.setEditable(false);
        
        panelNorth.add(labelzongchazhi);
        panelNorth.add(textFieldzongchazhi);
        textFieldzongchazhi.setEditable(false);
        
        panelNorth.add(labeljinrichazhi);
        panelNorth.add(textFieldjinrichazhi);
        textFieldjinrichazhi.setEditable(false);
        
        
/*        panelNorth.add(labeltime);
        panelNorth.add(textFieldtime);
        textFieldjinrichazhi.setEditable(false);*/


	    
		
	    final JTable table = new JTable(tableMode);

        JScrollPane scroll = new JScrollPane(table);  
        
        TableCellRenderer tcr = new ColorTableCellRenderer(); 
        
        
        table.setDefaultRenderer(Color.class,tcr);
        
        
        
        container.add(scroll, BorderLayout.CENTER);  

        setVisible(false);  
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
        
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);  
        
        

        
        setBounds(100, 100, 1220, 630); 

    }  
    
    

    

    
    
    
  
    private class MyTableModel extends AbstractTableModel  
    {  
        /* 
         * 这里和刚才一样，定义列名和每个数据的值 
         */  
        String[] columnNames =  
        { "日期", "期数", "状态", "投注金额", "输赢", "异常情况", "笔数", "封盘差值" };  
        

        
        //Object[][] data = new Object[2][5];  
        
        

        
  
        /** 
         * 构造方法，初始化二维数组data对应的数据 
         */  
        public MyTableModel()  
        {  

        }  
  
        // 以下为继承自AbstractTableModle的方法，可以自定义  
        /** 
         * 得到列名 
         */  
        @Override  
        public String getColumnName(int column)  
        {  
            return columnNames[column];  
        }  
          
        /** 
         * 重写方法，得到表格列数 
         */  
        @Override  
        public int getColumnCount()  
        {  
            return columnNames.length;  
        }  
  
        /** 
         * 得到表格行数 
         */  
        @Override  
        public int getRowCount()  
        {  
            return detailsData.size();  
        }  
  
        /** 
         * 得到数据所对应对象 
         */  
        @Override  
        public Object getValueAt(int rowIndex, int columnIndex)  
        {  
            //return data[rowIndex][columnIndex];
        	return detailsData.elementAt(rowIndex)[columnIndex];
        }  
  
        /** 
         * 得到指定列的数据类型 
         */  
        @Override  
        public Class<?> getColumnClass(int columnIndex)  
        {  
            return detailsData.elementAt(0)[columnIndex].getClass();
        }  
  
        /** 
         * 指定设置数据单元是否可编辑.这里设置"姓名","学号"不可编辑 
         */  
        @Override  
        public boolean isCellEditable(int rowIndex, int columnIndex)  
        {  
            if (columnIndex < 2)  
                return false;  
            else  
                return true;  
        }  
          
        /** 
         * 如果数据单元为可编辑，则将编辑后的值替换原来的值 
         */  
        @Override  
        public void setValueAt(Object aValue, int rowIndex, int columnIndex)  
        {  
            detailsData.elementAt(rowIndex)[columnIndex] = aValue;  
            /*通知监听器数据单元数据已经改变*/  
            fireTableCellUpdated(rowIndex, columnIndex);  
        }  
        
        public void updateTable(){
        	fireTableDataChanged();
        }
        
  
    }  
    
    
}