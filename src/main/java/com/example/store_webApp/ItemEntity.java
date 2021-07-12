package com.example.store_webApp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "inventory", schema = "public", catalog = "clothingStore")
public class ItemEntity {

    private long id;
    private String gender;
    private String style;
    private String season;
    private String patternType;
    private String color;
    private String size;
    private String material;
    private String description;
    private Integer quantity;
    private Integer numOfOrders;
    private BigDecimal price;
    private String imageName;
    private String type;

    private List<OrdersEntity> orders;

    public ItemEntity() {

    }

    public ItemEntity(ItemEntity item) {

        this.gender = item.gender;
        this.style = item.style;
        this.season = item.season;
        this.patternType = item.patternType;
        this.color = item.color;
        this.size = item.size;
        this.material = item.material;
        this.description = item.description;
        this.quantity = item.quantity;
        this.numOfOrders = item.numOfOrders;
        this.price = item.price;
        this.imageName = item.imageName;
        this.type = item.type;
    }

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    @Basic(optional = true)
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "gender", length = 30, nullable = true)
    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }

    @Basic
    @Column(name = "style", length = 30, nullable = true)
    public String getStyle() {
        return style;
    }
    public void setStyle(String style) {
        this.style = style;
    }

    @Basic
    @Column(name = "season", length = 30, nullable = true)
    public String getSeason() {
        return season;
    }
    public void setSeason(String season) {
        this.season = season;
    }

    @Basic
    @Column(name = "pattern_type", length = 100, nullable = true)
    public String getPatternType() {
        return patternType;
    }
    public void setPatternType(String paternType) {
        this.patternType = paternType;
    }

    @Basic
    @Column(name = "color", length = 30, nullable = true)
    public String getColor() {
        return color;
    }
    public void setColor(String color) {
        this.color = color;
    }

    @Basic
    @Column(name = "size", length = 30, nullable = true)
    public String getSize() {
        return size;
    }
    public void setSize(String size) {
        this.size = size;
    }

    @Basic
    @Column(name = "material", length = 30, nullable = true)
    public String getMaterial() {
        return material;
    }
    public void setMaterial(String material) {
        this.material = material;
    }

    @Basic
    @Column(name = "description", length = 255, nullable = true)
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    @Basic
    @Column(name = "quantity", nullable = true)
    public Integer getQuantity() {
        return quantity;
    }
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Basic
    @Column(name = "num_of_orders", nullable = true)
    public Integer getNumOfOrders() {
        return numOfOrders;
    }
    public void setNumOfOrders(Integer numOfOrders) {
        this.numOfOrders = numOfOrders;
    }

    @Basic
    @Column(name = "price", nullable = true, precision = 2)
    public BigDecimal getPrice() {
        return price;
    }
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Basic
    @Column(name = "image_name", nullable = true, length = 255)
    public String getImageName() {
        return imageName;
    }
    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    @Basic
    @Column(name = "type", nullable = true, length = 255)
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }


        @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ItemEntity that = (ItemEntity) o;

        if (gender != null ? !gender.equals(that.gender) : that.gender != null) return false;
        if (style != null ? !style.equals(that.style) : that.style != null) return false;
        if (season != null ? !season.equals(that.season) : that.season != null) return false;
        if (patternType != null ? !patternType.equals(that.patternType) : that.patternType != null) return false;
        if (color != null ? !color.equals(that.color) : that.color != null) return false;
        if (size != null ? !size.equals(that.size) : that.size != null) return false;
        if (material != null ? !material.equals(that.material) : that.material != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (gender != null ? gender.hashCode() : 0);
        result = 31 * result + (style != null ? style.hashCode() : 0);
        result = 31 * result + (season != null ? season.hashCode() : 0);
        result = 31 * result + (patternType != null ? patternType.hashCode() : 0);
        result = 31 * result + (color != null ? color.hashCode() : 0);
        result = 31 * result + (size != null ? size.hashCode() : 0);
        result = 31 * result + (material != null ? material.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (quantity != null ? quantity.hashCode() : 0);
        result = 31 * result + (numOfOrders != null ? numOfOrders.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        result = 31 * result + (imageName != null ? imageName.hashCode() : 0);
        return result;
    }
}
