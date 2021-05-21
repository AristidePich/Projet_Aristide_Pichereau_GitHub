package fr.ubo.master.sudoku;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.Random;

public class GridChoiceActivity extends Activity {
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gridchoice);
        String[] array = new String[]{"0","1","2","3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "Grille aléatoire"};
        ListView mListView = (ListView) findViewById(R.id.list_view);
        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, array);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                int rand = new Random().nextInt(29);
                final String item = (String) parent.getItemAtPosition(position);
                Log.i(item, "Position=" + position);
                if(position == 30){
                    request(rand);
                }else {
                    request(position);
                }
            }
        });
    }

    //fonction pour aller suir la page de jeu

    public void start(String response){
        Intent intent = new Intent(this,JeuActivity.class);
        intent.putExtra("grille",response);
        startActivity(intent);
    }

    //fonction de requete https poour récupérer les grilles

    public void request(int id) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://labsticc.univ-brest.fr/~bounceur/cours/android/tps/sudoku/index.php?v=" + id;
        TextView textView = findViewById(R.id.text);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                start(response);
            }
            }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                textView.setText("That didn't work!");
            }
        });
            queue.add(stringRequest);
        }
}