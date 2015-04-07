package com.projects.aliciamarie.folio;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.projects.aliciamarie.folio.data.DataContract;
import com.projects.aliciamarie.folio.data.DatabaseUtilities;
import com.projects.aliciamarie.folio.data.Datapiece;

import java.util.ArrayList;

/**
 * Created by Alicia Marie on 3/24/2015.
 */
public class ViewContentActivity extends ActionBarActivity {


    private static final String LOG_TAG = ViewContentActivity.class.getSimpleName();
    protected static final String DATAPIECES = "datapieces";
    protected ArrayList<Datapiece> mDatapieces;
    protected boolean showNewest = true;
    protected ListContentFragment listFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState == null) {
            mDatapieces = DatabaseUtilities.getDatapiecesByTime(this, "DESC", null, null);
        }
        else{
            mDatapieces = savedInstanceState.getParcelableArrayList(DATAPIECES);
        }
        if(mDatapieces == null){
            mDatapieces = new ArrayList();
        }

        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(DATAPIECES, mDatapieces);
        listFragment = createListContentFragment(bundle);

        setContentView(R.layout.activity_viewcontent);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.view_content_container, new ViewOptionsFragment())
                    .add(R.id.view_content_container, listFragment)
                    .commit();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(DATAPIECES, mDatapieces);
    }

    private ListContentFragment createListContentFragment(Bundle bundle) {
        ListContentFragment listFragment = new ListContentFragment();
        listFragment.setArguments(bundle);
        return listFragment;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }
        else if (id == R.id.action_capture){
            addContent();
        }
        else if (id == R.id.action_view_content){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void search(View view){
        EditText searchBox = (EditText) findViewById(R.id.viewoptions_edittext_search);
        if(searchBox == null){
            Log.v(LOG_TAG, "Search box not found");
        }
        else {
            String[] searchTerm = { searchBox.getText().toString() };
            mDatapieces = DatabaseUtilities.getDatapiecesByTag(this, "ASC", DataContract.SpecimenDatapiecePairing.COLUMN_SPECIMEN_ID + " LIKE ? ", searchTerm);
            listFragment.updateList(mDatapieces);
        }
    }

    public void orderBySpecimen(View view){
        Log.v(LOG_TAG, "Order by specimen called!");
        mDatapieces = DatabaseUtilities.getDatapiecesByTime(this,"DESC", null, null);
    }

    public void orderByTime(View view){
        if(showNewest){
            showNewest = false;
            mDatapieces = DatabaseUtilities.getDatapiecesByTime(this,"ASC", null, null);
        }
        else{
            showNewest = true;
            mDatapieces = DatabaseUtilities.getDatapiecesByTime(this,"DESC", null, null);
        }
        listFragment.updateList(mDatapieces);
    }

    private void addContent() {  startActivity(new Intent(this, MainActivity.class)); }

}
