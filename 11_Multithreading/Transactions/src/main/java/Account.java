public class Account {

    private volatile long money;
    private volatile String accNumber;
    private volatile boolean isBlock = false;

    public Account() {
    }

    public Account(long money, String accNumber) {
        this.money = money;
        this.accNumber = accNumber;
    }

    public long getMoney() {
        return money;
    }

    public void setMoney(long money) {
        this.money = money;
    }

    public String getAccNumber() {
        return accNumber;
    }

    public void setAccNumber(String accNumber) {
        this.accNumber = accNumber;
    }

    public boolean getBlock() {
        return isBlock;
    }

    public void setBlock() {
        this.isBlock = true;
    }
}
