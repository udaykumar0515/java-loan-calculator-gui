import java.awt.*;
import java.text.DecimalFormat;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class LoanCalculatorGUI extends JFrame {
    private final JTextField loanAmountField, interestRateField, loanTenureField, downPaymentField, downPaymentAmountField;
    private final JSlider interestRateSlider, loanTenureSlider, downPaymentSlider;
    private final JLabel emiLabel, totalInterestLabel, totalPaymentLabel;
    private final JCheckBox downPaymentCheckbox;
    private final JPanel downPaymentPanel;
    private final JRadioButton yearsButton, monthsButton;
    private final JButton calculateButton;

    private static final DecimalFormat df = new DecimalFormat("#,###");

    @SuppressWarnings("unused")
    public LoanCalculatorGUI() {
        setTitle("Loan Calculator");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        setResizable(false);

        // Main Panel with GridBagLayout for alignment
        final JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(Color.LIGHT_GRAY);

        // GridBagConstraints for component layout
        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Loan Amount Section
        loanAmountField = createFormattedTextField("");
        addLabelAndField(mainPanel, "Loan Amount (₹):", loanAmountField, gbc, 0);

        // Interest Rate Section with slider
        interestRateSlider = new JSlider(5, 20, 9);
        interestRateSlider.setMajorTickSpacing(5);
        interestRateSlider.setPaintTicks(true);
        interestRateSlider.setPaintLabels(true);
        interestRateField = createFormattedTextField("9");
        interestRateSlider.addChangeListener(e -> interestRateField.setText(String.valueOf(interestRateSlider.getValue())));
        addSliderPanel(mainPanel, "Interest Rate (%)", interestRateSlider, interestRateField, gbc, 1);

        // Loan Tenure Section with radio buttons for years and months
        yearsButton = new JRadioButton("Years", true);
        monthsButton = new JRadioButton("Months");
        final ButtonGroup tenureGroup = new ButtonGroup();
        tenureGroup.add(yearsButton);
        tenureGroup.add(monthsButton);
        yearsButton.addActionListener(e -> updateTenureSliderRange());
        monthsButton.addActionListener(e -> updateTenureSliderRange());

        final JPanel tenureOptionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        tenureOptionPanel.add(yearsButton);
        tenureOptionPanel.add(monthsButton);

        loanTenureSlider = new JSlider(1, 30, 5);
        loanTenureField = createFormattedTextField("20");
        loanTenureSlider.addChangeListener(e -> loanTenureField.setText(String.valueOf(loanTenureSlider.getValue())));

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        mainPanel.add(new JLabel("Loan Tenure:"), gbc);
        gbc.gridy = 3;
        mainPanel.add(tenureOptionPanel, gbc);
        gbc.gridy = 4;
        addSliderPanel(mainPanel, "", loanTenureSlider, loanTenureField, gbc, 4);

        // Call the method to set initial tenure range and labels
        updateTenureSliderRange();

        // Down Payment Section with additional amount display
        downPaymentCheckbox = new JCheckBox("Include Down Payment");
        downPaymentCheckbox.addActionListener(e -> toggleDownPayment());

        downPaymentSlider = new JSlider(0, 100, 10);
        downPaymentSlider.setMajorTickSpacing(25);
        downPaymentSlider.setPaintTicks(true);
        downPaymentSlider.setPaintLabels(true);
        downPaymentField = createFormattedTextField("10");
        downPaymentAmountField = createFormattedTextField("0");

        downPaymentSlider.addChangeListener(e -> {
            downPaymentField.setText(String.valueOf(downPaymentSlider.getValue()));
            updateDownPaymentAmount();
        });

        downPaymentPanel = createSliderPanel("Down Payment (%)", downPaymentSlider, downPaymentField, downPaymentAmountField);
        downPaymentPanel.setVisible(false);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        mainPanel.add(downPaymentCheckbox, gbc);
        gbc.gridy = 6;
        mainPanel.add(downPaymentPanel, gbc);

        // Calculate Button
        calculateButton = new JButton("Calculate");
        calculateButton.addActionListener(e -> calculateEMI());
        gbc.gridy = 7;
        mainPanel.add(calculateButton, gbc);

        // EMI and Total Payment Display
        final JPanel resultPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        resultPanel.setBorder(BorderFactory.createTitledBorder("Loan Summary"));
        resultPanel.setBackground(Color.WHITE);

        emiLabel = new JLabel("EMI: ₹0", SwingConstants.CENTER);
        totalInterestLabel = new JLabel("Total Interest Payable: ₹0", SwingConstants.CENTER);
        totalPaymentLabel = new JLabel("Total Payment: ₹0", SwingConstants.CENTER);

        resultPanel.add(emiLabel);
        resultPanel.add(totalInterestLabel);
        resultPanel.add(totalPaymentLabel);

        add(mainPanel, BorderLayout.CENTER);
        add(resultPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private JTextField createFormattedTextField(final String text) {
        final JTextField field = new JTextField(text, 5);
        field.setHorizontalAlignment(JTextField.CENTER);
        field.setFont(new Font("Arial", Font.BOLD, 14));
        field.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        return field;
    }

    private void addLabelAndField(final JPanel panel, final String labelText, final JTextField field, final GridBagConstraints gbc, final int yPos) {
        final JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 14));

        gbc.gridx = 0;
        gbc.gridy = yPos;
        gbc.gridwidth = 1;
        panel.add(label, gbc);

        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    private void addSliderPanel(final JPanel panel, final String labelText, final JSlider slider, final JTextField field, final GridBagConstraints gbc, final int yPos) {
        final JPanel sliderPanel = new JPanel(new BorderLayout(10, 10));
        sliderPanel.add(new JLabel(labelText), BorderLayout.WEST);
        sliderPanel.add(slider, BorderLayout.CENTER);
        sliderPanel.add(field, BorderLayout.EAST);

        gbc.gridx = 0;
        gbc.gridy = yPos;
        gbc.gridwidth = 2;
        panel.add(sliderPanel, gbc);
    }

    private JPanel createSliderPanel(final String label, final JSlider slider, final JTextField field, final JTextField amountField) {
        final JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.add(new JLabel(label), BorderLayout.WEST);
        panel.add(slider, BorderLayout.CENTER);

        final JPanel amountPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        amountPanel.add(field);
        amountPanel.add(new JLabel("₹"));
        amountPanel.add(amountField);
        panel.add(amountPanel, BorderLayout.EAST);
        panel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        return panel;
    }

    private void toggleDownPayment() {
        downPaymentPanel.setVisible(downPaymentCheckbox.isSelected());
        updateDownPaymentAmount();
    }

    private void updateTenureSliderRange() {
        if (yearsButton.isSelected()) {
            loanTenureSlider.setMaximum(30);
            loanTenureSlider.setMajorTickSpacing(5);
            loanTenureSlider.setPaintLabels(true);

            final java.util.Hashtable<Integer, JLabel> labelTable = new java.util.Hashtable<>();
            for (int i = 0; i <= 30; i += 5) {
                if (i == 0) continue;
                labelTable.put(i, new JLabel(String.valueOf(i)));
            }
            labelTable.put(1, new JLabel("1"));
            labelTable.put(30, new JLabel("30"));
            loanTenureSlider.setLabelTable(labelTable);
        } else {
            loanTenureSlider.setMaximum(360);
            loanTenureSlider.setMajorTickSpacing(60);
            loanTenureSlider.setMinorTickSpacing(12);
            loanTenureSlider.setPaintLabels(true);

            final java.util.Hashtable<Integer, JLabel> labelTable = new java.util.Hashtable<>();
            labelTable.put(1, new JLabel("1"));
            for (int i = 60; i <= 360; i += 60) {
                labelTable.put(i, new JLabel(String.valueOf(i)));
            }
            loanTenureSlider.setLabelTable(labelTable);
        }
        loanTenureSlider.setValue(loanTenureSlider.getMinimum());
        revalidate();
        repaint();
    }

    private void updateDownPaymentAmount() {
        if (loanAmountField.getText().isEmpty() || downPaymentField.getText().isEmpty()) {
            downPaymentAmountField.setText("0");
            return;
        }
        final double loanAmount = Double.parseDouble(loanAmountField.getText());
        final int downPaymentPercentage = downPaymentSlider.getValue();
        final double downPaymentAmount = (loanAmount * downPaymentPercentage) / 100;
        downPaymentAmountField.setText(df.format(downPaymentAmount));
    }

    private void calculateEMI() {
        try {
            final double loanAmount = Double.parseDouble(loanAmountField.getText());
            final double interestRate = Double.parseDouble(interestRateField.getText()) / 12 / 100;
            final int tenure = yearsButton.isSelected() ? loanTenureSlider.getValue() * 12 : loanTenureSlider.getValue();

            double principal = loanAmount;
            if (downPaymentCheckbox.isSelected()) {
                final double downPayment = Double.parseDouble(downPaymentAmountField.getText().replace(",", ""));
                principal -= downPayment;
            }

            final double emi = (principal * interestRate * Math.pow(1 + interestRate, tenure)) / (Math.pow(1 + interestRate, tenure) - 1);
            final double totalPayment = emi * tenure;
            final double totalInterest = totalPayment - principal;

            emiLabel.setText("EMI: ₹" + df.format(emi));
            totalInterestLabel.setText("Total Interest Payable: ₹" + df.format(totalInterest));
            totalPaymentLabel.setText("Total Payment: ₹" + df.format(totalPayment));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numeric values.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoanCalculatorGUI::new);
    }
}
