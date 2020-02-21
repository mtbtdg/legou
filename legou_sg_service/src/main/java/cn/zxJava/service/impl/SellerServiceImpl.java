package cn.zxJava.service.impl;

import cn.zxJava.domain.TbSeller;
import cn.zxJava.domain.TbSellerExample;
import cn.zxJava.mapper.TbSellerMapper;
import cn.zxJava.service.SellerService;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 服务实现层
 * @author Administrator
 */
@Service
@Transactional
public class SellerServiceImpl implements SellerService {

	@Autowired
	private TbSellerMapper sellerMapper;

	/**
	 * 查询所有
	 * @return
	 */
	@Override
	public List<TbSeller> findAll() {
		return sellerMapper.selectByExample(null);
	}

	/**
	 * 分页查询
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@Override
	public PageInfo<TbSeller> findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		List<TbSeller> list = sellerMapper.selectByExample(null);
		return new PageInfo<>(list);
	}

	/**
	 * 保存
	 * @param seller
	 */
	@Override
	public void add(TbSeller seller) {
		//设置状态
		seller.setStatus("0");
		sellerMapper.insert(seller);
	}

	/**
	 * 修改
	 * @param seller
	 */
	@Override
	public void update(TbSeller seller) {
		sellerMapper.updateByPrimaryKey(seller);
	}

	/**
	 * 通过主键查询
	 * @param id
	 * @return
	 */
	@Override
	public TbSeller findOne(String id) {
		return sellerMapper.selectByPrimaryKey(id);
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(String[] ids) {
		for(String id:ids){
			sellerMapper.deleteByPrimaryKey(id);
		}		
	}

	/**
	 * 分页条件查询
	 * @param seller
	 * @param pageNum  当前页 码
	 * @param pageSize 每页记录数
	 * @return
	 */
	@Override
	public PageInfo<TbSeller> findPage(TbSeller seller, int pageNum, int pageSize) {
		//设置当前页和条数
		PageHelper.startPage(pageNum, pageSize);
		//拼接查询条件
		TbSellerExample example=new TbSellerExample();
		TbSellerExample.Criteria criteria = example.createCriteria();
		
		if(seller!=null){
			String status = seller.getStatus();
			if (status != null && !status.trim().isEmpty()){
				criteria.andStatusEqualTo(status);
			}

		}
		// 查询数据
		List<TbSeller> list = sellerMapper.selectByExample(example);
		return new PageInfo<>(list);
	}

	@Override
	public void auditing(String sellerId, String status) {
		TbSeller seller = sellerMapper.selectByPrimaryKey(sellerId);
		seller.setStatus(status);
		sellerMapper.updateByPrimaryKey(seller);
	}

}
