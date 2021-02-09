import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class ClientHandler implements Runnable{

    private Socket socket;
    private DataInputStream input;
    private DataOutputStream output;
    private String nickName = "newUser";
    private boolean running=true;

    public ClientHandler( Socket socket) throws IOException {

        this.socket = socket;
        this.input = new DataInputStream(socket.getInputStream());
        this.output = new DataOutputStream(socket.getOutputStream());
    }

    String getNickName() {
        return nickName;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    void authorization() throws IOException {
        //System.out.println("тестовое сообщение");
        sendMessage("Введите Ваше имя :\n");
        String message = input.readUTF();
        System.out.println(message);
        nickName = message;
    }

    public void sendMessage (String message) throws IOException {
        output.writeUTF(message);
        output.flush();
        System.out.printf("[DEBUG] send the Messange %s %s%n",prefixMessage(),message);
    }

    private String prefixMessage(){
        Calendar data = new GregorianCalendar();
        DateFormat df = new SimpleDateFormat("HH:mm:ss ");
        return new String (String.format("%s %s > ",df.format(data.getTime()),getNickName()));
    }


    @Override
    public void run() {
        while (running) {
            try {
                String message = input.readUTF();

                System.out.printf("[DEBUG] received the Messange %s %s%n", prefixMessage(), message);
                if (message.equals("/end")) {
                    output.writeUTF(message);
                    output.flush();
                    input.close();
                    output.close();
                    socket.close();
                    Server.myPrint("Соединение разорвано по запрросу клиента");
                    Server.removeClients(this);
                    break;
                }
                else if (message.startsWith("/w")) {
                    Server.messageToNick(message,prefixMessage(),this);
                }
                else  {
                    Server.broadCastMessage(String.format("%s%s%n",prefixMessage(),message));
                }

            } catch (Exception e) {
                Server.myPrint("Соединение разорвано ClientHandler");
                setRunning(false);
                Server.removeClients(this);
            }

        }
    }
}
