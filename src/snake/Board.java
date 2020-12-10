package snake;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JPanel;


public class Board extends JPanel implements ActionListener {

    private final int B_WIDTH = 300;//szerokosc okna
    private final int B_HEIGHT = 300;//wysokość okna
    private final int DOT_SIZE = 10;//rozmiar pola
    private final int ALL_DOTS = 900;//liczba pól
    private final int RAND_POS = 29;//liczba pól do losowania wspł. jabłka

    private final int x[] = new int[ALL_DOTS];//nowa tablica z maksymalną długością wartości ciała weża dla x i y
    private final int y[] = new int[ALL_DOTS];

    private int dots;//długośc ciała + głowa
    private int pdots;//poprzednia długość ciała
    private int apple_x;//wsp. x jabłka
    private int apple_y;//wsp. y jabłka
    private int inGame = 0;//stan gry, 0 = start, 1= w grze, 2 = przegrana

    private boolean leftDirection = false;//kierunek węża
    private boolean rightDirection = true;
    private boolean upDirection = false;
    private boolean downDirection = false;

    
    public javax.swing.Timer timer = new javax.swing.Timer(100, this);//czas dla węza
    private Image ball;//obraz ciała, jabłka i głowy
    private Image apple;
    private Image head;

    public Board() {

        addKeyListener(new TAdapter());//obsługa klawiszy
        setBackground(Color.black);//tło
        setFocusable(true);//ustawia okno jako aktywne
        

        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT+20));//tworzenie okna gry
        loadImages();//ładowanie obrazków
        initGame();//zainicjowanie gry
    }

    private void loadImages() { //funckja ladująca obrazy

        ImageIcon iid = new ImageIcon("src/images/dot.png");//ciało
        ball = iid.getImage();

        ImageIcon iia = new ImageIcon("src/images/apple.png");///jabłko
        apple = iia.getImage();

        ImageIcon iih = new ImageIcon("src/images/head.png");//głowa
        head = iih.getImage();
    }

    private void initGame() {
        
        pdots=dots;        
        dots = 2; //ilośc początkowa kulek, z których składa się ciało węża
        for (int z = 0; z < dots; z++) { //ustalanie współżędnych ciała węża
            x[z] = 50 - z * 10;
            y[z] = 10;
        }

        locateApple(); //tworzenie jabłka
        rightDirection = true;//ustawianie poczatkowego kierunku ruchu
        leftDirection = false;
        upDirection = false;
        downDirection = false;
        timer.start();//start timera
        
    }

    @Override
    public void paintComponent(Graphics g) {//wywoływanie metody paint component
        super.paintComponent(g);

        doDrawing(g);//aktualnie obrazy przekazywane tej funkcji
        
        
    }
    
    private void doDrawing(Graphics g) {
        
        switch (inGame) {
            case 1:
                g.drawImage(apple, apple_x, apple_y, this);//rysowanie jabłka na planszy
                drawScore(g);//rysowanie wyniku
                for (int z = 0; z < dots; z++) {
                    if (z == 0) {
                        g.drawImage(head, x[z], y[z], this);//rysowanie głowy i ciała węża
                    } else {
                        g.drawImage(ball, x[z], y[z], this);
                    }
                }   Toolkit.getDefaultToolkit().sync();//synchronizacja animacji
                break;
            case 0:
                showIntroScreen((Graphics2D) g);//ekran początkowy
                break;
            case 2:
                gameOver(g);//ekrań koncowy
                break;
            default:
                break;
        }
            
    }

    private void checkApple() {//sprawdza czy głowa węża najechała na jabłko

        if ((x[0] == apple_x) && (y[0] == apple_y)) {

            dots++;//jeżeli tak, dodaje 1 punkt ciała i tworzy nowe jabłko
            locateApple();
        }
    }

    private void move() {

        for (int z = dots; z > 0; z--) {//poruszanie się węza, koordy głowy to teraz koordy 1 segmentu ciała
            x[z] = x[(z - 1)];
            y[z] = y[(z - 1)];
        }

        if (leftDirection) {
            x[0] -= DOT_SIZE;//lewo wspł. głowy [x] - rozmiar głowy
        }

        if (rightDirection) { // prawo wspł. głowy [x] + rozmiar głowy
            x[0] += DOT_SIZE;
        }

        if (upDirection) {   // góra wspł. głowy [y] - rozmiar głowy
            y[0] -= DOT_SIZE;
        }

        if (downDirection) {  // dół wspł. głowy [y] + rozmiar głowy
            y[0] += DOT_SIZE;
        }
    }

    private void checkCollision() {

        for (int z = dots; z > 0; z--) { //dla każdego segmentu ciała sprawdza, czy głowa nie jest na ciele 

            if ( (x[0] == x[z]) && (y[0] == y[z])) {
                inGame = 2;
            }
        }

        if (y[0] >= B_HEIGHT) { //sprawdza czy wąż nie wyszedł poza pole gry
            inGame = 2;
        }

        if (y[0] < 0) {
            inGame = 2;
        }

        if (x[0] >= B_WIDTH) {
            inGame = 2;
        }

        if (x[0] < 0) {
            inGame = 2;
        }
        
        if(inGame!=1) { //w przypadku przegranej zatrzymuje zegrar
            timer.stop();
        }
    }

    private void locateApple() //tworzenie jabłka
    {
        boolean qwe=true;
        while(qwe) //dopóki nie wylosuje punktu, gdzie nie ma węza bedzie losowało nowe koordynaty dla jabłka
        {
            int r = (int) (Math.random() * RAND_POS);
            apple_x = ((r * DOT_SIZE));
            r = (int) (Math.random() * RAND_POS);
            apple_y = ((r * DOT_SIZE));
            int z=dots-1;
            for(int qw=z;qw>=0;qw--)
            {
                qwe=false;
                if( (apple_x==x[z]) && (apple_y==y[z]) )
                {
                    qwe=true;
                    qw=-1;  
                }
                z--;
            }
        }   
    }

    @Override
    public void actionPerformed(ActionEvent e) { //obsługa akcji, która rusza po koncu cyklu zegara timer
        

        if (inGame == 1) {

            checkApple(); //sprawdza czy waż zjadł jabłko
            checkCollision();//sprawdza kolizje
            if(inGame==2)
            {
                initGame();//jezeli nastąpiła kolizja ładuje gre od nowa
            }
                move(); //funkcja ruchu
        }       
            repaint();//rysowanie
    }

    private class TAdapter extends KeyAdapter {//obsługa klawiszy

        @Override
        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode();
            
            if(inGame==1)//jezeli gra trwa
            {
                if ((key == KeyEvent.VK_LEFT) && (!rightDirection)) {//jeżeli wąż ma skręcić w lewo, nie może poruszac sie w prawo. nanlogicznie dla pozostałych kierunków
                    leftDirection = true;
                    upDirection = false;
                    downDirection = false;
                }

                if ((key == KeyEvent.VK_RIGHT) && (!leftDirection)) {
                    rightDirection = true;
                    upDirection = false;
                    downDirection = false;
                }

                if ((key == KeyEvent.VK_UP) && (!downDirection)) {
                    upDirection = true;
                    rightDirection = false;
                    leftDirection = false;
                }

                if ((key == KeyEvent.VK_DOWN) && (!upDirection)) {
                    downDirection = true;
                    rightDirection = false;
                    leftDirection = false;
                }
            }
            else
            {
                if (key == 'p' || key == 'P') {//uruchomienie nowej gry
                    inGame = 1;
                    initGame();
                   
                }
            }
        }
    }
    
    private void drawScore(Graphics g) { //rysowanie wyniku na dole planszy

        String s;

        g.setColor(new Color(96, 128, 255));//kolor
        s = "Score: " + (dots-2);//tekst
        g.drawString(s, B_WIDTH-60, B_HEIGHT+15 );//miejsce wyświetlania tekstu
        g.drawRect(0 , B_WIDTH, B_HEIGHT, 50);//ramka na tekst
        
    }
    private void showIntroScreen(Graphics2D g2d) { //ekron początkowy

        g2d.setColor(new Color(0, 32, 48));//kolor
        g2d.fillRect(50, B_WIDTH / 2 - 30, B_WIDTH - 100, 50);// ramka
        g2d.setColor(Color.white);//kolor 
        g2d.drawRect(50, B_WIDTH / 2 - 30, B_WIDTH - 100, 50);//wypełnienie ramki

        String s = "Naciśnij p aby grać";//wyświetlany tekst
        Font small = new Font("Helvetica", Font.BOLD, 14);//czcionka
        FontMetrics metr = this.getFontMetrics(small);

        g2d.setColor(Color.white);//kolor
        g2d.setFont(small);
        g2d.drawString(s, (B_WIDTH - metr.stringWidth(s)) / 2, B_WIDTH / 2);//wyświetlanie tekstu
    }
    private void gameOver(Graphics g2d) {
        
        String s = "Game Over";//tekst
        String s1 = "zebranych jabłek: "+(pdots-2);
        String s2 = "Wciśnij p aby zagrać ponownie";
        
        
        g2d.setColor(new Color(0, 32, 48));//kolor
        g2d.fillRect(20, B_WIDTH / 2 - 50, B_WIDTH - 40, 90);//wypełnienie
        g2d.setColor(Color.white);//kolor
        g2d.drawRect(20, B_WIDTH / 2 - 50, B_WIDTH - 40, 90);//ramka

        Font small = new Font("Helvetica", Font.BOLD, 14);//czcionka
        FontMetrics metr = this.getFontMetrics(small);

        g2d.setColor(Color.white);
        g2d.setFont(small);
        g2d.drawString(s, (B_WIDTH - metr.stringWidth(s)) / 2, B_WIDTH / 2-20);//wyswietlanie tekstu
        g2d.drawString(s1, (B_WIDTH - metr.stringWidth(s)-50) / 2, B_WIDTH / 2 );
        g2d.drawString(s2, (B_WIDTH - metr.stringWidth(s)-135) / 2, B_WIDTH / 2 + 20);
    }
}