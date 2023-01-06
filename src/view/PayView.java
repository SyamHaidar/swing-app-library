package view;

import service.PayService;
import util.JOptionUtil;

public class PayView {

    private final PayService payService;

    public PayView(PayService payService) {
        this.payService = payService;
    }

    public void menu() {
        String dendaMember = payService.getAll();
        String totalDenda = payService.total();

        double terlambat = payService.latePercentage();
        String oldValue = String.format("%.2f", terlambat);
        Double newValue = Double.parseDouble(oldValue);

        while (true) {
            String pilih = JOptionUtil.plainMessage(
                    "Perpustakaan - Denda",
                    "" + dendaMember + "\n" +
                            "" + totalDenda + "\n" +
                            "" + "Persentase keterlambatan : " + newValue + "%" +
                            "\n\n0.Kembali"
            );

            switch (pilih) {
                case "0":
                    goHome();
                    break;
                default:
                    JOptionUtil.showMessage("Menu tidak ada");
                    break;
            }
        }
    }

    public static void goHome() {
        HomeView.mainMenu();
    }

}
