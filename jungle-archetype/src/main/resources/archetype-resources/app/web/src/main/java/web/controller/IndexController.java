package ${groupId}.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 
 * @author allen
 * @version $Id: SampleController.java, v 0.1 2016年7月11日 下午10:12:13 allen Exp $
 */
@Controller
public class IndexController {

    @RequestMapping("/index")
    public String sample() {
        return "index";
    }
}
