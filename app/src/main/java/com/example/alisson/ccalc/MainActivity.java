package com.example.alisson.ccalc;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener{

    private static final int REQ_CODE_SPEECH_INPUT = 100;
    private ImageButton mSpeakBtn;
    private String opcao;
    private TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tts = new TextToSpeech(this, this);

        mSpeakBtn = (ImageButton) findViewById(R.id.btnFala);
        mSpeakBtn.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                startVoiceInput();
            }
        });
    }

    private void startVoiceInput() {
        tts.stop();
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hello, How can I help you?");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    opcao = result.get(0);
                    if (opcao.equals("Abrir calculadora")){
                        abrirCalculadora();
                    }
                }
                break;
            }

        }

    }
    public void abrirCalculadora(){
        Intent intencao = new Intent(this,Calculadora.class);
        startActivity(intencao);
    }

    @Override
    public void onDestroy() {
        // Don't forget to shutdown tts!
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

    @Override
    public void onInit(int status) {

        if (status == TextToSpeech.SUCCESS) {

            int result = tts.setLanguage(Locale.getDefault());

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            }else{
                String text = "Bem vindo ao app, Para abrir a calculadora toque na tela e fale 'abrir calculadora'";
                //tts.setSpeechRate(0.7f);
                tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
            }
        } else {
            Log.e("TTS", "Initilization Failed!");
        }

    }

}
