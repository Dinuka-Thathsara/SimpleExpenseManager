package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DB_Helper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME ="db_190282X" ;
    private static final int DATABASE_VERSION =1 ;

    public DB_Helper(Context context) {
        super(context, DATABASE_NAME, null,  DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDB) {//Create accounts and transaction tables
        String createAccount = "CREATE TABLE accounts(accountNo text primary key, bankName text, accountHolderName text, balance real);";
        sqLiteDB.execSQL(createAccount);

        String createTransation = "CREATE TABLE transactions(transactionId INTEGER PRIMARY KEY  AUTOINCREMENT, date text, accountNo text, type text, amount real);";
        sqLiteDB.execSQL(createTransation);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //Here Creates Tables again and again
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS account");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS transactions");
        onCreate(sqLiteDatabase);
    }
}
