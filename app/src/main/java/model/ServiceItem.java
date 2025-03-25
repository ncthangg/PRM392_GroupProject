package model;

public class ServiceItem {
    private String Id;
    private String Image;
    private String Name;
    private String Description;
    private int Price;
    private boolean Active;
    private Category Category;

    public ServiceItem(String image, String name, String description, int price, Category category) {
        this.Image = image;
        this.Name = name;
        this.Description = description;
        this.Price = price;
        this.Category = category;
        this.Active = true; // Mặc định là active khi tạo mới
    }

    public String getId() { return Id; }
    public String getImage() { return Image; }
    public String getName() { return Name; }
    public String getDescription() { return Description; }
    public int getPrice() { return Price; }
    public boolean isActive() { return Active; }
    public Category getCategory() { return Category; }
}
