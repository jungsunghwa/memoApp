package hs.dgsw.kr.memoappusedsqlite.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.net.Uri;

import java.util.ArrayList;

import hs.dgsw.kr.memoappusedsqlite.model.Image;
import hs.dgsw.kr.memoappusedsqlite.model.Memo;

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
            memo.setTitle(memo.getContent());

            memo.setImageList(getImageListByMemoIdx(memo.getIdx()));

            memoArrayList.add(memo);
        }

        res.close();

        return memoArrayList;
    }

    public Memo getMemoByIdx(int idx) {
        final SQLiteDatabase db = this.getWritableDatabase();
        final Cursor res = db.rawQuery("SELECT * FROM memo WHERE idx = " + idx, null);
        Memo memo = new Memo();

        while (res.moveToNext()) {
            memo.setIdx(res.getInt(res.getColumnIndex("idx")));
            memo.setContent(res.getString(res.getColumnIndex("content")));
            memo.setDate(res.getString(res.getColumnIndex("date")));
            memo.setTitle(res.getString(res.getColumnIndex("content")));
            memo.setImageList(getImageListByMemoIdx(memo.getIdx()));
        }

        res.close();

        return memo;
    }
    public int getLastMemoIdx() {
        final SQLiteDatabase db = this.getWritableDatabase();
        final Cursor res = db.rawQuery("SELECT idx FROM memo ORDER BY idx DESC LIMIT 1", null);
        Memo memo = new Memo();

        while (res.moveToNext()) {
            memo.setIdx(res.getInt(res.getColumnIndex("idx")));
        }

        res.close();

        return memo.getIdx();
    }

    //메모에 삽입된 사진들을 가지고 옵니다
    private ArrayList<Image> getImageListByMemoIdx(int memoIdx) {
        final SQLiteDatabase db = this.getWritableDatabase();
        final Cursor res = db.rawQuery("SELECT * FROM image WHERE memo_idx = " + memoIdx, null);

        final ArrayList<Image> imageList = new ArrayList<>();

        while (res.moveToNext()) {
            imageList.add(
                    new Image(res.getInt(res.getColumnIndex("idx"))
                    ,Uri.parse(res.getString(res.getColumnIndex("image"))))
            );
        }

        res.close();

        return imageList;
    }

    public void insertMemo(Memo memo) {
        final SQLiteDatabase db = this.getWritableDatabase();

        String sql = "INSERT OR REPLACE INTO memo VALUES(?,?,?)";

        final SQLiteStatement insertStmt = db.compileStatement(sql);

        insertStmt.clearBindings();

        insertStmt.bindLong(1, memo.getIdx());
        insertStmt.bindString(2, memo.getContent());
        insertStmt.bindString(3, memo.getDate());

        insertStmt.executeInsert();
    }

    public void insertImageList(ArrayList<Image> imageList, int memoIdx) {

        if (imageList == null) return;

        final SQLiteDatabase db = this.getWritableDatabase();

        String sql = "INSERT OR REPLACE INTO image VALUES(?,?,?)";

        final SQLiteStatement insertStmt = db.compileStatement(sql);

        for (Image image : imageList){
            insertStmt.clearBindings();

            insertStmt.bindLong(1, image.getIdx());
            insertStmt.bindString(2, image.getUri().toString());
            insertStmt.bindLong(3,memoIdx);

            insertStmt.executeInsert();
        }

    }

    public void deleteMemoByIdx(int idx) {
        final SQLiteDatabase db = this.getWritableDatabase();

        String sql = "DELETE FROM memo WHERE idx='";
        sql += idx;
        sql += "'";

        db.execSQL(sql);

        deleteImageByMemoIdx(idx);
    }

    private void deleteImageByMemoIdx(int memoIdx){
        final SQLiteDatabase db = this.getWritableDatabase();

        String sql = "DELETE FROM image WHERE memo_idx='";
        sql += memoIdx;
        sql += "'";

        db.execSQL(sql);
    }
}
