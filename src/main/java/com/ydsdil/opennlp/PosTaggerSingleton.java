package com.ydsdil.opennlp;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import com.ydsdil.opennlp.model.RootWord;
import io.reactivex.Observable;
import opennlp.tools.lemmatizer.DictionaryLemmatizer;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.WhitespaceTokenizer;

public class PosTaggerSingleton {

    InputStream in;
    POSModel posModel;
    POSTaggerME tagger;
    DictionaryLemmatizer lemmatizer;

    private PosTaggerSingleton() {
    }

    private static volatile PosTaggerSingleton sInstance;

    public static PosTaggerSingleton INSTANCE() {

        if (sInstance == null) {
            synchronized (PosTaggerSingleton.class) {
                if (sInstance == null) {
                    sInstance = new PosTaggerSingleton();
                }
            }
        }

        return sInstance;
    }

    public Observable<Boolean> setUpOpenNlpPosTagger() throws IOException {

        return Observable.fromCallable(() -> {


                    InputStream dictLemmatizer;

                    try {
                        in = new FileInputStream("en-pos-maxent.bin");

                        posModel = new POSModel(in);

                        tagger = new POSTaggerME(posModel);

                        dictLemmatizer = new FileInputStream("en-lemmatizer.txt");

                        // loading the lemmatizer with dictionary
                        lemmatizer = new DictionaryLemmatizer(dictLemmatizer);

                        return true;

                    } catch (Exception ex) {
                        // Log.e("NLP", "message: " + ex.getMessage(), ex);
                        // proper exception handling here...
                    } finally {
                        if (in != null) {
                            in.close();
                        }
                    }

                    return false;
                }
        );
    }

    public Observable<RootWord> findPosWithOpenNlp(String word, String sentence) throws IOException {
        if (posModel != null) {
            return Observable.fromCallable(() -> getLemma(sentence, word));
        } else {
            System.out.println("NULL DÖNÜYOR");
            return setUpOpenNlpPosTagger().flatMap(aBoolean -> Observable.fromCallable(() -> getLemma(sentence, word)));
        }
    }

    public boolean isPosTaggerAvailable() {
        return posModel != null;
    }

    private RootWord getLemma(String sentence, String word) {

        sentence = sentence.replaceAll("[?.,:!&]*", "");

        WhitespaceTokenizer whitespaceTokenizer = WhitespaceTokenizer.INSTANCE;
        String[] tokens = whitespaceTokenizer.tokenize(sentence);

        String[] tags = tagger.tag(tokens);

        RootWord rootWord = new RootWord();

        String[] lemmas = lemmatizer.lemmatize(tokens, tags);

        int index = Arrays.asList(tokens).indexOf(word); // pass value

        if (index >= 0 && index + 1 <= tags.length) {
            rootWord.setOriginalWord(tokens[index]);

            System.out.println("#POSX: " + lemmas[index] + " | TAG: " + tags[index]);

            if (lemmas[index].equals("O")) {

                System.out.println("#POSX: 0 dönüyor NN ");
                //yalın isimlerde rootWord "0" olarak dönüyor. Bu nedenle orijinal kelimeyi set ediyoruz.
                rootWord.setRootWord(tokens[index]);
            } else {
                rootWord.setRootWord(lemmas[index]);
            }

            rootWord.setPosTag(tags[index]);
        }

        return rootWord;
    }
}