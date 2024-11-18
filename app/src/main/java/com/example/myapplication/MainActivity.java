package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.myapplication.database.BookDatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private BookDatabaseHelper dbHelper;
    private EditText editTextTitle, editTextAuthor;
    private ListView listViewReviews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new BookDatabaseHelper(this);

        editTextTitle = findViewById(R.id.editTextTitle);
        editTextAuthor = findViewById(R.id.editTextAuthor);
        Button buttonSave = findViewById(R.id.buttonSave);

        listViewReviews = findViewById(R.id.listViewReviews);
        Button buttonLoad = findViewById(R.id.buttonLoad);

        buttonLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor = dbHelper.getAllBookReviews();
                if (cursor.getCount() == 0) {
                    Toast.makeText(MainActivity.this, "저장된 감상평이 없습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 감상평 목록을 저장할 리스트 생성
                List<String> reviewsList = new ArrayList<>();
                while (cursor.moveToNext()) {
                    String review = "제목: " + cursor.getString(cursor.getColumnIndex("title")) +
                            "\n저자: " + cursor.getString(cursor.getColumnIndex("author")) +
                            "\n별점: " + cursor.getInt(cursor.getColumnIndex("rating")) + "점";
                    reviewsList.add(review);
                }

                // 어댑터 설정
                ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this,
                        android.R.layout.simple_list_item_1, reviewsList);
                listViewReviews.setAdapter(adapter);

                cursor.close();
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = editTextTitle.getText().toString().trim();
                String author = editTextAuthor.getText().toString().trim();

                // 다른 필드 데이터도 입력 받을 수 있습니다.

                if (!title.isEmpty() && !author.isEmpty()) {
                    // 데이터베이스에 데이터 삽입
                    long id = dbHelper.insertBookReview(
                            title,       // 책 제목
                            author,      // 작가 이름
                            "출판사 예시",  // 출판사 (예시 값)
                            "옮긴이 예시",  // 옮긴이 (예시 값)
                            5,           // 별점 (예시 값)
                            "장르 예시",   // 장르 (예시 값)
                            "2024-11-18", // 감상평 작성 날짜 (예시 값)
                            "리뷰 예시",   // 감상평 (예시 값)
                            "태그 예시"    // 태그 (예시 값)
                    );

                    if (id != -1) {
                        Toast.makeText(MainActivity.this, "저장 성공", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "저장 실패", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "모든 필드를 입력해주세요", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}