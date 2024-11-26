package com.example.planetze86;

import java.util.ArrayList;


public class Questions {
    private String question_category;
    private String question_text;
    private ArrayList<String> options;
    protected int answer;

public Questions() {
    this.options = new ArrayList<>();

}

// Parameterized Constructor
public Questions(String question_category, String question_text, ArrayList<String> options) {
    this.question_category = question_category;
    this.question_text = question_text;
    this.options = options;
    this.answer = -1;


}


public String getQuestionCategory() {
    return question_category;
}

public void setQuestionCategory(String question_category) {
    this.question_category = question_category;
}

public String getQuestionText() {
    return question_text;
}

public void setQuestionText(String question_text) {
    this.question_text = question_text;
}

public ArrayList<String> getOptions() {
    return options;
}

public void setOptions(ArrayList<String> options) {
    this.options = options;
}

public void setAnswer(int ans){
     this.answer = ans;
}
public int getAnswer(){
    return this.answer;
}

}