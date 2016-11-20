package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.Context;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.exception.ExpenseManagerException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistantAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistantTransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;

/**
 * Created by Sidath on 2016-11-19.
 */
public class PersistantExpenseManager extends ExpenseManager {
    Context context;
    public PersistantExpenseManager(Context c){

        this.context = c;
    }
    @Override
    public void setup() throws ExpenseManagerException {
        TransactionDAO persistantTransactionDAO = new PersistantTransactionDAO(context);
        setTransactionsDAO(persistantTransactionDAO);

        AccountDAO persistantDAO = new PersistantAccountDAO(context);
        setAccountsDAO(persistantDAO);

        Account dummyAcct1 = new Account("140398", "BOC", "Sidath Asiri", 10000.0);
        Account dummyAcct2 = new Account("140686", "Sampath Bank", "Ishara", 80000.0);

        getAccountsDAO().addAccount(dummyAcct1);
        getAccountsDAO().addAccount(dummyAcct2);

    }



}
