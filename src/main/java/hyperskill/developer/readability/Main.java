package hyperskill.developer.readability;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        if (args.length == 0) {
            return;
        }
        try {
            String content = new String(Files.readAllBytes(Paths.get(args[0])));
            System.out.println("The text is:");
            System.out.println(content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try (Scanner scanner = new Scanner(new File(args[0]))) {
            int charactersCount = 0;
            int wordsCount = 0;
            int sentencesCount = 0;
            int syllablesCount = 0;
            int polySyllablesCount = 0;

            while (scanner.hasNextLine()) {
                String textToEvaluate = scanner.nextLine();

                var sentences = textToEvaluate.split("[.!?]\\s");
                sentencesCount += sentences.length;

                for (String sentence : sentences) {
                    var words = sentence.trim().split("\\s");
                    wordsCount += words.length;
                    for (String word : words) {
                        syllablesCount += SyllableCounter.countSyllables(word);
                        if (SyllableCounter.isPolySyllable(word)) {
                            polySyllablesCount++;
                        }
                    }
                }

                charactersCount += textToEvaluate.replaceAll("[\\s\\t]", "").length();

            }
            System.out.printf("Words: %d%n" +
                            "Sentences: %d%n" +
                            "Characters: %d%n" +
                            "Syllables: %d%n" +
                            "Polysyllables: %d%n",
                    wordsCount, sentencesCount, charactersCount, syllablesCount, polySyllablesCount);

            System.out.print("Enter the score you want to calculate (ARI, FK, SMOG, CL, all): ");
            Scanner scanner1 = new Scanner(System.in);

            String scoreType = scanner1.next();
            while (scoreType.isEmpty()) {
                scoreType = scanner1.next();
            }

            double ariScore = getARIScore(charactersCount, wordsCount, sentencesCount);
            double fleschKincaidScore = getFleschKincaidScore(wordsCount, sentencesCount, syllablesCount);
            double sMOGIndex = getSMOGIndex(sentencesCount, polySyllablesCount);
            double colemanLiauIndex = getColemanLiauIndex(charactersCount, wordsCount, sentencesCount);

            System.out.println();

            switch (scoreType.toLowerCase()) {
                case "ari" -> System.out.printf("Automated Readability Index: %.2f (about %s-year-olds).%n", ariScore, getAgeRangeByScore((int) Math.ceil(ariScore)));
                case "fk" -> System.out.printf("Flesch–Kincaid readability tests: %.2f (about %s-year-olds).%n", fleschKincaidScore, getAgeRangeByScore((int) Math.ceil(fleschKincaidScore)));
                case "smog" -> System.out.printf("Simple Measure of Gobbledygook: %.2f (about %s-year-olds).%n", sMOGIndex, getAgeRangeByScore((int) Math.ceil(sMOGIndex)));
                case "cl" -> System.out.printf("Coleman–Liau index: %.2f (about %s-year-olds).%n", colemanLiauIndex, getAgeRangeByScore((int) Math.ceil(colemanLiauIndex)));
                case "all" -> {
                    System.out.printf("Automated Readability Index: %.2f (about %s-year-olds).%n", ariScore, getAgeRangeByScore((int) Math.ceil(ariScore)));
                    System.out.printf("Flesch–Kincaid readability tests: %.2f (about %s-year-olds).%n", fleschKincaidScore, getAgeRangeByScore((int) Math.ceil(fleschKincaidScore)));
                    System.out.printf("Simple Measure of Gobbledygook: %.2f (about %s-year-olds).%n", sMOGIndex, getAgeRangeByScore((int) Math.ceil(sMOGIndex)));
                    System.out.printf("Coleman–Liau index: %.2f (about %s-year-olds).%n", colemanLiauIndex, getAgeRangeByScore((int) Math.ceil(colemanLiauIndex)));
                }
                default -> System.out.println("Invalid!");
            }

            System.out.println();
            System.out.println("This text should be understood in average by 14.25-year-olds.");

            scanner1.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    private static double getARIScore(double charactersCount, int wordsCount, int sentencesCount) {
        return 4.71 * (charactersCount / wordsCount)
                + 0.5 * ((double) wordsCount / sentencesCount)
                - 21.43;
    }

    private static double getFleschKincaidScore(int wordsCount, int sentencesCount, int syllablesCount) {
        return 0.39 * ((double) wordsCount / sentencesCount)
                + 11.8 * ((double) syllablesCount / wordsCount)
                - 15.59;
    }

    private static double getSMOGIndex(int sentencesCount, int polysyllablesCount) {
        return 1.043 * Math.sqrt(polysyllablesCount * ((double) 30 / sentencesCount))
                + 3.1291;
    }

    private static double getColemanLiauIndex(int charactersCount, int wordsCount, int sentencesCount) {
        return 0.0588 * ((double) charactersCount / wordsCount) * 100 - 0.296 * ((double) sentencesCount / wordsCount) * 100 - 15.8;
    }

    private static String getAgeRangeByScore(int score) {
        return switch (score) {
            case 1 -> "6";
            case 2 -> "7";
            case 3 -> "8";
            case 4 -> "9";
            case 5 -> "10";
            case 6 -> "11";
            case 7 -> "12";
            case 8 -> "13";
            case 9 -> "14";
            case 10 -> "15";
            case 11 -> "16";
            case 12 -> "17";
            case 13 -> "18";
            default -> "22";
        };
    }
}


