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
package org.fenixedu.bennu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

import org.fenixedu.commons.i18n.LocalizedString;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;

import pt.ist.fenixframework.DomainObject;
import pt.ist.standards.geographic.Country;
import pt.ist.standards.geographic.District;
import pt.ist.standards.geographic.Municipality;

import com.fatboyindustrial.gsonjodatime.Converters;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class BeanConverterService implements ConditionalGenericConverter {

    @Override
    public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
        return String.class.equals(sourceType.getType()) && IBean.class.isAssignableFrom(targetType.getType());
    }

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        return Collections.singleton(new ConvertiblePair(String.class, IBean.class));
    }

    @Override
    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {

        JsonParser parser = new JsonParser();

        String jsonStr = (String) source;

//	    	//Source is in base64
//	    	byte[] bytes = Base64.getDecoder().decode((String)source);
//			try {
//				jsonStr = new String(bytes, "UTF-8");
//			} catch (UnsupportedEncodingException e) {
//				jsonStr = "";
//			}
        JsonElement parse = parser.parse(jsonStr);

        if (parse instanceof JsonArray) {
            ArrayList list = new ArrayList();
            JsonArray jsonArray = (JsonArray) parse;
            for (int i = 0; i < jsonArray.size(); i++) {
                String className = jsonArray.get(i).getAsJsonObject().get("classname").getAsString();
                list.add(convertObject(jsonArray.get(i), targetType));
            }
            return list;
        } else {
            return convertObject(parse, targetType);
        }
    }

    private Object convertObject(JsonElement jsonElement, TypeDescriptor targetType) {
        String className = jsonElement.getAsJsonObject().get("classname").getAsString();
        Class beanObjectClass;
        try {
            beanObjectClass = Class.forName(className);
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(LocalizedString.class, new LocalizedStringAdapter());
            builder.registerTypeHierarchyAdapter(DomainObject.class, new DomainObjectAdapter());
            builder.registerTypeAdapter(Country.class, new CountryAdapter());
            builder.registerTypeAdapter(District.class, new DistrictAdapter());
            builder.registerTypeAdapter(Municipality.class, new MunicipalityAdapter());
            builder = Converters.registerAll(builder);
            Gson gson = builder.create();
            Object beanObject = gson.fromJson(jsonElement, beanObjectClass);
            //throws ClassCastException if not required domain type
            return targetType.getType().cast(beanObject);
        } catch (Exception e) {
            System.out.print("Error deserializing bean :" + e.getMessage());
            System.out.print("Bean JSON :" + jsonElement.toString());
            return null;
        }
    }
}
