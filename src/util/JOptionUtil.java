package util;

import javax.swing.JOptionPane;

public class JOptionUtil {

    public static String plainMessage(String title, String message) {
        return JOptionPane.showInputDialog(null, message, title, JOptionPane.PLAIN_MESSAGE);
    }

    public static String initialValue(String message, String value) {
        return JOptionPane.showInputDialog(null, message, value);
    }

    public static void showMessage(String message) {
        JOptionPane.showMessageDialog(null, message);
    }

    public static int showConfirm(String message) {
        return JOptionPane.showConfirmDialog(null, message);
    }

}
