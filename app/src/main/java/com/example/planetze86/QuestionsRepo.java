package com.example.planetze86;

import java.util.*;

public class QuestionsRepo {
    private final List<Questions> questionList;

    public QuestionsRepo() {
        this.questionList = new ArrayList<>();
        initializeQuestions();
    }


 private void initializeQuestions(){
    // Question 1
    ArrayList<String> options1 = new ArrayList<>();
        options1.add("Yes");
        options1.add("No");


    Questions question1 = new Questions("Transportation", "Do you own or regularly use a car?", options1);
        questionList.add(question1);

    // Question 2
    ArrayList<String> options2 = new ArrayList<>();
        options2.add("Gasoline");
        options2.add("Diesel");
        options2.add("Hybrid");
        options2.add("Electric");
        options2.add("I don't know");


    Questions question2 = new Questions("Personal Vehicle Use", "What type of car do you drive?", options2);
        questionList.add(question2);

    // Question 3
    ArrayList<String> options3 = new ArrayList<>();
        options3.add("Up to 5,000 km (3,000 miles)");
        options3.add("5,000–10,000 km (3,000–6,000 miles)");
        options3.add("10,000–15,000 km (6,000–9,000 miles)");
        options3.add("15,000–20,000 km (9,000–12,000 miles)");
        options3.add("20,000–25,000 km (12,000–15,000 miles)");
        options3.add("More than 25,000 km (15,000 miles)");

    Questions question3 = new Questions("Personal Vehicle Use", "How many kilometers/miles do you drive per year?", options3);
        questionList.add(question3);

    // Question 4
    ArrayList<String> options4 = new ArrayList<>();
        options4.add("Never");
        options4.add("Occasionally (1–2 times/week)");
        options4.add("Frequently (3–4 times/week)");
        options4.add("Always (5+ times/week)");

    Questions question4 = new Questions("Public Transportation", "How often do you use public transportation (bus, train, subway)?", options4);
        questionList.add(question4);

    // Question 5
    ArrayList<String> options5 = new ArrayList<>();
        options5.add("Under 1 hour");
        options5.add("1–3 hours");
        options5.add("3–5 hours");
        options5.add("5–10 hours");
        options5.add("More than 10 hours");

    Questions question5 = new Questions("Public Transportation", "How much time do you spend on public transport per week (bus, train, subway)?", options5);
        questionList.add(question5);

    // Question 6
    ArrayList<String> options6 = new ArrayList<>();
        options6.add("None");
        options6.add("1–2 flights");
        options6.add("3–5 flights");
        options6.add("6–10 flights");
        options6.add("More than 10 flights");

    Questions question6 = new Questions("Air Travel", "How many short-haul flights (less than 1,500 km / 932 miles) have you taken in the past year?", options6);
        questionList.add(question6);

    // Question 7
    ArrayList<String> options7 = new ArrayList<>();
        options7.add("None");
        options7.add("1–2 flights");
        options7.add("3–5 flights");
        options7.add("6–10 flights");
        options7.add("More than 10 flights");

    Questions question7 = new Questions("Air Travel", "How many long-haul flights (more than 1,500 km / 932 miles) have you taken in the past year?", options7);
        questionList.add(question7);



     // Food Category

     // Question 8
     ArrayList<String> options8 = new ArrayList<>();
     options8.add("Vegetarian");
     options8.add("Vegan");
     options8.add("Pescatarian (fish/seafood)");
     options8.add("Meat-based (eat all types of animal products)");

     Questions question8 = new Questions("Food", "What best describes your diet?", options8);
     questionList.add(question8);

     // Question 9 (Nested question for different meats)
     ArrayList<String> options9 = new ArrayList<>();
     options9.add("Daily");
     options9.add("Frequently (3–5 times/week)");
     options9.add("Occasionally (1–2 times/week)");
     options9.add("Never");

     Questions beefQuestion = new Questions("Food", "How often do you eat Beef?", options9);
     Questions porkQuestion = new Questions("Food", "How often do you eat Pork?", options9);
     Questions chickenQuestion = new Questions("Food", "How often do you eat Chicken?", options9);
     Questions fishQuestion = new Questions("Food", "How often do you eat Fish/Seafood?", options9);

     questionList.add(beefQuestion);
     questionList.add(porkQuestion);
     questionList.add(chickenQuestion);
     questionList.add(fishQuestion);

     // Question 10
     ArrayList<String> options10 = new ArrayList<>();
     options10.add("Never");
     options10.add("Rarely");
     options10.add("Occasionally");
     options10.add("Frequently");

     Questions question10 = new Questions("Food", "How often do you waste food or throw away uneaten leftovers?", options10);
     questionList.add(question10);

     // Housing Category

     // Question 11
     ArrayList<String> options11 = new ArrayList<>();
     options11.add("Detached house");
     options11.add("Semi-detached house");
     options11.add("Townhouse");
     options11.add("Condo/Apartment");
     options11.add("Other");

     Questions question11 = new Questions("Housing", "What type of home do you live in?", options11);
     questionList.add(question11);

     // Question 12
     ArrayList<String> options12 = new ArrayList<>();
     options12.add("1");
     options12.add("2");
     options12.add("3–4");
     options12.add("5 or more");

     Questions question12 = new Questions("Housing", "How many people live in your household?", options12);
     questionList.add(question12);

     // Question 13
     ArrayList<String> options13 = new ArrayList<>();
     options13.add("Under 1000 sq. ft.");
     options13.add("1000–2000 sq. ft.");
     options13.add("Over 2000 sq. ft.");

     Questions question13 = new Questions("Housing", "What is the size of your home?", options13);
     questionList.add(question13);

     // Question 14
     ArrayList<String> options14 = new ArrayList<>();
     options14.add("Natural Gas");
     options14.add("Electricity");
     options14.add("Oil");
     options14.add("Propane");
     options14.add("Wood");
     options14.add("Other");

     Questions question14 = new Questions("Housing", "What type of energy do you use to heat your home?", options14);
     questionList.add(question14);

     // Question 15
     ArrayList<String> options15 = new ArrayList<>();
     options15.add("Under $50");
     options15.add("$50–$100");
     options15.add("$100–$150");
     options15.add("$150–$200");
     options15.add("Over $200");

     Questions question15 = new Questions("Housing", "What is your average monthly electricity bill?", options15);
     questionList.add(question15);

     ArrayList<String> options22 = new ArrayList<>();
     options22.add("Natural Gas");
     options22.add("Electricity");
     options22.add("Oil");
     options22.add("Propane");
     options22.add("Solar");
     options22.add("Other");

     Questions question22 = new Questions("Housing", "What type of energy do you use to heat water in your home?", options22);
     questionList.add(question22);


     ArrayList<String> options23 = new ArrayList<>();
     options23.add("Yes, primarily (more than 50% of energy use)");
     options23.add("Yes, partially (less than 50% of energy use)");
     options23.add("No");


     Questions question23 = new Questions("Housing", "What type of energy do you use to heat water in your home?", options23);
     questionList.add(question23);





     // Consumption Category

     // Question 18
     ArrayList<String> options18 = new ArrayList<>();
     options18.add("Monthly");
     options18.add("Quarterly");
     options18.add("Annually");
     options18.add("Rarely");

     Questions question18 = new Questions("Consumption", "How often do you buy new clothes?", options18);
     questionList.add(question18);

     // Question 19
     ArrayList<String> options19 = new ArrayList<>();
     options19.add("Yes, regularly");
     options19.add("Yes, occasionally");
     options19.add("No");

     Questions question19 = new Questions("Consumption", "Do you buy second-hand or eco-friendly products?", options19);
     questionList.add(question19);

     // Question 20
     ArrayList<String> options20 = new ArrayList<>();
     options20.add("None");
     options20.add("1");
     options20.add("2");
     options20.add("3 or more");

     Questions question20 = new Questions("Consumption", "How many electronic devices (phones, laptops, etc.) have you purchased in the past year?", options20);
     questionList.add(question20);

     // Question 21
     ArrayList<String> options21 = new ArrayList<>();
     options21.add("Never");
     options21.add("Occasionally");
     options21.add("Frequently");
     options21.add("Always");

     Questions question21 = new Questions("Consumption", "How often do you recycle?", options21);
     questionList.add(question21);
 }

    public List<Questions> getQuestions() {
        return questionList;
    }

}
