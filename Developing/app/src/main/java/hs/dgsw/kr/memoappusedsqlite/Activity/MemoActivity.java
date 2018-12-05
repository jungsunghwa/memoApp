package hs.dgsw.kr.memoappusedsqlite.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import hs.dgsw.kr.memoappusedsqlite.model.Image;
import hs.dgsw.kr.memoappusedsqlite.model.Memo;
import hs.dgsw.kr.memoappusedsqlite.R;
import hs.dgsw.kr.memoappusedsqlite.adpater.ImageGirdViewAdapter;
import hs.dgsw.kr.memoappusedsqlite.database.DatabaseHelper;

public class MemoActivity extends AppCompatActivity {

  private DatabaseHelper databaseHelper; // 데이터 베이스 작업을 해주는 변수
  private ImageGirdViewAdapter imageGirdViewAdapter; // ImageGridViewAdapter 변수

  int memoIdx;
  private Calendar calendar;
  private Memo memo = new Memo();
  private String reservationDate;
  private ArrayList<Image> imageList = new ArrayList<>();

  EditText memoEditText;
  BottomNavigationView bottomNavigationView;
  Toolbar mToolbar;
  GridView imageGirdView;

  boolean isContentChanged = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_memo);

    // 각 요소들을 binding 해준다
    memoEditText = findViewById(R.id.memo_et);
    bottomNavigationView = findViewById(R.id.bottomNavigationView_main_menu);
    mToolbar = findViewById(R.id.main_toolbar);
    imageGirdView = findViewById(R.id.image_grid_view);

    // memoIdx를 가져옴
    memoIdx = getIntent().getIntExtra("memoIdx", -1);

    // 작업에사용될 database 를 가져옴
    databaseHelper = new DatabaseHelper(this);

    if (memoIdx != -1) {
      // database 에서 선택된 memo 를 가져온다
      memo = databaseHelper.getMemoByIdx(memoIdx);

      // 내용을 설정한다
      memoEditText.setText(memo.getContent());

      // imageList 를 설정해준다
      imageList = memo.getImageList();

      if (!imageList.isEmpty()) {
        imageGirdView.setVisibility(View.VISIBLE);
      }
    } else {
      memo.setIdx(databaseHelper.getLastMemoIdx() + 1);

      imageList = new ArrayList<>();

      imageGirdView.setVisibility(View.GONE);
    }

    // 이미지 리스트뷰의 어뎁터를 만들어준다
    imageGirdViewAdapter = new ImageGirdViewAdapter(imageList);
    imageGirdView.setAdapter(imageGirdViewAdapter);

    // 툴바를 설정하고 툴바의 취소버튼을 활성화한다
    setSupportActionBar(mToolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setDisplayShowTitleEnabled(false);

    // 달력을 가져옴
    calendar = Calendar.getInstance();

    // 현재의 시간으로 날짜를 초기화한다
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
    reservationDate = simpleDate.format(new Date());

    // 하단바의 작업을 나타낸다
    bottomNavigationView.setOnNavigationItemSelectedListener(
        item -> {

          // 어떤 메뉴 아이템이 터치되었는지 확인합니다.
          switch (item.getItemId()) {
            case R.id.action_delete:
              AlertDialog.Builder builder = new AlertDialog.Builder(MemoActivity.this);
              builder
                  .setMessage("정말 삭제 하시겠습니까?")
                  .setPositiveButton(
                      "Yes",
                      (dialog, swish) -> {
                        databaseHelper.deleteMemoByIdx(memo.getIdx());
                        finish();
                      })
                  .setNegativeButton("No", (dialog, swish) -> {})
                  .show();

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
              if (memoEditText.getText().toString().isEmpty() && imageList.isEmpty()) {

                AlertDialog.Builder builder1 = new AlertDialog.Builder(MemoActivity.this);

                builder1
                    .setMessage("내용을 입력해주세요.")
                    .setPositiveButton("Yes", (dialog, swish) -> {})
                    .show();

              } else {

                memo.setTitle(memoEditText.getText().toString());
                memo.setContent(memoEditText.getText().toString());
                memo.setDate(reservationDate);
                memo.setImageList(imageList);

                databaseHelper.insertMemo(memo);
                databaseHelper.insertImageList(imageList, memo.getIdx());

                finish();
              }

              return true;
          }
          return false;
        });

    memoEditText.addTextChangedListener(
        new TextWatcher() {

          public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!s.equals("")) {
              isContentChanged = true;
            }
          }

          public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

          public void afterTextChanged(Editable s) {}
        });
  }

  // 오른쪽 상단 +버튼을 생성하는 코드
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.mymenu, menu);
    return super.onCreateOptionsMenu(menu);
  }

  // 오른쪽상단 +버튼을 클릭할떄의 코드입니다
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();

    if (id == R.id.mybutton) {
      final Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);

      photoPickerIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
      photoPickerIntent.setType("image/*");

      startActivityForResult(photoPickerIntent, 100);
    } else {
      if (isContentChanged) {

        AlertDialog.Builder builder = new AlertDialog.Builder(MemoActivity.this);

        builder
            .setMessage("변경사항이 있습니다. 정말 취소하시겠습니까?")
            .setPositiveButton("Yes", (dialog, which) -> finish())
            .setNegativeButton("No", (dialog, which) -> {})
            .show();

      }else {
        finish();
      }
    }
    return super.onOptionsItemSelected(item);
  }

  // 앨범에서 사진을 선택하여 가지고 오면 아래의 메서드로 들어옵니다.
  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    if (resultCode == RESULT_OK && requestCode == 100) {
      imageGirdView.setVisibility(View.VISIBLE);
      try {
        // 선택한 이미지들의 uri 을 imageList 에 넣어 줍니다.
        ClipData clipData = data.getClipData();

        if (clipData != null) {
          for (int i = 0; i < clipData.getItemCount(); i++) {
            ClipData.Item item = clipData.getItemAt(i);
            imageList.add(new Image(0, item.getUri()));
          }
        }

        imageGirdViewAdapter.notifyDataSetChanged();

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
                + String.format(Locale.KOREA, "%02d", month + 1)
                + "-"
                + String.format(Locale.KOREA, "%02d", day)
                + " ";
      };
}
