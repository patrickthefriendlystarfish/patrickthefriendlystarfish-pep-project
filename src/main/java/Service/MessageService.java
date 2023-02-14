package Service;

import java.sql.Connection;
import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonProcessingException;

import DAO.MessageDAO;
import Model.Message;
import Util.ConnectionUtil;

public class MessageService {
    MessageDAO messageDAO = new MessageDAO();
    

    public Message callCreateMessage(Message message)
    {
       Message newmessage = messageDAO.createMessage(message);
       return newmessage;
    }

   
    public Boolean isMessageBlank(Message message)
    {
        String text = message.getMessage_text();

        if(text == "")
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public Boolean isUnderLength(Message message)
    {
        String messageText = message.getMessage_text();
        System.out.println(messageText);

        if(messageText.length() > 255)
        {
            
            return true;
        }
        else
        {
            return false;
        }
    }

    public Boolean callIsRealUser(Message message)
    {
        return messageDAO.checkIfRealUser(message);
    }

    public ArrayList<Message> callGetAllMessages()
    {
        return messageDAO.getAllMessages();
    }

    public Message callGetMessageById(int message_id)
    {   
        return messageDAO.getMessageById(message_id);
    }

    public ArrayList<Message> getAllMessagesByUser(int account_id)
    {
        return messageDAO.getAllMessagesByAccountId(account_id);
    }

    public Message callDeleteMessage(int message_id) throws JsonProcessingException
    {
        return messageDAO.deleteMessage(message_id);
    }

    public void callUpdateMessage(String message_text, int message_id)
    {
        messageDAO.updateMessage(message_text, message_id);
    }

    

    
    
}

