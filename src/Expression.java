import java.util.*;

public class Expression {
    private List<String> tokens;
    private boolean correct;

    private static final String[] OPERATIONS = {"+", "-", "*", "/", "%", "^"};
    private static final String[] BRACKETS = {"(", ")"};

    private boolean isCoordinate(String token) {
        boolean letters = true, digits = false;
        for(int i = 0; i < token.length(); i++) {
            char c = token.charAt(i);
            if(Character.isUpperCase(c)) {
                if(!letters) {
                    return false;
                }
            }
            else if(Character.isDigit(c)) {
                if(i == 0) {
                    return false;
                }
                else {
                    letters = false;
                }
            }
            else {
                return false;
            }
        }

        return !letters;
    }

    private boolean isCoordPref(String token) {
        boolean letters = true, digits = false;
        for(int i = 0; i < token.length(); i++) {
            char c = token.charAt(i);
            if(Character.isUpperCase(c)) {
                if(!letters) {
                    return false;
                }
            }
            else if(Character.isDigit(c)) {
                if(i == 0) {
                    return false;
                }
                else {
                    letters = false;
                }
            }
            else {
                return false;
            }
        }

        return true;
    }


    private boolean isOperation(String token) {
        for(String operation : OPERATIONS) {
            if(token.equals(operation)) {
                return true;
            }
        }
        return false;
    }


    private boolean isUnarOperation(String token) {
        return token.equals("+") || token.equals("-");
    }

    private boolean isBracket(String token) {
        for(String operation : BRACKETS) {
            if(token.equals(operation)) {
                return true;
            }
        }
        return false;
    }

    private boolean isNumber(String token) {
        if(token.isEmpty()) return false;
        if(token.length() == 1) {
            return Character.isDigit(token.charAt(0));
        }
        if(token.charAt(0) == '0') return false;
        for(int i = 0; i < token.length(); i++) {
            if(!Character.isDigit(token.charAt(i))) {
                return false;
            }
        }
        return true;
    }


    private boolean isClosingBracket(String token) {
        return token.equals(")");
    }

    private boolean isOpeningBracket(String token) {
        return isBracket(token) && !isClosingBracket(token);
    }

    private boolean isRightAssociative(String token) {
        return token.equals("^");
    }

    private boolean isOperand(String token) {
        return isCoordinate(token) || isNumber(token);
    }

    private boolean isOperator(String token) {
        return isOperation(token) || isBracket(token);
    }

    private boolean isCorrectToken(String token) {
        return isOperand(token) || isOperator(token);
    }
    private boolean isPartiallyCorrectToken(String token) {
        return isCorrectToken(token) || isCoordPref(token);
    }

    private int getPriority(String token)  {
        if(token.equals("+") || token.equals("-")) {
            return 2;
        }
        if(token.equals("*") || token.equals("/") || token.equals("%")) {
            return 1;
        }
        if(token.equals("^")) return 0;
        return 3;
    }

    private boolean checkCorrection() {
        if(tokens.isEmpty()) {
            return true;
        }
        int balance = 0;
        for(String token : tokens) {
            if(!isCorrectToken(token)) {
                return false;
            }

            if(token.equals("(")) balance++;
            if(token.equals(")")) balance--;

            if(balance < 0) {
                return false;
            }
        }
        if(balance != 0) {
            return false;
        }


        for(int i = 1; i < tokens.size(); i++) {
            String cur = tokens.get(i), prev = tokens.get(i - 1);
//            System.out.println("correction: " + prev + " " + cur);
            if(isOperand(cur) && isOperand(prev)) {
                return false;
            }
            if(isOperation(cur) && isOperation(prev)) {
                return false;
            }
            if(isOperation(cur) && !isUnarOperation(cur) && !(isOperand(prev) || isClosingBracket(prev))) {
                return false;
            }
            if(isOperation(prev) && !(isOperand(cur) || isOpeningBracket(cur))) {

                return false;
            }
            if(isClosingBracket(cur) && isOpeningBracket(prev)) {
                return false;
            }
            if(isClosingBracket(prev) && isOpeningBracket(cur)) {
                return false;
            }
            if(isOpeningBracket(cur) && isOperand(prev)) {
                return false;
            }
            if(isClosingBracket(prev) && isOperand(cur)) {
                return false;
            }
        }

        if(isOperation(tokens.get(0)) && !isUnarOperation(tokens.get(0))) {
            return false;
        }


        return true;
    }


    public Expression(String expressionStr) {
        tokens = new ArrayList<>();
        String curToken = "";
        expressionStr += " ";
        tokens.add("(");
        for(int i = 0; i < expressionStr.length(); i++) {
            char c = expressionStr.charAt(i);
            if(c == ' ' || !isPartiallyCorrectToken(curToken + c)) {
                if(!curToken.isEmpty()) {
                    if(isUnarOperation(curToken) &&
                            (tokens.isEmpty() || !(
                                    isOperand(tokens.get(tokens.size() - 1))
                                    || isClosingBracket(tokens.get(tokens.size() - 1))))
                        ){
                        tokens.add("0");
                    }
                    tokens.add(curToken);
                }
                curToken = "";

            }

            if(c != ' ') {
                curToken += c;
            }
        }
        tokens.add(")");
        correct = checkCorrection();
    }

    public boolean isCorrect() {
        return correct;
    }

    public List<String> getCoordinates() {
        List<String> result = new ArrayList<>();
        for(String token : tokens) {
            if(isCoordinate(token)) {
                result.add(token);
            }
        }
        return result;
    }

    private int getValueOf(String operand, ResultsSaver resultsSaver) {
        return Integer.parseInt((isNumber(operand) ? operand : resultsSaver.getResult(operand)));
    }

    private Integer performOperation(Integer a, Integer b, String operation) {
        if(operation.equals("+")) {
            return a + b;
        }
        if(operation.equals(("-"))) {
            return a - b;
        }
        if(operation.equals("*")) {
            return a * b;
        }

        if(operation.equals("^")) {
            return ((int)Math.round(Math.pow(a, b)));
        }

        if(operation.equals("%")) {
            if(b == 0) {
                setIncorrect();
                return 0;
            }
            return a % b;
        }
        if(operation.equals("/") ) {
            if(b == 0) {
                setIncorrect();
                return 0;
            }
            return a / b;
        }

        return 0;
    }

    private int getResultInt(ResultsSaver saver) {
        if(!correct) {
            return 0;
        }

        Stack<String> operators = new Stack<>();
        Stack<Integer> operands = new Stack<>();

        for(String token : tokens) {
//            System.out.println("Token : " + token);
            if(isOpeningBracket(token)) {
                operators.push(token);
            }
            else if(isClosingBracket(token)) {
                while(!isOpeningBracket(operators.peek())) {
                    Integer x = operands.pop();
                    Integer y = operands.pop();
                    String operator = operators.pop();
                    operands.push(performOperation(y, x, operator));
                }
                operators.pop();
            }
            else if(isOperation(token)) {
                while(!operators.empty() && isOperation(operators.peek())
                        && getPriority(operators.peek()) <= getPriority(token)){
                    List<Integer> curOperands = new ArrayList<>();
                    curOperands.add(operands.pop());
                    String operation = operators.peek();
                    while (!operators.empty() &&
                            getPriority(operators.peek()) == getPriority(operation)) {
                        curOperands.add(operands.pop());
                        operators.pop();
                    }

                    if(curOperands.size() > 1) {
                        if (!isRightAssociative(operation)) {
                            Collections.reverse(curOperands);
                        }

                        Integer result = curOperands.get(0);
                        for (int i = 1; i < curOperands.size(); i++) {
                            result = performOperation(result, curOperands.get(i), operation);
                        }
                        operands.push(result);
                    }
                    else {
                        for(int i = curOperands.size() - 1; i >= 0; i--) {
                            operands.push(curOperands.get(i));
                            if(i > 0) {
                                operators.push(operation);
                            }
                        }
                        break;
                    }

                }
                operators.push(token);
            }
            else {
                operands.push(getValueOf(token, saver));
            }

            if(!correct) {
                return 0;
            }
        }

        return operands.isEmpty() ? 0 : operands.peek();
    }

    public String getResult(ResultsSaver saver) {
        String result = String.valueOf(getResultInt(saver));
        return correct ? result : "ERR";
    }

    public void setIncorrect() {
        correct = false;

    }

    public String getFormula() {
        String text = "";
        for(String token : tokens) {
            text += token;
        }
        return text;
    }

}


