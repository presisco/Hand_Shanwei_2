package com.example.syd.hand_shanwei_2.BookSeats;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.example.syd.hand_shanwei_2.Model.OrderInfo;

import java.util.ArrayList;

/**
 * Created by syd on 2015/11/19.
 */
public class OrderSeatHistoryHelper extends SQLiteOpenHelper {
    //数据库名
    private static final String DB_NAME="Order_Seat_History.db";
    //表名
    private static final String TABLE_NAME="OrderSeatHistory";
    //行id，安卓一般以_id开始
    private static final String KEY_ROW_ID="_id";
    //列名，本数据库包括三列
    private static final String KEY_FLOOR="floor";
    private static final String KEY_TIME="date";
    private static final String KEY_SEAT_NUM="seatnum";
    String firstid;
    final Context context;
    /**
     * Create a helper object to create, open, and/or manage a database.
     * This method always returns very quickly.  The database is not actually
     * created or opened until one of {@link #getWritableDatabase} or
     * {@link #getReadableDatabase} is called.
     *
     * @param context to use to open or create the database
     *                {@link #onUpgrade} will be used to upgrade the database; if the database is
     *                newer, {@link #onDowngrade} will be used to downgrade the database
     */
    //调用父类构造方法，参数name
    public OrderSeatHistoryHelper(Context context) {
        super(context, DB_NAME, null, 2);
        this.context = context;
    }

    /**
     * Called when the database is created for the first time. This is where the
     * creation of tables and the initial population of the tables should happen.
     *
     * @param db The database.
     */

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建预约历史表
      /*  db.execSQL("create table" + TABLE_NAME + " ( " + KEY_ROW_ID + " integer primary key autoincrement," +
                KEY_FLOOR + " text not null," + KEY_TIME + " text not null," + KEY_SEAT_NUM + " text not null);");*/
//        db.execSQL("CREATE TABLE " + TABLE_NAME + " ( " +
//                KEY_ROW_ID + " INTEGER PRIMARY KEY , " +
//                KEY_FLOOR + " TEXT ,"+KEY_SEAT_NUM+" TEXT ,"+KEY_TIME+"TEXT);");
        db.execSQL("CREATE TABLE " + TABLE_NAME + " ( " +
                KEY_ROW_ID + " INTEGER PRIMARY KEY , " +
                KEY_FLOOR + " TEXT ,"+ KEY_SEAT_NUM + " TEXT ,"+KEY_TIME+" TEXT );");
    }

    /**
     * Called when the database needs to be upgraded. The implementation
     * should use this method to drop tables, add tables, or do anything else it
     * needs to upgrade to the new schema version.
     * <p/>
     * <p>
     * The SQLite ALTER TABLE documentation can be found
     * <a href="http://sqlite.org/lang_altertable.html">here</a>. If you add new columns
     * you can use ALTER TABLE to insert them into a live table. If you rename or remove columns
     * you can use ALTER TABLE to rename the old table, then create the new table and then
     * populate the new table with the contents of the old table.
     * </p><p>
     * This method executes within a transaction.  If an exception is thrown, all changes
     * will automatically be rolled back.
     * </p>
     *
     * @param db         The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_NAME);
        onCreate(db);
    }
    public ArrayList<OrderInfo> quryOrderInfo(){
            ArrayList<OrderInfo> orderInfos=new ArrayList<>();
            SQLiteDatabase sqLiteDatabase=getReadableDatabase();
        //按时间先后排序获取
        Cursor cursor=sqLiteDatabase.query(TABLE_NAME, new String[]{KEY_FLOOR, KEY_SEAT_NUM,KEY_TIME}, null, null, null, null,KEY_TIME);
        for (cursor.moveToFirst();!(cursor.isAfterLast());cursor.moveToNext()) {
            OrderInfo orderInfo=new OrderInfo();
            orderInfo.setRoom(cursor.getString(cursor.getColumnIndex(KEY_FLOOR)));
            orderInfo.setSeatNum(cursor.getString(cursor.getColumnIndex(KEY_SEAT_NUM)));
            orderInfo.setDate(cursor.getString(cursor.getColumnIndex(KEY_TIME)));
            orderInfos.add(orderInfo);
        }
        cursor.close();
        sqLiteDatabase.close();
        return orderInfos;
    }

    //delete a orderhistory
    public boolean deleteFirstOrderinfo()
    {   SQLiteDatabase db=getWritableDatabase();
        SQLiteDatabase sqLiteDatabase=getWritableDatabase();
        Cursor cursor=sqLiteDatabase.query(TABLE_NAME, new String[]{KEY_ROW_ID}, null, null, null, null, KEY_TIME);
        cursor.moveToFirst();
        firstid=cursor.getString(cursor.getColumnIndex(KEY_ROW_ID));
        Log.i("bac","firstid:"+firstid);
        cursor.close();
        boolean b=db.delete(TABLE_NAME, KEY_ROW_ID + "=" + firstid, null)>0 ;
        db.close();
        Log.i("bac",b+"");
        return b;
    }
    //get the size of OrderInfo
    public long getOrderInfoSize() {
        String sql = "SELECT COUNT(*) FROM " + TABLE_NAME;
        SQLiteStatement statement = getReadableDatabase().compileStatement(sql);
        long count = statement.simpleQueryForLong();
        return count;
    }
    //add an orderinfo
    public boolean insertOrderInfo(String floor ,String seatNum,String data){
        SQLiteDatabase sqLiteDatabase=getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(KEY_FLOOR,floor);
        contentValues.put(KEY_SEAT_NUM,seatNum);
        contentValues.put(KEY_TIME,data);
        //保证最多只保留50条记录
        long count=getOrderInfoSize();
        Log.i("bac",count+"====================");
        if (count>=50){
            //delete first
            deleteFirstOrderinfo();
        }
        sqLiteDatabase.insert(TABLE_NAME,null,contentValues);
        sqLiteDatabase.close();
        return true;
    }

}
