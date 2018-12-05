package hs.dgsw.kr.memoappusedsqlite.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

import hs.dgsw.kr.memoappusedsqlite.model.Memo;
import hs.dgsw.kr.memoappusedsqlite.R;
import hs.dgsw.kr.memoappusedsqlite.adpater.MemoListViewAdapter;
import hs.dgsw.kr.memoappusedsqlite.database.DatabaseHelper;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper databaseHelper;

    ListView memoListView;
    MemoListViewAdapter memoListViewAdapter;
    FloatingActionButton fab;

    ArrayList<Memo> memoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //작업권한을 얻기위해서 해주는 작업이다
        checkDiskPermission();

        //데이터베이스의 작업을 해줄 클래스를 생성합니다.
        databaseHelper = new DatabaseHelper(this);

        //각 요소들을 binding 해준다
        fab = findViewById(R.id.fab);
        memoListView = findViewById(R.id.memo_list_view);

        //메모리스트를 받아온다
        memoList = databaseHelper.getMemoList();

        //어뎁터를 만들어준다
        memoListViewAdapter = new MemoListViewAdapter(memoList);

        //메모리스트뷰의 어뎁터를 설정해준가
        memoListView.setAdapter(memoListViewAdapter);

        //플로팅버튼의 작업을 설정해줍니다
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, MemoActivity.class);
            intent.putExtra("memoIdx",-1);
            startActivity(intent);
            });

    }

    @Override
    protected void onResume() {
        super.onResume();

        //메모리스트를 리로드해준다
        memoListViewAdapter.swapItems(databaseHelper.getMemoList());
    }

    private void checkDiskPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[] {
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    },
                    0);
        } else {
        }
    }
}
