package library.assistant.ui.addbook;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import library.assistant.database.DatabaseHandler;

public class BookAddController implements Initializable
{
   
    @FXML
    private  AnchorPane rootPane;  
    @FXML
    private JFXTextField title;
    @FXML
    private JFXTextField id;
    @FXML
    private JFXTextField author;
    @FXML
    private JFXTextField publisher;
    @FXML
    private JFXButton saveButton;
    @FXML
    private JFXButton cancelButton;
    
    private DatabaseHandler databaseHandler;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        databaseHandler = DatabaseHandler.getInstance();
        checkData();
    }    

    @FXML
    private void addBook(ActionEvent event)
    {
        String bookID = id.getText();
        String bookName = title.getText();
        String bookAuthor = author.getText();
        String bookPublisher = publisher.getText();
        
        if(bookID.isEmpty() || bookName.isEmpty() || bookAuthor.isEmpty() || bookPublisher.isEmpty())
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please Enter in all fields");
            alert.showAndWait();
            return;
        }
        
        String qu = "insert into BOOK values (" + 
                        "'" +bookID + "'," +
                        "'" +bookName + "'," +
                       "'" +bookAuthor + "'," +
                       "'" +bookPublisher + "'," +
                       "" +true+ " " +
                       ")";
        System.err.println("qu");
        if(databaseHandler.execAction(qu))
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Success");
            alert.showAndWait();
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Failed");
            alert.showAndWait();
        }
    }

    @FXML
    private void cancel(ActionEvent event)
    {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }

    private void checkData()
    {
        String qu = "select title from BOOK";
        ResultSet rs = databaseHandler.execQuery(qu);
        try
        {
            while(rs.next())
            {
                String titlex = rs.getString("title");
                System.err.println(titlex);
            }
        } catch (SQLException e)
        {
            Logger.getLogger(BookAddController.class.getName()).log(Level.SEVERE,null,e);
        }
    }
    
}
