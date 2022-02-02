import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PalabrasEncontradas extends SimpleFileVisitor<Path> {


    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        String name = file.toAbsolutePath().toString();
        FileReader fi = null;

        try {
            fi = new FileReader(name);
        } catch (FileNotFoundException ex) {
            System.out.println(ex.getMessage());
            System.exit(-1);
        }

        BufferedReader in = new BufferedReader(new FileReader(name));


        String linea = null;

        if (name.toLowerCase().endsWith(".txt")) {


            int lineCount = 0;

            int wordCount = 0;

            int numberCount = 0;

            String delimiters = "\\s+|,\\s*|\\.\\s*|\\;\\s*|\\:\\s*|\\!\\s*|\\¡\\s*|\\¿\\s*|\\?\\s*|\\-\\s*"
                    + "|\\[\\s*|\\]\\s*|\\(\\s*|\\)\\s*|\\\"\\s*|\\_\\s*|\\%\\s*|\\+\\s*|\\/\\s*|\\#\\s*|\\$\\s*";


            // Lista con todas las palabras diferentes
            ArrayList<String> list = new ArrayList<String>();

            // Tiempo inicial
            long startTime = System.currentTimeMillis();

            try {
                while ((linea = in.readLine()) != null) {
                    lineCount++;

                    if (linea.trim().length() == 0) {
                        continue; // la linea esta vacia, continuar
                    }

                    // separar las palabras en cada linea
                    String words[] = linea.split(delimiters);

                    wordCount += words.length;

                    for (String theWord : words) {

                        theWord = theWord.toLowerCase().trim();

                        boolean isNumeric = true;

                        // verificar si el token es un numero
                        try {
                            Double num = Double.parseDouble(theWord);
                        } catch (NumberFormatException e) {
                            isNumeric = false;
                        }

                        // Si el token es un numero, pasar al siguiente
                        if (isNumeric) {
                            numberCount++;
                            continue;
                        }

                        // si la palabra no esta en la lista, agregar a la lista
                        if (!list.contains(theWord)) {
                            list.add(theWord);
                        }
                    }
                }
                // Obtener tiempo de ejecución
                long tiempoEjecucion = System.currentTimeMillis() - startTime;
                in.close();
                fi.close();

                System.out.printf("El directorio %-50s \n", name);
                System.out.printf("Tarda %2.3f  segundos en revisarse, tiene %2d lineas, tiene %3d palabras\n",
                        tiempoEjecucion / 1000.00, lineCount, wordCount - numberCount);
                System.out.printf("y cuenta con %5d palabras diferentes\n", list.size());



            } catch (IOException ex) {
                System.out.println(ex.getMessage());

            }
        }
        return super.visitFile(file, attrs);
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
        System.out.printf("No se puede procesar:%30s%n", file.toString()) ;
        return super.visitFileFailed(file, exc);
    }

    public static void main(String[] args)
            throws IOException {

        // /Users/rnavarro/datos
        if (args.length < 1) {
            System.exit(2);
        }

        // iniciar en este directorio
        Path startingDir = Paths.get(args[0]);

        // clase para procesar los archivos
        PalabrasEncontradas contadorLineas = new PalabrasEncontradas();

        // iniciar el recorrido de los archivos
        Files.walkFileTree(startingDir, contadorLineas);

    }}


