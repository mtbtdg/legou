package cn.zxJava.service.impl;

import cn.zxJava.domain.TbOrder;
import cn.zxJava.domain.TbOrderItem;
import cn.zxJava.domain.TbPayLog;
import cn.zxJava.groupentity.Cart;
import cn.zxJava.mapper.TbOrderItemMapper;
import cn.zxJava.mapper.TbOrderMapper;
import cn.zxJava.mapper.TbPayLogMapper;
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

    @Autowired
    private TbPayLogMapper tbPayLogMapper;

    @Override
    public void saveOrder(TbOrder tbOrder) {

        String cartListStr = (String) redisTemplate.boundValueOps(tbOrder.getUserId()).get();
        List<Cart> cartList = JSON.parseArray(cartListStr, Cart.class);
        //预设所有订单主键的集合
        StringBuffer sb = new StringBuffer();
        //预设所有订单的总金额
        Double allOrderMoney = 0.0;
        for (Cart cart : cartList) {
            TbOrder order = new TbOrder();
            //通过雪花算法生成唯一的订单Id
            long orderId = idWorker.nextId();
            order.setOrderId(orderId);
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
                tbOrderItem.setOrderId(orderId);
                tbOrderItem.setId(idWorker.nextId());
                //计算总金额
                money += tbOrderItem.getTotalFee().doubleValue();

                tbOrderItemMapper.insert(tbOrderItem);
            }
            //设置总金额
            order.setPayment(new BigDecimal(money));

            //拼接所有订单的主键值,用","分隔开
            sb.append(orderId);
            sb.append(",");

            //累加所有订单的总金额
            allOrderMoney += order.getPayment().doubleValue();

            tbOrderMapper.insert(order);
        }

        //创建支付日志对象
        TbPayLog log = new TbPayLog();
        //创建时间
        log.setCreateTime(new Date());
        //创建订单编号
        log.setOutTradeNo(idWorker.nextId()+"");
        //设置用户ID
        log.setUserId(tbOrder.getUserId());
        //设置用户支付类型
        log.setPayType(tbOrder.getPaymentType());
        //设置是否支付,0为未支付,1为已支付
        log.setTradeState("0");
        //设置所有订单的主键值,使用一个字符串表示,用","分隔开
        log.setOrderList(sb.toString());
        //设置总金额，注意单位转换，需要以分为单位
        log.setTotalFee((long) (allOrderMoney * 100));
        //存入数据库
        tbPayLogMapper.insert(log);
        //存入redis
        redisTemplate.boundHashOps("pay_log").put(log.getUserId(),log);

        //把Redis中的数据清除掉
        redisTemplate.delete(tbOrder.getUserId());
    }
}
