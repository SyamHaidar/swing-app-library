package view;

import repository.*;
import service.*;
import util.JOptionUtil;

public class HomeView {

    public static void menu() {
        boolean check = true;

        while (check) {
            boolean auth = auth();
            if (auth) {
                check = false;
                mainMenu();
            } else {
                JOptionUtil.showMessage("Data tidak valid");
            }
        }
    }

    public static void mainMenu() {
        while (true) {
            String pilih = JOptionUtil.plainMessage(
                    "Perpustakaan",
                    "1. Buku \n2. Member \n3. Transaksi \n4. Denda \n5. Exit"
            );

            switch (pilih) {
                case "1":
                    book();
                    break;
                case "2":
                    member();
                    break;
                case "3":
                    transaction();
                    break;
                case "4":
                    pay();
                    break;
                case "5":
                    JOptionUtil.showMessage("Aplikasi keluar");
                    System.exit(0);
                    break;
                default:
                    JOptionUtil.showMessage("Menu tidak ada");
                    break;
            }
        }
    }

    public static boolean auth() {
        AuthRepository authRepository = new AuthRepositoryImpl();
        AuthService authService = new AuthServiceImpl(authRepository);
        AuthView authView = new AuthView(authService);
        return authView.menu();
    }

    public static void book() {
        BookRepository bookRepository = new BookRepositoryImpl();
        BookService bookService = new BookServiceImpl(bookRepository);
        BookView bookView = new BookView(bookService);
        bookView.menu();
    }

    public static void member() {
        MemberRepository memberRepository = new MemberRepositoryImpl();
        MemberService memberService = new MemberServiceImpl(memberRepository);
        MemberView memberView = new MemberView(memberService);
        memberView.menu();
    }

    public static void transaction() {
        TransactionRepository transactionRepository = new TransactionRepositoryImpl();
        TransactionService transactionService = new TransactionServiceImpl(transactionRepository);
        TransactionView transactionView = new TransactionView(transactionService);
        transactionView.menu();
    }

    public static void pay() {
        PayRepository payRepository = new PayRepositoryImpl();
        PayService payService = new PayServiceImpl(payRepository);
        PayView payView = new PayView(payService);
        payView.menu();
    }

}
