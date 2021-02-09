
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Network {

    private static final int PORT = 8052;

    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    private static Network instance;

    public static Network getInstance() {
        if (instance == null) {
            try {
                instance = new Network();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return instance;
    }

    private Network() throws IOException {

        socket = new Socket("localhost", PORT);
        out = new DataOutputStream(socket.getOutputStream());
        in = new DataInputStream(socket.getInputStream());

    }

    public void writeMessage(String message) throws IOException {
        out.writeUTF(message);
        out.flush();
    }

    public String readMessage() throws IOException {
        return in.readUTF();
    }

    public void close() throws IOException {
        out.close();
        in.close();
        socket.close();
    }
}