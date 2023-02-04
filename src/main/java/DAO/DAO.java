package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import Model.Account;
import Util.ConnectionUtil;

public class DAO {
    
    public Account insertAccount(Account account)
    {
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "INSERT INTO ACCOUNT(username, password) VALUES(?,?);";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, account.getUsername());
            ps.setString(2, account.getPassword());
            ps.execute();
            return account;
        }
        catch(SQLException ex)
        {
            ex.printStackTrace();
            return null;
            
        }
    }
}
