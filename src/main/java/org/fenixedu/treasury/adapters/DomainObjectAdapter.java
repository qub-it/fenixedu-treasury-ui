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
package org.fenixedu.treasury.adapters;

import java.io.IOException;
import java.lang.reflect.Type;

import pt.ist.fenixframework.DomainObject;
import pt.ist.fenixframework.FenixFramework;

import com.google.gson.InstanceCreator;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

/***
 * Converts a string to a DomainObject
 * 
 */
public class DomainObjectAdapter extends TypeAdapter<DomainObject> implements InstanceCreator {

    @Override
    public DomainObject read(JsonReader reader) throws IOException {
        final DomainObject domainObject = FenixFramework.getDomainObject(reader.nextString());

        if (FenixFramework.isDomainObjectValid(domainObject)) {
            return domainObject;
        }

        return null;
    }

    @Override
    public void write(JsonWriter writer, DomainObject src) throws IOException {
        writer.value(src != null ? src.getExternalId() : "");
    }

    @Override
    public Object createInstance(Type arg0) {
        return null;
    }
}
