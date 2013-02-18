/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Yoshi;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrador
 */
public class Tarea{
    boolean activa;
    public boolean hecha;

    public int idConstruccion;
    public String tipoUnidad;
    public int cuentaatras;
    public int idRegion;
    public int idUnidad;
    public int cantidad;
    public int idTropa;
    public int idRegionDestino;
    ArrayList<Integer> pelotones = new ArrayList<Integer>();

    public String usuario;
    public int tiempo;

    javax.swing.Timer timer;

    //Construcctor 1
    public Tarea(int idConstruccion, final String tipoUnidad, int idUni, int idReg, int canti, int tiemp){
        activa = false; //constructor de la clase tarea
                        //que cuenta con un tiempo de espera una cantidad de repeticion
                        //y la sentencia que debe ser ejecutada pasado ese tiempo
        hecha = false;
        this.idConstruccion = idConstruccion;
        this.tipoUnidad = tipoUnidad;
        idRegion = idReg;
        idUnidad = idUni;

        this.tiempo = tiemp*60;
        cuentaatras = this.tiempo;

        this.cantidad = canti;


        timer = new javax.swing.Timer(1000, new ActionListener() { //cada segundo decrementa el tiempo en 1
            public void actionPerformed(ActionEvent e) {           //asi nos facilita el trabajo a la hora de mostrar el tiempo
                if(cantidad > 0 && cuentaatras > 0){
                    cuentaatras--;                                     //que falta para completar la tarea
                    System.out.println(cuentaatras);
                }
                else if(cuentaatras == 0 && cantidad > 0)
                {
                        cantidad--;
                        if(tipoUnidad.equalsIgnoreCase("edificio")){
                            accionInsertarEdificio(idRegion, idUnidad);
                        }
                        else{
                            accionInsertarUnidad(idRegion, idUnidad);
                        }


                        if(cantidad > 0)
                            cuentaatras = tiempo;
                }
                
                if(cuentaatras == 0 && cantidad == 0){
                    System.out.println("Timer parado.");
                    hecha = true;
                    timer.stop();
                    activa = false;
                }
            }
        });

    }

    //Constructor 2 para investigaciones

    public Tarea(int idInvestigacion, final String usuario, final String tipoUnidad, int tiemp){
        activa = false; //constructor de la clase tarea
                        //que cuenta con un tiempo de espera una cantidad de repeticion
                        //y la sentencia que debe ser ejecutada pasado ese tiempo
        hecha = false;

        this.idConstruccion = idInvestigacion;
        this.usuario = usuario;
        this.tipoUnidad = tipoUnidad;

        cantidad=1;
        this.tiempo = tiemp*60;
        cuentaatras = this.tiempo;


        timer = new javax.swing.Timer(1000, new ActionListener() { //cada segundo decrementa el tiempo en 1
            public void actionPerformed(ActionEvent e) {           //asi nos facilita el trabajo a la hora de mostrar el tiempo
                if(cuentaatras > 0){
                    cuentaatras--;                                     //que falta para completar la tarea
                    System.out.println(cuentaatras);
                }
                else if(cuentaatras == 0)
                {
                    cantidad = 0;
                    accionSubirNivel(usuario, tipoUnidad);

                    System.out.println("Timer parado.");
                    hecha = true;
                    timer.stop();
                    activa = false;
                }
            }
        });

    }

    //Constructor para movimientos
    public Tarea(int idMovimiento, String usuario, int idTropa,int idUnidad, int idOrigen, int idDestino, int canti){
        activa = false;
        hecha = false;

        idConstruccion = idMovimiento;
        this.usuario = usuario;
        this.idTropa = idTropa;
        pelotones.add(idUnidad);
        this.idRegion = idOrigen;
        this.idRegionDestino = idDestino;
        pelotones.add(canti);

        cantidad = 1;

        int valor = idOrigen - idDestino;
        if(idDestino > idOrigen)
            valor = valor * -1;

        cuentaatras = valor * 60;

        System.out.println("Tarea de movimiento creada. Cuentaatras " + cuentaatras);

        timer = new javax.swing.Timer(1000, new ActionListener() { //cada segundo decrementa el tiempo en 1
            public void actionPerformed(ActionEvent e) {           //asi nos facilita el trabajo a la hora de mostrar el tiempo
                if(cuentaatras > 0){
                    cuentaatras--;                                     //que falta para completar la tarea
                    System.out.println(cuentaatras);
                }
                else if(cuentaatras == 0)
                {
                    cantidad = 0;
                    accionEjecutarMovimiento();

                    System.out.println("Timer parado.");
                    hecha = true;
                    timer.stop();
                    activa = false;
                }
            }
        });
    }


    public void accionEjecutarMovimiento(){
        try {
                    Connection conn = Conectar.conectar();

                    Statement st = conn.createStatement();

                    ResultSet rs = st.executeQuery("SELECT propietario FROM regiones WHERE idRegion = "+idRegionDestino);
                    String propietario = "nulo";
                    if(rs.next()){
                        propietario = rs.getString(1);
                    }

                    //Si el territorio esta vacio****************************************************************************
                    if(propietario.equalsIgnoreCase("Deshabitado")){
                        String nombre = generaNombre();

                        st.executeQuery("UPDATE regiones SET nombreRegion = '" + nombre + "', propietario = '"+usuario+"' WHERE idRegion = "+idRegionDestino);
                        st.executeQuery("INSERT INTO edificiosregion VALUES ("+idRegionDestino+", "+ 1400 +", "+ 1 +")");
                        for(int i=0; i<pelotones.size(); i=i+2){
                            st.executeQuery("INSERT INTO despliegues VALUES ("+idRegionDestino+", "+pelotones.get(i)+", "+pelotones.get(i+1)+")");
                        }
                        st.executeQuery("DELETE movimientos WHERE idTropa = "+idTropa);
                        st.executeQuery("commit");

                        System.out.println("Query ejecutado.");
                    }

                    // Si el territorio te pertenece *******************************************************************************
                    else if(propietario.equalsIgnoreCase(usuario))
                    {
                        ResultSet rs2 = st.executeQuery("select * from despliegues where idRegion = " + idRegionDestino);
                        ArrayList<Despliegue> despliegues = new ArrayList<Despliegue>();
                        while(rs2.next()){
                            despliegues.add(new Despliegue(rs2.getInt(1), rs2.getInt(2), rs2.getInt(3)));
                        }
                        Boolean encontrado = false;

                        for(int i=0 ; i<pelotones.size() ; i=i+2){
                            encontrado = false;

                            for(Despliegue despliegue : despliegues){
                                if(pelotones.get(i).equals(despliegue.getIdUnidad())){
                                    st.executeQuery("update despliegues set cantidad = " + (despliegue.getCantidad() + pelotones.get(i+1)) + " where idRegion = " +
                                                     idRegionDestino + " and idUnidad = " + despliegue.getIdUnidad());
                                    encontrado = true;
                                }
                            }
                            if(!encontrado){
                                st.executeQuery("INSERT INTO despliegues VALUES (" + idRegionDestino + ", " + pelotones.get(i) + ", " + pelotones.get(i+1) + ")");
                            }
                        }
                        st.executeQuery("DELETE movimientos WHERE idTropa = "+idTropa);
                        st.executeQuery("commit");

                        System.out.println("Query ejecutado.");
                        rs2.close();
                    }

                    //Si es un territorio enemigo ( COMBATE ) *****************************************************************************
                    else{
                        int ataque = 0;
                        int defensa = 0;
                        int defenemiga = 0;
                        int ataenemigo = 0;

                        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                        java.util.Date fecha = new java.util.Date();
                        String fechahora = formato.format(fecha);

                        //Recolectamos el ataque y la defensa del enemigo
                        ResultSet rs3 = st.executeQuery("select * from despliegues where idRegion = " + idRegionDestino);
                        ArrayList<Despliegue> despliegues = new ArrayList<Despliegue>();
                        while(rs3.next()){
                            despliegues.add(new Despliegue(rs3.getInt(1), rs3.getInt(2), rs3.getInt(3)));
                        }

                        for(Despliegue despliegue : despliegues){
                            if(despliegue.getIdUnidad() == 4100){
                                ataenemigo = ataenemigo + (15 * despliegue.getCantidad());
                                defenemiga = defenemiga + (30 * despliegue.getCantidad());
                            }
                            else if(despliegue.getIdUnidad() == 4101){
                                ataenemigo = ataenemigo + (25 * despliegue.getCantidad());
                                defenemiga = defenemiga + (50 * despliegue.getCantidad());
                            }
                            else if(despliegue.getIdUnidad() == 4102){
                                ataenemigo = ataenemigo + (55 * despliegue.getCantidad());
                                defenemiga = defenemiga + (20 * despliegue.getCantidad());
                            }
                            else if(despliegue.getIdUnidad() == 4103){
                                ataenemigo = ataenemigo + 800;
                                ataenemigo = ataenemigo * ((ataenemigo*10) / 100);
                                defenemiga = defenemiga + 600;
                            }
                            else if(despliegue.getIdUnidad() == 4104){
                                ataenemigo = ataenemigo + (400 * despliegue.getCantidad());
                                defenemiga = defenemiga + (100 * despliegue.getCantidad());
                            }
                            else if(despliegue.getIdUnidad() == 4102){
                                ataenemigo = ataenemigo + (50 * despliegue.getCantidad());
                                defenemiga = defenemiga + (800 * despliegue.getCantidad());
                            }
                        }


                        //Recolectamos informacion de edificios defensivos
                        ResultSet rs4 = st.executeQuery("select * from edificiosregion where idRegion = " + idRegionDestino);
                        ArrayList<EdificiosRegion> despliegues2 = new ArrayList<EdificiosRegion>();
                        while(rs4.next()){
                            despliegues2.add(new EdificiosRegion(rs4.getInt(1), rs4.getInt(2), rs4.getInt(3)));
                        }

                        for(EdificiosRegion despliegue2 : despliegues2){
                            if(despliegue2.idEdificio == 1300){
                                ataenemigo = ataenemigo + (200 * despliegue2.cantidad);
                                defenemiga = defenemiga + (1000 * despliegue2.cantidad);
                            }
                            else if(despliegue2.idEdificio == 1301){
                                ataenemigo = ataenemigo + (500 * despliegue2.cantidad);
                                defenemiga = defenemiga + (500 * despliegue2.cantidad);
                            }
                        }

                        //Recolectamos ataque y defensa nuestra***************************
                        for(int i = 0 ; i < pelotones.size() ; i=i+2){
                            if(pelotones.get(i) == 4100){
                                ataque = ataque + (15 * pelotones.get(i+1));
                                defensa = defensa + (30 * pelotones.get(i+1));
                            }
                            else if(pelotones.get(i) == 4101){
                                ataque = ataque + (25 * pelotones.get(i+1));
                                defensa = defensa + (50 * pelotones.get(i+1));
                            }
                            else if(pelotones.get(i) == 4102){
                                ataque = ataque + (55 * pelotones.get(i+1));
                                defensa = defensa + (20 * pelotones.get(i+1));
                            }
                            else if(pelotones.get(i) == 4103){
                                ataque = ataque + 800;
                                ataque = ataque * ((ataenemigo*10) / 100);
                                defensa = defensa + 600;
                            }
                            else if(pelotones.get(i) == 4104){
                                ataque = ataque + (400 * pelotones.get(i+1));
                                defensa = defensa + (100 * pelotones.get(i+1));
                            }
                            else if(pelotones.get(i) == 4100){
                                ataque = ataque + (50 * pelotones.get(i+1));
                                defensa = defensa + (800 * pelotones.get(i+1));
                            }
                        }

                        int ataque1 = defenemiga - ataque;
                        int ataque2 = defensa - ataenemigo;
                        //Capturamos el nick del enemigo***************
                        String enemigo = "Deshabitado";
                            ResultSet rsenemigo = st.executeQuery("select propietario from regiones where idRegion = " + idRegionDestino);
                            if(rsenemigo.next()){
                                enemigo = rsenemigo.getString(1);
                            }

                        // 1 ---  Si el enemigo tiene vida inferior a 0 y tu no
                        if(ataque1 <= 0 && ataque2 > 0){
                            //Se conquista el territorio, se compra aldea y se recolocan las unidades sobrantes dependiendo del ataque 2
                            
                            st.executeQuery("UPDATE regiones SET propietario = '" + usuario + "' WHERE idRegion = " + idRegionDestino);

                            st.executeQuery("DELETE edificiosregion WHERE idRegion = " + idRegionDestino + " AND idEdificio != " + 1400);

                            st.executeQuery("DELETE despliegues WHERE idRegion = " + idRegionDestino);

                            st.executeQuery("DELETE construcciones WHERE idRegion = " + idRegionDestino);


                            st.executeQuery("DELETE movimientos WHERE idTropa = " + idTropa);

                            //Escribimos un mensaje al usuario con los resultados de la batalla
                            st.executeQuery("INSERT INTO mensajes VALUES (s_mensajes.NEXTVAL, 'System', '" + usuario + "', 'Informe de Batalla', '"+
                                    "¡Enhorabuena!#Has ganado la batalla en el territorio [" + idRegionDestino + "].#Daño Recibido-> " + ataenemigo +
                                    ".#Daño causado-> " + ataque + ".', '"+ fechahora +"')");

                            //Escribimos un mensaje al enemigo con los resultados de la batalla
                            st.executeQuery("INSERT INTO mensajes VALUES (s_mensajes.NEXTVAL, 'System', '" + enemigo + "', 'Informe de Batalla', '"+
                                    "Lo siento.#Has perdido la batalla en el territorio [" + idRegionDestino + "].#Daño Recibido-> " + ataque +
                                    ".#Daño causado-> " + ataenemigo + ".', '"+ fechahora +"')");

                            System.out.println(ataque2);
                            //Algoritmo para ver el porcentaje de unidades que me quedan
                            int porcperdido = (int) (ataque2*100) / defensa;

                            for(int i = 0 ; i < pelotones.size() ; i = i+2){
                                pelotones.set(i+1, (int)(pelotones.get(i+1)*porcperdido)/100);
                            }
                            for(int i = 0 ; i < pelotones.size() ; i = i+2){
                                st.executeQuery("INSERT INTO despliegues VALUES (" + idRegionDestino + ", " + pelotones.get(i) + ", " + pelotones.get(i+1) + ")");
                            }
                        }

                        // 2 --- Los 2 tienen la vida a 0 o menos
                        else if(ataque1 <= 0 && ataque2 <= 0)
                        {
                            //El enemigo se queda con el territorio pero pierde todos los edificios y se recolocan unidades dependiendo de ataque1.

                            st.executeQuery("DELETE edificiosregion WHERE idRegion = " + idRegionDestino + " AND idEdificio != " + 1400);

                            st.executeQuery("DELETE movimientos WHERE idTropa = " + idTropa);

                            st.executeQuery("DELETE despliegues WHERE idRegion = " + idRegionDestino);

                            //Escribimos un mensaje al usuario con los resultados de la batalla
                            st.executeQuery("INSERT INTO mensajes VALUES (s_mensajes.NEXTVAL, 'System', '" + usuario + "', 'Informe de Batalla', '"+
                                    "Lo siento.#Has perdido la batalla en el territorio [" + idRegionDestino + "].#Daño Recibido-> " + ataenemigo +
                                    ".#Daño causado-> " + ataque + ".', '"+ fechahora +"')");

                            //Escribimos un mensaje al enemigo con los resultados de la batalla
                            st.executeQuery("INSERT INTO mensajes VALUES (s_mensajes.NEXTVAL, 'System', '" + enemigo + "', 'Informe de Batalla', '"+
                                    "¡Enhorabuena!#Has conseguido defender tu territorio ubicado en [" + idRegionDestino + "].#"+
                                    "Aunque has perdido todas tus tropas y edificios.#Daño Recibido-> " + ataque +
                                    ".#Daño causado-> " + ataenemigo + ".', '"+ fechahora +"')");
                        }

                        // 3 -- Tu pierdes la batalla
                        else if(ataque1 > 0 && ataque2 <= 0)
                        {
                            //El enemigo recoloca unidades y tu lo pierdes todo
                            st.executeQuery("DELETE movimientos WHERE idTropa = " + idTropa);


                            int porcperdido = (int) (ataque1*100) / defenemiga;

                            for(Despliegue despliegue : despliegues){
                                despliegue.setCantidad( (int)(despliegue.getCantidad()*porcperdido) / 100);
                            }
                            for(Despliegue despliegue : despliegues){
                                st.executeQuery("UPDATE despliegues SET cantidad = " + despliegue.getCantidad() + " WHERE idRegion = " + idRegionDestino +
                                                " AND idUnidad = " + despliegue.getIdUnidad() );
                            }

                            //Escribimos un mensaje al usuario con los resultados de la batalla
                            st.executeQuery("INSERT INTO mensajes VALUES (s_mensajes.NEXTVAL, 'System', '" + usuario + "', 'Informe de Batalla', '"+
                                    "Lo siento.#Has perdido la batalla en el territorio [" + idRegionDestino + "].#Daño Recibido-> " + ataenemigo +
                                    ".#Daño causado-> " + ataque + ".', '"+ fechahora +"')");

                            //Escribimos un mensaje al enemigo con los resultados de la batalla
                            st.executeQuery("INSERT INTO mensajes VALUES (s_mensajes.NEXTVAL, 'System', '" + enemigo + "', 'Informe de Batalla', '"+
                                    "¡Enhorabuena!#Has conseguido defender tu territorio ubicado en [" + idRegionDestino + "].#Daño Recibido-> " + ataque +
                                    ".#Daño causado-> " + ataenemigo + ".', '"+ fechahora +"')");

                        }
                        // 4 --- Los 2 quedan con vida**************************************************************
                        else if(ataque1 > 0 && ataque2 > 0)
                        {
                            //Se recolocan unidades en los 2.

                            st.executeQuery("DELETE movimientos WHERE idTropa = " + idTropa);


                            int porcperdido1 = (int) (ataque1*100) / defenemiga;

                            for(Despliegue despliegue : despliegues){
                                despliegue.setCantidad( (int)(despliegue.getCantidad()*porcperdido1) / 100);
                            }
                            for(Despliegue despliegue : despliegues){
                                st.executeQuery("UPDATE despliegues SET cantidad = " + despliegue.getCantidad() + " WHERE idRegion = " + idRegionDestino +
                                                " AND idUnidad = " + despliegue.getIdUnidad() );
                            }

                            int porcperdido2 = (int) (ataque2*100) / defensa;

                            for(int i = 0 ; i < pelotones.size() ; i = i+2){
                                pelotones.set(i+1, (int)(pelotones.get(i+1)*porcperdido2)/100);
                            }
                            for(int i = 0 ; i < pelotones.size() ; i = i+2){
                                st.executeQuery("UPDATE despliegues SET cantidad = (SELECT cantidad+" + pelotones.get(i+1) + " FROM despliegues WHERE idRegion = " +
                                                idRegion + " AND idUnidad = " + pelotones.get(i) + ") WHERE idRegion = " + idRegion + " AND idUnidad = " + pelotones.get(i));
                            }

                            //Escribimos un mensaje al usuario con los resultados de la batalla
                            st.executeQuery("INSERT INTO mensajes VALUES (s_mensajes.NEXTVAL, 'System', '" + usuario + "', 'Informe de Batalla', '"+
                                    "Lo siento.#Has perdido la batalla en la region [" + idRegionDestino + "].#"+
                                    "Aunque has recuperado algunas unidades.#Daño Recibido-> " + ataenemigo +
                                    ".#Daño causado-> " + ataque + ".', '"+ fechahora +"')");

                            //Escribimos un mensaje al enemigo con los resultados de la batalla
                            st.executeQuery("INSERT INTO mensajes VALUES (s_mensajes.NEXTVAL, 'System', '" + enemigo + "', 'Informe de Batalla', '"+
                                    "¡Enhorabuena!#Has conseguido defender tu territorio ubicado en [" + idRegionDestino + "].#Daño Recibido-> " + ataque +
                                    ".#Daño causado-> " + ataenemigo + ".', '"+ fechahora +"')");
                            
                        }

                        
                        st.executeQuery("commit");
                        rs3.close();
                        rs4.close();
                        rsenemigo.close();
                    }

                    st.close();
                    rs.close();
                    conn.close();
        }catch(SQLException e){
            System.out.println(e.getMessage() + " Fallo ejecutar movimiento.");
        }
    }

    public static String generaNombre(){
        Random r=new Random();
        int limit=r.nextInt(4);
        int x=r.nextInt(25)+65;
        char c=(char)x;
        String nombre="";
        nombre=nombre.concat(""+c);
        char t[]={'a','e','i','o','u'};

        for(int i=0;i<=limit;i++){
            x=r.nextInt(5);
            nombre=nombre.concat(""+t[x]);
            x=r.nextInt(25)+97;
            c=(char)x;
            nombre=nombre.concat(""+c);
        }
        return(nombre);
    }


    public void accionSubirNivel(String usuario, String tipoUnidad){
        String campo;
        if(tipoUnidad.equalsIgnoreCase("tecnologia"))
            campo = "nivel";
        else if (tipoUnidad.equalsIgnoreCase("arma"))
            campo = "arma";
        else
            campo = "armadura";


        try {
                    Connection conn = Conectar.conectar();

                    Statement st = conn.createStatement();

                    st.executeQuery("UPDATE usuarios SET " + campo + "=(SELECT " + campo + "+1 FROM usuarios WHERE nick='"
                                    + usuario + "') WHERE nick='" + usuario + "'");

                    st.executeQuery("DELETE FROM investigaciones WHERE idInvestigacion = " + idConstruccion);

                    st.executeQuery("commit");

                    st.close();
                    conn.close();

                } catch (SQLException ex) {
                        System.out.println(ex.getMessage()+ ex.getErrorCode() + " :: Fallo subir nivel investigacion");

                }
    }


    public void accionInsertarUnidad(int idReg, int idUni) { //aqui se ejecuta el estamento
                try {                                           //de insertar edificios
                    Connection conn = Conectar.conectar();

                    Statement st = conn.createStatement(); //añade 1
                    System.out.println(idReg + "    " + idUni);
                    st.executeQuery("INSERT INTO despliegues VALUES (" + idReg + ", " + idUni + ", " + 1 + ")");

                    if(cuentaatras == 0 && cantidad > 0) //y decrementa las tareas pendientes en la base de datos
                        st.executeQuery("UPDATE construcciones SET cantidad = (SELECT cantidad-1 FROM construcciones WHERE idConstruccion = "+ idConstruccion +
                                ") WHERE idConstruccion = " + idConstruccion);

                    if(cuentaatras == 0 && cantidad == 0){
                        System.out.println(idConstruccion);
                        st.executeQuery("DELETE FROM construcciones WHERE idConstruccion = " + idConstruccion);
                    }

                    st.executeQuery("commit");

                    st.close();
                    conn.close();

                } catch (SQLException ex) {
                        System.out.println(ex.getMessage()+ ex.getErrorCode() + " :: Fallo de conexion del temporizador");

                        if(ex.getErrorCode() == 00001){
                            Connection conn;
                            try {
                                conn = Conectar.conectar();
                                //Si no se ha podido ejecutar la insercion porque la clave ya existe, se actualiza la tabla
                                Statement st = conn.createStatement();
                                st.executeQuery("UPDATE despliegues SET cantidad = (SELECT cantidad+1 FROM despliegues WHERE idRegion = " + idReg +
                                                "AND idUnidad = " + idUni + ") WHERE idRegion = "+idReg+" AND idUnidad = "+idUni);

                                if(cuentaatras == 0 && cantidad > 0) //y decrementa las tareas pendientes en la base de datos
                                    st.executeQuery("UPDATE construcciones SET cantidad = (SELECT cantidad-1 FROM construcciones WHERE idConstruccion = "+ idConstruccion +
                                            ") WHERE idConstruccion = " + idConstruccion);

                                if(cuentaatras == 0 && cantidad == 0){
                                    System.out.println(idConstruccion);
                                    st.executeQuery("DELETE FROM construcciones WHERE idConstruccion = " + idConstruccion);
                                }

                                st.executeQuery("commit");

                                st.close();
                                conn.close();

                            } catch (SQLException ex1) {
                                System.out.println(ex1.getMessage() + " :: Fallo en catch de insertar");
                            }
                        }
                }

            }


    public void accionInsertarEdificio(int idReg, int idUni) { //aqui se ejecuta el estamento
                try {                                           //de insertar edificios
                    Connection conn = Conectar.conectar();

                    Statement st = conn.createStatement(); //añade 1
                    System.out.println(idReg + "    " + idUni);
                    st.executeQuery("INSERT INTO edificiosregion VALUES (" + idReg + ", " + idUni + ", " + 1 + ")");
                    
                    if(cuentaatras == 0 && cantidad > 0) //y decrementa las tareas pendientes en la base de datos
                        st.executeQuery("UPDATE construcciones SET cantidad = (SELECT cantidad-1 FROM construcciones WHERE idConstruccion = "+ idConstruccion +
                                ") WHERE idConstruccion = " + idConstruccion);

                    if(cuentaatras == 0 && cantidad == 0){
                        System.out.println(idConstruccion);
                        st.executeQuery("DELETE FROM construcciones WHERE idConstruccion = " + idConstruccion);
                    }

                    st.executeQuery("commit");

                    st.close();
                    conn.close();

                } catch (SQLException ex) {
                        System.out.println(ex.getMessage()+ ex.getErrorCode() + " :: Fallo de conexion del temporizador");

                        if(ex.getErrorCode() == 00001){
                            Connection conn;
                            try {
                                conn = Conectar.conectar();
                                //Si no se ha podido ejecutar la insercion porque la clave ya existe, se actualiza la tabla
                                Statement st = conn.createStatement();
                                st.executeQuery("UPDATE edificiosregion SET cantidad = (SELECT cantidad+1 FROM edificiosregion WHERE idRegion = " + idReg +
                                                "AND idEdificio = " + idUni + ") WHERE idRegion = "+idReg+" AND idEdificio = "+idUni);

                                if(cuentaatras == 0 && cantidad > 0) //y decrementa las tareas pendientes en la base de datos
                                    st.executeQuery("UPDATE construcciones SET cantidad = (SELECT cantidad-1 FROM construcciones WHERE idConstruccion = "+ idConstruccion +
                                            ") WHERE idConstruccion = " + idConstruccion);

                                if(cuentaatras == 0 && cantidad == 0){
                                    System.out.println(idConstruccion);
                                    st.executeQuery("DELETE FROM construcciones WHERE idConstruccion = " + idConstruccion);
                                }
                                    
                                st.executeQuery("commit");

                                st.close();
                                conn.close();

                            } catch (SQLException ex1) {
                                System.out.println(ex1.getMessage() + " :: Fallo en catch de insertar");
                            }
                        }
                }

            }

    public void start(){
        activa = true;

        if(cantidad > 0 && timer.isRunning()==false){
            timer.start();
            System.out.println("Timer funcionando....");
        }
    }
    
    
    public boolean esPeloton(int idUni){
        boolean enrrolado=false;
        for (int i=0; i<pelotones.size(); i=i+2){
            if(pelotones.get(i)==idUni){
                enrrolado = true;
            }
        }
        return enrrolado;
    }

    
    public boolean estaActiva(){
        return activa;
    }

    public boolean estaHecha(){
        return hecha;
    }

    public int getCuentaAtras(){
        return cuentaatras;
    }


}

