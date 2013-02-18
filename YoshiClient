/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * Ynavi.java
 *
 * Created on 08-abr-2011, 22:35:10
 */

package Yoshi;
import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.net.*;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.JPopupMenu.Separator;
import javax.swing.border.Border;

/**
 *
 * @author Administrador
 */
public class Ynavi extends javax.swing.JFrame {
    
    Socket socket;
    public static PrintWriter salida;
    public static BufferedReader entrada;

    static Usuario usuario;
    public JTextField coordenadas;
    public static int idCurrentRegion;
    static int currentBoton;
    public static boolean heroe;
    ArrayList<JLabel> siluetas;
    public static ArrayList<Region> regiones;
    public static ArrayList<Region> regiones2;
    public static ArrayList<Edificio> edificios;
    public ArrayList<EdificiosRegion> construidos;
    public static ArrayList<Unidad> unidades;
    public ArrayList<Despliegue> despliegues;
    public static ArrayList<Tecnologia> tecnologias;
    public static ArrayList<Arma> armas;
    public static ArrayList<Armadura> armaduras;
    public static ArrayList<Movimiento> movimientos;
    public static ArrayList<Correo> correos;
    public static ArrayList<Usuario> usuarios;
    ArrayList<JRadioButton> selectores = new ArrayList<JRadioButton>();
    public static int regionInicio, regionFinal;
    ArrayList<Molde5ataq> moldes;
    JTextArea info;

    boolean conectado;

    /** Creates new form Ynavi */
    public Ynavi(){}

    public Ynavi (String jugador){
        conectado = false;
        initComponents();
        this.setResizable(false);
        this.setSize(950, 700);
        this.setLocationRelativeTo(null);
        this.setTitle("Yoshimitsu");


        
        cabezera.setHorizontalTextPosition(JLabel.CENTER);
        fondo.setUrlImagen(getClass().getResource("/imagenes/fondo.gif"));
        fondo.setAjustar(true);
        scroll.getViewport().setOpaque(false);
        scroll.setOpaque(false);
        panelScroll.setOpaque(false);
        

        //AÑADE ACCION AL CERRAR VENTANA
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
           public void windowClosing(WindowEvent e){
                
             if (conectado){
                salida.println("salir");
                desconectar();
             }
             System.exit(0);
           }
        });
        //botones de salida
        addMouseListener(bdesconectar);
        addMouseListener(bsalir);

        conectar();

        extraerDatos(jugador);
        jgd2.setText(usuario.getNick());
        oro2.setText(Integer.toString(usuario.getOro()));
        currentBoton = 1;
        //inicializa todos los territorios
        listarRegiones();
        listarTodasRegiones();
        listarInvestigaciones();
        heroe = false;
        esHeroe();
        regionInicio = 0;
        regionFinal = 20;

        siluetas.get(0).setOpaque(true);
        setCurrentRegion(siluetas.get(0).getText());

        panelCentro.setPreferredSize(new Dimension(620, 519));
        panelCentro.setTran(0.7f);

        /*panelCentro.addMouseMotionListener(new MouseMotionListener(){
            public void mouseDragged(MouseEvent e) {
            }
            public void mouseMoved(MouseEvent e) {
                panelCentro.repaint();
            }
        });*/
        //UNA PARANOIA PARA QUE REPINTE AL MOVER LA BARRA DE DESPLAZAMIENTO
        AdjustmentListener evento = new AdjustmentListener(){
            public void adjustmentValueChanged(AdjustmentEvent e) {
                repaint();
            }
        };
        JScrollBar vBar = centro.getVerticalScrollBar();
        vBar.addAdjustmentListener(evento);

            
        bienvenida();

        //inicializa los edificios
        listarEdificios();
        listarUnidades(); //inicializa las unidades
        
        pack();
    }


    private void bienvenida(){ //UN MENSAJE DE BIENVENIDA CADA VEZ QUE NOS LOGUEAMOS
        panelCentro.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        String cadena = " Yoshimitsu es un juego de estrategia ambientado"
                + " en el antigua japon, donde ninjas y\n samurais luchan"
                + " por la dominacion del territorio.\n"
                + " Miles de jugadores pueden competir al mismo tiempo,"
                + " empuña a Yoshimitsu y\n domina tu nueva era.";

        cabezera.setText("- Bienvenida -");
        JLabel l1 = new JLabel(" Bienvenido a Yoshimitsu");
        l1.setAlignmentY(0);
        l1.setSize(300, 50);
        l1.setFont(new Font("fuente1", Font.ROMAN_BASELINE + Font.ITALIC, 30));
        l1.setForeground(new Color(204,153,255));

        
        c.gridy = 0;
        c.insets = new Insets(40,0,0,0);

        panelCentro.add(l1, c);

        JTextArea tf = new JTextArea(cadena, 30, 15);
        tf.setFont(new Font("fuente2", Font.ROMAN_BASELINE, 14));
        tf.setOpaque(false);
        tf.setForeground(Color.white);

        c.anchor = c.NORTH;
        c.gridy = 1;
        c.weighty = 1.5;
        panelCentro.add(tf, c);
    }

    private void actualizarOro(){
        extraerDatos(usuario.getNick());
        oro2.setText(Integer.toString(usuario.getOro()));
        esHeroe();
    }

    private void setCurrentRegion(String labelSilueta){ //ESTABLECE LA REGION SELECCIONADA COMO REGION PRINCIPAL
        Mensaje idregion = new Mensaje(labelSilueta);
        idCurrentRegion = Integer.parseInt(idregion.getPalabra(1).substring(1, idregion.getPalabra(1).length()-1));
    }

    public static void setOro(String oro){
        oro2.setText(oro);
    }
    //EXTRAE DATOS DE JUGADOR
    private void extraerDatos(String jugador){ //EXTRAE LOS DATOS DE USUARIO Y CREA EL OBJETO USUARIO
        salida.println("extraer " + jugador);

        try {
            Mensaje mensaje = new Mensaje(entrada.readLine());
            usuario = new Usuario(mensaje.getPalabra(0), mensaje.getPalabra(1), mensaje.getPalabra(2), Integer.parseInt(mensaje.getPalabra(3)),
                    Integer.parseInt(mensaje.getPalabra(4)), Integer.parseInt(mensaje.getPalabra(5)), Integer.parseInt(mensaje.getPalabra(6)));

        } catch (IOException ex) {
            System.out.println(ex.getMessage() + " Fallo lectura de datos de usuario.");
        }

    }

    private void esHeroe(){
        salida.println("SELECT * FROM despliegues WHERE idRegion IN (SELECT idRegion FROM regiones WHERE propietario = '" + usuario + "')");

        String texto;
        try {
            while (!(texto = entrada.readLine()).equalsIgnoreCase("null")) {
                Mensaje consulta = new Mensaje(texto);
                if(Integer.parseInt(consulta.getPalabra(1)) == 4103)
                    heroe = true;
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage() + " Fallo es Heroe.");
        }
    }


    private void listarInvestigaciones(){

        tecnologias = new ArrayList<Tecnologia>();  //capturamos todas las tecnologias
        salida.println("SELECT * FROM tecnologias WHERE idTecnologia != 0");
        String texto;
        try { //HACEMOS LA CONSULTA
            while (!(texto = entrada.readLine()).equalsIgnoreCase("null")) {
                Mensaje consulta = new Mensaje(texto);
                consulta.desencadenar();
                tecnologias.add(new Tecnologia(Integer.parseInt(consulta.getPalabra(0)), consulta.getPalabra(1), consulta.getPalabra(2),
                                Integer.parseInt(consulta.getPalabra(3)), Integer.parseInt(consulta.getPalabra(4)), Integer.parseInt(consulta.getPalabra(5))));
            }
        } catch (IOException ex) {
            System.out.println("Fallo leer entrada tecnologias.");
            JOptionPane.showMessageDialog(this, "Has sido desconectado del servidor.");
            new Ylogin().setVisible(true);
            salir();
        }


        armas = new ArrayList<Arma>(); //capturamos todas las armas
        salida.println("SELECT * FROM armas");

        try { //HACEMOS LA CONSULTA
            while (!(texto = entrada.readLine()).equalsIgnoreCase("null")) {
                Mensaje consulta = new Mensaje(texto);
                consulta.desencadenar();
                armas.add(new Arma(Integer.parseInt(consulta.getPalabra(0)), consulta.getPalabra(1), consulta.getPalabra(2),
                                Double.parseDouble(consulta.getPalabra(3)), Integer.parseInt(consulta.getPalabra(4)), Integer.parseInt(consulta.getPalabra(5)),
                                Integer.parseInt(consulta.getPalabra(6)),Integer.parseInt(consulta.getPalabra(7))));
            }
        } catch (IOException ex) {
            System.out.println("Fallo leer entrada armas.");
        }



        armaduras = new ArrayList<Armadura>(); //capturamos todas las armaduras
        salida.println("SELECT * FROM armaduras");

        try { //HACEMOS LA CONSULTA
            while (!(texto = entrada.readLine()).equalsIgnoreCase("null")) {
                Mensaje consulta = new Mensaje(texto);
                consulta.desencadenar();
                armaduras.add(new Armadura(Integer.parseInt(consulta.getPalabra(0)), consulta.getPalabra(1), consulta.getPalabra(2),
                                Double.parseDouble(consulta.getPalabra(3)), Integer.parseInt(consulta.getPalabra(4)), Integer.parseInt(consulta.getPalabra(5)),
                                Integer.parseInt(consulta.getPalabra(6)),Integer.parseInt(consulta.getPalabra(7))));
            }
        } catch (IOException ex) {
            System.out.println("Fallo leer entrada armaduras.");
        }
    }



    private void listarUnidades(){
        unidades = new ArrayList();
        Mensaje mensaje = null;

        salida.println("SELECT * FROM unidades");
        String texto;
        try {
            while (!(texto = entrada.readLine()).equalsIgnoreCase("null")) {
                mensaje = new Mensaje(texto);
                mensaje.desencadenar(); //vuelve a pasar los dos puntos a espacios.

                unidades.add(new Unidad(Integer.parseInt(mensaje.getPalabra(0)), mensaje.getPalabra(1), mensaje.getPalabra(2), Integer.parseInt(mensaje.getPalabra(3)),
                        Integer.parseInt(mensaje.getPalabra(4)), Integer.parseInt(mensaje.getPalabra(5)), Integer.parseInt(mensaje.getPalabra(6)), Integer.parseInt(mensaje.getPalabra(7)),
                        Integer.parseInt(mensaje.getPalabra(8)), Integer.parseInt(mensaje.getPalabra(9))));
            }
        } catch (IOException ex) {
            System.out.println("Fallo entrada de listar unidades.");
        }


    }

    //EXTRAE LA INFORMACION DE EDIFICIOS DE LA BASE DE DATOS
    private void listarEdificios(){
        edificios = new ArrayList();
        Mensaje mensaje = null;

        salida.println("SELECT * FROM edificios");
        String texto;
        try {
            while (!(texto = entrada.readLine()).equalsIgnoreCase("null")) {
                mensaje = new Mensaje(texto);
                mensaje.desencadenar(); //transforma los dos puntos en espacios nuevamente

                if(Integer.parseInt(mensaje.getPalabra(0))!=0)
                edificios.add(new Edificio(Integer.parseInt(mensaje.getPalabra(0)), mensaje.getPalabra(1),
                              mensaje.getPalabra(2), Integer.parseInt(mensaje.getPalabra(3)), Integer.parseInt(mensaje.getPalabra(4)),
                              Integer.parseInt(mensaje.getPalabra(5)), Integer.parseInt(mensaje.getPalabra(6)), Integer.parseInt(mensaje.getPalabra(7)),
                              Integer.parseInt(mensaje.getPalabra(8)), Integer.parseInt(mensaje.getPalabra(9))));
            }
        } catch (IOException ex) {
            System.out.println("Fallo de ready en edificios.");;
        }
    }


    private void listarRegiones(){ //LISTA TODAS LAS REGIONES DE UN PROPIETARIO
        panelScroll.removeAll();
        panelScroll.validate();
        Mensaje mensaje = null;     //Y LAS MUESTRA EN EL PANEL DE LA DERECHA
        ImageIcon icon = null;      //TRANSFORMANDOLAS EN JLABELS
        int x = 10;
        regiones = new ArrayList();
        siluetas = new ArrayList();
        
        salida.println("SELECT * FROM regiones WHERE propietario = '" + usuario.getNick() + "'");
        String texto;
        try {
            while (!(texto = entrada.readLine()).equalsIgnoreCase("null")) {
                mensaje = new Mensaje(texto);
                //mensaje.desencadenar();
                    regiones.add(new Region(Integer.parseInt(mensaje.getPalabra(0)), mensaje.getPalabra(1), Integer.parseInt(mensaje.getPalabra(2)), 
                            Integer.parseInt(mensaje.getPalabra(3)), mensaje.getPalabra(4), Integer.parseInt(mensaje.getPalabra(5))));
                    
                    //ASIGNA UNA SILUETA DEPENDIENDO DE LA SUPERFICIE DE CADA PAIS
                    if (Integer.parseInt(mensaje.getPalabra(2)) < 10000) {
                        icon = new ImageIcon(getClass().getResource("/imagenes/p11.gif"));
                    } else if (Integer.parseInt(mensaje.getPalabra(2)) < 20000) {
                        icon = new ImageIcon(getClass().getResource("/imagenes/p12.gif"));
                    } else if (Integer.parseInt(mensaje.getPalabra(2)) < 30000) {
                        icon = new ImageIcon(getClass().getResource("/imagenes/p13.gif"));
                    } else if (Integer.parseInt(mensaje.getPalabra(2)) < 70000) {
                        icon = new ImageIcon(getClass().getResource("/imagenes/p14.gif"));
                    } else if (Integer.parseInt(mensaje.getPalabra(2)) < 160000) {
                        icon = new ImageIcon(getClass().getResource("/imagenes/p15.gif"));
                    }

                    //CREA LA ETIQUETA CON SUS PROPIEDADES
                    JLabel pais = new JLabel(mensaje.getPalabra(1) + " [" + mensaje.getPalabra(0) + "]", icon, JLabel.CENTER);
                    pais.setCursor(java.awt.Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    pais.setFont(new Font("Palatino Linotype", Font.BOLD, 12));
                    pais.setForeground(Color.black);
                    pais.setSize(130, 100);
                    pais.setVerticalTextPosition(JLabel.BOTTOM);
                    pais.setHorizontalTextPosition(JLabel.CENTER);
                    addMouseListener2(pais);

                    if(idCurrentRegion == Integer.parseInt(mensaje.getPalabra(0)))
                        pais.setOpaque(true);
                    panelScroll.add(pais);
                    siluetas.add(pais);
                    
                }
                } catch (IOException ex) {
                    System.out.println(ex.getMessage() + " Fallo leer region.");
                }
            
            panelScroll.setPreferredSize(new Dimension(110, regiones.size()*115));
            if (siluetas.size()<5){
                for(JLabel label : siluetas){
                    label.setLocation(0, x);
                    x += 110;
                }
            }
            else{
                for(JLabel label : siluetas){
                    label.setLocation(-10, x);
                    x += 110;
                }
            }
            pack();
        
    }


    private void listarTodasRegiones(){
        salida.println("select * from regiones");
        String texto;
        Mensaje mensaje = new Mensaje();
        regiones2 = new ArrayList<Region>();

        try { //HACEMOS LA CONSULTA
                    while(!(texto = entrada.readLine()).equalsIgnoreCase("null")){
                        mensaje = new Mensaje(texto);
                        regiones2.add(new Region(Integer.parseInt(mensaje.getPalabra(0)), mensaje.getPalabra(1), Integer.parseInt(mensaje.getPalabra(2)),
                                    Integer.parseInt(mensaje.getPalabra(3)), mensaje.getPalabra(4), Integer.parseInt(mensaje.getPalabra(5))));
                    }
         } catch(Exception e){
             System.out.println(e.getMessage() + " Fallo listar todas regiones.");
         }
    }

    //AGREGA LOS LISTENERS A LOS BOTONES DE SALIR Y DESCONECTAR
    private void addMouseListener(final Component c){

            c.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseEntered(MouseEvent e){
                c.setForeground(Color.red);
                e.getComponent().setCursor(java.awt.Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                repaint();
            }
            @Override
            public void mouseExited(MouseEvent e){
                c.setForeground(Color.white);
                repaint();
            }
            @Override
            public void mousePressed(MouseEvent e){
                if (e.getComponent() == bsalir){
                salir();
                }
                
                else if(e.getComponent() == bdesconectar)
                {
                new Ylogin().setVisible(true);
                salir();
                }
                
            }
        }
        );
    }
    
    //AGREGA LOS LISTENER DE RATON A LOS JLABEL DE LOS PAISES
    private void addMouseListener2 (final Component c){

            c.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseEntered(MouseEvent e){
                c.setForeground(Color.red);
                e.getComponent().setCursor(java.awt.Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                repaint();
            }
            @Override
            public void mouseExited(MouseEvent e){
                c.setForeground(Color.black);
                repaint();
            }
            @Override
            public void mousePressed(MouseEvent e){
                for(JLabel silueta : siluetas){ //PONE TODOS LAS SILUETAS TRANSPARENTES
                    silueta.setOpaque(false);
                }
                JLabel silueta = (JLabel)e.getComponent(); //TRANSFORMA LA LABEL SELECCIONADA A OPACA
                silueta.setOpaque(true);
                cabezera.setText(silueta.getText());    //PONE EL TITULO EN LA CABEZERA
                setCurrentRegion(silueta.getText());    //Y SELECCIONA LA REGION COMO ACTUAL
                repaint();
                if(currentBoton == 1)   //AL CAMBIAR DE PAIS, AUTOMATICAMENTE SE REALIZA LA ACCION DEL BOTON QUE ESTABA SELECCIONADO ANTERIORMENTE
                    b1.doClick();
                else if(currentBoton == 2)
                    b2.doClick();
                else if(currentBoton == 3)
                    b3.doClick();
                else if(currentBoton == 4)
                    b4.doClick();
                else if(currentBoton == 5)
                    b5.doClick();
                else if(currentBoton == 6)
                    b6.doClick();
                else if(currentBoton == 7)
                    b7.doClick();
                else if(currentBoton == 8)
                    b8.doClick();
                else if(currentBoton == 9)
                    b9.doClick();
            }
        }
        );
    }

    private void addMouseListener3(final Component c){

            c.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseEntered(MouseEvent e){
                c.setForeground(Color.red);
                e.getComponent().setCursor(java.awt.Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                repaint();
            }
            @Override
            public void mouseExited(MouseEvent e){
                c.setForeground(Color.white);
                repaint();
            }
            @Override
            public void mousePressed(MouseEvent e){
                panelCentro.removeAll();
                panelCentro.validate();
                regionInicio = 0;
                regionFinal = 20;
                listarRegiones();

                panelCentro.setLayout(new GridBagLayout()); //CREAMOS UN GESTOR DE BOLSA DE REJILLA
                GridBagConstraints c = new GridBagConstraints(); //CREAMOS Y ASIGNAMOS LAS CONSTRAINTS DE LA DISTRIBUCION
                c.insets = new Insets(0,0,20,0); //dejamos un espacio de 20 pixeles entre elementos
                c.gridy = 0;

                Molde6pais cor = (Molde6pais)e.getComponent();

                for(Correo correo : correos){
                    if(cor.id == correo.getIdCorreo()){
                        JLabel remitente = new JLabel("Remitente: ");
                        remitente.setHorizontalAlignment(remitente.LEFT);
                        remitente.setFont(new Font("Palatino Linotype", Font.BOLD, 14));
                        remitente.setForeground(Color.white);
                        panelCentro.add(remitente, c);

                        JLabel remitente2 = new JLabel();
                        remitente2.setHorizontalAlignment(remitente2.LEFT);
                        remitente2.setText(correo.getRemitente());
                        remitente2.setFont(new Font("Palatino Linotype", Font.PLAIN, 14));
                        remitente2.setForeground(Color.white);
                        panelCentro.add(remitente2, c);
                        c.gridy += 1;

                        JLabel asunto = new JLabel("Asunto: ");
                        asunto.setHorizontalAlignment(asunto.LEFT);
                        asunto.setFont(new Font("Palatino Linotype", Font.BOLD, 14));
                        asunto.setForeground(Color.white);
                        panelCentro.add(asunto, c);

                        JLabel asunto2 = new JLabel();
                        asunto2.setHorizontalAlignment(asunto2.LEFT);
                        asunto2.setText(correo.getAsunto());
                        asunto2.setFont(new Font("Palatino Linotype", Font.PLAIN, 14));
                        asunto2.setForeground(Color.white);
                        panelCentro.add(asunto2, c);
                        c.gridy += 1;

                        c.gridwidth = 3;

                        

                        JTextArea cuerpo = new JTextArea();
                        cuerpo.setDisabledTextColor(Color.white);
                        cuerpo.setFont(new Font("Palatino Linotype", Font.PLAIN, 12));
                        cuerpo.setEnabled(false);
                        cuerpo.setOpaque(false);
                        cuerpo.setText(correo.getCuerpo());
                        panelCentro.add(cuerpo, c);
                        c.gridy += 1;
                        c.gridwidth = 1;


                        JButton volver = new JButton("Volver");
                        volver.addActionListener(new ActionListener() {

                            public void actionPerformed(ActionEvent e) {
                                b7.doClick();
                            }
                        });
                        panelCentro.add(volver, c);
                    }
                }

                panelCentro.setPreferredSize(new Dimension(600, 500));
                currentBoton=7;
                actualizarOro();
                repaint();
                pack();
            }
        }
        );
    }

    public static void actualizarPantalla(){
        if(currentBoton == 1) 
                    b1.doClick();
                else if(currentBoton == 2)
                    b2.doClick();
                /*else if(currentBoton == 3)
                    b3.doClick();
                else if(currentBoton == 4)
                    b4.doClick();
                else if(currentBoton == 5)
                    b5.doClick();*/
    }

    private void conectar(){    //CONECTA EL SERVIDOR CON EL CLIENTE
        try{
            socket = new Socket("localhost",6660);
            salida = new PrintWriter(socket.getOutputStream(), true);
            entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            conectado = true;
        }catch(Exception e){
            System.out.println(e.getMessage()+" joder que mal");
        }
    }

    private void desconectar(){
        if(conectado)
        try {
           salida.close();
	         entrada.close();
	         socket.close();
	      }
	      catch( IOException e ) {
	                 System.out.println(e.getMessage());
	      }
    }
    
    private void salir(){
        salida.println("salir");
        desconectar();
        this.dispose();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        fondo = new imagenes.PanelImagen();
        panelTranslucido1 = new Yoshi.PanelTranslucido();
        titulo = new javax.swing.JLabel();
        jgd1 = new javax.swing.JLabel();
        jgd2 = new javax.swing.JLabel();
        oro1 = new javax.swing.JLabel();
        oro2 = new javax.swing.JLabel();
        bdesconectar = new javax.swing.JLabel();
        bsalir = new javax.swing.JLabel();
        panelTranslucido2 = new Yoshi.PanelTranslucido();
        b1 = new javax.swing.JButton();
        b2 = new javax.swing.JButton();
        b3 = new javax.swing.JButton();
        b4 = new javax.swing.JButton();
        b5 = new javax.swing.JButton();
        b6 = new javax.swing.JButton();
        b7 = new javax.swing.JButton();
        b8 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        b9 = new javax.swing.JButton();
        panelTranslucido3 = new Yoshi.PanelTranslucido();
        scroll = new javax.swing.JScrollPane();
        panelScroll = new javax.swing.JPanel();
        cabezera1 = new javax.swing.JLabel();
        cabezera2 = new javax.swing.JLabel();
        cabezera = new javax.swing.JLabel();
        centro = new javax.swing.JScrollPane();
        panelCentro = new Yoshi.PanelTranslucido();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        fondo.setBackground(new java.awt.Color(153, 153, 153));

        panelTranslucido1.setOpaque(false);

        titulo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Ylogo3.gif"))); // NOI18N

        jgd1.setFont(new java.awt.Font("Palatino Linotype", 0, 12));
        jgd1.setForeground(new java.awt.Color(255, 255, 255));
        jgd1.setText("Jugador:");

        jgd2.setFont(new java.awt.Font("Palatino Linotype", 0, 12));
        jgd2.setForeground(new java.awt.Color(255, 255, 255));
        jgd2.setText("jugador");

        oro1.setFont(new java.awt.Font("Palatino Linotype", 0, 12));
        oro1.setForeground(new java.awt.Color(255, 255, 255));
        oro1.setText("Oro:");

        oro2.setFont(new java.awt.Font("Palatino Linotype", 0, 12));
        oro2.setForeground(new java.awt.Color(255, 255, 255));
        oro2.setText("oro");

        bdesconectar.setBackground(new java.awt.Color(102, 102, 255));
        bdesconectar.setForeground(new java.awt.Color(255, 255, 255));
        bdesconectar.setText("Desconectar");

        bsalir.setForeground(new java.awt.Color(255, 255, 255));
        bsalir.setText("Salir");

        javax.swing.GroupLayout panelTranslucido1Layout = new javax.swing.GroupLayout(panelTranslucido1);
        panelTranslucido1.setLayout(panelTranslucido1Layout);
        panelTranslucido1Layout.setHorizontalGroup(
            panelTranslucido1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTranslucido1Layout.createSequentialGroup()
                .addComponent(titulo)
                .addGap(28, 28, 28)
                .addGroup(panelTranslucido1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jgd1)
                    .addComponent(oro1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelTranslucido1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(oro2)
                    .addGroup(panelTranslucido1Layout.createSequentialGroup()
                        .addComponent(jgd2, javax.swing.GroupLayout.DEFAULT_SIZE, 365, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bdesconectar)
                        .addGap(18, 18, 18)
                        .addComponent(bsalir)
                        .addGap(14, 14, 14)))
                .addContainerGap())
        );
        panelTranslucido1Layout.setVerticalGroup(
            panelTranslucido1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTranslucido1Layout.createSequentialGroup()
                .addGroup(panelTranslucido1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelTranslucido1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(panelTranslucido1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jgd1)
                            .addComponent(jgd2)
                            .addComponent(bdesconectar)
                            .addComponent(bsalir))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelTranslucido1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(oro1)
                            .addComponent(oro2)))
                    .addComponent(titulo))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelTranslucido2.setBackground(new java.awt.Color(153, 153, 153));

        b1.setFont(new java.awt.Font("Palatino Linotype", 0, 14));
        b1.setForeground(new java.awt.Color(153, 51, 255));
        b1.setText("Info");
        b1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b1ActionPerformed(evt);
            }
        });

        b2.setFont(new java.awt.Font("Palatino Linotype", 0, 14));
        b2.setForeground(new java.awt.Color(153, 51, 255));
        b2.setText("Edificios");
        b2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b2ActionPerformed(evt);
            }
        });

        b3.setFont(new java.awt.Font("Palatino Linotype", 0, 14)); // NOI18N
        b3.setForeground(new java.awt.Color(153, 51, 255));
        b3.setText("Unidades");
        b3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b3ActionPerformed(evt);
            }
        });

        b4.setFont(new java.awt.Font("Palatino Linotype", 0, 14)); // NOI18N
        b4.setForeground(new java.awt.Color(153, 51, 255));
        b4.setText("Investigacion");
        b4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b4ActionPerformed(evt);
            }
        });

        b5.setFont(new java.awt.Font("Palatino Linotype", 0, 14)); // NOI18N
        b5.setForeground(new java.awt.Color(153, 51, 255));
        b5.setText("Tareas");
        b5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b5ActionPerformed(evt);
            }
        });

        b6.setFont(new java.awt.Font("Palatino Linotype", 0, 14)); // NOI18N
        b6.setForeground(new java.awt.Color(153, 51, 255));
        b6.setText("Territorios");
        b6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b6ActionPerformed(evt);
            }
        });

        b7.setFont(new java.awt.Font("Palatino Linotype", 0, 14)); // NOI18N
        b7.setForeground(new java.awt.Color(153, 51, 255));
        b7.setText("Mensajes");
        b7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b7ActionPerformed(evt);
            }
        });

        b8.setFont(new java.awt.Font("Palatino Linotype", 0, 14)); // NOI18N
        b8.setForeground(new java.awt.Color(153, 51, 255));
        b8.setText("Clasificacion");
        b8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b8ActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Palatino Linotype", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("GENERAL");
        jLabel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel2.setFont(new java.awt.Font("Palatino Linotype", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("ZONAS");
        jLabel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        b9.setFont(new java.awt.Font("Palatino Linotype", 0, 14)); // NOI18N
        b9.setForeground(new java.awt.Color(153, 51, 255));
        b9.setText("Movilizar");
        b9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b9ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelTranslucido2Layout = new javax.swing.GroupLayout(panelTranslucido2);
        panelTranslucido2.setLayout(panelTranslucido2Layout);
        panelTranslucido2Layout.setHorizontalGroup(
            panelTranslucido2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTranslucido2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelTranslucido2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(b1, javax.swing.GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE)
                    .addComponent(b2, javax.swing.GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE)
                    .addComponent(b3, javax.swing.GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE)
                    .addComponent(b9, javax.swing.GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE)
                    .addComponent(b7, javax.swing.GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE)
                    .addComponent(b4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE)
                    .addComponent(b5, javax.swing.GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE)
                    .addComponent(b6, javax.swing.GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE)
                    .addComponent(b8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE))
                .addContainerGap())
        );
        panelTranslucido2Layout.setVerticalGroup(
            panelTranslucido2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTranslucido2Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(b1)
                .addGap(18, 18, 18)
                .addComponent(b2)
                .addGap(18, 18, 18)
                .addComponent(b3)
                .addGap(18, 18, 18)
                .addComponent(b9)
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(b4)
                .addGap(18, 18, 18)
                .addComponent(b5)
                .addGap(18, 18, 18)
                .addComponent(b6)
                .addGap(18, 18, 18)
                .addComponent(b7)
                .addGap(18, 18, 18)
                .addComponent(b8)
                .addContainerGap(49, Short.MAX_VALUE))
        );

        panelTranslucido3.setBackground(new java.awt.Color(153, 153, 153));
        panelTranslucido3.setPreferredSize(new java.awt.Dimension(154, 538));

        scroll.setBorder(null);
        scroll.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setViewportBorder(javax.swing.BorderFactory.createEtchedBorder());
        scroll.setAutoscrolls(true);
        scroll.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        scroll.setMaximumSize(new java.awt.Dimension(1000, 1000));
        scroll.setOpaque(false);
        scroll.setPreferredSize(new java.awt.Dimension(110, 700));

        panelScroll.setMaximumSize(new java.awt.Dimension(767, 767));
        panelScroll.setPreferredSize(new java.awt.Dimension(105, 729));

        javax.swing.GroupLayout panelScrollLayout = new javax.swing.GroupLayout(panelScroll);
        panelScroll.setLayout(panelScrollLayout);
        panelScrollLayout.setHorizontalGroup(
            panelScrollLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 114, Short.MAX_VALUE)
        );
        panelScrollLayout.setVerticalGroup(
            panelScrollLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 729, Short.MAX_VALUE)
        );

        scroll.setViewportView(panelScroll);

        javax.swing.GroupLayout panelTranslucido3Layout = new javax.swing.GroupLayout(panelTranslucido3);
        panelTranslucido3.setLayout(panelTranslucido3Layout);
        panelTranslucido3Layout.setHorizontalGroup(
            panelTranslucido3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTranslucido3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scroll, javax.swing.GroupLayout.DEFAULT_SIZE, 134, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelTranslucido3Layout.setVerticalGroup(
            panelTranslucido3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTranslucido3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scroll, javax.swing.GroupLayout.DEFAULT_SIZE, 521, Short.MAX_VALUE)
                .addContainerGap())
        );

        cabezera1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/cabezera6menu.gif"))); // NOI18N

        cabezera2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/cabezera6terr.gif"))); // NOI18N

        cabezera.setFont(new java.awt.Font("Palatino Linotype", 1, 30));
        cabezera.setForeground(new java.awt.Color(102, 0, 102));
        cabezera.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cabezera.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/cabezera6.gif"))); // NOI18N
        cabezera.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        centro.setOpaque(false);

        panelCentro.setBackground(new java.awt.Color(102, 102, 102));
        panelCentro.setForeground(new java.awt.Color(204, 153, 255));
        panelCentro.setPreferredSize(new java.awt.Dimension(620, 519));

        javax.swing.GroupLayout panelCentroLayout = new javax.swing.GroupLayout(panelCentro);
        panelCentro.setLayout(panelCentroLayout);
        panelCentroLayout.setHorizontalGroup(
            panelCentroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 620, Short.MAX_VALUE)
        );
        panelCentroLayout.setVerticalGroup(
            panelCentroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 519, Short.MAX_VALUE)
        );

        centro.setViewportView(panelCentro);

        javax.swing.GroupLayout fondoLayout = new javax.swing.GroupLayout(fondo);
        fondo.setLayout(fondoLayout);
        fondoLayout.setHorizontalGroup(
            fondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelTranslucido1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(fondoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(fondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelTranslucido2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cabezera1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(fondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(cabezera, javax.swing.GroupLayout.DEFAULT_SIZE, 624, Short.MAX_VALUE)
                    .addComponent(centro, javax.swing.GroupLayout.DEFAULT_SIZE, 624, Short.MAX_VALUE))
                .addGap(13, 13, 13)
                .addGroup(fondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(fondoLayout.createSequentialGroup()
                        .addComponent(cabezera2)
                        .addGap(23, 23, 23))
                    .addGroup(fondoLayout.createSequentialGroup()
                        .addComponent(panelTranslucido3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );
        fondoLayout.setVerticalGroup(
            fondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(fondoLayout.createSequentialGroup()
                .addComponent(panelTranslucido1, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(fondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(fondoLayout.createSequentialGroup()
                        .addGroup(fondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cabezera1)
                            .addComponent(cabezera2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(fondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(panelTranslucido2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(panelTranslucido3, javax.swing.GroupLayout.DEFAULT_SIZE, 543, Short.MAX_VALUE)))
                    .addGroup(fondoLayout.createSequentialGroup()
                        .addComponent(cabezera)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(centro, javax.swing.GroupLayout.DEFAULT_SIZE, 523, Short.MAX_VALUE)))
                .addContainerGap())
        );

        getContentPane().add(fondo, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents


    //ACCION DEL BOTON "PRINCIPAL"
    private void b1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b1ActionPerformed
        
        panelCentro.removeAll();
        panelCentro.validate();
        panelCentro.setPreferredSize(new Dimension(620, 519));
        regionInicio = 0;
        regionFinal = 20;
        


        panelCentro.setLayout(new GridBagLayout()); //CREAMOS UN GESTOR DE BOLSA DE REJILLA
        GridBagConstraints c = new GridBagConstraints(); //CREAMOS Y ASIGNAMOS LAS CONSTRAINTS DE LA DISTRIBUCION

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;  //empieza en la columna 0
        c.gridy = 0; // y en la fila 0
        c.gridwidth = 2;    //puede coger 2 celdas para mostrarse
        c.weighty = 1;  //espacio vertical +1

        JLabel seccion = new JLabel("Informacion de territorio");
        seccion.setFont(new java.awt.Font("Palatino Linotype", 0, 24));
        seccion.setForeground(Color.white);
        panelCentro.add(seccion);

        c.gridx = 0;
        c.gridy = 1;
        for (int i=0; i<regiones.size();i++){
            if(idCurrentRegion == regiones.get(i).id){
                cabezera.setText(regiones.get(i).nombre + " [" + regiones.get(i).id + "]"); //cambiamos el nombre a la cabezera
                panelCentro.add(regiones.get(i).silueta, c); //AGREGAMOS LA SILUETA DEL PAIS SELECCIONADO
                regiones.get(i).silueta.setLocation((panelCentro.getBounds().width/2)-80, 10);

                c.anchor = GridBagConstraints.FIRST_LINE_START; //CAMBIAMOS LAS CONSTRAINTS PARA EL SIGUIENTE ELEMENTO
                c.gridx = 0; //columna 0
                c.gridy = 2;    //fila 1
                c.gridwidth = 0; //coge solo 1 columna
                c.weighty = 2; //espacio vertical +2

                info = new JTextArea(regiones.get(i).toString(), 50, 50);
                info.setFont(new Font("Palatino Linotype", 0, 16));
                info.setForeground(Color.white);
                info.setDisabledTextColor(Color.white);
                info.setEnabled(false);
                info.setOpaque(false);
                panelCentro.add(info, c); //AGREGAMOS LA DESCRIPCION DEL ELEMENTO
            }
        }
        currentBoton = 1;
        actualizarOro();
        listarRegiones();
        repaint();
        pack();
    }//GEN-LAST:event_b1ActionPerformed

    @SuppressWarnings("static-access")
    private void b2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b2ActionPerformed
        panelCentro.removeAll();
        panelCentro.validate();
        regionInicio = 0;
        regionFinal = 20;
        listarRegiones();


        panelCentro.setLayout(new GridBagLayout()); //CREAMOS UN GESTOR DE BOLSA DE REJILLA
        GridBagConstraints c = new GridBagConstraints(); //CREAMOS Y ASIGNAMOS LAS CONSTRAINTS DE LA DISTRIBUCION
        c.insets = new Insets(0,0,20,0); //dejamos un espacio de 20 pixeles entre elementos

        
        cabezera.setText("Edificios");


        //metemos en un array los edificios que hay construidos en esta region
        construidos=new ArrayList<EdificiosRegion>();
        salida.println("SELECT * FROM edificiosregion WHERE idRegion = '"+ idCurrentRegion + "'");
        String texto;
                try { //HACEMOS LA CONSULTA
                    while (!(texto = entrada.readLine()).equalsIgnoreCase("null")) {
                        Mensaje consulta = new Mensaje(texto);
                        construidos.add(new EdificiosRegion(Integer.parseInt(consulta.getPalabra(0)), Integer.parseInt(consulta.getPalabra(1)), Integer.parseInt(consulta.getPalabra(2))));
                    }
                } catch (IOException ex) {
                    System.out.println("Fallo leer entrada edificiosRegion.");
                    JOptionPane.showMessageDialog(this, "Has sido desconectado del servidor.");
                    new Ylogin().setVisible(true);
                    salir();
                }


                //hacemos lo mismo para los edificios que ya se estan construyendo
                ArrayList<EdificiosRegion> construyendo=new ArrayList<EdificiosRegion>();
                salida.println("SELECT idRegion, idUnidad, cantidad FROM construcciones WHERE usuario = '"+ usuario + "'");
                try { //HACEMOS LA CONSULTA
                    while (!(texto = entrada.readLine()).equalsIgnoreCase("null")) {
                        Mensaje consulta = new Mensaje(texto);
                        construyendo.add(new EdificiosRegion(Integer.parseInt(consulta.getPalabra(0)), Integer.parseInt(consulta.getPalabra(1)), Integer.parseInt(consulta.getPalabra(2))));
                    }
                } catch (IOException ex) {
                    System.out.println("Fallo leer entrada edificiosRegion.");
                    JOptionPane.showMessageDialog(this, "Has sido desconectado del servidor.");
                    new Ylogin().setVisible(true);
                    salir();
                }
       
        Molde1 molde;
        int nedificios = 0;

        int idmina = 1100; //bucle para ver cual es la ultima mina construida, ya que este edificio es actualizable y no solo construible
        for (int i=0;i<construidos.size();i++){
            if(construidos.get(i).idEdificio>1100 && construidos.get(i).idEdificio<1106)
                if(construidos.get(i).idEdificio>idmina)
                    idmina = construidos.get(i).idEdificio;
            if(idmina == 1105)
                idmina--;
        }

        for(Edificio edificio : edificios){ //recorremos toda la lista de edificios y creamos un molde de salida para mostrar sus datos
            
            int id = edificio.getId();

            molde = new Molde1(id, edificio.getCoste(), edificio.getTiempo());

        if(id == idmina+1 || id >= 1200){ //aqui definimos que solo una mina sea mostrada

        if(id == 1101)
            molde.setImagen("/imagenes/mina2.gif");  //anadimos la imagen
        else if(id > 1101 && id < 1103){
            molde.setImagen("/imagenes/mina2.gif");
            molde.setB1Text("Actualizar");  //si la mina es ya de nivel uno, cambiamos el boton a actualizar.
        }
        else if(id == 1103){
            molde.setImagen("/imagenes/mina2.gif");
            molde.setB1Text("Actualizar");
            if(usuario.getNivel() < 1)
                molde.setB1Enabled(false); //si no tenemos la tecnologia necesaria para construir el edificio se desactiva el boton.
        }
        else if(id == 1104){
            molde.setImagen("/imagenes/mina2.gif");
            molde.setB1Text("Actualizar");
            if(usuario.getNivel() < 3)
                molde.setB1Enabled(false);
        }
        else if(id == 1105){
            molde.setImagen("/imagenes/mina2.gif");
            molde.setB1Text("Actualizar");
            if(usuario.getNivel() < 5)
                molde.setB1Enabled(false);
        }
        else if(id == 1200)
            molde.setImagen("/imagenes/dojo.gif");
        else if(id == 1201)
            molde.setImagen("/imagenes/establo.gif");
        else if(id == 1202){
            molde.setImagen("/imagenes/nindo.gif");
            if(usuario.getNivel() < 2)
                molde.setB1Enabled(false);
        }
        else if(id == 1203){
            molde.setImagen("/imagenes/telar.gif");
            if(usuario.getNivel() < 3)
                molde.setB1Enabled(false);
        }
        else if(id == 1204){
            molde.setImagen("/imagenes/forja.gif");
            if(usuario.getNivel() < 3)
                molde.setB1Enabled(false);
        }
        else if(id == 1205){
            molde.setImagen("/imagenes/taller.gif");
            if(usuario.getNivel() < 5)
                molde.setB1Enabled(false);
        }
        else if(id == 1206){
            molde.setImagen("/imagenes/templo.gif");
            if(usuario.getNivel() < 6)
                molde.setB1Enabled(false);
        }
        else if(id == 1300){
            molde.setCantidadEnabled();
            molde.setImagen("/imagenes/torre.gif");
            if(usuario.getNivel() < 1)
                molde.setB1Enabled(false);
        }
        else if(id == 1301){
            molde.setCantidadEnabled();
            molde.setImagen("/imagenes/canon.gif");
            if(usuario.getNivel() < 5)
                molde.setB1Enabled(false);
        }
        else if(id == 1400)
            molde.setImagen("/imagenes/aldea.gif");
        else if(id == 1401){
            molde.setImagen("/imagenes/ciudad.gif");
            if(usuario.getNivel() < 3)
                molde.setB1Enabled(false);
        }
        else if(id == 1402){
            molde.setImagen("/imagenes/ciudadela.gif");
            if(usuario.getNivel() < 5)
                molde.setB1Enabled(false);
        }


            //SI LOS EDIFICIOS SON MULTICONSTRUCTIBLES MOSTRAMOS LA CANTIDAD AL LADO DE SU NOMBRE
            int cantidad = 0;
                if(id == 1300 || id == 1301){
                    for(int i=0; i<construidos.size();i++){
                        if(construidos.get(i).idEdificio == edificio.getId()){
                            cantidad = construidos.get(i).cantidad;
                        }
                    }
                molde.setNombre(edificio.getNombre() + "  ( " + cantidad + " )");
                molde.nombrereal = edificio.getNombre();
                }
                else{
                     molde.setNombre(edificio.getNombre());
                     molde.nombrereal = edificio.getNombre();
                }
                //REllenamos el resto de datos
            molde.setOro2(Integer.toString(edificio.getCoste()));
            //molde.setHuecos2(Integer.toString(edificio.getHuecos()));
            molde.setTiempo2(Integer.toString(edificio.getTiempo()) + " min.");
            molde.setDescripcion(edificio.getDescripcion());

            //HACEMOS LO MISMO CON EL BOTON
            //si el edificio es multiconstruible, nunca se pone el simbolo de ok en el boton
            //en cambio si no hay dinero para construir, se desactivan los botones
            for (int i = 0; i<construidos.size(); i++){
                if(edificio.getId() == construidos.get(i).idEdificio && edificio.getId()!= 1300 && edificio.getId()!= 1301){
                    molde.setB1Imagen("/imagenes/ok4.gif");
                    molde.setConstruido(true);
                }
                
                if(molde.construido == false){
                    if(Integer.parseInt(oro2.getText()) < edificio.getCoste())
                        molde.setB1Enabled(false);
                }
            }

            for (int i = 0; i<construyendo.size(); i++){
                if(edificio.getId() == construyendo.get(i).idEdificio && idCurrentRegion == construyendo.get(i).idRegion && edificio.getId()!= 1300 && edificio.getId()!= 1301){
                    molde.tRestante.setText("En cola...");
                    molde.setB1Enabled(false);
                }
            }



                c.gridy = c.gridy +1;
                panelCentro.add(molde, c);
                nedificios++;

                if(molde.construido == true)
                    molde.setB1Enabled(true);
            }//fin if id es igual a una sola mina o es otro edificio

        }//fin for cada uno de los moldes

        panelCentro.setPreferredSize(new Dimension(600, nedificios*148));
        currentBoton = 2;
        actualizarOro();
        repaint();
        pack();

    }//GEN-LAST:event_b2ActionPerformed

    @SuppressWarnings("static-access")
    private void b3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b3ActionPerformed
        panelCentro.removeAll();
        panelCentro.validate();
        regionInicio = 0;
        regionFinal = 20;
        listarRegiones();

        panelCentro.setLayout(new GridBagLayout()); //CREAMOS UN GESTOR DE BOLSA DE REJILLA
        GridBagConstraints c = new GridBagConstraints(); //CREAMOS Y ASIGNAMOS LAS CONSTRAINTS DE LA DISTRIBUCION
        c.insets = new Insets(0,0,20,0); //dejamos un espacio de 20 pixeles entre elementos


        cabezera.setText("Unidades");


        despliegues = new ArrayList<Despliegue>();
        salida.println("SELECT * FROM despliegues WHERE idRegion = '"+ idCurrentRegion + "'");
        String texto;
        try { //HACEMOS LA CONSULTA
            while (!(texto = entrada.readLine()).equalsIgnoreCase("null")) {
                Mensaje consulta = new Mensaje(texto);
                despliegues.add(new Despliegue(Integer.parseInt(consulta.getPalabra(0)), Integer.parseInt(consulta.getPalabra(1)), Integer.parseInt(consulta.getPalabra(2))));
            }
        } catch (IOException ex) {
            System.out.println("Fallo leer entrada despliegues.");
            JOptionPane.showMessageDialog(this, "Has sido desconectado del servidor.");
            new Ylogin().setVisible(true);
            salir();
        }

        //listamos los edificios construidos para esta region para determinar si podemos entrenar cierto tipo de unidades
        construidos=new ArrayList<EdificiosRegion>();
        salida.println("SELECT * FROM edificiosregion WHERE idRegion = '"+ idCurrentRegion + "'");
                try { //HACEMOS LA CONSULTA
                    while (!(texto = entrada.readLine()).equalsIgnoreCase("null")) {
                        Mensaje consulta = new Mensaje(texto);
                        construidos.add(new EdificiosRegion(Integer.parseInt(consulta.getPalabra(0)), Integer.parseInt(consulta.getPalabra(1)), Integer.parseInt(consulta.getPalabra(2))));
                    }
                } catch (IOException ex) {
                    System.out.println("Fallo leer entrada edificiosRegion zona de tecnologias.");
                }


        //Miramos si ya se esta construyendo el samurai legendario
            salida.println("SELECT * from construcciones where idUnidad = " + 4103);
            boolean legencola = false;
            try {
                while(!(texto = entrada.readLine()).equalsIgnoreCase("null")){
                    Mensaje mensaje = new Mensaje(texto);
                    if((Integer.parseInt(mensaje.getPalabra(3))) == 4103)
                        legencola = true;
                }
            } catch (IOException ex) {
                System.out.println(ex.getMessage() + " Fallo leer legendario.");
            }


        Molde2 molde;
        int nunidades = 0;

        c.gridy = 0;

        for (Unidad unidad : unidades){
            int id = unidad.getId();

            molde = new Molde2(id, unidad.getCoste(), unidad.getTiempo());

            if(id == 4100){
                molde.setImagen("/imagenes/samurai2.gif");
            }
            else if(id==4101){
                molde.setImagen("/imagenes/yabusame.gif");
            }
            else if(id==4102){
                molde.setImagen("/imagenes/ninja2.gif");
            }
            else if(id==4103){
                molde.setImagen("/imagenes/legendario.gif");
            }
            else if(id==4104){
                molde.setImagen("/imagenes/catapulta2.gif");
            }
            else if(id==4105){
                molde.setImagen("/imagenes/ariete.gif");
            }

            int cantidad = 0;
            for (Despliegue despliegue : despliegues){
                if(despliegue.getIdUnidad() == unidad.getId() && despliegue.getIdRegion() == idCurrentRegion){
                    cantidad = despliegue.getCantidad();
                }
            }

            if(unidad.getId() == 4103 && heroe)  //pone todas las casillas heroe a verde
            {
                molde.setB1Imagen("/imagenes/ok4.gif");
                molde.setConstruido(true);
            }

            if(unidad.getId() != 4103){
                molde.setCantidadEnabled();
            }
            molde.setNombre(unidad.getNombre() + "  ( " + cantidad + " )");
            /*else
                molde.setNombre(unidad.getNombre());*/

            molde.nombrereal = unidad.getNombre();
            molde.setDescripcion(unidad.getDescripcion());
            molde.setOro2(Integer.toString(unidad.getCoste()));
            molde.setTiempo2(Integer.toString(unidad.getTiempo()) + " m.");
            molde.setAtaque(Integer.toString(unidad.getAtaque()));
            molde.setDefensa(Integer.toString(unidad.getResistencia()));
            molde.setB1Enabled(false);

            for(EdificiosRegion edificio : construidos){ //aqui se activan los botones si poseo el edificio en esa zona concreta
                
                if(unidad.getId() == 4100 && edificio.idEdificio == 1200 && Integer.parseInt(oro2.getText()) > unidad.getCoste()){
                    molde.setB1Enabled(true);
                }
                else if(unidad.getId() == 4101 && edificio.idEdificio == 1201 && Integer.parseInt(oro2.getText()) > unidad.getCoste())
                {
                    molde.setB1Enabled(true);
                }
                else if(unidad.getId() == 4102 && usuario.getNivel() > 1  && edificio.idEdificio == 1202 && Integer.parseInt(oro2.getText()) > unidad.getCoste())
                {
                    molde.setB1Enabled(true);
                }
                else if((unidad.getId() == 4103 && edificio.idEdificio == 1200 && Integer.parseInt(oro2.getText()) > unidad.getCoste()) || (unidad.getId() == 4103 && heroe))
                {
                    molde.setB1Enabled(true);
                }
                else if(unidad.getId() == 4104 && edificio.idEdificio == 1205 && Integer.parseInt(oro2.getText()) > unidad.getCoste())
                {
                    molde.setB1Enabled(true);
                }
                else if(unidad.getId() == 4105 && edificio.idEdificio == 1205 && Integer.parseInt(oro2.getText()) > unidad.getCoste())
                {
                    molde.setB1Enabled(true);
                }
            }

            if(unidad.getId() == 4103 && legencola){
                molde.setB1Text("En cola...");
                molde.setB1Enabled(false);
            }

            if(Integer.parseInt(oro2.getText()) < unidad.getCoste())
                molde.setB1Enabled(false);

            panelCentro.add(molde, c);
            c.gridy += 1;
            nunidades++;
        }

        
        panelCentro.setPreferredSize(new Dimension(600, nunidades*150));
        currentBoton=3;
        actualizarOro();
        repaint();
        pack();
    }//GEN-LAST:event_b3ActionPerformed



    @SuppressWarnings("static-access") //BOTON INVESTIGACIONES
    private void b4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b4ActionPerformed
        panelCentro.removeAll();
        panelCentro.validate();
        regionInicio = 0;
        regionFinal = 20;
        listarRegiones();

        panelCentro.setLayout(new GridBagLayout()); //CREAMOS UN GESTOR DE BOLSA DE REJILLA
        GridBagConstraints c = new GridBagConstraints(); //CREAMOS Y ASIGNAMOS LAS CONSTRAINTS DE LA DISTRIBUCION
        c.insets = new Insets(0,0,20,0); //dejamos un espacio de 20 pixeles entre elementos


        cabezera.setText("Investigacion");

        //listamos los edificios construidos para TODAS las regiones para determinar si podemos construir cierto tipo de armas.
        boolean cforja = false;
        boolean ctemplo = false;
        boolean ctelar = false;
        boolean ctaller = false;

        String texto;
        salida.println("SELECT idEdificio FROM edificiosregion WHERE idRegion IN (SELECT idRegion FROM regiones WHERE propietario = '"+ usuario + "')");
                try { //HACEMOS LA CONSULTA
                    while (!(texto = entrada.readLine()).equalsIgnoreCase("null")) {
                        Mensaje consulta = new Mensaje(texto);
                        int codigo = Integer.parseInt(consulta.getPalabra(0));

                        if(codigo == 1204)
                            cforja = true;
                        else if(codigo == 1206)
                            ctemplo = true;
                        else if(codigo == 1203)
                            ctelar = true;
                        else if(codigo == 1205)
                            ctaller = true;
                    }
                } catch (IOException ex) {
                    System.out.println("Fallo leer entrada edificiosRegion general.");
                    JOptionPane.showMessageDialog(this, "Has sido desconectado del servidor.");
                    new Ylogin().setVisible(true);
                    salir();
                }


        Molde3 molde;
        int ntecnologias = 0;

        c.gridy = 0;
        boolean investigando;
        int nivel = usuario.getNivel();
        int nivelarma = usuario.getArma();
        int nivelarmadura = usuario.getArmadura();

        JLabel sep1 = new JLabel();
        sep1.setIcon(new ImageIcon(getClass().getResource("/imagenes/septecnologias.gif")));
        panelCentro.add(sep1, c);

        c.gridy = c.gridy + 1;

        //ZONA DE TECNOLOGIAS********************************************************************************

        investigando = false; //COMPROBAMOS SI HAY ALGUNA TECNOLOGIA INVESTIGANDOSE
            salida.println("SELECT * FROM investigaciones WHERE tipoEntidad = 'tecnologia' AND usuario = '" + usuario + "'");

            try { //HACEMOS LA CONSULTA
                    while(!(texto = entrada.readLine()).equalsIgnoreCase("null"))
                        investigando = true;
             } catch(Exception e){
                 System.out.println(e.getMessage() + " Fallo ver investigando.");
             }

            
        for (Tecnologia tecnologia : tecnologias){
            int id = tecnologia.getId();

            molde = new Molde3(usuario.getNick(), "tecnologia", tecnologia.getTiempo());

            if(id == 1){
                molde.setImagen("/imagenes/arquitectura.gif");
            }
            else if(id==2){
                molde.setImagen("/imagenes/espionaje.gif");
            }
            else if(id==3){
                molde.setImagen("/imagenes/mejorada.gif");
            }
            else if(id==4){
                molde.setImagen("/imagenes/medicina.gif");
            }
            else if(id==5){
                molde.setImagen("/imagenes/quimica.gif");
            }
            else if(id==6){
                molde.setImagen("/imagenes/tecnicas.gif");
            }

            if(id <= nivel)
                {
                    molde.setB1Imagen("/imagenes/ok4.gif");
                    molde.setConstruido(true);
                }
            else if(id == nivel+1 && investigando){
                molde.setB1Enabled(false);
                molde.tRestante.setText("En cola...");
            }
            else if(id > nivel+1){
                molde.setB1Enabled(false);
            }
            

            molde.setNombre(tecnologia.getNombre());

            molde.nombrereal = tecnologia.getNombre();
            molde.setDescripcion(tecnologia.getDescripcion());
            molde.setOro2(Integer.toString(tecnologia.getCoste()));
            molde.setTiempo2(Integer.toString(tecnologia.getTiempo()) + " min.");

            if(Integer.parseInt(oro2.getText()) < tecnologia.getCoste() && molde.construido == false)
                molde.setB1Enabled(false);

            panelCentro.add(molde, c);
            c.gridy += 1;
            ntecnologias++;
        }


        //ZONA DE ARMAS*************************************************************
        c.gridy += 1;
        int narmas = 0;

        JLabel sep2 = new JLabel();
        sep2.setIcon(new ImageIcon(getClass().getResource("/imagenes/separmas.gif")));
        panelCentro.add(sep2, c);
        c.gridy += 1;
        
        
        investigando = false;
            salida.println("SELECT * FROM investigaciones WHERE tipoEntidad = 'arma' AND usuario = '" + usuario + "'");

            try { //HACEMOS LA CONSULTA
                    while(!(texto = entrada.readLine()).equalsIgnoreCase("null"))
                        investigando = true;
             } catch(Exception e){
                 System.out.println(e.getMessage() + " Fallo ver investigando arma.");
             }

        for(Arma arma : armas){
            int id = arma.getId();

            molde = new Molde3(usuario.getNick(), "arma", arma.getTiempo());

            if(id == 2100){
                molde.setImagen("/imagenes/katanas/katana1.gif");
            }
            else if(id==2101){
                molde.setImagen("/imagenes/katanas/katana2.gif");
                molde.setB1Enabled(cforja);
            }
            else if(id==2102){
                molde.setImagen("/imagenes/katanas/katana3.gif");
                molde.setB1Enabled(cforja);
            }
            else if(id==2103){
                molde.setImagen("/imagenes/katanas/katana4.gif");
                molde.setB1Enabled(ctemplo);
            }
            else if(id==2104){
                molde.setImagen("/imagenes/katanas/katana5.gif");
                molde.setB1Enabled(ctemplo);
            }


            if(id <= nivelarma)
                {
                    molde.setB1Imagen("/imagenes/ok4.gif");
                    molde.setConstruido(true);
                }
            else if(id == nivelarma+1 && investigando){
                molde.setB1Enabled(false);
                molde.tRestante.setText("En cola...");
            }
            else if(id > nivelarma+1){
                molde.setB1Enabled(false);
            }



            molde.setNombre(arma.getNombre());

            molde.nombrereal = arma.getNombre();
            molde.setDescripcion(arma.getDescripcion());
            molde.setOro2(Integer.toString(arma.getCoste()));
            molde.setTiempo2(Integer.toString(arma.getTiempo()) + " min.");

            if(Integer.parseInt(oro2.getText()) < arma.getCoste())
                molde.setB1Enabled(false);

            panelCentro.add(molde, c);
            c.gridy += 1;
            narmas++;
        }


        //ZONA DE ARMADURAS **********************************************************************
        c.gridy += 1;
        int narmaduras = 0;

        JLabel sep3 = new JLabel();
        sep3.setIcon(new ImageIcon(getClass().getResource("/imagenes/separmaduras.gif")));
        panelCentro.add(sep3, c);
        c.gridy += 1;


        investigando = false;
            salida.println("SELECT * FROM investigaciones WHERE tipoEntidad = 'armadura' AND usuario = '" + usuario + "'");

            try { //HACEMOS LA CONSULTA
                    while(!(texto = entrada.readLine()).equalsIgnoreCase("null"))
                        investigando = true;
             } catch(Exception e){
                 System.out.println(e.getMessage() + " Fallo ver investigando arma.");
             }

        for(Armadura armadura : armaduras){
            int id = armadura.getId();

            molde = new Molde3(usuario.getNick(), "armadura", armadura.getTiempo());

            if(id == 3200){
                molde.setImagen("/imagenes/kimonos/kimono.gif");
            }
            else if(id==3201){
                molde.setImagen("/imagenes/kimonos/yoroi1.gif");
                molde.setB1Enabled(ctelar);
            }
            else if(id==3202){
                molde.setImagen("/imagenes/kimonos/yoroi2.gif");
                molde.setB1Enabled(ctelar);
            }
            else if(id==3203){
                molde.setImagen("/imagenes/kimonos/yoroi3.gif");
                molde.setB1Enabled(ctaller);
            }
            else if(id==3204){
                molde.setImagen("/imagenes/kimonos/kabuto1.gif");
                molde.setB1Enabled(ctaller);
            }


            if(id <= nivelarmadura)
                {
                    molde.setB1Imagen("/imagenes/ok4.gif");
                    molde.setConstruido(true);
                }
            else if(id == nivelarmadura+1 && investigando){
                molde.setB1Enabled(false);
                molde.tRestante.setText("En cola...");
            }
            else if(id > nivelarmadura+1){
                molde.setB1Enabled(false);
            }



            molde.setNombre(armadura.getNombre());

            molde.nombrereal = armadura.getNombre();
            molde.setDescripcion(armadura.getDescripcion());
            molde.setOro2(Integer.toString(armadura.getCoste()));
            molde.setTiempo2(Integer.toString(armadura.getTiempo()) + " min.");

            if(Integer.parseInt(oro2.getText()) < armadura.getCoste())
                molde.setB1Enabled(false);

            panelCentro.add(molde, c);
            c.gridy += 1;
            narmaduras++;
        }



        panelCentro.setPreferredSize(new Dimension(600, (ntecnologias + narmas + narmaduras)*158));
        currentBoton = 4;
        actualizarOro();
        repaint();
        pack();
    }//GEN-LAST:event_b4ActionPerformed

    private void b5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b5ActionPerformed
        panelCentro.removeAll();
        panelCentro.validate();
        regionInicio = 0;
        regionFinal = 20;
        listarRegiones();

        int ntropas = 1;
        int ntareas = 0;
        int ntipos = 0;
        boolean sepunidades = true;
        boolean sepedificios = true;
        boolean septecnologia = true;
        boolean separma = true;
        boolean separmadura = true;
        boolean sepmovimientos = true;

        panelCentro.setLayout(new GridBagLayout()); //CREAMOS UN GESTOR DE BOLSA DE REJILLA
        GridBagConstraints c = new GridBagConstraints(); //CREAMOS Y ASIGNAMOS LAS CONSTRAINTS DE LA DISTRIBUCION
        c.insets = new Insets(0,0,20,0); //dejamos un espacio de 20 pixeles entre elementos
        c.gridy = 0;
        c.anchor=GridBagConstraints.NORTH;
        cabezera.setText("Tareas");


        salida.println("produccion " + usuario);

        String texto;
        Molde4prod molde;

        try {
            while (!(texto = entrada.readLine()).equalsIgnoreCase("null")) {

                Mensaje mensaje = new Mensaje(texto);

        //EDIFICIOS****************************************************************************
                if(mensaje.getPalabra(0).equalsIgnoreCase("edificio")){
                    if(sepedificios){
                    JLabel sep1 = new JLabel();
                    sep1.setIcon(new ImageIcon(getClass().getResource("/imagenes/sepedificios.gif")));
                    panelCentro.add(sep1, c);
                    c.gridy += 1;
                    sepedificios = false;
                    ntipos++;
                    }

                    molde = new Molde4prod(mensaje.getPalabra(0), Integer.parseInt(mensaje.getPalabra(1)), Integer.parseInt(mensaje.getPalabra(2)),
                            Integer.parseInt(mensaje.getPalabra(3)), Integer.parseInt(mensaje.getPalabra(4)), Boolean.parseBoolean(mensaje.getPalabra(5)));
                    panelCentro.add(molde, c);
                    c.gridy += 1;
                }

       //UNIDADES******************************************************************************
            if(sepunidades && !mensaje.getPalabra(0).equalsIgnoreCase("edificio") && mensaje.getPalabra(0).equalsIgnoreCase("unidad")){
                JLabel sep2 = new JLabel();
                sep2.setIcon(new ImageIcon(getClass().getResource("/imagenes/sepunidades.gif")));
                panelCentro.add(sep2, c);
                c.gridy += 1;
                sepunidades = false;
                ntipos++;
            }

            if(mensaje.getPalabra(0).equalsIgnoreCase("unidad")){

                molde = new Molde4prod(mensaje.getPalabra(0), Integer.parseInt(mensaje.getPalabra(1)), Integer.parseInt(mensaje.getPalabra(2)),
                        Integer.parseInt(mensaje.getPalabra(3)), Integer.parseInt(mensaje.getPalabra(4)), Boolean.parseBoolean(mensaje.getPalabra(5)));
                panelCentro.add(molde, c);
                c.gridy += 1;
            }

        //TECNOLOGIAS******************************************************************************
            if(septecnologia && !mensaje.getPalabra(0).equalsIgnoreCase("edificio") && !mensaje.getPalabra(0).equalsIgnoreCase("unidad")
                                && mensaje.getPalabra(0).equalsIgnoreCase("tecnologia")){
                JLabel sep3 = new JLabel();
                sep3.setIcon(new ImageIcon(getClass().getResource("/imagenes/septecnologias.gif")));
                panelCentro.add(sep3, c);
                c.gridy += 1;
                septecnologia = false;
                ntipos++;
            }

            if(mensaje.getPalabra(0).equalsIgnoreCase("tecnologia")){

                molde = new Molde4prod("tecnologia", Integer.parseInt(mensaje.getPalabra(1)));
                panelCentro.add(molde, c);
                c.gridy += 1;
            }


            //ARMAS******************************************************************************
            if(separma && !mensaje.getPalabra(0).equalsIgnoreCase("edificio") && !mensaje.getPalabra(0).equalsIgnoreCase("unidad") 
               && !mensaje.getPalabra(0).equalsIgnoreCase("tecnologia") && mensaje.getPalabra(0).equalsIgnoreCase("arma")){

                JLabel sep3 = new JLabel();
                sep3.setIcon(new ImageIcon(getClass().getResource("/imagenes/separmas.gif")));
                panelCentro.add(sep3, c);
                c.gridy += 1;
                separma = false;
                ntipos++;
            }

            if(mensaje.getPalabra(0).equalsIgnoreCase("arma")){

                molde = new Molde4prod("arma", Integer.parseInt(mensaje.getPalabra(1)));
                panelCentro.add(molde, c);
                c.gridy += 1;
            }
                
            //ARMADURAS******************************************************************************
            if(separmadura && !mensaje.getPalabra(0).equalsIgnoreCase("edificio") && !mensaje.getPalabra(0).equalsIgnoreCase("unidad") 
               && !mensaje.getPalabra(0).equalsIgnoreCase("tecnologia") && !mensaje.getPalabra(0).equalsIgnoreCase("arma") && mensaje.getPalabra(0).equalsIgnoreCase("armadura")){
                
                JLabel sep4 = new JLabel();
                sep4.setIcon(new ImageIcon(getClass().getResource("/imagenes/separmaduras.gif")));
                panelCentro.add(sep4, c);
                c.gridy += 1;
                separmadura = false;
                ntipos++;
            }

            if(mensaje.getPalabra(0).equalsIgnoreCase("armadura")){

                molde = new Molde4prod("armadura", Integer.parseInt(mensaje.getPalabra(1)));
                panelCentro.add(molde, c);
                c.gridy += 1;
            }


            //MOVIMIENTOS******************************************************************************
            if(sepmovimientos && !mensaje.getPalabra(0).equalsIgnoreCase("edificio") && !mensaje.getPalabra(0).equalsIgnoreCase("unidad")
               && !mensaje.getPalabra(0).equalsIgnoreCase("tecnologia") && !mensaje.getPalabra(0).equalsIgnoreCase("arma") && !mensaje.getPalabra(0).equalsIgnoreCase("armadura")
               && mensaje.getPalabra(0).equalsIgnoreCase("movimiento")){

                JLabel sep5 = new JLabel();
                sep5.setIcon(new ImageIcon(getClass().getResource("/imagenes/sepmovimientos.gif")));
                panelCentro.add(sep5, c);
                c.gridy += 1;
                sepmovimientos = false;
                ntipos++;
            }

            if(mensaje.getPalabra(0).equalsIgnoreCase("movimiento")){

                molde = new Molde4prod(ntropas, Integer.parseInt(mensaje.getPalabra(1)), Integer.parseInt(mensaje.getPalabra(2)), Integer.parseInt(mensaje.getPalabra(3)));
                panelCentro.add(molde, c);
                ntropas ++;
                c.gridy += 1;
            }



            ntareas++;
            }//fin while entrada

        } catch (IOException ex) {
            System.out.println(ex.getMessage() + " Error en b5 leer produccion.");
            JOptionPane.showMessageDialog(this, "Has sido desconectado del servidor.");
            new Ylogin().setVisible(true);
            salir();
        }

        if(ntareas == 0){
        JLabel nada = new JLabel("Nada en construccion.");
        nada.setForeground(Color.white);
        panelCentro.add(nada, c);
        ntareas = 1;
        }

        panelCentro.setPreferredSize(new Dimension(600, (ntareas*160)+(ntipos*30) ));
        currentBoton = 5;
        actualizarOro();
        repaint();
        pack();
    }//GEN-LAST:event_b5ActionPerformed

    private void b6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b6ActionPerformed
        panelCentro.removeAll();
        panelCentro.validate();
        listarRegiones();

        panelCentro.setLayout(new GridBagLayout()); //CREAMOS UN GESTOR DE BOLSA DE REJILLA
        GridBagConstraints c = new GridBagConstraints(); //CREAMOS Y ASIGNAMOS LAS CONSTRAINTS DE LA DISTRIBUCION
        c.insets = new Insets(0,0,5,0); //dejamos un espacio de 20 pixeles entre elementos
        c.gridy = 0;


        cabezera.setText("Territorios");
        
        listarTodasRegiones();
        
        JButton anterior = new JButton("< Anterior");
        anterior.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                regionInicio = regionInicio - 20;
                regionFinal = regionFinal - 20;
                b6.doClick();
            }
        });
        
        JButton siguiente = new JButton("Siguiente >");
        siguiente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                regionInicio = regionInicio + 20;
                regionFinal = regionFinal + 20;
                b6.doClick();
            }
        });
        
        if(regionInicio == 0){
            anterior.setEnabled(false);
        }
        if(regionFinal > 90){
            siguiente.setEnabled(false);
        }
        
        /*c.anchor = GridBagConstraints.WEST;
        panelCentro.add(anterior, c);
        c.anchor = GridBagConstraints.EAST;
        panelCentro.add(siguiente, c);
        c.gridy += 1;
        
        c.anchor = GridBagConstraints.CENTER;*/
        c.gridwidth = 2;
        Molde6pais cabeza = new Molde6pais();
        cabeza.setBackground(new Color(127,87,131));
        cabeza.coordenada.setFont(new Font("Palatino Linotype", Font.BOLD, 12));
        cabeza.coordenada.setText("Coordenadas");
        cabeza.nombre.setFont(new Font("Palatino Linotype", Font.BOLD, 12));
        cabeza.nombre.setText("Nombre");
        cabeza.propietario.setFont(new Font("Palatino Linotype", Font.BOLD, 12));
        cabeza.propietario.setText("Propietario");
        cabeza.superficie.setFont(new Font("Palatino Linotype", Font.BOLD, 12));
        cabeza.superficie.setText("Superficie");
        
        panelCentro.add(cabeza, c);
        c.gridy +=1;

        Molde6pais molde;
        for(int i = regionInicio ; i<regionFinal && i < 94 ; i++){
            molde = new Molde6pais();
            
            molde.coordenada.setText(Integer.toString(regiones2.get(i).id));
            if(regiones2.get(i).nombre.equalsIgnoreCase("vacio"))
                molde.nombre.setText("-");
            else
                molde.nombre.setText(regiones2.get(i).nombre);
            if(regiones2.get(i).propietario.equalsIgnoreCase("Deshabitado"))
                molde.propietario.setText("-");
            else
                molde.propietario.setText(regiones2.get(i).propietario);
            molde.superficie.setText(Integer.toString(regiones2.get(i).superficie) + " m2");
            
            panelCentro.add(molde, c);
            c.gridy += 1;
        }

        c.gridwidth = 1;
        
        c.anchor = GridBagConstraints.WEST;
        panelCentro.add(anterior, c);
        c.anchor = GridBagConstraints.EAST;
        panelCentro.add(siguiente, c);
        c.gridy +=1;
        
        
        panelCentro.setPreferredSize(new Dimension(600, 25*25 ));
        currentBoton = 6;
        actualizarOro();
        repaint();
        pack();
    }//GEN-LAST:event_b6ActionPerformed

    private void b7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b7ActionPerformed
        panelCentro.removeAll();
        panelCentro.validate();
        regionInicio = 0;
        regionFinal = 20;
        listarRegiones();
        int ncorreos = 0;

        panelCentro.setLayout(new GridBagLayout()); //CREAMOS UN GESTOR DE BOLSA DE REJILLA
        GridBagConstraints c = new GridBagConstraints(); //CREAMOS Y ASIGNAMOS LAS CONSTRAINTS DE LA DISTRIBUCION
        c.insets = new Insets(0,5,5,0); //dejamos un espacio de 20 pixeles entre elementos
        c.gridy = 0;
        c.gridwidth = 2;
        cabezera.setText("Mensajes");

        JButton nuevo = new JButton("Mensaje Nuevo");
        nuevo.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                panelCentro.removeAll();
                panelCentro.validate();
                regionInicio = 0;
                regionFinal = 20;
                listarRegiones();

                panelCentro.setLayout(new GridBagLayout()); //CREAMOS UN GESTOR DE BOLSA DE REJILLA
                GridBagConstraints c = new GridBagConstraints(); //CREAMOS Y ASIGNAMOS LAS CONSTRAINTS DE LA DISTRIBUCION
                c.insets = new Insets(0,0,20,0); //dejamos un espacio de 20 pixeles entre elementos
                c.gridy = 0;


                        JLabel remitente = new JLabel("Destinatario: ");
                        remitente.setHorizontalAlignment(remitente.LEFT);
                        remitente.setFont(new Font("Palatino Linotype", Font.BOLD, 14));
                        remitente.setForeground(Color.white);
                        panelCentro.add(remitente, c);

                        final JTextArea remitente2 = new JTextArea(1, 20);
                        remitente2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
                        remitente2.setFont(new Font("Palatino Linotype", Font.PLAIN, 14));
                        remitente2.setForeground(Color.black);
                        panelCentro.add(remitente2, c);
                        c.gridy += 1;

                        JLabel asunto = new JLabel("Asunto: ");
                        asunto.setHorizontalTextPosition(SwingConstants.LEFT);
                        asunto.setFont(new Font("Palatino Linotype", Font.BOLD, 14));
                        asunto.setForeground(Color.white);
                        panelCentro.add(asunto, c);

                        final JTextArea asunto2 = new JTextArea(1, 20);
                        asunto2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
                        asunto2.setFont(new Font("Palatino Linotype", Font.PLAIN, 14));
                        asunto2.setForeground(Color.black);
                        panelCentro.add(asunto2, c);
                        c.gridy += 1;

                        c.gridwidth = 3;



                        final JTextArea cuerpo = new JTextArea(15, 50);
                        cuerpo.setDisabledTextColor(Color.white);
                        cuerpo.setLineWrap(true);
                        cuerpo.setBorder(javax.swing.BorderFactory.createEtchedBorder());
                        cuerpo.setFont(new Font("Palatino Linotype", Font.PLAIN, 12));
                        panelCentro.add(cuerpo, c);
                        c.gridy += 1;
                        c.gridwidth = 1;

                        //Boton enviar mensaje ************************************************************************
                        JButton enviar = new JButton("Enviar");

                        //Capturamos la fecha y hora del mensaje*********************************************************
                        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                        java.util.Date fecha = new java.util.Date();
                        final String fechahora = formato.format(fecha);


                        enviar.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                salida.println("extraer " + remitente2.getText());
                                Mensaje mensaje = null;
                                try {
                                    mensaje = new Mensaje(entrada.readLine());
                                    String cmensaje = cuerpo.getText();
                                    Atributo cuerpomensaje = null;
                                    System.out.println(mensaje.toString());

                                    if(mensaje.getPalabra(0).equalsIgnoreCase(remitente2.getText())){
                                         if(cmensaje.length() > 500)
                                             cuerpomensaje = new Atributo(cmensaje.substring(0, 500));
                                         else
                                             cuerpomensaje = new Atributo(cmensaje);
                                        salida.println("insertar INSERT INTO mensajes VALUES (s_mensajes.NEXTVAL, '" + usuario + "', '" + remitente2.getText() + "', '" + asunto2.getText() + "', '" + cuerpomensaje.toString() + "', '" + fechahora + "')");
                                        b7.doClick();
                                    }else{
                                        JOptionPane.showMessageDialog(panelCentro, "Usuario incorrecto.");
                                    }

                                    if(mensaje.getPalabra(0).equalsIgnoreCase(null))
                                        while(!entrada.readLine().equalsIgnoreCase("null")){
                                            System.out.println("hola");
                                        }

                                }catch(IOException ex) {
                                    System.out.println(ex.getMessage() + " Fallo boton enviar correo, leer usuario");
                                }
                            }
                        });
                        panelCentro.add(enviar, c);

                        //Boton volver****************************************
                        JButton volver = new JButton("Volver");
                        volver.addActionListener(new ActionListener() {

                            public void actionPerformed(ActionEvent e) {
                                b7.doClick();
                            }
                        });
                        panelCentro.add(volver, c);




                        remitente2.requestFocus();
                panelCentro.setPreferredSize(new Dimension(600, 500));
                currentBoton=7;
                actualizarOro();
                repaint();
                pack();
            }
            
        });

        JButton eliminar = new JButton("Eliminar");
        eliminar.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                for(int i=0 ; i<selectores.size() ; i++){
                    if(selectores.get(i).isSelected()){
                        salida.println("insertar DELETE mensajes WHERE idMensaje = " + correos.get(i).getIdCorreo());
                    }
                }
                b7.doClick();
            }
        });

        panelCentro.add(nuevo, c);
        panelCentro.add(eliminar, c);

        c.gridy +=1;
        c.gridwidth = 5;
        Molde6pais cabeza = new Molde6pais();
        cabeza.setBackground(new Color(127,87,131));
        //cabeza.coordenada.setFont(new Font("Palatino Linotype", Font.BOLD, 12));
        //cabeza.coordenada.setText("Coordenadas");
        cabeza.nombre.setFont(new Font("Palatino Linotype", Font.BOLD, 12));
        cabeza.nombre.setHorizontalAlignment(cabeza.nombre.LEFT);
        cabeza.nombre.setText("De:");
        cabeza.propietario.setFont(new Font("Palatino Linotype", Font.BOLD, 12));
        cabeza.propietario.setHorizontalAlignment(cabeza.propietario.LEFT);
        cabeza.propietario.setText("Asunto:");
        cabeza.superficie.setFont(new Font("Palatino Linotype", Font.BOLD, 12));
        cabeza.superficie.setHorizontalAlignment(cabeza.superficie.LEFT);
        cabeza.superficie.setText("Fecha:");

        selectores = new ArrayList<JRadioButton>();

        salida.println("select * from mensajes where destinatario = '" + usuario + "' ORDER BY idMensaje DESC");
        panelCentro.add(cabeza, c);
        c.gridy +=1;

        correos = new ArrayList<Correo>();
        String texto = "null";
        try {
            while (!(texto = entrada.readLine()).equalsIgnoreCase("null")) {
                Mensaje mensaje = new Mensaje(texto);
                mensaje.desencadenar();
                correos.add(new Correo(Integer.parseInt(mensaje.getPalabra(0)), mensaje.getPalabra(1), mensaje.getPalabra(2),
                        mensaje.getPalabra(3), mensaje.getPalabra(4), mensaje.getPalabra(5)));
                ncorreos++;
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage() + " Fallo leer correo.");
        }

        for(Correo correo: correos){
            JRadioButton selector = new JRadioButton();
            selector.setBorder(javax.swing.BorderFactory.createEtchedBorder());
            selector.setHorizontalAlignment(selector.RIGHT);
            c.gridwidth = 1;
            c.anchor = GridBagConstraints.EAST;
            panelCentro.add(selector, c);
            selectores.add(selector);
            c.anchor = GridBagConstraints.CENTER;
            c.gridwidth = 4;

            Molde6pais molde = new Molde6pais();

            molde.id = correo.getIdCorreo();
            molde.coordenada.setVisible(false);
            molde.nombre.setHorizontalAlignment(molde.nombre.LEFT);
            molde.nombre.setText(correo.getRemitente());
            molde.propietario.setText(correo.getAsunto());
            molde.superficie.setText(correo.getFecha());
            addMouseListener3(molde);

            panelCentro.add(molde, c);
            c.gridy += 1;

        }

        if(ncorreos == 0){
                        JLabel nocorreos = new JLabel("Buzon vacio.");
                        nocorreos.setFont(new Font("Palatino Linotype", Font.BOLD, 14));
                        nocorreos.setForeground(Color.white);
                        panelCentro.add(nocorreos, c);
        }

        


        panelCentro.setPreferredSize(new Dimension(600, (ncorreos * 38) ));
        currentBoton=7;
        actualizarOro();
        repaint();
        pack();
    }//GEN-LAST:event_b7ActionPerformed

    private void b8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b8ActionPerformed
        panelCentro.removeAll();
        panelCentro.validate();
        regionInicio = 0;
        regionFinal = 20;
        listarRegiones();
        
        panelCentro.setLayout(new GridBagLayout()); //CREAMOS UN GESTOR DE BOLSA DE REJILLA
        GridBagConstraints c = new GridBagConstraints(); //CREAMOS Y ASIGNAMOS LAS CONSTRAINTS DE LA DISTRIBUCION
        c.insets = new Insets(0,0,5,0); //dejamos un espacio de 20 pixeles entre elementos
        c.gridy = 0;

        cabezera.setText("Clasificacion");

        usuarios = new ArrayList<Usuario>();
        salida.println("SELECT * from usuarios where nick not in ('Deshabitado','System')");
        String texto;
        try {
            while (!(texto = entrada.readLine()).equalsIgnoreCase("null")) {
                Mensaje mensaje = new Mensaje(texto);
                System.out.println(texto);
                usuarios.add(new Usuario(mensaje.getPalabra(0), mensaje.getPalabra(1), mensaje.getPalabra(2), Integer.parseInt(mensaje.getPalabra(3)),
                        Integer.parseInt(mensaje.getPalabra(4)), Integer.parseInt(mensaje.getPalabra(5)), Integer.parseInt(mensaje.getPalabra(6))));
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage() + " Fallo clasificiacion");
        }

        //actulizamos toda la lista de usuario con sus puntos
        for(Usuario usuar : usuarios){
            int nedificios = 0;
            int nunidades = 0;
            Mensaje mensaje;

            salida.println("select nvl(sum(cantidad), 0) from regiones, edificiosregion where regiones.idRegion=edificiosRegion.idRegion and propietario = '" + usuar.getNick() + "'");
            try {
                while (!(texto = entrada.readLine()).equalsIgnoreCase("null")) {
                     nedificios = Integer.parseInt(texto);
                }
            } catch (IOException ex) {
                System.out.println(ex.getMessage() + " Fallo clasificacion 2.");
            }
            usuar.puntos = usuar.puntos + (nedificios*13);


            salida.println("select nvl(sum(cantidad), 0) from regiones, despliegues where regiones.idRegion=despliegues.idRegion and propietario = '" + usuar.getNick() + "'");
            try {
                while (!(texto = entrada.readLine()).equalsIgnoreCase("null")) {
                    nunidades = Integer.parseInt(texto);
                }
            } catch (IOException ex) {
                System.out.println(ex.getMessage() + " Fallo clasificacion 2.");
            }
            usuar.puntos = usuar.puntos + nunidades;

            usuar.puntos = (int)(usuar.puntos + (usuar.getOro()/21));
            usuar.puntos = usuar.puntos * ((usuar.getNivel()*usuar.getNivel())+1);
            usuar.puntos = usuar.puntos * (usuar.getArma()-2095);
            usuar.puntos = usuar.puntos * (usuar.getArmadura()-3196);

            usuar.puntos = (int)usuar.puntos/255;
        }


        JLabel top = new JLabel("Top 10");
        top.setFont(new Font("Palatino Linotype", Font.BOLD, 25));
        top.setForeground(Color.white);
        panelCentro.add(top, c);
        c.gridy +=1;

        Molde6pais molde;

        for(Usuario usuar : usuarios){
            salida.println("insertar UPDATE usuarios set puntos = " + usuar.puntos + " WHERE nick = '" + usuar.getNick() + "'");
        }

       salida.println("SELECT nick, puntos from usuarios where nick not in('Deshabitado','System') ORDER BY puntos DESC");
        try {
            while (!(texto = entrada.readLine()).equalsIgnoreCase("null")) {
                Mensaje mensaje = new Mensaje(texto);
                molde = new Molde6pais();
                molde.nombre.setText(mensaje.getPalabra(0));
                molde.propietario.setText(mensaje.getPalabra(1));
                panelCentro.add(molde, c);
                c.gridy +=1;
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage() + " Fallo ver puntos usuario");
        }
        
        panelCentro.setPreferredSize(new Dimension(600, 500));
        currentBoton=8;
        //actualizarOro();
        repaint();
        pack();
    }//GEN-LAST:event_b8ActionPerformed

    @SuppressWarnings("static-access")
    private void b9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b9ActionPerformed
        panelCentro.removeAll();
        panelCentro.validate();
        boolean vacio = true;
        regionInicio = 0;
        regionFinal = 20;
        listarRegiones();

        panelCentro.setLayout(new GridBagLayout()); //CREAMOS UN GESTOR DE BOLSA DE REJILLA
        GridBagConstraints c = new GridBagConstraints(); //CREAMOS Y ASIGNAMOS LAS CONSTRAINTS DE LA DISTRIBUCION
        c.insets = new Insets(0,0,5,0); //dejamos un espacio de 20 pixeles entre elementos
        c.gridy = 0;
        c.anchor = c.NORTH;


        cabezera.setText("Movilizar");


        despliegues = new ArrayList<Despliegue>(); //consultamos las unidades que hay en este territorio.
        salida.println("SELECT * FROM despliegues WHERE idRegion = '"+ idCurrentRegion + "'");
        String texto;
        try { //HACEMOS LA CONSULTA
            while (!(texto = entrada.readLine()).equalsIgnoreCase("null")) {
                Mensaje consulta = new Mensaje(texto);
                despliegues.add(new Despliegue(Integer.parseInt(consulta.getPalabra(0)), Integer.parseInt(consulta.getPalabra(1)), Integer.parseInt(consulta.getPalabra(2))));
                vacio = false;
            }
        } catch (IOException ex) {
            System.out.println("Fallo leer entrada despliegues.");
            JOptionPane.showMessageDialog(this, "Has sido desconectado del servidor.");
            new Ylogin().setVisible(true);
            salir();
        }

        if(vacio){
            JLabel vac = new JLabel("No hay unidades en esta region.");
            vac.setFont(new Font("Palatino Linotype", Font.PLAIN, 14));
            vac.setForeground(Color.white);
            panelCentro.add(vac);
        }
        else{
        //anadimos el sep o titulo
        JLabel sep1 = new JLabel();
        sep1.setIcon(new ImageIcon(getClass().getResource("/imagenes/sepunidades.gif")));
        panelCentro.add(sep1, c);
        c.gridy += 1;

        //A diferencia que en los restantes botones, aqui vamos a meter todos los moldes que creamos en una lista,
        //para despues acceder a ellos mas facilmente con la accion de los botones.
        Molde5ataq molde;
        moldes = new ArrayList<Molde5ataq>();

        for(Despliegue despliegue : despliegues){
            molde = new Molde5ataq();

            for(Unidad unidad : unidades){
                if(despliegue.getIdUnidad()==unidad.getId()){
                    molde.nombre.setText(unidad.getNombre());
                    molde.cantidadreal = despliegue.getCantidad();
                    molde.setIdUnidad(unidad.getId());
                    molde.cantidad2.setText(Integer.toString(despliegue.getCantidad()));
                    molde.velocidad2.setText(Integer.toString(unidad.getVelocidad()));
                }
            }
            moldes.add(molde);
            panelCentro.add(molde, c);
            c.gridy += 1;
        }

        //BOTON SELECCIONAR TODO************************************************************
        JButton todo = new JButton("Seleccionar todo");
        todo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                for(Molde5ataq molde : moldes){
                    if(molde.cantidadreal > 0)
                        molde.setCantidad3(Integer.toString(molde.cantidadreal));
                }
            }
        });
        c.anchor = c.EAST;
        panelCentro.add(todo, c);
        c.gridy += 2;
        c.anchor = c.CENTER;

        
        JLabel info2 = new JLabel("Introduzca las coordenadas del territorio que desea invadir o reagrupar:");
        info2.setFont(new Font("Palatino Linotype", Font.BOLD, 15));
        info2.setForeground(Color.white);
        panelCentro.add(info2, c);
        c.gridy += 1;

        coordenadas = new JTextField(5);
        panelCentro.add(coordenadas, c);
        c.gridy += 1;

        //BOTON MOVILIZAR *********************************************************************
        JButton mov = new JButton("Movilizar");

        mov.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ArrayList<Integer> seleccionados = new ArrayList<Integer>();
                boolean valido = true;
                boolean enviar = false;
                int cantidad;

                //recogemos todas las cantidades de los campos de texto
                for(Molde5ataq molde : moldes){
                    try{
                    cantidad = Integer.parseInt(molde.cantidad3.getText());
                    if(cantidad <= molde.cantidadreal && cantidad > 0 && valido){
                        seleccionados.add(molde.getIdUnidad());
                        seleccionados.add(cantidad);
                        enviar = true;
                    }
                    else{
                        JOptionPane.showMessageDialog(panelCentro, "Cantidad incorrecta.");
                        valido = false;
                        enviar = false;
                    }

                    }catch(Exception e){
                        if(!molde.cantidad3.getText().equalsIgnoreCase("")){
                            JOptionPane.showMessageDialog(panelCentro, "Cantidad incorrecta.");
                            enviar = false;
                            valido = false;
                        }
                    }
                }

                //comrpobamos la mayor region que hay registrada en la base de datos
                salida.println("select max(idRegion) from regiones");
                String texto2;
                int regdestino = 0;
                int maxregion = 0;
                int idtropa = 0;
                try{
                    
                    while(!(texto2 = entrada.readLine()).equalsIgnoreCase("null")){
                        try{
                        maxregion = Integer.parseInt(texto2);
                            System.out.println(maxregion);
                        }catch(Exception e){
                            System.out.println("La jodimos al pasar maxregion.");
                        }
                    }
                    
                        try{
                        regdestino = Integer.parseInt(coordenadas.getText());

                        if(regdestino < 1 || regdestino > maxregion){ //y que la region destino se encuentre en un rango valido
                            JOptionPane.showMessageDialog(panelCentro, "Cantidad incorrecta.");
                            valido = false;
                        }
                        }catch(NumberFormatException e){
                            JOptionPane.showMessageDialog(panelCentro, "Cantidad incorrecta.");
                            valido = false;
                        }
                }catch(Exception e){
                    System.out.println(e.getMessage()+ " Fallo leer max region.");
                }

                
                salida.println("select max(idTropa) from movimientos");
                    try{
                        while(!(texto2 = entrada.readLine()).equalsIgnoreCase("null")){
                                try{
                                idtropa = Integer.parseInt(texto2);
                                }catch(Exception e){
                                    System.out.println("La jodimos al traspasar maxidTropa.");
                                }
                        }
                        if(idtropa == 0){ //con esto nos kitamos nulos repetidos cuando la consulta devuelve nulo
                            while(!(texto2 = entrada.readLine()).equalsIgnoreCase("null")){
                                System.out.println("nulo fuera");
                            }
                        }
                    }catch(Exception e){
                        System.out.println(e.getMessage() + " Fallo ver numero de tropa");
                    }

                //Si toda la extraccion de los campos se ha realizado de forma satisfactoria, hacemos la inserciones.
                if(enviar && valido){
                    for(int i = 0; i < seleccionados.size(); i=i+2){
                        salida.println("insertar INSERT INTO movimientos VALUES (s_movimientos.NEXTVAL, '"+ usuario + "',"+ (idtropa+1) +", "+
                                    seleccionados.get(i) +", " + idCurrentRegion + ", "+ regdestino + ", " + seleccionados.get(i+1)+")");

                        salida.println("insertar UPDATE despliegues SET cantidad = (select cantidad" + -seleccionados.get(i+1) + " from despliegues where idRegion = "+
                                        idCurrentRegion + " and idUnidad = " + seleccionados.get(i) + ") where idRegion = "+ idCurrentRegion + " AND "
                                        +"idUnidad = " + seleccionados.get(i));
                    }

                    //Reiniciamos los campos de texto y actualizamos la cantidad de efectivos en el territorio.
                    for (Molde5ataq molde : moldes){
                        molde.cantidad3.setText("");
                        for(int i = 0; i < seleccionados.size(); i=i+2){
                            if(seleccionados.get(i)==molde.idUnidad){
                                molde.cantidad2.setText(Integer.toString(molde.cantidadreal - seleccionados.get(i+1)));
                                molde.cantidadreal = molde.cantidadreal - seleccionados.get(i+1);
                            }
                        }
                    }
                    coordenadas.setText("");
                }
                else{
                    System.out.println(enviar);
                    System.out.println(valido);
                }
            }
        }); // fin de la accion del boton movilizar
        panelCentro.add(mov, c);
        c.gridy += 1;





        }// fin de sino vacio

        panelCentro.setPreferredSize(new Dimension(600, 500));
        currentBoton=9;
        actualizarOro();
        repaint();
        pack();
    }//GEN-LAST:event_b9ActionPerformed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Ynavi().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private static javax.swing.JButton b1;
    private static javax.swing.JButton b2;
    private javax.swing.JButton b3;
    private javax.swing.JButton b4;
    private javax.swing.JButton b5;
    private javax.swing.JButton b6;
    private javax.swing.JButton b7;
    private javax.swing.JButton b8;
    public javax.swing.JButton b9;
    private javax.swing.JLabel bdesconectar;
    private javax.swing.JLabel bsalir;
    private javax.swing.JLabel cabezera;
    private javax.swing.JLabel cabezera1;
    private javax.swing.JLabel cabezera2;
    private javax.swing.JScrollPane centro;
    private imagenes.PanelImagen fondo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jgd1;
    private javax.swing.JLabel jgd2;
    private javax.swing.JLabel oro1;
    public static javax.swing.JLabel oro2;
    private Yoshi.PanelTranslucido panelCentro;
    private javax.swing.JPanel panelScroll;
    private Yoshi.PanelTranslucido panelTranslucido1;
    private Yoshi.PanelTranslucido panelTranslucido2;
    private Yoshi.PanelTranslucido panelTranslucido3;
    private javax.swing.JScrollPane scroll;
    private javax.swing.JLabel titulo;
    // End of variables declaration//GEN-END:variables

}
