package library.assistant.ui.login;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import library.assistant.database.DatabaseHandler;
import library.assistant.settings.Preferences;
import library.assistant.ui.main.MainController;
import org.apache.commons.codec.digest.DigestUtils;

public class LoginController implements Initializable
{

    @FXML
    private JFXTextField username;
    @FXML
    private JFXPasswordField password;
    @FXML
    private Label titleLabel;
    
    Preferences preferences;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        preferences = Preferences.getPreferences();
    }    

    @FXML
    private void handleLoginButton(ActionEvent event)
    {
        titleLabel.setText("Library Assistant Login");
        titleLabel.setStyle("-fx-background-color: black");
        
        String uname = username.getText();
        String pass = DigestUtils.shaHex(password.getText());
        
        if(uname.equals(preferences.getUsername()) && pass.equals(preferences.getPassword()))
        {
            closeStage();
            loadMain();
            
        }else
        {
            titleLabel.setText("Invalid Credentails");
            titleLabel.setStyle("-fx-background-color: #d32f2f; -fx-text-fill: white");
        }
    }

    @FXML
    private void handleCancelButton(ActionEvent event)
    {
        System.exit(0);
    }

    private void closeStage()
    {
       ((Stage) username.getScene().getWindow()).close();
    }

    private void loadMain()
    {
        try
        {
            Parent root = (Parent) FXMLLoader.load(getClass().getResource("/library/assistant/ui/main/main.fxml"));
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setScene(new Scene(root));
            stage.setTitle("Library Assistant");
            stage.show();
            
            DatabaseHandler.getInstance();
        } catch (IOException e)
        {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
}
