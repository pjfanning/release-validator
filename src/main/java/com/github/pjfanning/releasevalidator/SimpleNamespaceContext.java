package com.github.pjfanning.releasevalidator;

import javax.xml.namespace.NamespaceContext;
import java.util.Iterator;
import java.util.Map;

public class SimpleNamespaceContext implements NamespaceContext {

    private final Map<String, String> namespaces;

    public SimpleNamespaceContext(Map<String, String> namespaces) {
        this.namespaces = namespaces;
    }

    @Override
    public String getNamespaceURI(String prefix) {
        return namespaces.get(prefix);
    }

    @Override
    public String getPrefix(String namespaceURI) {
        for (Map.Entry<String, String> entry : namespaces.entrySet()) {
            if (entry.getValue().equals(namespaceURI)) {
                return entry.getKey();
            }
        }
        return null;
    }

    @Override
    public Iterator getPrefixes(String namespaceURI) {
        return namespaces.entrySet().iterator();
    }
}
