package sql;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

class TFrame extends JFrame implements ActionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JFrame jf;
	JPanel jpanel;
    private JButton jb1, jb2;
	JTextArea jta = null;
	JScrollPane jscrollPane;
	private String tno="";
	private String tclass="";
	JTextField jtx=null;
	private String tgenre="";
	public TFrame(String tno,String tclass,String tgenre) throws Exception {
		 
		this.tno=tno;
		this.tclass=tclass;
		this.tgenre=tgenre;
		System.out.println(tno);
		System.out.println(tclass);
		
        
        //界面UI设置
		jf = new JFrame("教师查看界面");
		Container contentPane = jf.getContentPane();
		contentPane.setLayout(new BorderLayout());
 
		jta = new JTextArea(10, 15);
		jta.setTabSize(4);
		jta.setFont(new Font("标楷体", Font.BOLD, 16));
		jta.setLineWrap(true);// 激活自动换行功能
		jta.setWrapStyleWord(true);// 激活断行不断字功能
		jta.setEditable(false);//只读文本框
		jta.setBackground(Color.white);
		
		//jta.setBounds(200,0,200,500);
		
		jtx=new JTextField("请输入学号：");
		
		jscrollPane = new JScrollPane(jta);
		jpanel = new JPanel();
		jpanel.setLayout(new GridLayout(1, 3));
 
	
		jb1 = new JButton("查看全部");
		jb1.addActionListener(this);
		jb2 = new JButton("查看学生");
		jb2.addActionListener(this);
		
		//jb1.setBounds(50, 60, 30,10 );
		//jb1.setBounds(50, 100, 30, 10);
	
		jpanel.add(jb1);
		jpanel.add(jtx);
		jpanel.add(jb2);
		
		contentPane.add(jscrollPane, BorderLayout.CENTER);
		contentPane.add(jpanel, BorderLayout.SOUTH);
 
		jf.setSize(400, 500);
		jf.setLocation(400, 200);
		jf.setVisible(true);
 
		
		jf.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
	}


	private void queryAll() throws SQLException
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
		//String sql="select sname,stitle from mistake,student where student.sno=mistake.sno and "
			//	+ " mistake.sclass='"+tclass+"'";
		String sql="exec func '"+tclass+"',"+"'"+tgenre+"'";
		Statement ps = con.createStatement();
		ResultSet r=ps.executeQuery(sql);
		
		String sname="";
		String stitle="";
		int cnt=0;
		String rs=null;
		rs=(String.format("%1$-21s", "姓名")+String.format("%1$-21s", "题目类型")
		+String.format("%1$-21s", "数量")+"\r\n");
		
		jta.append(rs);
		
		while(r.next()){
			sname = r.getString("sname");
			stitle = r.getString("stitle");
			cnt=r.getInt("cnt");
			rs=(String.format("%1$-21s", sname)+String.format("%1$-21s", stitle)
			+String.format("%1$-21s", cnt)+"\r\n");
			jta.append(rs);

		}
		
	}
	
	private void querySomeone(String sno) throws SQLException
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
		
		/*
		String sql="select sname,stitle from mistake,student where mistake.sno= '"+sno+"'"+
				" and student.sno=mistake.sno and "
				+ " mistake.sclass='"+tclass+"'";
		*/
		
		String sql="exec func1 '"+tclass+"',"+"'"+sno+"','"+tgenre+"'";
		//Statement ps = con.createStatement();
		//ResultSet r=ps.executeQuery(sql);
		
		//因为要使用r.previous();那么结果集要用两次，所以需要设置一下
		Statement ps=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);	
		con.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
		ResultSet r=ps.executeQuery(sql);
		
		String sname="";
		String stitle="";
		int cnt=0;
		String rs=null;
		rs=(String.format("%1$-21s", "姓名")+String.format("%1$-21s", "题目类型")
		+String.format("%1$-21s", "数量")+"\r\n");
		
		//jta.append("姓名         题目类型           数量"+"\r\n");
		jta.append(rs);
		
		
		if(r.next()){
			 r.previous();
			while(r.next()){
				sname = r.getString("sname");
				stitle = r.getString("stitle");
				cnt=r.getInt("cnt");
				rs=(String.format("%1$-21s", sname)+String.format("%1$-21s", stitle)
				+String.format("%1$-21s", cnt)+"\r\n");
				//jta.append(sname+"     "+stitle+"    "+cnt+"\r\n");
				jta.append(rs);
				sname="";
				stitle="";
				//System.out.println(userclass);
			}
		}
		else
		{
			JOptionPane.showMessageDialog(jf, "请确认学号是否正确");
		}
		
		
		
		
		
		
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == jb1)
		{
			//首先把所以的内容都清空
			jta.setText("");
			try {
				queryAll();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		else if(e.getSource() == jb2)
		{
			jta.setText("");
			try {
				String sno=jtx.getText();
				System.out.println("\r\n"+sno);
				querySomeone(sno);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				
				e1.printStackTrace();
			}
		}
	}
}


public class Teacher {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		//以下是测试数据
		String tn="t1605111";
		String tc="1605111";
		String tg="网络";
		//new TFrame(tn,tc,tg);
		new LoginFrame("teacher").init();
	}

}
