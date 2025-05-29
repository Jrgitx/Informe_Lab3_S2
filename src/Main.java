import java.util.ArrayList;
import java.util.Random;
import java.util.Collections;

import java.io.FileWriter; // Necesario para archivo csv
import java.io.IOException; // Necesario para archivo csv


public class Main {

    public static class Game{
        private String name;
        private String category;
        private int price;
        private int quality;

        public Game(String name, String category, int price, int quality){
            this.name = name;
            this.category = category;
            this.price = price;
            this.quality = quality;
        }

        public String getName() { return name; }
        public String getCategory() { return category; }
        public int getPrice() { return price; }
        public int getQuality() { return quality; }

        //Resumen Juego Legible
        public String resumenJuego(){
            return "Título: " + name + "|Categoría/Género: " + category + "|Precio: $ " + price + "|Calidad: " + quality + "/100";
        }

    }

    public class Dataset{
        private ArrayList<Game> data; /** Lista de videojuegos */
        private String sortedByAttribute; // Indica por cuál campo está ordenado actualmente el dataset (precio, categoria o quality)

        public Dataset(ArrayList<Game> data){
            this.data = data;
            this.sortedByAttribute = null; // Se inicializa como nulo, ya que no está ordenado al principio
        }

        public ArrayList<Game> getGamesByPrice(int price) {
            ArrayList<Game> result = new ArrayList<>();
            if ("price".equals(sortedByAttribute)) {
                int left = 0, right = data.size() - 1;
                while (left <= right) {
                    int mid = (left + right) / 2;
                    int midPrice = data.get(mid).getPrice();
                    if (midPrice == price) {
                        int i = mid;
                        while (i >= 0 && data.get(i).getPrice() == price) result.add(0, data.get(i--));
                        i = mid + 1;
                        while (i < data.size() && data.get(i).getPrice() == price) result.add(data.get(i++));
                        break;
                    } else if (midPrice < price) {
                        left = mid + 1;
                    } else {
                        right = mid - 1;
                    }
                }
            } else {
                for (Game g : data) {
                    if (g.getPrice() == price) result.add(g);
                }
            }
            return result;
        }

        public ArrayList<Game> getGamesByPriceRange(int lowerPrice, int higherPrice) {
            ArrayList<Game> result = new ArrayList<>();
            if ("price".equals(sortedByAttribute)) {
                for (Game g : data) {
                    if (g.getPrice() > higherPrice) break;
                    if (g.getPrice() >= lowerPrice && g.getPrice() <= higherPrice) result.add(g);
                }
            } else {
                for (Game g : data) {
                    if (g.getPrice() >= lowerPrice && g.getPrice() <= higherPrice) result.add(g);
                }
            }
            return result;
        }

        public ArrayList<Game> getGamesByCategory(String category) {
            ArrayList<Game> result = new ArrayList<>();
            if ("category".equals(sortedByAttribute)) {
                for (Game g : data) {
                    if (g.getCategory().equals(category)) result.add(g);
                    else if (!result.isEmpty()) break;
                }
            } else {
                for (Game g : data) {
                    if (g.getCategory().equals(category)) result.add(g);
                }
            }
            return result;
        }

        public ArrayList<Game> getGamesByQuality(int quality) {
            ArrayList<Game> result = new ArrayList<>();
            if ("quality".equals(sortedByAttribute)) {
                for (Game g : data) {
                    if (g.getQuality() == quality) result.add(g);
                    else if (!result.isEmpty() && g.getQuality() != quality) break;
                }
            } else {
                for (Game g : data) {
                    if (g.getQuality() == quality) result.add(g);
                }
            }
            return result;
        }

        public ArrayList<Game> sortByAlgorithm(String algorithm, String attribute) {
            if ("bubbleSort".equals(algorithm)) {
                bubbleSort(attribute);
            } else if ("quickSort".equals(algorithm)) {
                quickSort(0, data.size() - 1, attribute);
            } else if ("insertionSort".equals(algorithm)) {
                insertionSort(attribute);
            } else if ("selectionSort".equals(algorithm)) {
                selectionSort(attribute);
            } else if ("mergeSort".equals(algorithm)) {
                mergeSort(0, data.size() - 1, attribute);
            } else {
                Collections.sort(data, (a, b) -> {
                    switch (attribute) {
                        case "price": return Integer.compare(a.getPrice(), b.getPrice());
                        case "quality": return Integer.compare(a.getQuality(), b.getQuality());
                        case "category": return a.getCategory().compareTo(b.getCategory());
                        default: return Integer.compare(a.getPrice(), b.getPrice());
                    }
                });
            }
            sortedByAttribute = attribute;
            return data;
        }

        /**Bubble Sort */
        private void bubbleSort(String attribute) {
            for (int i = 0; i < data.size() - 1; i++) {
                for (int j = 0; j < data.size() - i - 1; j++) {
                    Game a = data.get(j);
                    Game b = data.get(j + 1);
                    boolean shouldSwap = false;
                    switch (attribute) {
                        case "price":
                            shouldSwap = a.getPrice() > b.getPrice();
                            break;
                        case "quality":
                            shouldSwap = a.getQuality() > b.getQuality();
                            break;
                        case "category":
                            shouldSwap = a.getCategory().compareTo(b.getCategory()) > 0;
                            break;
                        default:
                            shouldSwap = a.getPrice() > b.getPrice();
                    }
                    if (shouldSwap) {
                        data.set(j, b);
                        data.set(j + 1, a);
                    }
                }
            }
        }

        /**Quick Sort */
        private void quickSort(int low, int high, String attribute) {
            if (low < high) {
                int pi = partition(low, high, attribute);
                quickSort(low, pi - 1, attribute);
                quickSort(pi + 1, high, attribute);
            }
        }

        private int partition(int low, int high, String attribute) { // es el corazón del algoritmo de QuickSort
            Game pivot = data.get(high);
            int i = (low - 1);
            for (int j = low; j < high; j++) {
                boolean condition = false;
                switch (attribute) {
                    case "price":
                        condition = data.get(j).getPrice() <= pivot.getPrice();
                        break;
                    case "quality":
                        condition = data.get(j).getQuality() <= pivot.getQuality();
                        break;
                    case "category":
                        condition = data.get(j).getCategory().compareTo(pivot.getCategory()) <= 0;
                        break;
                    default:
                        condition = data.get(j).getPrice() <= pivot.getPrice();
                }
                if (condition) {
                    i++;
                    Game temp = data.get(i);
                    data.set(i, data.get(j));
                    data.set(j, temp);
                }
            }
            Game temp = data.get(i + 1);
            data.set(i + 1, data.get(high));
            data.set(high, temp);
            return i + 1;
        }

        /**Insertion Sort */
        private void insertionSort(String attribute) {
            for (int i = 1; i < data.size(); i++) {
                Game key = data.get(i);
                int j = i - 1;

                while (j >= 0) {
                    boolean condition = false;
                    switch (attribute) {
                        case "price":
                            condition = data.get(j).getPrice() > key.getPrice();
                            break;
                        case "quality":
                            condition = data.get(j).getQuality() > key.getQuality();
                            break;
                        case "category":
                            condition = data.get(j).getCategory().compareTo(key.getCategory()) > 0;
                            break;
                        default:
                            condition = data.get(j).getPrice() > key.getPrice();
                    }

                    if (!condition) break;

                    data.set(j + 1, data.get(j));
                    j--;
                }
                data.set(j + 1, key);
            }
        }

        /**Selection Sort */
        private void selectionSort(String attribute) {
            int n = data.size();
            for (int i = 0; i < n - 1; i++) {
                int minIdx = i;
                for (int j = i + 1; j < n; j++) {
                    boolean condition = false;
                    switch (attribute) {
                        case "price":
                            condition = data.get(j).getPrice() < data.get(minIdx).getPrice();
                            break;
                        case "quality":
                            condition = data.get(j).getQuality() < data.get(minIdx).getQuality();
                            break;
                        case "category":
                            condition = data.get(j).getCategory().compareTo(data.get(minIdx).getCategory()) < 0;
                            break;
                        default:
                            condition = data.get(j).getPrice() < data.get(minIdx).getPrice();
                    }

                    if (condition) {
                        minIdx = j;
                    }
                }

                // Intercambio si es necesario
                if (minIdx != i) {
                    Game temp = data.get(i);
                    data.set(i, data.get(minIdx));
                    data.set(minIdx, temp);
                }
            }
        }

        /**Merge Sort */
        private void mergeSort(int left, int right, String attribute) {
            if (left < right) {
                int mid = (left + right) / 2;
                mergeSort(left, mid, attribute);
                mergeSort(mid + 1, right, attribute);
                merge(left, mid, right, attribute);
            }
        }

        private void merge(int left, int mid, int right, String attribute) {
            ArrayList<Game> leftList = new ArrayList<>(data.subList(left, mid + 1));
            ArrayList<Game> rightList = new ArrayList<>(data.subList(mid + 1, right + 1));

            int i = 0, j = 0, k = left;

            while (i < leftList.size() && j < rightList.size()) {
                boolean condition = false;
                Game a = leftList.get(i);
                Game b = rightList.get(j);
                switch (attribute) {
                    case "price":
                        condition = a.getPrice() <= b.getPrice();
                        break;
                    case "quality":
                        condition = a.getQuality() <= b.getQuality();
                        break;
                    case "category":
                        condition = a.getCategory().compareTo(b.getCategory()) <= 0;
                        break;
                    default:
                        condition = a.getPrice() <= b.getPrice();
                }
                if (condition) {
                    data.set(k++, a);
                    i++;
                } else {
                    data.set(k++, b);
                    j++;
                }
            }

            while (i < leftList.size()) {
                data.set(k++, leftList.get(i++));
            }
            while (j < rightList.size()) {
                data.set(k++, rightList.get(j++));
            }
        }


    }

    public class GenerateData {

        // Arreglos para la generación aleatoria como se especifica en el laboratorio
        private static final String[] PALABRAS = {"Dragon", "Empire", "Quest", "Galaxy", "Legends", "Warrior", "Shadow", "Final"};
        private static final String[] CATEGORIAS = {"Acción", "Aventura", "Estrategia", "RPG", "Deportes", "Simulación"};

        public static ArrayList<Game> generateGames(int n) {
            ArrayList<Game> games = new ArrayList<>();
            Random random = new Random();

            for (int i = 0; i < n; i++) {
                // Regla para name: Concatenar dos palabras al azar
                String name = PALABRAS[random.nextInt(PALABRAS.length)] + " " + PALABRAS[random.nextInt(PALABRAS.length)];

                // Regla para category: Elegir una del arreglo
                String category = CATEGORIAS[random.nextInt(CATEGORIAS.length)];

                // Regla para price: Entero aleatorio entre 0 y 70.000
                int price = random.nextInt(70001);

                // Regla para quality: Entero aleatorio entre 0 y 100
                int quality = random.nextInt(101);

                games.add(new Game(name, category, price, quality));
            }
            return games;
        }

    }

    public class SaveData {

        // Guarda una lista de juegos en un archivo CSV
        public static void saveGamesToCSV(ArrayList<Main.Game> games, String filename) {
            try (FileWriter writer = new FileWriter(filename)) {
                // Escribir encabezado
                writer.write("Name, Category, Price, Quality\n");

                // Escribir cada juego
                for (Main.Game game : games) {
                    writer.write(String.format("%s, %s, %d, %d\n",
                            game.getName(),
                            game.getCategory(),
                            game.getPrice(),
                            game.getQuality()));
                }

                System.out.println("Archivo guardado correctamente: " + filename);
            } catch (IOException e) {
                System.err.println("Error al guardar el archivo: " + e.getMessage());
            }
        }

        public static void saveBenchmark(String algoritmo, String atributo, String tam, long tiempo) {
            String archivo = "benchmark_" + algoritmo + ".csv";
            try (FileWriter writer = new FileWriter(archivo, true)) {
                writer.write(algoritmo + ", " + atributo + ", " + tam + ", " + tiempo + "\n");
            } catch (IOException e) {
                System.err.println("Error al guardar el benchmark: " + e.getMessage());
            }
        }
    }

    // Medir tiempo de algoritmos
    public static void medirTiempo(String algoritmo, String atributo, ArrayList<Game> dataset, String nombreDataset) {
        Main.Dataset ds = new Main().new Dataset(new ArrayList<>(dataset));
        long start = System.currentTimeMillis();
        ds.sortByAlgorithm(algoritmo, atributo);
        long end = System.currentTimeMillis();
        long tiempo = end - start;
        System.out.println("[" + nombreDataset + "] " + algoritmo + " ordenando por " + atributo + ": " + tiempo + " ms");
        SaveData.saveBenchmark(algoritmo, atributo, nombreDataset, tiempo);
    }


    public static void main(String[] args) {
        // Tarea: Generar datasets de 10^2, 10^4, y 10^6 elementos
        ArrayList<Game> smallDataset = GenerateData.generateGames(100);
        ArrayList<Game> mediumDataset = GenerateData.generateGames(10000);
        ArrayList<Game> largeDataset = GenerateData.generateGames(1000000);

        // Tarea: Guardar estos datasets en archivos .csv o .txt
        // Ejemplo de guardado (aquí implementarías la lógica para escribir en archivos)
        System.out.println("Generados " + smallDataset.size() + " juegos.");
        System.out.println("Generados " + mediumDataset.size() + " juegos.");
        System.out.println("Generados " + largeDataset.size() + " juegos.");

        SaveData.saveGamesToCSV(smallDataset, "games_100.csv");
        SaveData.saveGamesToCSV(mediumDataset, "games_10000.csv");
        SaveData.saveGamesToCSV(largeDataset, "games_1000000.csv");

        /**Bubble Sort */
        //medirTiempo("bubbleSort", "price", smallDataset, "100");
        //medirTiempo("bubbleSort", "price", mediumDataset, "10k");
        //medirTiempo("bubbleSort", "price", largeDataset, "1M");

        //medirTiempo("bubbleSort", "quality", smallDataset, "100");
        //medirTiempo("bubbleSort", "quality", mediumDataset, "10k");
        //medirTiempo("bubbleSort", "quality", largeDataset, "1M");

        //medirTiempo("bubbleSort", "category", smallDataset, "100");
        //medirTiempo("bubbleSort", "category", mediumDataset, "10k");
        //medirTiempo("bubbleSort", "category", largeDataset, "1M");

        /**Insertion Sort */
        //medirTiempo("insertionSort", "price", smallDataset, "100");
        //medirTiempo("insertionSort", "price", mediumDataset, "10k");
        //medirTiempo("insertionSort", "price", largeDataset, "1M");

        //medirTiempo("insertionSort", "quality", smallDataset, "100");
        //medirTiempo("insertionSort", "quality", mediumDataset, "10k");
        //medirTiempo("insertionSort", "quality", largeDataset, "1M");

        //medirTiempo("insertionSort", "category", smallDataset, "100");
        //medirTiempo("insertionSort", "category", mediumDataset, "10k");
        //medirTiempo("insertionSort", "price", largeDataset, "1M");


        /**Selection Sort */
        //medirTiempo("selectionSort", "price", smallDataset, "100");
        //medirTiempo("selectionSort", "price", mediumDataset, "10k");
        //medirTiempo("selectionSort", "price", largeDataset, "1M");

        //medirTiempo("selectionSort", "quality", smallDataset, "100");
        //medirTiempo("selectionSort", "quality", mediumDataset, "10k");
        //medirTiempo("selectionSort", "quality", largeDataset, "1M");

        //medirTiempo("selectionSort", "category", smallDataset, "100");
        //medirTiempo("selectionSort", "category", mediumDataset, "10k");
        //medirTiempo("selectionSort", "category", largeDataset, "1M");


        /**Merge Sort */
        //medirTiempo("mergeSort", "price", smallDataset, "100");
        //medirTiempo("mergeSort", "price", mediumDataset, "10k");
        //medirTiempo("mergeSort", "price", largeDataset, "1M");

        //medirTiempo("mergeSort", "quality", smallDataset, "100");
        //medirTiempo("mergeSort", "quality", mediumDataset, "10k");
        //medirTiempo("mergeSort", "quality", largeDataset, "1M");

        //medirTiempo("mergeSort", "category", smallDataset, "100");
        //medirTiempo("mergeSort", "category", mediumDataset, "10k");
        //medirTiempo("mergeSort", "category", largeDataset, "1M");


        /**Quick Sort*/
        //medirTiempo("quickSort", "price", smallDataset, "100");
        //medirTiempo("quickSort", "price", mediumDataset, "10k");
        //medirTiempo("quickSort", "price", largeDataset, "1M");

        //medirTiempo("quickSort", "quality", smallDataset, "100");
        //medirTiempo("quickSort", "quality", mediumDataset, "10k");
        //medirTiempo("quickSort", "quality", largeDataset, "1M");

        //medirTiempo("quickSort", "category", smallDataset, "100");
        //medirTiempo("quickSort", "category", mediumDataset, "10k");
        //medirTiempo("quickSort", "category", largeDataset, "1M");


        /**Collections Sort */
        //medirTiempo("collections", "price", smallDataset, "100");
        //medirTiempo("collections", "price", mediumDataset, "10k");
        //medirTiempo("collections", "price", largeDataset, "1M");

        //medirTiempo("collections", "quality", smallDataset, "100");
        //medirTiempo("collections", "quality", mediumDataset, "10k");
        //medirTiempo("collections", "quality", largeDataset, "1M");

        //medirTiempo("collections", "category", smallDataset, "100");
        //medirTiempo("collections", "category", mediumDataset, "10k");
        //medirTiempo("collections", "category", largeDataset, "1M");

    }

}

