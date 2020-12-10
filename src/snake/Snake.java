package snake;

import java.awt.EventQueue;
import javax.swing.JFrame;


public class Snake extends JFrame {

    public Snake() {

        add(new Board());
        
        setResizable(false);//blokuje możlwiosc zmiany rozmoaru okna
        pack();//ustawia rozmiar okna na maxymalny
        
        setTitle("Snake");//nazwa okna
        setLocationRelativeTo(null);//wyśrodkowanie okna na ekraniw
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//zamykanie okna przy wciscnieciu zamkniecia aplikacji
    }
    

    public static void main(String[] args) {
        
        EventQueue.invokeLater(new Runnable() {//tworzenie nowej instancji
            @Override
            public void run() {                
                JFrame ex = new Snake();
                ex.setVisible(true); //ustawia widoczność okna    
            }
        });
    }
}