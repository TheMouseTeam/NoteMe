package com.barmpas.noteme;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.barmpas.noteme.NotesData.NoteContract;

/**
 * NotesActivity is the activity that displays all stored notes. The user can access a note or delete all the notes.
 * @author Konstantinos Barmpas.
 */
public class NotesActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    /**
     * Loader
     */
    private static final int URL_LOADER = 0;
    /**
     * Simple cursor adapter
     */
    private SimpleCursorAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notes_activity);
        setTitle(getResources().getString(R.string.notes));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //The displayed views
        String[] columns = {
                NoteContract.UserEntry.COLUMN_NOTE,
                NoteContract.UserEntry.COLUMN_DATE
        };

        int[] views = {
                R.id.note,
                R.id.date
        };

        //Setting the adapter
        mAdapter = new SimpleCursorAdapter(this, R.layout.note_card, null, columns, views, 0) {

            @Override
            public void bindView(View view, Context context, final Cursor cursor) {
                super.bindView(view, context, cursor);

                //Setting each individual view of the adapter.
                TextView date_txt = (TextView) view.findViewById(R.id.date);
                TextView note_txt = (TextView) view.findViewById(R.id.note);

                final String date = cursor.getString(cursor.getColumnIndex("date"));
                final String text = cursor.getString(cursor.getColumnIndex("usernote"));

                date_txt.setText(date);
                if (text.length()<=10){
                    note_txt.setText(text);
                }else{
                    note_txt.setText(text.substring(0,10)+"...");
                }

                //On click pass data to the Graph activity
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(NotesActivity.this,NoteActivity.class);
                        intent.putExtra("note",text);
                        intent.putExtra("date",date);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        };

        //Activating the cursor and the adapter
        ListView favListView = (ListView) findViewById(R.id.saved_recycler_view);
        favListView.setAdapter(mAdapter);
        getLoaderManager().initLoader(URL_LOADER, null, this);
    }

    //Menus to allow delete the SQlite database.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.notes_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_all:
                new AlertDialog.Builder(this)
                        .setTitle(getResources().getString(R.string.delete))
                        .setMessage(getResources().getString(R.string.delete_notes))
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                getContentResolver().delete(NoteContract.UserEntry.CONTENT_URI, null, null);
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();
                return true;
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    //Creating the Cursor
    @Override
    public Loader<Cursor> onCreateLoader(int loaderID, Bundle bundle) {
        switch (loaderID) {
            case URL_LOADER:
                String[] projection = {
                        NoteContract.UserEntry._ID,
                        NoteContract.UserEntry.COLUMN_DATE,
                        NoteContract.UserEntry.COLUMN_NOTE
                };
                return new CursorLoader(
                        this,
                        NoteContract.UserEntry.CONTENT_URI,
                        projection,
                        null,
                        null,
                        null
                );
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }
}
