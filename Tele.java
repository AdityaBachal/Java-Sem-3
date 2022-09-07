import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

class TeleGUI extends JFrame implements ActionListener
{
    JLabel TeleNo,Name,Address,Head;
    JTextField no,name,address;
    JMenu menu;
    JMenuItem insert,search,delete,display,exit;
    Connection con = null;
    JPanel p1,p2,p3;

    TeleGUI()
    {
        try 
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://192.168.28.3/syca221635","syca221635","");
			
	    	JFrame f = new JFrame();
			
	    	f.setLayout(new GridLayout(2,1));
			
	    	p1 = new JPanel();
	    	p1.setLayout(new GridLayout(1,0));
	    
        	JMenuBar mb = new JMenuBar();
	    	menu = new JMenu("Menu");
	    
			insert = new JMenuItem("insert");
			search = new JMenuItem("search");
			delete = new JMenuItem("delete");
			display = new JMenuItem("display");
			exit = new JMenuItem("exit");
			
			menu.add(insert);
			menu.add(search);
			menu.add(delete);
			menu.add(display);
			menu.add(exit);
			mb.add(menu);
			
            Head = new JLabel("Telephone Directory", JLabel.CENTER);
            Head.setFont(new Font("", Font.BOLD, 18));
            p1.add(Head);
            
            p2 = new JPanel();
            p2.setLayout(new GridLayout(3,2,50,15));
            
            TeleNo = new JLabel("Telephone No.");
            no = new JTextField();
            p2.add(TeleNo);
            p2.add(no);
            
            Name = new JLabel("Name");
            name = new JTextField();
			p2.add(Name);
            p2.add(name);

            Address = new JLabel("Address");
            address = new JTextField();
			p2.add(Address);
            p2.add(address);

            insert.addActionListener(this);
            search.addActionListener(this);
            delete.addActionListener(this);
            display.addActionListener(this);
            exit.addActionListener(this);
			
			f.setJMenuBar(mb);
			f.add(p1);
            f.add(p2);
            			
			f.setSize(600, 550);
		    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  		    f.setVisible(true);
            JOptionPane.showMessageDialog(null,"Connected to Database","Success",JOptionPane.INFORMATION_MESSAGE);
        } 
        catch (Exception e) 
        {
            System.out.println(e);
        }
        
    }

    public void actionPerformed(ActionEvent Ae)
	{
		if(Ae.getSource() == exit)
		{
            try 
            {
                con.close();
                JOptionPane.showMessageDialog(null,"Disconnected From DB","Message",JOptionPane.INFORMATION_MESSAGE);
                System.exit(0);
            } 
            catch (Exception e) 
            {
                System.out.println(e);
            }

		}
		else if(Ae.getSource() == insert)
		{   
            try 
            {
             	long noParam;
				if((no.getText()).length() == 10 && Long.parseLong(no.getText()) > 0 && !name.getText().isEmpty() && !address.getText().isEmpty())
				{
					noParam = Long.parseLong(no.getText());
					String nameParam = name.getText();
        			String addressParam = address.getText();
    
                	String insQury = "insert into tele values(?,?,?)" ;
                
                	PreparedStatement stmt = con.prepareStatement(insQury);
                	stmt.setLong(1,noParam);
                	stmt.setString(2,nameParam);
                	stmt.setString(3,addressParam);

                	int res = stmt.executeUpdate();
                	JOptionPane.showMessageDialog(null,res+" Rows Added","Success",JOptionPane.INFORMATION_MESSAGE);
				}
				else
				{
					System.out.println((no.getText()).length());
					JOptionPane.showMessageDialog(null,"Number must be of 10 digits and positive or Name and Address can not be empty.","Warning",JOptionPane.WARNING_MESSAGE);
				no.setText("");
				}
        	
            }
            catch(SQLIntegrityConstraintViolationException dup)
            {
                JOptionPane.showMessageDialog(null,"Number already exists","Warning",JOptionPane.WARNING_MESSAGE);
            }
            catch (Exception e) 
            {
                System.out.println(e);
            }
		}
        else if(Ae.getSource() == delete)
        {
            try 
            {
                long noParam = Long.parseLong(no.getText());
    
                String deleteQury = "delete from tele where teleNo=?" ;
                PreparedStatement stmt = con.prepareStatement(deleteQury); 
                stmt.setLong(1,noParam);

                int res = stmt.executeUpdate();
                if(res == 0)
                {
                    JOptionPane.showMessageDialog(null,"No Records Found","Error",JOptionPane.ERROR_MESSAGE);
                }
                else
                {
                    JOptionPane.showMessageDialog(null,res+" Rows Deleted","Success",JOptionPane.INFORMATION_MESSAGE);
					no.setText("");
					name.setText("");
					address.setText("");
                }
            } 
            catch (Exception e) 
            {
                System.out.println(e);
            }
        }
        else if(Ae.getSource() == search)
        {
            try 
            {
                long noParam = Long.parseLong(no.getText());
                String nameParam = name.getText();
                String addressParam = address.getText();
                
                String searchQury = "select * from tele where teleNo=?";
                PreparedStatement stmt = con.prepareStatement(searchQury);
                
                stmt.setLong(1, noParam);

                ResultSet rs = stmt.executeQuery();
                if (rs.next() == false)
                {
                    JOptionPane.showMessageDialog(null,"No Records Found","Error",JOptionPane.ERROR_MESSAGE);
                }
                else
                {
                    do
                    {
                        no.setText(String.valueOf(rs.getLong(1)));
                        name.setText(rs.getString(2));
                        address.setText(rs.getString(3));
                    }while(rs.next());
                }
            }
            catch(NumberFormatException NFE)
            {
                JOptionPane.showMessageDialog(null,"Please Enter Phone number","Error",JOptionPane.ERROR_MESSAGE);
            }
            catch (Exception e) 
            {
                System.out.println(e);
            }
        }
        else
        {
            try 
            {   
                JFrame frame2 = new JFrame("Database Results");
                frame2.setLayout(new FlowLayout());
                frame2.setSize(400, 400);
         
                DefaultTableModel defaultTableModel = new DefaultTableModel();
                JTable table = new JTable(defaultTableModel);
                table.setPreferredScrollableViewportSize(new Dimension(300, 100));
                table.setFillsViewportHeight(true);
                frame2.add(new JScrollPane(table));
                defaultTableModel.addColumn("Telephone No.");
                defaultTableModel.addColumn("Name");
                defaultTableModel.addColumn("Address");
            
                String displayQury = "select * from tele" ;
                PreparedStatement stmt = con.prepareStatement(displayQury); 

                ResultSet rs = stmt.executeQuery();
                while(rs.next())
                {
                    defaultTableModel.addRow(new Object[]{rs.getLong("teleNo"), rs.getString("name"), rs.getString("address")});
                }
                frame2.setVisible(true);
                frame2.validate();  
            } 
            catch (Exception e) 
            {
                System.out.println(e);
            }
        }
	}
}

public class Tele 
{
    public static void main(String[] args)
    {
        new TeleGUI();
    }
}   
