package com.example.contentprovider_ws;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;

import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    final  String[] columns=new String[]{
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER,
            ContactsContract.CommonDataKinds.Phone._ID

    };
    ListView list;
    SearchView search;
    Cursor cursor;
    SimpleCursorAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list=findViewById(R.id.listView);
        search=findViewById(R.id.search);
    /** ====  check permission   ====  */
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_CONTACTS},101);
        }else {
                read();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 101 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            read();
    }

    /** ====  read contact   ====  */
    public void read(){
        String[] contact = new String[]   {
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER,
        };
        cursor= getContentResolver().query( ContactsContract.CommonDataKinds.Phone.CONTENT_URI,columns,null,null,ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
        int[] in=new int[]{R.id.text1,R.id.text2};
        adapter= new SimpleCursorAdapter(this,R.layout.list_layout,cursor,contact,in,0);
        list.setAdapter(adapter);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                final String check="%"+newText+"%";
                cursor = getContentResolver().query( ContactsContract.CommonDataKinds.Phone.CONTENT_URI,columns,
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " LIKE ? ",
                        new String[] {check},
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                String buffer = null;
                    while (cursor.moveToNext()) {
                        buffer = cursor.getString(0);
                    }
                adapter.changeCursor(cursor);
                return true;
            }
        });

    }
    /** ====  dialog box   ====  */
}