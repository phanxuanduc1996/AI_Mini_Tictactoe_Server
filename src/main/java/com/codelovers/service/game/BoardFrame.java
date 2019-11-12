
package com.codelovers.service.game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import java.util.ListIterator;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import com.codelovers.model.caro.XYLocation;

/**
 *
 @trungson
 */
public class BoardFrame extends JPanel implements
        ActionListener {

    private static final long serialVersionUID = 1L;
    JComboBox<String> strategyCombo;
    JButton clearButton;
    JButton fistPlayerOButton;
    static JButton[] squares;
    static JLabel statusBar;
    static JLabel co;
    static JLabel[] label;

    private static long t1;
    private static long t2;
    private static int nuoc = 0;
    
    private CaroState currState;

    {
        this.setLayout(new BorderLayout());
        JToolBar tbar = new JToolBar();
        tbar.setFloatable(true);
        
        co = new JLabel();
        co.setText("Game caro");
        tbar.add(co);
        
        tbar.add(Box.createHorizontalGlue());
        
        clearButton = new JButton("Clear");
        clearButton.addActionListener(this);
        tbar.add(clearButton);
        
        fistPlayerOButton = new JButton("AIAgent Move");
        fistPlayerOButton.addActionListener(this);
        tbar.add(fistPlayerOButton);

        add(tbar, BorderLayout.NORTH);
        
        JPanel spanel = new JPanel();
        spanel.setLayout(new GridLayout(CaroGame.COLUMN, CaroGame.ROW));
        add(spanel, BorderLayout.CENTER);
        
        JPanel spanel2 = new JPanel();
        spanel2.setLayout(new GridLayout(40,1));
        label = new JLabel[40];
        for (int i = 0; i < 40; i++) {
            if (i==0) {
                label[i] = new JLabel("                           ");
                spanel2.add(label[i]);
            } else {
                label[i] = new JLabel();
                spanel2.add(label[i]);
            }
        }
        
        add(spanel2, BorderLayout.EAST);
        
        squares = new JButton[CaroGame.COLUMN * CaroGame.ROW];
//        Font f = new java.awt.Font(Font.SANS_SERIF, Font.PLAIN, 32);
        for (int i = 0; i < CaroGame.COLUMN * CaroGame.ROW; i++) {
            JButton square = new JButton("");
//            square.setFont(f);
            square.setBackground(Color.WHITE);
            square.addActionListener(this);
            squares[i] = square;
            squares[i].setText(" ");
            spanel.add(square);
        }
        
        statusBar = new JLabel(" ");
        statusBar.setBorder(BorderFactory.createEtchedBorder());
        add(statusBar, BorderLayout.SOUTH);

        actionPerformed(null);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae == null || ae.getSource() == clearButton) {
            currState = CaroGame.getInitialState();
            display(currState);
            nuoc = 0;
            for (int i = 1; i < 40; i++) {
                label[i].setText("");
            }
        } else if (!CaroGame.isTerminal(currState)) {

            for (int i = 0; i < CaroGame.COLUMN * CaroGame.ROW; i++) {
                if (ae.getSource() == squares[i]) {
                    if(currState.getSquareOfBoard(i % CaroGame.COLUMN, i / CaroGame.COLUMN).getLabel() != CaroState.EMPTY ){
                        return;
                    }
                    currState = CaroGame.getResult(currState, new XYLocation(i % CaroGame.COLUMN, i / CaroGame.COLUMN));
                    display(currState);
                    
                    System.out.println("Player\n" + currState.toString1());
                    System.out.println("BoardScore:::=" + currState.getBoardScore());
                    System.out.println("Candidates:");
                    System.out.println("" + currState.getCandidates());


                    List<XYLocation> candidates = currState.getCandidates();
                    
                    ListIterator iterator = candidates.listIterator();
                    while (iterator.hasNext()) {
                        XYLocation candidate = (XYLocation) iterator.next();
                        int col = candidate.getX();
                        int row = candidate.getY();
                        System.out.printf("!" + col + "!" + row + "!" + currState.getSquareOfBoard(col, row).getScore() + " ; ");
                    }
                    System.out.println("");

                    for (int row = 0; row < CaroGame.ROW; row++) {
                        for (int j = 0; j < CaroGame.COLUMN; j++) {
                            double tmp = currState.getSquareOfBoard(j, row).getScore();
                            System.out.printf("%7.2f", currState.getSquareOfBoard(j, row).getScore());
                            System.out.print(";");
                        }
                        System.out.println("");
                    }
                    updateStatus(currState);

                    break;
                }
            }

            if (CaroGame.isTerminal(currState)) {
                System.out.println("Player WIN !");
            } else {
                currState = CaroGame.AIAgentMove(currState);
                System.out.println("AI\n" + currState.toString1());
                System.out.println("BoardScore:::=" + currState.getBoardScore());
                System.out.println("Candidates:");
                System.out.println("" + currState.getCandidates());
                List<XYLocation> candidates = currState.getCandidates();
                ListIterator iterator = candidates.listIterator();
                while (iterator.hasNext()) {
                    XYLocation candidate = (XYLocation) iterator.next();
                    int col = candidate.getX();
                    int row = candidate.getY();
                    System.out.printf("!" + col + "!" + row + "!" + currState.getSquareOfBoard(col, row).getScore() + " ; ");
                }
                System.out.println("");

                double sum = 0D;
                for (int i = 0; i < CaroGame.ROW; i++) {
                    for (int j = 0; j < CaroGame.COLUMN; j++) {
                        double tmp = currState.getSquareOfBoard(j, i).getScore();
                        System.out.printf("%7.2f", currState.getSquareOfBoard(j, i).getScore());
                        System.out.print(";");
                        sum += tmp;
                    }
                    System.out.println("");
                }

                if (CaroGame.isTerminal(currState)) {
                    System.out.println("AI WIN !");
                }
                updateStatus(currState);
            }

        }

//        display(currState);
    }

    public static void display(CaroState currState, String Agent, int position) {
        t2 = System.currentTimeMillis();
            nuoc++;
            label[nuoc].setText(" AI"+nuoc+" : "+(t2-t1)+"ms");
        if (Agent.equals("X")) {
            squares[position].setText(null);
            squares[position].setBorder(new javax.swing.border.LineBorder(new Color(255, 51, 51), 3, true));
            setPicture(squares[position],"xred.png");
            
        } else {
            squares[position].setText(null);
            squares[position].setBorder(new javax.swing.border.LineBorder(new Color(102,102,255), 2, true));
            setPicture(squares[position],"oblack.png");
            
        }
    }

    public static void display(CaroState currState) {
        for (int i = 0; i < CaroGame.COLUMN * CaroGame.ROW; i++) {
            String val = currState.getValue(i % CaroGame.COLUMN, i / CaroGame.COLUMN);
            if (val == CaroState.EMPTY) {
                val = " ";
            }
            if (val.equals("X")) {
                squares[i].setText(null);
                squares[i].setBorder(BorderFactory.createLineBorder(new Color(153, 153, 153)));
                setPicture(squares[i],"xred.png");
                
            }
            if (val.equals("O")) {
                squares[i].setText(null);
                squares[i].setBorder(BorderFactory.createLineBorder(new Color(153, 153, 153)));
                setPicture(squares[i],"oblack.png");
            }
            if (val.equals(" ")) {
                squares[i].setText(null);
                squares[i].setBorder(BorderFactory.createLineBorder(new Color(153, 153, 153)));
                squares[i].setIcon(null);
            }
            t1 = System.currentTimeMillis();
//            squares[i].setText(val);
//            squares[i].setText(val);
        }
    }
    
    private static void updateStatus(CaroState currState) {
        String statusText;
        if (CaroGame.isTerminal(currState)) {
            if (CaroGame.getUtility(currState, CaroState.X) == 1) {
                statusText = "         X has won :-)";
            } else if (CaroGame.getUtility(currState, CaroState.O) == 1) {
                statusText = "         O has won :-)";
            } else {
                statusText = "         No winner...";
            }
        } else {
            statusText = "         Next move: " + CaroGame.getPlayer(currState);
        }
        co.setFont(new Font("Tahoma", 1, 18));
        co.setText(statusText);
    }
    
    public static void setPicture(JButton button,String filename){
        try {
            BufferedImage image=ImageIO.read(new File(filename));
            int x=button.getSize().width;
            int y=button.getSize().height;
            int ix=image.getWidth();
            int iy=image.getHeight();
            int dx=0;
            int dy=0;
            if(x/y>ix/iy){ dy=y; dx=dy*ix/iy; }
            else{ dx=x; dy=dx*iy/ix; }
            ImageIcon icon=new ImageIcon(image.getScaledInstance(dx, dy, image.SCALE_SMOOTH));
            button.setIcon(icon);
        } catch (Exception e) {}
    }
}
