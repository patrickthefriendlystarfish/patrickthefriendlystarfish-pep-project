package Controller;

import static org.mockito.ArgumentMatchers.nullable;

import java.util.Scanner;

import Model.Account;
import Service.Service;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.start(8080);
        /*app.get("example-endpoint", this::exampleHandler);*/
       
        app.post("/register", ctx -> {
            String username = "";
            String password = "";
            Scanner sc = new Scanner(System.in);
            System.out.print("Enter a username: ");
            username = sc.nextLine();
            System.out.print("\n");
            System.out.print("Enter a Password: ");
            password = sc.nextLine();
            System.out.print("\n");

            Account account = new Account();
            account.setUsername(username);
            account.setPassword(password);

            Service service = new Service();
            
           Account acc = service.insertAccountIntoDatabase(account);

           if(acc == null)
           {
             ctx.status(400);
           }
           else
           {
             ctx.status(200);
           }

            

        });

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }




}