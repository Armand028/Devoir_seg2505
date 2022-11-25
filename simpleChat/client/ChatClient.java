// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package client;

import ocsf.client.*;
import common.*;
import java.io.*;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 * @version July 2000
 */
public class ChatClient extends AbstractClient
{
  //Instance variables **********************************************
  
  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the client.
   */
  ChatIF clientUI; 

  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  
  public ChatClient(String username,String host, int port, ChatIF clientUI) 
    throws IOException 
  {
    super(host, port); //Call the superclass constructor
    this.clientUI = clientUI;
    this.openConnection();
    this.sendToServer("#login " + username);
  }

  
  //Instance methods ************************************************
    
  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg) 
  {
    clientUI.display(msg.toString());
  }

  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
 * @throws Exception 
   */
  public void handleMessageFromClientUI(String message) throws Exception
  {
    try
    {
    	if (message.startsWith("#")) {
    	 handleCommand(message);
    	}else {
      sendToServer(message);}
    }
    catch(IOException e)
    {
      clientUI.display
        ("Could not send message to server.  Terminating client.");
      quit();
    }
  }
  
  /**
   * This method terminates the client.
   */
  public void quit()
  {
    try
    {
      closeConnection();
    }
    catch(IOException e) {}
    System.exit(0);
  }
  
  public void handleCommand(String cmd) throws Exception {
	  
	  if(cmd.equals("#quit")) {
		  
		  clientUI.display("disconnection starts");
		  quit();
		  clientUI.display("disconnection completed");
	  }
	 
	  else if(cmd.equals("#logoff")) {
		  try {
			  clientUI.display("logoff starts");
			  if(this.isConnected()) {
		  this.closeConnection();
		  clientUI.display("logoff completed");}
		  }catch(IOException e) {
			 clientUI.display("client non deconnecter"); 
		  }
	  }
	  
	  if(cmd.split("").length>1){
		  String[] donnee=cmd.split("");
		  
		   if(donnee[0].equals("#sethost")) {
				
			  if(!this.isConnected()) {
				  this.setHost(donnee[1]);
				
				  }
		  }
		  else if(donnee[0].equals("#setport")) {
			  if(!this.isConnected()) {
				  this.setPort(Integer.parseInt(donnee[1]));
				  }
		  }
	  }
	  else if(cmd.equals("#login")) {
		  try {
			  if(!this.isConnected()) {
		  this.openConnection();
		  }
		  }catch(IOException e) {
			 clientUI.display("client deja connecte"); 
		  }
	  }
	  
	  else if(cmd.equals("#gethost")) {
	  String t=this.getHost();
	  }
	  else if(cmd.equals("#getport")) {
	  int w=this.getPort();
	  }
  }
  
  /**
	 * Hook method called after the connection has been closed. The default
	 * implementation does nothing. The method may be overriden by subclasses to
	 * perform special processing such as cleaning up and terminating, or
	 * attempting to reconnect.
	 */
  @Override
	protected void connectionClosed() {
		clientUI.display("the connection has closed");
	}

	/**
	 * Hook method called each time an exception is thrown by the client's
	 * thread that is waiting for messages from the server. The method may be
	 * overridden by subclasses.
	 * 
	 * @param exception
	 *            the exception raised.
	 */
  @Override
	protected void connectionException(Exception exception) {
		clientUI.display("the connexion has shut down");
		quit();
	}
}
//End of ChatClient class
