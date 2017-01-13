/**
 * Created by Сергій on 26.11.2016.
 */
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Scanner;

public class Client{
    Socket requestSocket;
    ObjectOutputStream out;
    ObjectInputStream in;
    String message;

    Client(){}
    void run() throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException {
        try{
            //1. creating a socket to connect to the server
            requestSocket = new Socket("localhost", 2004);
            System.out.println("Connected to localhost in port 2004");
            //2. get Input and Output streams
            out = new ObjectOutputStream(requestSocket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(requestSocket.getInputStream());
            //3: Communicating with the server

                try{
                    message = (String)in.readObject();
                    System.out.println("server>" + message);
                    int i=0;
                    for (i=0;i<3;i++){
                        Scanner scanner = new Scanner(System.in);

                        System.out.print("\n Write login  ");
                        String login=scanner.nextLine();
                        System.out.print("Write password  ");
                        String pass=scanner.nextLine();

                        SecretKey key = KeyGenerator.getInstance("AES").generateKey();
                        AESCrypto crypto = new AESCrypto(key);
                        String enc = crypto.encrypt(pass);

                        String keyString = new String(Base64.getEncoder().encode(key.getEncoded()));

                        sendMessage(login);
                        sendMessage(enc);
                        sendMessage(keyString);
                  /*      sendMessage("Hi my server    аіім");
                        message = "bye";
                        sendMessage(message);
                  */
                        message = (String)in.readObject();
                        System.out.print(message);
                        if (message.equals("password correct")) break;
                    }
                    if (i==3)  System.out.print("you enter the wrong password three times");


                }
                catch(ClassNotFoundException classNot){
                    System.err.println("data received in unknown format");
                }

        }
        catch(UnknownHostException unknownHost){
            System.err.println("You are trying to connect to an unknown host!");
        }
        catch(IOException ioException){
            ioException.printStackTrace();
        }
        finally{
            //4: Closing connection
            try{
                in.close();
                out.close();
                requestSocket.close();
            }
            catch(IOException ioException){
                ioException.printStackTrace();
            }
        }
    }
    void sendMessage(String msg)
    {
        try{
            out.writeObject(msg);
            out.flush();
            System.out.println("client>" + msg);
        }
        catch(IOException ioException){
            ioException.printStackTrace();
        }
    }





    public static void main(String args[])
    {
        Client client = new Client();


        try {
            client.run();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
    }
}
