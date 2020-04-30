package cn.zxJava.service.impl;

import cn.zxJava.domain.TbItem;
import cn.zxJava.domain.TbOrderItem;
import cn.zxJava.groupentity.Cart;
import cn.zxJava.mapper.TbItemMapper;
import cn.zxJava.service.CartService;
import com.alibaba.dubbo.config.annotation.Service;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class CartServiceImpl implements CartService {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private TbItemMapper tbItemMapper;

    /*
    * 查询全部数据
    * */
    @Override
    public List<Cart> findCartList(String key) {

        String cartListStr = (String) redisTemplate.boundValueOps(key).get();
        //判断是否为空或为null
        if (cartListStr == null || cartListStr.isEmpty()){
            //给catListStr赋值空数组
            cartListStr = "[]";
        }
        //解析成集合
        List<Cart> cartList = JSON.parseArray(cartListStr, Cart.class);
        return cartList;
    }

    /*
    * 把追加的商品存入cartList集合中
    * */
    @Override
    public List<Cart> addCart(List<Cart> cartList, Long itemId, int num) {
        TbItem tbItem = tbItemMapper.selectByPrimaryKey(itemId);
        String sellerId = tbItem.getSellerId();
        String sellerName = tbItem.getSeller();

        //判断原集合中是否包含该商家
        Cart cart = sellerInCartList(cartList,sellerId);

        if (cart != null) {
            //如果存在,则再判断订单项是否存在
            List<TbOrderItem> orderItemList = cart.getOrderItemList();
            TbOrderItem tbOrderItem = itemInOrderItemList(orderItemList,itemId);
            if (tbOrderItem != null) {
                //如果存在,则只改变订单项的数量和总价格就可以了
                //设置总数量
                tbOrderItem.setNum(tbOrderItem.getNum() + num);

                //判断商品数量是否为0,为0则删除这个订单项
                if (tbOrderItem.getNum() <= 0){
                    orderItemList.remove(tbOrderItem);
                    //如果订单项删除后orderItemList的长度也为0了,则删除这个cart
                    if (orderItemList.size() <= 0){
                        cartList.remove(cart);
                    }
                }

                //设置总金额
                tbOrderItem.setTotalFee(new BigDecimal(tbOrderItem.getTotalFee().doubleValue() + tbItem.getPrice().doubleValue()*num));
            }else {
                //如果不存在,则创建新的订单

                tbOrderItem = new TbOrderItem();
                //设置总数量
                tbOrderItem.setNum(num);
                //设置总金额
                tbOrderItem.setTotalFee(new BigDecimal(tbItem.getPrice().doubleValue()*num));
                //设置sku的数据
                tbOrderItem.setGoodsId(tbItem.getGoodsId());
                tbOrderItem.setPrice(tbItem.getPrice());
                tbOrderItem.setItemId(itemId);
                tbOrderItem.setPicPath(tbItem.getImage());
                tbOrderItem.setSellerId(sellerId);
                tbOrderItem.setTitle(tbItem.getTitle());
                //存入订单项集合
                orderItemList.add(tbOrderItem);
            }


        }else {
            //如果不存在，则创建新的购物车
            cart = new Cart();
            cart.setSellerId(sellerId);
            cart.setSellerName(sellerName);
            //创建订单项集合
            List<TbOrderItem> orderItemList = new ArrayList<>();

            TbOrderItem tbOrderItem = new TbOrderItem();
            //设置总数量
            tbOrderItem.setNum(num);
            //设置总金额
            tbOrderItem.setTotalFee(new BigDecimal(tbItem.getPrice().doubleValue()*num));
            //设置sku的数据
            tbOrderItem.setGoodsId(tbItem.getGoodsId());
            tbOrderItem.setPrice(tbItem.getPrice());
            tbOrderItem.setItemId(itemId);
            tbOrderItem.setPicPath(tbItem.getImage());
            tbOrderItem.setSellerId(sellerId);
            tbOrderItem.setTitle(tbItem.getTitle());
            //存入订单项集合
            orderItemList.add(tbOrderItem);

            cart.setOrderItemList(orderItemList);

            cartList.add(cart);

        }

        return cartList;
    }

    /*
    * 把商品存入购物车
    * */
    @Override
    public void saveRedis(String key, List<Cart> cartList) {
        String cartListStr = JSON.toJSONString(cartList);
        redisTemplate.boundValueOps(key).set(cartListStr,3, TimeUnit.DAYS);
    }

    /*
    * 把未登录状态下的数据转移至已登录用户的数据下
    * */
    @Override
    public List<Cart> merge(List<Cart> cartList_sessionId, List<Cart> cartList_username) {
        for (Cart cart : cartList_sessionId) {
            for (TbOrderItem tbOrderItem : cart.getOrderItemList()) {
                addCart(cartList_username,tbOrderItem.getItemId(),tbOrderItem.getNum());
            }
        }
        return cartList_username;
    }

    /*
    * 删除redis中的数据
    * */
    @Override
    public void deleteSessionRedis(String sessionId) {
        redisTemplate.delete(sessionId);
    }

    /*
    * 查询cartList集合中的商家
    * */
    private Cart sellerInCartList(List<Cart> cartList, String sellerId) {
        for (Cart cart : cartList) {
            if (cart.getSellerId().equals(sellerId)){
                return cart;
            }
        }
        return null;
    }

    /*
    * 查询orderItemList中的订单项
    * */
    private TbOrderItem itemInOrderItemList(List<TbOrderItem> orderItemList, Long itemId) {
        for (TbOrderItem orderItem : orderItemList) {
            if (orderItem.getItemId().longValue() == itemId.longValue()){
                return orderItem;
            }
        }
        return null;
    }
}
