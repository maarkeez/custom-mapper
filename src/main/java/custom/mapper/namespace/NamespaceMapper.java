package custom.mapper.namespace;

import custom.mapper.AnnotationMapper;
import custom.mapper.Wrapper;
import custom.mapper.namespace.annotation.MapperField;
import custom.mapper.namespace.annotation.MapperFieldCustom;
import custom.mapper.namespace.annotation.MapperType;
import custom.mapper.namespace.annotation.MapperTypeCustom;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;

@Slf4j
public class NamespaceMapper extends AnnotationMapper<SObject> {

    private static final String SUFFIX_CUSTOM = "__c";

    @Override
    protected String getType(Class clazz) {
        try {
            if (clazz.isAnnotationPresent(MapperType.class)) {
                MapperType type = (MapperType) clazz.getAnnotation(MapperType.class);
                return type.value();
            } else {
                MapperTypeCustom name = (MapperTypeCustom) clazz.getAnnotation(MapperTypeCustom.class);
                return name.namespace() + name.value() + SUFFIX_CUSTOM;
            }
        } catch (Exception e) {
            log.error("Could not get type from class {}", clazz);
            return "";
        }
    }


    @Override
    protected String getFieldName(Field field) {
        try {

            if (field.isAnnotationPresent(MapperField.class)) {
                MapperField name = field.getAnnotation(MapperField.class);
                return name.namespace() + name.value();
            } else {
                MapperFieldCustom name = field.getAnnotation(MapperFieldCustom.class);
                return name.namespace() + name.value() + SUFFIX_CUSTOM;
            }

        } catch (Exception e) {
            log.error("Could not get type from field {}", field.getName());
            return "";
        }
    }

    @Override
    protected boolean isAcceptedClass(Class clazz) {
        return clazz.isAnnotationPresent(MapperType.class) || clazz.isAnnotationPresent(MapperTypeCustom.class);
    }

    @Override
    protected boolean isAcceptedField(Field field) {
        return field.isAnnotationPresent(MapperField.class) || field.isAnnotationPresent(MapperFieldCustom.class);
    }

    @Override
    protected Wrapper buildWrapper() {
        return new NamespaceWrapper();
    }

    @Override
    protected Class<SObject> getGenericTypeClass() {
        return SObject.class;
    }
}
