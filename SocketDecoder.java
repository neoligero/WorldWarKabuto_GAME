/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Yoshi;

/**
 *
 * @author Administrador
 */
public class Mensaje { //divide los mensajes en palabras para facilitar el manejo para trabajar con los comandos
    String palabras[];
    int npalabras;

    public Mensaje (){
        palabras = null;
    }

    public Mensaje (String mensa){
    String mensaje = mensa.concat(" ");
    //System.out.println(mensaje);

    if(mensaje.length()>3)
        palabras = new String [mensaje.length()/2];
    else
        palabras = new String [mensaje.length()];
    int punto = 0;
    npalabras = 0;

    for (int i=0;i<mensaje.length();i++){
        if(Character.isWhitespace(mensaje.charAt(i))){
            palabras[npalabras] = mensaje.substring(punto, i);
            punto = i+1;
            npalabras++;
        }
    }
    }


    public void desencadenar(){
        String texto;
        int punto;
        boolean desconcatenado;

        for (int i=0;i<npalabras;i++){
            desconcatenado = false;
            texto = "";
            punto = 0;
            for (int j=0;j<palabras[i].length(); j++){
                if(palabras[i].charAt(j) == '_'){
                    texto = texto + palabras[i].substring(punto, j) + " ";
                    punto = j+1;
                    desconcatenado = true;
                }
                else if(palabras[i].charAt(j) == '#')
                {
                    texto = texto + palabras[i].substring(punto, j) + "\n";
                    punto = j+1;
                    desconcatenado = true;
                }

            }
            texto = texto + palabras[i].substring(punto, palabras[i].length());
            
            if(desconcatenado)
                palabras[i] = texto;
        }
    }


    public String getPalabra (int pos){
        return palabras[pos];
    }

    @Override
    public String toString(){
        String texto = palabras[0];

        for(int i=1;i<npalabras;i++){
            texto = texto + " " + palabras[i];
        }
        return (texto);
    }
} //fin de clase mensaje
