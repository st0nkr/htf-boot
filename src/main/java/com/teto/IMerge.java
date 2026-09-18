package com.teto;

import com.teto.domain.annotation.Meta;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

public interface IMerge extends IFields {

    default <T> T merge(T target, T source) {
        if (target == null) {
            return source;
        }
        if (source == null) {
            return target;
        }
        return merge(target, source, Collections.newSetFromMap(new IdentityHashMap<>()));
    }

    default <T> T merge(T target, T source, Set<Object> visited) {
        if (target == null) {
            return source;
        }
        if (source == null || target == source) {
            return target;
        }
        if (visited.contains(source)) {
            return target;
        }
        visited.add(source);

        Class<?> currentTargetClass = target.getClass();
        Class<?> currentSourceClass = source.getClass();

        while (currentTargetClass != null && currentTargetClass != Object.class) {
            Field[] targetFields = currentTargetClass.getDeclaredFields();
            for (Field targetField : targetFields) {
                if (isIgnorableField(targetField)) {
                    continue;
                }

                Field sourceField = findFieldInHierarchy(currentSourceClass, targetField.getName());
                if (sourceField == null || isIgnorableField(sourceField)) {
                    continue;
                }

                try {
                    targetField.setAccessible(true);
                    sourceField.setAccessible(true);

                    Object sourceVal = sourceField.get(source);
                    if (sourceVal == null) {
                        continue;
                    }

                    Object targetVal = targetField.get(target);

                    if (targetVal == null) {
                        targetField.set(target, sourceVal);
                    } else if (sourceVal instanceof String strSource) {
                        if (!strSource.isBlank()) {
                            targetField.set(target, sourceVal);
                        }
                    } else if (targetVal instanceof Collection<?> targetCol && sourceVal instanceof Collection<?> sourceCol) {
                        try {
                            ((Collection<Object>) targetCol).addAll(sourceCol);
                        } catch (Exception e) {
                            targetField.set(target, sourceVal);
                        }
                    } else if (targetVal instanceof Map<?, ?> targetMap && sourceVal instanceof Map<?, ?> sourceMap) {
                        try {
                            ((Map<Object, Object>) targetMap).putAll(sourceMap);
                        } catch (Exception e) {
                            targetField.set(target, sourceVal);
                        }
                    } else if (isSimpleType(targetField.getType())) {
                        targetField.set(target, sourceVal);
                    } else {
                        merge(targetVal, sourceVal, visited);
                    }
                } catch (Exception ignored) {
                }
            }
            currentTargetClass = currentTargetClass.getSuperclass();
        }

        return target;
    }

    default boolean isIgnorableField(Field field) {
        int mod = field.getModifiers();
        if (Modifier.isStatic(mod) || Modifier.isFinal(mod) || field.isSynthetic()) {
            return true;
        }
        if (isIgnorable(field)) {
            return true;
        }
        Meta meta = getMeta(field);
        if (meta != null && meta.ignore()) {
            return true;
        }
        return false;
    }

    default Field findFieldInHierarchy(Class<?> clazz, String fieldName) {
        Class<?> current = clazz;
        while (current != null && current != Object.class) {
            try {
                return current.getDeclaredField(fieldName);
            } catch (NoSuchFieldException ignored) {
            }
            current = current.getSuperclass();
        }
        return null;
    }

    default boolean isSimpleType(Class<?> type) {
        if (type.isPrimitive()) {
            return true;
        }
        if (Number.class.isAssignableFrom(type) ||
                Boolean.class == type ||
                Character.class == type ||
                String.class == type ||
                Date.class.isAssignableFrom(type) ||
                UUID.class == type ||
                type.isEnum() ||
                java.time.temporal.Temporal.class.isAssignableFrom(type)) {
            return true;
        }
        return false;
    }
}
