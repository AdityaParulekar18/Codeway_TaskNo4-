import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

class QuizQuestion 
{
    String text;
    ArrayList<String> options;
    char correctAnswer;

    public QuizQuestion(String text, ArrayList<String> options, char correctAnswer) 
    {
        this.text = text;
        this.options = options;
        this.correctAnswer = correctAnswer;
    }
}

public class QuizApp extends JFrame 
{
    private ArrayList<QuizQuestion> questions;
    private int score;
    private int currentQuestionIndex;
    private Timer questionTimer;

    private JLabel questionLabel;
    private JRadioButton optionA, optionB, optionC, optionD;
    private JButton submitButton;
    private JLabel timerLabel;

    public QuizApp(ArrayList<QuizQuestion> questions)
     {
        this.questions = questions;
        this.score = 0;
        this.currentQuestionIndex = 0;

        setTitle("Quiz App");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initializeComponents();
        setLayout(new BorderLayout());
        addComponents();
        displayQuestion();

        setVisible(true);
    }

    private void initializeComponents() 
      {
        questionLabel = new JLabel();
        optionA = new JRadioButton("A");
        optionB = new JRadioButton("B");
        optionC = new JRadioButton("C");
        optionD = new JRadioButton("D");
        submitButton = new JButton("Submit");
        timerLabel = new JLabel();

        ButtonGroup optionsGroup = new ButtonGroup();
        optionsGroup.add(optionA);
        optionsGroup.add(optionB);
        optionsGroup.add(optionC);
        optionsGroup.add(optionD);

        submitButton.addActionListener(new ActionListener() 
     {
            @Override
            public void actionPerformed(ActionEvent e) 
           {
                submitAnswer();
            }
        });

        questionTimer = new Timer();
    }

    private void addComponents() 
   {
        JPanel centerPanel = new JPanel(new GridLayout(6, 1));
        centerPanel.add(questionLabel);
        centerPanel.add(optionA);
        centerPanel.add(optionB);
        centerPanel.add(optionC);
        centerPanel.add(optionD);
        centerPanel.add(timerLabel);

        JPanel southPanel = new JPanel();
        southPanel.add(submitButton);

        add(centerPanel, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);
    }

    private void displayQuestion() 
      {
        clearSelection();
        updateTimerLabel(15);
        submitButton.setEnabled(true);

        QuizQuestion currentQuestion = questions.get(currentQuestionIndex);
        questionLabel.setText(currentQuestion.text);
        optionA.setText(currentQuestion.options.get(0));
        optionB.setText(currentQuestion.options.get(1));
        optionC.setText(currentQuestion.options.get(2));
        optionD.setText(currentQuestion.options.get(3));

        startTimer(15); 
    }

    private void submitAnswer() {
        questionTimer.cancel();
        submitButton.setEnabled(false); 

        QuizQuestion currentQuestion = questions.get(currentQuestionIndex);
        char correctAnswer = currentQuestion.correctAnswer;

        if (optionA.isSelected() && 'A' == correctAnswer ||
                optionB.isSelected() && 'B' == correctAnswer ||
                optionC.isSelected() && 'C' == correctAnswer ||
                optionD.isSelected() && 'D' == correctAnswer) {
            JOptionPane.showMessageDialog(this, "Correct!");
            score++;
        } 
        else {
            JOptionPane.showMessageDialog(this, "Incorrect! The correct answer is " + correctAnswer);
        }

        currentQuestionIndex++;

        if (currentQuestionIndex < questions.size()) {
            displayQuestion();
        } 
         else {
            displayResult();
        }
    }

    private void displayResult() 
       {
        JOptionPane.showMessageDialog(this, "Quiz Completed!\nYour score: " + score + "/" + questions.size());
        System.exit(0);
    }

    private void clearSelection() 
    {
        optionA.setSelected(false);
        optionB.setSelected(false);
        optionC.setSelected(false);
        optionD.setSelected(false);
    }

    private void startTimer(int timeLimit) 
     {
        questionTimer = new Timer();
        questionTimer.scheduleAtFixedRate(new TimerTask() {
            int remainingTime = timeLimit;

            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> {
                    updateTimerLabel(remainingTime);
                    remainingTime--;

                    if (remainingTime < 0) {
                        questionTimer.cancel(); 
                        submitAnswer();
                        JOptionPane.showMessageDialog(QuizApp.this, "Time's up! The correct answer is " +
                                questions.get(currentQuestionIndex).correctAnswer);
                        currentQuestionIndex++;

                        if (currentQuestionIndex < questions.size()) {
                            displayQuestion();
                        } else {
                            displayResult();
                        }
                    }
                });
            }
        }, 0, TimeUnit.SECONDS.toMillis(1));
    }

    private void updateTimerLabel(int remainingTime) 
     {
        timerLabel.setText("Time remaining: " + remainingTime + " seconds");
    }

    public static void main(String[] args) 
     {
        ArrayList<QuizQuestion> quizQuestions = new ArrayList<>();
        quizQuestions.add(new QuizQuestion("What is the capital of France?", new ArrayList<>(List.of("A) Paris", "B) London", "C) Berlin", "D) Madrid")), 'A'));

        quizQuestions.add(new QuizQuestion("In which year did Rohit Sharma make his debut for the Indian cricket team?", new ArrayList<>(List.of("A) 2006", "B) 2007", "C) 2008", "D) 2009")), 'B'));

        quizQuestions.add(new QuizQuestion("How many double centuries has Rohit Sharma scored in One Day Internationals (ODIs)?", new ArrayList<>(List.of("A) 3", "B) 2", "C) 4", "D) 5")), 'A'));

        quizQuestions.add(new QuizQuestion("Who is known as the 'God of Cricket' in India?", new ArrayList<>(List.of("A) Sachin Tendulkar", "B) Virat Kohli", "C) Rohit Sharma", "D) Virender Sehwag")), 'A'));

        quizQuestions.add(new QuizQuestion("Which IPL team were the Champions in the year 2015?", new ArrayList<>(List.of("A) Chennai Super Kings", "B) Kolkata Knight Riders", "C) Royal Challengers Bangalore", "D) Mumbai Indians")), 'D'));

         
        SwingUtilities.invokeLater(() -> new QuizApp(quizQuestions));
    }
}
