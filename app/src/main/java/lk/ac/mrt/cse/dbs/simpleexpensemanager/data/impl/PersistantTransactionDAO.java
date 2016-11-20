package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

/**
 * Created by Sidath on 2016-11-19.
 */
public class PersistantTransactionDAO extends SQLiteOpenHelper implements TransactionDAO {
    public static final String DATABASE_NAME = "140398L";
    public PersistantTransactionDAO(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table logs (id integer primary key autoincrement,date varchar(20), account_number varchar(30), type varchar(20), amount double)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists logs");
        onCreate(db);
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("date", date.toString());
        contentValues.put("account_number", accountNo);
        contentValues.put("type", String.valueOf(expenseType.values()[0]));
        contentValues.put("amount", amount);
        db.insert("logs", null, contentValues);
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        List<Transaction> arr = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from logs", null);
        while (res.moveToNext()){
            String dateStr = res.getString(1);
            DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
            java.util.Date date1 = null;
            try {
                date1 = formatter.parse(dateStr);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            Date date =date1;
            String accNum = res.getString(2);
            ExpenseType expenseType = ExpenseType.valueOf(res.getString(3));
            double amount = res.getDouble(4);
            arr.add(new Transaction(date, accNum, expenseType, amount));
        }
        return arr;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from logs", null);
        int size = res.getCount();
        List<Transaction> transactions = getAllTransactionLogs();
        if (size <= limit) {
            return transactions;
        }
        return transactions.subList(size - limit, size);
    }


}
