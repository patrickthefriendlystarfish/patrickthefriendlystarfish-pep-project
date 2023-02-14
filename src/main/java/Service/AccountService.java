package Service;

import static org.mockito.ArgumentMatchers.nullable;

import java.sql.Connection;

import java.util.Scanner;



import DAO.AccountDAO;
import Model.Account;
import Util.ConnectionUtil;

public class AccountService {
    private AccountDAO accountDAO;
    Connection conn = ConnectionUtil.getConnection();

    public AccountService()
    {
       accountDAO = new AccountDAO();
    }

    public Boolean isUsernameBlank(String username)
    {
        

        if(username == "")
        {
            return true;
        }
        else
        {
            return false;
        }


    }

    public Boolean isPasswordBlank(String password)
    {
            if(password == "")
            {return true ;
            }
            else
            {
                return false;
            }
    }
    public Boolean callIfUsernameInDatabase(Account acc)
    {
        String username = acc.getUsername();
        if(username == "")
        {
            return false;
        }


        Boolean isInDatabase = accountDAO.checkIfUsernameIsInDatabase(username);
        return isInDatabase;
       
    }

    public Boolean isPasswordValid(String password)
    {
        if(password.length() >= 4)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public Account callInsertAccount(Account acc)
    {
        return accountDAO.insertAccount(acc);
    }
    
    public Account callGetFullAccount(String username, String password)
    {
        return accountDAO.getAccountWithUsernameAndPassword(username, password);
    }


  
}


   



   
