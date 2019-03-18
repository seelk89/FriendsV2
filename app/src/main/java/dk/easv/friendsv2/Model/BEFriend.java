package dk.easv.friendsv2.Model;

public class BEFriend {

    private String m_name;
    private String m_phone;
    private Boolean m_isFavorite;

    private String m_imageStorageLocation;

    public BEFriend(String name, String phone) {
        this(name, phone, false);
    }

    public BEFriend(String name, String phone, Boolean isFavorite) {
        m_name = name;
        m_phone = phone;
        m_isFavorite = isFavorite;
    }

    public String getPhone() {
        return m_phone;
    }

    public void setPhone(String phone) {
        m_phone = phone;
    }

    public String getName() {
        return m_name;
    }

    public void setName(String name) {
        m_name = name;
    }

    public Boolean isFavorite() {
        return m_isFavorite;
    }

    public String getImageStoragelocation() {
        return m_imageStorageLocation;
    }

    public void setImageStoragelocation(String imageStorageLocation) {
        m_imageStorageLocation = imageStorageLocation;
    }
}
