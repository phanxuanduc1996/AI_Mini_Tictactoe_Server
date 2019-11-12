package com.codelovers.AI;

import java.util.List;
import java.util.ListIterator;

import com.codelovers.model.caro.XYLocation;
import com.codelovers.service.game.CaroGame;
import com.codelovers.service.game.CaroState;
/**
 *
 @trungson
 */
public class AlphaBeta {

    public AlphaBeta() {
    }

    public static XYLocation makeDecision(CaroState state, int deep) {
        
        XYLocation result = null;
        double resultValue = Double.NEGATIVE_INFINITY;
        String player = CaroGame.getPlayer(state);
        long t1 = System.currentTimeMillis(); // currentTimeMillis: Trả về khoảng thời gian bằng mili giây tính từ ngày 1-1-1971 cho tới thời điểm hiện tại.
        List<XYLocation> actionList = CaroGame.getActions(state);
        ListIterator<XYLocation> iterator = actionList.listIterator();
        if (iterator.hasNext() == false) {
            System.out.println("Action Null!");
            return result;
        }

        result = iterator.next();
        CaroState afterResultActionState = CaroGame.getResult(state,result);
        resultValue= AlphaBeta.minValue(afterResultActionState, player,
                    Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, deep-1);
        int candidteNumber=0;
        while (iterator.hasNext() && candidteNumber<=CaroGame.candidateInspectNumber) {
            candidteNumber++;
            XYLocation action = iterator.next();

            CaroState afterActionState = CaroGame.getResult(state, action);
            double value = AlphaBeta.minValue(afterActionState, player,
                    Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, deep-1);
            if (value > resultValue) {
                result = action;
                resultValue = value;
            }
        }
        System.out.println("Times search: " + (System.currentTimeMillis() - t1));
        return result;
    }

    public static double maxValue(CaroState state, String player, double alpha, double beta, int deep) {
        if (CaroGame.isTerminal(state) || deep == 0 ) {
            return state.evaluateBoard();
        }
        double value = Double.NEGATIVE_INFINITY;
        List<XYLocation> actionList = CaroGame.getActions(state);
        ListIterator<XYLocation> iterator = actionList.listIterator();
        int candidteNumber=0;
//        while (iterator.hasNext() ) {
        while (iterator.hasNext()&& candidteNumber<=CaroGame.candidateInspectNumber) {
            candidteNumber++;
            if (iterator == null) {
                System.out.println("NULL");
            }
            XYLocation action = iterator.next();
            if (action == state.getMiddle()) {
                break;
            }
            CaroState afterActionState = CaroGame.getResult(state, action);
            value = Math.max(value, minValue(
                    afterActionState, player, alpha, beta, deep - 1));
//            System.out.println("value"+value);

            if (value >= beta) {
                return value;
            }
            alpha = Math.max(alpha, value);
        }
        return value;
    }

    public static double minValue(CaroState state, String player, double alpha, double beta, int deep) {
        if (CaroGame.isTerminal(state) || deep == 0 ) {
            return -1D*state.evaluateBoard();
        }
        double value = Double.POSITIVE_INFINITY;
        List<XYLocation> actionList = CaroGame.getActions(state);
        ListIterator<XYLocation> iterator = actionList.listIterator();
        int candidteNumber=0;
//        while (iterator.hasNext() ) {
        while (iterator.hasNext()&& candidteNumber<=CaroGame.candidateInspectNumber) {
            candidteNumber++;
            XYLocation action = iterator.next();
            if (action == state.getMiddle()) {
                break;
            }
            value = Math.min(value, maxValue(
                    CaroGame.getResult(state, action), player, alpha, beta, deep - 1));
            if (value <= alpha) {
                return value;
            }
            beta = Math.min(beta, value);
        }
        return value;
    }

}
