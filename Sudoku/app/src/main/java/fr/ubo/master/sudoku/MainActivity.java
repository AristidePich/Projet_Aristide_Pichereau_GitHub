package fr.ubo.master.sudoku;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.content.Intent;
import android.widget.Button;

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button b = findViewById(R.id.buttonjeu);
        SharedPreferences pref = this.getSharedPreferences("sudoku",MODE_PRIVATE);
        if((pref.getString("sudoku", "")).isEmpty() == true){
            b.setEnabled(false);
        }else {
            b.setEnabled(true);
        }
    }

    //Fonction pour reprendre la derniere partie en cours

    public void Continuer(View v) {
        Intent intent=new Intent(this,JeuActivity.class);
        intent.putExtra("continuer",1);
        startActivity(intent);
    }

    //Fonction pour aller a la page about

    public void About(View v) {
        Intent intent1=new Intent(this,AboutActivity.class);
        startActivity(intent1);
    }

    //Fonction pour aller a la de choix des grilles

    public void GridChoice(View v) {
        Intent intent1=new Intent(this,GridChoiceActivity.class);
        startActivity(intent1);
    }
}