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

  public String loginID;


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
    try{
        openConnection();
    }catch (Exception e){
        System.out.println("Cannot open connection, awaiting command");
    }
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
   */
  public void handleMessageFromClientUI(String message)
  {

        if (!isConnected()){ //Handle when logged off
            switch (message ){

            case "#logoff":
                System.out.println("Please #login first");
                break;

            case "#quit": //DONE
                quit();
                break;

            case "#login":
                System.out.println("Please #logoff first");
                break;

            case "#gethost": //DONE
                System.out.println( getHost() );
                break;

            case "#getport": //DONE
                System.out.println( getPort() );
                break;

            default:
                if ( message.indexOf("#sethost") == 0 ){
                    String newHost= new String();
                    int i = 0;

                    for (String word : message.split(" ")) {
                        if (i == 1){
                            try{
                                newHost = word.toString();
                            }catch(Exception e){}
                        }
                        i++;
                    }
                    if (newHost!= null){
                        setHost(newHost);
                        System.out.println("Host changed to: " + getHost());
                    }else System.out.println("Command should be in format #setport <port>");
                }

                else if ( message.indexOf("#setport") == 0 ){
                    int newPort = 0;
                    int i = 0;

                    for (String word : message.split(" ")) {
                        if (i == 1){
                            try{
                                newPort = Integer.parseInt(word);
                            }catch(Exception e){
                                System.out.println("Please make sure <port> is an integer");
                            }
                        }
                        i++;
                    }
                    if (newPort != 0){
                        setPort(newPort);
                        System.out.println("Port changed to: " + getPort());
                    }
                    else System.out.println("Command should be in format #setport <port>");
                }

                else if ( message.indexOf("#login") == 0 ){
                    String newHost= new String();
                    int i = 0;

                    try{
                        openConnection();
                        sendToServer(message);
                    }
                    catch(IOException e)
                    {
                        System.out.println( e  );
                        clientUI.display
                        ("Could not send message to server.  Terminating client.");
                        quit();
                  }
              }

                //Handle invalid commands
                else if ( message.indexOf('#') == 0 ){
                    System.out.println("Command not found");
                }

                else{
                    System.out.println("WARNING - Server has stopped listening for connections.");
                }
            }
        }else{
            switch (message){
                case "#quit": //DONE
                    quit();
                    break;

                case "#logoff": //DONE
                    try{
                        closeConnection();
                    }
                    catch(IOException e) {}
                    break;

                case "#login":
                    System.out.println("Please #logoff first");
                    break;

                case "#gethost": //DONE
                    System.out.println( getHost() );
                    break;

                case "#getport": //DONE
                    System.out.println( getPort() );
                    break;

                default:
                //Just to setLoginID in ChatClient
                    if ( message.indexOf("setlogin") == 0 ){
                        int j = 0;
                        for (String word : message.split(" ")) {
                            if (j == 1){
                                try{
                                    loginID = word;
                                }catch(Exception e){
                                    System.out.println(e);
                                }
                            }
                            j++;
                       }
                    }
                    else if ( message.indexOf("#sethost") == 0 ){
                        System.out.println("Please #logoff first");
                        break;
                    }

                    else if ( message.indexOf("#setport") == 0 ){
                        System.out.println("Please #logoff first");
                        break;
                    }

                    //Handle invalid commands
                    else if ( message.indexOf('#') == 0 ){
                        System.out.println("Command not found");
                    }
                    else{
                        try{
                        sendToServer(message);
                        }
                        catch(IOException e)
                        {
                            System.out.println( e  );
                            clientUI.display
                            ("Could not send message to server.  Terminating client.");
                            quit();
                      }
                  }
              }
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
    System.out.println("SERVER SHUTTING DOWN! DISCONNECTING!");
    System.out.println("Abnormal termination of connection.");
 }


  @Override
 protected void connectionClosed() {
    System.out.println("Connection Closed");
 }

 @Override
 /**
  * Hook method called after a connection has been established. The default
  * implementation does nothing. It may be overridden by subclasses to do
  * anything they wish.
  */
 protected void connectionEstablished() {
    System.out.println("Connection Established");
}

}
//End of ChatClient class
