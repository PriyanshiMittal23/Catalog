package shamiSecret;
import org.json.JSONObject;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.math.BigInteger;

public class shamiSecretSharing {

	public static void main(String[] args) {
        try {
            // Step 1: Read JSON file
            String content = new String(Files.readAllBytes(Paths.get("input.json")));
            JSONObject jsonObject = new JSONObject(content);

            // Extract keys 'n' and 'k'
            int n = jsonObject.getJSONObject("keys").getInt("n");
            int k = jsonObject.getJSONObject("keys").getInt("k");

            // Create arrays to store x and y values
            int[] x = new int[n];
            BigInteger[] y = new BigInteger[n];

            // Parse the given roots from JSON
            int index = 0;
            for (String key : jsonObject.keySet()) {
                if (!key.equals("keys")) {
                    int base = jsonObject.getJSONObject(key).getInt("base");
                    String value = jsonObject.getJSONObject(key).getString("value");

                    // Convert y value from given base to decimal
                    BigInteger yValue = new BigInteger(value, base);

                    // Store the decoded (x, y) pairs
                    x[index] = Integer.parseInt(key);  // x is the key in the JSON
                    y[index] = yValue;                 // y is the decoded value
                    index++;
                }
            }

            // Step 3: Use Lagrange interpolation to find the constant term (secret 'c')
            BigInteger secret = lagrangeInterpolation(x, y, k);

            // Output the constant term (secret)
            System.out.println("Secret (c): " + secret);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static BigInteger lagrangeInterpolation(int[] x, BigInteger[] y, int k) {
        BigInteger result = BigInteger.ZERO;

        for (int i = 0; i < k; i++) {
            BigInteger term = y[i];

            for (int j = 0; j < k; j++) {
                if (i != j) {
                    term = term.multiply(BigInteger.valueOf(-x[j]))
                               .divide(BigInteger.valueOf(x[i] - x[j]));
                }
            }
            result = result.add(term);
        }
        return result;
    }

}
