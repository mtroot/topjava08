package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * GKislin
 * 31.05.2015.
 */
public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,10,0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,13,0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,20,0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,10,0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,13,0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,20,0), "Ужин", 510)
        );
        System.out.println(getFilteredWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(12,0), 2000));
//        .toLocalDate();
//        .toLocalTime();
    }

    public static List<UserMealWithExceed>  getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        /*return getFilteredWithExceededByCycle(mealList, startTime, endTime, caloriesPerDay);*/
        return getFilteredWithExceededByStream(mealList, startTime, endTime, caloriesPerDay);
    }

    private static List<UserMealWithExceed> getFilteredWithExceededByStream(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> mapColories = mealList.stream()
                .collect(Collectors.toMap(m -> m.getDateTime().toLocalDate(), m -> m.getCalories(), Integer::sum));
        return mealList.stream()
                .filter(m -> TimeUtil.isBetween(m.getDateTime().toLocalTime(), startTime, endTime))
                .map(m -> new UserMealWithExceed(m.getDateTime(),
                        m.getDescription(),
                        m.getCalories(),
                        mapColories.get(m.getDateTime().toLocalDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }

    private static List<UserMealWithExceed> getFilteredWithExceededByCycle(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        int colories = 0;
        LocalDate date = null;
        List<UserMeal> list = new ArrayList<>();
        List<UserMealWithExceed> result = new ArrayList<>();
        boolean exceeded = false;
        for (UserMeal um : mealList) {
            if(!um.getDateTime().toLocalDate().equals(date)) {
                exceeded = colories > caloriesPerDay;
                addToResult(result, list, startTime, endTime, exceeded);
                list.clear();
                colories = 0;
                date = um.getDateTime().toLocalDate();
            }
            colories += um.getCalories();
            list.add(um);
        }
        exceeded = colories > caloriesPerDay;
        addToResult(result, list, startTime, endTime, exceeded);
        return result;
    }

    private static void addToResult(List<UserMealWithExceed> result, List<UserMeal> list, LocalTime start, LocalTime end, boolean exceeded) {
        for (UserMeal m : list) {
            if(TimeUtil.isBetween(m.getDateTime().toLocalTime(), start, end)) {
                result.add(new UserMealWithExceed(m.getDateTime(), m.getDescription(), m.getCalories(), exceeded));
            }
        }
    }
}
