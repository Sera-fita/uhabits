/*
 * Copyright (C) 2016 Álinson Santos Xavier <isoron@gmail.com>
 *
 * This file is part of Loop Habit Tracker.
 *
 * Loop Habit Tracker is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Loop Habit Tracker is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package org.isoron.uhabits.helpers;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import org.isoron.uhabits.R;

public class ColorHelper
{
    public static int CSV_PALETTE[] =
    {
        Color.parseColor("#D32F2F"), //  0 red
        Color.parseColor("#E64A19"), //  1 orange
        Color.parseColor("#F9A825"), //  2 yellow
        Color.parseColor("#AFB42B"), //  3 light green
        Color.parseColor("#388E3C"), //  4 dark green
        Color.parseColor("#00897B"), //  5 teal
        Color.parseColor("#00ACC1"), //  6 cyan
        Color.parseColor("#039BE5"), //  7 blue
        Color.parseColor("#5E35B1"), //  8 deep purple
        Color.parseColor("#8E24AA"), //  9 purple
        Color.parseColor("#D81B60"), // 10 pink
        Color.parseColor("#303030"), // 11 dark grey
        Color.parseColor("#aaaaaa")  // 12 light grey
    };

    public static int colorToPaletteIndex(Context context, int color)
    {
        int[] palette = getPalette(context);

        for(int k = 0; k < palette.length; k++)
            if(palette[k] == color) return k;

        return -1;
    }

    public static int[] getPalette(Context context)
    {
        int resourceId = UIHelper.getStyleResource(context, R.attr.palette);
        if(resourceId < 0) return CSV_PALETTE;

        return context.getResources().getIntArray(resourceId);
    }

    public static int getColor(Context context, int paletteColor)
    {
        if(context == null) throw new IllegalArgumentException("Context is null");

        int palette[] = getPalette(context);
        if(paletteColor < 0 || paletteColor >= palette.length)
        {
            Log.w("ColorHelper", String.format("Invalid color: %d. Returning default.", paletteColor));
            paletteColor = 0;
        }

        return palette[paletteColor];
    }

    public static int mixColors(int color1, int color2, float amount)
    {
        final byte ALPHA_CHANNEL = 24;
        final byte RED_CHANNEL = 16;
        final byte GREEN_CHANNEL = 8;
        final byte BLUE_CHANNEL = 0;

        final float inverseAmount = 1.0f - amount;

        int a = ((int) (((float) (color1 >> ALPHA_CHANNEL & 0xff) * amount) +
                ((float) (color2 >> ALPHA_CHANNEL & 0xff) * inverseAmount))) & 0xff;
        int r = ((int) (((float) (color1 >> RED_CHANNEL & 0xff) * amount) +
                ((float) (color2 >> RED_CHANNEL & 0xff) * inverseAmount))) & 0xff;
        int g = ((int) (((float) (color1 >> GREEN_CHANNEL & 0xff) * amount) +
                ((float) (color2 >> GREEN_CHANNEL & 0xff) * inverseAmount))) & 0xff;
        int b = ((int) (((float) (color1 & 0xff) * amount) +
                ((float) (color2 & 0xff) * inverseAmount))) & 0xff;

        return a << ALPHA_CHANNEL | r << RED_CHANNEL | g << GREEN_CHANNEL | b << BLUE_CHANNEL;
    }

    public static int setHue(int color, float newHue)
    {
        return setHSVParameter(color, newHue, 0);
    }

    public static int setSaturation(int color, float newSaturation)
    {
        return setHSVParameter(color, newSaturation, 1);
    }

    public static int setValue(int color, float newValue)
    {
        return setHSVParameter(color, newValue, 2);
    }

    public static int setAlpha(int color, float newAlpha)
    {
        int intAlpha = (int) (newAlpha * 255);
        return Color.argb(intAlpha, Color.red(color), Color.green(color), Color.blue(color));
    }

    public static int setMinValue(int color, float newValue)
    {
        float hsv[] = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] = Math.max(hsv[2], newValue);
        return Color.HSVToColor(hsv);
    }

    private static int setHSVParameter(int color, float newValue, int index)
    {
        float hsv[] = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[index] = newValue;
        return Color.HSVToColor(hsv);
    }

    public static String toHTML(int color)
    {
        return String.format("#%06X", 0xFFFFFF & color);
    }
}