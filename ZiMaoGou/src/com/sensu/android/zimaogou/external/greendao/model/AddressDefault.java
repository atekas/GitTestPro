package com.sensu.android.zimaogou.external.greendao.model;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table ADDRESS_DEFAULT.
 */
public class AddressDefault {

    private String id;
    private String name;
    private String mobile;
    private String id_card;
    private String province_id;
    private String city_id;
    private String district_id;
    private String province;
    private String city;
    private String district;
    private String address;
    private String is_default;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public AddressDefault() {
    }

    public AddressDefault(String id, String name, String mobile, String id_card, String province_id, String city_id, String district_id, String province, String city, String district, String address, String is_default) {
        this.id = id;
        this.name = name;
        this.mobile = mobile;
        this.id_card = id_card;
        this.province_id = province_id;
        this.city_id = city_id;
        this.district_id = district_id;
        this.province = province;
        this.city = city;
        this.district = district;
        this.address = address;
        this.is_default = is_default;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getId_card() {
        return id_card;
    }

    public void setId_card(String id_card) {
        this.id_card = id_card;
    }

    public String getProvince_id() {
        return province_id;
    }

    public void setProvince_id(String province_id) {
        this.province_id = province_id;
    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public String getDistrict_id() {
        return district_id;
    }

    public void setDistrict_id(String district_id) {
        this.district_id = district_id;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIs_default() {
        return is_default;
    }

    public void setIs_default(String is_default) {
        this.is_default = is_default;
    }

    // KEEP METHODS - put your custom methods here
    // KEEP METHODS END

}
