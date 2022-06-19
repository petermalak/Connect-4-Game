import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Server {
	
	private static ServerSocket server = null;
	private static int port = 59090;
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		server = new ServerSocket( port );  //opens new port
		System.out.println( "Connect-4 server online ....\n" );
		
		Executor pool = Executors.newFixedThreadPool(20); //Max no. of players 40
		while ( true )
		{
			gamePlay round = new gamePlay();
			pool.execute( round.new game( server.accept() , '1') ); //accept player one connect request
			pool.execute( round.new game( server.accept() , '2') ); //accept player two connect request
		}
		
	}

}
