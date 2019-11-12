package com.codelovers.service.game;

import com.codelovers.AI.AlphaBeta;
import com.codelovers.model.caro.XYLocation;

import java.util.LinkedList;
/**
 *
 @trungson
 */
public class CaroGame {

    private static CaroState initialState = new CaroState(null);
    public static final int COLUMN = 20;
    public static final int ROW = 20;
    public static final int RADIUS=2;
    public static final double LIMITED_FIRST=0D;
    public static final double FIRST=35D;
    public static final double LIMITED_SECOND=1D;
    public static final double SECOND=61D; 
    public static final double LIMITED_THIRD=40D;
    public static final double THIRD=100D;
    public static final double LIMITED_FOURTH=120D;// third+ limited_fourth > third*2
    public static final double FOURTH=200D; //= 2*THIRD
    public static final double LIMITED_FIFTH=1000D;
    public static final double FIFTH=1000D;
    public static final double DISTANCE_DIVIDE_COEFFICIENT_=3.1D;
    public static final int candidateInspectNumber=10; // Do rong 
    public static XYLocation action;

    public static CaroState AIAgentMove(CaroState currState) {
        
        action = AlphaBeta.makeDecision(currState,8);  // Do sau
        if (action == null) {
            System.out.println("Null Action");
        } else {
            //BoardFrame.display(currState, "O", action.getY() * CaroGame.COLUMN + action.getX());
            currState = CaroGame.getResult(currState, action);
            System.out.println("AIaction(" + action.getX() + " , " + action.getY());
        }

        return currState;

    }

    public static CaroState getInitialState() {
        return initialState;
    }

    public static String[] getPlayers() {
        return new String[]{CaroState.X, CaroState.O};
    }

    public static String getPlayer(CaroState state) {
        return state.getPlayerToMove();
    }

    public static LinkedList<XYLocation> getActions(CaroState state) {
        return state.getUnMarkedPositions();
    }

    public static CaroState getResult(CaroState state, XYLocation action) {
        CaroState result = state.clone();
        result.setLastMove(action);
        result.mark(action);
        
        return result;
    }

    public static boolean isTerminal(CaroState state) {
        return state.getUtility() != -1;
    }

    public static double getUtility(CaroState state, String player) {
        double result = state.getUtility();
        if (result != -1) {
            if (player == CaroState.O) {
                result = 1 - result;
            }
        } else {
            throw new IllegalArgumentException("State is not terminal.");
        }
        return result;
    }
}
