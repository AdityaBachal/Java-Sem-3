import java.sql.*;
import java.io.*;

class DML
{
	public static void main(String args[]) throws IOException
  	{
        Connection con = null;
    	try
    	{
      	    Class.forName("com.mysql.cj.jdbc.Driver");
      	    System.out.println("Driver class found.");
      	    con=DriverManager.getConnection("jdbc:mysql://192.168.28.3/syca221635","syca221635","");
            System.out.println("Connected...");
      	    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            int ch = 0;
            do {
               System.out.println("1. Insert\n2. Delete\n3. Display\n4. Exit"); 
               System.out.println("Enter operation number : ");
               ch = Integer.parseInt(br.readLine());

               switch (ch) 
               {
                case 1:
                    System.out.println("Enter Employee Name : ");
                    String name = br.readLine();
                    int sal = 0;
                    while(true)
					{
						 System.out.println("Enter Employee Salary : ");
						 sal = Integer.parseInt(br.readLine());
						 if(sal > 0)
						 {
							break;
						 }
						 else
						 {
							System.out.println("Salary must be greater than 0.");
						 }
					}
                    String insQury = "insert into emp(ename,esal) values(?,?)";
                    PreparedStatement ins = con.prepareStatement(insQury);
                    ins.setString(1,name);
                    ins.setInt(2,sal);

                    int i = ins.executeUpdate();
                    System.out.println(i+" records inserted.");
		    String empId = "select LAST_INSERT_ID()";
		    Statement empIdFetch = con.createStatement();
		    ResultSet emp1 = empIdFetch.executeQuery(empId);
		    while(emp1.next())
		    {
			 System.out.println(emp1.getInt("LAST_INSERT_ID()"));
		     }
		    System.out.println();
                break;
               
                case 2:
                	System.out.println("Enter employee Id : ");
                    int id = Integer.parseInt(br.readLine());
                    String qury = "delete from emp where empno = ?";
                    PreparedStatement delById = con.prepareStatement(qury); 
                    delById.setInt(1,id);

                    int delId = delById.executeUpdate();
                    System.out.println("\n"+delId+" records removed.");
                break;
                
                case 3:
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery("select * from emp");
                    while(rs.next()) 
                    {
                        System.out.println("\n"+rs.getInt("empno")+"\t"+rs.getString("ename")+"\t"+rs.getInt("esal"));
                    }
                break;
                
                case 4:
                    System.out.println("Exiting...");
                break;

                default:
                    System.out.println("Enter appropriate choice.");
                break;
               }
            } while (ch<4 && ch > 0);
      	    
            
    	}
    	catch(Exception e)
    	{
		    System.out.println(e);
    	}
        finally
        {
            try 
            {
                con.close();
                System.out.println("Diconnected...");
            }
            catch (Exception e) 
            {
                System.out.println(e);
            }
        }
  	}
}
