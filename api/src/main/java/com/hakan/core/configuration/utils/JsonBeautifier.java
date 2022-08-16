package com.hakan.core.configuration.utils;

import com.hakan.core.utils.Validate;

import javax.annotation.Nonnull;

/**
 * JsonBeautifier class to
 * beautify json string.
 */
public final class JsonBeautifier {

    /**
     * Beautifies the given json string.
     *
     * @param input Input json string.
     * @return Beautified json string.
     */
    @Nonnull
    public static String of(@Nonnull String input) {
        Validate.notNull(input, "input cannot be null!");

        StringBuilder inputBuilder = new StringBuilder();
        char[] inputChar = input.toCharArray();

        int tabCount = 0;
        for (int i = 0; i < inputChar.length; i++) {
            String charI = String.valueOf(inputChar[i]);
            if (charI.equals("}") || charI.equals("]")) {
                tabCount--;
                if (!String.valueOf(inputChar[i - 1]).equals("[") && !String.valueOf(inputChar[i - 1]).equals("{"))
                    inputBuilder.append(newLine(tabCount));
            }
            inputBuilder.append(charI);

            if (charI.equals("{") || charI.equals("[")) {
                tabCount++;
                if (String.valueOf(inputChar[i + 1]).equals("]") || String.valueOf(inputChar[i + 1]).equals("}"))
                    continue;

                inputBuilder.append(newLine(tabCount));
            }

            if (charI.equals(","))
                inputBuilder.append(newLine(tabCount));
        }

        return inputBuilder.toString();
    }

    /**
     * New line.
     *
     * @param tabCount Tab count.
     * @return New line.
     */
    @Nonnull
    private static String newLine(int tabCount) {
        StringBuilder builder = new StringBuilder();

        builder.append("\n");
        for (int j = 0; j < tabCount; j++)
            builder.append("  ");
        return builder.toString();
    }
}