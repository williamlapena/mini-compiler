import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Analyze {

    static ArrayList<String> output = new ArrayList<>();
    static ArrayList<String> syntaxList = new ArrayList<>();
    static boolean error = false;
    static Scanner scan = new Scanner(System.in);
    static String syntaxFormat = "";

    public static void main (String args[]){

        while(true){
            System.out.print("Enter Expression: ");
            String expression = scan.nextLine();

            List<String> splitted = splitString(expression);

            boolean lexValid = true;
            boolean syntaxValid = true;
            


            lexValid = lexical(splitted);

            if(lexValid == true){
                syntaxValid = syntax(output);
            }
            
            if(syntaxValid == true){
                semantic(syntaxList);
            }

            System.out.print("\nDo you want to try again? (Enter (y) if yes, enter any key if no) ");
            String enter = scan.nextLine();

            if(enter.equals("y")){
                System.out.println();
                output.clear();
                syntaxList.clear();
                System.out.println("");
            }
            else{
                System.out.println("End Program");
                break;
            }
        
        }

    }

    public static boolean semantic(List<String> syntaxList){

        if("assignment".equals(syntaxFormat)){

            if(syntaxList.get(0).equals("int") && syntaxList.get(3).matches("-?\\d+")){
                System.out.println("Semantics Correct!");
                return true;
            }
            else if(syntaxList.get(0).equals("String") && syntaxList.get(3).matches("\"[^\"]*\"")){
                System.out.println("Semantics Correct!");
                return true;                                
            }
            else if(syntaxList.get(0).equals("char") && syntaxList.get(3).matches("'.'")){
                System.out.println("Semantics Correct!");
                return true;  
            }
            else if(syntaxList.get(0).equals("double") && syntaxList.get(3).matches("-?\\d+(\\.\\d+)?")){
                System.out.println("Semantics Correct!");
                return true;    
            }
            else{
                System.out.println("Semantics Wrong!");
            }
        }
        else if ("declaration".equals(syntaxFormat)){
            System.out.println("Semantics Vague");
        }

        return false;

        
    }

    public static boolean syntax(List<String> output){

        if("<data_type>".equals(output.get(0)) && "<identifier>".equals(output.get(1)) && "<assignment_operator>".equals(output.get(2)) && "<value>".equals(output.get(3)) && "<delimiter>".equals(output.get(4)))
        {
            syntaxFormat = "assignment";

            return true;
        }
        else if("<data_type>".equals(output.get(0)) && "<identifier>".equals(output.get(1)) && "<delimiter>".equals(output.get(2))){
            syntaxFormat = "declaration";

            return true;
        }
        else{
            System.out.println("Syntax Wrong!");
            return false;
        }
    }
    

    public static boolean lexical(List<String> splited){
        for (String out : splited) {

            if(out.equals(";")){
                tokenizer(out);
            }
            else if(out.endsWith(";")){
                String noSemiColon = out.substring(0, out.length() - 1);
                tokenizer(noSemiColon);

                String semiColon = ";";
                tokenizer(semiColon);
            }
            else{
                tokenizer(out);
            }
        }

        System.out.println();

        if(error == true){
            System.out.println("Lexeme Error!");
            return false;
        }
        else{
            return true;
        }
    }

    public static void tokenizer(String out){
        if(out.equals("int") || out.equals("double") || out.equals("char") || out.equals("String")){
            syntaxList.add(out);
            output.add("<data_type>");
        }
        else if(out.equals("=")){
            syntaxList.add(out);
            output.add("<assignment_operator>");
        }
        else if(out.equals(";")){
            syntaxList.add(out);
            output.add("<delimiter>");
        }
        else if(out.matches("-?\\d+") || out.matches("\"[^\"]*\"") || out.matches("'.'") || out.matches("-?\\d+(\\.\\d+)?")){
            syntaxList.add(out);
            output.add("<value>");
        }
        else if(!out.contains("\"")){
            syntaxList.add(out);
            output.add("<identifier>");
        }
        else{
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
