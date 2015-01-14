package com.vrumale.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class TodoActivity extends ActionBarActivity {
    ListView lvItems;
    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    List<TodoItem> db_items;
    TodoItemDatabase db;
    private final int REQUEST_CODE = 20;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Create our sqlite database object
        db = new TodoItemDatabase(this);
        setContentView(R.layout.activity_todo);
        lvItems = (ListView) findViewById(R.id.lvItems);
        items = new ArrayList<String>();
        //db_items = new TodoItem();
        readAllItems();
        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,items);
        lvItems.setAdapter(itemsAdapter);
        setupListViewListener();
    }

    private void setupListViewListener() {
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapter, View item, int pos, long id) {
                //remove item from database
                removeItem(items.get(pos));
                items.remove(pos);
                itemsAdapter.notifyDataSetChanged();
                return true;
                }
            }
        );
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View item, int pos, long l) {
                Intent intent = new Intent(TodoActivity.this,EditItemActivity.class);
                intent.putExtra("item",items.get(pos).toString());
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
            updateItems(items.get(pos), name);
            itemsAdapter.remove(itemsAdapter.getItem(pos));
            itemsAdapter.insert(name, pos);
            itemsAdapter.notifyDataSetChanged();

            // Toast the name to display temporarily on screen
            Toast.makeText(this, "Updated!", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateItems(String oldItem, String newItem) {
        for(TodoItem t : db_items) {
            System.out.println("UPDATING "+ t.getBody());
            if(t.getBody().equals(oldItem)){
                t.setBody(newItem);
                db.updateTodoItem(t);
                return;
            }
        }
    }

    private void readAllItems() {
        // Querying all todo items
        db_items = db.getAllTodoItems();
        for(TodoItem t : db_items) {
            String itemString = new String(t.getBody());
            items.add(itemString);

        }
    }
    public void onAddItem(View v) {
        EditText etNewItem = (EditText)findViewById(R.id.etNewItem);
        String newItem = etNewItem.getText().toString();
        itemsAdapter.add(newItem);
        etNewItem.setText("");
        TodoItem item = new TodoItem(newItem,1);
        writeItems(item);
    }
    private void writeItems(TodoItem item) {
        db.addTodoItem(item);
    }
    private void removeItem(String item) {
        for(TodoItem t : db_items) {

            if(t.getBody().equals(item)){
                System.out.println("deleting "+ t.getBody());
                db.deleteTodoItem(t);
                return;
            }
        }
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
