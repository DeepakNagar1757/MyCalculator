package com.example.mycalculator;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {

    private TextView solutionTextView, resultTextView;
    private StringBuilder input = new StringBuilder();

    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        solutionTextView = findViewById(R.id.solution_tv);
        resultTextView = findViewById(R.id.result_tv);

        findViewById(R.id.btn_1).setOnClickListener(buttonClickListener);
        findViewById(R.id.btn_2).setOnClickListener(buttonClickListener);
        findViewById(R.id.btn_3).setOnClickListener(buttonClickListener);
        findViewById(R.id.btn_4).setOnClickListener(buttonClickListener);
        findViewById(R.id.btn_5).setOnClickListener(buttonClickListener);
        findViewById(R.id.btn_6).setOnClickListener(buttonClickListener);
        findViewById(R.id.btn_7).setOnClickListener(buttonClickListener);
        findViewById(R.id.btn_8).setOnClickListener(buttonClickListener);
        findViewById(R.id.btn_9).setOnClickListener(buttonClickListener);
        findViewById(R.id.btn_zero).setOnClickListener(buttonClickListener);
        findViewById(R.id.btn_dot).setOnClickListener(buttonClickListener);

        findViewById(R.id.btn_addition).setOnClickListener(buttonClickListener);
        findViewById(R.id.btn_subtraction).setOnClickListener(buttonClickListener);
        findViewById(R.id.btn_multiplication).setOnClickListener(buttonClickListener);
        findViewById(R.id.btn_divison).setOnClickListener(buttonClickListener);

        findViewById(R.id.btn_openbracket).setOnClickListener(buttonClickListener);
        findViewById(R.id.btn_closebracket).setOnClickListener(buttonClickListener);

        findViewById(R.id.btn_equal).setOnClickListener(v -> {
            try {
                double result = evaluate(input.toString());
                resultTextView.setText(String.valueOf(result));
            } catch (Exception e) {
                resultTextView.setText("Error");
            }
        });

        findViewById(R.id.btn_Ac).setOnClickListener(v -> {
            input = new StringBuilder();
            solutionTextView.setText("");
            resultTextView.setText("0");
        });

        findViewById(R.id.btn_c).setOnClickListener(v -> {
            if (input.length() > 0) {
                input.deleteCharAt(input.length() - 1);
                solutionTextView.setText(input.toString());
            }
        });
    }

    private final View.OnClickListener buttonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String buttonText = ((MaterialButton) v).getText().toString();
            input.append(buttonText);
            solutionTextView.setText(input.toString());
        }
    };

    private double evaluate(String expression) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < expression.length()) ? expression.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < expression.length()) throw new RuntimeException("Unexpected: " + (char) ch);
                return x;
            }

            double parseExpression() {
                double x = parseTerm();
                for (; ; ) {
                    if (eat('+')) x += parseTerm();
                    else if (eat('-')) x -= parseTerm();
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (; ; ) {
                    if (eat('*')) x *= parseFactor();
                    else if (eat('/')) x /= parseFactor();
                    else return x;
                }
            }

            double parseFactor() {
                if (eat('+')) return parseFactor();
                if (eat('-')) return -parseFactor();

                double x;
                int startPos = this.pos;
                if (eat('(')) {
                    x = parseExpression();
                    eat(')');
                } else if ((ch >= '0' && ch <= '9') || ch == '.') {
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(expression.substring(startPos, this.pos));
                } else {
                    throw new RuntimeException("Unexpected: " + (char) ch);
                }

                return x;
            }
        }.parse();
    }
}
