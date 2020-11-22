package com.spring.Final.modules.page;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MyErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError() {
        return "client/modules/pages/error/error-page";
    }

    @Override
    public String getErrorPath() {
        return null;
    }
}
