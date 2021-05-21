package com.example.appliprojet;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import android.widget.EditText;
import android.widget.Toast;
import java.util.HashMap;
import java.util.Map;


public class  MainActivity extends AppCompatActivity {

    private String id;
    private String secret;
    private String adressipserveur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EditText Clientid = (EditText)findViewById(R.id.Clientid);
        EditText Clientsecret = (EditText)findViewById(R.id.Clientsecret);
        EditText adresseip = (EditText)findViewById(R.id.adresseip);
        EditText datasetid = (EditText)findViewById(R.id.datasetid);
        EditText aurionapi = (EditText)findViewById(R.id.aurionapi);




        SharedPreferences datasauvegarde = this.getSharedPreferences("logxibo",MODE_PRIVATE);

        Clientid.setText(Clientid.getText().append(datasauvegarde.getString("Clientid","")));
        //Clientid.setText("1FMfeD9bZtqqxmMsFL257FxBByjb4mTgqOL85UxS");
        adresseip.setText(adresseip.getText().append(datasauvegarde.getString("adresseip","")));
        //adresseip.setText("http://172.31.9.49/");
        Clientsecret.setText(Clientsecret.getText().append(datasauvegarde.getString("Clientsecret","")));
        //Clientsecret.setText("sKNKS7eHlrJEaJOb34FbzOQ924s5AVfHbBAn3NtAWoh5iDRl0a9UdHND3wd0dCEG1rKeXDfQEz0HDf9rPPMuOmgWpYAu9XBs27vqJq6b4CNHX8XDmzSRNTKhyPiDZipMozigN015xF9D5XW28utjM0zFUIglV8itGdncqoBLh5D8mv8aYWJreAE3TOsLZPohlZG1crYhBoU4dJmIMv2C0Tm7aSr96KaUHJmZQVOS49IZi5eYlPukDX8zyhDMKq");
        datasetid.setText(datasetid.getText().append(datasauvegarde.getString("datasetid","")));
        aurionapi.setText(aurionapi.getText().append(datasauvegarde.getString("aurionapi","")));
        //aurionapi.setText("http://api-aurion-preprod.isen-ouest.fr/api/35be8e2fff57a1a0c43f56826ead135235df415d5f2c018792fac7aeba6e4619/id_evenement/");

    }

    //demarage de l'activité de rendu de la camera

    public void Start(View v) {

        Intent intent=new Intent(this,CameraActivity.class);
        sauvegarder(v);
        startActivity(intent);

    }

    //sauvegarde des données rentrées

    public void sauvegarder(View v){



        EditText Clientid = (EditText)findViewById(R.id.Clientid);
        EditText Clientsecret = (EditText)findViewById(R.id.Clientsecret);
        EditText adresseip = (EditText)findViewById(R.id.adresseip);
        EditText Datasetid = (EditText)findViewById(R.id.datasetid);
        EditText aurionapi = (EditText)findViewById(R.id.aurionapi);

        SharedPreferences str = this.getSharedPreferences("logxibo",MODE_PRIVATE);
        SharedPreferences.Editor edit = str.edit();
        edit.putString("adresseip",adresseip.getText().toString());
        edit.putString("Clientid",Clientid.getText().toString());
        edit.putString("Clientsecret",Clientsecret.getText().toString());
        edit.putString("datasetid",Datasetid.getText().toString());
        edit.putString("aurionapi",aurionapi.getText().toString());

        edit.apply();

    }

    //demande de token pour tester les données enregistrées

    public void test(View v){

        EditText Clientid = (EditText)findViewById(R.id.Clientid);
        EditText Clientsecret = (EditText)findViewById(R.id.Clientsecret);
        EditText adresseip = (EditText)findViewById(R.id.adresseip);

        adressipserveur = adresseip.getText().toString();
        secret = Clientsecret.getText().toString();
        id = Clientid.getText().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, adressipserveur + "api/authorize/access_token",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(MainActivity.this, "Connexion au serveur Xibo effectué avec succès", Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("client_id", id);
                params.put("client_secret", secret);
                params.put("grant_type", "client_credentials");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}