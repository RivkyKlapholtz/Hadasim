public class TriangularTower {
    private int length;
    private int width;
    public TriangularTower(int len, int wid)
    {
        //Correct input is guaranteed.
        length = len;
        width = wid;
    }

    public double calcPerimeter()
    {
        return (2 * Math.sqrt(Math.pow((width/2.0), 2) + Math.pow(length, 2))) + width;
    }
    public boolean isEvenWidth()
    {
        return width % 2 == 0;
    }
    public boolean widGreaterThanTwiceLen()
    {
        return width > length * 2;
    }

    public String print() {
        int numOfInnerParts = (width-2)/2;
        int numOfLinesPerPart = (length - 2)/numOfInnerParts;
        int numOfLinesInFirstPart = numOfLinesPerPart + ((length - 2)%numOfInnerParts);
        int numOfSpaces = width/2;
        String s = "";
        //first line
        s = addSpaces(numOfSpaces, s);
        s += "*\n";
        //the inner lines
        for (int i = 1; i <= numOfInnerParts; i++) {
            numOfSpaces--;
            if(i == 1)
            {
                for (int j = 1; j <= numOfLinesInFirstPart; j++) {
                    s = addSpaces(numOfSpaces, s);
                    s = addStars((i * 2) + 1, s);
                    s += "\n";
                }
            }
            else
            {
                for (int j = 1; j <= numOfLinesPerPart; j++) {
                    s = addSpaces(numOfSpaces, s);
                    s = addStars((i * 2) + 1, s);
                    s += "\n";
                }
            }
        }
        //last line
        s = addStars(width, s);

        return s;

    }
    private String addSpaces(int numOfSpaces, String s)
    {
        for (int i = 1; i <= numOfSpaces ; i++) {
            s += " ";
        }
        return s;
    }
    private String addStars(int numOfStars, String s)
    {
        for (int i = 1; i <= numOfStars ; i++) {
            s += "*";
        }
        return s;
    }







}
