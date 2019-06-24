package cn.edu.swufe.eatingbar;

public class CarItem {
    private int number;
    private String name;
    private float price;
    private String intro;
    byte[] photo;
    private int count;
    private  String logname;
    public CarItem() {
        super();
        // TODO Auto-generated constructor stub
    }


    public String getLogname() {
        return logname;
    }

    public void setLogname(String logname) {
        this.logname = logname;
    }

    public CarItem(int number, String name, float price, String intro, byte[] photo, int count, String logname) {
        super();
        this.number = number;
        this.name = name;
        this.price = price;
        this.intro = intro;
        this.photo = photo;
        this.count = count;

        this.logname = logname;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "Product [number=" + number + ", name="
                + name + ", price=" + price + ", intro=" + intro +", photo=" + photo + ", count" + count +"]";
    }

}

