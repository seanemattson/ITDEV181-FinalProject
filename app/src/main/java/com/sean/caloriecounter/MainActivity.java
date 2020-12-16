package com.sean.caloriecounter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    EditText etCalLimit, etCalsEaten;
    Button btnUpdateLimit, btnSubtract, btnReset;
    TextView tvCalsTitle, tvCalsRemain;

    boolean firstTimeOpened = true; // FLIP THIS FLAG FIRST TIME "UPDATE" IS PRESSED

    private int dailyCalorieLimit, calsRemaining, calsEaten;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String calsRemainString = "calsRemainingForDay";
    public static final String dailyCalorieLimitString = "dailyLimit";
    public static final String calsEatenString = "calsEatenToday";
    public static final String firstOpenedString = "firstTimeOpenedFlag";

    SharedPreferences savedValues;
    SharedPreferences.Editor editor;


    // PERSISTENT VARIABLES NEEDED
        // DAILY LIMIT
            // DAILY LIMIT AGAIN
        // CALS EATEN TODAY (CUMULATIVE)
        // CALS REMAINING



    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etCalLimit = findViewById(R.id.etCalLimit);
        etCalsEaten = findViewById(R.id.etCalsEaten);
        btnUpdateLimit = findViewById(R.id.btnUpdateLimit);
        btnSubtract = findViewById(R.id.btnSubtract);
        btnReset = findViewById(R.id.btnReset);
        tvCalsRemain = findViewById(R.id.tvCalsRemain);

        savedValues = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        editor = savedValues.edit();

        fillSavedData();

        // SET BUTTON FUNCTIONS

        // WHENEVER UPDATE IS PRESSED
            // UPDATE Daily Calorie Limit variable in shared prefs
            // DISPLAY daily cal limit (minus whatever meals were already eaten, if any )
                // NEED A calories eaten today VARIABLE THAT'S SAVED

        btnUpdateLimit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstTimeOpened = false;
                editor.putBoolean(firstOpenedString, firstTimeOpened);
                editor.apply();

                // update daily calorie limit
                // update the calories remaining total (if the person had entered meals that day)
                // SAVE IN SHARED PREFERENCES

                // GET and SAVE daily calorie limit
                dailyCalorieLimit = Integer.parseInt(etCalLimit.getText().toString());
                editor.putInt(dailyCalorieLimitString, dailyCalorieLimit);
                editor.apply();

                //int testCalLimitVar = getSharedPreferences()

                // DISPLAY updated limit (account for meals already entered)
                if((savedValues.getInt(calsEatenString, 0)) > 0)
                {
                    calsRemaining = savedValues.getInt(dailyCalorieLimitString, 0) - savedValues.getInt(calsEatenString, 0);
                    editor.putInt(calsRemainString, calsRemaining);
                    editor.apply();
                    tvCalsRemain.setText(String.valueOf(calsRemaining));
                }
                else
                {
                    tvCalsRemain.setText(String.valueOf(savedValues.getInt(dailyCalorieLimitString, 0)));
                }
            }
        });


        btnSubtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calsEaten = (savedValues.getInt(calsEatenString, 0)) + (Integer.parseInt(etCalsEaten.getText().toString()));
                editor.putInt(calsEatenString, calsEaten);
                editor.apply();

                calsRemaining = (savedValues.getInt(calsRemainString, 0)) - (Integer.parseInt(etCalsEaten.getText().toString()));
                editor.putInt(calsRemainString, calsRemaining);
                editor.apply();

                tvCalsRemain.setText(Integer.toString(calsRemaining));
                etCalsEaten.getText().clear();
            }
        });


        // RESET REMAINING CALORIES TO THE LAST SAVED DAILY LIMIT
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // set calories eaten = 0
                // set cals remaining = daily allowed limit
                    // save them
                // call fillSavedData?

                calsEaten = 0;
                editor.putInt(calsEatenString, calsEaten);
                editor.apply();

                calsRemaining = savedValues.getInt(dailyCalorieLimitString, 0);
                editor.putInt(calsRemainString, calsRemaining);
                editor.apply();

                fillSavedData();
            }
        });
    }

    // CALL THIS WHENEVER APP OPENS TO ENSURE DATA IS ACCURATE
    public void fillSavedData()
    {
        if (!(savedValues.getBoolean(firstOpenedString, true)))
        {
            // check what calories eaten is
                // then subtract from saved daily limit and display

            calsRemaining = savedValues.getInt(dailyCalorieLimitString, 0) - savedValues.getInt(calsEatenString, 0);
            editor.putInt(calsRemainString, calsRemaining);
            editor.apply();
            tvCalsRemain.setText((String.valueOf(savedValues.getInt(calsRemainString, 0))));
        }
        else
        {
            // THERE'S NOTHING TO FILL, IT'S THE FIRST TIME IT'S OPENED
            calsEaten = 0; // was having issues when not initializing cals eaten at beginning of app
            editor.putInt(calsEatenString, calsEaten);
            editor.apply();
            return;
        }
    }


}