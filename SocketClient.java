import java.io.*;
import java.net.*;
public class SocketClient{
	public static void main(String[] args) throws IOException{
		try{
			Socket socket = new Socket("172.17.0.2",2022);
			System.out.println("Client enable Success");
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			PrintWriter writer = new PrintWriter(socket.getOutputStream());
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String readline;
			readline = br.readLine();
			while(!readline.equals("end")){
                                writer.println(readline);
                                writer.flush();
                                System.out.println("Client:" + readline);
                                System.out.println("Service:" + in.readLine());
                                readline = br.readLine();}
                        writer.close();
                        in.close();
                        socket.close();}
                catch(Exception e){System.out.println("can not listen to" + e);}}}
