package objectcreationanddestruction;

import java.util.List;

/**
 * Dependency Object Injection
 */
public class ITEM5_DOI {

    private final Lexicon dictionary;

    public ITEM5_DOI(Lexicon dictionary) {
        this.dictionary = dictionary;
    }

    public static boolean isValid(String word) {
        return true;
    }
    public static List<String> suggestions(String typo) {
        return null;
    }


    static class Lexicon {}
}
