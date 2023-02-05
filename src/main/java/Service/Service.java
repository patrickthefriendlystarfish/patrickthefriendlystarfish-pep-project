package Service;

import DAO.DAO;
import Model.Account;

public class Service {
    private DAO dao;

    public Service()
    {
        dao = new DAO();
    }

    public Account insertAccountIntoDatabase(Account acc)
    {
      
        Account account = dao.insertAccount(acc);

       if(dao.insertAccount(acc) != null)
       {
            return account;
       }
       else 
       {
        return null;
       }
    }

    public Boolean checkInfo(String username, String password)
    {
       
    }





}
