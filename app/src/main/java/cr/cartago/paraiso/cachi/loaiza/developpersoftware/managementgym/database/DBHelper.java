package cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Jay on 06-06-2017.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static DBHelper dbHelper = null;
    private Context context;
    private static final int DATABASE_VERSION = 1;

    private static final String CREATE_TABLE = "create table "+ DBCustomer.TABLE_NAME+
            "(id integer primary key autoincrement,"+ DBCustomer.CUSTOMER_NAME+" text,"+ DBCustomer.CUSTOMER_LASTNAME+" text,"
            + DBCustomer.CUSTOMER_ID+" integer);";

    private static final String DROP_TABLE = "drop table if exists "+ DBCustomer.TABLE_NAME;

    private DBHelper(Context context)
    {
        super(context, DBCustomer.DB_NAME,null,DATABASE_VERSION);
        this.context = context;
    }

    public static DBHelper getInstance(Context context) {
        /**
         * use the application context as suggested by CommonsWare.
         * this will ensure that you dont accidentally leak an Activitys
         * context (see this article for more information:
         * http://developer.android.com/resources/articles/avoiding-memory-leaks.html)
         */
        if (dbHelper == null) {
            dbHelper = new DBHelper(context);
        }
        return dbHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE);
        onCreate(db);
    }

    //  Save info for local Database(SQLite Database)
    public void SaveToLocalDatabase(String Name, String Email, int Sync_Status, SQLiteDatabase database){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBCustomer.CUSTOMER_NAME,Name);
        contentValues.put(DBCustomer.CUSTOMER_LASTNAME,Email);
        contentValues.put(DBCustomer.CUSTOMER_ID,Sync_Status);
        //  Insert Data into SQlite Database
        database.insert(DBCustomer.TABLE_NAME,null,contentValues);
    }

    // Read Info from SQLiteDatabse(SQLite Database)
    public Cursor ReadFromLocalDatabase(SQLiteDatabase database){

        //  Coloumn Names for reading data from
        String[] Projection = {DBCustomer.CUSTOMER_NAME, DBCustomer.CUSTOMER_LASTNAME, DBCustomer.CUSTOMER_ID};
        return (database.query(DBCustomer.TABLE_NAME,Projection,null,null,null,null,null));
    }

    //  Update SQLite Database(SQLite Database)
    public void UpdataLocalDatabase(String Name, String Email, int Sync_Status, SQLiteDatabase database){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBCustomer.CUSTOMER_ID, Sync_Status);
		//	Update SyncStatus Based on Name and Email
		String Selection = DBCustomer.CUSTOMER_NAME + " = ? AND " + DBCustomer.CUSTOMER_LASTNAME + " = ?";
        String[] selection_args = {Name, Email};
        database.update(DBCustomer.TABLE_NAME, contentValues, Selection, selection_args);
    }
}