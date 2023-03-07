package com.yjm.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AdminViewController {

    @RequestMapping("/aview/tttt")
    public String tttt(@RequestParam(value = "id")Integer id, Model model){
        model.addAttribute("id",id);
        return "/admin/resume";
    }

    @RequestMapping({"/aview/toadmin","/adm"})
    public String toAdmin(){
        return "/admin/login";
    }

    @RequestMapping("/aview/main")
    public String toMain(){
        return "/admin/index";
    }

    @RequestMapping("/admin/userinfo")
    public String userInfo(){
        return "/admin/userinfo";
    }

    @RequestMapping("/aview/address")
    public String updateAddress(@RequestParam(defaultValue = "aid")Integer aid,Model model){
        model.addAttribute("aid",aid);
    return "/admin/admin_updateAddress";
    }

    @RequestMapping("/aview/waitDeliver")
    public String waitdeliver(){
        return "/admin/admin_waitDeliver";
    }

    @RequestMapping("/aview/waitTake")
    public String waitTake(){
        return "/admin/admin_waitTake";
    }

    @RequestMapping("/aview/waitComment")
    public String waitComment(){
        return "/admin/admin_waitComment";
    }

}
