package model;

public class BookingRequest {

    private int bookingnumber;

    private String name;

    private int items;

    public BookingRequest() {
    }

    public BookingRequest(int bookingnumber, String name, int items) {
        this.bookingnumber = bookingnumber;
        this.name = name;
        this.items = items;
    }

    public int getBookingnumber() {
        return bookingnumber;
    }

    public void setBookingnumber(int bookingnumber) {
        this.bookingnumber = bookingnumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getItems() {
        return items;
    }

    public void setItems(int items) {
        this.items = items;
    }
}