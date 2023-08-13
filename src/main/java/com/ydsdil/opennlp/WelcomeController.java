package com.ydsdil.opennlp;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@RestController
public class WelcomeController {

    @GetMapping("/welcome")
    public String welcome(){

        final String[] result = {""};
        final long startTime = System.currentTimeMillis();

        try {
            PosTaggerSingleton.INSTANCE().setUpOpenNlpPosTagger()
                    .subscribeOn(Schedulers.io())
                    .doOnComplete(() -> {
                        long endTime = System.currentTimeMillis();

                        System.out.println("Took: " + (endTime - startTime));
                    })
                    .observeOn(Schedulers.single())
                    .subscribe(aLong -> {
                        System.out.println("POS Tagger kuruldu.");
                        getPos();
                    });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }



        return result[0];
    }

    public void getPos(){
        if (PosTaggerSingleton.INSTANCE().isPosTaggerAvailable()) {

            try {
                PosTaggerSingleton.INSTANCE().findPosWithOpenNlp("important", "This is an important decision.")
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.single())
                        .subscribe(rootWord -> {
                            //result[0] = rootWord.getRootWord() + " | " + rootWord.getPosTag();
                            // lookUpWord(rootWord);
                        });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


    public static void openNlp() {

        InputStream tokenModelIn = null;
        InputStream posModelIn = null;

        try {
            String sentence = "John is 27 years old.";
            // tokenize the sentence
            tokenModelIn = new FileInputStream("en-token.bin");
            TokenizerModel tokenModel = new TokenizerModel(tokenModelIn);
            Tokenizer tokenizer = new TokenizerME(tokenModel);
            String tokens[] = tokenizer.tokenize(sentence);

            // Parts-Of-Speech Tagging
            // reading parts-of-speech model to a stream
            posModelIn = new FileInputStream("en-pos-maxent.bin");
            // loading the parts-of-speech model from stream
            POSModel posModel = new POSModel(posModelIn);
            // initializing the parts-of-speech tagger with model
            POSTaggerME posTagger = new POSTaggerME(posModel);
            // Tagger tagging the tokens
            String tags[] = posTagger.tag(tokens);
            // Getting the probabilities of the tags given to the tokens
            double probs[] = posTagger.probs();

            System.out.println("Token\t:\tTag\t:\tProbability\n---------------------------------------------");
            for(int i=0;i<tokens.length;i++){
                System.out.println(tokens[i]+"\t:\t"+tags[i]+"\t:\t"+probs[i]);
            }

        }
        catch (IOException e) {
            // Model loading failed, handle the error
            e.printStackTrace();
        }
        finally {
            if (tokenModelIn != null) {
                try {
                    tokenModelIn.close();
                }
                catch (IOException e) {
                }
            }
            if (posModelIn != null) {
                try {
                    posModelIn.close();
                }
                catch (IOException e) {
                }
            }
        }
    }
}