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

  public ChatClient(String host, int port, ChatIF clientUI)
    throws IOException
  {
    super(host, port); //Call the superclass constructor
    this.clientUI = clientUI;
    openConnection();
  }


  //Instance methods ************************************************

  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg)
  {
    if (!isConnected()){
        try{
            openConnection();
        }catch(IOException e) {}

        if (msg.toString().equals("#login")){
            System.out.println("Beep");
        }
    }


    if (msg.toString().equals("#quit")){
          System.out.println("Quitting...");
          quit();

    }

    else if (msg.toString().equals("#logoff")){
            try{
                closeConnection();
            }
            catch(IOException e) {}
             System.out.println("You have successfully logged off");
    }

    else if (msg.toString().equals("#sethost")){
          if (!isConnected()){
              System.out.println("Setting host");
          }
          else{
              System.out.println("Please #logoff first");
          }
    }

    else if (msg.toString().equals("#setport")){
          System.out.println("Setport ");
    }

    else if (!isConnected()){

    }

    else if (msg.toString().equals("#gethost")){
          System.out.println("Gethost ");
    }

    else if (msg.toString().equals("#getport")){
          System.out.println("Gethost ");
    }

    else{
          clientUI.display(msg.toString());
    }
  }

  /**
   * This method handles all data coming from the UI
   *
   * @param message The message from the UI.
   */
  public void handleMessageFromClientUI(String message)
  {
    try
    {
      sendToServer(message);
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

  @Override
  /**
  * @param exception
  *            the exception raised.
  */
 protected void connectionException(Exception exception) {
    System.out.println("Server has shut down. " + "Quitting.");
    System.exit(1);
 }
}
//End of ChatClient class
