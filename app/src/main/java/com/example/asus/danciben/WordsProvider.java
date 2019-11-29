package com.example.asus.danciben;



import android.content.ContentProvider;

import android.content.ContentValues;

import android.content.UriMatcher;

import android.database.Cursor;

import android.database.sqlite.SQLiteDatabase;

import android.net.Uri;



import com.example.asus.danciben.wordcontract.Words;



public class WordsProvider extends ContentProvider {



    public static final int WORDS_DIR=0;

    public static final int WORDS_ITEM=1;

    public static final String AUTHORITY="com.example.asus.danciben.provider";

    private static UriMatcher uriMatcher;

    private WordsDBHelper mDbHelper;

    static {

        uriMatcher=new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(AUTHORITY,"words",WORDS_DIR);

        uriMatcher.addURI(AUTHORITY,"words/*",WORDS_ITEM);

    }



    public boolean onCreate(){

        mDbHelper=new WordsDBHelper(this.getContext());

        return true;

    }



    @Override

    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteDatabase db=mDbHelper.getReadableDatabase();

        Cursor cursor=null;

        switch (uriMatcher.match(uri)){

            case WORDS_DIR:

                //查询words表中所有数据

                cursor=db.query("Words.Word.TABLE_NAME ",projection,selection,selectionArgs,null,null,sortOrder);

                break;

            case WORDS_ITEM:

                //查询words表中单条数据

                String words_word=uri.getPathSegments().get(1);

                cursor=db.query("Words.Word.TABLE_NAME ",projection,"word=?",new String[]{words_word},null,null,sortOrder);

                break;

            default:

                break;

        }

        return cursor;

    }



    @Override

    public Uri insert(Uri uri, ContentValues values) {

        //添加数据

        SQLiteDatabase db=mDbHelper.getWritableDatabase();

        Uri uriReturn=null;

        switch (uriMatcher.match(uri)){

            case WORDS_DIR:

            case WORDS_ITEM:

                long i=db.insert("Words.Word.TABLE_NAME ",null,values);

                String id=values.getAsString("word");

                uriReturn=Uri.parse("content://"+AUTHORITY+"/Words.Word.TABLE_NAME /"+id);

            default:break;

        }

        return uriReturn;

    }



    @Override

    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        SQLiteDatabase db=mDbHelper.getWritableDatabase();

        int updateRows=0;

        switch (uriMatcher.match(uri)){

            case WORDS_DIR:

                updateRows=db.update("Words.Word.TABLE_NAME ",values,selection,selectionArgs);

                break;

            case WORDS_ITEM:

                String id=uri.getPathSegments().get(1);

                updateRows=db.update("Words.Word.TABLE_NAME ",values,"word=?",new String[]{id});

                break;

        }

        return updateRows;

    }



    @Override

    public int delete(Uri uri, String selection, String[] selectionArgs) {

        SQLiteDatabase db=mDbHelper.getWritableDatabase();

        int deleteRows=0;

        switch (uriMatcher.match(uri)){

            case WORDS_DIR:

                deleteRows=db.delete("Words.Word.TABLE_NAME ",selection,selectionArgs);

                break;

            case WORDS_ITEM:

                String id =uri.getPathSegments().get(1);

                deleteRows=db.delete("Words.Word.TABLE_NAME ","word=?",new String[]{id});

                break;

            default:break;

        }

        return deleteRows;

    }



    @Override

    public String getType(Uri uri) {

        switch (uriMatcher.match(uri)){

            case WORDS_DIR:

                return "vnd.android.cursor.dir/vnd.com.example.asus.danciben.provider.words";

            case WORDS_ITEM:

                return "vnd.android.cursor.item/vnd.com.example.asus.danciben.provider.words";

        }

        return null;

    }

}