package com.codeoftheweb.salvo.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/")
@Controller
public class RedirectController {
    @RequestMapping(value="/")
    public String Welcome (){
        return "redirect:/web/games.html";
    }
}
