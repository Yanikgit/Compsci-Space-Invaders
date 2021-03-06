import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Menu {
    public static final int WIDTH = 600; //width of canvas in pixels
    public static final int HEIGHT = 600; //height of canvas in pixels
    public static final int X_SCALE = 10; //dividing canvas up horizontally (helps with placement of drawings)
    public static final int Y_SCALE = 10; //dividing canvas up vertically (helps with placement of drawings)

    //font sizes used throughout different text displays
    public static final Font font1 = new Font("Serif", Font.BOLD, 50);
    public static final Font font2 = new Font("Serif", Font.PLAIN, 30);
    public static final Font font3 = new Font("Serif", Font.BOLD, 35);

    public void createGame() { //ccalls methods to create canvas and then draw the main menu of the game
        createCanvas();
        startMenu();
    }

    public void createCanvas() { //creates canvas for game
        StdDraw.setCanvasSize(WIDTH, HEIGHT);
        StdDraw.setXscale(0, X_SCALE);
        StdDraw.setYscale(0, Y_SCALE);
    }

    public void startMenu() { //displays menu options and instructions for player; game is started after respective key stroke

        StdDraw.clear(Color.RED);

        StdDraw.setPenColor(StdDraw.BLACK);

        StdDraw.setFont(font1);
        StdDraw.text(5, 9.5, "Zombie Zapper");

        StdDraw.setFont(font2);
        StdDraw.text(5, 8.7, "Press Enter to enter at own risk...");

        Font font3 = new Font("Serif", Font.BOLD, 35);
        StdDraw.setFont(font3);
        StdDraw.text(5, 6.5, "Survival Guide:");

        StdDraw.setFont(font2);
        StdDraw.text(5, 5.5, "Shoot(Space)");
        StdDraw.text(5, 4.5, "Rotate: Left(a), Right(d)");
        StdDraw.text(5, 3.5, "Move: left(left arrow), right(right arrow)");

        StdDraw.setFont(font3);
        StdDraw.text(5, 1.9, "To exit the mission press 'q'");
        StdDraw.text(5, 1.2, "To view the top 5 scores press 'l'");
        StdDraw.text(5, 0.5, "To restart the game press 'r'");

        StdDraw.show(); //Displays drawings that are "stored" in buffer waiting for the show() command

        StdAudio.loopInBackground("images/heartbeatNoise.wav");
        StdAudio.loopInBackground("images/apocalypseSuspense.wav");
        Game game = new Game();

        while ((!StdDraw.isKeyPressed(10)) && (!StdDraw.isKeyPressed(81)) && (!StdDraw.isKeyPressed(76))) { //while enter(32) or "q"(81) or "l"(108) has not been pressed, start menu screen must remain same
            while (!StdDraw.hasNextKeyTyped()) {
            } // loop until a key is pressed
        }


        if (StdDraw.isKeyPressed(10)) { // 10 = ascii for Enter
            game.newGame();

            StdDraw.clear(StdDraw.BLACK); //this line of code is only read when player has used all 3 lives and game is finished
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.setFont(font3);
            StdDraw.text(5, 5, "GAME OVER");

            StdDraw.show();

            try {               // allows for "game over" display to show for 2 seconds before going back to main menu
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            startMenu();
        } else if (StdDraw.isKeyPressed(81)) { // 81 = ascii for "q"
            quit();
        } else if (StdDraw.isKeyPressed(76)) { // 76 = ascii for "l"
            leaderboard();
        }

    }

    public void quit() { //displays "quit" display when player wishes to exit the game
        // also asks player if they are sure that they want to leave the game
        StdDraw.clear(StdDraw.BLACK);
        StdDraw.setPenColor(StdDraw.WHITE);

        StdDraw.setFont(font2);

        StdDraw.text(5, 6, "Are you sure you want to quit?");
        StdDraw.text(3.5, 5.0, "Yes: Enter");
        StdDraw.text(6.5, 5.0, "No: 'n'");

        StdDraw.show();
        while (!StdDraw.isKeyPressed(10) && !StdDraw.isKeyPressed(78)) {
            while (!StdDraw.hasNextKeyTyped()) {
            } // loop until a key is pressed
        }

        if (StdDraw.isKeyPressed(10)) { //program terminated; game exited
            System.out.println("TERMINATING:  " + (int) StdDraw.nextKeyTyped());
            System.exit(0);

        } else if (StdDraw.isKeyPressed(78)) { //taken back to main menu; 110 = ascii for "n"
            startMenu();
        }
    }

    public ArrayList<Integer> displayLeaderboard() { //reads file and returns contents of file (scores) as an array list of integer scores
        ArrayList<Integer> scores = new ArrayList<Integer>();
        File scoreText = new File("HighScore.txt");
        Scanner scanner;

        if (!scoreText.exists()) {
            //System.out.println(scores);
            return scores;
        }

        try {
            scanner = new Scanner(scoreText);

            while (scanner.hasNext()) {
                scores.add(scanner.nextInt());
            }

            Collections.sort(scores); //sorts contents of array list in ascending order
            Collections.reverse(scores); //reverses the order of the now sorted array list
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return scores;
    }

    public void leaderboard() { //displays top 5 scores in array list
        StdDraw.clear();

        StdDraw.setFont(font2);
        StdDraw.text(5, 9.5, "TOP 5 SCORES:");

        StdDraw.setFont();
        StdDraw.text(5, 9.0, "To go back to main menu press 'b'");

        ArrayList<Integer> topScores = new ArrayList<Integer>(displayLeaderboard());
        if (topScores.size() < 5) {
            for (int i = 0; i < topScores.size(); i++) {
                StdDraw.text(5, (8.5 - (0.5 * i)), String.valueOf(topScores.get(i)));
            }
        } else {
            for (int i = 0; i < 5; i++) {
                StdDraw.text(5, (8.5 - (0.5 * i)), String.valueOf(topScores.get(i)));
            }
        }
        StdDraw.show();

        while (!StdDraw.isKeyPressed(66)) { //66 = ascii for 'b'
            while (!StdDraw.hasNextKeyTyped()) {
            }
        }

        createGame();
    }

    public void livesDecrease(int lives) { //display for lives decrease
        StdDraw.clear(StdDraw.BLACK);

        StdDraw.setFont(font1);
        StdDraw.setPenColor(StdDraw.RED);

        StdDraw.text(5, 5, "LIVES: " + lives);

        StdDraw.show();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void nextlevel(int level) { //display for next level
        StdDraw.clear(Color.BLACK);

        StdDraw.setFont(font1);
        StdDraw.setPenColor(StdDraw.GREEN);

        StdDraw.text(5, 5, "LEVEL: " + level);

        StdDraw.show();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
