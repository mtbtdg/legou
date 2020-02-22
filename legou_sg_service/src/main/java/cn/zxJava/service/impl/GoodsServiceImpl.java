package cn.zxJava.service.impl;

import cn.zxJava.groupentity.Goods;
import cn.zxJava.service.GoodsService;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class GoodsServiceImpl implements GoodsService{

    @Override
    public void add(Goods goods) {

    }
}
