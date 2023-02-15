package DAO;

import static org.junit.Assert.fail;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Util.ConnectionUtil;

public class MessageDAO {
     Connection conn = ConnectionUtil.getConnection();

    public Message createMessage(Message message)
    {
        try{
            String sql = "INSERT INTO Message (posted_by, message_text, time_posted_epoch) VALUES (?,?,?);";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, message.getPosted_by());
            ps.setString(2, message.getMessage_text());
            ps.setLong(3, message.getTime_posted_epoch());
            ps.executeUpdate();

            

            sql = "SELECT * FROM Message WHERE message_text = ? AND posted_by = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, message.getMessage_text());
            ps.setInt(2, message.getPosted_by());
            ResultSet rs = ps.executeQuery();

            if(rs.next())
            {
                return new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));
            }
            else
            {
                return null;
            }

        }
        catch(SQLException ex)
        {
            System.out.println("Caught SQLException at MessageDAO.createMessage()");
            ex.printStackTrace();
            return null;
        }
    }

  

   public Boolean checkIfRealUser(Message message)
   {
       
        int posted_by = message.getPosted_by();
        Boolean matchFound = false;
        try{
            String sql = "Select account_id from account;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            

            while(rs.next())
            {
                int account_id = rs.getInt("account_id");

                if(account_id == posted_by)
                {
                    System.out.println("Match found returning true;");
                    System.out.println(account_id);
                    return true;
                }
            }

            if(matchFound == false)
            {
                System.out.println("Match not found. Returning false;");
                return false;
            }

            return null;
            
        }
        catch(SQLException ex)
        {
            System.out.println("Caught SQLException in MessageDAO.checkIfRealUser()");
            ex.printStackTrace();
            return null;
        }
   }

   public ArrayList<Message> getAllMessages()
   {
        try{
        ArrayList<Message> allMessages = new ArrayList<>();
        String sql = "SELECT * FROM message;";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while(rs.next())
        {
            allMessages.add(new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch")));
        }

        return allMessages;

        }
        catch(SQLException ex)
        {
            System.out.println("Caught SQLException in MessageDAO.getAllMessages()");
            ex.printStackTrace();
            return null;
        }
   }

   public Message getMessageById(int message_id)
   {
        try{
            String sql = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, message_id);
            ResultSet rs = ps.executeQuery();

            while(rs.next())
            {
                return new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));
            }


        }
        catch(SQLException ex)
        {
            System.out.println("Caught SQLException at MessageDAO.getMessageById()");
            ex.printStackTrace();
            return null;
        }

        return null;
   }

   public ArrayList<Message> getAllMessagesByAccountId(int account_id)
   {
        ArrayList<Message> allMessages = new ArrayList<>();
        try{
            String sql = "SELECT * FROM message INNER JOIN account ON posted_by = account_id WHERE account_id = ?;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, account_id);
            ResultSet rs = ps.executeQuery();
            while(rs.next())
            {
                allMessages.add(new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch")));
            }
            return allMessages;
        }
        catch(SQLException ex)
        {
            System.out.println("Caught SQLException at AccountDAO.getAllMessagesByAccountId()");
            ex.printStackTrace();
            return null;
        }
   }

   public Message deleteMessage(int message_id) throws JsonProcessingException
   {
        ObjectMapper om = new ObjectMapper();
        try{
            Message message;
            String sql = "SELECT * FROM Message WHERE message_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, message_id);
            ResultSet rs = ps.executeQuery();
            if(rs.next())
            {
                 message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                 System.out.println("Message to Be deleted:" + om.writeValueAsString(message));
                 sql = "DELETE FROM message WHERE message_id = ?";
                 ps = conn.prepareStatement(sql);
                 ps.setInt(1, message_id);
                 ps.executeUpdate();
     
            }
            else 
            {
                message = null;
            }

           
            getMessageById(message_id);

            return message;

        }
        catch(SQLException ex)
        {
            System.out.println("Caught SQLException at MessageDAO.deleteMessage()");
            ex.printStackTrace();
            return null;
        }
   }

   public void updateMessage(String messageText, int message_id)
    {
        try{
            String sql = "UPDATE message SET message_text = ? WHERE message_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, messageText);
            ps.setInt(2, message_id);
            ps.executeUpdate();
        }
        catch(SQLException ex)
        {
            System.out.println("Caught SQLException in MessageDAO.updateMessage");
            ex.printStackTrace();
        }
    }
    
}



    

