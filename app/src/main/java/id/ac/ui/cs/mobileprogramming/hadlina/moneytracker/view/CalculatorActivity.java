package id.ac.ui.cs.mobileprogramming.hadlina.moneytracker.view;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import id.ac.ui.cs.mobileprogramming.hadlina.moneytracker.R;
import id.ac.ui.cs.mobileprogramming.hadlina.moneytracker.utils.Calculator;

public class CalculatorActivity extends AppCompatActivity {

    private Calculator calc;
    private double a;
    private double b;
    private EditText firstNum;
    private EditText secondNum;
    private TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        calc = new Calculator();
        firstNum = findViewById(R.id.firstNum);
        secondNum = findViewById(R.id.secondNum);
        result = findViewById(R.id.result);
        initButtons();
    }

    private void initButtons(){
        TableLayout table = findViewById(R.id.table);
        for(int i = 0; i < table.getChildCount(); i++){
            TableRow row = (TableRow) table.getChildAt(i);
            for(int j = 0; j < row.getChildCount(); j++){
                if(row.getChildAt(j) instanceof Button){
                    Button button = (Button) row.getChildAt(j);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(checkEditTextNotEmpty(firstNum) && checkEditTextNotEmpty(secondNum)){

                                String aString = firstNum.getText().toString();
                                String bString = secondNum.getText().toString();

                                a = Double.parseDouble(aString);
                                b = Double.parseDouble(bString);
                                double res;
                                switch(v.getId()){
                                    case R.id.add:
                                        res = calc.add(a, b);
                                        setResultTextView((Button)v, res);
                                        break;
                                    case R.id.multiply:
                                        res = calc.multiply(a, b);
                                        setResultTextView((Button)v, res);
                                        break;
                                    case R.id.subtract:
                                        res = calc.subtract(a, b);
                                        setResultTextView((Button)v, res);
                                        break;
                                    case R.id.divide:
                                        res = calc.divide(a, b);
                                        setResultTextView((Button)v, res);
                                        break;
                                }
                            }else{
                                Toast.makeText(CalculatorActivity.this, "Please enter a number", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        }
    }
    private void setResultTextView(Button btn, double res){
        result.setText(firstNum.getText().toString()+btn.getText().toString()+
                secondNum.getText().toString()+" = "+String.valueOf(res));
        firstNum.setText("");
        secondNum.setText("");
    }



    private boolean checkEditTextNotEmpty(EditText edit){
        String text = edit.getText().toString();
        if(TextUtils.isEmpty(text)){
            return false;
        }
        return true;
    }
}