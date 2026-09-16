package com.teto;

import com.teto.command.Context;
import com.teto.domain.annotation.Meta;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

public interface IFields extends IString{

    default <X> X newInstance(X x) {
        Constructor[] ctors = x.getClass().getDeclaredConstructors();
        Constructor ctor = null;
        for (int i = 0; i < ctors.length; i++) {
            ctor = ctors[i];
            if (ctor.getGenericParameterTypes().length == 0) {
                try {
                    ctor.setAccessible(true);
                    X item = (X) ctor.newInstance();
                    return item;
                } catch(Exception e) {

                }
            }
        }
        return null;
    }

    default boolean isTransient(Field field) {
        int mod = field.getModifiers();
        return Modifier.isTransient(mod);
    }
    default boolean isIgnorable(Field field) {
        if(isTransient(field)) {
            return true;
        }
        return false;
    }

    default <X> Map<String, String> getFieldsWithValues(X x) {
        final Map<String, String> map = new HashMap<>();
        Field[] fields = x.getClass().getDeclaredFields();
        for (Field field : fields) {
            if(isIgnorable(field)) {
                continue;
            }
            field.setAccessible(true);
            try {
                Object val = field.get(x);
                String str = (val == null) ? "" : val.toString();
                if(str != null && !str.isBlank()) {
                    map.put(field.getName(), str);
                }
            } catch (IllegalAccessException e) {

            }
        }
        return map;
    }

    default Meta getMeta(Field field) {
        for(Annotation annot : field.getAnnotations()) {
            if(annot instanceof Meta) {
                return (Meta) annot;
            }
        }
        return null;
    }

    default <X> Map<String, String> getFieldsValues(X x) {
        final Map<String, String> map = new HashMap<>();
        Field[] fields = x.getClass().getDeclaredFields();
        for (Field field : fields) {
            Meta meta = getMeta(field);
            if(meta != null) {
                if(meta.ignore()) {
                    continue;
                }
            }
            if(isIgnorable(field)) {
                continue;
            }
            field.setAccessible(true);
            try {
                Object val = field.get(x);
                String str = (val == null) ? "" : val.toString();
                map.put(field.getName(), str);
            } catch (IllegalAccessException e) {

            }
        }
        return map;
    }


    default boolean hasAcceptableType(Field f) {
        var type = f.getType();
        if(type == boolean.class || type == Boolean.class) {
            return true;
        }
        if(type == int.class || type == Integer.class) {
            return true;
        }
        if(type == double.class || type == Double.class) {
            return true;
        }
        if(type == long.class || type == Long.class) {
            return true;
        }
        if(type == float.class || type == Float.class) {
            return true;
        }
        if(type == char.class || type == Character.class) {
            return true;
        }
        if(type == byte.class || type == Byte.class) {
            return true;
        }
        if(type == short.class || type == Short.class) {
            return true;
        }
        if(type == String.class) {
            return true;
        }
        if(type == Date.class) {
            return true;
        }
        return false;
    }

}
