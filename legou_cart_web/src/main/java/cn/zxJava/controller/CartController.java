package cn.zxJava.controller;

import cn.zxJava.entity.Result;
import cn.zxJava.groupentity.Cart;
import cn.zxJava.service.CartService;
import cn.zxJava.utils.CookieUtil;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;


/**
 * 拓薪教育 -- 腾讯课程认证机构
 * 樱木老师
 */
@RestController
@RequestMapping("/cart")
public class CartController {

    @Reference
    private CartService cartService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpSession session;

    @Autowired
    private HttpServletResponse response;

    /*
    * 未登录时使用sessionId做为key，把订单项集合转换为json格式的字符串做为value
    * */
    @RequestMapping("/findCartList")
    public Result findCartList() {
        try {
            //从cookie中获取sessionId
            String sessionId = getSessionId();
            //从redis中查询数据
            List<Cart> cartList_sessionId = cartService.findCartList(sessionId);
            //获取用户名
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            //判断是否登录
            if (username.equals("anonymousUser")){
                //返回未登录的数据
                return new Result(true,"查询成功",cartList_sessionId);
            }
            //如果登录,则查询用户购物车的数据
            List<Cart> cartList_username = cartService.findCartList(username);
            //对未登录的数据和用户自身的数据做整合
            if (cartList_sessionId != null && cartList_sessionId.size() > 0){
                //数据转移
                cartList_username = cartService.merge(cartList_sessionId,cartList_username);
                //清除redis中未登录的数据
                cartService.deleteSessionRedis(sessionId);
                cartService.saveRedis(username,cartList_username);
            }

            return new Result(true,"查询成功",cartList_username);

        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"查询失败");
        }
    }

    /*
    * 添加购物车
    * */
    @RequestMapping("/addCart/{itemId}/{num}")
    //解决跨域问题的注解  @CrossOrigin(value = {"value1","value2","value3"})
    @CrossOrigin("http://localhost:8086")
    public Result addCart(@PathVariable("itemId") Long itemId, @PathVariable("num") int num) {
        try {
            //从cookie中获取sessionId
            //默认为未登录用户
            String key = getSessionId();
            //获取用户名
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            //判断是否登录,切换key值
            if (!username.equals("anonymousUser")){
                key = username;
            }
            //从redis中查询数据
            List<Cart> cartList = cartService.findCartList(key);
            //将新加入的商品存入集合中
            cartList = cartService.addCart(cartList,itemId,num);
            //存入redis
            cartService.saveRedis(key,cartList);

            return new Result(true,"添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"添加失败");
        }
    }

    /*
    * 从cookie中获取sessionId
    * */
    public String getSessionId() {
        String value = CookieUtil.getCookieValue(request, "legouSessionId", "UTF-8");
        //判断是否是第一次访问
        if (value == null){
            value = session.getId();
            CookieUtil.setCookie(request,response,"legouSessionId",value,72*60*60,"UTF-8");
        }
        return value;
    }
}
