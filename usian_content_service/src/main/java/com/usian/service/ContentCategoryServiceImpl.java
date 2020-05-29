package com.usian.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.usian.mapper.TbContentCategoryMapper;
import com.usian.mapper.TbContentMapper;
import com.usian.pojo.TbContent;
import com.usian.pojo.TbContentCategory;
import com.usian.pojo.TbContentCategoryExample;
import com.usian.pojo.TbContentExample;
import com.usian.redis.RedisClient;
import com.usian.utils.AdNode;
import com.usian.utils.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class ContentCategoryServiceImpl implements ContentCategoryService {

    @Value("${AD_CATEGORY_ID}")
    private Long AD_CATEGORY_ID;

    @Value("${AD_HEIGHT}")
    private Integer AD_HEIGHT;

    @Value("${AD_WIDTH}")
    private Integer AD_WIDTH;

    @Value("${AD_HEIGHTB}")
    private Integer AD_HEIGHTB;

    @Value("${AD_WIDTHB}")
    private Integer AD_WIDTHB;

    @Autowired
    private TbContentCategoryMapper tbContentCategoryMapper;

    @Autowired
    private TbContentMapper tbContentMapper;

    @Value("${PORTAL_AD_KEY}")
    private String PORTAL_AD_KEY;

    @Autowired
    private RedisClient redisClient;

    @Override
    public List<TbContentCategory> selectContentCategoryByParentId(Long id) {
        TbContentCategoryExample tbContentCategoryExample = new TbContentCategoryExample();
        TbContentCategoryExample.Criteria criteria = tbContentCategoryExample.createCriteria();
        criteria.andParentIdEqualTo(id);
        List<TbContentCategory> list = tbContentCategoryMapper.selectByExample(tbContentCategoryExample);
        return list;
    }

    @Override
    public Integer insertContentCategory(TbContentCategory tbContentCategory) {
        Integer num;
        //添加分类内容
        Date date = new Date();
        tbContentCategory.setSortOrder(1);
        tbContentCategory.setStatus(1);
        tbContentCategory.setIsParent(false);
        tbContentCategory.setCreated(date);
        tbContentCategory.setUpdated(date);
        num = tbContentCategoryMapper.insert(tbContentCategory);

        //查询父节点
        TbContentCategory tbContentCategory1 = tbContentCategoryMapper.selectByPrimaryKey(tbContentCategory.getParentId());
        if (!tbContentCategory1.getIsParent()){
            tbContentCategory1.setIsParent(true);
            tbContentCategory1.setUpdated(date);
            int num2 = tbContentCategoryMapper.updateByPrimaryKeySelective(tbContentCategory1);
            num += num2;
        }
        return num;
    }

    @Override
    public Integer deleteContentCategoryById(Long categoryId) {
        //判断父节点
        TbContentCategory tbContentCategory = tbContentCategoryMapper.selectByPrimaryKey(categoryId);
        if (tbContentCategory.getIsParent()){
            return 0;
        }

        //删除当前节点
        tbContentCategoryMapper.deleteByPrimaryKey(categoryId);

        //判断父节点中的子节点
        TbContentCategoryExample tbContentCategoryExample = new TbContentCategoryExample();
        TbContentCategoryExample.Criteria criteria = tbContentCategoryExample.createCriteria();
        criteria.andParentIdEqualTo(categoryId);
        List<TbContentCategory> list = tbContentCategoryMapper.selectByExample(tbContentCategoryExample);
        if (list == null || list.size() == 0){
            TbContentCategory contentCategory = new TbContentCategory();
            contentCategory.setId(tbContentCategory.getParentId());
            contentCategory.setIsParent(false);
            contentCategory.setUpdated(new Date());
            tbContentCategoryMapper.updateByPrimaryKeySelective(contentCategory);
        }
        return 200;
    }

    @Override
    public Integer updateContentCategory(TbContentCategory tbContentCategory) {
        tbContentCategory.setUpdated(new Date());
        Integer num = tbContentCategoryMapper.updateByPrimaryKeySelective(tbContentCategory);
        return num;
    }

    @Override
    public PageResult selectTbContentAllByCategoryId(Integer page,Integer rows,Long categoryId) {
        PageHelper.startPage(page,rows);

        TbContentExample tbContentExample = new TbContentExample();
        tbContentExample.setOrderByClause("updated DESC");
        TbContentExample.Criteria criteria = tbContentExample.createCriteria();
        criteria.andCategoryIdEqualTo(categoryId);
        List<TbContent> list = tbContentMapper.selectByExampleWithBLOBs(tbContentExample);

        PageInfo<TbContent> pageInfo = new PageInfo<>(list);

        PageResult pageResult = new PageResult();
        pageResult.setPageIndex(pageInfo.getPageNum());
        pageResult.setTotalPage(pageInfo.getTotal());
        pageResult.setResult(pageInfo.getList());
        return pageResult;
    }

    @Override
    public Integer insertTbContent(TbContent tbContent) {
        Date date = new Date();
        tbContent.setCreated(date);
        tbContent.setUpdated(date);
        Integer num = tbContentMapper.insert(tbContent);

        //缓存同步
        redisClient.hdel(PORTAL_AD_KEY,AD_CATEGORY_ID.toString());
        return num;
    }

    @Override
    public Integer deleteContentByIds(Long ids) {
        redisClient.hdel(PORTAL_AD_KEY,AD_CATEGORY_ID.toString());
        return tbContentMapper.deleteByPrimaryKey(ids);
    }

    @Override
    public List<AdNode> selectFrontendContentByAD() {
        //查询缓存
        List<AdNode> adNodeListRedis = (List<AdNode>)redisClient.hget(PORTAL_AD_KEY,AD_CATEGORY_ID.toString());
        if(adNodeListRedis!=null){
            return adNodeListRedis;
        }

        //查询数据库
        TbContentExample tbContentExample = new TbContentExample();
        TbContentExample.Criteria criteria = tbContentExample.createCriteria();
        criteria.andCategoryIdEqualTo(AD_CATEGORY_ID);
        List<TbContent> tbContentList = tbContentMapper.selectByExample(tbContentExample);
        List<AdNode> list = new ArrayList<>();
        for (TbContent tbContent : tbContentList){
            AdNode adNode = new AdNode();
            adNode.setSrc(tbContent.getPic());
            adNode.setSrcB(tbContent.getPic2());
            adNode.setHref(tbContent.getUrl());
            adNode.setHeight(AD_HEIGHT);
            adNode.setWidth(AD_WIDTH);
            adNode.setHeightB(AD_HEIGHTB);
            adNode.setWidthB(AD_WIDTHB);
            list.add(adNode);
        }

        //添加到缓存
        redisClient.hset(PORTAL_AD_KEY,AD_CATEGORY_ID.toString(),list);

        return list;
    }
}
