import java.io.*;
import java.net.*;
public class SocketService{
	public static void main(String[] args) throws IOException{
		SocketService socketService = new SocketService();
		socketService.oneServer();}
	public void oneServer(){
		try{
			ServerSocket server = null;
			try{
				server = new ServerSocket(2022);
				System.out.println("Service enable Success...");}
			catch(Exception e){System.out.println("No Listen:" + e);}
			Socket socket = null;
			try{socket = server.accept();}
			catch(Exception e){System.out.println("Error." + e);}
			String line;
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter writer = new PrintWriter(socket.getOutputStream());
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("Client:" + in.readLine());
			line = br.readLine();
			while(!line.equals("end")){
				writer.println(line);
				writer.flush();
				System.out.println("Service:" + line);
				System.out.println("Client:" + in.readLine());
				line = br.readLine();}
			writer.close();
			in.close();
			socket.close();
			server.close();}
		catch(Exception e){System.out.println("Error." + e);}}}
