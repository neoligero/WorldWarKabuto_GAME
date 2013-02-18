/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Yoshi;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.sql.*;
import java.util.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Administrador
 */
public class Yserver extends Thread{

    public static ArrayList<ConexionCliente> conectados;
    public static ArrayList<Construcciones> produccion;
    public static ArrayList<Usuario> usuarios;
    ServerSocket serversk;
    Socket tempsk;

public Yserver(){
    serversk = null;

    //Se crea el servidor y se pone en escucha en el puerto 6666
    try{
        serversk = new ServerSocket(6660);
    }catch(IOException e){
        System.out.println(e.getMessage());
        System.exit(0);
    }
    System.out.println("Servidor escuchando.");

    tempsk = new Socket();
    conectados = new ArrayList<ConexionCliente>();
    produccion = new ArrayList<Construcciones>();
    usuarios = new ArrayList<Usuario>();
    ActualizadorOro();
    iniciarProduccion();
    this.start();

    //Un gran metodo que actualiza el oro de todos los jugadores cada 10 min :)
    }
    

    @Override  //hemos metido el bucle infinito de aceptar conexiones en un hilo independiente
    public void run(){
        while(true){
        try{
        tempsk = serversk.accept();
        ConexionCliente jugador = new ConexionCliente(tempsk);
        conectados.add(jugador);
        jugador.start();
        System.out.println("Conexion establecida con " + tempsk.getInetAddress());

        }catch(IOException e){
            System.out.println(e.getMessage());
        }
    }

}//fin de Yserver



//INICIO DE ACTUALIZACION DE PRODUCCIONES
public void iniciarProduccion(){
    try {
        Connection conn = Conectar.conectar();
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("select nick from usuarios where nick in(select usuario from construcciones) or nick "
                                       +"in(select usuario from investigaciones) or nick in(select usuario from movimientos)");

        while(rs.next()){
            produccion.add(new Construcciones(rs.getString(1)));
            System.out.println(rs.getString(1));
        }

        st.close();
        rs.close();
        conn.close();

    }catch(SQLException e){
        System.out.println("Fallo actualizacion de produccion.");
    }
}


public static boolean estaConectado (String usuario){
    boolean conectado = false;

    //System.out.println(conectados.size());

    for (int i = 0; i<conectados.size(); i++){
        //System.out.println(conectados.get(i).usuario);
        if(conectados.get(i).usuario.equalsIgnoreCase(usuario))
            conectado = true;
    }
    //System.out.println(conectado);
    return conectado;
}

//elimina del array los elementos que estan parados
public synchronized static void borrarProduccion(String usuario){
    if(!produccion.isEmpty())
        for(Construcciones construccion : produccion){
            if(construccion.usuario.equalsIgnoreCase(usuario))
                produccion.remove(construccion);
        }
}

public void ActualizadorOro(){ //METODO QUE IRA INCREMENTANDO EL ORO PARA CADA JUGADOR DEPENDIENDO DE LAS MINAS QUE TENGA
                               //Y EL NIVEL DE CADA UNA DE ELLAS

javax.swing.Timer ao = new javax.swing.Timer(1000*60, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    Connection conn = Conectar.conectar();
                    Statement st = conn.createStatement();

                    usuarios = new ArrayList<Usuario>();
                    ResultSet rs = st.executeQuery("select * from usuarios");
                    while(rs.next()){
                        usuarios.add(new Usuario(rs.getString(1), rs.getString(2), rs.getString(3), Integer.parseInt(rs.getString(4)), Integer.parseInt(rs.getString(5)), Integer.parseInt(rs.getString(6)), Integer.parseInt(rs.getString(7))));
                    }

                    //preparamos una consulta que nos lanzara el numero de minas por categoria que tiene cada usuario
                    String consulta1 = "select idEdificio, count(*) from regiones, edificiosregion"+
                                        " where regiones.idRegion=edificiosregion.idRegion"+
                                        " and propietario='";

                    String consulta2 = "' and idEdificio in (1101,1102,1103,1104,1105)"+
                                        " group by idEdificio";

                    //recorremos toda la lista sumando el oro, dependiendo del numero de minas que posea
                    ResultSet rs2 = null;
                    for(Usuario usuario : usuarios){
                        rs2 = st.executeQuery(consulta1 + usuario.getNick() + consulta2);
                        int oro = 0;
                            while(rs2.next()){
                                    System.out.println(Integer.parseInt(rs2.getString(1)));
                                    if(Integer.parseInt(rs2.getString(1)) == 1101){
                                        oro = oro + (rs2.getInt(2) * 100);
                                    }
                                    else if(Integer.parseInt(rs2.getString(1)) == 1102){
                                        oro = oro + (rs2.getInt(2) * 150);
                                    }
                                    else if(Integer.parseInt(rs2.getString(1)) == 1103){
                                        oro = oro + (rs2.getInt(2) * 300);
                                    }
                                    else if(Integer.parseInt(rs2.getString(1)) == 1104){
                                        oro = oro + (rs2.getInt(2) * 800);
                                    }
                                    else if(Integer.parseInt(rs2.getString(1)) == 1105){
                                        oro = oro + (rs2.getInt(2) * 2000);
                                    }
                            }
                            st.executeQuery("UPDATE usuarios SET oro = (SELECT oro+" + oro + " FROM usuarios WHERE nick ='" + usuario.getNick() + "'"+
                                            ") WHERE nick = '" + usuario.getNick() + "'");

                    }
                    st.close();
                    rs.close();
                    rs2.close();
                    conn.close();

                } catch (SQLException ex) {
                    System.out.println(ex.getMessage() + " Fallo actualizar oro.");
                }

            }
            });

            ao.start();
}

//Clase conexion con un hilo independiente para cada una de ellas

public class ConexionCliente extends Thread {
    Socket socket;
    PrintWriter salida;
    BufferedReader entrada;
    String usuario;

    public ConexionCliente(Socket clientsocket) throws IOException{
        socket = clientsocket;
        salida = new PrintWriter(socket.getOutputStream(), true);
        entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        usuario = "null";
    }


    @Override
    public synchronized void run(){
        boolean vivo = true;
        System.out.println("Empieza el run");

        while(vivo){
            String texto = null;

            try{

            texto = entrada.readLine();
            
            }catch(IOException e){
                System.out.println(e.getMessage()+" Fallo lectura de entrada.");
                vivo = false;
            }
            
            //System.out.println(texto); //COMPROBACION <---------
            //A PARTIR DE AQUI TRABAJAMOS CON LA CLASE MENSAJE QUE DIFERENCIA LAS PALABRAS
            //CODIGO PARA SALIR
            if(texto != null){

                Mensaje mensaje = new Mensaje(texto);

        /**/    if (mensaje.getPalabra(0).equalsIgnoreCase("salir")){
                    conectados.remove(this);
                    vivo = false;
                }

                //CODIGO PARA LOGIN
       /**/     else if (mensaje.getPalabra(0).equalsIgnoreCase("login")) {
                        try {
                            Connection conn = Conectar.conectar();
                            Statement st = conn.createStatement();
                            ResultSet rs = st.executeQuery("SELECT pass FROM usuarios "+
                                                            "WHERE nick = '" + mensaje.getPalabra(1) + "'");
                            
                            if(rs.next()){
                                if(rs.getString(1).equals(mensaje.getPalabra(2))) {
                                    salida.println("1");
                                    System.out.println(mensaje.getPalabra(1));
                                    

                                    boolean encolado = false; //Comprobamos si la cola de produccion del usuario que
                                    
                                        for(int i=0 ; i< produccion.size() ; i++){ //se loguee ha sido creada o no, y la crea
                                            if(produccion.get(i).usuario.equalsIgnoreCase(mensaje.getPalabra(1))/* && produccion.get(i).isAlive()*/){
                                                encolado = true;
                                                /*if(!produccion.get(i).isAlive()){
                                                    produccion.get(i).run();
                                                    System.out.println("Produccion activada de nuevo");
                                                }*/
                                            }
                                        }
                                        if(!encolado){
                                            produccion.add(new Construcciones(mensaje.getPalabra(1)));
                                            System.out.println("Cola de produccion creada con exito.");
                                        }

                                    
                                }
                                else{
                                    salida.println("0");
                                }
                            }
                            else{
                                    salida.println("0");
                            }

                            salida.println("null");
                            rs.close();
                            st.close();
                            conn.close();


                        } catch (SQLException ex) {
                            System.out.println(ex.getMessage() + "fallo de login");
                            salida.println("0");
                        }
                } //fin de elseif login

                //CODIGO EXTRACCION DE DATOS DE USUARIO
                else if(mensaje.getPalabra(0).equalsIgnoreCase("extraer")){
                    try {
                            Connection conn = Conectar.conectar();
                            Statement st = conn.createStatement();
                            ResultSet rs = st.executeQuery("SELECT * FROM usuarios "+
                                                            "WHERE nick = '" + mensaje.getPalabra(1) + "'");

                            if(rs.next()){
                                int ncol = rs.getMetaData().getColumnCount();

                                //System.out.println(ncol); //Mostramos el numero de columnas

                                int j = 0;
                                for(int i=1; i < ncol; i++){
                                    salida.print(rs.getString(i) + " ");
                                }
                                salida.println(rs.getString(ncol));
                            }
                            else{
                                salida.println("null");
                            }
                            this.usuario = mensaje.getPalabra(1);
                            rs.close();
                            st.close();
                            conn.close();


                        } catch (SQLException ex) {
                            System.out.println(ex.getMessage() + " fallo de extraccion de datos.");
                        }
                }//fin elseif extraer

                //CODIGO EXTRACION SIMPLE CON LA SENTENCIA TAL CUAL
                else if(mensaje.getPalabra(0).equalsIgnoreCase("select")){
                    Atributo atri = null;
                        try {
                            Connection conn = Conectar.conectar();

                            System.out.println(texto);  //Mostramos el texto
                            Statement st = conn.createStatement();
                            ResultSet rs = st.executeQuery(texto);

                            while(rs.next()){
                                int ncol = rs.getMetaData().getColumnCount();
                                //USAMOS LA CLASE ATRIBUTO QUE NOS PERMITE CONCATENAR RESULTADOS Y ENVIARLOS EN MODO TEXTO
                                for(int i=1; i < ncol; i++){
                                    atri = new Atributo(rs.getString(i));
                                    salida.print(atri.toString() + " ");
                                }

                                atri = new Atributo(rs.getString(ncol));
                                salida.println(atri.toString());
                            }

                            salida.println("null");
                            rs.close();
                            st.close();
                            conn.close();

                        } catch (SQLException ex) {
                            salida.println("null");
                            System.out.println(ex.getMessage() + " fallo consulta simple");
                        }

                }//fin elseif consulta simple

                //CODIGO CONSTRUICCION EDIFICIO
                else if(mensaje.getPalabra(0).equalsIgnoreCase("insertar")){

                    try {
                            Connection conn = Conectar.conectar();

                            Statement st = conn.createStatement();
                            String insercion = texto.substring(9, texto.length());

                            System.out.println(insercion);  //Mostramos por pantalla la insercion

                            st.executeQuery(insercion);
                            st.executeQuery("commit");

                            st.close();
                            conn.close();

                    }catch(SQLException ex){
                        System.out.println(ex.getMessage() + " Fallo al conectar construir edificios");
                    }


                }//fin elseif codigo de construccion

                else if(mensaje.getPalabra(0).equalsIgnoreCase("descontar")){
                    try {
                            Connection conn = Conectar.conectar();

                            Statement st = conn.createStatement();

                            st.executeQuery("UPDATE usuarios SET oro="+(Integer.parseInt(mensaje.getPalabra(1))-Integer.parseInt(mensaje.getPalabra(2)))+
                                            " WHERE nick='"+usuario+"'");
                            st.executeQuery("commit");

                            ResultSet rs = st.executeQuery("SELECT oro FROM usuarios WHERE nick='"+usuario+"'");
                            if(rs.next())
                                salida.println(rs.getString(1));

                            st.close();
                            rs.close();
                            conn.close();
                    }catch(SQLException e){
                        System.out.println(e.getMessage() + " fallo descontar oro.");
                    }
                } //fin elseif codigo descontar

                else if(mensaje.getPalabra(0).equalsIgnoreCase("produccion")){

                    for(Construcciones construccion : produccion){ //manda al cliente todas las colas de produccion

                        if(construccion.usuario.equalsIgnoreCase(mensaje.getPalabra(1))){
                            //tipo EDIFICIO
                            for(Tarea tarea : construccion.edificio){
                                salida.println("edificio " + tarea.idUnidad + " " + tarea.idRegion + " " + tarea.cantidad + " " + tarea.cuentaatras + " " + tarea.activa);
                            }
                            //tipo UNIDAD
                            for(Tarea tarea : construccion.samurai){
                                salida.println("unidad " + tarea.idUnidad + " " + tarea.idRegion + " " + tarea.cantidad + " " + tarea.cuentaatras + " " + tarea.activa);
                            }
                            for(Tarea tarea : construccion.yabusame){
                                salida.println("unidad " + tarea.idUnidad + " " + tarea.idRegion + " " + tarea.cantidad + " " + tarea.cuentaatras + " " + tarea.activa);
                            }
                            for(Tarea tarea : construccion.ninja){
                                salida.println("unidad " + tarea.idUnidad + " " + tarea.idRegion + " " + tarea.cantidad + " " + tarea.cuentaatras + " " + tarea.activa);
                            }
                            for(Tarea tarea : construccion.legendario){
                                salida.println("unidad " + tarea.idUnidad + " " + tarea.idRegion + " " + tarea.cantidad + " " + tarea.cuentaatras + " " + tarea.activa);
                            }
                            for(Tarea tarea : construccion.asedio){
                                salida.println("unidad " + tarea.idUnidad + " " + tarea.idRegion + " " + tarea.cantidad + " " + tarea.cuentaatras + " " + tarea.activa);
                            }

                            //tipo TECNOLOGIA
                            for(Tarea tarea : construccion.tecnologia){
                                salida.println("tecnologia "+ tarea.cuentaatras);
                            }

                            //tipo ARMA
                            for(Tarea tarea : construccion.arma){
                                salida.println("arma "+ tarea.cuentaatras);
                            }

                            //tipo ARMADURA
                            for(Tarea tarea : construccion.armadura){
                                salida.println("armadura "+ tarea.cuentaatras);
                            }

                            //tipo MOVIMIENTO
                            for(Tarea tarea : construccion.movimientos){
                                salida.println("movimiento " + tarea.idRegion + " " + tarea.idRegionDestino + " " + tarea.cuentaatras);
                            }

                            salida.println("null");
                        }
                    }
                }  //fin de produccion
            } //fin de texto != null
        }//fin de bucle infinito
    }//fin run

}//fin conexioncliente



public static void main(String args[]) throws SQLException{
    new Yserver();
}

}

//Clase actualizar oro


