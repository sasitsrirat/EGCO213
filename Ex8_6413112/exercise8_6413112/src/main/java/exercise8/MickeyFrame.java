/*
 * MR.SASIT SRIRAT 6413112
 * MR. 
 */

package exercise8;

import java.awt.*;
import java.awt.event.*;
import java.util.Random;

import javax.swing.*;
import javax.sound.sampled.*; // for sounds

public class MickeyFrame extends JFrame {
    // components
    private JPanel contentpane;
    private JLabel drawpane;
    private JComboBox combo;
    private JToggleButton[] tb;
    private ButtonGroup bgroup;
    private JButton walkButton, stopButton, moreButton;
    private JTextField scoreText;
    private MyImageIcon backgroundImg;
    private MySoundEffect themeSound;

    private MickeyLabel mickeyLabel;
    private MickeyFrame currentFrame;
    private ItemLabel itemLabel; //add

    // working variables - adjust the values as you want
    private int itemWidth  = 40,   itemHeight  = 50 ,itemSpeed   = 1000;
    private int frameWidth = 1200, frameHeight = 600;
    private int score;
    private boolean MickeyLeft = true, MickeyMove = false;

    public static void main(String[] args) {
        new MickeyFrame();
    }

    //////////////////////////////////////////////////////////////////////////
    public MickeyFrame() {
        setTitle("Mickey Mouse");
        setBounds(50, 50, frameWidth, frameHeight);
        setResizable(false);
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        currentFrame = this;

        // (1) Add WindowListener (anonymous class)
        // - Stop Mickey & music. Show total score

        addWindowListener(new WindowAdapter() {
        	public void windowClosing(WindowEvent e) {
        		themeSound.stop();
        		JOptionPane.showMessageDialog(new JFrame()," Total score = "+score , "Mickey Mouse",JOptionPane.INFORMATION_MESSAGE );
        	}
        });
        contentpane = (JPanel) getContentPane();
        contentpane.setLayout(new BorderLayout());
        AddComponents();
    }

    //////////////////////////////////////////////////////////////////////////
    public void AddComponents() {
        String path = "src/main/java/exercise8/resources/";

        backgroundImg = new MyImageIcon(path + "background.jpg").resize(frameWidth, frameHeight);
        drawpane = new JLabel();
        drawpane.setIcon(backgroundImg);
        drawpane.setLayout(null);

        themeSound = new MySoundEffect(path + "theme.wav");
        themeSound.playLoop();

        mickeyLabel = new MickeyLabel(currentFrame);
        drawpane.add(mickeyLabel);

        //add
        itemLabel = new ItemLabel(currentFrame);
        //drawpane.add(itemLabel);

        // (2) Add ActionListener (anonymous class) to walkButton
        // - If Mickey isn't waking, create mickeyThread to make him walk
        walkButton = new JButton("Walk");
        walkButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                //if (!MickeyMove)
                if(mickeyLabel.isWalk() == false)
                {
                    mickeyLabel.setWalk(true);
                    setMickeyThread();
                }   
            }
        });

        // (3) Add ActionListener (anonymous class) to stopButton
        // - Stop mickeyThread, i.e. make it return from method run
        stopButton = new JButton("Stop");
        stopButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                if (mickeyLabel.isWalk() == true)
                {
                    mickeyLabel.setWalk(false);
                    setMickeyThread();
                }   
            }
        });

        // (4) Add ItemListener (anonymouse class) to combo
        // - Set Mickey's speed, i.e. sleeping time for mickeyThread
        String[] speed = { "fast", "medium", "slow" };
        combo = new JComboBox(speed);
        combo.setSelectedIndex(1);
        combo.addItemListener(new ItemListener()
        {
            @Override
            public void itemStateChanged(ItemEvent e)
            {
                String Input = (String) e.getItem();
                if (Input.equals("fast"))
                {
                    mickeyLabel.setSpeed(100);
                }
                else if (Input.equals("medium"))
                {
                    mickeyLabel.setSpeed(1000);
                }
                else if (Input.equals("slow"))
                {
                    mickeyLabel.setSpeed(2000);
                }
            }
        });
        // (5) Add ItemListener (anonymouse class) to tb[i]
        // - Make Mickey turn left/right
        tb = new JToggleButton[2];
        bgroup = new ButtonGroup();
        tb[0] = new JRadioButton("Left");
        tb[0].setName("Left");
        tb[1] = new JRadioButton("Right");
        tb[1].setName("Right");
        tb[0].setSelected(true);

        for (int i = 0; i < 2; i++) {
            //bgroup.add(tb[i]);
        }

        tb[0].addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                JRadioButton inputSide = (JRadioButton) e.getItem();
                if(inputSide.isSelected()){
                    tb[0].setSelected(true); //LEFT
                    tb[1].setSelected(false); //RIGHT
                    mickeyLabel.turnLeft();
                } else {
                    if(!tb[1].isSelected()) {
                        MickeyLeft = true;
                        mickeyLabel.turnLeft();
                        tb[0].setSelected(true);
                    }
                }
            }
        });
        tb[1].addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                JRadioButton inputSide = (JRadioButton) e.getItem();
                if(inputSide.isSelected()){
                    tb[0].setSelected(false); //LEFT
                    tb[1].setSelected(true); //RIGHT
                    mickeyLabel.turnRight();
                } else {
                    if(!tb[0].isSelected()) {
                        mickeyLabel.turnRight();
                        MickeyLeft = false;
                        tb[1].setSelected(true);
                    }
                }
            }
        });

        // (6) Add ActionListener (anonymous class) to moreButton
        // - Create a new itemThread
        moreButton = new JButton("More item");
        moreButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setItemThread();
            }
        });
        scoreText = new JTextField("0", 5);
        scoreText.setEditable(false);

        JPanel control = new JPanel();
        control.setBounds(0, 0, 1000, 50);
        control.add(new JLabel("Mickey Control : "));
        control.add(walkButton);
        control.add(stopButton);
        control.add(combo);
        control.add(tb[0]);
        control.add(tb[1]);
        control.add(new JLabel("                 "));
        control.add(new JLabel("Item Control : "));
        control.add(moreButton);
        control.add(new JLabel("                 "));
        control.add(new JLabel("Score : "));
        control.add(scoreText);
        contentpane.add(control, BorderLayout.NORTH);
        contentpane.add(drawpane, BorderLayout.CENTER);
        validate();
    }

    ////////////////////////////////////////////////////////////////////////////
    public void setMickeyThread() {
        Thread mickeyThread = new Thread() {
            public void run() {
                while (mickeyLabel.isWalk()) {
                    mickeyLabel.updateLocation();
                }
            } // end run
        }; // end thread creation
        mickeyThread.start();
    }
   
    ////////////////////////////////////////////////////////////////////////////
    public void setItemThread() {
        Thread itemThread = new Thread() {
            int getScore;
            ItemLabel Label = new ItemLabel(currentFrame);
            public void run() {
                // (7) Create a new ItemLabel, add it to drawpane
                // - Keep updating its location
                // - Check whether it collides with Mickey. If it does,
                // play hit sound and update score
                // - Once reaching the bottom or colliding with Mickey,
                // remove the item from drawpane and end this threadz
                while(true)
                {
                drawpane.add(Label);
                Label.updateLocation();
                if(mickeyLabel.getBounds().intersects(Label.getBounds())) {
                    getScore = Label.getHitPoints();
                    Label.playHitSound();
                    updateScore(getScore);
                    drawpane.remove(Label);
                    repaint();
                    break;
                    }
                if(Label.getCurY()>=frameHeight-120){
                    drawpane.remove(Label);
                    repaint();
                    break;
                }
            }
            } // end run
        }; // end thread creation
        itemThread.start();
    }

    ////////////////////////////////////////////////////////////////////////////
    public void updateScore(int hp) {
        // (8) Score update must be synchronized since it can be done by >1 itemThreads
        score += hp;
        String s = Integer.toString(score);
        scoreText.setText(s);
    }
} // end outer class MickeyGame

////////////////////////////////////////////////////////////////////////////////
class MickeyLabel extends JLabel {
    private MyImageIcon leftImg, rightImg;
    private MickeyFrame parentFrame;

    private String[] imageFiles = { "src/main/java/Exercise8/resources/mickey_left.png",
            "src/main/java/Exercise8/resources/mickey_right.png" };

    // working variables - adjust the values as you want
    private int width = 200, height = 200;
    private int curX = 700, curY = 300;
    private int speed = 500;
    private boolean left = true, walk = false;

    public MickeyLabel(MickeyFrame pf) {
        parentFrame = pf;

        leftImg = new MyImageIcon(imageFiles[0]).resize(width, height);
        rightImg = new MyImageIcon(imageFiles[1]).resize(width, height);
        setIcon(leftImg);
        setBounds(curX, curY, width, height);
    }

    public void setSpeed(int s) {
        speed = s;
    }

    public void turnLeft() {
        setIcon(leftImg);
        left = true;
    }

    public void turnRight() {
        setIcon(rightImg);
        left = false;
    }

    public void setWalk(boolean w) {
        walk = w;
    }

    public boolean isWalk() {
        return walk;
    }

    public void updateLocation() {
        if (left) {
            curX = curX - 50;
            if (curX < -100) {
                curX = parentFrame.getWidth();
            }
        } else {
            curX = curX + 50;
            if (curX > parentFrame.getWidth() - 100) {
                curX = 0;
            }
        }
        setLocation(curX, curY);
        repaint();
        try {
            Thread.sleep(speed);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
} // end class MickeyLabel

class ItemLabel extends JLabel {
    private int type; // 0 = bad item, 1 = good item
    MyImageIcon itemImg; //original is public 
    private MySoundEffect hitSound;
    private MickeyFrame parentFrame;

    private String[] imageFiles = { "src/main/java/exercise8/resources/bomb.png",
            "src/main/java/exercise8/resources/burger.png" };

    private String[] soundFiles = { "src/main/java/exercise8/resources/punch.wav",
            "src/main/java/exercise8/resources/coin.wav" };

    // working variables - adjust the values as you want
    private int width = 50, height = 50;
    private int curX, curY = 0;
    private int speed = 1000;

    //Constructor
    public ItemLabel(MickeyFrame pf) {
        parentFrame = pf;
        curX = (int) (Math.random() * 5555) % (parentFrame.getWidth() - 100);
        if (curX % 2 == 0)
            type = 0;
        else
            type = 1;
        itemImg = new MyImageIcon(imageFiles[type]).resize(width, height);
        hitSound = new MySoundEffect(soundFiles[type]);
        setIcon(itemImg);
        setBounds(curX, curY, width, height);
    }

    public void playHitSound() {
        hitSound.playOnce();
    }

    public int getHitPoints() {
        if (type == 0)
            return -1;
        else
            return 1;
    }
    
    public int getCurY() {
        return curY;
    }

    public void updateLocation() {
        if (curY < 600) {
            curY = curY + 50;
        }
        setLocation(curX, curY);
        repaint();
        try {
            Thread.sleep(speed);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

} // end class ItemLabel

////////////////////////////////////////////////////////////////////////////////
// Auxiliary class to resize image
class MyImageIcon extends ImageIcon {
    public MyImageIcon(String fname) {
        super(fname);
    }

    public MyImageIcon(Image image) {
        super(image);
    }

    public MyImageIcon resize(int width, int height) {
        Image oldimg = this.getImage();
        Image newimg = oldimg.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
        return new MyImageIcon(newimg);
    }
};

// Auxiliary class to play sound effect (support .wav or .mid file)
class MySoundEffect {
    private Clip clip;

    public MySoundEffect(String filename) {
        try {
            java.io.File file = new java.io.File(filename);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
            clip = AudioSystem.getClip();
            clip.open(audioStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void playOnce() {
        clip.setMicrosecondPosition(0);
        clip.start();
    }

    public void playLoop() {
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stop() {
        clip.stop();
    }
}
