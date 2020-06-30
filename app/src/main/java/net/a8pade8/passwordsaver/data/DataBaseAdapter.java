package net.a8pade8.passwordsaver.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Обертка для разных баз данных
 * для расширения определить нужные методы из android.database.sqlite.SQLiteDataBase по аналогии
 */
public class DataBaseAdapter {

    private SQLiteDatabase dataBase;
    private net.sqlcipher.database.SQLiteDatabase cryptoDatabase;

    boolean isCrypto = false;

    public DataBaseAdapter(SQLiteDatabase dataBase) {
        this.dataBase = dataBase;
    }

    public DataBaseAdapter(net.sqlcipher.database.SQLiteDatabase cryptoDatabase) {
        this.cryptoDatabase = cryptoDatabase;
        isCrypto = true;
    }

    public long insert(String table, String nullColumnHack, ContentValues values) {
        if (isCrypto) {
            return cryptoDatabase.insert(table, nullColumnHack, values);
        } else {
            return dataBase.insert(table, nullColumnHack, values);
        }
    }

    public Cursor query(boolean distinct, String table, String[] columns,
                        String selection, String[] selectionArgs, String groupBy,
                        String having, String orderBy, String limit) {
        if (isCrypto) {
            return cryptoDatabase.query(distinct, table, columns,
                    selection, selectionArgs, groupBy,
                    having, orderBy, limit);
        } else {
            return dataBase.query(distinct, table, columns,
                    selection, selectionArgs, groupBy,
                    having, orderBy, limit);
        }
    }

    public Cursor query(String table, String[] columns, String selection,
                        String[] selectionArgs, String groupBy, String having,
                        String orderBy){
        if (isCrypto) {
            return cryptoDatabase.query(table, columns, selection,
                    selectionArgs, groupBy, having, orderBy);
        } else {
            return dataBase.query(table, columns,
                    selection, selectionArgs, groupBy,
                    having, orderBy);
        }
    }

    public int delete(String table, String whereClause, String[] whereArgs) {
        if (isCrypto) {
            return cryptoDatabase.delete(table, whereClause, whereArgs);
        } else {
            return dataBase.delete(table, whereClause, whereArgs);
        }
    }
}
