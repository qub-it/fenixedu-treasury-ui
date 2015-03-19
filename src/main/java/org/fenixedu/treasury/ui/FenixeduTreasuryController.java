package org.fenixedu.treasury.ui;

import org.fenixedu.bennu.spring.portal.SpringApplication;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/fenixedu-treasury")
@SpringApplication(group = "logged", path = "fenixedu-treasury", title = "title.FenixeduTreasury")
@SpringFunctionality(app = FenixeduTreasuryController.class, title = "title.FenixeduTreasury")
public class FenixeduTreasuryController {

    @RequestMapping
    public String home(Model model) {
        model.addAttribute("world", "World");
        return "fenixedu-treasury/home";
    }

}
