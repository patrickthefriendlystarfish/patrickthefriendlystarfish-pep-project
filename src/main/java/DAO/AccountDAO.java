package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Util.ConnectionUtil;

public class AccountDAO {
    
    public Boolean checkIfUsernameExists(String username)
    {
        int count = 0;
        Connection conn = ConnectionUtil.getConnection();
        try{
            String sql = "Select * from Account where username = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            while(rs.next())
            {
                count++;
            }
            if(count == 0)
            {
                return true;
            }
            return false;
        }
        catch(SQLException ex)
        {  
            System.out.println("Caught SQLException"); 
            return false;
        }
    }
}
