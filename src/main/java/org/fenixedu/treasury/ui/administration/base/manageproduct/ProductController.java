/**
 * This file was created by Quorum Born IT <http://www.qub-it.com/> and its 
 * copyright terms are bind to the legal agreement regulating the FenixEdu@ULisboa 
 * software development project between Quorum Born IT and Serviços Partilhados da
 * Universidade de Lisboa:
 *  - Copyright © 2015 Quorum Born IT (until any Go-Live phase)
 *  - Copyright © 2015 Universidade de Lisboa (after any Go-Live phase)
 *
 * Contributors: ricardo.pedro@qub-it.com, anil.mamede@qub-it.com
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
package org.fenixedu.treasury.ui.administration.base.manageproduct;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.fenixedu.bennu.FenixeduTreasurySpringConfiguration;
import org.fenixedu.bennu.core.domain.exceptions.DomainException;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.fenixedu.treasury.domain.Product;
import org.fenixedu.treasury.domain.ProductGroup;
import org.fenixedu.treasury.ui.TreasuryBaseController;
import org.fenixedu.treasury.ui.TreasuryController;
import org.fenixedu.treasury.util.Constants;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import pt.ist.fenixframework.Atomic;

//@Component("org.fenixedu.treasury.ui.administration.base.manageProduct") <-- Use for duplicate controller name disambiguation
@SpringFunctionality(app = TreasuryController.class, title = "label.title.administration.base.manageProduct",
        accessGroup = "logged")
// CHANGE_ME accessGroup = "group1 | group2 | groupXPTO"
@RequestMapping("/treasury/administration/base/manageproduct/product")
public class ProductController extends TreasuryBaseController {

//

    @RequestMapping
    public String home(Model model) {
        //this is the default behaviour, for handling in a Spring Functionality
        return "forward:/treasury/administration/base/manageproduct/product/";
    }

    private Product getProduct(Model m) {
        return (Product) m.asMap().get("product");
    }

    private void setProduct(Product product, Model m) {
        m.addAttribute("product", product);
    }

    @Atomic
    public void deleteProduct(Product product) {
        // CHANGE_ME: Do the processing for deleting the product
        // Do not catch any exception here

        product.delete();
    }

//				
    @RequestMapping(value = "/")
    public String search(@RequestParam(value = "code", required = false) java.lang.String code, @RequestParam(value = "name",
            required = false) org.fenixedu.commons.i18n.LocalizedString name, @RequestParam(value = "unitofmeasure",
            required = false) org.fenixedu.commons.i18n.LocalizedString unitOfMeasure, @RequestParam(value = "active",
            required = false) boolean active, Model model) {
        List<Product> searchproductResultsDataSet = filterSearchProduct(code, name, unitOfMeasure, active);

        //add the results dataSet to the model
        model.addAttribute("searchproductResultsDataSet", searchproductResultsDataSet);
        return "treasury/administration/base/manageproduct/product/search";
    }

    private List<Product> getSearchUniverseSearchProductDataSet() {
        return Product.findAll().collect(Collectors.toList());
    }

    private List<Product> filterSearchProduct(java.lang.String code, org.fenixedu.commons.i18n.LocalizedString name,
            org.fenixedu.commons.i18n.LocalizedString unitOfMeasure, boolean active) {

        return getSearchUniverseSearchProductDataSet()
                .stream()
                .filter(product -> code == null || code.length() == 0 || product.getCode() != null
                        && product.getCode().length() > 0 && product.getCode().toLowerCase().contains(code.toLowerCase()))
                .filter(product -> name == null
                        || name.isEmpty()
                        || name.getLocales()
                                .stream()
                                .allMatch(
                                        locale -> product.getName().getContent(locale) != null
                                                && product.getName().getContent(locale).toLowerCase()
                                                        .contains(name.getContent(locale).toLowerCase())))
                .filter(product -> unitOfMeasure == null
                        || unitOfMeasure.isEmpty()
                        || unitOfMeasure
                                .getLocales()
                                .stream()
                                .allMatch(
                                        locale -> product.getUnitOfMeasure().getContent(locale) != null
                                                && product.getUnitOfMeasure().getContent(locale).toLowerCase()
                                                        .contains(unitOfMeasure.getContent(locale).toLowerCase())))
                .filter(product -> product.getActive() == active).collect(Collectors.toList());
    }

    @RequestMapping(value = "/search/view/{oid}")
    public String processSearchToViewAction(@PathVariable("oid") Product product, Model model,
            RedirectAttributes redirectAttributes) {

        // CHANGE_ME Insert code here for processing viewAction
        // If you selected multiple exists you must choose which one to use below	 
        return redirect("/treasury/administration/base/manageproduct/product/read" + "/" + product.getExternalId(), model,
                redirectAttributes);
    }

//				
    @RequestMapping(value = "/read/{oid}")
    public String read(@PathVariable("oid") Product product, Model model) {
        setProduct(product, model);
        return "treasury/administration/base/manageproduct/product/read";
    }

//
    @RequestMapping(value = "/delete/{oid}")
    public String delete(@PathVariable("oid") Product product, Model model, RedirectAttributes redirectAttributes) {

        setProduct(product, model);
        try {
            //call the Atomic delete function
            deleteProduct(product);

            addInfoMessage(BundleUtil.getString(Constants.BUNDLE, "label.success.delete"), model);
            return redirect("/treasury/administration/base/manageproduct/product/", model, redirectAttributes);

        } catch (DomainException ex) {
            //Add error messages to the list
            addErrorMessage(BundleUtil.getString(Constants.BUNDLE, "label.error.delete") + ex.getMessage(), model);

        } catch (Exception ex) {
            //Add error messages to the list
            addErrorMessage(BundleUtil.getString(Constants.BUNDLE, "label.error.delete") + ex.getMessage(), model);
        }

        //The default mapping is the same Read View
        return redirect("treasury/administration/base/manageproduct/product/read/" + getProduct(model).getExternalId(), model,
                redirectAttributes);
    }

//				
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String create(Model model) {
        model.addAttribute("productGroupList", ProductGroup.readAll());

        return "treasury/administration/base/manageproduct/product/create";
    }

//				
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(@RequestParam(value = "productGroup", required = false) ProductGroup productGroup, @RequestParam(
            value = "code", required = false) java.lang.String code,
            @RequestParam(value = "name", required = false) org.fenixedu.commons.i18n.LocalizedString name, @RequestParam(
                    value = "unitofmeasure", required = false) org.fenixedu.commons.i18n.LocalizedString unitOfMeasure,
            @RequestParam(value = "active", required = false) boolean active, Model model, RedirectAttributes redirectAttributes) {
        /*
        *  Creation Logic
        *	
        	do something();
        *    		
        */
        try {
            Product product = createProduct(productGroup, code, name, unitOfMeasure, active);

            /*
             * Success Validation
             */

            //Add the bean to be used in the View
            model.addAttribute("product", product);

            return redirect("/treasury/administration/base/manageproduct/product/read/" + getProduct(model).getExternalId(),
                    model, redirectAttributes);

        } catch (DomainException tde) {

            addErrorMessage(tde.getLocalizedMessage(), model);
            return create(model);

        } catch (Exception tde) {

            addErrorMessage(tde.getLocalizedMessage(), model);
            return create(model);
        }
    }

    @Atomic
    public Product createProduct(ProductGroup productGroup, java.lang.String code,
            org.fenixedu.commons.i18n.LocalizedString name, org.fenixedu.commons.i18n.LocalizedString unitOfMeasure,
            boolean active) {
        /*
         * Modify the creation code here if you do not want to create
         * the object with the default constructor and use the setter
         * for each field
         */
        Product product = Product.create(productGroup, code, name, unitOfMeasure, active);
        return product;
    }

//				
    @RequestMapping(value = "/update/{oid}", method = RequestMethod.GET)
    public String update(@PathVariable("oid") Product product, Model model) {
        setProduct(product, model);
        model.addAttribute("productGroupList", ProductGroup.readAll());
        return "treasury/administration/base/manageproduct/product/update";
    }

//				
    @RequestMapping(value = "/update/{oid}", method = RequestMethod.POST)
    public String update(@RequestParam(value = "productGroup", required = false) ProductGroup productGroup,
            @PathVariable("oid") Product product, @RequestParam(value = "code", required = false) java.lang.String code,
            @RequestParam(value = "name", required = false) org.fenixedu.commons.i18n.LocalizedString name, @RequestParam(
                    value = "unitofmeasure", required = false) org.fenixedu.commons.i18n.LocalizedString unitOfMeasure,
            @RequestParam(value = "active", required = false) boolean active, Model model, RedirectAttributes redirectAttributes) {

        setProduct(product, model);

        /*
        *  UpdateLogic here
        *	
        	do something();
        *    		
        */

        /*
         * Succes Update
         */
        try {
            updateProduct(productGroup, code, name, unitOfMeasure, active, model);

            return redirect("/treasury/administration/base/manageproduct/product/read/" + getProduct(model).getExternalId(),
                    model, redirectAttributes);

        } catch (DomainException de) {
            // @formatter: off

            /*
             * If there is any error in validation
             * 
             * Add a error / warning message
             * 
             * addErrorMessage(" Error updating due to " +
             * de.getLocalizedMessage(),model);
             * addWarningMessage(" Warning updating due to " +
             * de.getLocalizedMessage(),model);
             */
            // @formatter: on

            addErrorMessage(BundleUtil.getString(Constants.BUNDLE, "label.error.update") + de.getLocalizedMessage(), model);

            return update(product, model);

        } catch (Exception de) {

            addErrorMessage(BundleUtil.getString(Constants.BUNDLE, "label.error.update") + de.getLocalizedMessage(), model);
            return update(product, model);
        }
    }

    @Atomic
    public void updateProduct(ProductGroup productGroup, java.lang.String code, org.fenixedu.commons.i18n.LocalizedString name,
            org.fenixedu.commons.i18n.LocalizedString unitOfMeasure, boolean active, Model m) {
        /*
         * Modify the update code here if you do not want to update
         * the object with the default setter for each field
         */
        getProduct(m).setProductGroup(productGroup);
        getProduct(m).setCode(code);
        getProduct(m).setName(name);
        getProduct(m).setUnitOfMeasure(unitOfMeasure);
        getProduct(m).setActive(active);
    }

}
