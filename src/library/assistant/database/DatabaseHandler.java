package library.assistant.database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

public class DatabaseHandler
{
    private static DatabaseHandler handler;
    
    private static final String DB_URL = "jdbc:derby:database;create=true";
    private static Connection connection = null;
    private static Statement statement = null;
    
    private  DatabaseHandler()
    {
        createConnection();
        setupBookTable();
        setupMemberTable();
        setupIssueTable();
    }
    
    public static DatabaseHandler getInstance()
    {
        if(handler == null)
        {
            handler = new DatabaseHandler();
        }
        return  handler;
    }
    
     void  createConnection()
    {
        try
        {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
            connection = DriverManager.getConnection(DB_URL);
        } catch (Exception e)
        {
            JOptionPane.showMessageDialog(null, "Can't load Database", "Database Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }
    
   private void setupBookTable()
    {
        String TABLE_NAME = "BOOK";
        try
        {
            statement = connection.createStatement();
            DatabaseMetaData dbn = connection.getMetaData();
            ResultSet tables = dbn.getTables(null, null, TABLE_NAME.toUpperCase(), null);
            if(tables.next())
            {
                System.out.println("Table " + TABLE_NAME + "already exists. Ready for go!");
            }
            else
            {
                statement.execute("create table " + TABLE_NAME + "("
                    + "         id varchar(200) primary key,\n"
                    + "         title varchar(200),\n"
                    + "         author varchar(200),\n"
                    + "         publisher varchar(100),\n"
                    + "         isAvail boolean default true"
                    + "     )");
            }
        } catch (SQLException e)
        {
            System.err.println(e.getMessage() + "---setupDatabase");
        } finally
        {
        }
    }
    
    public ResultSet execQuery(String query)
    {
        ResultSet result;
        try
        {
            statement = connection.createStatement();
            result = statement.executeQuery(query);
        } catch (SQLException e)
        {
            System.out.print("Exception at execQuery:dataHandler" + e.getLocalizedMessage());
            return null;
        } finally
        {
        }
        return  result;
    }
    
    public boolean execAction(String qu)
    {
        try
        {
            statement = connection.createStatement();
            statement.execute(qu);
            return true;
        } catch (SQLException e)
        {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Error occured" , JOptionPane.ERROR_MESSAGE);
            System.out.println("Exception at execQuery:dataHandler" + e.getLocalizedMessage());
            return false;
        }finally
        {           
        }
    }

    private void setupMemberTable()
    {
         String TABLE_NAME = "MEMBER";
        try
        {
            statement = connection.createStatement();
            DatabaseMetaData dbn = connection.getMetaData();
            ResultSet tables = dbn.getTables(null, null, TABLE_NAME.toUpperCase(), null);
            if(tables.next())
            {
                System.out.println("Table " + TABLE_NAME + "already exists. Ready for go!");
            }
            else
            {
                statement.execute("create table " + TABLE_NAME + "("
                    + "         id varchar(200) primary key,\n"
                    + "         name varchar(200),\n"
                    + "         mobile varchar(200),\n"
                    + "         email varchar(100)\n"
                    + "     )");
            }
        } catch (SQLException e)
        {
            System.err.println(e.getMessage() + "---setupDatabase");
        } finally
        {
        }
       
    }
    
    private void setupIssueTable()
    {
         String TABLE_NAME = "ISSUE";
        try
        {
            statement = connection.createStatement();
            DatabaseMetaData dbn = connection.getMetaData();
            ResultSet tables = dbn.getTables(null, null, TABLE_NAME.toUpperCase(), null);
            if(tables.next())
            {
                System.out.println("Table " + TABLE_NAME + "already exists. Ready for go!");
            }
            else
            {
                statement.execute("create table " + TABLE_NAME + "("
                    + "         bookID varchar(200) primary key,\n"
                    + "         memberID varchar(200),\n"
                    + "         issueTime timestamp default CURRENT_TIMESTAMP,\n"
                    + "         renew_count integer default 0,\n"
                    + "         foreign key (bookID)   references BOOK(id),\n"
                    + "         foreign key (memberID) references MEMBER(id)"
                    + "     )");
            }
        } catch (SQLException e)
        {
            System.err.println(e.getMessage() + "---setupDatabase");
        } finally
        {
        }
       
    }
}
