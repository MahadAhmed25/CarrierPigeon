package com.group12.carrierpigeon.components.contacts;

/**
 * Represents a contact with a name, phone number, and image.
 */
public class Contact {
    String name;
    String phoneNo;
    int image;

    public Contact(String name, String phoneNo, int image) {
        this.name = name;
        this.phoneNo = phoneNo;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public int getImage() {
        return image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
