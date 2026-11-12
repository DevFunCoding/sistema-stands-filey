import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ICalculatorModel model = new CalcModel();
            CalcView view = new CalcView();
            new CalcController(model, view);
            view.setVisible(true);
        });
    }
}