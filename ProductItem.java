package cn.edu.swufe.eatingbar;

public class ProductItem {

    private int number;
    private String name;
    private String price;
    private String intro;
    byte[] photo;
    private String type;
    public ProductItem() {
        super();
        // TODO Auto-generated constructor stub
    }


    public ProductItem(int number, String name, String price, String intro, byte[] photo, String type) {
        super();
        this.number = number;
        this.name = name;
        this.price = price;
        this.intro = intro;
        this.photo = photo;
        this.type = type;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Product [number=" + number + ", name="
                + name + ", price=" + price + ", intro=" + intro +", photo=" + photo + ", type" + type +"]";
    }

}

