package com.app.assignmentr2.activity;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.app.assignmentr2.R;
import com.app.assignmentr2.adaptor.WordRecyclerAdapter;
import com.app.assignmentr2.controller.ControllerCallback;
import com.app.assignmentr2.controller.ControllerManager;
import com.app.assignmentr2.controller.NetworkController;
import com.app.assignmentr2.database.SqLiteDataFetcher;
import com.app.assignmentr2.database.TableConstants;
import com.app.assignmentr2.model.NetworkResponse;
import com.app.assignmentr2.model.Words;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Govind on 28-02-2016.
 */
public class ActivityWordAndMeaning extends AppCompatActivity {

    private static final String TAG = "ActivityWordAndMeaning";
    private static final String INSERTION_ERROR = "failure";
    private static final String INSERTION_SUCCESS = "success";

    private RecyclerView mRecyclerView;
    private ProgressDialog progressBar;
    private WordRecyclerAdapter adapter;
    SqLiteDataFetcher sqLiteDataFetcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_and_meaning);
        progressBar = new ProgressDialog(this);

        progressBar.setMessage("Downloading ....");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setIndeterminate(true);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initialize recycler view
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        progressBar.show();
        sqLiteDataFetcher = new SqLiteDataFetcher(getApplicationContext());

        if (sqLiteDataFetcher.isTableEmpty()) {
            networkCall();
        } else {
            setAdapter();
        }
    }

    private void networkCall() {
        final ControllerManager controllerManager = ControllerManager.getInstance();
        final NetworkController networkController = controllerManager.getNetworkController();

        ControllerCallback controllerCallback = new ControllerCallback(ActivityWordAndMeaning.this) {
            @Override
            protected void onSuccessResponse(int requestId, int status, Object data) {
                Log.d(TAG, "In OnSuccess Network Raw data = " + data.toString());
                NetworkResponse arr = (NetworkResponse) data;
                List<Words> wordsList = arr.getWordsList();
                List<Words> wordsListWithOutImage = new ArrayList<>();

                for (int i = 0; i < wordsList.size(); i++) {
                    if (1.0f == Math.signum(Float.parseFloat(wordsList.get(i).getRatio()))) {
                        String imageUri = "http://appsculture.com/vocab/images/" + wordsList.get(i).getId() + ".png";
                        wordsList.get(i).setUri(imageUri);
                        wordsListWithOutImage.add(wordsList.get(i));
                    }
                }

                new InsertingIntoDBTask(wordsListWithOutImage).execute();
                String version = arr.getVersion();
                Log.d(TAG, "VERSION number = " + version.toString());
            }

            @Override
            protected void onErrorResponse(int requestId, int status, Object data) {
                progressBar.dismiss();
                NetworkResponse arr = (NetworkResponse) data;
                if (arr != null) {
                    Log.d(TAG, "In OnError network response code = " + arr.getmErrorCode()
                            + " message = " + arr.getmErrorDescription());
                }
            }
        };
        networkController.fetchListOfWords(controllerCallback);
    }


    class InsertingIntoDBTask extends AsyncTask<Void, Void, String> {
        private List<Words> wordsList = null;

        public InsertingIntoDBTask(List<Words> wordsList) {
            this.wordsList = wordsList;
        }

        @Override
        protected String doInBackground(Void... params) {

            for (int i = 0; i < wordsList.size(); i++) {
                try {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(TableConstants.WordsColumn.WID, wordsList.get(i).getId());
                    contentValues.put(TableConstants.WordsColumn.WORD, wordsList.get(i).getWord());
                    contentValues.put(TableConstants.WordsColumn.VARIANT, wordsList.get(i).getVariant());
                    contentValues.put(TableConstants.WordsColumn.MEANING, wordsList.get(i).getMeaning());
                    contentValues.put(TableConstants.WordsColumn.ASPECT_RATIO, wordsList.get(i).getRatio());
                    contentValues.put(TableConstants.WordsColumn.IMAGE_URL, wordsList.get(i).getUri());

                    sqLiteDataFetcher.insertRecord(TableConstants.TABLE_WORD, contentValues);
                } catch (Exception e) {
                    return INSERTION_ERROR;
                }
            }
            return INSERTION_SUCCESS;
        }

        @Override
        protected void onPostExecute(String msg) {
            super.onPostExecute(msg);

            if (msg.equalsIgnoreCase(INSERTION_SUCCESS)) {
                setAdapter();
            } else if (msg.equalsIgnoreCase(INSERTION_ERROR)) {
                progressBar.dismiss();
                Log.e(TAG, msg);
            }
        }
    }

    private void setAdapter() {
        Cursor cursor = sqLiteDataFetcher.queryDatabase(TableConstants.TABLE_WORD, null, null, null, null);
        List<Words> wordsList = new ArrayList<>();
        Words words;
        if (cursor.moveToFirst()) {
            do {
                words = new Words();
                words.setWord(cursor.getString(cursor.getColumnIndex(TableConstants.WordsColumn.WORD)));
                words.setMeaning(cursor.getString(cursor.getColumnIndex(TableConstants.WordsColumn.MEANING)));
                words.setUri(cursor.getString(cursor.getColumnIndex(TableConstants.WordsColumn.IMAGE_URL)));
                wordsList.add(words);
                // do what ever you want here
            } while (cursor.moveToNext());
        }
        cursor.close();

        progressBar.dismiss();
        adapter = new WordRecyclerAdapter(ActivityWordAndMeaning.this, wordsList);
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_word_and_meaning, menu);
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
