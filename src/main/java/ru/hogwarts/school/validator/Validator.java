package ru.hogwarts.school.validator;

import org.apache.commons.lang3.StringUtils;
import ru.hogwarts.school.exception.DataEntryError;

import java.util.Arrays;
import java.util.List;

public class Validator {
    public static String validateString(String string, String message) {
        if (StringUtils.isAlpha(string)) {
            return transform(string);
        } else {
            throw new DataEntryError(message);

        }
    }

    public static int validateNumber(int num) {
        if (num <= 15 || num >= 30) {
            throw new DataEntryError("Возраст студента в интервале от 15 до 30 лет.");
        }
        return num;
    }

    private static String transform(String string) {
        return StringUtils.capitalize(string.toLowerCase());
    }

    public static String validateName(String name) {
        List<String> e = Arrays.stream(name.split(" ")).map((x) -> validateString(x, "Имя не может быть пустым и должено содержать только слова через пробел.")).toList();
        StringBuilder stringBuilder = new StringBuilder((e.get(0)));
        for (int i = 1; i < e.size(); i++) {
            stringBuilder.append(" ").append(e.get(i));
        }
        return stringBuilder.toString();
    }
}
