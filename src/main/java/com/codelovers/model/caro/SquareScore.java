
package com.codelovers.model.caro;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 @trungson
 */
public class SquareScore implements Cloneable {

    private double row;
    private double col;
    private double slashDiag;
    private double backSlashDiag;

    public SquareScore() {
        this.row = 0D;
        this.col = 0D;
        this.slashDiag = 0D;
        this.backSlashDiag = 0D;
    }

    public SquareScore(double row, double col, double slashDiag) {
        this.row = row;
        this.col = col;
        this.slashDiag = slashDiag;
        this.backSlashDiag = slashDiag;
    }

    public String toString() {
        return "row =" + this.row + " ; col =" + this.col + " ; slashDiag =" + this.slashDiag + " ; backSlashDiag =" + this.backSlashDiag;
    }

    public SquareScore clone() {
        SquareScore squareScore = null;
        try {
            squareScore = new SquareScore();
            squareScore = (SquareScore) super.clone();
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(SquareScore.class.getName()).log(Level.SEVERE, null, ex);
        }
        return squareScore;
    }

    public double getScore() {
        return this.col + this.row + this.slashDiag + this.backSlashDiag;
    }
    public void setScore(double score){
        this.col=score;
        this.row=0D;
        this.slashDiag=0D;
        this.backSlashDiag=0D;
    }

    public double getRow() {
        return row;
    }

    public void setRow(double row) {
        this.row = row;
    }

    public double getCol() {
        return col;
    }

    public void setCol(double col) {
        this.col = col;
    }

    public double getDiag() {
        return slashDiag;
    }

    public void setDiag(double slashDiag) {
        this.slashDiag = slashDiag;
    }

    public double getSlashDiag() {
        return slashDiag;
    }

    public void setSlashDiag(double slashDiag) {
        this.slashDiag = slashDiag;
    }

    public double getBackSlashDiag() {
        return backSlashDiag;
    }

    public void setBackSlashDiag(double backSlashDiag) {
        this.backSlashDiag = backSlashDiag;
    }

}
