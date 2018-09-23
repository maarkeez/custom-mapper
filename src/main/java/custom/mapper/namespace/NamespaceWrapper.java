package custom.mapper.namespace;


import custom.mapper.Wrapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;


@Slf4j
class NamespaceWrapper implements Wrapper<SObject> {

    @Getter
    private SObject sObject;

    public NamespaceWrapper() {
        this.sObject = new SObject();
    }


    @Override
    public void addField(String key, SObject sourceObject) {
        log.debug("Adding SObject:\n\tkey -> {}\n\tvalue -> {}", key, sourceObject);
        sObject.getFields().put(key, sourceObject);
    }

    @Override
    public <E extends Collection<SObject>> void addCollection(String key, E iterable) {
        log.debug("Adding iterable field:\n\tkey -> {}\n\tvalue -> {}", key, iterable);
        sObject.getFields().put(key, iterable);
    }

    @Override
    public <E extends Number> void addNumber(String key, E number) {
        log.debug("Adding number:\n\tkey -> {}\n\tvalue -> {}", key, number);
        sObject.getFields().put(key, number);
    }

    @Override
    public void addString(String key, String str) {
        log.debug("Adding String:\n\tkey -> {}\n\tvalue -> {}", key, str);
        sObject.getFields().put(key, str);
    }

    @Override
    public void setType(String type) {
        log.debug("Setting type:\n\ttype -> {}", type);
        sObject.setType(type);
    }

    @Override
    public SObject build() {
        return sObject;
    }

    @Override
    public <F> F getFieldValue(SObject source, String fieldName, Class<F> fieldClass) {
        return (F) source.getFields().get(fieldName);
    }

}
