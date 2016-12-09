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

//���幹����

public ColorTableCellRenderer ()

{

//���ñ�ǩΪ��͸��״̬

this.setOpaque(true);

//���ñ�ǩ���ı����뷽ʽΪ����

this.setHorizontalAlignment(JLabel.CENTER);

}

//ʵ�ֻ�ȡ���ֿؼ���getTableCellRendererComponent����

public Component getTableCellRendererComponent(JTable table,Object value,

           boolean isSelected,boolean hasFocus,int row,int column)

{           

//��ȡҪ���ֵ���ɫ

Color c=(Color)value;

//��ݲ���value���ñ���ɫ

this.setBackground(c);



return this;   

}

 }   



public class DSNDataDetailsWindow extends JFrame  
{  
  
   
	private static final long serialVersionUID = 508685938515369544L;
	
	private  Stack<Object[]> detailsData = new Stack<Object[]>();
	
	
	
	
    
    private JLabel labelzongqishu = new JLabel("������:");
    private JTextField textFieldzongqishu = new JTextField(15);  
    
    private JLabel labelzongshibai = new JLabel("��ʧ��:");
    private JTextField textFieldzongshibai = new JTextField(15);  
    
    private JLabel labelzongyichang = new JLabel("���쳣:");
    private JTextField textFieldzongyichang = new JTextField(15);  
    
    private JLabel labeljinriqishu = new JLabel("��������:");
    private JTextField textFieldjinriqishu = new JTextField(15);  
    
    private JLabel labeljinrishibai = new JLabel("����ʧ��:");
    private JTextField textFieldjinrishibai = new JTextField(15);  
    
    private JLabel labeljinriyichang = new JLabel("�����쳣:");
    private JTextField textFieldjinriyichang = new JTextField(15);  

    private JLabel labelyue = new JLabel("���:");
    private JTextField textFieldyue = new JTextField(15);  
    
    private JLabel labeljinrishuying = new JLabel("������Ӯ:");
    private JTextField textFieldjinrishuying = new JTextField(15);  
    
    
    private JLabel labelzongchazhi = new JLabel("�ܲ�ֵ:");
    private JTextField textFieldzongchazhi = new JTextField(15);  
    
    private JLabel labeljinrichazhi = new JLabel("���ղ�ֵ:");
    private JTextField textFieldjinrichazhi = new JTextField(15);  
    
    
/*    private JLabel labeltime = new JLabel("�����:");
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
    		labeltime.setText("�ѷ��̣��࿪��:");
    	}
    	else {
    		labeltime.setText("�����:");
    	}
    }*/
    
    
    
	

	public DSNDataDetailsWindow()  
    {  
		//setTitle("Ͷע����������");  
		
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
			for(int i = 0; i<detailsData.size(); i++){ //todo �Ż�
				if(((String)detailsData.elementAt(i)[1]).equals(drawNumber)){
					return;
				}
			}
			
			
			if(detailsData.size() >= 500){
				while(detailsData.size() > 300){
					detailsData.remove(detailsData.size()-1);
				}
			}
			
			
			
			
			
			Object[] newData = new Object[8];
			newData[TYPEINDEX.DATA.ordinal()] = data;
			newData[TYPEINDEX.DN.ordinal()] = drawNumber;
			if(stat == 0){
				newData[TYPEINDEX.STATC.ordinal()] = new Color(100, 255, 100);
				newData[TYPEINDEX.BETAMOUNT.ordinal()] = betAmount;
				newData[TYPEINDEX.PROFIT.ordinal()] = "0";
				newData[TYPEINDEX.STATS.ordinal()] = "�ɹ�";
				newData[TYPEINDEX.COUNT.ordinal()] = count;
				newData[TYPEINDEX.DVALUE.ordinal()] = "0";
			}else if(stat == 1){
				newData[TYPEINDEX.STATC.ordinal()] = new Color(255, 100, 100);
				newData[TYPEINDEX.BETAMOUNT.ordinal()] = betAmount;
				newData[TYPEINDEX.PROFIT.ordinal()] = "---";
				newData[TYPEINDEX.STATS.ordinal()] = "ʧ��";
				newData[TYPEINDEX.COUNT.ordinal()] = count;
				newData[TYPEINDEX.DVALUE.ordinal()] = "---";
			}
			else if(stat == 2){
				newData[TYPEINDEX.STATC.ordinal()] = new Color(100, 100, 255);
				newData[TYPEINDEX.BETAMOUNT.ordinal()] = betAmount;
				newData[TYPEINDEX.PROFIT.ordinal()] = "---";
				newData[TYPEINDEX.STATS.ordinal()] = "δ֪";
				newData[TYPEINDEX.COUNT.ordinal()] = count;
				newData[TYPEINDEX.DVALUE.ordinal()] = "---";
			}
			else{//©Ͷ
				newData[TYPEINDEX.STATC.ordinal()] = new Color(255, 100, 100);
				newData[TYPEINDEX.BETAMOUNT.ordinal()] = "---";
				newData[TYPEINDEX.PROFIT.ordinal()] = "---";
				newData[TYPEINDEX.STATS.ordinal()] = "©Ͷ";
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
							detailsData.elementAt(i)[TYPEINDEX.STATS.ordinal()] = "�ɹ�";
						}else if(stat == 1){
							detailsData.elementAt(i)[TYPEINDEX.STATC.ordinal()] = new Color(255, 100, 100);
							detailsData.elementAt(i)[TYPEINDEX.STATS.ordinal()] = "ʧ��";
						}
					}else if(index == TYPEINDEX.COUNT.ordinal()){
						int count = Integer.parseInt((String)detailsData.elementAt(i)[TYPEINDEX.COUNT.ordinal()]);
						int actualCount = Integer.parseInt(value);
						
						if(actualCount > count){
							detailsData.elementAt(i)[TYPEINDEX.STATC.ordinal()] = new Color(255, 100, 100);
							detailsData.elementAt(i)[TYPEINDEX.STATS.ordinal()] = "��Ͷ";
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
     * ��ʼ��������� 
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
         * ����͸ղ�һ���������ÿ����ݵ�ֵ 
         */  
        String[] columnNames =  
        { "����", "����", "״̬", "Ͷע���", "��Ӯ", "�쳣���", "����", "���̲�ֵ" };  
        

        
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