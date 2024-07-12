package exemple.udemy.java.olx.model;

import com.google.firebase.database.DatabaseReference;

import java.util.List;

import exemple.udemy.java.olx.helper.SettingsFirebase;

public class Advert {

    private String idAdvert;
    private String state;
    private String category;
    private String title;
    private String description;
    private String price;
    private String phone;

    List<String> Photos;

    public Advert() {

        DatabaseReference advertReference = SettingsFirebase.getDatabaseReference()
                .child("my_adverts");
        setIdAdvert(advertReference.push().getKey());
    }

    public void saveMyAdvert(){

        String idUser = SettingsFirebase.getUserID();
        DatabaseReference advertReference = SettingsFirebase.getDatabaseReference()
                .child("my_adverts");

        advertReference.child( idUser )
                .child( getIdAdvert() )
                .setValue(this);
    }

    public void savePublicAdvert(){

        DatabaseReference advertReference = SettingsFirebase.getDatabaseReference()
                .child("adverts");

        advertReference.child( getState() )
                .child( getCategory() )
                .child( getIdAdvert() )
                .setValue(this);
    }


    public void deleteMyAdvert(){

        String idUser = SettingsFirebase.getUserID();
        DatabaseReference advertReference = SettingsFirebase.getDatabaseReference()
                .child("my_adverts")
                .child( idUser )
                .child( getIdAdvert() );
        advertReference.removeValue();
    }

    public void deletePublicAdvert(){

        DatabaseReference advertReference = SettingsFirebase.getDatabaseReference()
                .child("adverts")
                .child( getState() )
                .child( getCategory() )
                .child( getIdAdvert() );
        advertReference.removeValue();
    }



    public String getIdAdvert() {
        return idAdvert;
    }

    public void setIdAdvert(String idAdvert) {
        this.idAdvert = idAdvert;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<String> getPhotos() {
        return Photos;
    }

    public void setPhotos(List<String> photos) {
        Photos = photos;
    }
}
