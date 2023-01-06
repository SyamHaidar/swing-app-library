package model;

public class Member {

    private String id;
    private String nama;
    private String tglLahir;
    private String alamat;
    private String kelamin;
    private String telepon;

    public Member(String id, String nama, String tglLahir, String alamat, String kelamin, String telepon) {
        this.id = id;
        this.nama = nama;
        this.tglLahir = tglLahir;
        this.alamat = alamat;
        this.kelamin = kelamin;
        this.telepon = telepon;
    }

    public Member(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getTglLahir() {
        return tglLahir;
    }

    public void setTglLahir(String tglLahir) {
        this.tglLahir = tglLahir;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getKelamin() {
        return kelamin;
    }

    public void setKelamin(String kelamin) {
        this.kelamin = kelamin;
    }

    public String getTelepon() {
        return telepon;
    }

    public void setTelepon(String telepon) {
        this.telepon = telepon;
    }
}
