package seedu.address.logic;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Collections;
import java.util.List;

import seedu.address.logic.commands.ViewPlanCommand;
import seedu.address.model.diet.DietPlan;
import seedu.address.model.food.Food;
import seedu.address.model.food.FoodIntake;
import seedu.address.model.food.FoodIntakeList;
import seedu.address.model.user.User;

/**
 * Class for generating a progress report of diet plan.
 */
public class ProgressCalculator {

    // Leeway value for adherence to diet requirements (in percentage)
    public static final double LEEWAY = 5.00;

    /**
     * Calculates and reports on how much percentage of each day's food intake is adhering to the diet plan.
     *
     * @param dietPlan The diet plan to calculate daily food intake against.
     * @param foodIntakeList The food consumption for each day.
     * @param user User's information
     * @return Progress Report
     */
    public static String calculateProgress(FoodIntakeList foodIntakeList, DietPlan dietPlan, User user) {

        // Get list of Foods
        List<FoodIntake> foodIntakes = initializeFoodIntake(foodIntakeList);

        // Get the daily requirements of the diet plan
        PlanInfoCalculator infoCalculator = new PlanInfoCalculator(user, dietPlan);
        double dailyCarbs = infoCalculator.getCarbohydrates();
        double dailyFats = infoCalculator.getFats();
        double dailyProteins = infoCalculator.getProteins();

        // Initialize report
        StringBuilder report = initializeReport(dietPlan, foodIntakes, infoCalculator);

        // For each day, give a progress report on whether intake is over or less
        // than the required amount.
        reportDailyIntake(report, foodIntakes, dailyCarbs, dailyFats, dailyProteins);

        // Return the final report
        return report.toString();

    }

    /**
     * Initializes the list of food intake for processing
     *
     * @param foodIntakeList List of food intakes
     * @return List of food intakes
     */
    private static List<FoodIntake> initializeFoodIntake(FoodIntakeList foodIntakeList) {
        // Get list of Foods
        List<FoodIntake> foodIntakes = foodIntakeList.getFoodIntakeList();

        // Sort food intake list by dates
        Collections.sort(foodIntakes, new FoodIntakeComparator());

        return foodIntakes;
    }

    /**
     * Initializes the report with dietPlan details
     *
     * @param dietPlan Active diet plan
     * @param foodIntakes List of daily food intake
     * @return Progress report
     */
    private static StringBuilder initializeReport(DietPlan dietPlan, List<FoodIntake> foodIntakes,
                                                  PlanInfoCalculator infoCalculator) {
        // Print details of diet plan
        StringBuilder report = new StringBuilder();
        report.append(dietPlan.viewPlan() + "\n");
        report.append(ViewPlanCommand.getResult(dietPlan, infoCalculator));

        // Dates that progress report is listing
        report.append("\nHere is the report for the days ");
        LocalDate firstIntakeDay = foodIntakes.get(0).getDate();
        LocalDate lastIntakeDay = foodIntakes.get(foodIntakes.size() - 1).getDate();
        report.append(firstIntakeDay.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)));
        report.append(" to ");
        report.append(lastIntakeDay.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)));
        report.append(":\n\n");

        return report;
    }

    /**
     * Appends report with daily progress
     *
     * @param report Report to append to
     * @param foodIntakes List of daily food intake
     * @param dailyCarbs Daily requirement of carbohydrates
     * @param dailyFats Daily requirement of fats
     * @param dailyProteins Daily requirement of proteins
     */
    private static void reportDailyIntake(StringBuilder report, List<FoodIntake> foodIntakes,
                                          double dailyCarbs, double dailyFats, double dailyProteins) {
        LocalDate previousDay = null;
        double carbsSum = 0.0;
        double fatsSum = 0.0;
        double proteinsSum = 0.0;
        boolean firstDay = true;
        for (FoodIntake foodIntake : foodIntakes) {
            // Report on date
            LocalDate day = foodIntake.getDate();

            if (!day.equals(previousDay)) {
                // New Day
                if (!firstDay) {
                    // Calculate total adherence percentages
                    double carbsAdherence = calculatePercentage(carbsSum, dailyCarbs);
                    double fatsAdherence = calculatePercentage(fatsSum, dailyFats);
                    double proteinsAdherence = calculatePercentage(proteinsSum, dailyProteins);

                    // Report daily adherence percentage
                    reportAdherence(report, carbsAdherence, fatsAdherence, proteinsAdherence);
                } else {
                    firstDay = false;
                }

                // Reset sums
                carbsSum = 0.0;
                fatsSum = 0.0;
                proteinsSum = 0.0;

                // Report new day
                reportNewDay(report, day);
                previousDay = day;
            }

            // Report on foods consumed and macronutrients
            Food food = foodIntake.getFood();
            reportFood(report, food);

            // Store sum of macronutrient consumption
            carbsSum += food.getCarbos();
            fatsSum += food.getFats();
            proteinsSum += food.getProteins();

        }

        // Calculate total adherence percentages
        double carbsAdherence = calculatePercentage(carbsSum, dailyCarbs);
        double fatsAdherence = calculatePercentage(fatsSum, dailyFats);
        double proteinsAdherence = calculatePercentage(proteinsSum, dailyProteins);

        // Report daily adherence percentage
        reportAdherence(report, carbsAdherence, fatsAdherence, proteinsAdherence);
    }

    /**
     * Appends the report with new date
     *
     * @param report Report to append to
     * @param day Date of new day
     */
    private static void reportNewDay(StringBuilder report, LocalDate day) {
        report.append("Date: ");
        report.append(day.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)));
        report.append("\n");
        report.append("Name\t\t\tCarbohydrates\t\tFats\t\tProtein\n");
    }

    /**
     * Report food details
     *
     * @param report Report to append to
     * @param food Food to report
     */
    private static void reportFood(StringBuilder report, Food food) {
        // Get macronutrients consumed
        double carbs = food.getCarbos();
        double fats = food.getFats();
        double proteins = food.getProteins();

        // Format consumed macronutrients
        String carbsString = String.format("%,.2f", carbs);
        String fatsString = String.format("%,.2f", fats);
        String proteinsString = String.format("%,.2f", proteins);

        report.append(food.getName() + "\t\t\t");
        report.append(carbsString + "\t\t");
        report.append(fatsString + "\t\t");
        report.append(proteinsString + "\t\n");

    }

    /**
     * Calculates the percentage of macronutrient intake against requirements
     *
     * @param intake Macronutrient intake in grams
     * @param required Daily macronutrient intake requirement
     * @return Percentage of adherence
     */
    private static double calculatePercentage(double intake, double required) {
        return (intake / required) * 100.00;
    }

    /**
     * Reports the adherence for each macronutrient
     *
     * @param report Report to append to
     * @param carbsAdherence Daily carbohydrate adherence
     * @param fatsAdherence Daily fats adherence
     * @param proteinsAdherence Daily proteins adherence
     */
    private static void reportAdherence(StringBuilder report, double carbsAdherence, double fatsAdherence,
                                        double proteinsAdherence) {

        // Report daily adherence percentage
        if (carbsAdherence > 100 + LEEWAY) {
            double exceed = carbsAdherence - (100.00 + LEEWAY);
            String exceedString = String.format("%,.2f", exceed);
            report.append("Your daily carbohydrate consumption has exceeded by " + exceedString + "%\n");
        } else if (carbsAdherence < 100 - LEEWAY) {
            double under = (100 - LEEWAY) - carbsAdherence;
            String underString = String.format("%,.2f", under);
            report.append("Your daily carbohydrate consumption is under by " + underString + "%\n");
        } else {
            report.append("Your daily carbohydrate consumption "
                    + "is within diet requirements. Well done!\n");
        }

        if (fatsAdherence > 100 + LEEWAY) {
            double exceed = fatsAdherence - (100.00 + LEEWAY);
            String exceedString = String.format("%,.2f", exceed);
            report.append("Your daily fat consumption has exceeded by " + exceedString + "%\n");
        } else if (fatsAdherence < 100 - LEEWAY) {
            double under = (100 - LEEWAY) - fatsAdherence;
            String underString = String.format("%,.2f", under);
            report.append("Your daily fat consumption is under by " + underString + "%\n");
        } else {
            report.append("Your daily fat consumption is within diet requirements. Well done!\n");
        }

        if (proteinsAdherence > 100 + LEEWAY) {
            double exceed = proteinsAdherence - (100.00 + LEEWAY);
            String exceedString = String.format("%,.2f", exceed);
            report.append("Your daily protein consumption has exceeded by " + exceedString + "%\n\n");
        } else if (proteinsAdherence < 100 - LEEWAY) {
            double under = (100 - LEEWAY) - proteinsAdherence;
            String underString = String.format("%,.2f", under);
            report.append("Your daily protein consumption is under by " + underString + "%\n\n");
        } else {
            report.append("Your daily protein consumption is within diet requirements. Well done!\n\n");
        }

    }

}
