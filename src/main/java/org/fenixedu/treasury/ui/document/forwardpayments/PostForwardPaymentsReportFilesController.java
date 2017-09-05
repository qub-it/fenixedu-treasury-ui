package org.fenixedu.treasury.ui.document.forwardpayments;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.fenixedu.bennu.spring.portal.BennuSpringController;
import org.fenixedu.treasury.domain.forwardpayments.PostForwardPaymentsReportFile;
import org.fenixedu.treasury.ui.TreasuryBaseController;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@BennuSpringController(value = ManageForwardPaymentsController.class)
@RequestMapping(PostForwardPaymentsReportFilesController.CONTROLLER_URL)
public class PostForwardPaymentsReportFilesController extends TreasuryBaseController {

    public static final String CONTROLLER_URL = "/treasury/document/forwardpayments/postforwardpaymentsreportfiles";
    private static final String JSP_PATH = "/treasury/document/forwardpayments/postforwardpaymentsreportfiles";

    
    @RequestMapping
    public String home() {
        return "redirect:" + SEARCH_URL;
    }

    public static final String SEARCH_URI = "/search";
    public static final String SEARCH_URL = CONTROLLER_URL + SEARCH_URI;

    @RequestMapping(value = SEARCH_URI, method = RequestMethod.GET)
    public String search(final Model model) {
        final Set<PostForwardPaymentsReportFile> filesSet = PostForwardPaymentsReportFile.findAll().collect(Collectors.toSet());
        
        model.addAttribute("postForwardPaymentsReportFilesSet", filesSet);
        
        return jspPage(SEARCH_URI);
    }    
    
    
    private static final String _DOWNLOAD_URI = "/download";
    public static final String DOWNLOAD_URL = CONTROLLER_URL + _DOWNLOAD_URI;

    @RequestMapping(value = _DOWNLOAD_URI + "/{fileId}", method = RequestMethod.GET)
    public void download(@PathVariable("fileId") final PostForwardPaymentsReportFile postForwardPaymentsReportFile,
            final HttpServletRequest request, final HttpServletResponse response, final Model model) {
        
        response.setContentLength(postForwardPaymentsReportFile.getContent().length);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-disposition", "attachment; filename=" + postForwardPaymentsReportFile.getFilename());

        try {
            response.getOutputStream().write(postForwardPaymentsReportFile.getContent());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        
    }
    
    private String jspPage(final String mapping) {
        return JSP_PATH + mapping;
    }

}
