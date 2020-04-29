package cn.zxJava.service;

import cn.zxJava.domain.TbAddress;

import java.util.List;

/**
 * 拓薪教育 -- 腾讯课程认证机构
 * 樱木老师
 */
public interface AddressService {
    List<TbAddress> findAddressList(String username);
}
