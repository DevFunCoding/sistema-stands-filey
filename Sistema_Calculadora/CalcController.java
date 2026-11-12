import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CalcController {
    private final ICalculatorModel model;
    private final CalcView view;

    public CalcController(ICalculatorModel model, CalcView view) {
        this.model = model;
        this.view = view;

        // Registro de los ActionListeners en la vista
        this.view.addSumListener(new SumListener());
        this.view.addMultiplyListener(new MultiplyListener());
        this.view.addClearListener(new ClearListener());
    }

    class SumListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String userInput = view.getUserInput();
            try {
                if (model.getValue().equals("0") && !userInput.isEmpty()) {
                    model.setValue(userInput);
                } else {
                    model.add(userInput);
                }
                view.setTotal(model.getValue());
                view.resetInput();
            } catch (NumberFormatException nfex) {
                view.showError("Entrada inválida: '" + userInput + "'. Ingrese un número válido.");
            }
        }
    }

    class MultiplyListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String userInput = view.getUserInput();
            try {
                if (model.getValue().equals("0") && !userInput.isEmpty()) {
                    model.setValue(userInput);
                } else {
                    model.multiplyBy(userInput);
                }
                view.setTotal(model.getValue());
                view.resetInput();
            } catch (NumberFormatException nfex) {
                view.showError("Entrada inválida: '" + userInput + "'. Ingrese un número válido.");
            }
        }
    }

    class ClearListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            model.reset();
            view.setTotal(model.getValue());
            view.resetInput();
        }
    }
}