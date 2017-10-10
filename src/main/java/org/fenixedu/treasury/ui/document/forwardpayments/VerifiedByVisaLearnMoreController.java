package org.fenixedu.treasury.ui.document.forwardpayments;

import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.fenixedu.treasury.ui.TreasuryBaseController;
import org.fenixedu.treasury.ui.TreasuryController;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@SpringFunctionality(app = TreasuryController.class, title = "label.title.forwardpayment.verifiedbyvisalearnmore", accessGroup = "logged")
@RequestMapping(VerifiedByVisaLearnMoreController.CONTROLLER_URI)
public class VerifiedByVisaLearnMoreController extends TreasuryBaseController {
    public static final String CONTROLLER_URI = "/treasury/document/forwardpayments/verifiedbyvisalearnmore";
    private static final String JSP_PATH = "/treasury/document/forwardpayments/forwardpayment";
    
    private static final String VERIFIED_BY_VISA_LEARN_MORE_URI = "/verifiedbyvisalearnmore";
    
    @RequestMapping
    public String home(Model model) {
        return jspPage(VERIFIED_BY_VISA_LEARN_MORE_URI);
    }
    
    private String jspPage(final String page) {
        return JSP_PATH + page;
    }

}
