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
     * ��ʼ��������� 
     */  
    private void intiComponent()  
    {  

        
        
		final Container container = getContentPane();
		
		container.setLayout(new BorderLayout());
		
		JPanel panelNorth = new JPanel(new GridLayout(1, 6));

        container.add(panelNorth, BorderLayout.NORTH);  
        
        labelA = new JLabel("������: ");   
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
         * ����͸ղ�һ��������������ÿ�����ݵ�ֵ 
         */  
        String[] columnNames =  
        { "����", "����", "״̬", "��Ӯ", "�쳣���", "����", "��ֵ", "�ܲ�ֵ" };  
        

        
        //Object[][] data = new Object[2][5];  
        
        

        
  
        /** 
         * ���췽������ʼ����ά����data��Ӧ������ 
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
         * �õ���������Ӧ���� 
         */  
        @Override  
        public Object getValueAt(int rowIndex, int columnIndex)  
        {  
            //return data[rowIndex][columnIndex];
        	return detailsData.elementAt(rowIndex)[columnIndex];
        }  
  
        /** 
         * �õ�ָ���е��������� 
         */  
        @Override  
        public Class<?> getColumnClass(int columnIndex)  
        {  
            return detailsData.elementAt(0)[columnIndex].getClass();
        }  
  
        /** 
         * ָ���������ݵ�Ԫ�Ƿ�ɱ༭.��������"����","ѧ��"���ɱ༭ 
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
         * ������ݵ�ԪΪ�ɱ༭���򽫱༭���ֵ�滻ԭ����ֵ 
         */  
        @Override  
        public void setValueAt(Object aValue, int rowIndex, int columnIndex)  
        {  
            detailsData.elementAt(rowIndex)[columnIndex] = aValue;  
            /*֪ͨ���������ݵ�Ԫ�����Ѿ��ı�*/  
            fireTableCellUpdated(rowIndex, columnIndex);  
        }  
  
    }  
    
    
}