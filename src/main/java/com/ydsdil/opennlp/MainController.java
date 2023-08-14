package com.ydsdil.opennlp;

import com.ydsdil.opennlp.model.RootWord;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class MainController {


    @GetMapping({"/", "/home"})
    public String home() {
        return "Merhaba!";
    }

    @GetMapping("/welcome")
    public String welcome() {

        final String[] result = {""};
        final long startTime = System.currentTimeMillis();

        return "Welcome";
    }

    @PostMapping(path = "/tagger", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> getPos(@RequestParam(value = "param1") String word, @RequestParam(value = "param2") String sentence) {

        RootWord rootWord = new RootWord();

        final String[] result = {""};

        try {
            rootWord = PosTaggerSingleton.INSTANCE().findPosWithOpenNlp(word, sentence);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Map<String, Object> map = new LinkedHashMap<>();

        if(rootWord != null){

            if (rootWord.getRootWord() == null) {
                map.put("error", true);
                map.put("msg", "Kelime cümle içinde bulunamadı.");

            } else {
                map.put("error", false);
                map.put("originalWord",rootWord.getOriginalWord());
                map.put("rootWord",rootWord.getRootWord());
                map.put("posId",rootWord.getPosId());
                map.put("posTag",rootWord.getPosTag());
            }
        }else{
            map.put("error", true);
            map.put("msg", "POS Tagger hazır değil.");
        }

        return map;
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
            for (int i = 0; i < tokens.length; i++) {
                System.out.println(tokens[i] + "\t:\t" + tags[i] + "\t:\t" + probs[i]);
            }

        } catch (IOException e) {
            // Model loading failed, handle the error
            e.printStackTrace();
        } finally {
            if (tokenModelIn != null) {
                try {
                    tokenModelIn.close();
                } catch (IOException e) {
                }
            }
            if (posModelIn != null) {
                try {
                    posModelIn.close();
                } catch (IOException e) {
                }
            }
        }
    }

    public interface ResultHandler<T> {
        RootWord onSuccess();

        void onFailure(Exception e);
    }
}