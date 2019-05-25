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
	//这两个都要为空，如果不写的话strAnswer字符串的字符为“null”,与预想的不匹配
	String strAnswer="";
	String selfAnswer="";
	File testFile;
    Pattern pattern=Pattern.compile("[A-Z]");//匹配答案
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
			
			else if(s.startsWith("【答案】")==false){      //非答案行直接输出
                jta.append(s);
            }
            else {                                 //在答案处停留，因为要在此输入答案
                //先获取答案，按个数判断需要输入几次
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
		//初始化题库
		testFile=new File("exam.txt"); 
        inOne=new FileReader(testFile);
        inTwo=new BufferedReader(inOne);
        
        //界面UI设置
		jf = new JFrame("答题界面");
		Container contentPane = jf.getContentPane();
		contentPane.setLayout(new BorderLayout());
 
		jta = new JTextArea(10, 15);
		jta.setTabSize(4);
		jta.setFont(new Font("标楷体", Font.BOLD, 16));
		jta.setLineWrap(true);// 激活自动换行功能
		jta.setWrapStyleWord(true);// 激活断行不断字功能
		jta.setEditable(false);//只读文本框
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
		jb1 = new JButton("提交");
		jb1.addActionListener(this);
		jb2 = new JButton("下一题");
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
 
		//首先读两行
		examContent();
		
		
		jf.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
	}
 
	// 覆盖接口ActionListener的方法actionPerformed
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == jb1) {
			try {
				check_answer(strAnswer);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} else if (e.getSource() == jb2) {
			//点击下一题后的任务
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
	
	
	//这个是用来保存当前的答案的
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
		genre=genre.replaceAll("【类别】", "");
		title=title.replaceAll("【类型】", "");
		System.out.println(genre);
		System.out.println(title);
		//把错误的类型题目写入数据库
		stmt.executeUpdate("insert into mistake(sno,sclass,sgenre,stitle) values('"
				+ sno +"',"+"'"+sclass+"',"+"'"+genre+"',"+"'"+title+"')");
	}
	
	//检查答案是否出错
	private void check_answer(String strAnswer2) throws IOException {
		//flag=true 表示已经做过这道题目了
		if(flag) {
		if(selfAnswer.equals(strAnswer))
		{
			JOptionPane.showMessageDialog(jf, "回答正确");
			flag=false;
			while((s=inTwo.readLine())!=null){
				//因为类型是一道题的最后一行，所以就是不是最后一行，就输出
			    if(s.startsWith("【类型】")==false){      //非答案行直接输出
			       jta.append("\r\n");
			    	jta.append(s);
			        //break;
			    }
			    else {//如果已经到达最后一行，也输出，不过要加一个推出循环
			    	jta.append("\r\n");
			    	//这里是输出类型的那一行的
			    	jta.append(s);
			    	break;
			    }
			}
		}
		else {
			JOptionPane.showMessageDialog(jf, "回答错误");
			flag=false;
			//这里输出的行数是从解析开始的
			while((s=inTwo.readLine())!=null){
				//要把类别和类型的内容保存起来，所以要判断，因为类别是最后一个，所以如果碰到就推出
			    
				//注意，这里显示每一道题目的逻辑是“类别”为一道题的最后一行，如果这个逻辑出错，会导致程序不正常
				if(s.startsWith("【类别】")==true){      //非答案行直接输出
			       jta.append("\r\n");
			    	jta.append(s);
			        genre=s;
			    }
			    else if(s.startsWith("【类型】")==true) {
			    	jta.append("\r\n");
			    	jta.append(s);
			        title=s;
			        break;
			    }
			    else {//如果已经到达最后一行，也输出，不过要加一个推出循环
			    	jta.append("\r\n");
			    	jta.append(s);
			    }
			}
			
			//todo 把错题的类型加到数据库当中
			try {
				addmistake(genre,title);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		}
		else {
			JOptionPane.showMessageDialog(jf, "不能重复答题");
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
