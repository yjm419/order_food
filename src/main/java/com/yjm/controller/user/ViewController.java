package com.yjm.controller.user;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class ViewController {
    //测试
    @RequestMapping("/view/mytest")
    public String mytest(@RequestParam(value = "sId")Integer sId,
                         @RequestParam(value = "fId")Integer fId,
                         Model model){
        model.addAttribute("sId",sId);
        model.addAttribute("fId",fId);
        return "waitPay_single";
    }

    //去主页
    @RequestMapping({"/view/index", "/"})
    public String toIndex() {
        return "/visitor/store_index";
    }

    //去商店详情
    @RequestMapping("/view/store_details")
    public String toStore_details(@RequestParam(value = "sId") Integer sId, Model model) {
        model.addAttribute("sId", sId);
        return "/visitor/store_details";
    }

    //去商品详情
    @RequestMapping("/view/food_details")
    public String toFood_details(@RequestParam(value = "id") Integer id,@RequestParam(value = "sId")Integer sId, Model model) {
        model.addAttribute("id", id);
        model.addAttribute("sId", sId);
        return "/visitor/food_details";
    }

    //去用户登录
    @RequestMapping("/view/tologin")
    public String tologin() {
        return "/user/login";
    }

    //用户注册
    @RequestMapping("/view/register")
    public String register(){
        return "/user/register";
    }
    //去个人信息
    @RequestMapping("/view/myinfo")
    public String myinfo() {
        return "/user/myinfo";
    }

    //退出
    @RequestMapping("/view/exit")
    public String exit(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.invalidate();
        return "/visitor/store_index";
    }

    //去支付
    @RequestMapping("/user/waitpay")
    public String waitpay(@RequestParam(value = "sId")Integer sId,
                          @RequestParam(value = "fId")Integer fId,
                          Model model){
        model.addAttribute("sId",sId);
        model.addAttribute("fId",fId);
        return "waitPay_single";
    }

    //去购物车
    @RequestMapping("/shopcar")
    public String toShopCar(){
        return "/user/shoping_car";
    }

    //去提交订单
    @RequestMapping("/user/submit")
    public String toSubmit(
            @RequestParam(value = "fId")Integer fId,
                           Model model){
        model.addAttribute("fId",fId);

        return "/user/submit_details_single";
    }
    //去单独购买选择地址的页面
    @RequestMapping("/user/address")
    public String address(@RequestParam(value = "fId")Integer fId,Model model){
        model.addAttribute("fId",fId);
        return "/user/address";
    }
    //去购物车购买选择地址的页面
    @RequestMapping("/user/addresses")
    public String addresses(){
        return "/user/address";
    }

    //个人中心去管理地址的页面
    @RequestMapping("/user/infoAddress")
    public String infoAddress(){
        return "/user/address_myinfo";
    }
    //个人中心修改地址
    @RequestMapping("/user/infoUpdateAddress")
    public String infoUpdateAddress(){
        return "/user/addressUpdate_myinfo";
    }
//    个人中心添加地址
    @RequestMapping("/user/infoAddAddress")
    public  String infoAddAddress(){
        return "/user/addressAdd_myinfo";
    }
//提交订单页面--单独购买
    @RequestMapping("/user/chooseaddre")
    public String chooseaddre( @RequestParam(value = "fId")Integer fId,Model model){
        model.addAttribute("fId",fId);
        return "/user/submit_details_single";
    }
//提交订单页面--购物车购买
    @RequestMapping("/user/chooseaddres")
    public String chooseaddres(){
        return "/user/submit_details_multiple";
    }


//单独购买  支付页面
    @RequestMapping("/user/pay")
    public String toPay(@RequestParam(value = "key")String key,
                        @RequestParam(value = "aid") Integer aid,
                        Model model){
        model.addAttribute("key", key);
        model.addAttribute("aid",aid);
        return "/user/wait-pay";
    }
//购物车购买 支付页面
    @RequestMapping("/user/pays")
    public String toPays(@RequestParam(value = "key")String key,@RequestParam(value = "aid") Integer aid,Model model){
        model.addAttribute("key",key);
        model.addAttribute("aid",aid);
        return "/user/wait-pays";
    }
//单独购买去添加地址
    @RequestMapping("/user/addAddress")
    public String addAddress(@RequestParam(value = "fId")Integer fId,Model model){
        model.addAttribute("fId",fId);
        return "/user/address_add";
    }
//购物车购买去添加地址
    @RequestMapping("/user/addAddressCar")
    public String addAddressCar(){
        return "/user/address_add";
    }
//个人信息查看待付款信息
    @RequestMapping("/user/checkWaitPay")
    public String checkWaitPay(){
        return "/user/myinfo_waitpay";
    }
    //个人信息查看待发货信息
    @RequestMapping("/user/checkwaitdeliver")
    public String checkwaitdeliver(){
        return "/user/myinfo_waitdeliver";
    }
    //个人中心查看待收货信息
    @RequestMapping("/user/checkwaittake")
    public String checkwaittake(){
        return "/user/myinfo_waitTake";
    }
    //个人中心查看待评价
    @RequestMapping("/user/checkwaitcomment")
    public String checkwaitcomment(){
        return "/user/myinfo_waitcomment";
    }
    //评价订单详情1
    @RequestMapping("/user/orderdetails")
    public String orderdetails(){
        return "/user/order_details";
    }
    //评价订单详情2
    @RequestMapping("/user/aorderdetails")
    public String aorderdetails(){
        return "/user/aorder_details";
    }

    //评价页面
    @RequestMapping("/user/comment")
    public String comment(){
        return "/user/comment";
    }


}
