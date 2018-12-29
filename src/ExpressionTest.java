import junit.framework.TestCase;

public class ExpressionTest extends TestCase {
    private static void testIsCorrectTestCase(String expr, boolean expected) {
        System.out.println("Creating an expression: " + expr + "...");

        Expression expression = new Expression(expr);

        System.out.println("Expression created, good!");


        System.out.println("Testing...");

        boolean result =  expression.isCorrect();

        assertEquals(expected, result);

        System.out.println("The expression already tested!");
        System.out.println();
    }

    public static void testIsCorrect() {
        System.out.println("Running unit-tests for class Expression :: public boolean isCorrect()");
        System.out.println();

        testIsCorrectTestCase("42", true);
        testIsCorrectTestCase("1+2", true);
        testIsCorrectTestCase("+2-1", true);
        testIsCorrectTestCase("*2-1", false);
        testIsCorrectTestCase("-2*1", true);
        testIsCorrectTestCase("2 * (3 + 4)", true);
        testIsCorrectTestCase("2(3 + 4)", false);
        testIsCorrectTestCase("2^2^2^2^2^2", true);
        testIsCorrectTestCase("                           2      +       2       ", true);
        testIsCorrectTestCase("()()()()()((()))(()(", false);
        testIsCorrectTestCase("(1+2)*(-10)-(((35)+42)*228)((1488)-3(", false);
        testIsCorrectTestCase("((11))))))", false);
        testIsCorrectTestCase("ajdkjhdkajdka", false);
        testIsCorrectTestCase("1+2*(3-1) + (321 -312)/3", true);


        System.out.println("All tests are processed!");
        System.out.println();
    }

    private static void testGetResultTestCase(String expr, String expected) {
        System.out.println("Creating an expression: " + expr + "...");

        Expression expression = new Expression(expr);

        System.out.println("Expression created, good!");

        ResultsSaver saver = new ResultsSaver();

        System.out.println("Testing...");

        String result = expression.getResult(saver);
        assertEquals(expected, result);

        System.out.println("The expression already tested!");
        System.out.println();
    }

    public static void testGetResult() {
        System.out.println("Running unit-tests for class Expression :: public String getResult(ResultSaver)");
        System.out.println();


        testGetResultTestCase("1+2", "3");
        testGetResultTestCase("0", "0");
        testGetResultTestCase("*1", "ERR");
        testGetResultTestCase("2^2^2", "16");
        testGetResultTestCase("1-2-3", "-4");
        testGetResultTestCase("1+2*(3-1) + (321 -312)/3", "8");
        testGetResultTestCase("1/0", "ERR");
        testGetResultTestCase("0/1", "0");

        System.out.println("All tests are processed!");
        System.out.println();
    }

    private static void testGetCoordinatesTestCase(String expr, String expected) {
        System.out.println("Creating an expression: " + expr + "...");

        Expression expression = new Expression(expr);

        System.out.println("Expression created, good!");

        System.out.println("Testing...");

        String result = expression.getCoordinates().toString();
        assertEquals(expected, result);

        System.out.println("The expression already tested!");
        System.out.println();
    }

    public static void testGetCoordinates() {
        System.out.println("Running unit-tests for class Expression :: public List<String> getCoordinates()");
        System.out.println();


        testGetCoordinatesTestCase("A0", "[A0]");
        testGetCoordinatesTestCase("0", "[]");
        testGetCoordinatesTestCase("A11+BA11+222+PA2", "[A11, BA11, PA2]");
        testGetCoordinatesTestCase("2^2^2", "[]");
        testGetCoordinatesTestCase("1+(2+3)+(A1)*SQSQDQDQDQDQDQDQDQDQD1212121311313131313",
                "[A1, SQSQDQDQDQDQDQDQDQDQD1212121311313131313]");
        testGetCoordinatesTestCase("A*2", "[]");

        System.out.println("All tests are processed!");
        System.out.println();
    }


    private static void testGetFormulaTestCase(String expr, String expected) {
        System.out.println("Creating an expression: " + expr + "...");

        Expression expression = new Expression(expr);

        System.out.println("Expression created, good!");

        System.out.println("Testing...");

        String result = expression.getFormula();
        assertEquals(expected, result);

        System.out.println("The expression already tested!");
        System.out.println();
    }

    public static void testGetFormula() {
        System.out.println("Running unit-tests for class Expression :: public boolean getFormula()");
        System.out.println();


        testGetFormulaTestCase("A0", "(A0)");
        testGetFormulaTestCase("0", "(0)");
        testGetFormulaTestCase("A11   +    BA11   +     222 + PA2", "(A11+BA11+222+PA2)");
        System.out.println("All tests are processed!");
        System.out.println();
    }
}
