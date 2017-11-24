package library.assistant.ui.listbook;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import library.assistant.database.DatabaseHandler;
import library.assistant.ui.addbook.BookAddController;

public class BookListController implements Initializable 
{
    ObservableList<Book> list = FXCollections.observableArrayList();
    @FXML
    private AnchorPane rootPane;
    @FXML
    private TableView<Book> tableView;
    @FXML
    private TableColumn<Book, String> titleCol;
    @FXML
    private TableColumn<Book , String> idCol;
    @FXML
    private TableColumn<Book, String> authorCol;
    @FXML
    private TableColumn<Book, String> publisherCol;
    @FXML
    private TableColumn<Book, Boolean> availabilityCol;
    
    private DatabaseHandler handler;

    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
       initCol();
       loadData();
       
    }    

    private void initCol()
    {
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
        publisherCol.setCellValueFactory(new PropertyValueFactory<>("publisher"));
        availabilityCol.setCellValueFactory(new PropertyValueFactory<>("availability"));     
     }

    private void loadData()
    {
        handler = DatabaseHandler.getInstance();
        String qu = "select * from BOOK";
        ResultSet rs = handler.execQuery(qu);
        try
        {
            while(rs.next())
            {
                String title = rs.getString("title");
                String author = rs.getString("author");
                String id = rs.getString("id");
                String publisher = rs.getString("publisher");
                boolean avail = rs.getBoolean("isAvail");
                
                list.add(new Book(title, id, author, publisher, avail));
            }
        } catch (SQLException e)
        {
            Logger.getLogger(BookAddController.class.getName()).log(Level.SEVERE,null,e);
        }
        
        tableView.getItems().setAll(list);
    }
    
    public static class Book
    {
        private final  SimpleStringProperty title;
        private final  SimpleStringProperty id;
        private final  SimpleStringProperty author;
        private final  SimpleStringProperty publisher;
        private final  SimpleBooleanProperty availability;
        
        public Book(String title, String id, String author, String publisher, Boolean avail)
        {
            this.title = new SimpleStringProperty(title);
            this.id = new SimpleStringProperty(id);
            this.author = new SimpleStringProperty(author);
            this.publisher = new SimpleStringProperty(publisher);
            this.availability = new SimpleBooleanProperty(avail);
        }

        public String getTitle()
        {
            return title.get();
        }

        public String getId()
        {
            return id.get();
        }

        public String getAuthor()
        {
            return author.get();
        }

        public String getPublisher()
        {
            return publisher.get();
        }

        public Boolean getAvailability()
        {
            return availability.get();
        }
        
        
    }
    
}
