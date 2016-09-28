import java.awt.BorderLayout;
import java.awt.Container;  
 
import java.awt.GridLayout;
import java.awt.event.ActionEvent;  
import java.awt.event.ActionListener;  
import java.util.Vector;  
  
import java.awt.Color;


import javax.swing.JFrame;  
import javax.swing.JLabel;  
import javax.swing.JPanel;  
import javax.swing.JScrollPane;  
import javax.swing.JTable;  
import javax.swing.JTextField;  
import javax.swing.table.DefaultTableModel; 
import javax.swing.table.AbstractTableModel; 




import java.util.Date;      

import javax.swing.Timer;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.concurrent.atomic.AtomicLong;



public class DSNBJSCDetailsWindow extends JFrame  
{  
  
   
	private static final long serialVersionUID = 508685938515369544L;
	
	private static Vector<Object[]> detailsData = new Vector<Object[]>();
	
	
    
    private JLabel labelA;
    final private JTextField textFieldA = new JTextField(15);  
    

	

	public DSNBJSCDetailsWindow()  
    {  

		
        intiComponent();  
        
        Object[] data = new Object[8];
        data[0] = "201618";
        data[1] = "577863";
        data[2] = new Color(255, 0, 0);
        data[3] = new Boolean(true);
        data[4] = "577863";
        data[5] = "577863";
        data[6] = "577863";
        data[7] = "577863";
        
        addData(data);

    }  
	
	
	public static void addData(Object[] a){
		detailsData.add(a);
	}
	
  
    /** 
     * 初始化窗体组件 
     */  
    private void intiComponent()  
    {  

        
        
		final Container container = getContentPane();
		
		container.setLayout(new BorderLayout());
		
		JPanel panelNorth = new JPanel(new GridLayout(1, 6));

        container.add(panelNorth, BorderLayout.NORTH);  
        
        labelA = new JLabel("总期数: ");   
        textFieldA.setEditable(false);
        
        panelNorth.add(labelA, BorderLayout.WEST);
        panelNorth.add(textFieldA, BorderLayout.EAST);
        
        textFieldA.setText("100");
	    
	    MyTableModel tableMode = new MyTableModel();
		
	    final JTable table = new JTable(tableMode);

        JScrollPane scroll = new JScrollPane(table);  
        container.add(scroll, BorderLayout.CENTER);  

        setVisible(true);  
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
        
        
        setBounds(100, 100, 1220, 630); 

    }  
    
    
    
    
    
  
    private class MyTableModel extends AbstractTableModel  
    {  
        /* 
         * 这里和刚才一样，定义列名和每个数据的值 
         */  
        String[] columnNames =  
        { "日期", "期数", "状态", "输赢", "异常情况", "笔数", "差值", "总差值" };  
        

        
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
  
    }  
    
    
}