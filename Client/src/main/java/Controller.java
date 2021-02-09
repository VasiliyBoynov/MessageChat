import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class Controller implements Initializable {

    private Network network;

    @FXML
    TextArea listStrings;
    public TextField message;

    public void sendMessage(ActionEvent actionEvent) throws IOException {
        //listStrings.appendText(String.format("%s> %s%n",nickName,message.getText()));
        System.out.println("[Debug] " + message.getText());
        network.writeMessage(message.getText());
        message.clear();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        network = Network.getInstance();
        if (!network.equals("null")){
            new Thread(new Runnable() {
                @Override
                public void run() {

                    try {
                        while (true) {
                            String mess = network.readMessage();
                            if (mess.equals("/end")) {
                                network.close();
                                break;
                            }
                            //Platform.runLater(() -> listView.getItems().add(message));
                            listStrings.appendText(mess);

                        }
                    } catch (IOException ioException) {
                        System.err.println("Сервер остановлен");
                        //Platform.runLater(() -> listView.getItems().add("Server was broken"));
                        listStrings.appendText("Сервер остановлен");
                    }

                }
            }).start();
        }


    }
}
