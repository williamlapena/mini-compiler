import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AnalyzeUI {

    static ArrayList<String> output = new ArrayList<>();
    static ArrayList<String> syntaxList = new ArrayList<>();
    static boolean error = false;
    static String syntaxFormat = "";

    public static void main(String[] args) {
        // Create the main frame
        JFrame frame = new JFrame("Expression Analyzer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        // Create UI components
        JLabel expressionLabel = new JLabel("Enter Expression:");
        JTextField expressionField = new JTextField(30);
        JButton analyzeButton = new JButton("Analyze");
        JTextArea resultArea = new JTextArea(10, 50);
        resultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultArea);

        // Add components to the frame
        JPanel panel = new JPanel();
        panel.add(expressionLabel);
        panel.add(expressionField);
        panel.add(analyzeButton);
        panel.add(scrollPane);

        frame.add(panel);
        frame.setVisible(true);

        // Add event listener to the button
        analyzeButton.addActionListener(e -> {
            String expression = expressionField.getText().trim();
            if (expression.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please enter an expression.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            List<String> splitted = splitString(expression);

            boolean lexValid = lexical(splitted);
            boolean syntaxValid = false;
            if (lexValid) {
                syntaxValid = syntax(output);
            }
            if (syntaxValid) {
                semantic(syntaxList);
            }

            // Clear the lists for the next input
            resultArea.setText(String.join("\n", output));
            output.clear();
            syntaxList.clear();
        });
    }

    public static boolean semantic(List<String> syntaxList) {
        if ("assignment".equals(syntaxFormat)) {
            if (syntaxList.get(0).equals("int") && syntaxList.get(3).matches("-?\\d+")) {
                JOptionPane.showMessageDialog(null, "Semantics Correct!");
                return true;
            } else if (syntaxList.get(0).equals("String") && syntaxList.get(3).matches("\"[^\"]*\"")) {
                JOptionPane.showMessageDialog(null, "Semantics Correct!");
                return true;
            } else if (syntaxList.get(0).equals("char") && syntaxList.get(3).matches("'.'")) {
                JOptionPane.showMessageDialog(null, "Semantics Correct!");
                return true;
            } else if (syntaxList.get(0).equals("double") && syntaxList.get(3).matches("-?\\d+(\\.\\d+)?")) {
                JOptionPane.showMessageDialog(null, "Semantics Correct!");
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "Semantics Wrong!");
            }
        } else if ("declaration".equals(syntaxFormat)) {
            JOptionPane.showMessageDialog(null, "Semantics Vague");
        }
        return false;
    }

    public static boolean syntax(List<String> output) {
        if ("<data_type>".equals(output.get(0)) && "<identifier>".equals(output.get(1)) &&
                "<assignment_operator>".equals(output.get(2)) && "<value>".equals(output.get(3)) &&
                "<delimiter>".equals(output.get(4))) {
            syntaxFormat = "assignment";
            return true;
        } else if ("<data_type>".equals(output.get(0)) && "<identifier>".equals(output.get(1)) &&
                "<delimiter>".equals(output.get(2))) {
            syntaxFormat = "declaration";
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "Syntax Wrong!");
            return false;
        }
    }

    public static boolean lexical(List<String> splited) {
        for (String out : splited) {
            if (out.equals(";")) {
                tokenizer(out);
            } else if (out.endsWith(";")) {
                String noSemiColon = out.substring(0, out.length() - 1);
                tokenizer(noSemiColon);

                String semiColon = ";";
                tokenizer(semiColon);
            } else {
                tokenizer(out);
            }
        }

        if (error) {
            JOptionPane.showMessageDialog(null, "Lexeme Error!");
            return false;
        }
        return true;
    }

    public static void tokenizer(String out) {
        if (out.equals("int") || out.equals("double") || out.equals("char") || out.equals("String")) {
            syntaxList.add(out);
            output.add("<data_type>");
        } else if (out.equals("=")) {
            syntaxList.add(out);
            output.add("<assignment_operator>");
        } else if (out.equals(";")) {
            syntaxList.add(out);
            output.add("<delimiter>");
        } else if (out.matches("-?\\d+") || out.matches("\"[^\"]*\"") || out.matches("'.'") || out.matches("-?\\d+(\\.\\d+)?")) {
            syntaxList.add(out);
            output.add("<value>");
        } else if (!out.contains("\"")) {
            syntaxList.add(out);
            output.add("<identifier>");
        } else {
            error = true;
        }
    }

    public static List<String> splitString(String input) {
        List<String> split = new ArrayList<>();
        Pattern pattern = Pattern.compile("\"([^\"]*)\"|\\S+");
        Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            String strSplit = matcher.group();
            split.add(strSplit);
        }

        return split;
    }
}
