package cn.zxJava.service;

import cn.zxJava.groupentity.Cart;

import java.util.List;


public interface CartService {


    List<Cart> findCartList(String key);

    List<Cart> addCart(List<Cart> cartList, Long itemId, int num);

    void saveRedis(String key, List<Cart> cartList);

    List<Cart> merge(List<Cart> cartList_sessionId, List<Cart> cartList_username);

    void deleteSessionRedis(String sessionId);
}
