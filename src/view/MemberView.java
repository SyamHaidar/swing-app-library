package view;

import model.Member;
import service.MemberService;
import util.JOptionUtil;

public class MemberView {

    private final MemberService memberService;

    public MemberView(MemberService memberService) {
        this.memberService = memberService;
    }

    public void menu() {
        while (true) {
            String pilih = JOptionUtil.plainMessage(
                    "Perpustakaan - Member",
                    "1. Data member \n2. Tambah member \n3. Edit member \n4. Hapus member \n5. Kembali"
            );

            switch (pilih) {
                case "1":
                    getAllMember();
                    break;
                case "2":
                    addMember();
                    break;
                case "3":
                    editMember();
                    break;
                case "4":
                    removeMember();
                    break;
                case "5":
                    goHome();
                    break;
                default:
                    JOptionUtil.showMessage("Menu tidak ada");
                    break;
            }
        }
    }

    public void getAllMember() {
        while (true) {
            String allData = memberService.getAllData();
            String getData = JOptionUtil.plainMessage(
                    "Perpustakaan - Data member",
                    "" + allData + "\n0. Kembali \nMasukan kode member untuk detail : "
            );

            if (getData.equals("0")) {
                menu();
            } else {
                try {
                    Integer.parseInt(getData);
                    memberService.getData(getData);
                } catch (NumberFormatException ex) {
                    JOptionUtil.showMessage("Masukan kode member dengan benar");
                }
            }
        }
    }

    public void addMember() {
        Member member = new Member();
        while (true) {
            String nama = JOptionUtil.plainMessage("Perpustakaan - Tambah member", "Nama lengkap :");
            if (!nama.isEmpty()) {
                member.setNama(nama);
                break;
            } else {
                JOptionUtil.showMessage("Nama tidak boleh kosong");
            }
        }
        while (true) {
            String tgl = JOptionUtil.plainMessage("Perpustakaan - Tambah member", "Tgl Lahir : (e.g : 01/12/2000)");
            if (!tgl.isEmpty()) {
                member.setTglLahir(tgl);
                break;
            } else {
                JOptionUtil.showMessage("Tgl lahir tidak boleh kosong");
            }
        }
        while (true) {
            String alamat = JOptionUtil.plainMessage("Perpustakaan - Tambah member", "Alamat :");
            if (!alamat.isEmpty()) {
                member.setAlamat(alamat);
                break;
            } else {
                JOptionUtil.showMessage("Alamat tidak boleh kosong");
            }
        }
        while (true) {
            String kelamin = JOptionUtil.plainMessage("Perpustakaan - Tambah member", "Kelamin : \n1. pria \n2. wanita");
            try {
                Integer.parseInt(kelamin);
                if (kelamin.equals("1")) {
                    member.setKelamin("pria");
                    break;
                } else if (kelamin.equals("2")) {
                    member.setKelamin("wanita");
                    break;
                } else {
                    JOptionUtil.showMessage("Pilih jenis kelamin");
                }
            } catch (NumberFormatException ex) {
                JOptionUtil.showMessage("Pilih jenis kelamin");
            }
        }
        while (true) {
            String telepon = JOptionUtil.plainMessage("Perpustakaan - Tambah member", "Telepon :");
            try {
                Long.parseLong(telepon);
                member.setTelepon(telepon);
                break;
            } catch (NumberFormatException ex) {
                JOptionUtil.showMessage("Masukan nomor telepon dengan benar");
            }
        }

        memberService.add(member);
    }

    public void editMember() {
        Member member = new Member();

        String id = JOptionUtil.plainMessage("Perpustakaan - Edit buku", "0. Kembali \nMasukan kode member yang akan diedit :");

        if (id.equals("0")) {
            menu();
        } else {
            try {
                Integer.parseInt(id);
                boolean dataExist = memberService.dataExist(id);
                if (dataExist) {
                    memberService.edit(id, member);
                    while (true) {
                        String nama = JOptionUtil.initialValue("Nama lengkap :", member.getNama());
                        if (!nama.isEmpty()) {
                            member.setNama(nama);
                            break;
                        } else {
                            JOptionUtil.showMessage("Nama tidak boleh kosong");
                        }
                    }
                    while (true) {
                        String tgl = JOptionUtil.initialValue("Tgl Lahir : (e.g : 01/12/2000)", member.getTglLahir());
                        if (!tgl.isEmpty()) {
                            member.setTglLahir(tgl);
                            break;
                        } else {
                            JOptionUtil.showMessage("Tgl lahir tidak boleh kosong");
                        }
                    }
                    while (true) {
                        String alamat = JOptionUtil.initialValue("Alamat :", member.getAlamat());
                        if (!alamat.isEmpty()) {
                            member.setAlamat(alamat);
                            break;
                        } else {
                            JOptionUtil.showMessage("Alamat tidak boleh kosong");
                        }
                    }
                    while (true) {
                        String isGender = member.getKelamin();
                        if (isGender.equals("pria")) {
                            isGender = "1";
                        } else if (isGender.equals("wanita")) {
                            isGender = "2";
                        }

                        String kelamin = JOptionUtil.initialValue("Kelamin : \n1. pria \n2. wanita", isGender);
                        try {
                            int kl = Integer.parseInt(kelamin);
                            if (kl == 1) {
                                member.setKelamin("pria");
                                break;
                            } else if (kl == 2) {
                                member.setKelamin("wanita");
                                break;
                            } else {
                                JOptionUtil.showMessage("Pilih jenis kelamin");
                            }
                        } catch (NumberFormatException ex) {
                            JOptionUtil.showMessage("Pilih jenis kelamin");
                        }
                    }
                    while (true) {
                        String telepon = JOptionUtil.initialValue("Telepon :", member.getTelepon());
                        try {
                            Long.parseLong(telepon);
                            member.setTelepon(telepon);
                            break;
                        } catch (NumberFormatException ex) {
                            JOptionUtil.showMessage("Masukan nomor telepon dengan benar");
                        }
                    }

                    memberService.update(id, member);
                } else {
                    JOptionUtil.showMessage("Kode member " + id + " tidak ditemukan");
                }
            } catch (NumberFormatException ex) {
                JOptionUtil.showMessage("Masukan kode member dengan benar");
            }
        }
    }

    public void removeMember() {
        String id = JOptionUtil.plainMessage(
                "Perpustakaan - Hapus member",
                "0. Kembali \nMasukan kode member untuk dihapus : "
        );

        if (id.equals("0")) {
            menu();
        } else {
            try {
                Integer.parseInt(id);
                boolean dataExist = memberService.dataExist(id);
                if (dataExist) {
                    int input = JOptionUtil.showConfirm("Kode member : " + id + "\nHapus member?");
                    if (input == 0) {
                        memberService.remove(id);
                    } else {
                        menu();
                    }
                } else {
                    JOptionUtil.showMessage("Kode member " + id + " tidak ditemukan");
                }
            } catch (Exception ex) {
                JOptionUtil.showMessage("Masukan kode member dengan benar");
            }
        }
    }

    public void goHome() {
        HomeView.mainMenu();
    }

}
