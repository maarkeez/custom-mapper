package custom.mapper.namespace;

import custom.mapper.AnnotationMapper;
import custom.mapper.Wrapper;
import custom.mapper.namespace.annotation.MapperField;
import custom.mapper.namespace.annotation.MapperType;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;

@Slf4j
public class NamespaceMapper extends AnnotationMapper<SObject> {

    @Override
    protected String getType(Class clazz) {
        try {

            MapperType type = (MapperType) clazz.getAnnotation(MapperType.class);
            return type.value();

        } catch (Exception e) {
            log.error("Could not get type from class {}", clazz);
            return "";
        }
    }

    @Override
    protected boolean isAcceptedClass(Class clazz) {
        return clazz.isAnnotationPresent(MapperType.class);
    }

    @Override
    protected String getFieldName(Field field) {
        try {

            MapperField name = (MapperField) field.getAnnotation(MapperField.class);
            return name.value();

        } catch (Exception e) {
            log.error("Could not get type from field {}", field.getName());
            return "";
        }
    }

    @Override
    protected boolean isAcceptedField(Field field) {
        return field.isAnnotationPresent(MapperField.class);
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
