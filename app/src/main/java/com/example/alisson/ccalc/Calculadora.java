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

public class Calculadora extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private static final int REQ_CODE_SPEECH_INPUT = 100;
    private TextView telaResultado;
    private ImageButton mSpeakBtn;
    private String fraseFalada;
    private String resultadoFalado;
    private TextToSpeech tts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intencao = getIntent();
        setContentView(R.layout.activity_calculadora);
        tts = new TextToSpeech(this, this);



        telaResultado = (TextView) findViewById(R.id.resutado);
        mSpeakBtn = (ImageButton) findViewById(R.id.btnFalaCalculadora);

        mSpeakBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startVoiceInput();
            }
        });

    }

    private void startVoiceInput() {
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
                    fraseFalada = result.get(0);
                    String[] variaveis = fraseFalada.split(" ");
                    /*
                     * Para o reconhecimento dos casos dos numeros 1 e 2
                     * */
                    if(fraseFalada.equals("instrução")){
                        String text = "Para utilizar a calculadora basta falar o primeiro número," +
                                " em seguida o operador e depois o outro número. " +
                                "Para Somar fale - Mais;" +
                                "Para Subtrair fale - Menos;" +
                                "Para Divisão fale - dividido por;" +
                                "Para Multiplicação fale - Vezes" +
                                "Para Fechar a aplicação fale - Sair";
                        //tts.setSpeechRate(0.7f);
                        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
                    }else if(fraseFalada.equals("sair")){
                        System.exit(0);
                    }else {
                        if (variaveis[0].equals("um")) {
                            variaveis[0] = "1";
                        }
                        if (variaveis[2].equals("um")) {
                            variaveis[2] = "1";
                        }
                        if (variaveis[0].equals("dois")) {
                            variaveis[0] = "2";
                        }
                        if (variaveis[2].equals("dois")) {
                            variaveis[2] = "2";
                        }
                        /*
                         * Declaração dos resultados obtidos
                         * */
                        int numero1 = Integer.parseInt(variaveis[0]);
                        int numero2 = Integer.parseInt(variaveis[2]);
                        int resultado = 0;
                        String operador = variaveis[1];

                        if (operador.equals("+") || operador.equals("mais")) {
                            resultado = (numero1 + numero2);
                            telaResultado.setText(variaveis[0] + " + " + variaveis[2] + " = " + resultado);
                        }
                        if (operador.equals("-") || operador.equals("menos")) {
                            resultado = numero1 - numero2;
                            telaResultado.setText(variaveis[0] + " - " + variaveis[2] + " = " + resultado);
                        }
                        if (operador.equals("/") || operador.equals("dividido por")) {
                            resultado = numero1 / numero2;
                            telaResultado.setText(variaveis[0] + " / " + variaveis[2] + " = " + resultado);
                        }
                        if (operador.equals("x") || operador.equals("vezes")) {
                            resultado = numero1 * numero2;
                            telaResultado.setText(variaveis[0] + " * " + variaveis[2] + " = " + resultado);
                        }
                        resultadoFalado = "O resultado é: " + resultado;
                        speakOut();
                    }

                }

                break;
            }

        }
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
                String text = "Bem vindo a calculadora, Caso deseje instruções toque na tela e fale 'instrução'";
                //tts.setSpeechRate(0.7f);
                tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
            }
        } else {
            Log.e("TTS", "Initilization Failed!");
        }

    }

    private void speakOut() {
        String text = resultadoFalado;
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

}
