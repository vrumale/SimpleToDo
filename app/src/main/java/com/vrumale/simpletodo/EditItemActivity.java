package com.vrumale.simpletodo;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


public class EditItemActivity extends ActionBarActivity {
    private int pos = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String editItemText = new String(getIntent().getStringExtra("item"));
        pos = getIntent().getIntExtra("pos", -1);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        EditText editItem = (EditText)findViewById(R.id.etEditItem);
        if(editItem == null) {
            //ToDo Need to catch the error here
            System.out.println("Cant find editiTEM");
        }
        editItem.setText(editItemText,TextView.BufferType.EDITABLE);
        editItem.setSelection(editItem.getText().length());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void onEditItem(View v) {
        //Intent i = new Intent(this,EditItemActivity.class);
        System.out.println("Doe submit get called");
        EditText etItem = (EditText) findViewById(R.id.etEditItem);
        // Prepare data intent
        Intent data = new Intent();
        data.putExtra("item", etItem.getText().toString());
        data.putExtra("pos",pos);
        // Activity finished ok, return the data
        setResult(RESULT_OK, data); // set result code and bundle data for response
        this.finish(); // closes the activity, pass data to parent

    }
}
