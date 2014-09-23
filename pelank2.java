import java.io.*; 
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class pelank2 {
	ArrayList<Integer> _pid1=new ArrayList<>();
	ArrayList<Integer> _pid=new ArrayList<>();
	ArrayList<String> _name=new ArrayList<>();
	
public static void main(String[] args) throws IOException {
        pelank2 courseControler = new pelank2();
        Connection conn = null;
        conn = courseControler.connectDB();
        courseControler.creatTable(conn);
        courseControler.operations("transfile.txt",conn);
        courseControler.deleteTable(conn);
        courseControler.closeConnection(conn);
    }
	public void operations(String inputTransfile,Connection conn) throws IOException,IndexOutOfBoundsException
	{
		
		BufferedReader br=new BufferedReader(new FileReader(inputTransfile));
		String sCurrentLine;
		while ((sCurrentLine = br.readLine()) != null) {
			_pid.clear();
			_pid1.clear();
			_name.clear();
			String[] split=sCurrentLine.split(" ");
			String transCode=split[0];
			if(transCode.equals("1"))
			{
				String CourseId=split[1];
				int CoursId=Integer.parseInt(CourseId);
				String sql1="DELETE FROM `courses`.`PrerequisiteCourse` WHERE cid="+CoursId;
				String sql2="DELETE FROM `courses`.`PrerequisiteCourse` WHERE pid="+CoursId;
				String sql3="DELETE FROM `courses`.`Course` WHERE cid="+CoursId;
				Statement stmt = null;
				try {
		            stmt = conn.createStatement();
		            stmt.execute(sql1);
		            stmt.execute(sql2);
		            int i=stmt.executeUpdate(sql3);
		            if(i==0)
		            	 System.out.println("error");
		            else
		            	System.out.println("done");
		            stmt.close();
		        } catch (SQLException e) {
		        	 e.printStackTrace();
		        }
				
			}
			if(transCode.equals("2"))
			{
				String CourseId=split[1];
				int CoursId=Integer.parseInt(CourseId);
				String CourseName=split[2];
				String CourseCapacity=split[3];
				int CoursCapacity=Integer.parseInt(CourseCapacity);
				String sql1="INSERT INTO `courses`.`Course` VALUES("+CoursId+",'"+CourseName+"',"+CoursCapacity+")";
				Statement stmt = null;
				try {
		            stmt = conn.createStatement();
		            int ins=stmt.executeUpdate(sql1);
		            if(ins>0)
		            	System.out.println("done");
		            stmt.close();
		        } catch (SQLException e) {
		        	System.out.println("error");
		        }
				
				for(int i=4;i<split.length;i++)
				{
					try {
			            stmt = conn.createStatement();
			            String sql2="INSERT INTO `courses`.`PrerequisiteCourse` VALUES("+CoursId+","+split[i]+")";
			            stmt.execute(sql2);
			            stmt.close();
			        } catch (SQLException e) {
			            e.printStackTrace();
			        }
				}
				
			}
				if(transCode.equals("3"))
				{
					try {
						Statement stmt = null;
						stmt = conn.createStatement();
						String sql1="SELECT avg(capacity)  from Course";
						ResultSet rs=stmt.executeQuery(sql1);
						 while (rs.next()) {
						BigDecimal capacity=rs.getBigDecimal(1);
						System.out.println(capacity);}
			            stmt.close();
			        } catch (SQLException e) {
			          	System.out.println("error");
			        }
				}
			if(transCode.equals("4"))
			{
				int cid=Integer.parseInt(split[1]);
				recursiveFind(cid,conn);
				try {
					for(int i=0;i<_pid1.size();i++)
					{
					Statement stmt = null;
					stmt = conn.createStatement();
					String sql1="SELECT name from Course where cid="+_pid1.get(i);
					ResultSet rs=stmt.executeQuery(sql1);
					 while (rs.next()) {
					 _name.add(rs.getString(1));
					 }
					stmt.close();
					}
		        } catch (SQLException e) {
		          	System.out.println("error");
		        }
				if(_name.size()!=0)
				{
				for(int i=0;i<_name.size();i++)
					System.out.print(_name.get(i)+" ");
				System.out.println();
				}
				else
					System.out.println("No prerequisite");
				
			}
			if(transCode.equals("5"))
			{
				
				try {
					Statement stmt = null;
					stmt = conn.createStatement();
					String sql1="SELECT avg(capacity) from Course WHERE cid IN (select pid from PrerequisiteCourse where cid="+split[1]+")";
					ResultSet rs=stmt.executeQuery(sql1);
					 while (rs.next()) 
					 {
						 BigDecimal capacity=rs.getBigDecimal(1);
						 BigDecimal scaled = capacity.setScale(0, RoundingMode.HALF_UP);
						 System.out.println(scaled);
					}
					 stmt.close();
		        } catch (SQLException e) {
		          	System.out.println("error");
		        }
			}
			if(transCode.equals("6"))
			{
				try {
					Statement stmt = null;
					stmt = conn.createStatement();
					String sql1="select name from Course where cid in(select cid from PrerequisiteCourse group by cid having count(cid)>1)";
					ResultSet rs=stmt.executeQuery(sql1);
					 while (rs.next()) 
					 {
						 String courseName=rs.getString(1);
						 System.out.println(courseName);
					}
					 stmt.close();
		        } catch (SQLException e) {
		          	System.out.println("error");
		        }
			}
		}
	}
	private void recursiveFind(int cid, Connection conn) {
		try {
			Statement stmt = null;
			stmt = conn.createStatement();
			String sql1="Select name,pid from Course NATURAL JOIN PrerequisiteCourse where cid="+cid;
			ResultSet rs=stmt.executeQuery(sql1);
			while (rs.next()) {
		    if(!_pid.contains(rs.getString(1)))
		    	{
		    	_pid.add(rs.getInt(2));
		    	_pid1.add(rs.getInt(2));
		    	}
			}
			if(_pid.size()!=0)
			{
				for(int i=0;i<=_pid.size();i++)
				{
					int temp=_pid.get(i);
					_pid.remove(i);
					recursiveFind(temp, conn);
				}
			}
            stmt.close();
        } catch (SQLException e) {
          	System.out.println("error");
        }
	}
	public Connection connectDB() {
        String url = "jdbc:mysql://localhost:3306/";
        String dbName = "courses";
        String driver = "com.mysql.jdbc.Driver";
        String userName = "root";
        String password = "root";
        Connection conn = null;
        try {
            Class.forName(driver).newInstance();
            conn = DriverManager.getConnection(url + dbName, userName, password);
//            System.out.println("Connected to the database");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return conn;
    }

    public void closeConnection(Connection conn) {
        try {
            if (conn != null) {
                conn.close();
//                System.out.println("Close the connection to database!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
	private void creatTable(Connection conn) {
        Statement stmt = null;
        String sql1 = "CREATE TABLE `courses`.`Course` (`cid` INTEGER  NOT NULL,`name` VARCHAR(20)  NOT NULL, `capacity` int(11), PRIMARY KEY (`cid`) )";
        String sql2 = "CREATE TABLE `courses`.`PrerequisiteCourse` (`cid` INTEGER  NOT NULL,`pid` INTEGER  NOT NULL,  PRIMARY KEY (`cid`, `pid`))";
        try {
            stmt = conn.createStatement();
            stmt.execute(sql1);
            stmt.execute(sql2);
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void deleteTable(Connection conn) {
       Statement stmt = null;
        String sql1 = "drop table `courses`.`Course`";
        String sql2 = "drop table `courses`.`PrerequisiteCourse`";
        try {
            stmt = conn.createStatement();
            stmt.execute(sql1);
            stmt.execute(sql2);
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
 }