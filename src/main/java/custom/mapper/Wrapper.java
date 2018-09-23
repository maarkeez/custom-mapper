package custom.mapper;

import org.apache.commons.lang3.StringUtils;

import java.util.Collection;

public interface Wrapper<T> {

    void addField(String key, T sourceObject);
    <E extends Collection<T>> void addCollection(String key, E iterable);
    <E extends Number> void addNumber(String key, E number);
    void addString(String key, String str);
    void setType(String type);
    T build();
    <F> F getFieldValue(T source, String fieldName, Class<F> fieldClass);

}
