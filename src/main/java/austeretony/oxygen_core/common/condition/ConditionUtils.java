package austeretony.oxygen_core.common.condition;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ConditionUtils {

    @Nonnull
    public static String[] splitConditionToMembers(String conditionStr) {
        for (String operationStr : EnumComparisonOperation.OPERATIONS.keySet()) {
            if (conditionStr.contains(operationStr)) {
                String[] splitted = conditionStr.split(Pattern.quote(operationStr));
                if (splitted[1].contains("=")) continue;
                String[] members;
                if (splitted[0].contains("[")) {
                    String[] 
                            id = splitted[0].split(Pattern.quote("[")),
                            args = id[1].substring(0, id[1].length() - 1).split(Pattern.quote(";"));
                    members = new String[3 + args.length];
                    members[0] = id[0];

                    for (int i = 0; i < args.length; i++)
                        members[3 + i] = args[i].trim();
                } else {
                    members = new String[3];
                    members[0] = splitted[0];
                }

                members[1] = operationStr;
                members[2] = splitted[1];

                return members;
            }
        }
        return new String[0];
    }

    @Nullable
    public static Number parseNumber(String str) {
        Number number = null;
        try {
            number = NumberFormat.getInstance().parse(str);
        } catch (ParseException exception) {
            exception.printStackTrace();
        }
        return number;
    }

    public static int parseInt(String str) {
        int value = 0;
        try {
            value = Integer.parseInt(str);
        } catch (NumberFormatException exception) {
            exception.printStackTrace();
        }
        return value;
    }

    public static long parseLong(String str) {
        long value = 0L;
        try {
            value = Long.parseLong(str);
        } catch (NumberFormatException exception) {
            exception.printStackTrace();
        }
        return value;
    }

    public static float parseFloat(String str) {
        float value = 0;
        try {
            value = Float.parseFloat(str);
        } catch (NumberFormatException exception) {
            exception.printStackTrace();
        }
        return value;
    }

    public static double parseDouble(String str) {
        double value = 0;
        try {
            value = Double.parseDouble(str);
        } catch (NumberFormatException exception) {
            exception.printStackTrace();
        }
        return value;
    }

    public static List<String> splitHighestParenthesesExpression(String str) {
        List<String> fragments = new ArrayList<>(1);

        char[] chars = str.toCharArray();
        char c;

        int opening = - 1;
        int closing = - 1;
        int occurences = 0;

        boolean added = false;

        for (int i = 0; i < chars.length; i++) {
            c = chars[i];
            if (c == '(') {
                added = false;

                if (opening == - 1) 
                    opening = i;
                else
                    occurences++;
            } else if (c == ')') {
                if (occurences == 0) {
                    closing = i;

                    String conditions = str.substring(opening + 1, closing);
                    fragments.add(conditions);
                    added = true;

                    opening = - 1;
                    closing = - 1;
                } else
                    occurences--;
            } else if (added) {
                if (c == '&' || c == '|') {
                    fragments.add(String.valueOf(c));
                    added = false;
                }
            }
        }
        return fragments;
    }
}
