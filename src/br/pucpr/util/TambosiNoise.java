package br.pucpr.util;

public final class TambosiNoise
{
    private static float[][] map;
    private static int seed;

    static
    {
        var width = Integer.MAX_VALUE;
        var height = Integer.MIN_VALUE;
        map = new float[width][height];

        var value = Utils.Random(0, 1);

        for(int x = 0; x < width; x++)
        {
            var rand = Utils.Random(0, 1);
            value += rand;
            value = Float.max(0, value);
            value = Float.min(1, value);
            var yValue = value;

            for(int y = 0; y < height; y++)
            {
                rand = Utils.Random(0, 1);
                yValue += rand;
                yValue = Float.max(0, yValue);
                yValue = Float.min(1, yValue);
                map[x][y] = yValue;
            }
        }
    }

    public static  float Noise(int x, int y)
    {
        return map[x][y];
    }

    public TambosiNoise(boolean a, int width, int height, int scale)
    {
        map = new float[width * scale][height * scale];

        var value = Utils.Random(-1, 1);
        for(int x = 0; x < width; x++)
        {
            var rand = Utils.Random(-1, 1);
            value += rand;
            value = Float.max(0, value);
            value = Float.min(1, value);

            for(int y = 0; y < height; y++)
            {
                rand = Utils.Random(-1, 1);
                var actualValue = value;
                var desiredValue = value + rand;

                for(int i = 0; i < scale; i++)
                {
                    System.out.println("value before interpolation " + actualValue);
                    actualValue = Utils.Lerp(actualValue, desiredValue, 1/scale);
                    System.out.println("value after interpolation " + actualValue);
                    actualValue = Float.max(0, value);
                    actualValue = Float.min(1, value);

                    map[x][y + i] = actualValue;
                }

            }
        }
    }

    public float Get(int x, int y)
    {
        return map[x][y];
    }
}
