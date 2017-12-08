package edu.eci.arsw.bombermanx.model.game;

import edu.eci.arsw.bombermanx.model.game.entities.Caja;
import edu.eci.arsw.bombermanx.model.game.entities.Caja_Metalica;
import edu.eci.arsw.bombermanx.model.game.entities.Espacio;
import edu.eci.arsw.bombermanx.model.game.entities.Bomba;
import edu.eci.arsw.bombermanx.model.game.entities.Jugador;
import edu.eci.arsw.bombermanx.model.game.entities.Elemento;
import edu.eci.arsw.bombermanx.model.game.entities.Man;
import java.util.ArrayList;
import java.awt.event.*;
import javax.swing.Timer;

/**
 *
 * @author Kvn CF <ECI>
 */
public class Juego {

    public static final int ARRIBA = 0;
    public static final int ABAJO = 1;
    public static final int DERECHA = 2;
    public static final int IZQUIERDA = 3;
    public static final int ANCHO = 20;
    public static final int ALTO = 10;
    private static final int TIEMPOEXPLOTARBOMBAS = 5000;
    private ArrayList<Jugador> jugadores;
    private Elemento[][] tablero;
    private ArrayList<Man> manes;
    public static final int MAXIMOJUGADORES = 4;
    private Timer timer;

    public Juego(ArrayList<Jugador> jugadores, String[][] tableroTemporal) {
        manes = new ArrayList<>();
        this.jugadores = jugadores;
        this.tablero = new Elemento[ALTO][ANCHO];

        int x;
        int y;
        // creando Manes y agregándolos al tablero
        for (int i = 0; i < jugadores.size(); i++) {
            switch (i) {
                case 0:
                    x = 0;
                    y = 0;
                    break;
                case 1:
                    y = 19;
                    x = 9;
                    break;
                case 2:
                    y = 0;
                    x = 9;
                    break;
                case 3:
                    y = 19;
                    x = 0;
                    break;
                default:
                    x = 10;
                    y = 10;
            }

            Man manTMP = new Man("black", jugadores.get(i), "key", x, y);
            tablero[x][y] = manTMP;
            manes.add(manTMP);
        }
        tablero[2][2] = new Caja("", 2, 2);
        tablero[2][3] = new Caja("", 2, 3);
        tablero[2][4] = new Caja("", 2, 4);
        tablero[2][5] = new Caja("", 2, 5);
        tablero[2][6] = new Caja("", 2, 6);
        // Mapear Tablero
        //mapearTablero(tableroTemporal);
    }

    /**
     * Mapear tablero de String a Objetos Author: Kevin S Sanchez
     *
     * @param temp Matriz de Strings
     */
    private void mapearTablero(String[][] temp) {
        //Recorrer Filas
        String letter;
        for (int row = 0; row < temp.length; row++) {
            //Recorrer Columnas
            for (int col = 0; col < temp[row].length; col++) {
                letter = temp[row][col];
                System.out.println("///////////////////////// Letra: " + letter);
                // Convenciones para hacer escenarios:
                // * {1,2,3,4,5,6.....} =  Numeros para representar jugadores.
                // * 'O' = Espacio vacio.
                // * 'B' = Bomba.
                // * 'C' = Caja que se puede destruir.
                // * 'X' = Bloque (No se puede destruir).
                // * 'R' = Poder de Correr.
                // * 'T' = Poder de expansion de explosion de Bomba.
                // * 'M' = Añadir cantidad de bombas que se pueden colocar al mismo tiempo
                // * {'@', '-', '/'} = Caracteres especiales para enemigos.
                if (isNumeric(letter)) {
                    this.tablero[row][col] = new Man("red", jugadores.get(Integer.parseInt(letter) - 1), letter, row, col);
                    System.out.println("---- POSX: " + this.tablero[row][col].getPosRow() + " + + + POSY: " + this.tablero[row][col].getPosCol());
                } else {
                    switch (letter) {
                        case "O":
                            this.tablero[row][col] = new Espacio(letter, row, col);
                            System.out.println("---- POSX: " + this.tablero[row][col].getPosRow() + " + + + POSY: " + this.tablero[row][col].getPosCol());
                            break;

                        case "C":
                            this.tablero[row][col] = new Caja(letter, row, col);
                            System.out.println("---- POSX: " + this.tablero[row][col].getPosRow() + " + + + POSY: " + this.tablero[row][col].getPosCol());
                            break;

                        case "X":
                            this.tablero[row][col] = new Caja_Metalica(letter, row, col);
                            System.out.println("---- POSX: " + this.tablero[row][col].getPosRow() + " + + + POSY: " + this.tablero[row][col].getPosCol());
                            break;

                        default:
                            this.tablero[row][col] = new Espacio(letter, row, col);
                            System.out.println("---- POSX: " + this.tablero[row][col].getPosRow() + " + + + POSY: " + this.tablero[row][col].getPosCol());
                            break;
                    }
                }
            }
        }
    }

    /**
     * Verificar si String es Numerico
     *
     * @param str Cadena de texto a verificar
     * @return True: Es numerico, False: NO es numerico
     */
    private boolean isNumeric(String str) {
        double de;
        try {
            de = Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    /**
     * Ejecuta la accion de la bomba de ser posible, la coloca y luego temporiza
     * 5s para explotar
     *
     * @param jugador
     * @return
     */
    public boolean accionBomba(Jugador jugador) {
        Man man = manes.get(jugadores.indexOf(jugador));
        int mposCol = man.getPosCol();
        int mposRow = man.getPosRow();
        Bomba explotara;

        boolean puede = hay_objeto(mposCol, mposRow, man);

        if (puede) {
            System.out.println("Pudo poner bomba >>");

            explotara = man.accionBomba();
            tablero[mposCol][mposRow] = (Elemento) explotara;
            explotar(explotara);
        }

        return puede;
    }

    /**
     * Explota la bomba segun el TIEMPOEXPLOTARBOMBAS, y en la trayectoria de la
     * explosion informa que daños causo
     *
     * @param explotara
     */
    private void explotar(Bomba explotara) {

        timer = new Timer(TIEMPOEXPLOTARBOMBAS, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                timer.stop();
                
                for(int i=0; i<tablero.length;i++){
                    for(int k=0; k<tablero[0].length;k++){
                            
                        
                        }                    
                }
                
                
                

            }//fin actionPerformed
        });
        timer.start();
        System.out.println("empieza");
    }

    private void destrucionDeExplosion(int pCOl, int pRow) {
        Bomba tmp = (Bomba) tablero[pCOl][pRow];
        int radio = tmp.getRadio();
    }

    /**
     * Revisa que fila y columna del tablero no este ocuapda, expectuando por el
     * Man
     *
     * @param fila
     * @param columna
     * @return
     */
    private boolean hay_objeto(int fila, int columna, Man man) {
        Bomba bomba = (Bomba) tablero[fila][columna];
        return bomba.get_man().equals(man);         // provisional solo mirando Man mientras se implementa para revisar si hay otra cosa
    }

    public boolean mover() {
        return false;
    }

    @Override
    public String toString() {
        ArrayList<String> cajasS = new ArrayList<>();
        ArrayList<String> manesS = new ArrayList<>();
        for (int i = 0; i < tablero.length; i++) {
            for (int k = 0; k < tablero[0].length; k++) {
                if (tablero[i][k] instanceof Caja) {
                    cajasS.add("{x:" + i + ",y:" + k + "}");
                }
                if (tablero[i][k] instanceof Man) {
                    manesS.add(tablero[i][k].toString());
                }
            }
        }
        return "{\"cajas\":" + cajasS.toString() + ",\"manes\":" + manesS.toString() + ",\"ancho\":" + ANCHO + ",\"alto\":" + ALTO + "}";
    }

    public ArrayList<Jugador> getJugadores() {
        return jugadores;
    }

    public void setJugadores(ArrayList<Jugador> jugadores) {
        this.jugadores = jugadores;
    }

    public Elemento[][] getTablero() {
        return tablero;
    }

    public void setTablero(Elemento[][] tablero) {
        this.tablero = tablero;
    }
}



/*


package javaapplication3;

import java.util.ArrayList;
import java.util.Arrays;

public class JavaApplication3 {

    private static int alto = 5;
    private static int ancho = 6;
    private static String[][] tablero;

       public static void main(String[] args) {

        tablero = new String[alto][ancho];

        for (int i = 0; i < alto; i++) {
            for (int k = 0; k < ancho; k++) {
                tablero[i][k] = Integer.toString(i) + Integer.toString(k);
            }
        }

        tablero[3][5] = "X";
        tablero[1][2] = "M";
        tablero[3][1] = "X";
        tablero[3][2] = "B"; // bomba

        for (int i = 0; i < alto; i++) {
            System.out.println(Arrays.toString(tablero[i]));
        }

        recorrido(alto-1, ancho-1);

    }

    private static void recorrido(int x, int y) {
        int radio = 2;
        int cont = 0;

        System.out.println("IZQUIERDA");
        //izquierda
        if (distancia(x, y, 0, y) >= 1) {
            cont = y - 1;
            while (cont < ancho && cont >= 0) {
                System.out.println(tablero[x][cont]);

                cont -= 1;

            }
        }

        System.out.println("DERECHA");
        //derecha
        if (distancia(x, y, x, ancho - 1) >= 1) {
            cont = y + 1;
            while (cont < ancho && cont >= 0) {
                System.out.println(tablero[x][cont]);

                cont += 1;

            }
        }

        System.out.println("ARRIBA");
        //arriba
        if (distancia(x, y, 0, y) >= 1) {
            cont = x - 1;
            while (cont < alto && cont >= 0) {
                System.out.println(tablero[cont][y]);

                cont -= 1;

            }
        }

        System.out.println("ABAJO");
        //abajo
        if (distancia(x, y, x, alto - 1) >= 1) {
            cont = x + 1;
            while (cont < alto && cont >= 0) {
                System.out.println(tablero[cont][y]);

                cont += 1;

            }
        }

    }

    private static int distancia(int x1, int y1, int x2, int y2) {
        return (int) Math.sqrt((int) Math.pow(x1 - x2, 2) + (int) Math.pow(y1 - y2, 2));
    }

}



*/