package custom.mapper;

/**
 * This interface defined needed methods to marshall and un-marshall to the target generic class:<br/>
 * <code><li>D</li></code>
 *
 * @param <D>
 */
public interface Mapper<D> {


    /**
     * Parse any object into target class
     *
     * @param obj object wich will be parsed into target class
     * @param <O> Object class
     * @return target class with retrieved fields from the object
     * @throws MapperException
     */
    <O> D readValue(O obj) throws MapperException;

    /**
     * Parse the generic type class field values into the given object.
     *
     * @param source
     * @param destObject
     * @param <O>
     * @return
     * @throws MapperException
     */
    <O> O writeValue(D source, O destObject) throws MapperException;

}
