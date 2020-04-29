package cn.zxJava.service.impl;

import cn.zxJava.domain.TbAddress;
import cn.zxJava.mapper.TbAddressMapper;
import cn.zxJava.service.AddressService;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 拓薪教育 -- 腾讯课程认证机构
 * 樱木老师
 */
@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private TbAddressMapper tbAddressMapper;

    @Override
    public List<TbAddress> findAddressList(String username) {
        return null;
    }
}
