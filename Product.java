import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;


class ProductGUI extends JFrame implements ActionListener
{
    JLabel Pid,Name,Price,Quantity,Head;
    JTextField no,name,price,quantity;
    JButton insert,search,update,exit;
    Connection con = null;
    JPanel p1,p2,p3;
    ProductGUI()
    {
        try 
        {
            Class.forName("com.mysql.cj.jdbc.Driver");

            con = DriverManager.getConnection("jdbc:mysql://192.168.28.3/syca221635","syca221635","");
            JFrame f = new JFrame();
            
            f.setLayout(new GridLayout(3,1));

            p1 = new JPanel();
	    p1.setLayout(new GridLayout(1,0));
			
            Head = new JLabel("Product Information", JLabel.CENTER);
            Head.setFont(new Font("", Font.BOLD, 18));
            p1.add(Head);
            
            p2 = new JPanel();
            p2.setLayout(new GridLayout(4,2,50,15));

            Pid = new JLabel("Product Id");
            Pid.setFont(new Font("", Font.PLAIN, 16));
            no = new JTextField();
            
            Name = new JLabel("Name");
            Name.setFont(new Font("", Font.PLAIN, 16));
            name = new JTextField();

            Price = new JLabel("Price");
            Price.setFont(new Font("", Font.PLAIN, 16));
            price = new JTextField();

	    Quantity = new JLabel("Quantity");
	    Quantity.setFont(new Font("", Font.PLAIN, 16));
            quantity = new JTextField();
            
            p2.add(Pid);
            p2.add(no);

            p2.add(Name);
            p2.add(name);

            p2.add(Price);
            p2.add(price);
            
            p2.add(Quantity);
            p2.add(quantity);

            p3 = new JPanel();
	    p3.setLayout(null);
			
			
            insert = new JButton("Add");
            insert.setBounds(8,110,100,60);
            search = new JButton("Search");
            search.setBounds(130,110,100,60);
            update = new JButton("Update");
            update.setBounds(255,110,100,60);
            exit = new JButton("Exit");
            exit.setBounds(380,110,100,60);
			
            p3.add(insert);
            p3.add(update);
            p3.add(search);
            p3.add(exit);

            f.add(p1);
            f.add(p2);
            f.add(p3);

            insert.addActionListener(this);
            search.addActionListener(this);
            update.addActionListener(this);
            exit.addActionListener(this);

            f.setSize(500, 700);
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
                int noParam; //= Integer.parseInt(no.getText());
                String nameParam = name.getText();
                int priceParam = Integer.parseInt(price.getText());
                int quantityParam = Integer.parseInt(quantity.getText());
		if((no.getText()).isEmpty())
		{
		    JOptionPane.showMessageDialog(null,"Product Id can not be null","Warning",JOptionPane.WARNING_MESSAGE);
		    no.setText("");
		    
		}
    		if(priceParam <= 0 || quantityParam <= 0 )
		{
			JOptionPane.showMessageDialog(null,"Price and Quantity must be greater than zero","Warning",JOptionPane.WARNING_MESSAGE);
			price.setText("");
			quantity.setText("");
		}
		else
		{
                	String insQury = "insert into product values(?,?,?,?)" ;
                	noParam = Integer.parseInt(no.getText());
                	PreparedStatement stmt = con.prepareStatement(insQury);
                	stmt.setInt(1,noParam);
                	stmt.setString(2,nameParam);
                	stmt.setInt(3,priceParam);
                	stmt.setInt(4,quantityParam);

                	int res = stmt.executeUpdate();
                	JOptionPane.showMessageDialog(null,res+" Records Added","Success",JOptionPane.INFORMATION_MESSAGE);
		}
            }
            catch(SQLIntegrityConstraintViolationException dup)
            {
                JOptionPane.showMessageDialog(null,"Product already exists","Warning",JOptionPane.WARNING_MESSAGE);
            }
            catch (Exception e) 
            {
                System.out.println(e);
            }
		}
        else if(Ae.getSource() == update)
        {
            try 
            {
                int noParam = Integer.parseInt(no.getText());
                String nameParam = name.getText();
                System.out.println();
                int priceParam = Integer.parseInt(price.getText());
                System.out.println(priceParam);
                int quantityParam = Integer.parseInt(quantity.getText());
    		
                if(!nameParam.isEmpty())
                {
                	String deleteQury = "update product set name=?,price=?,quantity=? where ProdId=?" ;
                	PreparedStatement stmt = con.prepareStatement(deleteQury); 
                
                	stmt.setString(1,nameParam);
                	stmt.setInt(2,priceParam);
                	stmt.setInt(3,quantityParam);
                	stmt.setInt(4,noParam);

                	int res = stmt.executeUpdate();
                	if(res == 0)
                	{
                   		JOptionPane.showMessageDialog(null,"No Records Found","Error",JOptionPane.ERROR_MESSAGE);
                	}
               		else
                	{
                   		JOptionPane.showMessageDialog(null,res+" Records Updated.","Success",JOptionPane.INFORMATION_MESSAGE);
                	}	
            	}
            	else
            	{
            		throw new NumberFormatException();
            	}
            }
            catch(NumberFormatException NFE)
            {
            	JOptionPane.showMessageDialog(null,"Please Fill all the info.","Warning",JOptionPane.WARNING_MESSAGE);
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
                int noParam = Integer.parseInt(no.getText());
                               
                String searchQury = "select * from product where ProdId=?";
                PreparedStatement stmt = con.prepareStatement(searchQury);
                
                stmt.setInt(1,noParam);

                ResultSet rs = stmt.executeQuery();
                
                if (rs.next() == false)
                {
                    JOptionPane.showMessageDialog(null,"No Records Found","Error",JOptionPane.ERROR_MESSAGE);
                }
                else
                {
                    do
                    {	
                        no.setText(String.valueOf(rs.getInt(1)));
                        name.setText(rs.getString(2));
                        price.setText(String.valueOf(rs.getInt(3)));
                        quantity.setText(String.valueOf(rs.getInt(4)));
                    }while(rs.next());
                }
            }
            catch(NumberFormatException NFE)
            {
                JOptionPane.showMessageDialog(null,"Please Enter Product Id","Error",JOptionPane.ERROR_MESSAGE);
            }
            catch (Exception e) 
            {
                System.out.println(e);
            }
        }
	}
}

public class Product 
{
    public static void main(String[] args)
    {
        new ProductGUI();
    }
}   
