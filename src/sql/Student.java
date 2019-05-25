package sql;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


class myFrame extends JFrame implements ActionListener,ItemListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	JFrame jf;
	JPanel jpanel;
	private JRadioButton jr1;
    private JRadioButton jr2;
    private JRadioButton jr3; 
    private JRadioButton jr4; 
    private JButton jb1, jb2;
	JTextArea jta = null;
	JScrollPane jscrollPane;
	//��������ҪΪ�գ������д�Ļ�strAnswer�ַ������ַ�Ϊ��null��,��Ԥ��Ĳ�ƥ��
	String strAnswer="";
	String selfAnswer="";
	File testFile;
    Pattern pattern=Pattern.compile("[A-Z]");//ƥ���
    Matcher matcher;
    String s;
    FileReader inOne;
    BufferedReader inTwo;
    boolean flag=true;
    
    String sno="";
    String sclass="";
    String genre="";
    String title="";
    
    private void examContent() throws IOException
    {
    	while((s=inTwo.readLine())!=null){
			if(s.startsWith("A")||s.startsWith("B")||s.startsWith("C")||s.startsWith("D")){
            	jta.append("\r\n");
            	jta.append(s);
            }
			
			else if(s.startsWith("���𰸡�")==false){      //�Ǵ���ֱ�����
                jta.append(s);
            }
            else {                                 //�ڴ𰸴�ͣ������ΪҪ�ڴ������
                //�Ȼ�ȡ�𰸣��������ж���Ҫ���뼸��
                matcher=pattern.matcher(s);
                while(matcher.find()){
                    strAnswer+=matcher.group();
                }
                System.out.println("strAnswer:"+strAnswer);
                break;
            }
		}
    }
    
    
    
    
	public myFrame(String sno,String sclass) throws Exception {
 
		this.sno=sno;
		this.sclass=sclass;
		System.out.println(sno);
		System.out.println(sclass);
		//��ʼ�����
		testFile=new File("exam.txt"); 
        inOne=new FileReader(testFile);
        inTwo=new BufferedReader(inOne);
        
        //����UI����
		jf = new JFrame("�������");
		Container contentPane = jf.getContentPane();
		contentPane.setLayout(new BorderLayout());
 
		jta = new JTextArea(10, 15);
		jta.setTabSize(4);
		jta.setFont(new Font("�꿬��", Font.BOLD, 16));
		jta.setLineWrap(true);// �����Զ����й���
		jta.setWrapStyleWord(true);// ������в����ֹ���
		jta.setEditable(false);//ֻ���ı���
		jta.setBackground(Color.white);
 
		jscrollPane = new JScrollPane(jta);
		jpanel = new JPanel();
		jpanel.setLayout(new GridLayout(1, 6));
 
		/*
		jb1 = new JButton("A");
		jb1.addActionListener(this);
		jb2 = new JButton("B");
		jb2.addActionListener(this);
		jb3 = new JButton("C");
		jb3.addActionListener(this);
		jb4 = new JButton("D");
		jb4.addActionListener(this);
 	*/
		jb1 = new JButton("�ύ");
		jb1.addActionListener(this);
		jb2 = new JButton("��һ��");
		jb2.addActionListener(this);
		
		ButtonGroup group = new ButtonGroup(); 
		jr1 = new JRadioButton("A");
		jr1.addItemListener(this);
		jr2 = new JRadioButton("B");
		jr2.addItemListener(this);
		jr3 = new JRadioButton("C");
		jr3.addItemListener(this);
		jr4 = new JRadioButton("D");
		jr4.addItemListener(this);
		group.add(this.jr1);
        group.add(this.jr2);
        group.add(this.jr3);
        group.add(this.jr4);
		
		jpanel.add(jr1);
		jpanel.add(jr2);
		jpanel.add(jr3);
		jpanel.add(jr4);
		jpanel.add(jb1);
		jpanel.add(jb2);
		
		contentPane.add(jscrollPane, BorderLayout.CENTER);
		contentPane.add(jpanel, BorderLayout.SOUTH);
 
		jf.setSize(500, 300);
		jf.setLocation(400, 200);
		jf.setVisible(true);
 
		//���ȶ�����
		examContent();
		
		
		jf.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
	}
 
	// ���ǽӿ�ActionListener�ķ���actionPerformed
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == jb1) {
			try {
				check_answer(strAnswer);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} else if (e.getSource() == jb2) {
			//�����һ��������
			flag=true;
			jta.setText("");
			strAnswer="";
			try {
				examContent();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} 
		}
	}
	
	
	//������������浱ǰ�Ĵ𰸵�
	public void itemStateChanged(ItemEvent e) {
		if (e.getSource() == jr1) {
			selfAnswer="A";
		} else if (e.getSource() == jr2) {
			selfAnswer="B";
		} else if (e.getSource() == jr3) {
			selfAnswer="C";
		}
		else if (e.getSource() == jr4) {
			selfAnswer="D";
		}
		System.out.println("selfAnswer:"+selfAnswer);
	}
	
	private void showstep()
	{
		
	}
	
	
	private void addmistake(String genre,String title) throws SQLException
	{
		Connection con=null;
		try
		{
		    con=DriverManager.getConnection("jdbc:sqlserver://localhost:1433;DatabaseName=cust","sa","135868");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		Statement stmt = con.createStatement();
		genre=genre.replaceAll("�����", "");
		title=title.replaceAll("�����͡�", "");
		System.out.println(genre);
		System.out.println(title);
		//�Ѵ����������Ŀд�����ݿ�
		stmt.executeUpdate("insert into mistake(sno,sclass,sgenre,stitle) values('"
				+ sno +"',"+"'"+sclass+"',"+"'"+genre+"',"+"'"+title+"')");
	}
	
	//�����Ƿ����
	private void check_answer(String strAnswer2) throws IOException {
		//flag=true ��ʾ�Ѿ����������Ŀ��
		if(flag) {
		if(selfAnswer.equals(strAnswer))
		{
			JOptionPane.showMessageDialog(jf, "�ش���ȷ");
			flag=false;
			while((s=inTwo.readLine())!=null){
				//��Ϊ������һ��������һ�У����Ծ��ǲ������һ�У������
			    if(s.startsWith("�����͡�")==false){      //�Ǵ���ֱ�����
			       jta.append("\r\n");
			    	jta.append(s);
			        //break;
			    }
			    else {//����Ѿ��������һ�У�Ҳ���������Ҫ��һ���Ƴ�ѭ��
			    	jta.append("\r\n");
			    	//������������͵���һ�е�
			    	jta.append(s);
			    	break;
			    }
			}
		}
		else {
			JOptionPane.showMessageDialog(jf, "�ش����");
			flag=false;
			//��������������Ǵӽ�����ʼ��
			while((s=inTwo.readLine())!=null){
				//Ҫ���������͵����ݱ�������������Ҫ�жϣ���Ϊ��������һ������������������Ƴ�
			    
				//ע�⣬������ʾÿһ����Ŀ���߼��ǡ����Ϊһ��������һ�У��������߼������ᵼ�³�������
				if(s.startsWith("�����")==true){      //�Ǵ���ֱ�����
			       jta.append("\r\n");
			    	jta.append(s);
			        genre=s;
			    }
			    else if(s.startsWith("�����͡�")==true) {
			    	jta.append("\r\n");
			    	jta.append(s);
			        title=s;
			        break;
			    }
			    else {//����Ѿ��������һ�У�Ҳ���������Ҫ��һ���Ƴ�ѭ��
			    	jta.append("\r\n");
			    	jta.append(s);
			    }
			}
			
			//todo �Ѵ�������ͼӵ����ݿ⵱��
			try {
				addmistake(genre,title);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		}
		else {
			JOptionPane.showMessageDialog(jf, "�����ظ�����");
		}
	}
 

	
}



public class Student {

	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		/*
		String sn="160511101";
		String sc="1605111";
		try {
			new myFrame(sn,sc);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		new LoginFrame("student").init();
	}
		

}
