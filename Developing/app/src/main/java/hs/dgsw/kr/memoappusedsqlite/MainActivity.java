package hs.dgsw.kr.memoappusedsqlite;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import hs.dgsw.kr.memoappusedsqlite.adpater.MenoListViewAdapter;
import hs.dgsw.kr.memoappusedsqlite.database.DatabaseHelper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //데이터베이스의 작업을 해줄 클래스를 생성합니다.
        DatabaseHelper databaseHelper = new DatabaseHelper(this);

        //플로팅버튼을 설정해줍니다
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, MemoActivity.class);
            startActivity(intent);
            });

        ListView memoListView = findViewById(R.id.memo_list_view);
        ListAdapter adapter = new MenoListViewAdapter(databaseHelper.getMemoList());
        memoListView.setAdapter(adapter);


    }


}
