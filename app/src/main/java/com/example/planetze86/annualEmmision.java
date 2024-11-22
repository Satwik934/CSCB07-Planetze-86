package com.example.planetze86;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

public class annualEmmision extends AppCompatActivity {

    private TextView questionText;      // Displays the question
    private RadioGroup optionsGroup;   // Displays the answer options
    private Button nextButton, prevButton;

    private List<Questions> questionList; // Stores all questions
    private  int currentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_annual_emmision);
        questionText = findViewById(R.id.questionText);
        optionsGroup = findViewById(R.id.optionsGroup);
        nextButton = findViewById(R.id.nextButton);
        prevButton = findViewById(R.id.prevButton);


        QuestionsRepo repository = new QuestionsRepo();
        questionList = repository.getQuestions();
        loadQuestion();

        nextButton.setOnClickListener(v -> {
            saveAnswer(); // Save current answer
            if (currentIndex < questionList.size() - 1) {
                currentIndex++; // Move to the next question
                loadQuestion();
            } else {
                Toast.makeText(this, "You've completed the questionnaire!", Toast.LENGTH_SHORT).show();
            }
        });

        // Handle Previous button click
        prevButton.setOnClickListener(v -> {
            if (currentIndex > 0) {
                currentIndex--; // Move to the previous question
                loadQuestion();
            }
        });

    }
    private void loadQuestion() {
        // Clear previous options
        optionsGroup.removeAllViews();

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


    private void saveAnswer() {
        // Get the selected RadioButton ID
        int optionId = optionsGroup.getCheckedRadioButtonId();
        if (optionId == -1)
            Toast.makeText(this, "Please select an option before proceeding.", Toast.LENGTH_SHORT).show();
            else { //  an option is selected
            Questions currentQuestion = questionList.get(currentIndex);
            currentQuestion.setAnswer(optionId); // Save the answer index
        }
    }

}









