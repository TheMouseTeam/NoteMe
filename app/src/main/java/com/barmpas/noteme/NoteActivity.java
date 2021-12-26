package com.barmpas.noteme;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import com.barmpas.noteme.NotesData.NoteContract;

import static com.barmpas.noteme.NotesData.NoteProvider.deleteNote;

/**
 * NoteActivity is the activity that displays a stored note. The user can edit or delete the note.
 * @author Konstantinos Barmpas.
 */
public class NoteActivity extends AppCompatActivity {

    /**
     * EditText view to edit/display the note
     */
    private EditText displayTxt;
    /**
     * String to get the date of the note
     */
    private String date;
    /**
     * String to get the date of the original
     */
    private String original_note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_activity);
        setTitle(getResources().getString(R.string.notes));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        displayTxt = (EditText) findViewById(R.id.text_display);

        Intent intent = getIntent();
        original_note = intent.getStringExtra("note");
        date=intent.getStringExtra("date");
        displayTxt.setText(original_note);
    }

    //Menus to allow delete the SQlite database.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.delete_note_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete:
                new AlertDialog.Builder(this)
                        .setTitle(getResources().getString(R.string.delete))
                        .setMessage(getResources().getString(R.string.delete_note))
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                deleteNote(NoteContract.UserEntry.CONTENT_URI,"usernote" + " = '" + original_note + "'"+ " AND "+ "date"+" = '" + date + "'", null);
                                Intent intent = new Intent(NoteActivity.this, NotesActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();
                return true;
            case android.R.id.home:
                ContentValues values = new ContentValues();
                values.put(NoteContract.UserEntry.COLUMN_NOTE, displayTxt.getText().toString());
                values.put(NoteContract.UserEntry.COLUMN_DATE, date);
                getContentResolver().update(NoteContract.UserEntry.CONTENT_URI, values, "usernote" + " = '" + original_note + "'"+ " AND "+ "date"+" = '" + date + "'", null);
                Intent intent = new Intent(NoteActivity.this, NotesActivity.class);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
}
