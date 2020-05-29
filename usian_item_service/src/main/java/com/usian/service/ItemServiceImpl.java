package com.usian.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.usian.mapper.*;
import com.usian.pojo.*;
import com.usian.redis.RedisClient;
import com.usian.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ItemServiceImpl implements ItemService {

	@Autowired
	private TbItemMapper tbItemMapper;

	@Autowired
	private TbItemCatMapper tbItemCatMapper;

	@Autowired
	private TbItemParamMapper tbItemParamMapper;

	@Autowired
	private TbItemDescMapper tbItemDescMapper;

	@Autowired
	private TbItemParamItemMapper tbItemParamItemMapper;

	@Autowired
	private RedisClient redisClient;

	@Value("${PROTAL_CATRESULT_KEY}")
	private String PROTAL_CATRESULT_KEY;

	@Override
	public TbItem selectItemInfo(Long itemId) {
		return tbItemMapper.selectByPrimaryKey(itemId);
	}

	@Override
	public PageResult selectTbItemAllByPage(Integer page, Integer rows) {
		//分页查询
		PageHelper.startPage(page,rows);
		TbItemExample example = new TbItemExample();
		TbItemExample.Criteria criteria = example.createCriteria();
		criteria.andStatusEqualTo((byte)1);
		List<TbItem> list = tbItemMapper.selectByExample(example);
		PageInfo<TbItem> pageInfo = new PageInfo<TbItem>(list);
		//封装 结果集数据
		PageResult pageResult = new PageResult();
		pageResult.setPageIndex(page);
		pageResult.setTotalPage(pageInfo.getTotal());
		pageResult.setResult(list);

		return pageResult;
	}

	@Override
	public List<TbItemCat> selectItemCategoryByParentId(Long id) {
		TbItemCatExample example = new TbItemCatExample();
		TbItemCatExample.Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(id);
		criteria.andStatusEqualTo(1);
		List<TbItemCat> list = tbItemCatMapper.selectByExample(example);
		return list;
	}

	@Override
	public TbItemParam selectItemParamByItemCatId(Long itemCatId) {
		TbItemParamExample example = new TbItemParamExample();
		TbItemParamExample.Criteria criteria = example.createCriteria();
		criteria.andItemCatIdEqualTo(itemCatId);
		List<TbItemParam> list = tbItemParamMapper.selectByExampleWithBLOBs(example);
		if (list != null && list.size()>0){
			return list.get(0);
		}
		return null;
	}

	@Override
	public Integer insertTbItem(TbItem tbItem, String desc, String itemParams) {
		//补齐 Tbitem 数据
		Long itemId = IDUtils. genItemId ();
		Date date = new Date();
		tbItem.setId(itemId);
		tbItem.setStatus((byte)1);
		tbItem.setUpdated(date);
		tbItem.setCreated(date);
		Integer tbItemNum = tbItemMapper.insertSelective(tbItem);

		//补齐商品描述对象
		TbItemDesc tbItemDesc = new TbItemDesc();
		tbItemDesc.setItemId(itemId);
		tbItemDesc.setItemDesc(desc);
		tbItemDesc.setCreated(date);
		tbItemDesc.setUpdated(date);
		Integer tbitemDescNum = tbItemDescMapper.insertSelective(tbItemDesc);

		//补齐商品规格参数
		TbItemParamItem tbItemParamItem = new TbItemParamItem();
		tbItemParamItem.setItemId(itemId);
		tbItemParamItem.setParamData(itemParams);
		tbItemParamItem.setUpdated(date);
		tbItemParamItem.setCreated(date);
		Integer itemParamItmeNum = tbItemParamItemMapper.insertSelective(tbItemParamItem);

		return tbItemNum + tbitemDescNum + itemParamItmeNum;
	}

	@Override
	public Integer deleteItemById(Long itemId) {
		Integer num = tbItemMapper.deleteByPrimaryKey(itemId);
		return num;
	}

	@Override
	public ItemAll preUpdateItem(Long itemId) {
		//根据商品 ID 查询商品
		TbItem tbItem = tbItemMapper.selectByPrimaryKey(itemId);
		//根据商品 ID 查询商品类目
		TbItemCat tbItemCat = tbItemCatMapper.selectByPrimaryKey(tbItem.getCid());
		//根据商品 ID 查询商品描述
		TbItemDesc tbItemDesc = tbItemDescMapper.selectByPrimaryKey(itemId);
		//根据商品 ID 查询商品规格参数
		TbItemParamItemExample example = new TbItemParamItemExample();
		TbItemParamItemExample.Criteria criteria = example.createCriteria();
		criteria.andItemIdEqualTo(itemId);
		List<TbItemParamItem> list = tbItemParamItemMapper.selectByExampleWithBLOBs(example);


		ItemAll itemAll = new ItemAll();
		itemAll.setItem(tbItem);
		//判断非空，set数据，防止空指针
		Optional.ofNullable(tbItemCat).ifPresent(u-> itemAll.setItemCat(tbItemCat.getName()));
		Optional.ofNullable(tbItemDesc).ifPresent(u-> itemAll.setItemDesc(tbItemDesc.getItemDesc()));
		if (list != null && list.size() > 0) {
			itemAll.setItemParamItem(list.get(0).getParamData());
		}
		return itemAll;
	}

	@Override
	public Integer updateTbItem(TbItem tbItem, String desc, String itemParams) {
		//补齐 Tbitem 数据
		Date date = new Date();
		tbItem.setUpdated(date);
		int tbItemNum = tbItemMapper.updateByPrimaryKeySelective(tbItem);
		//补齐商品描述对象
		TbItemDesc itemDesc = new TbItemDesc();
		itemDesc.setItemId(tbItem.getId());
		itemDesc.setItemDesc(desc);
		itemDesc.setUpdated(date);
		int tbitemDescNum = tbItemDescMapper.updateByPrimaryKeySelective(itemDesc);
		//补齐商品规格参数
		TbItemParamItemExample example = new TbItemParamItemExample();
		TbItemParamItemExample.Criteria criteria = example.createCriteria();
		criteria.andItemIdEqualTo(tbItem.getId());
		List<TbItemParamItem> itemParamItems = tbItemParamItemMapper.selectByExampleWithBLOBs(example);

		TbItemParamItem tbItemParamItem = new TbItemParamItem();
		tbItemParamItem.setId(itemParamItems.get(0).getId());
		tbItemParamItem.setUpdated(date);
		tbItemParamItem.setItemId(tbItem.getId());
		tbItemParamItem.setParamData(itemParams);
		int itemParamItmeNum = tbItemParamItemMapper.updateByPrimaryKeySelective(tbItemParamItem);
		return tbItemNum+tbitemDescNum+itemParamItmeNum;
	}

	@Override
	public PageResult selectItemParamAll(Integer page, Integer rows) {
		PageHelper.startPage(page,rows);
		TbItemParamExample tbItemParamExample = new TbItemParamExample();
		tbItemParamExample.setOrderByClause("updated DESC");
		List<TbItemParam> tbItemParamList = tbItemParamMapper.selectByExampleWithBLOBs(tbItemParamExample);
		PageInfo<TbItemParam> pageInfo = new PageInfo<>(tbItemParamList);

		PageResult pageResult = new PageResult();
		pageResult.setTotalPage(Long.valueOf(pageInfo.getPages()));
		pageResult.setPageIndex(pageInfo.getPageNum());
		pageResult.setResult(pageInfo.getList());

		return pageResult;
	}

	@Override
	public Integer insertItemParam(Long itemCatId, String paramData) {

		TbItemParamExample tbItemParamExample = new TbItemParamExample();
		TbItemParamExample.Criteria criteria = tbItemParamExample.createCriteria();
		criteria.andItemCatIdEqualTo(itemCatId);
		List<TbItemParam> tbItemParamList = tbItemParamMapper.selectByExample(tbItemParamExample);
		if (tbItemParamList.size()>0){
			return 0;
		}

		Date date = new Date();
		TbItemParam tbItemParam = new TbItemParam();
		tbItemParam.setItemCatId(itemCatId);
		tbItemParam.setParamData(paramData);
		tbItemParam.setUpdated(date);
		tbItemParam.setCreated(date);
		Integer num = tbItemParamMapper.insertSelective(tbItemParam);

		return num;
	}

	@Override
	public Integer deleteItemParamById(Long id) {
		Integer num = tbItemParamMapper.deleteByPrimaryKey(id);
		return num;
	}

	@Override
	public CatResult selectItemCategoryAll() {
		//先查询Redis
		CatResult catResultRedis = (CatResult) redisClient.get(PROTAL_CATRESULT_KEY);
		if (catResultRedis!=null){
			//如果有数据，直接返回
			return catResultRedis;
		}

		//如果没有查数据库，并放到Redis中
		List<?> list = getCatList(0L);
		CatResult catResult = new CatResult();
		catResult.setData(list);

		redisClient.set(PROTAL_CATRESULT_KEY,catResult);

		return catResult;
	}

	private List<?> getCatList(Long parentId){
		TbItemCatExample tbItemCatExample = new TbItemCatExample();
		TbItemCatExample.Criteria criteria = tbItemCatExample.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		List<TbItemCat> tbItemCatList = tbItemCatMapper.selectByExample(tbItemCatExample);
		List list = new ArrayList<>(0);
		if(tbItemCatList.size()!=0) {
			int temp = tbItemCatList.size() > 18 ? 18 : tbItemCatList.size();
			for (int i = 0; i < temp; i++) {
				TbItemCat tbItemCat = tbItemCatList.get(i);
				if (tbItemCat.getIsParent()) {
					CatNode catNode = new CatNode();
					catNode.setName(tbItemCat.getName());
					list.add(catNode);
					catNode.setItem(getCatList(tbItemCat.getId()));
				}else {
					list.add(tbItemCat.getName());
				}
			}
		}
		return list;
	}

}
