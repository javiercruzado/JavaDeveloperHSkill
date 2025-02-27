package hyperskill.developer.readability;

class SyllableCounter {

    private static final String VOWELS = "aeiouy";
    private static final String CONSONANTS = "bcdfghjklmnpqrstvwxyz";

    public static int countSyllables(String word) {
        if (word == null || word.isEmpty()) {
            return 0;
        }

        word = word.toLowerCase().replaceAll("[^a-z]", ""); // Remove non-alphabetic characters and lowercase

        int count = 0;
        boolean lastWasVowel = false;
        //String vowels = "aeiouy"; // 'y' is sometimes a vowel

        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            if (VOWELS.indexOf(c) != -1) {
                if (!lastWasVowel) {
                    count++;
                    lastWasVowel = true;
                }
            } else {
                lastWasVowel = false;
            }
        }

        // Special cases:
        if (word.endsWith("e")) {
            count--;
        }

        return Math.max(1, count); // Ensure at least 1 syllable
    }

    public static boolean isPolySyllable(String word) {
        if (word == null || word.isEmpty()) {
            return false;
        }

        word = word.toLowerCase().replaceAll("[^a-z]", ""); // Remove non-alphabetic characters and lowercase

        int count = 0;
        boolean lastWasVowel = false;
        //String vowels = "aeiouy"; // 'y' is sometimes a vowel

        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            if (VOWELS.indexOf(c) != -1) {
                if (!lastWasVowel) {
                    count++;
                    lastWasVowel = true;
                }
            } else {
                lastWasVowel = false;
            }
        }

        // Special cases:
        if (word.endsWith("e")) {
            count--;
        }

        return Math.max(1, count) > 2; // Ensure at least 1 syllable
    }

    private static boolean isConsonant(char c) {
        return CONSONANTS.indexOf(c) != -1;
    }

}
