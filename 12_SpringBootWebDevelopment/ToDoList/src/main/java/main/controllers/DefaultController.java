package main.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
public class DefaultController {

    @RequestMapping("/date/")
    public String string() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d.M.yyyy ~ HH:mm");
        return simpleDateFormat.format(new Date());
    }
}