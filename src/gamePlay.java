import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class gamePlay {
	
	player Player1 , Player2;
	
	public gamePlay()
	{
		Player1 = new player();
		Player2 = new player();
	}
		
	public class game implements Runnable
	{
		
		public game( Socket socket , char mark ) throws IOException
		{
			if 		( Player1.mark == '?' )
			{
				Player1.setPlayer( socket , mark );
			}
			else if ( Player2.mark == '?' )
			{
				Player2.setPlayer( socket , mark );
			}
		}
		
		public void run() 
		{
			
			boolean P1_turn = true;
			boolean EndGame = false;
			
			if (Player1.mark != '?' && Player2.mark != '?')
			{
				while (true)
				{
					if (P1_turn)
					{
						Player1.out.println("p1");
						Player2.out.println("p2");
						int index = -1;				//holds player's move (-1 represents if anything goes wrong)
						
						if (Player1.in.hasNext() == true)
							index = Player1.in.nextInt();	//gets player 1 move
	
						if (Player2.socket.isClosed() == true) //player 1 turn but 2nd player closes
						{
							Player1.closeConnection();
							Player2.closeConnection();
							EndGame = true;
							break;
						}
						
						Player2.out.println( index ); 								
						
						if (index == -1)					//player 1 turn but closes
						{
							Player1.closeConnection();
							Player2.closeConnection();
							EndGame = true;
							break;
						}
						
						// >>>>>  player 1 END GAME
						if (Player1.in.hasNext() == true)
							EndGame = Player1.in.nextBoolean();
						
						P1_turn = false;
						if ( EndGame == true )
						{
							break;
						}
						
					}
					else		//p2_turn
					{
						Player2.out.println("p1");
						Player1.out.println("p2");
						
						if (Player1.socket.isClosed() == true)
						{
							Player1.closeConnection();
							Player2.closeConnection();
							EndGame = true;
							break;
						}

						int index = -1;
						if (Player2.in.hasNext() == true)
							index = Player2.in.nextInt();
						
						Player1.out.println( index );
						
						if (index == -1)
						{
							Player1.closeConnection();
							Player2.closeConnection();
							EndGame = true;
							break;
						}
						
						// >>>>>  player 2 END GAME

						if (Player2.in.hasNext() == true)
							EndGame = Player2.in.nextBoolean();
						
						if ( EndGame == true )
						{
							break;
						}
						
						P1_turn = true;
					}
				}
				
				Player1.closeConnection();
				Player2.closeConnection();
				
			}
		}
	}
	
	public class player
	{
		char mark = '?';
		Socket socket = null;
		
		Scanner in = null;			//input stream with client
		PrintWriter out = null;		//output stream to client
		
		public void setPlayer( Socket socket , char mark ) throws IOException
		{
			this.socket = socket;
			this.mark   = mark;
			
			in = new Scanner(socket.getInputStream()); 				//identify clients' stream (receive)
            out = new PrintWriter(socket.getOutputStream(), true);
            
            out.println( "Welcome Player " + mark );				//prints Welcome to Client's Console
		}
		public void closeConnection() //closes all connection
		{
			try 
			{
				socket.close();					
				in.close();
				out.close();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
	}
}
