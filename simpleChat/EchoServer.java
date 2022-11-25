// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 


import java.io.IOException;

import ocsf.server.*;

/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 * @version July 2000
 */
public class EchoServer extends AbstractServer 
{
  //Class variables *************************************************
  
  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port) 
  {
    super(port);
  }

  
  //Instance methods ************************************************
  
  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient
    (Object msg, ConnectionToClient client)
  {
	  String message = msg.toString();
      if (message.startsWith("#")) {
          String[] donnee = message.substring(1).split(" ");
          
          if (donnee[0].equalsIgnoreCase("login") && donnee.length > 1) {
              if (client.getInfo("username") == null) {
                  client.setInfo("username",donnee[1]);
              } else {
                  try {
                      client.sendToClient("Your username has already been set!");
                  } catch (IOException e) {
                  }
              }
          }
      } else {
          if (client.getInfo("username") == null) {
              try {
                  client.sendToClient(("Please set a username before messaging the server!"));
                  client.close();
              } catch (IOException e) {
                  e.printStackTrace();
              }
          } else {
              System.out.println("Message received: " + msg + " from " + client.getInfo("username"));
              this.sendToAllClients((client.getInfo("username")));
          }
      }      
  }
    
  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {
    System.out.println
      ("Server listening for connections on port " + getPort());
  }
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {
    System.out.println
      ("Server has stopped listening for connections.");
  }
  
  /**
   * Hook method called each time a new client connection is
   * accepted. The default implementation does nothing.
   * @param client the connection connected to the client.
   */
  @Override
  protected void clientConnected(ConnectionToClient client) {
	    System.out.println
	      (client.getInfo("username")+" just connected");
  }

  /**
   * Hook method called each time a client disconnects.
   * The default implementation does nothing. The method
   * may be overridden by subclasses but should remains synchronized.
   *
   * @param client the connection with the client.
   */
  @Override
  synchronized protected void clientDisconnected(
    ConnectionToClient client) {
	  String mes=client.getInfo("username")+" jsut disconnected";
	    System.out.println
	      (mes);
	    this.sendToAllClients(mes);
  } 
  
  /** cette methode, simillairement a handleMessageFromClientUI
   * gere le cas echant pour la console server
   */
  
  public void handleMessageFromServerConsole(String message) {
	     if (message.startsWith("#")) {
	            String[] donnee = message.split(" ");
	            switch (donnee[0]) {
	                case "#quit":
	                    try {
	                        this.close();
	                    } catch (IOException e) {
	                        System.exit(1);
	                    }
	                    System.exit(0);
	                    break;
	                case "#stop":
	                    this.stopListening();
	                    break;
	                case "#close":
	                    try {
	                    	this.stopListening();
	                        this.close();
	                    } catch (IOException e) {
	                    }
	                    break;
	                case "#setport":
	                    if (!this.isListening() && this.getNumberOfClients() < 1) {
	                        super.setPort(Integer.parseInt(donnee[1]));
	                        System.out.println("Le nouveau port est " + Integer.parseInt(donnee[1]));
	                    } else {
	                        System.out.println("Le server est connecte");
	                    }
	                    break;
	                case "#start":
	                    if (!this.isListening()) {
	                        try {
	                            this.listen();
	                        } catch (IOException e) {
	                        	System.out.println("Le server est deja en ecoute"); 
	                        }
	                    } 
	                    break;
	                case "#getport":
	                    System.out.println("Le port actueles " + this.getPort());
	                    break;
	                default:
	                	this.sendToAllClients(message);
	                    break;
	            }
	        }
	  
  }
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of 
   * the server instance (there is no UI in this phase).
   *
   * @param args[0] The port number to listen on.  Defaults to 5555 
   *          if no argument is entered.
   */
  
  public static void main(String[] args) 
  {
    int port = 0; //Port to listen on

    try
    {
      port = Integer.parseInt(args[0]); //Get port from command line
    }
    catch(Throwable t)
    {
      port = DEFAULT_PORT; //Set port to 5555
    }
	
    EchoServer sv = new EchoServer(port);
    
    try 
    {
      sv.listen(); //Start listening for connections
    } 
    catch (Exception ex) 
    {
      System.out.println("ERROR - Could not listen for clients!");
    }
  }
}
//End of EchoServer class
