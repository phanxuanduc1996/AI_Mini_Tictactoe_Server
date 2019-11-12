
package com.codelovers.model.caro;

import com.codelovers.service.game.CaroState;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 @trungson
 */
public class Square implements Cloneable {

    private String label;
    private XYLocation positive;
    public SquareScore squareScore;

    public Square() {
        this.label = CaroState.EMPTY;
        this.positive = new XYLocation();
        squareScore = new SquareScore();
    }

    public Square(String label, XYLocation positive, SquareScore score) {
        this.label = label;
        this.positive = positive;
        this.squareScore = score;
    }

    public String toString() {
        return "Label :" + this.label + "\nScore :" + this.squareScore;
    }

//    public static void main(String[] args) {
//        SquareScore score = new SquareScore(1, 2, 3);
//        Square square = new Square("cuong", new XYLocation(1, 1),  score);
//        System.out.println("TSquare :" + square.toString());
//        Square square2 = square.clone();
//        square2.setLabel("Duc");
//        square2.squareScore.setRow(5);
//        square2.squareScore.setCol(5);
//        square2.squareScore.setDiag(5);
//        System.out.println("Square :" + square.toString());
//        System.out.println("Square2 :" + square2.toString());
//
//    }

    public Square clone() {
        Square copy = null;
        try {
            copy = (Square) super.clone();
            copy.positive = this.positive.clone();
            copy.squareScore = this.squareScore.clone();
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(Square.class.getName()).log(Level.SEVERE, null, ex);
        }
        return copy;
    }

    

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public XYLocation getPositive() {
        return positive;
    }

    public void setPositive(XYLocation positive) {
        this.positive = positive;
    }

    public SquareScore getSquareScore() {
        return squareScore;
    }

    public void setSquareScore(SquareScore score) {
        this.squareScore = score;
    }
    public void setScore(double score){
        this.squareScore.setScore(score);
    }
    public double getScore(){
        return this.squareScore.getScore();
    }
  

}
