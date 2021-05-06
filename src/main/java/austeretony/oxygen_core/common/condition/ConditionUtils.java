package austeretony.oxygen_core.common.condition;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;

public class ConditionUtils {

    public static final String
            LEFT_BRACKET_PATTERN = Pattern.quote("["),
            SEMICOLON_PATTERN = Pattern.quote(";");

    @Nonnull
    public static String[] splitConditionToMembers(String conditionStr) {
        for (String operationStr : ComparisonOperator.OPERATIONS.keySet()) {
            if (!conditionStr.contains(operationStr)) continue;

            String[] fragments = conditionStr.split(Pattern.quote(operationStr));
            if (fragments[1].contains("=")) continue;

            String[] members;
            if (fragments[0].contains("[")) {
                String[] id = fragments[0].split(LEFT_BRACKET_PATTERN);
                String[] args = id[1].substring(0, id[1].length() - 1).split(SEMICOLON_PATTERN);
                members = new String[3 + args.length];
                members[0] = id[0];

                for (int i = 0; i < args.length; i++) {
                    members[3 + i] = args[i].trim();
                }
            } else {
                members = new String[3];
                members[0] = fragments[0];
            }

            members[1] = operationStr;
            members[2] = fragments[1];

            return members;
        }
        return new String[0];
    }

    public static List<String> parseTopLevelParenthesesExpression(String str) {
        List<String> fragments = new ArrayList<>(3);

        char[] chars = str.toCharArray();
        char c;
        int opening = -1, closing, occurrences = 0;
        boolean added = false;

        for (int i = 0; i < chars.length; i++) {
            c = chars[i];
            if (c == '(') {
                added = false;

                if (opening == -1) {
                    opening = i;
                } else {
                    occurrences++;
                }
            } else if (c == ')') {
                if (occurrences == 0) {
                    closing = i;

                    String conditions = str.substring(opening + 1, closing);
                    fragments.add(conditions);
                    added = true;

                    opening = -1;
                } else {
                    occurrences--;
                }
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
