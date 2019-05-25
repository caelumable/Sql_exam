package sql;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.io.*;
import java.sql.*;

/**
 *
 * 功能：与数据库中的用户数据进行匹配 匹配成功的话显示成功对话框。
 * 
 */





public class LoginFrame extends JFrame implements ActionListener
{
 
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// 登录界面的GUI组件
	private JFrame jf = new JFrame("登录");
	private JPanel jp1=new JPanel();
	private JPanel jp2=new JPanel();
	private JPanel jp3=new JPanel();
	private JButton jb1=new JButton("注册");
	private JTextField userField = new JTextField(20);
	//private JTextField passField = new JTextField(20);
	private JPasswordField passField= new JPasswordField(20);
	private JButton loginButton = new JButton("登录");
	private String usernum="";
	private String userclass="";
	//教师所教课程和所教班级
	private String usersubgenre="";
	private String userteclass="";
	private String person="";
	private String userclassname="";
	
	LoginFrame(String person){
		this.person=person;
		if(person.equals("student"))
		this.userclassname="sclass";
		else 
		this.userclassname="tclass";
	}
	
	public void init()throws Exception
	{
		// 加载驱动
		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

		// 为登录按钮添加事件监听器
		loginButton.addActionListener(this);
		// 为注册按钮添加事件监听器
		jb1.addActionListener(this);
		
		class mylister extends WindowAdapter{
 
			@Override
			public void windowClosing(WindowEvent e) {
				
				System.exit(0);
			}
			
		}
		jf.addWindowListener(new mylister());
		jp1.add(new Label("用户名："));
		jp1.add(userField);
		jp2.add(new Label("密码："));
		jp2.add(passField);
		jp3.add(jb1);
		jp3.add(loginButton);
		jf.add(jp3,BorderLayout.SOUTH);
 
		jf.add(jp1,BorderLayout.NORTH);
		jf.add(jp2 , BorderLayout.CENTER);
		jf.pack();
		jf.setVisible(true);
		jf.setLocation(400, 200);
	}
	//注册用户和密码 （1）首先先检查数据库中是否有相应的数据，如果有的话提示"该用户存在，请直接登录。"
	private void cheak(String userName, String userPass) throws Exception
	{    
		if (validate(userField.getText(), passField.getText()))
		{
			JOptionPane.showMessageDialog(jf, "您已经有账号了请直接登录。");
		}
 
		else
		{
			//String sql="insert ignore into student values(?,?)";
			String sql="";
			if(person.equals("student"))
			 sql="insert into Slogin values(?,?)";
			else 
			  sql="insert into Tlogin values(?,?)";
			Connection conn=DriverManager.getConnection("jdbc:sqlserver://localhost:1433;DatabaseName=cust","sa","135868");
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userName);
			pstmt.setString(2, userPass);
		    pstmt.executeUpdate();
		    JOptionPane.showMessageDialog(jf, "注册成功请登录。。。。");
		}
			
		
	}
	//判断数据库中是否有该用户名和密码
	private boolean validate(String userName, String userPass)
	{
		String sql="";
		if(person.equals("student"))
		 sql="select * from Slogin where lognum= '"+userName+"' and password= '"+userPass+"'";
		else 
		  sql="select * from Tlogin where lognum= '"+userName+"' and password= '"+userPass+"'";
		
		//String sql="select * from Slogin where lognum='"+userName+"' and password='"+userPass+"'";
		try(
			Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;DatabaseName=cust","sa","135868");
			Statement pstmt = conn.createStatement();
				ResultSet rs=pstmt.executeQuery(sql))
		
		{	//如果查询的ResultSet里有超过一条的记录，则登录成功
				if (rs.next())
				{
					//成功以后得到登录者的班级，学号
					usernum=userName;
					
					//System.out.println(usernum);
					return true;
				}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}
	
	/*
	public static void main(String[] args) throws Exception
	{
		//new LoginFrame().init();

	}
	*/
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource()==loginButton)
		{
			System.out.println(userField.getText() +"\t"+passField.getText()+"\t"+validate(userField.getText(), passField.getText()));
			if (validate(userField.getText(), passField.getText()))
			{
				JOptionPane.showMessageDialog(jf, "登录成功");
				String sq="";
				//这里一定要主要空格，因为如果没有加空格到sql里面会出事情的
				if(person.equals("student"))
				 {
					sq="select "+ userclassname + " from " + 
				    person +" where sno='"+usernum+"'";
					Connection con = null;
					Statement ps;
					try {
						//获取班级号
						con = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;DatabaseName=cust","sa","135868");
						ps = con.createStatement();
						ResultSet r=ps.executeQuery(sq);
						while(r.next()){
							//获得使用者的班级
							userclass = r.getString(userclassname);
							//System.out.println(userclass);
						}
						//userclass=r.getString("sclass");
					} catch (SQLException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
				 }
				else {
				 sq="select "+ userclassname +",tgenre "+ " from " + person +" where tno='"+usernum+"'";
				 Connection con = null;
					Statement ps;
					try {
						//获取班级号
						con = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;DatabaseName=cust","sa","135868");
						ps = con.createStatement();
						ResultSet r=ps.executeQuery(sq);
						while(r.next()){
							//获得使用者的班级
							userclass = r.getString(userclassname);
							usersubgenre=r.getString("tgenre");
							//System.out.println(userclass);
						}
						//userclass=r.getString("sclass");
					} catch (SQLException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
				}
				 //String sq="select sclass from"+ person +"where sno='"+usernum+"'";
				
				
				 //this.dispose();  //关闭登陆界面 
				 //this.setVisible(false);
				jf.dispose();
				//new myFrame().setVisible(true); //打开主界面 
				//new myFrame().jf.setVisible(true);
				
				//如果是学生界面就登录学生界面
				if(person.equals("student"))
				{
				 myFrame myfra = null;
				//在创建操作界面的时候就先输入学号
				try {
					myfra = new myFrame(usernum,userclass);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				myfra.jf.setVisible(true);
				}
				else {
					TFrame fra = null;
					//在创建操作界面的时候就先输入学号
					try {
						fra = new TFrame(usernum,userclass,usersubgenre);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					fra.jf.setVisible(true);
				}
				
			}
			// 否则显示“登录失败”
			else
			{
				JOptionPane.showMessageDialog(jf, "登录失败");
			}
		}//登录按键的操作
		else if(e.getSource()==jb1)
		{
			System.out.println(userField.getText() +"\t"+passField.getText()+"\t"+validate(userField.getText(), passField.getText()));
			try {
				cheak(userField.getText(),passField.getText());
				
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}//注册按键操作
	}
	
}
