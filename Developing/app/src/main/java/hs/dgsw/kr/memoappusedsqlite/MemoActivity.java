package hs.dgsw.kr.memoappusedsqlite;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MemoActivity extends AppCompatActivity {
    private Calendar calendar;
    private String reservationDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);

        Toolbar mToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        calendar = Calendar.getInstance();

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDate =  new SimpleDateFormat("yyyy-MM-dd");
        reservationDate = simpleDate.format(new Date());

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView_main_menu);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                item -> {

                    // 어떤 메뉴 아이템이 터치되었는지 확인합니다.
                    switch (item.getItemId()) {

                        case R.id.action_delete:

                            finish();
                            return true;

                        case R.id.action_date_picker:

                            // DatePicker를 설정합니다.
                            DatePickerDialog datePickerDialog =
                                    new DatePickerDialog(
                                            MemoActivity.this,
                                            onDateSetListener,
                                            calendar.get(Calendar.YEAR),
                                            calendar.get(Calendar.MONTH),
                                            calendar.get(Calendar.DAY_OF_MONTH));

                            datePickerDialog.show();

                            return true;

                        case R.id.action_add:

                            Toast.makeText(this,reservationDate, Toast.LENGTH_SHORT).show();

                            return true;
                    }
                    return false;
                });


    }

    // 오른쪽 상단 +버튼을 생성하는 코드
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mymenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //오른쪽상단 +버튼을 클릭할떄의 코드입니다
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.mybutton) {
            final Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);

            photoPickerIntent.setType("image/*");

            startActivityForResult(photoPickerIntent, 100);
        }else{
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    //앨범에서 사진을 선택하여 가지고 오면 아래의 메서드로 들어옵니다.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && requestCode == 100){
            try {
                //선택한 사진의 uri를 가지고옵니다.
                Uri uri = data.getData();

                Log.e("uri", uri.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @NonNull
    // 날짜를 선택하여 확인을 누르면 작동하는 리스너입니다. DATE를 String으로 바꾸워줌니다.
    private final DatePickerDialog.OnDateSetListener onDateSetListener =
            (datePicker, year, month, day) -> {
                reservationDate =
                        String.valueOf(year)
                                + "-"
                                + String.format(Locale.KOREA, "%02d", month+1)
                                + "-"
                                + String.format(Locale.KOREA, "%02d", day)
                                + " ";
            };
}
