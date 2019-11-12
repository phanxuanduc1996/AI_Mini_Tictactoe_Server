package com.codelovers.service.game;

import com.codelovers.model.caro.Square;
import com.codelovers.model.caro.SquareScore;
import com.codelovers.model.caro.XYLocation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 @trungson
 */
public class CaroState implements Cloneable {

    public static final String O = "O";
    public static final String X = "X";
    public static final String EMPTY = "-";
    private String playerToMove;
    private boolean isTerminal;
    private XYLocation middle;
    private double utility = -1; // 1: win for X, 0: win for O, 0.5: draw
    private LinkedList<XYLocation> candidates;
    private Square[][] board = new Square[CaroGame.ROW][CaroGame.COLUMN];
    private XYLocation lastMove;
    private double boardScore;
   
    {
        this.isTerminal = false;
        this.lastMove = null;
        this.playerToMove = X;
        this.middle = null;
        this.candidates = new LinkedList<>();
        this.boardScore = 0D;
        for (int i = 0; i < CaroGame.ROW; i++) {
            for (int j = 0; j < CaroGame.COLUMN; j++) {
                this.board[i][j] = new Square();
            }
        }
    }

    public CaroState(XYLocation lastMove) {
        this.lastMove = lastMove;
    }

    public double evaluateBoard() {

        return this.getBoardScore();
    }

    public String toString1() {
        return " { \"PlayToMove\":\"" + this.playerToMove + "\",\n \"utiliti\" : \"" + this.utility + "\",\n \"map\" :\n \"" + this.toString() +"\" } ";
    }

    public double getBoardScore() {
        if (this.playerToMove == X) {
            return -1D * boardScore;
        } else {
            return boardScore;
        }
    }

    public void mark(XYLocation action) {
        if (this.candidates.contains(action)) {
            this.candidates.remove(action);
        }
        int col = action.getX(), row = action.getY();
        if (utility == -1 && getValue(col, row) == EMPTY) {
            this.board[row][col].setLabel(playerToMove);

            this.evaluateSquareScore(col, row); // Cáº­p nháº­t láº¡i Ä‘iá»ƒm cho cÃ¡c Ã´ xung quanh Ã´ vá»«a má»›i Ä‘Ã¡nh
            if(this.playerToMove==X){
                this.board[row][col].setScore(7771D);
            }
            else this.board[row][col].setScore(7770D);

            if (this.isTerminal == true) {
                utility = (playerToMove == X ? 1 : 0);
            }
            playerToMove = (playerToMove == X ? O : X);
        }
    }

    public void evaluateSquareScore(int col, int row) {
        //Cap nhat diem cho hang ngang, doc va 2 duong cheo
        evaluateSquareScoresAroundPosition(col, row, 1, 0, "Row");
        evaluateSquareScoresAroundPosition(col, row, 0, 1, "Col");
        evaluateSquareScoresAroundPosition(col, row, 1, -1, "SlashDiag");
        evaluateSquareScoresAroundPosition(col, row, 1, 1, "BackSlashDiag");
        // Sap xep danh sach cac ung vien candidates
        Object[] candidateArray = candidates.toArray(); // Chuyen thanh mang
        int length = candidateArray.length, max = 0;
        for (int i = 0; i < length - 1; i++) {
            max = i;

            for (int j = i + 1; j < length; j++) {
                XYLocation maxPosition = (XYLocation) candidateArray[max];
                int colTemp, rowTemp;
                colTemp = maxPosition.getX();
                rowTemp = maxPosition.getY();
                double maxScore = this.board[rowTemp][colTemp].getScore();

                XYLocation jPosition = (XYLocation) candidateArray[j];
                int colTempJ, rowTempJ;
                colTempJ = jPosition.getX();
                rowTempJ = jPosition.getY();
                double jScore = this.board[rowTempJ][colTempJ].getScore();
                if (jScore > maxScore) {
                    max = j;
                }
            }
            Object currPostion = candidateArray[i];
            candidateArray[i] = candidateArray[max];
            candidateArray[max] = currPostion;
        }
        // Cap nhat lai vao LinkedList
        ListIterator<XYLocation> iterator = candidates.listIterator();
        int count = 0;
        while (iterator.hasNext()) {
            iterator.next();
            iterator.set((XYLocation) candidateArray[count++]);
        }

    }

    private void evaluateSquareScoresAroundPosition(int col, int row, int colFactor, int rowFactor, String direction) {
        try {
            Method getMethod;
            Method setMethod;
            getMethod = SquareScore.class.getMethod("get" + direction);
            setMethod = SquareScore.class.getMethod("set" + direction, double.class);

            double xNumber = 0D; // So o duoc danh la X
            double oNumber = 0D;

            for (int colI = col - 4 * colFactor, rowI = row - 4 * rowFactor; colI <= col + 4 * colFactor && ((rowFactor >= 0) ? (rowI <= row + 4 * rowFactor) : (rowI >= row + 4 * rowFactor)); colI = colI + 1 * colFactor, rowI = rowI + 1 * rowFactor) {
                if (this.isValue(new XYLocation(colI, rowI)) == false) {
                    continue;
                }
                String preCheckValue = null;
                String checkValue = getValue(colI, rowI);
                // Them vao List cac ung cu vien
                if (checkValue == EMPTY) {
                    XYLocation candidate = new XYLocation(colI, rowI);
                    if (candidates.contains(candidate) == false) {
                        if (Math.abs(colI - col) <= Math.abs(CaroGame.RADIUS * colFactor) && Math.abs(rowI - row) <= Math.abs(CaroGame.RADIUS * rowFactor)) {
                            this.candidates.addFirst(candidate);
                        }

                    }
                }
                // Kiá»ƒm tra xem Ä‘Ã£ háº¿t bÃ n cá»� hay chÆ°a 
                if (checkValue == O) {
                    oNumber++;
                    xNumber = 0D;
                } else if (checkValue == X) { //checkValue==X
                    xNumber++;
                    oNumber = 0D;
                }
                if (xNumber == 5 || oNumber == 5) { // X thang!
                    this.isTerminal = true;
                }
                if(this.board[rowI][colI].getLabel()==EMPTY){
                //Cáº­p nháº­t láº¡i Ä‘iá»ƒm náº¿u cÃ³ dÃ£y X liÃªn tiáº¿p á»Ÿ xung quanh Ã´ Ä‘ang xÃ©t
                addUpdateScore(col, row, colI, rowI, colFactor, rowFactor, X, getMethod, setMethod);
                //Cáº­p nháº­t láº¡i Ä‘iá»ƒm náº¿u cÃ³ dÃ£y O liÃªn tiáº¿p á»Ÿ xung quanh Ã´ Ä‘ang xÃ©t
                //Chi co 1 trong 2 ham duoc thuc hien !
                addUpdateScore(col, row, colI, rowI, colFactor, rowFactor, O, getMethod, setMethod);
                }
                // Giáº£m sá»‘ Ã´ O hoáº·c sá»‘ Ã´ X khi 1 Ã´ khÃ´ng cÃ²n á»Ÿ trong Ä‘Æ°á»�ng 5
                if (this.isVaule(colI, rowI, colFactor, rowFactor) && ((colI - col) * colFactor >= 0 && (rowI - row) * rowFactor >= 0)) {
                    preCheckValue = getValue(colI - 4 * colFactor, rowI - 4 * rowFactor);
                    if (preCheckValue == X) {
                        xNumber--;
                    } else if (preCheckValue == O) {
                        oNumber--;
                    }
                }
            }
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(CaroState.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(CaroState.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(CaroState.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addUpdateScore(int col, int row, int colI, int rowI, int colFactor, int rowFactor, String checkLable, Method getMethod, Method setMethod) {
        boolean middle = true;
        double increase = 0D; //Biáº¿n trung gian Ä‘á»ƒ Ä‘áº¿m sá»‘ Ã´ X(O) liÃªn tiáº¿p bÃªn trÃ¡i vÃ  pháº£i Ã´ Ä‘ang xÃ©t
        double distanceAddScore = 0D; // Ã” cÃ ng gáº§n dÃ£y X(O) liÃªn tiáº¿p thÃ¬ cÃ ng Ä‘Æ°á»£c nhiá»�u Ä‘iá»ƒm - distanceAddScore : sá»‘ Ä‘iá»ƒm cá»™ng theo khoáº£ng cÃ¡ch
        double valueSquareNumber = 0D; // Ä�áº¿m xem cÃ³ bao nhiÃªu Ã´ cÃ³ thá»ƒ táº¡o thÃ nh Ä‘Æ°á»�ng 5. Náº¿u >=5 thÃ¬ Nhá»¯ng Ã´ trong dÃ£y má»›i cÃ³ giÃ¡ trá»‹.
        double numberSquareOfSegment = 0D; //Sá»‘ Ã´ X(O) liÃªn tiáº¿p trong dÃ£y 
        SquareScore scoreObject = this.board[rowI][colI].squareScore;
        // Ä�áº¿m sá»‘ Ã´ liÃªn tiáº¿p bÃªn trÃ¡i Ã´ Ä‘ang xÃ©t 
        int j = -1;
        boolean isIncrease = true;
        for (j = -1; j >= -4; j--) {
            if (this.isValue(new XYLocation(colI + j * colFactor, rowI + j * rowFactor)) == false) {
                break;
            }

            if (this.board[rowI + j * rowFactor][colI + j * colFactor].getLabel() == checkLable) {
                if (isIncrease == true) {
                    increase++;
//                    System.out.println("increase"+increase+"j"+j);
                } else {
                    distanceAddScore += (4D - (Math.abs(Math.abs(j) - increase) - 1)) / CaroGame.DISTANCE_DIVIDE_COEFFICIENT_;
                }
                valueSquareNumber++;
            } else if (this.board[rowI + j * rowFactor][colI + j * colFactor].getLabel() == EMPTY) {
                valueSquareNumber++;
                isIncrease = false;
            } else {
                isIncrease = false;
                break;
            }
        }
        // Kiá»ƒm tra xem bÃªn trÃ¡i cá»§a dÃ£y cÃ¡c Ã´ X(O) liÃªn tiáº¿p cÃ³ trá»‘ng hay khÃ´ng
        int checkLeftForMiddle = -1 - (int) increase;
        if (this.isValue(new XYLocation(colI + checkLeftForMiddle * colFactor, rowI + checkLeftForMiddle * rowFactor)) == false) {
            middle = false;
        } else if (this.board[rowI + checkLeftForMiddle * rowFactor][colI + checkLeftForMiddle * colFactor].getLabel() != EMPTY) {
            middle = false;
        }
        numberSquareOfSegment += increase;

        // Ä�áº¿m sá»‘ Ã´ liÃªn tiáº¿p bÃªn pháº£i Ã´ Ä‘ang xÃ©t
        j = 1;
        increase = 0D;
        isIncrease = true;
        for (j = 1; j <= 4; j++) {
            if (this.isValue(new XYLocation(colI + j * colFactor, rowI + j * rowFactor)) == false) {
                break;
            }

            if (this.board[rowI + j * rowFactor][colI + j * colFactor].getLabel() == checkLable) {
                if (isIncrease == true) {
                    increase++;
//                    System.out.println("increase"+increase+"j"+j);
                } else {
                    distanceAddScore += (4D - (Math.abs(Math.abs(j) - increase) - 1)) / CaroGame.DISTANCE_DIVIDE_COEFFICIENT_;
                }
                valueSquareNumber++;
            } else if (this.board[rowI + j * rowFactor][colI + j * colFactor].getLabel() == EMPTY) {
                valueSquareNumber++;
                isIncrease = false;
            } else {
                isIncrease = false;
                break;
            }
        }
        // Kiá»ƒm tra xem bÃªn pháº£i cá»§a dÃ£y cÃ¡c Ã´ X(O) liÃªn tiáº¿p cÃ³ trá»‘ng hay khÃ´ng
        int checkRightForMiddle = 1 + (int) increase;
        if (this.isValue(new XYLocation(colI + checkRightForMiddle * colFactor, rowI + checkRightForMiddle * rowFactor)) == false) {
            middle = false;
        } else if (this.board[rowI + checkRightForMiddle * rowFactor][colI + checkRightForMiddle * colFactor].getLabel() != EMPTY) {
            middle = false;
        }
        numberSquareOfSegment += increase;
        valueSquareNumber++;
//        System.out.println("colrow"+colI+rowI+"numberSquare"+numberSquareOfSegment+"value="+valueSquareNumber);
        // Cáº­p nháº­t láº¡i Ä‘iá»ƒm
        try {
            double currScore = 0D;
            if (checkLable == O) {
                currScore = (double) getMethod.invoke(scoreObject);
            }
            if (valueSquareNumber < 5) { // KhÃ´ng xÃ©t náº¿u khÃ´ng Ä‘á»§ sá»‘ Ã´ Ä‘á»ƒ cÃ³ thá»ƒ táº¡o thÃ nh Ä‘Æ°á»�ng 5
                setMethod.invoke(scoreObject, currScore);
                return;
            }
            switch ((int) numberSquareOfSegment) {
                case 0:
                    setMethod.invoke(scoreObject, currScore + distanceAddScore);
                    //scoreObject.setRow/Col/..(currScore +distanceAddScore))
                    break;
                case 1:
                    if (middle == true) {
                        setMethod.invoke(scoreObject, currScore + CaroGame.FIRST + distanceAddScore);
                    } else {
                        setMethod.invoke(scoreObject, currScore + CaroGame.LIMITED_FIRST + distanceAddScore);
                    }
                    break;
                case 2:
                    if (middle == true) {
                        setMethod.invoke(scoreObject, currScore + CaroGame.SECOND + distanceAddScore);
                        // 2 Ã´ liÃªn tiáº¿p khÃ´ng bá»‹ cháº·n
                    } else {
                        setMethod.invoke(scoreObject, currScore + CaroGame.LIMITED_SECOND + distanceAddScore);
                        //  2 Ã´ liÃªn tiáº¿p nhÆ°ng bá»‹ cháº·n
                    }
                    break;
                case 3:

                    if (middle == true) {
                        setMethod.invoke(scoreObject, currScore + (CaroGame.THIRD + distanceAddScore));
                    } else {
                        setMethod.invoke(scoreObject, currScore + CaroGame.LIMITED_THIRD + distanceAddScore);
                        // 3 Ã´ liÃªn tiáº¿p nhÆ°ng bá»‹ cháº·n
                    }
                    break;
                case 4:
                    if (middle == true) {
                        setMethod.invoke(scoreObject, currScore + (CaroGame.FOURTH + distanceAddScore));
                    } else {
//                        System.out.println("!");
                        setMethod.invoke(scoreObject, currScore + CaroGame.LIMITED_FOURTH + distanceAddScore);
//                        System.out.println(""+(double) getMethod.invoke(scoreObject));
                    }
                    break;
                case 5:
                    setMethod.invoke(scoreObject, currScore + CaroGame.FIFTH);
                    break;
            }
        } catch (IllegalAccessException ex) {
            Logger.getLogger(CaroState.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(CaroState.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(CaroState.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public boolean isVaule(int col, int row, int colFactor, int rowFactor) {
        if (col - 4 * colFactor < 0 || col - 4 * colFactor >= CaroGame.COLUMN || row - 4 * rowFactor < 0
                || row - 4 * rowFactor >= CaroGame.ROW) {
            return false;
        }
        return true;
    }

    public boolean isValue(XYLocation position) {
        int col = position.getX();
        int row = position.getY();
        if (col < 0 || col >= CaroGame.COLUMN || row < 0 || row >= CaroGame.ROW) {
            return false;
        }
        return true;
    }

    public Square getSquareOfBoard(int col, int row) {
        return this.board[row][col];
    }

    public LinkedList<XYLocation> getUnMarkedPositions() {
        return this.candidates;
    }

//    public static void main(String[] args) {
//        CaroState first = new CaroState(null);
//
//        first.mark(new XYLocation(10, 10));
//        CaroState second = first.clone();
//
//        second.mark(new XYLocation(11, 10));
//    }

    @Override
    public CaroState clone() {

        CaroState copy = null;
        try {
            copy = (CaroState) super.clone();
            if (this.lastMove == null) {
                copy.lastMove = new XYLocation(-1, -1);
            } else {
                copy.lastMove = this.lastMove.clone();
            }
            copy.board = new Square[CaroGame.ROW][CaroGame.COLUMN];
            for (int i = 0; i < CaroGame.ROW; i++) {
                for (int j = 0; j < CaroGame.COLUMN; j++) {
                    copy.board[i][j] = this.board[i][j].clone();
                }
            }
            LinkedList<XYLocation> copycadidates = new LinkedList<>();
            ListIterator<XYLocation> iterator = this.candidates.listIterator();
            while (iterator.hasNext()) {
                XYLocation copyCandidate = iterator.next().clone();
                copycadidates.add(copyCandidate);
            }
            copy.candidates = copycadidates;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace(); // should never happen...
        }
        return copy;
    }

    @Override
    public boolean equals(Object anObj) {
        if (anObj != null && anObj.getClass() == getClass()) {
            CaroState anotherState = (CaroState) anObj;
            for (int i = 0; i < CaroGame.COLUMN * CaroGame.ROW; i++) {
                if (board[i] != anotherState.board[i]) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder strBuilder = new StringBuilder();
        for (int row = 0; row < CaroGame.COLUMN; row++) {
            for (int col = 0; col < CaroGame.COLUMN; col++) {
                strBuilder.append(getValue(col, row) + " ");
            }
            strBuilder.append("\n");
        }
        return strBuilder.toString();
    }

    public LinkedList<XYLocation> getCandidates() {
        return candidates;
    }

    public void setCandidates(LinkedList<XYLocation> candidates) {
        this.candidates = candidates;
    }

    public Square[][] getBoard() {
        return board;
    }

    public void setBoard(Square[][] board) {
        this.board = board;
    }

    public XYLocation getLastMove() {

        return lastMove;
    }

    public void setLastMove(XYLocation lastMove) {
        this.lastMove = lastMove;
    }

    public void setPlayerToMove(String playerToMove) {
        this.playerToMove = playerToMove;
    }

    public String getPlayerToMove() {
        return playerToMove;
    }

    public boolean isEmpty(int col, int row) {
        return board[row][col].getLabel() == EMPTY;
    }

    public String getValue(int col, int row) {
        return board[row][col].getLabel().toString();
    }

    public double getUtility() {
        return utility;
    }

    public XYLocation getMiddle() {
        return middle;
    }

    public void setMiddle(XYLocation middle) {
        this.middle = middle;
    }

}
