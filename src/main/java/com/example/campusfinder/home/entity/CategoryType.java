package com.example.campusfinder.home.entity;

/**
 * packageName    : com.example.campusfinder.home.entity
 * fileName       : CategoryType
 * author         : tlswl
 * date           : 2024-09-30
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-09-30        tlswl       최초 생성
 */
public enum CategoryType {
    DESIGN("디자인"),
    DOCUMENT("문서작성"),
    PROGRAMMING("개발"),
    VIDEO("영상"),
    LANGUAGE("외국어"),
    ETC("기타");

    private final String categoryName;

    CategoryType(String categoryName){
        this.categoryName=categoryName;
    }

    public String getCategoryName(){
        return categoryName;
    }
}
