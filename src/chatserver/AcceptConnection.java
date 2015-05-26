/**
 * @author Masoom Shaikh
 * masoomshaikh@users.sourceforge.net
 */
package chatserver;

import java.io.*;
import java.net.*;

import common.*;

public class AcceptConnection
    implements Runnable
{
  private ServerAppFrame server;
  private ServerSocket connection;
  private boolean running;

  public AcceptConnection(ServerAppFrame parent, ServerSocket conn)
  {
    Thread acceptThread = new Thread(this);
    server = parent;
    connection = conn;
    acceptThread.start();
    running = true;
  }

  public void run()
  {
    while (running)
    {
      try
      {
    	  System.out.println("Socket....starting....");
        Socket newClient = connection.accept();
        System.out.println("Accepted............");
        server.addNewUser(newClient);
      }
      catch(SocketException sxp)
      {
        running = false;
      }
      catch (IOException ex)
      {
        System.err.println("AcceptConnection:39" + ex.toString());
      }
    }
  }

  public void stop()
  {
    running = false;
  }
}
