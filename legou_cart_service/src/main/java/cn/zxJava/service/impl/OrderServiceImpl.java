package cn.zxJava.service.impl;

import cn.zxJava.domain.TbOrder;
import cn.zxJava.domain.TbOrderItem;
import cn.zxJava.groupentity.Cart;
import cn.zxJava.mapper.TbOrderItemMapper;
import cn.zxJava.mapper.TbOrderMapper;
import cn.zxJava.service.OrderService;
import cn.zxJava.utils.IdWorker;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private TbOrderMapper tbOrderMapper;

    @Autowired
    private TbOrderItemMapper tbOrderItemMapper;

    @Override
    public void saveOrder(TbOrder tbOrder) {

        String cartListStr = (String) redisTemplate.boundValueOps(tbOrder.getUserId()).get();
        List<Cart> cartList = JSON.parseArray(cartListStr, Cart.class);
        for (Cart cart : cartList) {
            TbOrder order = new TbOrder();
            //通过雪花算法生成唯一的订单Id
            long nextId = idWorker.nextId();
            order.setOrderId(nextId);
            //从已知数据中设置值
            //设置用户Id
            order.setUserId(tbOrder.getUserId());
            //设置支付方式
            order.setPaymentType(tbOrder.getPaymentType());
            //设置支付状态
            order.setStatus("1");
            //设置创建时间和更新时间
            order.setCreateTime(new Date());
            order.setUpdateTime(new Date());
            //设置地址
            order.setReceiverAreaName(tbOrder.getReceiverAreaName());
            //设置联系电话
            order.setReceiverMobile(tbOrder.getReceiverMobile());
            //设置收货人
            order.setReceiver(tbOrder.getReceiver());
            //设置订单来源
            order.setSourceType(tbOrder.getSourceType());
            //设置商家Id
            order.setSellerId(cart.getSellerId());

            //获取订单项集合
            List<TbOrderItem> orderItemList = cart.getOrderItemList();
            //定义总金额
            Double money = 0.0;
            //遍历
            for (TbOrderItem tbOrderItem : orderItemList) {
                //设置属性值
                tbOrderItem.setOrderId(nextId);
                tbOrderItem.setId(idWorker.nextId());
                //计算总金额
                money += tbOrderItem.getTotalFee().doubleValue();

                tbOrderItemMapper.insert(tbOrderItem);
            }
            //设置总金额
            order.setPayment(new BigDecimal(money));

            tbOrderMapper.insert(order);
        }

        //把Redis中的数据清除掉
        redisTemplate.delete(tbOrder.getUserId());
    }
}
