package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class PersistentTransactionDAO extends DB_Helper implements TransactionDAO {
    private List<Transaction> transactionList;

    public PersistentTransactionDAO(Context context) {
        super(context);
        this.transactionList = new ArrayList<Transaction>();
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        Calendar calender = Calendar.getInstance();
        calender.setTime(date);
        String strdate = calender.get(Calendar.YEAR) + "," + calender.get(Calendar.MONTH) +","+ calender.get(Calendar.DAY_OF_MONTH);

        contentValues.put("accountNo", accountNo);
        contentValues.put("date",strdate);
        contentValues.put("amount", amount);
        contentValues.put("type", String.valueOf(expenseType));

        database.insert("transactions", null, contentValues);

    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        transactionList = new ArrayList<Transaction>();

        String sql = "SELECT * FROM transactions";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql,null);

        if (cursor.moveToFirst()){

            do{
                String[] date = cursor.getString(1).split(",");
                Calendar calendar = Calendar.getInstance();
                calendar.set(Integer.parseInt(date[0]),Integer.parseInt(date[1]), Integer.parseInt(date[2]));
                Date tarnsDate = calendar.getTime();

                String accountNo = cursor.getString(2);

                String type = cursor.getString(3);
                ExpenseType expenseType = ExpenseType.valueOf(type.toUpperCase());

                double amount = cursor.getDouble(4);

                Transaction acc = new Transaction(tarnsDate,accountNo,expenseType,amount);
                transactionList.add(acc);


            }while(cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return transactionList;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        transactionList = getAllTransactionLogs();
        int size = transactionList.size();

        if (size <= limit) {
            return transactionList;
        }
        return transactionList.subList(size - limit, size);
    }
}
