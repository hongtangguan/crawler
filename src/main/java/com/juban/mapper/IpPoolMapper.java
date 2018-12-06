package com.juban.mapper;

import com.juban.pojo.IpPool;
import org.apache.ibatis.annotations.Select;

public interface IpPoolMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(IpPool record);

    int insertSelective(IpPool record);

    IpPool selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(IpPool record);

    int updateByPrimaryKey(IpPool record);

    @Select("SELECT id,ip, port , ip_status ,create_time,update_time FROM ip_pool WHERE ip_status = 1 LIMIT 1")
    IpPool getAvailableIP();
}