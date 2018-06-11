package com.example.miestro.google_maps;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;

/**
 * Created by MIESTRO on 06/06/2018.
 */

public class DB_Sqlite extends SQLiteOpenHelper {
    public static final  String DBname ="data.db";

    public DB_Sqlite(Context context) {
        super(context, DBname, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table user (id TEXT,name TEXT,profile_url TEXT,cover_url TEXT)");

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS user");
        onCreate(db);
    }


    public boolean insertData (String id , String name ){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id",id);
        contentValues.put("name",name);
        long resuelt = db.insert("user",null,contentValues);
        if(resuelt == -1)
            return false;
        else
            return true;

    }


    public ArrayList getAllrecord(){
        ArrayList<user> arrayList = new ArrayList();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from user",null);
        res.moveToFirst();

        while(res.isAfterLast()==false){
            String id = res.getString(res.getColumnIndex("id"));
            String name = res.getString(res.getColumnIndex("name"));
            String profile_url = res.getString(res.getColumnIndex("profile_url"));
            String cover_url = res.getString(res.getColumnIndex("cover_url"));
            user user = new user(id,name,profile_url,cover_url);
            arrayList.add(user);

            res.moveToNext();
        }

        return arrayList;
    }


    public void updateData(String id,String type,String url) {
        SQLiteDatabase database = getWritableDatabase();

        String sql="";
        if(type.equals("profile")) {

             sql = "UPDATE user SET profile_url = ? WHERE id = ?";


        }else if (type.equals("cover")){

             sql = "UPDATE user SET cover_url = ? WHERE id = ?";

        }
        SQLiteStatement statement = database.compileStatement(sql);
        statement.bindString(1, url);
        statement.bindString(2, id);
        statement.execute();
        database.close();

    }


    public boolean updateimages(String id,String type,byte[] image){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

           //contentValues.put("id",id);

           if(type.equals("profile")) {

                contentValues.put("profile", image);

           }else if (type.equals("cover")){

                contentValues.put("cover", image);

            }

         if(db.update("user",contentValues,"id= ?",new String[]{id})>0)
         {
             return true;}
        else{
            return false;
        }
    }


    public Integer Delete(String id){

        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("user","id = ?",new String[]{id});

    }


}
