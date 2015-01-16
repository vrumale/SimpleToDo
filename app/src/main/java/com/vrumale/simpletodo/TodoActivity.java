package com.vrumale.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class TodoActivity extends ActionBarActivity {
    ListView lvItems;
    UsersAdapter itemsAdapter;
    ArrayList<TodoItem> db_items;
    TodoItemDatabase db;
    private final int REQUEST_CODE = 20;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Create our sqlite database object
        db = new TodoItemDatabase(this);
        setContentView(R.layout.activity_todo);
        lvItems = (ListView) findViewById(R.id.lvItems);
        //Read all the items from the database
        db_items = (ArrayList<TodoItem>) db.getAllTodoItems();
        itemsAdapter = new UsersAdapter(this, db_items);
        lvItems.setAdapter(itemsAdapter);
        setupListViewListener();
    }

    private void setupListViewListener() {
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapter, View item, int pos, long id) {
               //remove item from database
                db.deleteTodoItem(db_items.get(pos));
                db_items.remove(pos);
                itemsAdapter.notifyDataSetChanged();
                return true;
                }
            }
        );
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View item, int pos, long l) {
                Intent intent = new Intent(TodoActivity.this,EditItemActivity.class);
                intent.putExtra("item", db_items.get(pos).toString());
                intent.putExtra("pos",pos);
                startActivityForResult(intent,REQUEST_CODE);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // REQUEST_CODE is defined above
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            // Extract name value from result extras
            String name = data.getExtras().getString("item");
            int pos = data.getExtras().getInt("pos");
            TodoItem oldItem = db_items.get(pos);
            oldItem.setBody(name);
            //Update the database with newBody for the item
            db.updateTodoItem(oldItem);
            itemsAdapter.notifyDataSetChanged();

            // Toast the name to display temporarily on screen
            Toast.makeText(this, "Updated!", Toast.LENGTH_SHORT).show();
        }
    }

    public void onAddItem(View v) {
        EditText etNewItem = (EditText)findViewById(R.id.etNewItem);
        String newBody = etNewItem.getText().toString();
        TodoItem newItem = new TodoItem(newBody,1);
        newItem.setId(itemsAdapter.getCount()+1);
        itemsAdapter.add(newItem);
        etNewItem.setText("");
        // Write item to database
        db.addTodoItem(newItem);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_todo, menu);
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
}
