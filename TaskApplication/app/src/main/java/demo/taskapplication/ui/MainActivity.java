package demo.taskapplication.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import demo.taskapplication.R;


/**
 * The type Main activity.
 */
public class MainActivity extends AppCompatActivity {

    /**
     * The M word count recycler view.
     */
    @InjectView(R.id.wordCountRecyclerView)
    RecyclerView mWordCountRecyclerView;
    /**
     * The M upload button.
     */
    @InjectView(R.id.uploadButton)
    Button mUploadButton;
    private WordCountAdapter mWordCountAdapter;
    /**
     * The M word hash map.
     */
    Map<String, Integer> mWordHashMap = new HashMap<>();
    /**
     * The M item array list.
     */
    List<Item> mItemArrayList;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final int FILE_SELECT_CODE = 1111;
    /**
     * The M string builder.
     */
    StringBuilder mStringBuilder;
    /**
     * The M sectioned adapter.
     */
    WordCountSectionAdapter mSectionedAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        //// TODO: 26/04/17 intializing
        mItemArrayList = new ArrayList<>();
        mStringBuilder = new StringBuilder();
        //// TODO: 26/04/17  dummy string
        String myString = "Binay Binay Binay Binay Binay Binay Binay Binay Binay Binay Binay Binay Binay Binay Binay Binay Binay Binay Binay Binay Binay Binay Binay Binay Binay Binay Binay Binay Binay Binay Binay Binay Binay Binay Binay Binay Binay Binay Binay Binay Binay Binay Binay Binay Binay Binay Binay Binay Binay Binay Binay Binay Binay Binay Binay Binay Binay" +
                "Binay Binay Binay Binay Binay Binay Binay Binay i am new at android android development and i am new at development development development development development development development development development development development development development development development development developmentandroid development android development android development and i am new at android development and android development android development android development android development android development android development";

        //// TODO: 26/04/17 counting word from string
        getWordCount(mWordHashMap, myString);

        //// TODO: 26/04/17 creating array list from hash
        for (Map.Entry<String, Integer> m : mWordHashMap.entrySet()) {
            mItemArrayList.add(new Item(m.getKey(), m.getValue()));
        }

        //// TODO: 26/04/17 sorting list based on word count
        Collections.sort(mItemArrayList, new Comparator<Item>() {

            public int compare(Item item1, Item item2) {
                if (item1.value < item2.value)
                    return -1;
                else if (item1.value > item2.value)
                    return 1;
                else
                    return 0;
            }
        });
        //// TODO: 26/04/17 recyclerview
        mWordCountRecyclerView.setHasFixedSize(true);
        mWordCountRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mWordCountRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mWordCountAdapter = new WordCountAdapter(this, mItemArrayList);


        //// TODO: 26/04/17 code to provide section list
        List<WordCountSectionAdapter.Section> mSections = new ArrayList<WordCountSectionAdapter.Section>();
        //// TODO: 26/04/17 adding total sections
        mSections.add(new WordCountSectionAdapter.Section(0, "0 - 10"));
        int sectionCount = 1, sectionIndex = 0;
        for (Item item : mItemArrayList) {
            if ((item.value >= sectionCount) && (item.value < (sectionCount + 10))) {
                ++sectionIndex;
            } else {
                if ((sectionCount + 10) > 10) {
                    sectionCount += 10;
                    mSections.add(new WordCountSectionAdapter.Section(sectionIndex, (sectionCount) + " - " + (sectionCount + 9)));
                    ++sectionIndex;
                }

            }

        }

        //// TODO: 26/04/17 adding custom adapter to sectioned adapter
        WordCountSectionAdapter.Section[] mSection = new WordCountSectionAdapter.Section[mSections.size()];
        mSectionedAdapter = new WordCountSectionAdapter(this, R.layout.layout_section_item, R.id.section_text, mWordCountAdapter);
        mSectionedAdapter.setSections(mSections.toArray(mSection));

        //// TODO: 26/04/17 adding sectioned adapter to recyclerview
        mWordCountRecyclerView.setAdapter(mSectionedAdapter);


    }

    /**
     * Gets word count.
     *
     * @param mWordHashMap the m word hash map
     * @param string       the string
     */
    public void getWordCount(Map<String, Integer> mWordHashMap, String string) {

        for (String item : string.trim().split(" ")) {
            if (!mWordHashMap.containsKey(item)) {
                mWordHashMap.put(item, 1);
            } else {
                int count = mWordHashMap.get(item);
                mWordHashMap.put(item, ++count);
            }

        }


    }

    /**
     * Func select file.
     */
    @OnClick(R.id.uploadButton)
    public void funcSelectFile() {
        if (checkPermission()) {
            chooseFile();
        } else {
            requestPermission();
            chooseFile();
        }
    }

    //// TODO: 26/04/17 opening mobile directory for selcting file
    private void chooseFile() {
        Intent intent = new Intent();
        intent.setType("text/plain");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, FILE_SELECT_CODE);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILE_SELECT_CODE) {
            Uri uri = data.getData();
            mStringBuilder = new StringBuilder();
            try {
                File file = new File(getRealPath(uri, MainActivity.this));
                BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    mStringBuilder.append(line);
                }
                if (mItemArrayList != null && mItemArrayList.size() > 0)
                    mItemArrayList.clear();
                getWordCount(mWordHashMap, String.valueOf(mStringBuilder));
                mWordCountAdapter.notifyDataSetChanged();
                mSectionedAdapter.notifyDataSetChanged();
            } catch (IOException e) {
                e.printStackTrace();

            } finally {
                // bufferedReader.close();
            }


        }
    }

    /**
     * Gets real path.
     *
     * @param contentUri the content uri
     * @param activity   the activity
     * @return the real path
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String getRealPath(Uri contentUri, Context activity) {
        String result;
        Cursor cursor = activity.getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            result = contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;

        }
    }

    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
        } else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }


}
