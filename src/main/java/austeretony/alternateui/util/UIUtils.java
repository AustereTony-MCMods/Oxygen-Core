package austeretony.alternateui.util;

import java.util.List;

public class UIUtils {

    public static void divideText(List<String> dest, String text, int maxWidth, int maxHeight, float textScale, int verticalOffset) {     
        dest.clear();

        StringBuilder builder = new StringBuilder();    
        int 
        index = 0, 
        wordStartIndex = 0;
        boolean
        reachedLimit = false,
        wordProcessing = false;
        char prevSymbol = '0';
        String line;
        for (char symbol : text.toCharArray()) {
            if ((getTextHeight(textScale) + verticalOffset) * dest.size() >= maxHeight)
                break;
            if (symbol != ' ') {
                wordProcessing = true;
                if (prevSymbol == ' ')
                    wordStartIndex = index;
            }
            if (symbol == '\n') {
                dest.add(builder.toString());
                builder.delete(0, builder.length());
                index = 0;
                continue;
            }
            if (getTextWidth(builder.toString() + String.valueOf(symbol), textScale) <= maxWidth)
                builder.append(symbol);
            else {
                if (symbol == '.' 
                        || symbol == ',' 
                        || symbol == '!'
                        || symbol == '?')
                    builder.append(symbol);
                if (wordProcessing) {
                    dest.add(builder.toString().substring(0, wordStartIndex));
                    builder.delete(0, wordStartIndex);
                } else {
                    dest.add(builder.toString());
                    builder.delete(0, builder.length());
                }
                if (symbol != ' ')
                    builder.append(symbol);
                index = builder.length() - 1;
            }
            wordProcessing = false;
            prevSymbol = symbol;
            index++;
        }
        if (builder.length() != 0)
            dest.add(builder.toString());
    }

    public static float getTextWidth(String text, float textScale) {
        return AlternateUIReference.getMinecraft().fontRenderer.getStringWidth(text) * textScale;
    }

    public static float getTextHeight(float textScale) {
        return AlternateUIReference.getMinecraft().fontRenderer.FONT_HEIGHT * textScale;
    }
}
