package object_creation_and_destruction.item5_document_object_injection;

import java.util.List;

/**
 * Dependency Object Injection
 */
public class ITEM5_Document_Object_Injection {

    private final Lexicon dictionary;

    public ITEM5_Document_Object_Injection(Lexicon dictionary) {
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
