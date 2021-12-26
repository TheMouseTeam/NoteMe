package com.barmpas.noteme;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.barmpas.noteme.NotesData.NoteContract;

import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 * MainActivity is the activity where the user can write down a note and store it.
 * @author Konstantinos Barmpas.
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    /**
     * EditText view to write the note
     */
    private EditText editor;
    /**
     * The button to add note to database
     */
    private at.markushi.ui.CircleButton add_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(getResources().getString(R.string.write_a_note));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Set up the UI
        add_btn = (at.markushi.ui.CircleButton) findViewById(R.id.btn_add);
        editor = (EditText) findViewById(R.id.note_editor);

        //Store to the database
        add_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (!editor.getText().toString().equals("") && editor.getText().toString()!=null) {
                    ContentValues values = new ContentValues();
                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                    String formattedDate = df.format(c.getTime());
                    values.put(NoteContract.UserEntry.COLUMN_DATE, formattedDate);
                    values.put(NoteContract.UserEntry.COLUMN_NOTE, editor.getText().toString());
                    Uri newUri = getContentResolver().insert(NoteContract.UserEntry.CONTENT_URI, values);
                    editor.setText("");
                    Toast.makeText(MainActivity.this,getResources().getString(R.string.success_note),Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(MainActivity.this,getResources().getString(R.string.write_first),Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            moveTaskToBack(true);
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_today) {
            // Handle the camera action
        } else if (id == R.id.nav_previous_days) {
            Intent intent = new Intent(MainActivity.this, NotesActivity.class);
            startActivity(intent);
        }else if (id == R.id.nav_info) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.themouseteam.com")));
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
