package fr.ubo.master.sudoku;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class Grille extends View {

    private int screenWidth;
    private int screenHeight;
    private int top;
    private int bottom;
    private int left;
    private int right;
    private int tailleCase;
    private int modifier;
    private int n;
    private boolean isWin = false;
    private boolean isLoose = false;

    private OnClickListener mOnClickListener;
    private OnCoordinateUpdate mCoordinatesListener;

    private Paint paint1;   // Pour dessiner la grille (lignes noires)
    private Paint paint2;   // Pour le texte des cases fixes
    private Paint paint3;   // Pour dessiner les lignes rouges (grosse)
    private Paint paint4;   // Pour le texte noir des cases a modifier
    private Paint paint5;

    private int[][] matrix = new int[9][9];
    private boolean[][] fixIdx = new boolean[9][9];

    public Grille(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public Grille(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Grille(Context context) {
        super(context);
        init();
    }

    //initialisation de la grille

    private void init() {

        paint1 = new Paint();
        paint1.setAntiAlias(true);
        paint1.setColor(Color.BLACK);

        paint2 = new Paint();
        paint2.setAntiAlias(true);
        paint2.setColor(Color.RED);
        // Centre le texte

        paint3 = new Paint();
        paint3.setAntiAlias(true);
        paint3.setColor(Color.RED);
        // Couleur rouge et grosses lignes

        paint4 = new Paint();
        paint4.setAntiAlias(true);
        paint4.setColor(Color.BLACK);
        paint4.setTextSize( 45 );

        paint5 = new Paint();
        paint5.setAntiAlias(true);
        paint5.setColor(Color.GREEN);
    }

    @Override

    //dessin de la grille

    protected void onDraw(Canvas canvas) {
        screenWidth = getWidth();
        screenHeight = getHeight();

        int w = Math.min(screenWidth, screenHeight);
        w = w - (w%9);
        n = w / 9 ;

        tailleCase = n;
        modifier = 10;
        paint2.setTextSize( n*0.33f );


        // Affiche les lignes rouge

        paint3.setStrokeWidth( n/9 );
        paint5.setStrokeWidth( n/9 );
        for( int i=0; i<=1; i++ ) {
            canvas.drawLine( (i+1)*(n*3)- modifier/2, 0- modifier, (i+1)*(n*3) - modifier/2, n*9- modifier, paint3 );
            canvas.drawLine( 0- modifier,(i+1)*(n*3)- modifier/2, n*9 - modifier, (i+1)*(n*3)- modifier/2, paint3 );
        }

        // Les contenus des cases

        paint1.setStyle(Paint.Style.STROKE);
        String s;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                s = "" + (matrix[j][i] == 0 ? "" : matrix[j][i]);

                top = i*tailleCase;
                bottom = top+tailleCase - modifier;
                left = j*tailleCase;
                right = left+tailleCase - modifier;

                if (fixIdx[j][i]) {
                    canvas.drawRect(left, top, right, bottom, paint1);
                    canvas.drawText(s, i * n + (n / 2) - (n / 10) - modifier/2, j * n + (n / 2) + (n / 10) - modifier/2, paint2);
                }
                else {
                    canvas.drawRect(left, top, right, bottom, paint1);
                    canvas.drawText(s, i * n + (n / 2) - (n / 10) - modifier/2, j * n + (n / 2) + (n / 10) - modifier/2, paint4);
                }
            }
        }
        if (isWin) {
            canvas.drawLine(0, 0, n * 9, 0, paint5);
            canvas.drawLine(0, 0, 0, n * 9, paint5);
            canvas.drawLine(0, n * 9, n * 9, n * 9, paint5);
            canvas.drawLine(n * 9, 0, n * 9, n * 9, paint5);
        }
        if (isLoose){
            canvas.drawLine(0, 0, n * 9, 0, paint3);
            canvas.drawLine(0, 0, 0, n * 9, paint3);
            canvas.drawLine(0, n * 9, n * 9, n * 9, paint3);
            canvas.drawLine(n * 9, 0, n * 9, n * 9, paint3);
        }
    }

    public int getXFromMatrix(int x) {
        // Renvoie l'indice d'une case a partir du pixel x de sa position h
        return (x / n);
    }

    public int getYFromMatrix(int y) {
        // Renvoie l'indice d'une case a partir du pixel y de sa position v
        return (y / n);
    }

    public void set(String s, int i) {
        // Remplir la ieme ligne de la matrice matrix avec un vecteur String s
        int v;
        for (int j = 0; j < 9; j++) {
            v = s.charAt(j) - '0';
            matrix[i][j] = v;
            if (v == 0)
                fixIdx[i][j] = false;
            else
                fixIdx[i][j] = true;
        }
    }

    public void setFixIdx(String s) {
        for(int i = 0; i < 81; i++)
            fixIdx[i / 9][i % 9] = (Integer.parseInt(s.substring(i, i+1)) == 1);
    }



    public void set(String s) {
        // Remplir la matrice matrix a partir d'un vecteur String s
        for (int i = 0; i < 9; i++) {
            set(s.substring(i * 9, i * 9 + 9), i);
        }
    }

    public void set(int x, int y, int v) {
        matrix[y][x] = v;
        invalidate();
    }

    public boolean isNotFix(int x, int y) {
        return !fixIdx[y][x];
    }

    public boolean gagne() {
        // Verifier si la case n'est pas vide ou bien s'il existe
        // un numero double dans chaque ligne ou chaque colonne de la grille
        for (int v = 1; v <= 9; v++) {
            for (int i = 0; i < 9; i++) {
                boolean bx = false;
                boolean by = false;
                for (int j = 0; j < 9; j++) {
                    if (matrix[i][j] == 0) {
                        isLoose = true;
                        invalidate();
                        return false;
                    }
                    if ((matrix[i][j] == v) && bx) {
                        isLoose = true;
                        invalidate();
                        return false;
                    }
                    if ((matrix[i][j] == v) && !bx) bx = true;

                    if ((matrix[j][i] == v) && by){
                        isLoose = true;
                        invalidate();
                        return false;
                    }
                    if ((matrix[j][i] == v) && !by) by = true;
                }
            }
        }
        isLoose = false;
        isWin = true;
        invalidate();
        return true;
    }

    //fonction appelé lorsque que l'on touche une case de la grille

    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();

        int x_f = getXFromMatrix(x);
        int y_f = getYFromMatrix(y);

        if(mCoordinatesListener != null && isNotFix(x_f,y_f)) {
            mCoordinatesListener.onUpdate(x_f, y_f);
        }
        return false;
    }

    public void setCoordinatesListener(OnCoordinateUpdate listener) {
        mCoordinatesListener = listener;
    }

    public interface OnCoordinateUpdate {
        void onUpdate(int x, int y);
    }

    //fonction de recuperation des grille pour sauvegarde

    public String getGrille(){
        String tab = "";
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                 tab += "" + (matrix[i][j]);

            }
        }
        return tab;
    }
    public String getBaseGrille(){
        String tab = "";
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                tab += "" + (fixIdx[i][j] ? "1":"0");
            }
        }
        return tab;
    }
}