package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

/**
 * Created by Sidath on 2016-11-19.
 */
public class PersistantAccountDAO extends SQLiteOpenHelper implements AccountDAO {

    public static final String DATABASE_NAME = "140398L.db";
    Context context;
    public PersistantAccountDAO(Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.context = context;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table account (account_number varchar(30) primary key, bankname varchar(50), account_holder varchar(50), balance double)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists account");
        onCreate(db);
    }


    @Override
    public List<String> getAccountNumbersList() {
        List<String> arr = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from account", null);
        while(res.moveToNext()){
            arr.add(res.getString(0));
        }
        return arr;
    }

    @Override
    public List<Account> getAccountsList() {
        List<Account> arr = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from account", null);
        while(res.moveToNext()){
            arr.add(new Account(res.getString(0), res.getString(1), res.getString(2), res.getDouble(3)));
        }
        return arr;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from account where account_number="+accountNo, null);
        if(res.getCount()==0){
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);
        }

        else{
            res.moveToNext();
            return new Account(res.getString(0), res.getString(1), res.getString(2), res.getDouble(3));
        }

    }

    @Override
    public void addAccount(Account account) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("account_number", account.getAccountNo());
        contentValues.put("bankname", account.getBankName());
        contentValues.put("account_holder", account.getAccountHolderName());
        contentValues.put("balance", account.getBalance());
        db.insert("account", null, contentValues);

    }


    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase db = this.getWritableDatabase();
        int val = db.delete("account", "account_number = ?", new String[] {accountNo});
        if(val==0){
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);
        }

    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        Account account = getAccount(accountNo);
        switch (expenseType) {
            case EXPENSE:
                account.setBalance(account.getBalance() - amount);
                Toast.makeText(context, "Successfully Completed!", Toast.LENGTH_LONG).show();

            case INCOME:
                account.setBalance(account.getBalance() + amount);
                Toast.makeText(context, "Successfully Completed!", Toast.LENGTH_LONG).show();
                break;
        }

        contentValues.put("account_number", account.getAccountNo());
        contentValues.put("bankname", account.getBankName());
        contentValues.put("account_holder", account.getAccountHolderName());
        contentValues.put("balance", account.getBalance());
        db.update("account", contentValues, "account_number = ?", new String[] {accountNo});


    }


}
