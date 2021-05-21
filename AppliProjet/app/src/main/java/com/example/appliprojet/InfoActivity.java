package com.example.appliprojet;

import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.view.WindowManager;
import android.widget.TextView;
import android.os.Bundle;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import java.io.UnsupportedEncodingException;
import java.lang.*;
import android.os.Handler;
import android.widget.Button;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
import android.view.View;
import android.widget.Toast;
import java.util.HashMap;
import java.util.Map;

public class InfoActivity extends AppCompatActivity {
    String qrcodeurl;
    private String jsondata;
    private JSONObject obj;
    private String access_token;
    private String id;
    private String secret;
    private String adressipserveur;
    private String datasetid;
    private String aurionapi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int pleinEcran = (int) WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setFlags(pleinEcran, pleinEcran);
        setContentView(R.layout.activity_info);

        //récupération de la chaine de caractères renvoyer par le qrcode

        qrcodeurl = getIntent().getStringExtra("qrcode");

        //récupération des données sauvegardées

        SharedPreferences datasauvegarde = this.getSharedPreferences("logxibo",MODE_PRIVATE);

        adressipserveur = datasauvegarde.getString("adresseip","");
        id = datasauvegarde.getString("Clientid","");
        secret = datasauvegarde.getString("Clientsecret","");
        datasetid = datasauvegarde.getString("datasetid","");
        aurionapi = datasauvegarde.getString("aurionapi","");

        //appel de la fonction de recuperation du JSON sur l'API avec l'identifiant du Qrcode

        getString(qrcodeurl);

        //bouton retour

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sortie();
            }
        });
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            //sortie apres 10 secondes

            public void run() {
                sortie();
            }
        }, 10000L);
    }

    // récéption du JSON de l'API aurion

    public void getString(String idaurion) {
        StringRequest strReq = new StringRequest(Request.Method.GET, aurionapi + idaurion, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(InfoActivity.this, response, Toast.LENGTH_LONG).show();
                try {
                    jsondata = response;
                    System.out.println(response);
                    traitementmessage(jsondata);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                gettoken(id,secret);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(InfoActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(strReq);
    }

    //Requete de récupération du token d'acces a l'API

    public void gettoken(String id , String secret) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, adressipserveur + "api/authorize/access_token",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(InfoActivity.this, response, Toast.LENGTH_LONG).show();
                        try {
                            JSONObject objetjson = new JSONObject(response);
                            access_token = (String) objetjson.get("access_token");
                            //System.out.println(access_token);
                            xiborequest();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(InfoActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {

            //Paramètre du body de la requète

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

    //requete d'envoie du JSON sur la Dataset de L'API de Xibo

    public void xiborequest(){

        //création du JSON a envoyées dans le body de la requète

        try
        {
            String jsonstring =
            "{\n" +
                    "\t\"uniqueKeys\": [\n" +
                    "\t\t\"Nom\"\n" +
                    "\t],\n" +
                    "\t\"truncate\": [\n" +
                    "\t\t\"True\"\n" +
                    "\t],\n" +
                    "\t\"rows\": [{\n" +
                    "\t\t\t\"Nom\": \""+ obj.get("Nom.Individu") + "\",\n" +
                    "\t\t\t\"Prenom\": \""+ obj.get("Pr\u00e9nom.Individu") + "\",\n" +
                    "\t\t\t\"Nomapprenant\": \""+ obj.get("Nom.Apprenant") +"\",\n" +
                    "\t\t\t\"Prenomapprenant\": \""+ obj.get("Pr\u00e9nom.Apprenant") +"\",\n" +
                    "\t\t\t\"Titre\": \""+ obj.get("Titre stage.Stage") +"\",\n" +
                    "\t\t\t\"Stage\": \""+ obj.get("Libell\u00e9.Type stage") +"\"\n" +
                    "\t\t}\n" +
                    "\t]\n" +
                    "}";

            JSONObject jsonBody = new JSONObject(jsonstring);
            final String mRequestBody = jsonBody.toString();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, adressipserveur +  "api/dataset/importjson/" + datasetid, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //Toast.makeText(InfoActivity.this, response, Toast.LENGTH_LONG).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //Toast.makeText(InfoActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                }

            }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError
                {
                    Map<String, String> params = new HashMap<>();
                    System.out.println(access_token);
                    params.put("gcm_token", access_token);
                    return params;
                }

                //header de la requète

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError
                {
                    Map<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json");
                    String bearer = "Bearer " + access_token;
                    headers.put("Authorization", bearer);
                    return headers;
                }

                //corp de la requète

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                        return null;
                    }
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        } catch(
                JSONException e)
        {
            e.printStackTrace();
        }

    }

    //fonction d'affichage des données sur l'application

    public void traitementmessage(String JsonString) throws JSONException {
        TextView message = findViewById(R.id.message);
        JSONArray Array = new JSONArray(JsonString);
        obj = Array.getJSONObject(0);
        message.setText("Bonjour " + obj.get("Pr\u00e9nom.Individu") + " " + obj.get("Nom.Individu") + ".\n" + "La soutenance de " + obj.get("Pr\u00e9nom.Apprenant") + " " + obj.get("Nom.Apprenant") + " pour le " + obj.get("Libell\u00e9.Type stage") + " en tant que " + obj.get("Titre stage.Stage") +" va débuter.");
    }

    //fonction de fermeture de l'activité

    public void sortie(){
        finish();
    }
}