package sql;
import java.sql.*;
public class sql {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Connection con=null;
		try
		{
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver"); 
			System.out.println("���ݿ����ӳɹ�");
		}
		catch(ClassNotFoundException e)
		{
			System.out.println("������������");  
            System.out.println(e.getMessage()); 
		}
		try
		{
		    con=DriverManager.getConnection("jdbc:sqlserver://localhost:1433;DatabaseName=cust","sa","135868");
			System.out.println("Success connect sql server!");  
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		Statement stmt = con.createStatement();//3��Statement �ӿ���Ҫͨ��Connection �ӿڽ���ʵ��������
		//stmt.executeUpdate("insert into student values(2,\'С��\',\'11102\',\'��\',\'111\',\'����\',\'�����רҵ\',\'�����ѧԺ\',\'xiaoming@126.com\')");
		stmt.executeUpdate("delete from student where name = 'Jery'");
		//stmt.executeUpdate("update student set name='Jery' where number = 2");
		ResultSet result = stmt.executeQuery("select name from student");
		while (result.next()){
			String name = result.getString("name");
			System.out.println(name);
		}
		//stmt.executeUpdate("insert into ��Ʒ values(\'Tom\',20,\'SH\')");
		//stmt.executeUpdate("update java_study.person set name='Jery' where age = 20");
		//stmt.executeUpdate("delete from java_study.person where age = 20");
        result.close();
        con.close(); // 4���ر����ݿ�
	}

}
