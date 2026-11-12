public interface ICalculatorModel {
    void add(String operand) throws NumberFormatException;
    void multiplyBy(String operand) throws NumberFormatException;
    void reset();
    void setValue(String value);
    String getValue();
}