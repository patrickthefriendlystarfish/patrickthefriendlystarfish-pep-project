package Controller;

import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.nullable;

import java.util.ArrayList;
import java.util.Scanner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import DAO.MessageDAO;
import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;

import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    
    AccountService accountService = new AccountService();
    MessageService messageService = new MessageService();
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        //app.start(8080);
        /*app.get("example-endpoint", this::exampleHandler);*/
        
       
       
       app.post("/register" , this::registerHandler);
       app.post("/login", this::loginHandler);
       app.post("/messages", this::createMessageHandler);
       app.get("/messages", this::getMessageHandler);
       app.get("/messages/{message_id}", this::getMessageByIdHandler);
       app.get("/accounts/{account_id}/messages", this::getMessagesByAccountIdHandler);
       app.delete("/messages/{message_id}", this::deleteMessagehandler);
       app.patch("/messages/{message_id}", this::patchMessageHandler);
       

       

          
          
         


       

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }

    private void registerHandler(Context ctx) throws JsonProcessingException
    {
       ObjectMapper om = new ObjectMapper();
       Account givenAccount = om.readValue(ctx.body(), Account.class);
       Boolean isUsernameInDatabase = accountService.callIfUsernameInDatabase(givenAccount);
       Boolean isPasswordValid = accountService.isPasswordValid(givenAccount.getPassword());
       Boolean isUsernameBlank = accountService.isUsernameBlank(givenAccount.getUsername());

        

        if(isUsernameInDatabase == false && isPasswordValid == true && isUsernameBlank == false)
        {
            Account addedAccount = accountService.callInsertAccount(givenAccount);
            ctx.status(200);
            ctx.result(om.writeValueAsString(addedAccount));
        }
        else
        {
            ctx.status(400);
            ctx.result("The username is already in the database or the password is too short. Please try again");
        }
  
    }

    private void loginHandler(Context ctx) throws JsonProcessingException
    {
       
        ObjectMapper om = new ObjectMapper();
        Account acc = om.readValue(ctx.body(), Account.class);
        Boolean usernameIsBlank = accountService.isUsernameBlank(acc.getUsername());
        Boolean passwordIsBlank = accountService.isPasswordBlank(acc.getPassword());



        if(usernameIsBlank == true || passwordIsBlank == true)
        {
            ctx.status(401);
            ctx.result("The username or password is blank. They cannot be blank. Please try again");
        }

       Account attemptedLogin = accountService.callGetFullAccount(acc.getUsername(), acc.getPassword());
 
       if(attemptedLogin == null)
       {
            ctx.status(401);
            ctx.result("The username or password is incorrect. Please try again");
       }
       else
       {
        System.out.println(om.writeValueAsString(attemptedLogin));
        ctx.status(200);
        ctx.result(om.writeValueAsString(attemptedLogin));
        
       }

    }

   private void createMessageHandler(Context ctx) throws JsonProcessingException
   {
        System.out.println("In createmessageHandler()");
       ObjectMapper om = new ObjectMapper();
       Message message = om.readValue(ctx.body(), Message.class);

       

       Boolean isRealUser = messageService.callIsRealUser(message);
       
      

       if(message.message_text.length() < 255 && message.message_text != ""  && isRealUser == true)
       {
            
            Message postedMessage = messageService.callCreateMessage(message);

            ctx.status(200);
            ctx.json(postedMessage);
       }
       else
       {
            ctx.status(400);
            ctx.result("The message was not persisted to the database. Please try again");
       }
       
    }

    private void getMessageHandler(Context ctx) throws JsonProcessingException
    {
        ObjectMapper om = new ObjectMapper();
        ArrayList<Message> allMessages = messageService.callGetAllMessages();


        if(allMessages == null)
        {
            ctx.status(400);
            ctx.result("Something went wrong when retrieving all messages");
        }
        else
        {
            ctx.status(200);
            ctx.json(allMessages);
        }


    }

    private void getMessageByIdHandler(Context ctx) throws JsonProcessingException
    {
        int pathParam = Integer.parseInt(ctx.pathParam("message_id"));

        Message message = messageService.callGetMessageById(pathParam);
        ObjectMapper om = new ObjectMapper();

        if(message == null)
        {
            /*ctx.status(400);
            ctx.result("Could not get message. Please try again");*/
        }
        else
        {
            ctx.status(200);
            ctx.json(message);
        } 
    }
    private void getMessagesByAccountIdHandler(Context ctx) throws JsonProcessingException
    {
        int pathParam = Integer.parseInt(ctx.pathParam("account_id"));

        ArrayList<Message> allMessages = messageService.getAllMessagesByUser(pathParam);
        ObjectMapper om = new ObjectMapper();


        if(allMessages == null)
        {
            ctx.status(400);
            ctx.result("Something went wrong. Pelase try again");
        }
        else{
            System.out.println(om.writeValueAsString(allMessages));
            ctx.status(200);
            ctx.json(allMessages);
            
        }

    }

    private void deleteMessagehandler(Context ctx) throws JsonProcessingException
    {
        
        System.out.println("in deleteMessageHandler()");
        int pathparam = Integer.parseInt(ctx.pathParam("message_id"));

        Message message = messageService.callDeleteMessage(pathparam);
        //System.out.println(om.writeValueAsString(message));

       
        if(message != null)
        {
            System.out.println("Message not null");
            ctx.json(message);
        }
 
    }

      
    private void patchMessageHandler(Context ctx) throws JsonProcessingException
    {
        int pathParam = Integer.parseInt(ctx.pathParam("message_id"));
        ObjectMapper om = new ObjectMapper();
        Message existingMessage = messageService.callGetMessageById(pathParam);
        Message updatedMessage = om.readValue(ctx.body(), Message.class);

        if(existingMessage == null || updatedMessage.getMessage_text() == "" || updatedMessage.getMessage_text().length() > 255)
        {
            ctx.status(400);
        }
        else
        {
            messageService.callUpdateMessage(updatedMessage.getMessage_text(), pathParam);
            existingMessage = messageService.callGetMessageById(pathParam);
            ctx.status(200);
            ctx.json(existingMessage);
        }


        



        
       



    }

        
       
}







