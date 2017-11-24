package library.assistant.ui.main;

import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.effects.JFXDepthManager;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import library.assistant.database.DatabaseHandler;

public class MainController implements Initializable
{
    DatabaseHandler handler = DatabaseHandler.getInstance();
    ObservableList<String> issueData;

    @FXML
    private HBox book_info;
    @FXML
    private TextField bookIDInput;
    @FXML
    private Text bookName;
    @FXML
    private Text bookAuthor;
    @FXML
    private Text bookStatus;
    @FXML
    private HBox member_info;
    @FXML
    private TextField memberIDInput;
    @FXML
    private Text memberName;
    @FXML
    private Text memberMobile;
    @FXML
    private JFXTextField bookID;
    @FXML
    private JFXListView<String> issueDataList;
    
    Boolean isReadyForSubmission;
    @FXML
    private StackPane rootPane;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        JFXDepthManager.setDepth(book_info, 1);
        JFXDepthManager.setDepth(member_info, 1);
    }    

    @FXML
    private void addMember(ActionEvent event)
    {
        loadWindow("/library/assistant/ui/addmember/add_member.fxml", "Add New Member");
    }

    @FXML
    private void addBook(ActionEvent event)
    {
        loadWindow("/library/assistant/ui/addbook/add_book.fxml", "Add New Book");
    }

    @FXML
    private void loadMemberTable(ActionEvent event)
    {
        loadWindow("/library/assistant/ui/listmember/member_list.fxml", "Member");
    }

    @FXML
    private void loadBookTable(ActionEvent event)
    {
        loadWindow("/library/assistant/ui/listbook/book_list.fxml", "Book");
    }
    
     @FXML
    private void LoadSettings(ActionEvent event)
    {
         loadWindow("/library/assistant/settings/settings.fxml", "Settings");
    }
    
    void loadWindow(String location, String title)
    {
        try
        {
            Parent root = (Parent) FXMLLoader.load(getClass().getResource(location));
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setScene(new Scene(root));
            stage.setTitle(title);
            stage.show();
            
            DatabaseHandler.getInstance();
        } catch (IOException e)
        {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
    void clearBookApach()
    {
        bookName.setText("");
        bookAuthor.setText("");
        bookStatus.setText("");
    }

    @FXML
    private void LoadBookInfo(ActionEvent event)
    {
        clearBookApach();
        
        String id = bookIDInput.getText();
        String qu = "select * from BOOK where id = '" + id + "'";
        ResultSet rs = handler.execQuery(qu);
        Boolean flag = false;
        try
        {
            while(rs.next())
            {
                String bName = rs.getString("title");
                String bAuthor = rs.getString("author");
                Boolean bStatus = rs.getBoolean("isAvail");
                
                bookName.setText(bName);
                bookAuthor.setText(bAuthor);
                String status = (bStatus)? "Available" : "Not Available";
                bookStatus.setText(status);
                
                flag = true;
            }
            if(!flag)
            {
                bookName.setText("No Such Book Available");
            }
            
        } catch (SQLException e)
        {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE,null,e);
        }
    }
    
    void memberClearApach()
    {
        memberMobile.setText("");
        memberName.setText("");
    }

    @FXML
    private void loadMemberInfo(ActionEvent event)
    {
        memberClearApach();
        
        String id = memberIDInput.getText();
        String qu = "select * from MEMBER where id = '" + id + "'";
        ResultSet rs = handler.execQuery(qu);
        Boolean flag = false;
        try
        {
            while(rs.next())
            {
                String mName = rs.getString("name");
                String mMobile = rs.getString("mobile");
                            
                memberName.setText(mName);
                memberMobile.setText(mMobile);
               
                flag = true;
            }
            if(!flag)
            {
                memberName.setText("No Such Member Available");
            }
            
        } catch (SQLException e)
        {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE,null,e);
        }
    }

    @FXML
    private void loadIssueOperation(ActionEvent event)
    {
        String bookID = bookIDInput.getText();
        String memberID = memberIDInput.getText();
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Comfirm Issue Operation");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure to issue the book " + bookName.getText() + "\n to " + memberName.getText() + "?");
        
        Optional<ButtonType> response = alert.showAndWait();
        if(response.get() == ButtonType.OK)
        {
            String str = "insert into ISSUE(memberID, bookID) values ( '"+ memberID + "', '" + bookID + " ')";
            String str1 = "update BOOK set isAvail = false where id = '" + bookID + "'";
            System.out.println(str + " and " + str1 );
            
            if(handler.execAction(str) && handler.execAction(str1))
            {
                Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                alert1.setTitle("Success");
                alert1.setHeaderText(null);
                alert1.setContentText("Book Issue Complete");
                alert1.showAndWait();
            }
            else
            {
                Alert alert1 = new Alert(Alert.AlertType.ERROR);
                alert1.setTitle("Failed");
                alert1.setHeaderText(null);
                alert1.setContentText("Issue Operation Failed");
                 alert1.showAndWait();
            }
        }
        else
        {
                Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                alert1.setTitle("Canceled");
                alert1.setHeaderText(null);
                alert1.setContentText("Issue Operation Canceled");
                alert1.showAndWait();
        }
    }

    @FXML
    private void LoadBookInfo2(ActionEvent event)
    {
        issueData = FXCollections.observableArrayList();
        isReadyForSubmission = false;
        
        String id = bookID.getText();
        String qu = "select * from issue where bookID = '" + id + "'";
        ResultSet rs = handler.execQuery(qu);
        try
        {
            while(rs.next())
            {
                String mBookID = id;
                String mMemberID = rs.getString("memberID");
                Timestamp mIssueTime = rs.getTimestamp("issueTime");
                int mRenewCount = rs.getInt("renew_count");
            
                issueData.add("Issue Date and Time: " + mIssueTime + "GMT");
                issueData.add("Renew Count: " + mRenewCount);
                 issueData.add("------Book Information------");
                qu = "select * from BOOK where ID = '" + mBookID + "'";
                ResultSet r1 = handler.execQuery(qu);
                while(r1.next())
                {
                    issueData.add("Book Name :" + r1.getString("title"));
                    issueData.add("Book ID :" + r1.getString("id"));
                    issueData.add("Book Author :" + r1.getString("author"));
                    issueData.add("Book Publisher :" + r1.getString("publisher"));
                    
                }
                
                issueData.add("------Member Information------");
                qu = "select * from MEMBER where ID = '" + mMemberID + "'";
                r1 = handler.execQuery(qu);
                while(r1.next())
                {
                    issueData.add("Name :" + r1.getString("name"));
                    issueData.add("Mobile :" + r1.getString("mobile"));
                    issueData.add("Email :" + r1.getString("email"));
                }
                isReadyForSubmission = true;
            } 
        } catch (SQLException e)
        {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE,null,e);
        }
        
        issueDataList.getItems().setAll(issueData);
    }

    @FXML
    private void LoadSubmissionOp(ActionEvent event)
    {
        if (!isReadyForSubmission)
        {
                Alert alert1 = new Alert(Alert.AlertType.ERROR);
                alert1.setTitle("Failed");
                alert1.setHeaderText(null);
                alert1.setContentText("Please select a book to submit");
                alert1.showAndWait();
        }
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Comfirm Submisson Operation");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure to return the book? ");
        
        Optional<ButtonType> response = alert.showAndWait();
        if(response.get() == ButtonType.OK)
        {
      
        String id = bookID.getText();
        String st1 = "delete from ISSUE where BOOKID = '" + id + "'";
        String st2 = "update BOOK set ISAVAIL = TRUE where ID = '" + id + "'";
        
         if(handler.execAction(st1) && handler.execAction(st2))
         {
                Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                alert1.setTitle("Success");
                alert1.setHeaderText(null);
                alert1.setContentText("Book Has Been Submitted");
                alert1.showAndWait();
         }
         else
         {
                Alert alert1 = new Alert(Alert.AlertType.ERROR);
                alert1.setTitle("Failed");
                alert1.setHeaderText(null);
                alert1.setContentText("Submitted Has Been Failed");
                alert1.showAndWait();
         }
        }
        else
        {
                Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                alert1.setTitle("Canceled");
                alert1.setHeaderText(null);
                alert1.setContentText("Submisson Operation Canceled");
                alert1.showAndWait();
        }
       
    }

    @FXML
    private void LoadRenewOp(ActionEvent event)
    {
        if (!isReadyForSubmission)
        {
                Alert alert1 = new Alert(Alert.AlertType.ERROR);
                alert1.setTitle("Failed");
                alert1.setHeaderText(null);
                alert1.setContentText("Please select a book to renew");
                alert1.showAndWait();
        }
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Comfirm Renew Operation");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure to renew the book? ");
        
        Optional<ButtonType> response = alert.showAndWait();
        if(response.get() == ButtonType.OK)
        {
    
        String st2 = "update ISSUE set issueTime = CURRENT_TIMESTAMP, renew_count = renew_count+1  where BOOKID = '" + bookID.getText() + "'";
            System.out.println(st2); 
       
         if(handler.execAction(st2))
         {
                Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                alert1.setTitle("Success");
                alert1.setHeaderText(null);
                alert1.setContentText("Book Has Been Renew");
                alert1.showAndWait();
         }
         else
         {
                Alert alert1 = new Alert(Alert.AlertType.ERROR);
                alert1.setTitle("Failed");
                alert1.setHeaderText(null);
                alert1.setContentText("Renew Has Been Failed");
                alert1.showAndWait();
         }
        }
        else
        {
                Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                alert1.setTitle("Canceled");
                alert1.setHeaderText(null);
                alert1.setContentText("Renew Operation Canceled");
                alert1.showAndWait();
        }
    }

    @FXML
    private void handleMenuAddBook(ActionEvent event)
    {
         loadWindow("/library/assistant/ui/addbook/add_book.fxml", "Add New Book");
    }

    @FXML
    private void handleMenuAddMember(ActionEvent event)
    {
        loadWindow("/library/assistant/ui/addmember/add_member.fxml", "Add New Member");
    }

   

    @FXML
    private void handleMenuClose(ActionEvent event)
    {
        ((Stage) rootPane.getScene().getWindow()).close();
    }

    @FXML
    private void handleMenuViewBook(ActionEvent event)
    {
         loadWindow("/library/assistant/ui/listbook/book_list.fxml", "Book");
    }

    @FXML
    private void handleMenuViewMember(ActionEvent event)
    {
         loadWindow("/library/assistant/ui/listmember/member_list.fxml", "Member");
    }

    @FXML
    private void handleMenuFullScreen(ActionEvent event)
    {
         Stage stage = (Stage) rootPane.getScene().getWindow();
         stage.setFullScreen(true);
                 
    }

   
    

   

   
}
