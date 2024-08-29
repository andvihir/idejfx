package com.ide.lenguaje;

import javafx.scene.control.TextArea;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.output.TeeOutputStream;

import javax.tools.*;
import java.io.*;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class Compilador {


    public static Class<?> compilarCodigoSeparado(File[] files, TextArea textoSalida, File raiz) throws Exception {
        StringBuilder stringBuilder = new StringBuilder();
        JavaCompiler compilador = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager fileManager = compilador.getStandardFileManager(null, Locale.ENGLISH, Charset.defaultCharset());


        //File directorioSalida = new File(files[0].getParentFile(), "class");
        File directorioSalida = new File(raiz, "build-out");
        FileUtils.forceMkdir(directorioSalida);

        fileManager.setLocation(StandardLocation.CLASS_OUTPUT, Collections.singletonList(directorioSalida));
        //Iterable<? extends JavaFileObject> fileObjects;
        //fileObjects = getJavaSourceFromString(programa);

        //System.out.println(programa);
        List<String> optionList = new ArrayList<String>();
// set compiler's classpath to be same as the runtime's
        optionList.addAll(Arrays.asList("-classpath",System.getProperty("java.class.path")));

// any other options you want
        //optionList.addAll(Arrays.asList(options));

        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
        //File[] files = new File[]{new File("C:/Users/And/a/a1/a.java")};
        Iterable<? extends JavaFileObject> compilationUnits1 =
                fileManager.getJavaFileObjectsFromFiles(Arrays.asList(files));

        Boolean call = compilador.getTask(null, fileManager, diagnostics, null, null, compilationUnits1).call();


        for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics()) {
            /*
            System.out.format("Error on line %d in %s%n",
                    diagnostic.getLineNumber(),
                    diagnostic.getSource().toUri());

             */
            stringBuilder.append(String.format("Error en linea %d en %s%n",
                    diagnostic.getLineNumber(),
                    diagnostic.getSource().toUri()));
        }

        /*
        Class<?> theClass = theClassLoader.loadClass("HelloWorld");
        Object theInstance = theClass.newInstance();
        System.out.println(theInstance);
        System.out.println(theClass);
        System.out.println(theClass.getName());
         */

        if(call) stringBuilder.append("Compilado con éxito.\n");
        else stringBuilder.append("No Compilado.\n");


        if(call){
            StringBuilder sb = new StringBuilder();
            for(JavaFileObject jfo : fileManager.list(StandardLocation.CLASS_OUTPUT, "", Collections.singleton(JavaFileObject.Kind.CLASS), true)){
                System.out.println(jfo.getName());
                String name = jfo.getName();
                String name1 = name.substring(name.indexOf("build-out"));
                String[] split = name1.split("\\\\");
                for(String s:split){
                    if(!s.contains(".class") && !s.contains("build-out")){
                        sb.append(s);
                        sb.append(".");
                    }else if (s.contains(".class")){
                        sb.append(s.substring(0, s.indexOf(".class")));
                    }
                }
                System.out.println(sb.toString());
                //Class<?> clase = classLoader.loadClass(jfo.toUri().toURL().toString());
            }
            ClassLoader classLoader = fileManager.getClassLoader(StandardLocation.CLASS_OUTPUT);

            Class<?> clase = classLoader.loadClass("a.HelloWorld99");
            //Class<?> clase = classLoader.loadClass(sb.toString());
            try{
                Method myMethod = clase.getMethod("main", String[].class);
                myMethod.setAccessible(true);
                String[] input = new String[] {};
                /*
                final ByteArrayOutputStream baos = new ByteArrayOutputStream();
                PrintStream ps = new PrintStream(baos);
                System.setOut(ps);


                //stringBuilder.append(myMethod.getReturnType().getSimpleName()).append(" ");
                String data = baos.toString("UTF-8");
                stringBuilder.append(data);
                ps.close();
                System.setOut(System.out);
                System.out.println(data);
                */

                myMethod.invoke(null, (Object) input);
                /*
                PipedOutputStream pOut = new PipedOutputStream();
                System.setOut(new PrintStream(pOut));
                PipedInputStream pIn = new PipedInputStream(pOut);
                BufferedReader reader = new BufferedReader(new InputStreamReader(pIn));


                try{
                    long length = 0;
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (line.isEmpty()) {
                            break;
                        }
                        length += line.length();
                        stringBuilder.append("\n");
                        stringBuilder.append(line);
                    }
                    System.out.println("Read length: " + length);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                System.setOut(System.out);

                 */


            }catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }

        }
        fileManager.close();
        textoSalida.setText(stringBuilder.toString());

        return null;


        //System.out.println(compilador.getTask(null, null, null ,null , null, fileObjects).call());
    /*
        Class<?> clase = Class.forName(nombreClase);
        Method method = clase.getMethod("main", String[].class);
        method.invoke(null, (Object) new String[]{});
*/

    }


    public static ClassLoader compilarCodigoTotalDevuelveClaseTest(File[] files, TextArea textoSalida, File raiz) throws Exception {

        ClassLoader classLoader = null;
        StringBuilder stringBuilder = new StringBuilder();
        JavaCompiler compilador = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager fileManager = compilador.getStandardFileManager(null, Locale.getDefault(), Charset.defaultCharset());


        //File directorioSalida = new File(files[0].getParentFile(), "class");
        File directorioSalida = new File(raiz, ".classes");
        FileUtils.forceMkdir(directorioSalida);

        fileManager.setLocation(StandardLocation.CLASS_OUTPUT, Collections.singletonList(directorioSalida));
        //Iterable<? extends JavaFileObject> fileObjects;
        //fileObjects = getJavaSourceFromString(programa);

        //System.out.println(programa);
        List<String> optionList = new ArrayList<String>();
// set compiler's classpath to be same as the runtime's
        //optionList.addAll(Arrays.asList("-classpath",System.getProperty("java.class.path")));

        optionList.add("-classpath");
        optionList.add(".classes");


// any other options you want
        //optionList.addAll(Arrays.asList(options));

        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
        //File[] files = new File[]{new File("C:/Users/And/a/a1/a.java")};
        Iterable<? extends JavaFileObject> compilationUnits =
                fileManager.getJavaFileObjectsFromFiles(Arrays.asList(files));

        List<JavaFileObject> cU1 = new ArrayList<>();
        compilationUnits.forEach(f-> {
            if(f.getKind().equals(JavaFileObject.Kind.SOURCE)) cU1.add(f);
        });


        Boolean call = compilador.getTask(null, fileManager, diagnostics, optionList, null, cU1).call();



        for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics()) {
            /*
            System.out.format("Error on line %d in %s%n",
                    diagnostic.getLineNumber(),
                    diagnostic.getSource().toUri());

             */


            stringBuilder.append(String.format("Error en linea %d y columna %d en %s%n",
                    diagnostic.getLineNumber(),
                    diagnostic.getColumnNumber(),
                    diagnostic.getSource().toUri()));
        }

        /*
        Class<?> theClass = theClassLoader.loadClass("HelloWorld");
        Object theInstance = theClass.newInstance();
        System.out.println(theInstance);
        System.out.println(theClass);
        System.out.println(theClass.getName());
         */

        if(call) stringBuilder.append("Compilado con éxito.\n");
        else stringBuilder.append("No Compilado.\n");
        Class<?> clase = null;
        if(call){
            /*
            StringBuilder sb = new StringBuilder();
            for(JavaFileObject jfo : fileManager.list(StandardLocation.CLASS_OUTPUT, "", Collections.singleton(JavaFileObject.Kind.CLASS), true)){
                System.out.println(jfo.getName());
                String name = jfo.getName();
                String name1 = name.substring(name.indexOf("build-out"));
                String[] split = name1.split("\\\\");
                for(String s:split){
                    if(!s.contains(".class") && !s.contains("build-out")){
                        sb.append(s);
                        sb.append(".");
                    }else if (s.contains(".class")){
                        sb.append(s.substring(0, s.indexOf(".class")));
                    }
                }
                System.out.println(sb.toString());
                //Class<?> clase = classLoader.loadClass(jfo.toUri().toURL().toString());
            }
             */

            classLoader = fileManager.getClassLoader(StandardLocation.CLASS_OUTPUT);

           // clase = classLoader.loadClass("a.HelloWorld99");


            //Class<?> clase = classLoader.loadClass(sb.toString());
                /*
            try{
                Method myMethod = clase.getMethod("main", String[].class);
                myMethod.setAccessible(true);
                String[] input = new String[] {};
                final ByteArrayOutputStream baos = new ByteArrayOutputStream();
                PrintStream ps = new PrintStream(baos);
                System.setOut(ps);


                //stringBuilder.append(myMethod.getReturnType().getSimpleName()).append(" ");
                String data = baos.toString("UTF-8");
                stringBuilder.append(data);
                ps.close();
                System.setOut(System.out);
                System.out.println(data);


                myMethod.invoke(null, (Object) input);

                PipedOutputStream pOut = new PipedOutputStream();
                System.setOut(new PrintStream(pOut));
                PipedInputStream pIn = new PipedInputStream(pOut);
                BufferedReader reader = new BufferedReader(new InputStreamReader(pIn));


                try{
                    long length = 0;
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (line.isEmpty()) {
                            break;
                        }
                        length += line.length();
                        stringBuilder.append("\n");
                        stringBuilder.append(line);
                    }
                    System.out.println("Read length: " + length);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                System.setOut(System.out);



            }catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            */

        }
        fileManager.close();
        textoSalida.setText(stringBuilder.toString());

        return classLoader;


        //System.out.println(compilador.getTask(null, null, null ,null , null, fileObjects).call());
    /*
        Class<?> clase = Class.forName(nombreClase);
        Method method = clase.getMethod("main", String[].class);
        method.invoke(null, (Object) new String[]{});
*/

    }

}
