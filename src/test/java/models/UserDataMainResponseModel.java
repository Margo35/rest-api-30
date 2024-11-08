package models;

import lombok.Data;

import java.util.List;

@Data
public class UserDataMainResponseModel {
    int page;
    int per_page;
    int total;
    int total_pages;
    List<UserDataModel> data;
    SupportModel support;

}
