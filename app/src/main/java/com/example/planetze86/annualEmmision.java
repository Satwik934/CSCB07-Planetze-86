package com.example.planetze86;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hbb20.CountryCodePicker;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class annualEmmision extends AppCompatActivity {

    private TextView questionText;      // Displays the question
    private RadioGroup optionsGroup;   // Displays the answer options
    private Button nextButton, prevButton;

    private List<Questions> questionList; // Stores all questions
    private  int currentIndex = 0;
    private  String country;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_annual_emmision);
        questionText = findViewById(R.id.questionText);
        optionsGroup = findViewById(R.id.optionsGroup);
        nextButton = findViewById(R.id.nextButton);
        prevButton = findViewById(R.id.prevButton);
        CountryCodePicker countryPicker = findViewById(R.id.countryPicker);

        country = "";
        countryPicker.setOnCountryChangeListener(() -> {
            country = countryPicker.getSelectedCountryName();

        });

        QuestionsRepo repository = new QuestionsRepo();
        questionList = repository.getQuestions();
        loadQuestion();

        nextButton.setOnClickListener(v -> {
            if(saveAnswer()){   // Save current answer
             if (currentIndex < questionList.size() - 1) {
                 if(currentIndex == 0){
                     Questions currentQuestion = questionList.get(currentIndex);
                     int answer = currentQuestion.getAnswer();
                     if(answer == 1)
                         currentIndex = skip_car_question(currentIndex);
                     else
                         currentIndex++;
                 loadQuestion();
                 }

                 else if(currentIndex == 7){
                     Questions currentQuestion = questionList.get(currentIndex);
                     int answer = currentQuestion.getAnswer();
                     if( answer != 3)
                         currentIndex = skip_food_question(currentIndex);
                     else
                         currentIndex++;
                 loadQuestion();
                 }
                 else{
                currentIndex++; // Move to the next question
                loadQuestion();
                 }
               }
             else{
                 AnnualCalculator calc = new AnnualCalculator(questionList,country,this);
                 AnnualAnswers ans = new AnnualAnswers(country, calc.getTotal(), calc.getTransportation(), calc.getFood(),
                 calc.getHousing(), calc.getConsumption(), calc.getCountryCompare());
                 String uid = FirebaseAuth.getInstance().getCurrentUser().getUid(); // Get current user's UID
                 DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(uid);

                 userRef.child("AnnualAnswers").setValue(ans) // Update the email field
                         .addOnSuccessListener(aVoid -> {
                             Toast.makeText(this, "You've completed the questionnaire!", Toast.LENGTH_SHORT).show();
                             Intent intent = new Intent(annualEmmision.this, AnnualEmissionResult.class);
                             startActivity(intent);
                         })
                         .addOnFailureListener(e -> {
                             Toast.makeText(this, "Network Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                         });

             }
            }
            else {
                Toast.makeText(this, "Please select an option before proceeding.", Toast.LENGTH_SHORT).show();
            }
        });

        // Handle Previous button click
        prevButton.setOnClickListener(v -> {
            if (currentIndex > 0) {
                  if(currentIndex == 3){
                      Questions prev_question = questionList.get(0);
                      if(prev_question.getAnswer() == 1)
                          currentIndex = 0;
                      else
                          currentIndex--;
                  loadQuestion();
                  }
                  else if (currentIndex == 12){
                      Questions prev_question = questionList.get(7);
                      if(prev_question.getAnswer() != 3)
                          currentIndex = 7;
                      else
                          currentIndex--;
                  loadQuestion();
                  }
                  else{
                currentIndex--; // Move to the previous question
                loadQuestion();
                  }
            }
        });

    }
    private void loadQuestion() {
        // Clear previous options
        optionsGroup.removeAllViews();

        optionsGroup.clearCheck();
        // Get the current question
        Questions currentQuestion = questionList.get(currentIndex);

        // Set the question text
        questionText.setText(currentQuestion.getQuestionText());

        // Dynamically add options to the RadioGroup
        List<String> options = currentQuestion.getOptions();
        for (int i = 0; i < options.size(); i++) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(options.get(i));  // Set option text
            radioButton.setId(i);                // Assign ID to each RadioButton
            optionsGroup.addView(radioButton);   // Add to RadioGroup
        }

        // Enable/Disable the Previous button
        prevButton.setEnabled(currentIndex > 0);
        ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setMax(questionList.size());
        progressBar.setProgress(currentIndex + 1);
    }


    private boolean saveAnswer() {
        // Get the selected RadioButton ID
        int optionId = optionsGroup.getCheckedRadioButtonId();
        if(optionId == -1)
            return false;
        else{
            //  an option is selected
            Questions currentQuestion = questionList.get(currentIndex);
            currentQuestion.setAnswer(optionId); // Save the answer index
            return true;
       }
    }


private int skip_car_question(int index){
         index = index + 3;
         return index;
}
private int skip_food_question(int index){
        index = index + 5;
        return index;
    }
}









