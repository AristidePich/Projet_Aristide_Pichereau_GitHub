package fr.ubo.master.sudoku;

import android.os.Bundle;
import android.app.Activity;
import android.widget.ListView;
import java.util.ArrayList;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.view.View;
import android.util.Log;

public class ChoixActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choix);
        int[] array = new int[]{0,1,2,3,4,5,6,7,8,9};
        ListView mListView = (ListView) findViewById(R.id.list_view);
        ArrayList<String> arrayList = new ArrayList<String>();
        for(int s:array) {
            arrayList.add(String.valueOf(s));
        }
        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, arrayList);
        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                final String item = (String) parent.getItemAtPosition(position);
                Log.i(item, "Position=" + position);            }
        });
    }

}
