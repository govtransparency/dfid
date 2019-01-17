package eu.dfid.worker.wb.clean.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides useful methods for World Bank cleaners.
 *
 * @author Tomas Mrazek
 */
public final class WBCleanUtils {

    private static final Logger logger = LoggerFactory.getLogger(WBCleanUtils.class.getName());

    /**
     * Supress default constructor for noninstatiability.
     */
    private WBCleanUtils() {
    }

    /**
     * Relpaces all string values "N/A" with NULL. Method recursively inspects the given object {@code o} and if its any
     * string property (or property of nasted objects) is equals to "N/A" replaces it by NULL.
     *
     * @param o
     *      object that potentially includes "N/A" values
     * @return object without "N/A" values
     */
    public static Object replaceNAWithNull(final Object o) {
        final List<Method> getters = Arrays.stream(o.getClass().getMethods())
            .filter(m -> (m.getName().startsWith("get") && !m.getName().equals("getClass")))
            .collect(Collectors.toList());

        for (Method getter : getters) {
            Method setter = getSetter(o.getClass(), getter);
            //object class doesn't includes appropriate setter for the given getter, skip it
            if (setter == null) {
                logger.warn("Appropriate setter for getter {} not found.", getter.getName());
                continue;
            }

            Object value;
            try {
                value = getter.invoke(o);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                logger.warn("Unable to acquire value with getter {} because of {}", getter.getName(), e);
                continue;
            }

            try {
                if (value instanceof String && value.equals("N/A")) {
                    setter.invoke(o, new Object[]{ null });
                } else if (value instanceof List && !((List) value).isEmpty()){
                    List list = (List) value;
                    List<Object> newList = new ArrayList<>();
                    if (list.get(0) instanceof String) {
                        list.stream()
                            .filter((item) -> (!((String) item).equals("N/A")))
                            .forEach((item) -> {
                                newList.add(item);
                            });
                    } else if (list.get(0) instanceof Object) {
                        list.stream().forEach((item) -> {
                            newList.add(replaceNAWithNull(item));
                        });
                    }

                    setter.invoke(o, newList.isEmpty() ? null : newList);
                } else if (value instanceof Object) {
                    setter.invoke(o, replaceNAWithNull(value));
                }
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                logger.warn("Unable to set value with setter {} because of {}", setter.getName(), e);
            }
        }

        return o;
    }

    /**
     * Returns the setter for the given context class and its getter method. The method suposes that the setter method
     * name is the same like name of the getter method but with the difference this one starts with "set".
     *
     * @param context
     *      context class
     * @param getter
     *      getter method of the context class
     * @return setter or null
     */
    private static Method getSetter(final Class context, final Method getter) {
        try {
            return context.getMethod(getter.getName().replaceFirst("get", "set"), getter.getReturnType());
        } catch (NoSuchMethodException | SecurityException e) {
            return null;
        }
    }
}
