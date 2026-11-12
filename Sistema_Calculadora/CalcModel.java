import java.math.BigDecimal;

public class CalcModel implements ICalculatorModel {
    private static final String INITIAL_VALUE = "0";
    private BigDecimal total;

    public CalcModel() {
        reset();
    }

    @Override
    public void reset() {
        this.total = new BigDecimal(INITIAL_VALUE);
    }

    @Override
    public void add(String operand) throws NumberFormatException {
        BigDecimal number = new BigDecimal(operand.trim());
        this.total = this.total.add(number);
    }

    @Override
    public void multiplyBy(String operand) throws NumberFormatException {
        BigDecimal number = new BigDecimal(operand.trim());
        this.total = this.total.multiply(number);
    }

    @Override
    public void setValue(String value) throws NumberFormatException {
        this.total = new BigDecimal(value.trim());
    }

    @Override
    public String getValue() {
        return this.total.toPlainString();
    }
}