package com.example.gonza.to_doapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import static com.example.gonza.to_doapp.MainActivity.ITEM_POSITION;
import static com.example.gonza.to_doapp.MainActivity.ITEM_TEXT;

public class EditItemActivity extends AppCompatActivity {

    EditText editItemText;
    int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        editItemText = (EditText) findViewById(R.id.item_text);
        editItemText.setText(getIntent().getStringExtra(ITEM_TEXT));
        //updating position from intent extra
        position = getIntent().getIntExtra(ITEM_POSITION, 0);
        //updating titlebar
        getSupportActionBar().setTitle("Edit Item");
    }
    public void onSaveItem(View v){
        //prepares new intent for result
        Intent i = new Intent();

        i.putExtra(ITEM_TEXT, editItemText.getText().toString());
        i.putExtra(ITEM_POSITION, position);
        //setting intent as result of activity
        setResult(RESULT_OK, i);
        finish();
    }
}
