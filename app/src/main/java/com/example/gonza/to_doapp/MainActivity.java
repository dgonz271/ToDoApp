package com.example.gonza.to_doapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //numeric code to identify edit activity
    public final static int EDIT_REQUEST_CODE = 20;
    //keys used to pass data between activities
    public final static String ITEM_TEXT = "itemText";
    public final static String ITEM_POSITION = "itemPosition";

    ArrayList<String> listItems;
    ArrayAdapter<String> listItemsAdapter;
    ListView lvItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        readItems();
        listItemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItems);
        lvItems = (ListView) findViewById(R.id.lv_items);
        lvItems.setAdapter(listItemsAdapter);

        //fake data
        //listItems.add("First Item");
        //listItems.add("Second Item");
        //listItems.add("Third Item");

        setupListViewListener();

    }

    public void onAddItem(View v) {
        EditText newItem = (EditText) findViewById(R.id.new_item_text);
        String itemText = newItem.getText().toString();
        listItemsAdapter.add(itemText);
        newItem.setText("");
        writeItems();
        Toast.makeText(getApplicationContext(), "Item added to list", Toast.LENGTH_SHORT).show();
    }

    private void setupListViewListener() {
        Log.i("MainActivity", "Setting up listener on list view");
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("MainActivity", "Item removed from list: " + position);
                listItems.remove(position);
                listItemsAdapter.notifyDataSetChanged();
                writeItems();
                return true;
            }
        });

        //setting up item listener for edits(regular click)
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //creating new activity
                Intent i = new Intent(MainActivity.this, EditItemActivity.class);
                //passing data to new activity
                i.putExtra(ITEM_TEXT, listItems.get(position));
                i.putExtra(ITEM_POSITION, position);
                //displaying new activity
                startActivityForResult(i, EDIT_REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == EDIT_REQUEST_CODE)
        {
            //extracting string from from other activity
            String updatedItem = data.getExtras().getString(ITEM_TEXT);
            //extracting original position of edited Item
            int position = data.getExtras().getInt(ITEM_POSITION);
            //updating the model with the edited text
            listItems.set(position, updatedItem );
            //notifying adapter that model updated
            listItemsAdapter.notifyDataSetChanged();
            //persist the changed model
            writeItems();
            Toast.makeText(this, "Successfully edited item", Toast.LENGTH_SHORT).show();


        }
    }

    private File getDataFile() {
        return new File(getFilesDir(), "todo.txt");
    }

    private void readItems() {
        try {
            listItems = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e("MainActivity", "Error reading file", e);
            listItems = new ArrayList<>();
        }
    }
    private void writeItems(){
        try {
            FileUtils.writeLines(getDataFile(), listItems);
        } catch (IOException e) {
            Log.e("MainActivity", "Error writing file", e);
        }
    }
}
