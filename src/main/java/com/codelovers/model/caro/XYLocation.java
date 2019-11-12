package com.codelovers.model.caro;
/**
 *
 @trungson
 */
public class XYLocation implements Cloneable {

    public enum Direction {
        North, South, East, West
    };

    private int x;
    private int y;

    public XYLocation() {
    }

    public XYLocation(int x, int y) {
        this.x = x;
        this.y = y;
    }

  
    public XYLocation clone() {

        XYLocation copy = new XYLocation(this.x, this.y);
        return copy;
    }
  
    

    @Override
    public boolean equals(Object o) {
        if (null == o || !(o instanceof XYLocation)) {
            return super.equals(o);
        }
        XYLocation anotherLoc = (XYLocation) o;
        return ((anotherLoc.getX() == x) && (anotherLoc
                .getY() == y));
    }

    @Override
    public String toString() {
        return " ( " + x + " , " + y + " ) ";
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

}
