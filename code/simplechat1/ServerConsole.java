import java.io.*;
import client.*;
import common.*;



public class ServerConsole implements ChatIF{

    final public static int DEFAULT_PORT = 5555;
    EchoServer server;

    public ServerConsole(int port){

        server= new EchoServer(port);
        try{
            server.listen();
        }catch(Exception e){
            System.out.println(e);
        }
    }


    public void display(String message){
        System.out.println("SERVER MSG> " + message);
    }


    public void accept(){
        BufferedReader fromConsole = new BufferedReader(new InputStreamReader(System.in));
        String message;
        while (true)
        {
            try{
                message = fromConsole.readLine();
                if (message.charAt(0) == '#'){
                    resolveCommands(message);
                }else{
                    display(message);
                    server.sendToAllClients( "SERVER MSG> "+ (Object)message );
                }
            }
            catch (Exception e){
                System.out.println(e);
            }
        }
    }

    public void resolveCommands(String message){
        switch (message){

            case "#quit": //quit gracefully
                server.stopListening();
                try{
                    server.close();
                }catch(Exception e){
                    System.out.println("Unable to close: " + e);
                }
                System.exit(1);
                break;

            case "#stop": //stop listening
                server.stopListening();
                server.sendToAllClients("WARNING - Server has stopped listening for connections.");
                break;

            case "#close": //disconnect and stop listening
                try{
                    server.close();
                }catch(Exception e){
                    System.out.println("Unable to close");
                }
                break;

            case "#getport":
                System.out.println( server.getPort() );
                break;

            default:
                if (server.isListening()){ //if sever is still listening
                    if ( message.indexOf("#login") == 0 ){
                        System.out.println("Please #stop first");
                        break;
                    }

                    else if ( message.indexOf("#setport") == 0 ){
                        System.out.println("Please #stop first");
                        break;
                    }

                    else if ( message.indexOf("#start") == 0 ){
                        System.out.println("Please #stop first");
                        break;
                    }
                    else if ( message.indexOf('#') == 0 ){
                        System.out.println("Command not found");
                    }
                }

                else{ //When server has stopped listening
                    if ( message.indexOf("#setport") == 0 ){
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
                            server.setPort(newPort);
                            System.out.println("Port changed to: " + server.getPort());
                        }

                        else System.out.println("Command should be in format #setport <port>");
                    }

                    else if ( message.equals("#start")){
                        try{
                            server.listen();
                        }catch( Exception e){
                            System.out.println("Failed to start server: " + e);
                        }
                    }

                    else if ( message.indexOf('#') == 0 ){
                        System.out.println("Command not found");
                    }
                }
        }
    }

    public static void main(String[] args){
      String host = "";
      int port = 0;  //The port number

      host = "localhost";

      try{
        port = Integer.parseInt(args[0]);
      }catch(ArrayIndexOutOfBoundsException e){
        port = 5555;
      }

      ServerConsole serverChat= new ServerConsole(port);
      serverChat.accept();  //Wait for console data
    }
}
