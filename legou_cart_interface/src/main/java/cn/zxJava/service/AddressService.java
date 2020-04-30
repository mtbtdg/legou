package cn.zxJava.service;

import cn.zxJava.domain.TbAddress;

import java.util.List;


public interface AddressService {
    List<TbAddress> findAddressList(String username);
}
