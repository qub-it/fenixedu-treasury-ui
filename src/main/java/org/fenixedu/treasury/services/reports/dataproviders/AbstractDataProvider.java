package org.fenixedu.treasury.services.reports.dataproviders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import com.qubit.terra.docs.util.IDocumentFieldsData;
import com.qubit.terra.docs.util.IReportDataProvider;

public abstract class AbstractDataProvider implements IReportDataProvider {
    protected final List<String> allKeys = new ArrayList<String>();
    protected Map<String, Function<IReportDataProvider, Object>> keysDictionary =
            new HashMap<String, Function<IReportDataProvider, Object>>();

    protected void registerKey(String key, Function<IReportDataProvider, Object> function) {
        keysDictionary.put(key, function);
    }

    @Override
    public boolean handleKey(String arg0) {
        return keysDictionary.containsKey(arg0);
    }

    @Override
    public Object valueForKey(String arg0) {
        return keysDictionary.get(arg0).apply(this);
    }

    @Override
    public abstract void registerFieldsAndImages(IDocumentFieldsData arg0);

}
