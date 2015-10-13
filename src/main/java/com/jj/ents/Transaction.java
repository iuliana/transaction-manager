package com.jj.ents;

import java.io.Serializable;

/**
 * Created by iuliana.cosmina on 10/13/15.
 */
public class Transaction implements Serializable {

    private Long id;
    private Double amount;
    private String type;
    private Long parentId;

    public Transaction(){
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Transaction that = (Transaction) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (amount != null ? !amount.equals(that.amount) : that.amount != null) return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;
        return !(parentId != null ? !parentId.equals(that.parentId) : that.parentId != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (amount != null ? amount.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (parentId != null ? parentId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "\nTransaction {" +
                "\n\tid=" + id +
                "\n\t, amount=" + amount +
                "\n\t, type='" + type + '\'' +
                "\n\t, parentId=" + parentId +
                "\n}";
    }
}
