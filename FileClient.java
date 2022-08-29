import java.io.*;
import java.net.*;

class FileClient
{
	public static void main(String[] args) throws IOException,UnknownHostException
	{
			Socket s = new Socket("localhost",50710);

            System.out.println("Client Connected"); 

            BufferedReader br1 = new BufferedReader(new InputStreamReader(s.getInputStream()));
            BufferedReader br2 =  new BufferedReader(new InputStreamReader(System.in));

            PrintStream ps = new PrintStream(s.getOutputStream());

            //accept file and pass it to the server
            System.out.println("Enter Filename :"); 
            String fname=br2.readLine();
            ps.println(fname);

            //receive flag
            boolean end=Boolean.parseBoolean(br1.readLine());
            boolean exist=Boolean.parseBoolean(br1.readLine());

            if(end==true && exist==true)
            {
                String string = br1.readLine();
                while(string != null)
                {
                    System.out.println(string);
                    string = br1.readLine();
                }
            }
            else if(end==true && exist==false)
            {
                System.out.println("File not found...");
            }
            else
            {
                System.out.println("Enter text file only...");
            }    

            br2.close();
            br1.close();
            s.close();
    }
}