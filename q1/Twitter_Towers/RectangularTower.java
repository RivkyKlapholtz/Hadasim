public class RectangularTower {
    private int length;
    private int width;
    public RectangularTower(int len, int wid)
    {
        //Correct input is guaranteed: the height of a tower is large equal to 2
        length = len;
        width = wid;
    }
    public int calcPerimeter()
    {
        return (2 * length) + (2 * width);
    }

    public int calcArea()
    {
        return length * width;
    }

    public boolean isSquare()
    {
        return length == width;
    }
    public boolean diffLenGreaterThan5()
    {
        return Math.abs(length - width) >= 5;
    }
}
