package custom.mapper;


import com.sun.org.apache.xml.internal.resolver.readers.ExtendedXMLCatalogReader;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

@Slf4j
public abstract class AnnotationMapper<D> extends MapperAbstract<D> {

    public <O> D readValue(O originObj) {
        Wrapper<D> wrapper = buildWrapper();

        Class originObjClass = originObj.getClass();

        if (isAcceptedClass(originObjClass)) {
            log.debug("Class {} is correctly type annotated ", originObjClass);
            String typeStr = getType(originObjClass);
            wrapper.setType(typeStr);

            Arrays.stream(originObjClass.getDeclaredFields()).forEach(f -> {
                if (isAcceptedField(f)) {
                    log.debug("Field {} is correctly annotated ", f.getName());

                    try {
                        f.setAccessible(true);
                        Class fieldType = f.getType();
                        String fieldName = getFieldName(f);

                        if (f.get(originObj) instanceof Collection) {
                            log.debug("Field {} is iterable", f.getName());

                            ParameterizedType stringListType = (ParameterizedType) f.getGenericType();
                            Class<?> iterableClass = (Class<?>) stringListType.getActualTypeArguments()[0];

                            if (isAcceptedClass(iterableClass) || Number.class.isAssignableFrom(iterableClass) || String.class.isAssignableFrom(iterableClass)) {
                                //Is custom class, must do it recursively

                                Collection it = (Collection) f.get(originObj);
                                Iterator iter = it.iterator();
                                log.debug("PARSING ITERABLE");

                                Collection list = newCollectionInstance(fieldType);
                                while (iter.hasNext()) {
                                    Object itObj = iter.next();
                                    log.debug("    - This is the nested iterable object: {}", itObj);

                                    if(Number.class.isAssignableFrom(iterableClass) || String.class.isAssignableFrom(iterableClass)) {
                                        list.add(itObj);
                                    }else {

                                        D itObjDest = readValue(itObj);
                                        if (itObjDest != null) {
                                            log.debug("    - ADDED:{}", itObj);
                                            list.add(itObjDest);
                                        }
                                    }
                                }
                                wrapper.addCollection(getFieldName(f), list);

                            } else {
                                log.debug("Discarded Field {} that is not annotated", f.getName());
                            }
                        } else if (f.get(originObj) instanceof Number) {
                            wrapper.addNumber(fieldName, (Number) f.get(originObj));
                        } else if (f.get(originObj) instanceof String) {
                            wrapper.addString(fieldName, f.get(originObj).toString());
                        } else if (getGenericTypeClass().isAssignableFrom(fieldType)) {
                            log.debug("Field {} is plain field", f.getName());
                            wrapper.addField(fieldName, (D) f.get(originObj));
                        } else if (isAcceptedClass(fieldType)) {
                            wrapper.addField(fieldName, readValue(f.get(originObj)));
                        } else {
                            throw new IllegalArgumentException("Class {" + fieldType + "} should be annotated with 'type' annotation in order to be used as field {" + f.getName() + "} in class {" + originObjClass + "}");
                        }
                    } catch (IllegalAccessException e) {
                        log.error("Could not access to field:{}. Error:", f.getName(), e);
                    } catch (MapperException e) {
                        log.error("Could not create collection to field:{}. Error:", f.getName(), e);
                    }
                }
            });
        }
        return wrapper.build();
    }


    @Override
    public <O> O writeValue(D source, O destObject) throws MapperException {
        Wrapper<D> wrapper = buildWrapper();
        Class destObjectClass = destObject.getClass();
        if (isAcceptedClass(destObjectClass)) {

            Arrays.stream(destObjectClass.getDeclaredFields()).forEach(field -> {
                if (isAcceptedField(field)) {

                    String fieldName = getFieldName(field);
                    field.setAccessible(true);
                    Class fieldType = field.getType();

                    if (isCollection(fieldType)) {
                        try {
                            Collection newCollection = newCollectionInstance(fieldType);
                            Iterable it = (Iterable) wrapper.getFieldValue(source, fieldName, fieldType);
                            Iterator iterator = it.iterator();

                            ParameterizedType stringListType = (ParameterizedType) field.getGenericType();
                            Class<?> iterableClass = (Class<?>) stringListType.getActualTypeArguments()[0];

                            while (iterator.hasNext()) {
                                D innerObject = (D) iterator.next();

                                if (String.class.isAssignableFrom(iterableClass) || Number.class.isAssignableFrom(iterableClass)) {
                                    newCollection.add(innerObject);
                                }else{
                                    Object obj = iterableClass.newInstance();
                                    writeValue(innerObject, obj);
                                    newCollection.add(obj);
                                }

                            }

                            field.set(destObject, newCollection);

                        } catch (MapperException e) {
                            log.error("Could not write collection field:{}. Error:", field.getName(), e);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InstantiationException e) {
                            log.error("Could not create new instance of collection generic class for field:{}. Error:", field.getName(), e);
                        } catch (Exception e) {
                            log.error("Not expected error while creating new collection instance field:{}. Error:", field.getName(), e);
                        }

                    } else {

                        try {
                            field.set(destObject, wrapper.getFieldValue(source, fieldName, fieldType));
                        } catch (IllegalAccessException e) {
                            log.error("Could not access to field:{}. Error:", field.getName(), e);
                        }
                    }
                }
            });
        } else {
            throw new MapperException("Dest object class {" + destObjectClass + "} is not annotated with any accepted type.");
        }
        return destObject;
    }

    private boolean isCollection(Class clazz) {
        return Collection.class.isAssignableFrom(clazz);
    }

    private <E, C extends Collection<E>> C newCollectionInstance(Class<C> clazz) throws MapperException {
        if (List.class.isAssignableFrom(clazz)) {
            return (C) new ArrayList<E>();
        }

        if (Queue.class.isAssignableFrom(clazz)) {
            return (C) new LinkedList<E>();
        }

        if (Set.class.isAssignableFrom(clazz)) {
            return (C) new HashSet<E>();
        }

        if (Collection.class.getTypeName().equals(clazz.getTypeName())) {
            return (C) new ArrayList<E>();
        }

        throw new MapperException("Unsoported class type: " + clazz);
    }


}
