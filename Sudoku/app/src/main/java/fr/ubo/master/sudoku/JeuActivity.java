package fr.ubo.master.sudoku;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class JeuActivity extends Activity {
    String[] tab = {"Supprimer","1","2","3","4","5","6","7","8","9"};
    Grille grille;
    Canvas canvas;
    Intent intent = new Intent();
    Button valider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jeu);
        grille = findViewById(R.id.grille);
        if(getIntent().hasExtra("continuer")){
            int i = getIntent().getExtras().getInt("continuer");
            SharedPreferences pref = this.getSharedPreferences("sudoku",MODE_PRIVATE);
            String grille_user = pref.getString("sudoku","");
            String grille_base = pref.getString("sudoku_base","");
            grille.set(grille_user);
            grille.setFixIdx(grille_base);
        }
        if(getIntent().hasExtra("grille")) {
            String grille_selec = getIntent().getStringExtra("grille");
            grille.set(grille_selec);
        }
        grille.setCoordinatesListener(new Grille.OnCoordinateUpdate() {
            @Override
            public void onUpdate(int x, int y) {
                f(x,y);
            }
        });
    }

    //Fonction d'affichage de l'alertbox permettant le choix des nombres a rentrer dans les case

    public void f(int x,int y) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
                .setItems(tab, (dialog, which) -> {
                    grille.set(x,y,which);
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    //Fonction de validation de la grille

    public void isValidate(View view) {
        valider = findViewById(R.id.valider);
        grille.gagne();
        if(grille.gagne()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Gagné!");
            builder.setMessage("Félicitation!");
            AlertDialog alert = builder.create();
            alert.show();
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Perdu!");
            builder.setMessage("Essaye encore!");
            AlertDialog alert = builder.create();
            alert.show();
        }
        Log.i("mySudoku", "" + grille.gagne());
    }

    //fonction de sauvegarde de la grille

    public void sauvegarde(){
        SharedPreferences str = this.getSharedPreferences("sudoku",MODE_PRIVATE);
        SharedPreferences.Editor edit = str.edit();
        edit.putString("sudoku",grille.getGrille());
        edit.putString("sudoku_base",grille.getBaseGrille());
        edit.apply();

        String retour = str.getString("sudoku", "");
        String retour_base = str.getString("sudoku_base", "");
        Log.i("mySudoku",retour);
        Log.i("mySudoku",retour_base);
    }

    @Override
    public void onStop(){
        sauvegarde();
        super.onStop();
    }
}