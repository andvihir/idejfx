package com.ide.plantillas;

public class Plantilla {

        public static String crearArchivoJava(String nombrePaquete, String nombre, TipoArchivoJava tipoArchivoJava){
            StringBuilder stringBuilder = new StringBuilder();
            if(nombrePaquete!=null && !nombrePaquete.isEmpty()){
                stringBuilder.append("package " + nombrePaquete+";");
                stringBuilder.append("\n");
                stringBuilder.append("\n");
            }
            if(!nombre.isEmpty()){
                stringBuilder.append("public " + tipoArchivoJava.toString().toLowerCase() + " " + nombre + "() {\n");
                stringBuilder.append("}");

            }
            return stringBuilder.toString();
        }

        public enum TipoArchivoJava {
            CLASS,
            INTERFACE,
            RECORD,
            ENUM
        }
}
