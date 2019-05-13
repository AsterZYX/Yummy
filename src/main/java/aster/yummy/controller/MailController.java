package aster.yummy.controller;

import aster.yummy.enums.ResultMessage;
import aster.yummy.service.MailService;
import aster.yummy.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Random;

@RestController
@RequestMapping(value = "/mail")
public class MailController {

    @Autowired
    private MailService mailService;

    @GetMapping("")
    public ResultVO<String> getCheckCode(@RequestParam(value = "email", required = true) String email,
                                         HttpServletRequest request){
        String checkCode = String.valueOf(new Random().nextInt(899999) + 100000);
        String message = "感谢您的注册！您的注册验证码为："+checkCode;
        try {
            mailService.sendSimpleMail(email, "欢迎使用Yummy平台", message);
            HttpSession session = request.getSession();
            session.setAttribute("mailCode", checkCode);
        }catch (Exception e){
            return new ResultVO<>(ResultMessage.FAILED.getCode(), ResultMessage.FAILED.getValue(), "");
        }
        return new ResultVO<>(ResultMessage.SUCCESS.getCode(), ResultMessage.SUCCESS.getValue(), "");
    }

    @GetMapping("/check")
    public ResultVO<Boolean> checkMailCode(@RequestParam(value = "mailCode", required = true) String mailCode,
                                           HttpServletRequest request){
        HttpSession session = request.getSession();
        String code = (String) session.getAttribute("mailCode");
        return new ResultVO<>(ResultMessage.SUCCESS.getCode(), ResultMessage.SUCCESS.getValue(), mailCode.equals(code));
    }

}
