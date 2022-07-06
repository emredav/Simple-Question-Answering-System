package Main;

import java.io.*;
import java.util.*;

public class Main {


    public static int BoyerMooreHorspoolSearch(char[] pattern, char[] text) {

        int shift[] = new int[256];

        for (int k = 0; k < 256; k++) {
            shift[k] = pattern.length;
        }

        for (int k = 0; k < pattern.length - 1; k++) {
            shift[pattern[k]] = pattern.length - 1 - k;
        }

        int i = 0, j = 0;

        while ((i + pattern.length) <= text.length) {
            j = pattern.length - 1;

            while (text[i + j] == pattern[j]) {
                j -= 1;
                if (j < 0)
                    return i;
            }

            i = i + shift[text[i + pattern.length - 1]];
        }
        return -1;
    }


    public static String find(String pattern, char[] scriptC) {

        pattern = pattern.substring(0, pattern.length() - 1);

        String[] str = pattern.split(" ");

        pattern = str[(str.length - 5)] + " " + str[(str.length - 4)] + " " + str[(str.length - 3)] + " " + str[(str.length - 2)] + " " + str[(str.length - 1)];

        char[] patternC = pattern.toCharArray();
        int index = BoyerMooreHorspoolSearch(patternC, scriptC);

        if (index == -1) {
            pattern = str[0] + " " + str[1] + " " + str[2];
            patternC = pattern.toCharArray();
            index = BoyerMooreHorspoolSearch(patternC, scriptC);

            if (index == -1) {
                pattern = str[(str.length - 2)] + " " + str[(str.length - 1)];
                patternC = pattern.toCharArray();
                index = BoyerMooreHorspoolSearch(patternC, scriptC);
                if (index == -1) {
                    return "not found";
                }
            }
        }

        // find sentence
        int sentenceStart = 0;
        int sentenceEnd = 0;
        StringBuilder sentence = new StringBuilder();

        for (int i = index; scriptC[i] != '.'; i--) {
            sentenceStart = i + 1;
        }
        for (int i = index; scriptC[i] != '.'; i++) {
            sentenceEnd = i + 1;
        }

        for (int i = sentenceStart; i < sentenceEnd; i++) {
            sentence.append(scriptC[i]);
        }

        return sentence.toString();
    }

    public static ArrayList<String> compareText(String answer, String question) {

        question = question.substring(0, question.length() - 1);

        String[] questionArray = question.replaceAll("([a-z]+)[?:!.,;]*", "$1").split(" ");
        String[] answerArray = answer.replaceAll("([a-z]+)[?:!.,;]*", "$1").split(" ");

        ArrayList<String> resultList = new ArrayList<>();

        for (int i = 0; i < answerArray.length; i++) {
            for (String s : questionArray) {
                if (Objects.equals(answerArray[i], s)) {
                    answerArray[i] = "";
                    break;
                }
            }
        }

        for (String s : answerArray) {
            s = s.toLowerCase();
            if (!s.equals("") && !s.equals("the") && !s.equals("as") && !s.equals("a") && !s.equals("an")) {
                resultList.add(s);
            }
        }

        for (String r : resultList) {
            if (r.endsWith("s")) {
                r = r.substring(0, r.length() - 1);

                for (String q : questionArray) {
                    if (r.equals(q)) {
                        r += "s";
                        resultList.remove(r);
                    }
                }
            }
        }

        return resultList;
    }

    public static void main(String[] args) throws IOException {

        long start = System.nanoTime();

        File questions = new File("c:\\Users\\emred\\eclipse-workspace\\proje\\src\\proje\\questions.txt");
        File script = new File("c:\\Users\\emred\\eclipse-workspace\\proje\\src\\proje\\the_truman_show_script.txt");

        BufferedReader questionBuffer = new BufferedReader(new FileReader(questions));
        BufferedReader scriptBuffer = new BufferedReader(new FileReader(script));

        String scriptText = scriptBuffer.readLine();

        char[] scriptC = scriptText.toCharArray();

        int i = 1;
        String questionText;

        while ((questionText = questionBuffer.readLine()) != null) {
            String answerText = find(questionText, scriptC);
            ArrayList<String> oneWordAnswer = compareText(answerText, questionText);
            System.out.println("Question " + i + ": " + questionText);
            System.out.println("Answer   " + i + ": " + oneWordAnswer);
            i++;
            System.out.println();
        }

        long finish = System.nanoTime();
        long timeElapsed = finish - start;
        System.out.println(timeElapsed / 1000000.0 + " milliseconds");

    }
}
