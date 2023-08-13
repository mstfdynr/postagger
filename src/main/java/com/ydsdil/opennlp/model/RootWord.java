package com.ydsdil.opennlp.model;

public class RootWord {
    String rootWord;
    String originalWord;
    String posTag;

    PartOfSpeech pos;

    public String getRootWord() {
        return rootWord;
    }

    public void setRootWord(String rootWord) {
        this.rootWord = rootWord;
    }

    public String getOriginalWord() {
        return originalWord;
    }

    public void setOriginalWord(String originalWord) {
        this.originalWord = originalWord;
    }

    public String getPosTag() {
        return posTag;
    }


    public void setPosTag(String posTag) {
        this.posTag = posTag;

        setPartOfSpeech();
    }

    public PartOfSpeech getPos() {
        return pos;
    }

    public void setPos(PartOfSpeech pos) {
        this.pos = pos;
    }

    public String printWord() {
        return "#WORD: \n"
                + "originalWord: " + this.originalWord + "\n"
                + "rootWord: " + this.rootWord + "\n"
                + "pos: " + this.pos.getName() + "\n";
    }

    private void setPartOfSpeech() {
        if (posTag.equals("NN") || posTag.equals("NNS") || posTag.equals("NNP") || posTag.equals("NNPS")) {
            setPos(PartOfSpeech.NOUN);
        } else if (posTag.equals("VB") || posTag.equals("VBD") || posTag.equals("VBG") || posTag.equals("VBN") || posTag.equals("VBP") || posTag.equals("VBZ")) {
            setPos(PartOfSpeech.VERB);
        } else if (posTag.equals("RB") || posTag.equals("RBR") || posTag.equals("RBS")) {
            setPos(PartOfSpeech.ADVERB);
        } else if (posTag.equals("JJ") || posTag.equals("JJR") || posTag.equals("JJS")) {
            setPos(PartOfSpeech.ADJECTIVE);
        } else if (posTag.equals("PRP") || posTag.equals("PRP$")) {
            setPos(PartOfSpeech.PRONOUN);
        } else if (posTag.equals("DT")) {
            setPos(PartOfSpeech.DETERMINER);
        } else if (posTag.equals("IN")) {
            setPos(PartOfSpeech.PREPOSITION);
        } else if (posTag.equals("MD")) {
            setPos(PartOfSpeech.MODAL);
        } else if (posTag.equals("UH")) {
            setPos(PartOfSpeech.INTERJECTION);
        } else if (posTag.equals("CC")) {
            setPos(PartOfSpeech.CONJUNCTION);
        } else {
            setPos(PartOfSpeech.OTHER);
        }
    }

    public enum PartOfSpeech {
        VERB("verb"),
        NOUN("noun"),
        ADVERB("adverb"),
        ADJECTIVE("adjective"),
        DETERMINER("determiner"),
        PREPOSITION("preposition"),
        MODAL("modal"),
        PRONOUN("pronoun"),
        CONJUNCTION("conjunction"),
        INTERJECTION("interjection"),
        PHRASAL_VERB("phrasal verb"),
        ARTICLE("article"),
        PHRASE("phrase"),
        PREP_PHRASE("prep. phrase"),
        IDIOM("idiom"),
        COLLOCATION("collocation"),
        QUANTIFIER("quantifier"),

        OTHER("other");

        private final String name;

        PartOfSpeech(String value) {
            name = value;
        }

        public String getName() {
            return name;
        }
    }

    public int getPosId() {

        switch (this.pos) {
            case NOUN:
                return 1;
            case VERB:
                return 2;
            case ADJECTIVE:
                return 3;
            case ADVERB:
                return 4;
            case PREPOSITION:
                return 5;
            case CONJUNCTION:
                return 6;
            case PHRASAL_VERB:
                return 7;
            case PRONOUN:
                return 8;
            case DETERMINER:
                return 9;
            case ARTICLE:
                return 9;
            case INTERJECTION:
                return 10;
            case PHRASE:
                return 11;
            case IDIOM:
                return 12;
            case COLLOCATION:
                return 13;
            case PREP_PHRASE:
                return 14;
            case QUANTIFIER:
                return 15;
            case MODAL:
                return 16;
            default:
                return 0;
        }
    }

    public static String getPartOfSpeechLabel(int posId) {

        switch (posId) {
            case 1:
                return "noun";
            case 2:
                return "verb";
            case 3:
                return "adj.";
            case 4:
                return "adverb";
            case 5:
                return "prep.";
            case 6:
                return "conj.";
            case 7:
                return "phrasal verb";
            case 8:
                return "pronoun";
            case 9:
                return "article & determiner";
            case 10:
                return "interjection";
            case 11:
                return "phrase";
            case 12:
                return "idiom";
            case 13:
                return "collocation";
            case 14:
                return "prepositional phrase";
            case 15:
                return "quantifier";
            case 16:
                return "modal";
            default:
                return "";
        }
    }

}
