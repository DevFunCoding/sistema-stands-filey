import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class CalcView extends JFrame {
    private static final String INITIAL_VALUE = "0";
    private final JTextField userInputTf = new JTextField(10);
    private final JTextField totalTf = new JTextField(20);
    private final JButton sumBtn = new JButton("Sumar");
    private final JButton multiplyBtn = new JButton("Multiplicar");
    private final JButton clearBtn = new JButton("Limpiar");

    public CalcView() {
        this.setTitle("Calculadora MVC");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        totalTf.setText(INITIAL_VALUE);
        totalTf.setEditable(false);

        JPanel content = new JPanel(new FlowLayout());
        content.add(new JLabel("Entrada:"));
        content.add(userInputTf);
        content.add(sumBtn);
        content.add(multiplyBtn);
        content.add(new JLabel("Total:"));
        content.add(totalTf);
        content.add(clearBtn);

        this.setContentPane(content);
        this.pack();
        this.setLocationRelativeTo(null);
    }

    public String getUserInput() {
        return userInputTf.getText();
    }

    public void setTotal(String newTotal) {
        totalTf.setText(newTotal);
    }

    public void resetInput() {
        userInputTf.setText("");
    }

    public void showError(String errMessage) {
        JOptionPane.showMessageDialog(this, errMessage, "Error de Entrada", JOptionPane.ERROR_MESSAGE);
    }

    public void addSumListener(ActionListener sal) {
        sumBtn.addActionListener(sal);
    }

    public void addMultiplyListener(ActionListener mal) {
        multiplyBtn.addActionListener(mal);
    } 
    
    public void addClearListener(ActionListener cal) {
        clearBtn.addActionListener(cal);
    }
}