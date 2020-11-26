package br.pucpr.util;

import java.util.Random;

public class Utils
{
    private static Random r = new Random();
    public static float Lerp(float value, float desiredValue, float interpolation)
    {
        return (float)(value * (1.0 - interpolation)) + (desiredValue * interpolation);
    }

    public static float Random(float min, float max)
    {
        return r.nextFloat() * (max - min) + min;
    }
}
