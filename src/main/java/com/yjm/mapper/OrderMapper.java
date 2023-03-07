package com.yjm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yjm.entity.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
@Mapper
public interface OrderMapper extends BaseMapper<Orders> {
    //待评价--通过order_detailes中未评价【详情订单】查询其对应的【订单】
    @Select("SELECT * FROM orders WHERE STATUS = 2 AND id IN (\n" +
            "SELECT o_id FROM order_detailes WHERE is_comment = 0 AND u_id = #{id} AND o_id IN (  SELECT id FROM orders where u_id = #{id}) GROUP BY o_id)")
    List<Orders> findOrdersByDetailes(Integer id);

    @Select("SELECT * FROM order_detailes WHERE is_comment = 0  AND o_id IN(\n" +
            "\tSELECT id FROM orders WHERE STATUS = #{status} AND u_id = #{id}\n" +
            ")")
    List<Order_detailes> findDetaliesByStatus(Integer id,Integer status);

    //待评价--通过订单详情查询商品
    @Select("SELECT * FROM food f JOIN (\n" +
            "\tSELECT f_id FROM order_detailes od JOIN (\n" +
            "\t\tSELECT id FROM orders  WHERE STATUS = #{status} AND id IN (\n" +
            "\t\t\tSELECT o_id  FROM order_detailes  WHERE is_comment = 0 AND u_id = #{id} AND o_id IN (  SELECT id FROM orders where u_id = #{id} ) GROUP BY o_id\n" +
            "\t\t)\n" +
            "\t) o ON od.o_id = o.id \n" +
            ")ods ON f.id = ods.f_id")
    List<Food> findFoodByDetailes(Integer id,Integer status);

    @Select("SELECT COUNT(*) FROM order_detailes od JOIN (\n" +
            "\tSELECT id FROM orders WHERE STATUS = 2 AND id IN (\n" +
            "\t\tSELECT o_id FROM order_detailes WHERE is_comment = 0 AND u_id = #{id} AND o_id IN (  SELECT id FROM orders where u_id = #{id} ) GROUP BY o_id\n" +
            "\t\t)\n" +
            ") o ON od.o_id = o.id")
    Integer getCommentCount(Integer id);

    @Select("SELECT  STATUS,COUNT(*) num FROM orders WHERE u_id = #{id} GROUP BY STATUS")
    List<StatusCount> getStatusCount(Integer id);

    @Select("SELECT  STATUS,COUNT(*) num FROM orders WHERE u_id = #{id} GROUP BY STATUS")
    List getStatusCount1(Integer id);

}
