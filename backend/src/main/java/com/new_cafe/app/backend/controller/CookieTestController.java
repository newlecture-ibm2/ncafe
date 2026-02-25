package com.new_cafe.app.backend.controller;

import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/cookie")
public class CookieTestController {
    
    @GetMapping("/test")
    public String cookieTest(
            @CookieValue(name = "age", required = false) String age,
            HttpServletRequest request,
            Model model) { // controller

        //데이터(Model)를 만련하고
        model.addAttribute("name", "홍길동");
        model.addAttribute("age", age);

        HttpSession session = request.getSession();
        String sessionName = (String)session.getAttribute("name");
        Object sessionAge = session.getAttribute("age");

        model.addAttribute("sessionName", sessionName);
        model.addAttribute("sessionAge", sessionAge);

        return "test"; // view
    }







    @GetMapping("/create")
    public String cookieCreate(Model model) {
        return "create";
    }

    @PostMapping("/create")
    public String cookieCreate(String name, String value
            , HttpServletResponse response) {
        
        System.out.println("쿠키 생성 요청: " + name 
                            + "=" + value);
                            
        Cookie cookie = new Cookie(name, value);        
        cookie.setPath("/cookie");
        // cookie.setHttpOnly(true);        
        // cookie.setSecure(true);
        // cookie.setMaxAge(60 * 60 * 24 * 30); // 30일
        response.addCookie(cookie);


        return "redirect:/cookie/test";     
    }   

    @GetMapping("/session/create")
    public String sessionCreate(Model model) {
        return "create";
    }

    @PostMapping("/session/create")
    public String sessionCreate(String name, String value
            , HttpServletRequest request) {        

        HttpSession session = request.getSession();
        session.setAttribute(name, value);

        return "redirect:/cookie/test";     
    }   

}