package cn.zxJava.service.impl;

import cn.zxJava.groupentity.Cart;
import cn.zxJava.mapper.TbItemMapper;
import cn.zxJava.service.CartService;
import com.alibaba.dubbo.config.annotation.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * 拓薪教育 -- 腾讯课程认证机构
 * 樱木老师
 */
@Service
@Transactional
public class CartServiceImpl implements CartService {


}
