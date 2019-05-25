package sql;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.io.*;
import java.sql.*;

/**
 *
 * ���ܣ������ݿ��е��û����ݽ���ƥ�� ƥ��ɹ��Ļ���ʾ�ɹ��Ի���
 * 
 */





public class LoginFrame extends JFrame implements ActionListener
{
 
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// ��¼�����GUI���
	private JFrame jf = new JFrame("��¼");
	private JPanel jp1=new JPanel();
	private JPanel jp2=new JPanel();
	private JPanel jp3=new JPanel();
	private JButton jb1=new JButton("ע��");
	private JTextField userField = new JTextField(20);
	//private JTextField passField = new JTextField(20);
	private JPasswordField passField= new JPasswordField(20);
	private JButton loginButton = new JButton("��¼");
	private String usernum="";
	private String userclass="";
	//��ʦ���̿γ̺����̰༶
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
		// ��������
		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

		// Ϊ��¼��ť����¼�������
		loginButton.addActionListener(this);
		// Ϊע�ᰴť����¼�������
		jb1.addActionListener(this);
		
		class mylister extends WindowAdapter{
 
			@Override
			public void windowClosing(WindowEvent e) {
				
				System.exit(0);
			}
			
		}
		jf.addWindowListener(new mylister());
		jp1.add(new Label("�û�����"));
		jp1.add(userField);
		jp2.add(new Label("���룺"));
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
	//ע���û������� ��1�������ȼ�����ݿ����Ƿ�����Ӧ�����ݣ�����еĻ���ʾ"���û����ڣ���ֱ�ӵ�¼��"
	private void cheak(String userName, String userPass) throws Exception
	{    
		if (validate(userField.getText(), passField.getText()))
		{
			JOptionPane.showMessageDialog(jf, "���Ѿ����˺�����ֱ�ӵ�¼��");
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
		    JOptionPane.showMessageDialog(jf, "ע��ɹ����¼��������");
		}
			
		
	}
	//�ж����ݿ����Ƿ��и��û���������
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
		
		{	//�����ѯ��ResultSet���г���һ���ļ�¼�����¼�ɹ�
				if (rs.next())
				{
					//�ɹ��Ժ�õ���¼�ߵİ༶��ѧ��
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
				JOptionPane.showMessageDialog(jf, "��¼�ɹ�");
				String sq="";
				//����һ��Ҫ��Ҫ�ո���Ϊ���û�мӿո�sql�����������
				if(person.equals("student"))
				 {
					sq="select "+ userclassname + " from " + 
				    person +" where sno='"+usernum+"'";
					Connection con = null;
					Statement ps;
					try {
						//��ȡ�༶��
						con = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;DatabaseName=cust","sa","135868");
						ps = con.createStatement();
						ResultSet r=ps.executeQuery(sq);
						while(r.next()){
							//���ʹ���ߵİ༶
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
						//��ȡ�༶��
						con = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;DatabaseName=cust","sa","135868");
						ps = con.createStatement();
						ResultSet r=ps.executeQuery(sq);
						while(r.next()){
							//���ʹ���ߵİ༶
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
				
				
				 //this.dispose();  //�رյ�½���� 
				 //this.setVisible(false);
				jf.dispose();
				//new myFrame().setVisible(true); //�������� 
				//new myFrame().jf.setVisible(true);
				
				//�����ѧ������͵�¼ѧ������
				if(person.equals("student"))
				{
				 myFrame myfra = null;
				//�ڴ������������ʱ���������ѧ��
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
					//�ڴ������������ʱ���������ѧ��
					try {
						fra = new TFrame(usernum,userclass,usersubgenre);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					fra.jf.setVisible(true);
				}
				
			}
			// ������ʾ����¼ʧ�ܡ�
			else
			{
				JOptionPane.showMessageDialog(jf, "��¼ʧ��");
			}
		}//��¼�����Ĳ���
		else if(e.getSource()==jb1)
		{
			System.out.println(userField.getText() +"\t"+passField.getText()+"\t"+validate(userField.getText(), passField.getText()));
			try {
				cheak(userField.getText(),passField.getText());
				
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}//ע�ᰴ������
	}
	
}
