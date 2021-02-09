import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Client extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Network network = Network.getInstance();

        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("TextMessage");
        primaryStage.setScene(new Scene(root, 500, 300));
        primaryStage.show();
        primaryStage.setOnCloseRequest(request -> {
            try {
                network.writeMessage("/end");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

    }


    public static void main(String[] args) {
        launch(args);
    }
}
