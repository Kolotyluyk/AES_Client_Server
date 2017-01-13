/**
 * Created by Сергій on 26.11.2016.
 */
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.*;
import java.security.InvalidKeyException;;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class Server{
    ServerSocket providerSocket;
    Socket connection = null;
    ObjectOutputStream out;
    ObjectInputStream in;
    String message;
    Server(){}
    void run() throws NoSuchAlgorithmException {
        try{
            providerSocket = new ServerSocket(2004, 10);
             System.out.println("Waiting for connection");
            connection = providerSocket.accept();
            System.out.println(" Connection received from " + connection.getInetAddress().getHostName());
            out = new ObjectOutputStream(connection.getOutputStream());
            out.flush();
            in = new ObjectInputStream(connection.getInputStream());
            sendMessage("Connection successful");
               int i=0;
                try{
                    for (i=0;i<3;i++){
                        String login=(String)in.readObject();
                        String pass=(String)in.readObject();
                        String key=(String)in.readObject();
                    System.out.println("3 client>" + message);
                    if (checkKey(login,pass,key)){
                        sendMessage("password correct");
                        break;
                    }
                    else
                        sendMessage("password incorrect");
                }
                }
                catch(ClassNotFoundException classnot){
                    System.err.println("Data received in unknown format");
                }
        }
        catch(IOException ioException){
            ioException.printStackTrace();
        }
        finally{
             try{
                in.close();
                out.close();
                providerSocket.close();
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
            System.out.println(" 11 server>" + msg);
        }
        catch(IOException ioException){
            ioException.printStackTrace();
        }
    }


    private boolean checkKey(String login,String pass,String key){
        SecretKey thisKey= null;
        String s="";
        String thisPass=WorkWithMYSQL.getPass(login);
        try {
            byte[] encodedKey = Base64.getDecoder().decode(key);
             thisKey = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES"); AESCrypto aesCrypto=new AESCrypto(thisKey);
                s=aesCrypto.decrypt(pass);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }

        return thisPass.equals(s);
    }

    public static void main(String args[])
    {
        Server server = new Server();
            try {
                server.run();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }

    }
}