package com.example.libbbreriaapp;

import android.content.Context;
import android.content.Intent;

public class Config {
    public class ToolsApi{
        public static final String IMG = "https://francescoarzilli3g.altervista.org/libbbreria/api/tools/img/index.php?url=";
    }

    public class ProductApi{
        public static final String ALL = "https://francescoarzilli3g.altervista.org/libbbreria/api/product/index.php";
        public static final String LIBRI = "https://francescoarzilli3g.altervista.org/libbbreria/api/product/index.php?type=libro";
        public static final String CD = "https://francescoarzilli3g.altervista.org/libbbreria/api/product/index.php?type=cd";
        public static final String ID = "https://francescoarzilli3g.altervista.org/libbbreria/api/product/index.php?id=";
        public static final String SEARCH = "https://francescoarzilli3g.altervista.org/libbbreria/api/product/index.php?search=";
    }

    public class CartApi{
        public static final String CART = "https://francescoarzilli3g.altervista.org/libbbreria/api/product/cart.php?cart";
        public static final String ADD = "https://francescoarzilli3g.altervista.org/libbbreria/api/product/cart.php?add=";
        public static final String RM = "https://francescoarzilli3g.altervista.org/libbbreria/api/product/cart.php?rm=";
        public static final String DEL = "https://francescoarzilli3g.altervista.org/libbbreria/api/product/cart.php?del=";
        public static final String CLEAR = "https://francescoarzilli3g.altervista.org/libbbreria/api/product/cart.php?svuota";
        public static final String BUY = "https://francescoarzilli3g.altervista.org/libbbreria/api/product/cart.php?ordina";
    }

    public class UserApi{
        public static final String ISLOGGED = "https://francescoarzilli3g.altervista.org/libbbreria/api/user/index.php?logged";
        public static final String LOGOUT = "https://francescoarzilli3g.altervista.org/libbbreria/api/user/index.php?logout";
        public static final String LOGIN = "https://francescoarzilli3g.altervista.org/libbbreria/api/user/index.php";
        public static final String REGISTER = "https://francescoarzilli3g.altervista.org/libbbreria/api/user/index.php";
    }

    public static class Pages {
        public static final Class LIBRI_LIST = LibriList.class;
        public static final Class CD_LIST = CdList.class;
        public static final Class FIND_LIST = FindList.class;
        public static final Class CART_LIST = CartList.class;
        public static final Class LOGIN = Login.class;
        public static final Class LOGOUT = LogOut.class;
        public static final Class ERROR = ErrorPage.class;
    }
}
