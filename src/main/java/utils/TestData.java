package utils;

/**
 * Test data shared by the test classes: users, password and product names.
 */
public final class TestData {

    private TestData() {
    }

    // Users (all accepted users are listed on the SauceDemo login page)
    public static final String STANDARD_USER = ConfigReader.get("username");
    public static final String PASSWORD = ConfigReader.get("password");
    public static final String LOCKED_OUT_USER = "locked_out_user";
    public static final String PROBLEM_USER = "problem_user";

    // Products
    public static final String BACKPACK = "Sauce Labs Backpack";
    public static final String BIKE_LIGHT = "Sauce Labs Bike Light";
    public static final String BOLT_T_SHIRT = "Sauce Labs Bolt T-Shirt";
    public static final String FLEECE_JACKET = "Sauce Labs Fleece Jacket";
    public static final String ONESIE = "Sauce Labs Onesie";
    public static final String RED_T_SHIRT = "Test.allTheThings() T-Shirt (Red)";

    public static final int TOTAL_PRODUCTS = 6;

    // Checkout customer
    public static final String FIRST_NAME = "Jane";
    public static final String LAST_NAME = "Doe";
    public static final String POSTAL_CODE = "12345";

    // SauceDemo charges 8% tax on the item total
    public static final double TAX_RATE = 0.08;
}
