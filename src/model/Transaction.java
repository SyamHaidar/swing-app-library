package model;

public class Transaction {

    private Integer id;

    private String transactionId;
    private String bookId;
    private String memberId;
    private String tglPinjam;
    private String tglKembali;
    private String tglEstimasi;
    private String totalDenda;

    public Transaction(Integer id, String transactionId, String bookId, String memberId, String tglPinjam, String tglKembali, String tglEstimasi, String totalDenda) {
        this.id = id;
        this.transactionId = transactionId;
        this.bookId = bookId;
        this.memberId = memberId;
        this.tglPinjam = tglPinjam;
        this.tglKembali = tglKembali;
        this.tglEstimasi = tglEstimasi;
        this.totalDenda = totalDenda;
    }

    public Transaction(){}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getTglPinjam() {
        return tglPinjam;
    }

    public void setTglPinjam(String tglPinjam) {
        this.tglPinjam = tglPinjam;
    }

    public String getTglKembali() {
        return tglKembali;
    }

    public void setTglKembali(String tglKembali) {
        this.tglKembali = tglKembali;
    }

    public String getTglEstimasi() {
        return tglEstimasi;
    }

    public void setTglEstimasi(String tglEstimasi) {
        this.tglEstimasi = tglEstimasi;
    }

    public String getTotalDenda() {
        return totalDenda;
    }

    public void setTotalDenda(String totalDenda) {
        this.totalDenda = totalDenda;
    }
}
