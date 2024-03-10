package com.example.producttesttask.ui.sorting

enum class SortType(val title: String) {
    NONE(""),
    SMARTPHONES("smartphones"),
    LAPTOPS("laptops"),
    FRAGRANCES("fragrances"),
    SKINCARE("skincare"),
    GROCERIES("groceries"),
    HOME_DECORATION("home-decoration"),
    FURNITURE("furniture"),
    TOPS("tops"),
    WOMEN_DRESSES("womens-dresses"),
    WOMEN_SHOES("womens-shoes"),
    MENS_SHIRTS("mens-shirts"),
    MENS_SHOES("mens-shoes"),
    MENS_WATCHES("mens-watches"),
    WOMEN_WATCHES("womens-watches"),
    WOMEN_BAGS("womens-bags"),
    WOMENS_JEWELLERY("womens-jewellery"),
    SUNGLASSES("sunglasses"),
    AUTOMOTIVE("automotive"),
    MOTORCYCLE("motorcycle"),
    LIGHTING("lighting");

    companion object {
        fun getSortTypes(): List<SortType> {
            return entries
        }
    }
}