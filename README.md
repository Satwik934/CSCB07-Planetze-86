# CSCB07-Planetze-86
# USER FLOW

1. **User Opens App**
   - The app launches, and the user is greeted with a Sign-Up/Login module.

2. **Sign-Up/Login Module**
   - **For Sign-Up:**
     a) Users are prompted to provide their account details.
     b) Users cannot create an account using an email address already associated with an existing account.
     c) Password confirmation is required, with the password needing to meet the criteria of being greater than 6 alphanumeric characters.
     d) If the account information provided is valid, the user receives a verification email and is redirected to the login page. They can log in only after verifying their email.
   - **For Login:**
     a) If a user attempts to log in before verifying their email, they will be denied access.
     b) If invalid credentials are entered, the user will be informed that the credentials are incorrect and will not be granted access.
     c) Upon entering valid credentials, the user proceeds into the app, landing on Plantze.

3. **Post-Login Experience**
   - **First-Time Login:**
     - The user is directed to the initial setup page with a button to calculate their Annual Carbon Emissions Survey.
   - **Subsequent Logins:**
     - The user is taken directly to Eco Tracker.

4. **Annual Carbon Emissions Survey**
   i) Users respond to a series of annually updated questions about their carbon footprint, presented in multiple-choice format.
   ii) A Back button is available, allowing users to revisit and modify their previous answers as they complete the survey.
   iii) Certain responses dynamically remove related questions (e.g., if the user selects "Vegan" for "What best describes your diet?", questions related to non-vegan activities will be skipped).
   iv) Inputs provided during the survey, such as the default country, are saved for future use.
   v) Upon completing the survey, the user is directed to the Annual Carbon Footprint Results page.

5. **Annual Carbon Footprint Results**
   Users are presented with the following:
   i) A summary of their total carbon footprint.
   ii) A chart breaking down emissions by category (e.g., transportation, food, housing, consumption).
   iii) Percentage-based comparisons of the user’s emissions to:
      - (1) The national average for the selected country.
      - (2) The global target of 2 tons of CO₂ per year per person.
   iv) An option to proceed to the Dashboard (Eco Tracker).

6. **Eco Tracker**
   i) Provides access to key sections related to the user’s daily carbon footprint, such as:
      - Transportation
      - Food
      - Shopping/Energy Bills
      - User Habits
      - Plantze Menu

7. **Plantze Menu**
   i) Serves as the homepage of the app, allowing users to:
      - Navigate to take the survey again.
      - View statistical representations of their carbon footprint (annually/monthly/weekly/daily) via Eco Gauge.
      - Return to Eco Tracker to modify activities.

8. **Eco Gauge**
   i) Represents the user’s carbon footprint through statistical graphs.
   ii) Users can select any specific date to view their carbon footprint for that period.

## FEATURE SUMMARY

1. **Eco Tracker**
   i) **Total Emissions:** Displays the user’s total CO₂ emissions (in kg) for the current day prominently at the top of the screen.
   ii) **Date Selection:** Shows the current date in a text field with a "Select Date" button. Clicking this button opens a calendar popup where the user can select a new date.
   iii) **View Activities:**
      - Users can view a breakdown of their logged activities, including the CO₂ emissions for each activity, for the selected day.
      - Allows users to Add, Edit, or Delete activities as needed.
   iv) **View Habit:**
      - Users can view their currently adopted habits and log the days they follow each habit.
      - Users can remove a habit if no longer applicable.
      - Clicking Add Habits opens a comprehensive list of habits, which can be filtered by:
        - Impact: Low, Medium, or High.
        - Category: Specific habit categories.
        - Suggested: Habits recommended based on the user’s highest annual emissions categories.
   v) **Navigation to Plantze Menu:** Includes a button at the bottom of the Eco Tracker that allows users to navigate back to the main Plantze Menu.
   vi) **Calendar Features:**
      a) Selecting a new date updates the Eco Tracker to display data for that specific day.
      b) Users can edit previously logged activities on past dates, plan activities for future dates, or delete activities as necessary.

2. **Eco Gauge**
   i) Displays the user’s total CO₂ emissions for the selected time period at the top of the screen, along with the date of calculation.
   ii) Users can select a time period (Yearly, Monthly, Weekly, or Daily) via buttons prominently displayed on the screen.
   iii) A Comparison Module compares the user’s emissions to the average annual carbon footprint of a specific country, providing direct numerical feedback.
   iv) An Emissions Breakdown is visualized in a donut chart, highlighting the contributions of different categories (e.g., food, transportation) to the user’s total emissions.
   v) A CO₂ Emissions Trend Graph tracks and displays the user’s emissions over time for the selected period, allowing for trend analysis.

## ASSUMPTIONS (While Developing App)

1. For all calculations where there is an "other option" and it is not provided in the formulas table, the average of all given values will be used.
2. If "3 or more devices" is mentioned for annual device purchases, the value provided for "4 or more" will be used.
3. Recycling adjustment values follow the provided ratios:
   - 0x for no recycling
   - 0.3 for occasional recycling
   - 0.5 for always recycling
   Example: Quarterly clothing buyers’ reductions are 0 KG (no recycling), 36 KG (occasional recycling), and 60 KG (always recycling).
4. Suggested habits are derived from categories with the highest annual emissions.
5. Annual carbon emissions prompts follow indexing logic to dynamically adjust questions or skip based on user responses. Altering the question order or content might impact functionality.
6. Habits are ranked by impact level: Low, Medium, or High.
7. Emission calculations for Eco Tracker are based on the midpoint of annual ranges (e.g., for a range of "1-2 annually," the value 1.5 will be used) and scaled down linearly based on time.

## DEPENDENCIES

- **Apache POI:** Utilized for reading Excel files and extracting calculation values.
- **Firebase Authentication:** Used for secure login and managing user authentication.
- **Firebase Realtime Database:** Employed for storing and retrieving user data.
- **MPAndroidChart:** Generates pie charts and line charts for the Eco Gauge feature.
- **Mockito Framework:** Used for testing application components and ensuring functionality.

## FUTURE EXTENSIONS/MODIFICATIONS

- Developers can maintain or update the app by modifying the following dynamically-read files:
  - formulas.xlsx: For emission calculations.
  - global.xlsx: For country emission data.
  - habit_list.txt: To add new habits in a format consistent with the existing habits.
- These files are located in the raw folder in the app's resources directory.
