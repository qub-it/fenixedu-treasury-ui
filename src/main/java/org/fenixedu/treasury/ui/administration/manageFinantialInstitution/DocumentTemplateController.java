/**
 * This file was created by Quorum Born IT <http://www.qub-it.com/> and its 
 * copyright terms are bind to the legal agreement regulating the FenixEdu@ULisboa 
 * software development project between Quorum Born IT and ServiÃ§os Partilhados da
 * Universidade de Lisboa:
 *  - Copyright Â© 2015 Quorum Born IT (until any Go-Live phase)
 *  - Copyright Â© 2015 Universidade de Lisboa (after any Go-Live phase)
 *
 * Contributors: xpto@qub-it.com
 *
 * 
 * This file is part of FenixEdu Treasury.
 *
 * FenixEdu Treasury is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Treasury is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Treasury.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.treasury.ui.administration.manageFinantialInstitution;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletResponse;

import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.spring.portal.BennuSpringController;
import org.fenixedu.commons.StringNormalizer;
import org.fenixedu.treasury.domain.FinantialEntity;
import org.fenixedu.treasury.domain.document.DocumentTemplate;
import org.fenixedu.treasury.domain.document.DocumentTemplateFile;
import org.fenixedu.treasury.domain.document.FinantialDocumentType;
import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;
import org.fenixedu.treasury.ui.TreasuryBaseController;
import org.fenixedu.treasury.util.Constants;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import pt.ist.fenixframework.Atomic;

//@Component("org.fenixedu.treasury.ui.administration.manageFinantialInstitution") <-- Use for duplicate controller name disambiguation
//@SpringFunctionality(app = TreasuryController.class, title = "label.title.administration.manageFinantialInstitution",accessGroup = "anyone")// CHANGE_ME accessGroup = "group1 | group2 | groupXPTO"
//or
@BennuSpringController(value = FinantialInstitutionController.class)
@RequestMapping("/treasury/administration/managefinantialinstitution/documenttemplate")
public class DocumentTemplateController extends TreasuryBaseController {

    @RequestMapping
    public String home(Model model) {
        //this is the default behaviour, for handling in a Spring Functionality
        if (model.containsAttribute("finantialInstitutionId")) {
            return "forward:/treasury/administration/managefinantialinstitution/finantialinstitution/read/"
                    + model.asMap().get("finantialInstitutionId");
        }
        return "forward:/treasury/administration/managefinantialinstitution/finantialinstitution/";
    }

    private DocumentTemplate getDocumentTemplate(Model model) {
        return (DocumentTemplate) model.asMap().get("documentTemplate");
    }

    private void setDocumentTemplate(DocumentTemplate documentTemplate, Model model) {
        model.addAttribute("documentTemplate", documentTemplate);
    }

    @Atomic
    public void deleteDocumentTemplate(DocumentTemplate documentTemplate) {
        documentTemplate.delete();
    }

    @RequestMapping(value = "/search/view/{oid}")
    public String processSearchToViewAction(@PathVariable("oid") DocumentTemplate documentTemplate, Model model,
            RedirectAttributes redirectAttributes) {
        return redirect(
                "/treasury/administration/managefinantialinstitution/documenttemplate/read" + "/"
                        + documentTemplate.getExternalId(), model, redirectAttributes);
    }

    @RequestMapping(value = "/read/{oid}")
    public String read(@PathVariable("oid") DocumentTemplate documentTemplate, Model model) {
        setDocumentTemplate(documentTemplate, model);
        return "treasury/administration/managefinantialinstitution/documenttemplate/read";
    }

    @RequestMapping(value = "/delete/{oid}", method = RequestMethod.POST)
    public String delete(@PathVariable("oid") DocumentTemplate documentTemplate, Model model,
            RedirectAttributes redirectAttributes) {
        setDocumentTemplate(documentTemplate, model);
        try {
            deleteDocumentTemplate(documentTemplate);
            addInfoMessage(BundleUtil.getString(Constants.BUNDLE, "label.success.delete"), model);
            return redirect("/treasury/administration/managefinantialinstitution/documenttemplate/", model, redirectAttributes);
        } catch (TreasuryDomainException tde) {
            //Add error messages to the list
            addErrorMessage("Error deleting the DocumentTemplate due to " + tde.getLocalizedMessage(), model);
        }
        return "treasury/administration/managefinantialinstitution/documenttemplate/read/"
                + getDocumentTemplate(model).getExternalId();
    }

    @RequestMapping(value = "/create")
    public String create(
            @RequestParam(value = "finantialdocumenttypeid", required = true) FinantialDocumentType finantialDocumentTypes,
            @RequestParam(value = "finantialentityid", required = true) FinantialEntity finantialEntity, Model model,
            RedirectAttributes redirectAttributes) {
        try {
            DocumentTemplate documentTemplate = createDocumentTemplate(finantialDocumentTypes, finantialEntity);
            //Success Validation
            //Add the bean to be used in the View
            model.addAttribute("documentTemplate", documentTemplate);
            return redirect(
                    "/treasury/administration/managefinantialinstitution/documenttemplate/read/"
                            + getDocumentTemplate(model).getExternalId(), model, redirectAttributes);
        } catch (TreasuryDomainException tde) {
            addErrorMessage(" Error creating due to " + tde.getLocalizedMessage(), model);
            return redirect("/treasury/administration/managefinantialinstitution/finantialinstitution/read/"
                    + finantialEntity.getFinantialInstitution().getExternalId(), model, redirectAttributes);
        }
    }

    @Atomic
    public DocumentTemplate createDocumentTemplate(FinantialDocumentType finantialDocumentTypes, FinantialEntity finantialEntity) {
        DocumentTemplate documentTemplate = DocumentTemplate.create(finantialDocumentTypes, finantialEntity);
        return documentTemplate;
    }

    @RequestMapping(value = "/search/upload/{oid}", method = RequestMethod.POST)
    public String processSearchToUploadAction(@PathVariable("oid") DocumentTemplate documentTemplate, @RequestParam(
            value = "documentTemplateFile", required = true) MultipartFile documentTemplateFile, Model model,
            RedirectAttributes redirectAttributes) {
        setDocumentTemplate(documentTemplate, model);
        try {
            uploadDocumentTemplateFile(documentTemplate, documentTemplateFile, model);
            addInfoMessage(BundleUtil.getString(Constants.BUNDLE, "label.success.upload"), model);
        } catch (TreasuryDomainException tde) {
            addErrorMessage(" Error updating due to " + tde.getLocalizedMessage(), model);
        }
        return redirect("/treasury/administration/managefinantialinstitution/documenttemplate/read/"
                + getDocumentTemplate(model).getExternalId(), model, redirectAttributes);
    }

    @Atomic
    public void uploadDocumentTemplateFile(DocumentTemplate documentTemplate, MultipartFile requestFile, Model model) {
        if (!requestFile.getContentType().equals(DocumentTemplateFile.CONTENT_TYPE)) {
            throw new TreasuryDomainException("error.file.different.content.type");
        }
        DocumentTemplateFile documentTemplateFile =
                DocumentTemplateFile.create(documentTemplate, true, requestFile.getName(), requestFile.getOriginalFilename(),
                        getContent(requestFile));
        getDocumentTemplate(model).addDocumentTemplateFiles(documentTemplateFile);
    }

    //TODOJN - how to handle this exception
    private byte[] getContent(MultipartFile requestFile) {
        try {
            return requestFile.getBytes();
        } catch (IOException e) {
            return null;
        }
    }

    @RequestMapping(value = "/search/download/{documentTemplateFileId}", method = RequestMethod.GET)
    public void processSearchToDownloadAction(@PathVariable("documentTemplateFileId") DocumentTemplateFile documentTemplateFile,
            HttpServletResponse response) {
        try {
            response.setContentType(documentTemplateFile.getContentType());
            String filename =
                    URLEncoder.encode(StringNormalizer.normalizePreservingCapitalizedLetters(documentTemplateFile.getFilename())
                            .replaceAll("\\s", "_"), "UTF-8");
            response.setHeader("Content-disposition", "attachment; filename=" + filename);
            response.getOutputStream().write(documentTemplateFile.getContent());
        } catch (IOException e) {
            // TODOJN
            throw new RuntimeException(e);
        }
    }
}
