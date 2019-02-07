package snakepackage;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.*;

import enums.GridSize;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author jd-
 *
 */
public class SnakeApp {

    private static SnakeApp app;
    public static final int MAX_THREADS = 8;
    Snake[] snakes = new Snake[MAX_THREADS];
    private static final Cell[] spawn = {
        new Cell(1, (GridSize.GRID_HEIGHT / 2) / 2),
        new Cell(GridSize.GRID_WIDTH - 2,
        3 * (GridSize.GRID_HEIGHT / 2) / 2),
        new Cell(3 * (GridSize.GRID_WIDTH / 2) / 2, 1),
        new Cell((GridSize.GRID_WIDTH / 2) / 2, GridSize.GRID_HEIGHT - 2),
        new Cell(1, 3 * (GridSize.GRID_HEIGHT / 2) / 2),
        new Cell(GridSize.GRID_WIDTH - 2, (GridSize.GRID_HEIGHT / 2) / 2),
        new Cell((GridSize.GRID_WIDTH / 2) / 2, 1),
        new Cell(3 * (GridSize.GRID_WIDTH / 2) / 2,
        GridSize.GRID_HEIGHT - 2)};
    private JFrame frame;
    private static Board board;
    int nr_selected = 0;
    Thread[] thread = new Thread[MAX_THREADS];
    private int terminar = 0;

    public SnakeApp() {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        frame = new JFrame("The Snake Race");
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // frame.setSize(618, 640);
        frame.setSize(GridSize.GRID_WIDTH * GridSize.WIDTH_BOX + 17,
                GridSize.GRID_HEIGHT * GridSize.HEIGH_BOX + 40);
        frame.setLocation(dimension.width / 2 - frame.getWidth() / 2,
                dimension.height / 2 - frame.getHeight() / 2);
        board = new Board();


        frame.add(board,BorderLayout.CENTER);
        JMenuBar mb=new JMenuBar();
        frame.setJMenuBar(mb);
        JMenu menu1=new JMenu("Opciones");
        mb.add(menu1);
        JMenuItem mi1=new JMenuItem("Start");
        mi1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resume();
            }
        });
        menu1.add(mi1);
        JMenuItem mi2=new JMenuItem("Pause");
        mi2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pause();
            }

            
        });
        menu1.add(mi2);
        JMenuItem mi3=new JMenuItem("Resume");
        mi3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resume();
            }
        });
        menu1.add(mi3);
        JPanel actionsBPabel=new JPanel();
        actionsBPabel.setLayout(new FlowLayout());
        actionsBPabel.add(new JButton("Action "));
        frame.add(actionsBPabel,BorderLayout.SOUTH);

    }

    private synchronized void resume() {
        for(int i = 0; i<snakes.length;i++){
            snakes[i].resume();
        }
    }

    private synchronized  void pause() {
        for(int i = 0; i<snakes.length;i++){
            snakes[i].pause();
        }
        Snake bestSnake = snakes[0];
        int best = 0;
        Snake worstSnake = null;
        int worst = 0;
        for(int i = 0; i<snakes.length;i++){
            if(snakes[i].getGrowing()>bestSnake.getGrowing()){
                bestSnake = snakes[i];
                best = i;
            }
            if(snakes[i].isSnakeEnd().get()){
                worstSnake = snakes[i];
                worst = i;
            }
        }
        System.out.println("Mejor Serpiente:" + bestSnake + " Numero: " + best);
        if(worstSnake != null){
            System.out.println("Peor Serpiente:" + worstSnake + " Numero: " + worst);
        }
    }

    public static void main(String[] args) {
        app = new SnakeApp();
        app.init();
    }

    private void init() {
        
        
        
        for (int i = 0; i != MAX_THREADS; i++) {
            
            snakes[i] = new Snake(i + 1, spawn[i], i + 1);
            snakes[i].addObserver(board);
            thread[i] = new Thread(snakes[i]);
            thread[i].start();
        }

        frame.setVisible(true);


        //ESTE CICLO ES INNECESARIO
        /**while (true) {
            int x = 0;
            for (int i = 0; i != MAX_THREADS; i++) {
                if (snakes[i].isSnakeEnd() == true) {
                    x++;
                }
            }
            if (x == MAX_THREADS) {
                break;
            }
        }**/
        //El CICLO SE CAMBIA POR ESTA ESPERA
        synchronized (this){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Thread (snake) status:");
        for (int i = 0; i != MAX_THREADS; i++) {
            System.out.println("["+i+"] :"+thread[i].getState());
        }
        

    }

    public static SnakeApp getApp() {
        return app;
    }

    public synchronized void snakeDead(){
        terminar++;
        if(terminar==MAX_THREADS){
            notify();
        }
    }

}
