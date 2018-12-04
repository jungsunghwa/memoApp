package hs.dgsw.kr.memoappusedsqlite.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import hs.dgsw.kr.memoappusedsqlite.Memo;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "memo_app.db";
    //DBManager 에서는 table 을 생성하는 코드를 모아놓았습니다.
    private DBManager dbManager = new DBManager();


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    //테이블을 생성하는 메서드입니다.
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("PRAGMA foreign_keys = ON;");
        db.execSQL(dbManager.getCreateMemoTable());
        db.execSQL(dbManager.getCreateImagesTable());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + "memo");
        db.execSQL("DROP TABLE IF EXISTS " + "image");
        onCreate(db);
    }

    //메모리스트를 데이버베이스에서 가져옵니다.
    public ArrayList<Memo> getMemoList() {
        final SQLiteDatabase db = this.getWritableDatabase();
        final Cursor res = db.rawQuery("SELECT * FROM memo", null);
        final ArrayList<Memo> memoArrayList = new ArrayList<>();

        while (res.moveToNext()) {

            Memo memo = new Memo();

            memo.setIdx(res.getInt(res.getColumnIndex("idx")));
            memo.setContent(res.getString(res.getColumnIndex("content")));
            memo.setDate(res.getString(res.getColumnIndex("date")));
            memo.setTitle(res.getString(res.getColumnIndex("Title")));

            memo.setImageList(getImageListByMemoIdx(memo.getIdx()));

            memoArrayList.add(memo);
        }

        res.close();

        return memoArrayList;
    }

    //메모에 삽입된 사진들을 가지고 옵니다
    private ArrayList<String> getImageListByMemoIdx(int memoIdx) {
        final SQLiteDatabase db = this.getWritableDatabase();
        final Cursor res = db.rawQuery("SELECT * FROM image WHERE memo_idx = " + memoIdx, null);

        final ArrayList<String> imageList = new ArrayList<>();

        while (res.moveToNext()) {
            imageList.add(res.getString(res.getColumnIndex("image")));
        }

        res.close();

        return imageList;
    }
}
