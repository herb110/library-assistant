package library.assistant.settings;

import library.assistant.ui.main.*;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import library.assistant.database.DatabaseHandler;

public class SettingsLoader extends Application
{
    @Override
    public void start(Stage stage) throws Exception
    {
        Parent root = FXMLLoader.load(getClass().getResource("settings.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setTitle("Settings");
        stage.setScene(scene);
        stage.show();
        
        new Thread(() -> {
            DatabaseHandler.getInstance();
        }).start();
        
      
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        launch(args);
    }
    
}