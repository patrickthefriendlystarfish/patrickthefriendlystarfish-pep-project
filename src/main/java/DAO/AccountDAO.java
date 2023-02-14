package DAO;

import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.nullable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.h2.command.ddl.PrepareProcedure;

import Model.Account;
import Util.ConnectionUtil;

public class AccountDAO {
    
    Connection conn = ConnectionUtil.getConnection();
    

    public Boolean checkIfUsernameIsInDatabase(String username)
    {
       try
       {
            String sql = "SELECT * FROM Account where username = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            Account acc = null;

                if(rs.next())
                {
                    return true;
                }
                else
                {
                    return false;
                }
       }
       catch(SQLException ex)
       {
         System.out.println("Caught SQLException in AccountDAO.checkIfUsernameIsInDatabase()");
         ex.printStackTrace();
         return null;
         
       }

    }

    public Account insertAccount(Account acc)
    {
        try{
            String username = acc.getUsername();
            String password = acc.getPassword();
            String sql = "INSERT INTO Account(username, password) VALUES(?,?);";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ps.execute();

            Account account = getAccountByUsername(username);

            return account;

            


        }
        catch(SQLException ex)
        {
            System.out.println("Caught SQLException in AccountDAO.insertAccount()");
            ex.printStackTrace();
        }
        return null;
    }

    public Account getAccountByUsername(String username)
    {
        try{
        String sql = "SELECT * FROM Account WHERE username = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, username);
        ResultSet rs = ps.executeQuery();

            while(rs.next())
            {
                return new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
            }
            return null;
        }
        catch(SQLException ex)
        {
            System.out.println("Caught SQLException in AccountDAO.getAccountByUsername()");
            ex.printStackTrace();
            return null;
        }
    }

    public Account getAccountWithUsernameAndPassword(String username, String password)
    {
        try{
            String sql = "SELECT * FROM Account WHERE username = ? AND password = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            Boolean hasEntered = false;

           if(rs.next())
           {
                System.out.println("In rs.next()");
                return new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
           }
           else
           {
                System.out.println("In else");
                return null;
           }
            

        }
        catch(SQLException ex)
        {
            System.out.println("Caught SQLException in AccountDAO.getAccountWithUsernameAndPassword()");
            ex.printStackTrace();
            return null;
        }

    }
    
}
