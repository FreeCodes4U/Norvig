package ir.chista.spellCorrector;

import com.google.gson.Gson;
import spark.Request;
import spark.Response;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import static spark.Spark.get;

/**
 * Created by AA on 1/17/17.
 */
public class Norvig {
    public static String[] alefba = {"آ", "ا", "ب", "پ", "ت", "ث", "ج ", "چ ", "ح", "خ", "د", "ذ", "ر", "ز", "ژ", "س", "ش", "ص", "ض", "ط", "ظ", "ع", "غ", "ف", "ق", "ک", "گ", "ل", "م", "ن", "و", "ه", "ی"};
    public HashMap<String, Integer> word_hashmap = new HashMap<String, Integer>();
    public String fileName, word;
    List<String> edit_List;

    public static void main(String[] args) {
        Norvig obj = new Norvig("شلام");
        Gson json = new Gson();
        get("/sc/:word", (Request request, Response response) ->
                new Norvig().getAnswer(request.params("word")), json::toJson);
    }

    /**
     * Constructor whit filename parameters
     *
     * @param fn
     */
    Norvig(String fn) {
        fileName = fn;
        edit_List = new ArrayList();
    }

    /**
     * Constructor
     */
    Norvig() {
        fileName = "files/golestan.txt";
        edit_List = new ArrayList();
    }

    /**
     * Create list of word by deleting characters
     * and add words to edit list
     */
    public void createDeleteList() {
        StringBuilder delete_sb = new StringBuilder();
        for (int i = 0; i < word.length(); i++) {
            delete_sb.append(word);
            delete_sb.deleteCharAt(i);
            edit_List.add(delete_sb.toString());
            delete_sb.setLength(0);
        }
    }

    /**
     * Create list of word by inserting alphabets between anya pairs of word characters
     * and add words  to edit list
     */
    public void createInsertList() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i <= word.length(); i++) {
            sb.setLength(0);
            sb.append(word);
            for (String c : alefba) {
                sb = new StringBuilder(word);
                sb.insert(i, c);
                edit_List.add(sb.toString());
            }
        }
    }

    /**
     * Create list of words by replacing any character of word to alphabets
     * and ad to edit list
     */
    public void createReplaceList() {
        StringBuilder replace_sb = new StringBuilder();
        for (int i = 0; i < word.length(); i++) {

            for (String c : alefba) {
                replace_sb.append(word);
                char c1 = c.charAt(0);
                replace_sb.setCharAt(i, c1);
                edit_List.add(replace_sb.toString());
                replace_sb.setLength(0);
            }
        }
    }

    /**
     * Create list of words by exchange position of ith and (i+1)th character
     * and add to edit list
     */
    public void createTransposeList() {
        StringBuilder transpose_sb = new StringBuilder();
        for (int i = 0; i < word.length() - 1; i++) {
            transpose_sb.append(word);
            char c1 = word.charAt(i);
            transpose_sb.setCharAt(i, word.charAt(i + 1));
            transpose_sb.setCharAt(i + 1, c1);
            edit_List.add(transpose_sb.toString());
            transpose_sb.setLength(0);
        }
    }

    /**
     * Read knowledge file and store in hashmap  data structure
     *
     * @throws IOException
     */
    public void getFile() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(fileName).getFile());

        Files.lines(Paths.get(file.getPath())).forEach(line -> {
            for (String word : line.split("\\s+")) {
                word_hashmap.putIfAbsent(word, 0);
                word_hashmap.put(word, word_hashmap.get(word) + 1);
            }
        });
    }

    /**
     * Find Maximum value and return related key (best word)
     *
     * @return
     */
    public String returnBestWord() {
        String answer = word;
        int maxFreq = Integer.MIN_VALUE;
        System.out.println("edit_words:");
        for (String s : edit_List)
            if (word_hashmap.containsKey(s) && maxFreq < word_hashmap.get(s)) {
                maxFreq = word_hashmap.get(s);
                answer = s;
            }
        return answer;
    }

    /**
     * Input word and call other methods to create complete edit list
     * and then read file and return answer by calling  eturnBestWord
     *
     * @param word
     * @return
     */
    public String getAnswer(String word) {
        this.word = word;
        this.createDeleteList();
        this.createReplaceList();
        this.createInsertList();
        this.createTransposeList();
        try {
            this.getFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this.returnBestWord();
    }
}