package dsn;
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


enum BATYPEINDEX{
	DATA,
	DN,
	BETCONTENT,
	BETAMOUNT	
}






public class DSNBetAmountWindow extends JFrame  
{  
  
   
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7254177860718209552L;

	private  Stack<Object[]> detailsData = new Stack<Object[]>();

    MyTableModel tableMode = new MyTableModel();


	public DSNBetAmountWindow()  
    {  
		//setTitle("Ͷע����������");  
		
        intiComponent();  
        
    }  
	
	
	

	
	
	public void addData(String data, String drawNumber, String betContent, String betAmount){
		
		try{
/*			for(int i = 0; i<detailsData.size(); i++){ //todo �Ż�
				if(((String)detailsData.elementAt(i)[1]).equals(drawNumber)){
					return;
				}
			}*/
			
			
			if(detailsData.size() >= 2500){
				while(detailsData.size() > 1000){
					detailsData.remove(detailsData.size()-1);
				}
			}
			
			
			
			
			
			Object[] newData = new Object[4];
			newData[BATYPEINDEX.DATA.ordinal()] = data;
			newData[BATYPEINDEX.DN.ordinal()] = drawNumber;
			
			newData[BATYPEINDEX.BETCONTENT.ordinal()] = betContent;
			
			newData[BATYPEINDEX.BETAMOUNT.ordinal()] = betAmount;
			
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
	

  
    /** 
     * ��ʼ��������� 
     */  
    private void intiComponent()  
    {  

		final Container container = getContentPane();
		

		
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
         * ����͸ղ�һ���������ÿ����ݵ�ֵ 
         */  
        String[] columnNames =  
        { "����", "����", "Ͷע����", "��ע���  %1"};  
        

        
        //Object[][] data = new Object[2][5];  
        
        

        
  
        /** 
         * ���췽������ʼ����ά����data��Ӧ����� 
         */  
        public MyTableModel()  
        {  

        }  
  
        // ����Ϊ�̳���AbstractTableModle�ķ����������Զ���  
        /** 
         * �õ����� 
         */  
        @Override  
        public String getColumnName(int column)  
        {  
            return columnNames[column];  
        }  
          
        /** 
         * ��д�������õ�������� 
         */  
        @Override  
        public int getColumnCount()  
        {  
            return columnNames.length;  
        }  
  
        /** 
         * �õ�������� 
         */  
        @Override  
        public int getRowCount()  
        {  
            return detailsData.size();  
        }  
  
        /** 
         * �õ�������Ӧ���� 
         */  
        @Override  
        public Object getValueAt(int rowIndex, int columnIndex)  
        {  
            //return data[rowIndex][columnIndex];
        	return detailsData.elementAt(rowIndex)[columnIndex];
        }  
  
        /** 
         * �õ�ָ���е�������� 
         */  
        @Override  
        public Class<?> getColumnClass(int columnIndex)  
        {  
            return detailsData.elementAt(0)[columnIndex].getClass();
        }  
  
        /** 
         * ָ��������ݵ�Ԫ�Ƿ�ɱ༭.��������"����","ѧ��"���ɱ༭ 
         */  
        @Override  
        public boolean isCellEditable(int rowIndex, int columnIndex)  
        {  
            return false;
        }  
          
        /** 
         * �����ݵ�ԪΪ�ɱ༭���򽫱༭���ֵ�滻ԭ����ֵ 
         */  
        @Override  
        public void setValueAt(Object aValue, int rowIndex, int columnIndex)  
        {  
            detailsData.elementAt(rowIndex)[columnIndex] = aValue;  
            /*֪ͨ��������ݵ�Ԫ����Ѿ��ı�*/  
            fireTableCellUpdated(rowIndex, columnIndex);  
        }  
        
        public void updateTable(){
        	fireTableDataChanged();
        }
        
  
    }  
    
    
}