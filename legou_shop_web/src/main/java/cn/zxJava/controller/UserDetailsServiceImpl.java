package cn.zxJava.controller;

import cn.zxJava.domain.TbSeller;
import cn.zxJava.service.SellerService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    @Reference
    private SellerService sellerService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // 通过用户名查询数据
        TbSeller seller = sellerService.findOne(username);
        // 判断
        if(seller == null){
            // 把null数据返回给SpringSecurity框架，并且后台会出现异常
            return null;
        }
        // 必须是审核通过
        if(!seller.getStatus().equals("1")){
            return null;
        }

        // 存储的用户的角色
        List<SimpleGrantedAuthority> list = new ArrayList<>();
        list.add(new SimpleGrantedAuthority("ROLE_USER"));
        list.add(new SimpleGrantedAuthority("ROLE_SELLER"));

        // 给SpringSecurity框架返回数据
        User user = new User(username,seller.getPassword(),list);
        return user;

    }
}
