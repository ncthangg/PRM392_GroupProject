package model;

public class ServiceItem {
    private String Id;
    private String Image;
    private String Name;
    private String Description;
    private int Price;
    private boolean Active;
    private Category Category;

    public String getId() { return Id; }
    public String getImage() { return Image; }
    public String getName() { return Name; }
    public String getDescription() { return Description; }
    public int getPrice() { return Price; }
    public boolean isActive() { return Active; }
    public Category getCategory() { return Category; }
}
