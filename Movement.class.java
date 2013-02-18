 /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Yoshi;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

/**
 *
 * @author Administrador
 */
public class Construcciones extends Thread { //Clase que se utiliza para clasificar y tener el control de todas las posibles
    public String usuario;         //construcciones que puede realizar el usuario, dependiendo del edificio en el que se crean.
    ArrayList<Tarea> samurai;   //por ejemplo no puedes construir 2 samurais a la vez porq las dos se crean en los cuarteles.
    ArrayList<Tarea> yabusame;
    ArrayList<Tarea> ninja;
    ArrayList<Tarea> legendario;
    ArrayList<Tarea> asedio;
    ArrayList<Tarea> edificio;
    ArrayList<Tarea> tecnologia;
    ArrayList<Tarea> arma;
    ArrayList<Tarea> armadura;
    ArrayList<Tarea> movimientos;


    //inicializamos todas las variables cuando nos conectamos
    public Construcciones(final String usuario){
        this.usuario = usuario;
        samurai = new ArrayList<Tarea>();
        yabusame = new ArrayList<Tarea>();
        ninja = new ArrayList<Tarea>();
        legendario = new ArrayList<Tarea>();
        asedio = new ArrayList<Tarea>();
        edificio = new ArrayList<Tarea>();
        tecnologia = new ArrayList<Tarea>();
        arma = new ArrayList<Tarea>();
        armadura = new ArrayList<Tarea>();
        movimientos = new ArrayList<Tarea>();

        /*javax.swing.Timer actualizador = new javax.swing.Timer(3000, new ActionListener(){

            public void actionPerformed(ActionEvent e) {
                actualizacion();
                //System.out.println("Actualizando construcciones cada 5 seg.");
            }
        });

        actualizador.setRepeats(true);
        actualizador.start();*/
        this.start();
    }


    //repasamos la base de datos en busca de construcciones pendientes, muy util si se cae el servidor
    @Override
    public synchronized void run(){ //al mismo tiempo con este metodo se mantiene la cola actualizada

        Connection conn = null;
        boolean borrar = false;
        System.out.println("Construccion creada");

        try {
            conn = Conectar.conectar();
        } catch (SQLException ex) {
            System.out.println("Fallo de conexion de construcciones.");
        }

        while(!borrar){
            try {
                Thread.sleep(3000);
            } catch (InterruptedException ex) {
            }
        try {
                    Statement st = conn.createStatement();
                    ResultSet rs = st.executeQuery("SELECT * FROM construcciones WHERE usuario = '" + usuario + "' ORDER BY idConstruccion ASC");

                    while(rs.next()){
                        if(rs.getString(3).equals("samurai") && !estaEnCola(rs.getInt(1))){
                            samurai.add(new Tarea(rs.getInt(1), rs.getString(3), rs.getInt(4), rs.getInt(5), rs.getInt(6), rs.getInt(7)));
                        }

                        else if(rs.getString(3).equals("yabusame") && !estaEnCola(rs.getInt(1))){
                            yabusame.add(new Tarea(rs.getInt(1), rs.getString(3), rs.getInt(4), rs.getInt(5), rs.getInt(6), rs.getInt(7)));
                        }

                        else if(rs.getString(3).equals("ninja") && !estaEnCola(rs.getInt(1))){
                            ninja.add(new Tarea(rs.getInt(1), rs.getString(3), rs.getInt(4), rs.getInt(5), rs.getInt(6), rs.getInt(7)));
                        }

                        else if(rs.getString(3).equals("legendario") && !estaEnCola(rs.getInt(1))){
                            legendario.add(new Tarea(rs.getInt(1), rs.getString(3), rs.getInt(4), rs.getInt(5), rs.getInt(6), rs.getInt(7)));
                        }

                        else if(rs.getString(3).equals("asedio") && !estaEnCola(rs.getInt(1))){
                            asedio.add(new Tarea(rs.getInt(1), rs.getString(3), rs.getInt(4), rs.getInt(5), rs.getInt(6), rs.getInt(7)));
                        }

                        else if(rs.getString(3).equals("edificio") && !estaEnCola(rs.getInt(1))){
                            edificio.add(new Tarea(rs.getInt(1), rs.getString(3), rs.getInt(4), rs.getInt(5), rs.getInt(6), rs.getInt(7)));
                            System.out.println("Edificio insertado en la cola");
                        }
                    }

                    ResultSet resultado = st.executeQuery("SELECT * FROM investigaciones WHERE usuario = '" + usuario + "' ORDER BY idInvestigacion ASC");

                    while(resultado.next()){

                        if(resultado.getString(3).equals("tecnologia") && !estaEnCola(resultado.getInt(1))){
                            System.out.println("Tecnologia añadida a la cola");
                            tecnologia.add(new Tarea(resultado.getInt(1), resultado.getString(2), resultado.getString(3), resultado.getInt(4)));
                        }

                        else if(resultado.getString(3).equals("arma") && !estaEnCola(resultado.getInt(1))){
                            arma.add(new Tarea(resultado.getInt(1), resultado.getString(2), resultado.getString(3), resultado.getInt(4)));
                        }

                        else if(resultado.getString(3).equals("armadura") && !estaEnCola(resultado.getInt(1))){
                            armadura.add(new Tarea(resultado.getInt(1), resultado.getString(2), resultado.getString(3), resultado.getInt(4)));
                        }
                    }

                    ResultSet movi = st.executeQuery("SELECT * FROM movimientos WHERE usuario = '" + usuario + "' ORDER BY idMovimiento ASC");
                    boolean encontrado;
                    boolean espeloton;

                    while(movi.next()){
                        encontrado = false;
                        espeloton = false;

                        for(Tarea movimiento : movimientos){
                            if(movimiento.idTropa == movi.getInt(3) && !movimiento.esPeloton(movi.getInt(4))) { //si la tropa ya esta repetida es decir, 2 unidades distintas pero que van al mismo sitio
                                movimiento.pelotones.add(movi.getInt(4)); //solo se añaden las unidades y la cantidad de estas a la tarea ya existente
                                movimiento.pelotones.add(movi.getInt(7));
                                encontrado = true;
                            }
                            if(movimiento.idTropa == movi.getInt(3) && movimiento.esPeloton(movi.getInt(4)))
                                espeloton = true;
                        }
                        if(!encontrado && !estaEnCola(movi.getInt(1)) && !espeloton){
                        movimientos.add(new Tarea(Integer.parseInt(movi.getString(1)), movi.getString(2), Integer.parseInt(movi.getString(3)), Integer.parseInt(movi.getString(4)),
                                    Integer.parseInt(movi.getString(5)), Integer.parseInt(movi.getString(6)), Integer.parseInt(movi.getString(7))));
                        System.out.println("Movimiento insertado en la cola " + movi.getString(1));
                        }
                    }

                    st.close();
                    rs.close();
                    resultado.close();
                    movi.close();


                } catch (SQLException ex) {
                    System.out.println(ex.getMessage() + " Fallo de actualizacion de construcciones");
                }



        //A partir de aqui controlamos que haya siempre una tarea activa en cada una de las colas.
        boolean funcionando = false; //COLA SAMURAI

        for(int i=0;i<samurai.size();i++){

            if (samurai.get(i).estaActiva()){
                funcionando = true;
            }
            if(!samurai.get(i).estaActiva() && !samurai.get(i).estaHecha() && !funcionando){
                samurai.get(i).start();
                funcionando = true;
            }

            if(samurai.get(i).estaHecha()){
                samurai.remove(i);
                System.out.println("Construccion samurai removida de la cola");
            }

        }

        funcionando = false; //COLA YABUSAME

        for(int i=0;i<yabusame.size();i++){

            if (yabusame.get(i).estaActiva()){
                funcionando = true;
            }
            if(!yabusame.get(i).estaActiva() && !yabusame.get(i).estaHecha() && !funcionando){
                yabusame.get(i).start();
                funcionando = true;
            }

            if(yabusame.get(i).estaHecha()){
                yabusame.remove(i);
                System.out.println("Construccion yabusame removida de la cola");
            }

        }


        funcionando = false; //COLA NINJA

        for(int i=0;i<ninja.size();i++){

            if (ninja.get(i).estaActiva()){
                funcionando = true;
            }
            if(!ninja.get(i).estaActiva() && !ninja.get(i).estaHecha() && !funcionando){
                ninja.get(i).start();
                funcionando = true;
            }

            if(ninja.get(i).estaHecha()){
                ninja.remove(i);
                System.out.println("Construccion ninja removida de la cola");
            }

        }

        funcionando = false; //COLA LEGENDARIO

        for(int i=0;i<legendario.size();i++){

            if (legendario.get(i).estaActiva()){
                funcionando = true;
            }
            if(!legendario.get(i).estaActiva() && !legendario.get(i).estaHecha() && !funcionando){
                legendario.get(i).start();
                funcionando = true;
            }

            if(legendario.get(i).estaHecha()){
                legendario.remove(i);
                System.out.println("Construccion legendario removida de la cola");
            }

        }


        funcionando = false; //COLA ASEDIO

        for(int i=0;i<asedio.size();i++){

            if (asedio.get(i).estaActiva()){
                funcionando = true;
            }
            if(!edificio.get(i).estaActiva() && !asedio.get(i).estaHecha() && !funcionando){
                asedio.get(i).start();
                funcionando = true;
            }

            if(asedio.get(i).estaHecha()){
                asedio.remove(i);
                System.out.println("Construccion asedio removida de la cola");
            }

        }


        funcionando = false; //COLA EDIFICIO

        for(int i=0;i<edificio.size();i++){
            
            if (edificio.get(i).estaActiva()){
                funcionando = true;
            }
            if(!edificio.get(i).estaActiva() && !edificio.get(i).estaHecha() && !funcionando){
                edificio.get(i).start();
                funcionando = true;
            }

            if(edificio.get(i).estaHecha()){
                edificio.remove(i);
                System.out.println("Construccion removida de la cola");
            }

        }


        funcionando = false; //COLA TECNOLOGIA

        for(int i=0;i<tecnologia.size();i++){

            if (tecnologia.get(i).estaActiva()){
                funcionando = true;
            }
            if(!tecnologia.get(i).estaActiva() && !tecnologia.get(i).estaHecha() && !funcionando){
                tecnologia.get(i).start();
                funcionando = true;
            }

            if(tecnologia.get(i).estaHecha()){
                tecnologia.remove(i);
                System.out.println("Construccion tecnologia removida de la cola");
            }

        }

        funcionando = false; //COLA ARMA

        for(int i=0;i<arma.size();i++){

            if (arma.get(i).estaActiva()){
                funcionando = true;
            }
            if(!arma.get(i).estaActiva() && !arma.get(i).estaHecha() && !funcionando){
                arma.get(i).start();
                funcionando = true;
            }

            if(arma.get(i).estaHecha()){
                arma.remove(i);
                System.out.println("Construccion arma removida de la cola");
            }

        }

        funcionando = false; //COLA ARMADURA

        for(int i=0;i<armadura.size();i++){

            if (armadura.get(i).estaActiva()){
                funcionando = true;
            }
            if(!armadura.get(i).estaActiva() && !armadura.get(i).estaHecha() && !funcionando){
                armadura.get(i).start();
                funcionando = true;
            }

            if(armadura.get(i).estaHecha()){
                armadura.remove(i);
                System.out.println("Construccion armadura removida de la cola");
            }

        }

        //funcionando = false; //COLA MOVIMIENTO

        for(int i=0;i<movimientos.size();i++){

            /*if (movimientos.get(i).estaActiva()){
                funcionando = true;
            }*/
            if(!movimientos.get(i).estaActiva() && !movimientos.get(i).estaHecha()){// && !funcionando){
                movimientos.get(i).start();
                System.out.println("Movimiento a " + movimientos.get(i).idRegionDestino + " iniciado.");
                //funcionando = true;
            }

            if(movimientos.get(i).estaHecha()){
                movimientos.remove(i);
                System.out.println("Movimiento removido de la cola");
            }


        }


        if(!tieneTareas() && !Yserver.estaConectado(usuario)){
            borrar = true;
            System.out.println("Thread de produccion parado.");
            Yserver.produccion.remove(this);
                try {
                    conn.close();
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }

        }//fin while true
        /*if(conn != null)
        try {
            conn.close();
        } catch (SQLException ex) {
            System.out.println("Fallo al cerrar conexion construcciones.");
        }*/
    }

    public boolean tieneTareas(){
        boolean trabajando = false;

        if(samurai.size() > 0)
            trabajando = true;
        if(yabusame.size() > 0)
            trabajando = true;
        if(ninja.size() > 0)
            trabajando = true;
        if(legendario.size() > 0)
            trabajando = true;
        if(asedio.size() > 0)
            trabajando = true;
        if(edificio.size() > 0)
            trabajando = true;
        if(tecnologia.size() > 0)
            trabajando = true;
        if(arma.size() > 0)
            trabajando = true;
        if(armadura.size() > 0)
            trabajando = true;
        if(movimientos.size() > 0)
            trabajando = true;

        return trabajando;
    }


    public boolean estaEnCola(int id){ //metodo para repasar las colas y no repetir la misma construccion
        boolean repetido = false;

        for(int i = 0; i < samurai.size() ; i++){
            if(samurai.get(i).idConstruccion == id)
                repetido = true;
        }

        for(int i = 0; i < yabusame.size() ; i++){
            if(yabusame.get(i).idConstruccion == id)
                repetido = true;
        }

        for(int i = 0; i < ninja.size() ; i++){
            if(ninja.get(i).idConstruccion == id)
                repetido = true;
        }

        for(int i = 0; i < legendario.size() ; i++){
            if(legendario.get(i).idConstruccion == id)
                repetido = true;
        }

        for(int i = 0; i < asedio.size() ; i++){
            if(asedio.get(i).idConstruccion == id)
                repetido = true;
        }

        for(int i = 0; i < edificio.size() ; i++){
            if(edificio.get(i).idConstruccion == id)
                repetido = true;
        }

        for(int i = 0; i < tecnologia.size() ; i++){
            if(tecnologia.get(i).idConstruccion == id)
                repetido = true;
        }
        for(int i = 0; i < arma.size() ; i++){
            if(arma.get(i).idConstruccion == id)
                repetido = true;
        }
        for(int i = 0; i < armadura.size() ; i++){
            if(armadura.get(i).idConstruccion == id)
                repetido = true;
        }
        for(int i = 0; i < movimientos.size() ; i++){
            if(movimientos.get(i).idConstruccion == id)
                repetido = true;
        }


    return repetido;
    }


}
