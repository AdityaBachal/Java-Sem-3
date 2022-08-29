import java.io.*;
import java.net.*;

class FileServer 
{
    public static void main(String[] args) throws IOException,UnknownHostException
    {
        ServerSocket ss = new ServerSocket(50710);
		System.out.println("Server waiting for client ");

		Socket s = ss.accept();
		System.out.println("Client connected ");

        BufferedReader br = new BufferedReader (new InputStreamReader(s.getInputStream()));
		PrintStream ps= new PrintStream(s.getOutputStream());

        boolean end=false,exist=false;

        //received fname passed by client
        String fname=br.readLine();

        if(fname.endsWith(".txt"))
        {
            end=true;
            File ferr = new File(fname);
			if(ferr.exists())
			{
                exist=true;
                ps.println(end);
                ps.println(exist);
				FileReader fr = new FileReader(fname);
				BufferedReader br1 = new BufferedReader(fr);
				String string;
				while((string=br1.readLine())!=null)
				{
					ps.println(string);
					//System.out.println(string);
				}
				fr.close();
				br1.close();
			}	
            else
            {
                exist=false;
				//System.out.println("File not found...");
                ps.println(end);
                ps.println(exist);
            }
        }
        else
        {
            end=false;
            //System.out.println("Enter text file name only...");
            ps.println(end);
            ps.println(exist);
        }
        s.close();
        ss.close();
    }
}
