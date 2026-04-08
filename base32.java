public class base32 {

    private static final String BASE32_ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567";

    public static String base32Encode(byte[] data) {
        StringBuilder binary = new StringBuilder();

        for (byte b : data) {
            binary.append(String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0'));
        }

        while (binary.length() % 5 != 0) {
            binary.append("0");
        }

        StringBuilder encoded = new StringBuilder();

        for (int i = 0; i < binary.length(); i += 5) {
            String chunk = binary.substring(i, i + 5);
            int index = Integer.parseInt(chunk, 2);
            encoded.append(BASE32_ALPHABET.charAt(index));
        }

        while (encoded.length() % 8 != 0) {
            encoded.append("=");
        }

        return encoded.toString();
    }

    public static byte[] base32Decode(String encoded) {
        encoded = encoded.replace("=", "");
        StringBuilder binary = new StringBuilder();

        for (char c : encoded.toCharArray()) {
            int index = BASE32_ALPHABET.indexOf(c);
            if (index == -1) {
                throw new IllegalArgumentException("Invalid character: " + c);
            }
            binary.append(String.format("%5s", Integer.toBinaryString(index)).replace(' ', '0'));
        }

        java.io.ByteArrayOutputStream output = new java.io.ByteArrayOutputStream();

        for (int i = 0; i + 8 <= binary.length(); i += 8) {
            String byteStr = binary.substring(i, i + 8);
            output.write(Integer.parseInt(byteStr, 2));
        }

        return output.toByteArray();
    }

    public static void main(String[] args) {
        String text = "ju pershendes";

        String encoded = base32Encode(text.getBytes());
        System.out.println("Encoded: " + encoded);

        String decoded = new String(base32Decode(encoded));
        System.out.println("Decoded: " + decoded);
    }
}
