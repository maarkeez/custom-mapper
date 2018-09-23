package custom.mapper;

import java.lang.reflect.Field;

/**
 * This interface defined needed methods to marshall and un-marshall to the target generic class:<br/>
 * <code><li>D</li></code>
 *
 * @param <D>
 */
public abstract class MapperAbstract<D> implements Mapper<D> {

    /**
     * Gets custom type for class
     *
     * @param clazz
     * @return
     */
    protected abstract String getType(Class clazz);

    /**
     * Checks if class should be used for serialization and deserialization
     *
     * @param clazz
     * @return
     */
    protected abstract boolean isAcceptedClass(Class clazz);


    /**
     * Gets custom field name for the field
     *
     * @param field
     * @return
     */
    protected abstract String getFieldName(Field field);

    /**
     * Checks if field should be used for serialization and deserialization
     *
     * @param field
     * @return
     */
    protected abstract boolean isAcceptedField(Field field);


    /**
     * This wrapper will be used as mapper to transform any object into target class
     *
     * @return custom wrapper for the target object class
     */
    protected abstract Wrapper<D> buildWrapper();

    /**
     * Class for the generic type, needed in order to make easier reflection features
     *
     * @return
     */
    protected abstract Class<D> getGenericTypeClass();
}
