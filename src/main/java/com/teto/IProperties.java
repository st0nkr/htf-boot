package com.teto;

import com.teto.command.Context;
import com.teto.domain.meta.Tag;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public interface IProperties extends IStream, ISystem, IException {

    default Properties properties(Context ctx) {
        return ctx.fetch(Tag.Properties.name());
    }

    default Properties loadProperties(Context ctx, String fileName) {
        FileInputStream fis = null;
        try {
            Properties props = new Properties();
            fis = new FileInputStream(fileName);
            props.load(fis);
            props.put(Tag.ConfigFileName.name(), fileName);
            return props;
        } catch (Exception e) {
            return null;
        } finally {
            if(fis != null) {
                try {
                    fis.close();
                } catch(Exception ignored) {
                }
            }
        }
    }

    default void saveProperties(Context ctx) {
        Properties props = properties(ctx);
        String fileName = (String) props.get(Tag.ConfigFileName.name());
        saveProperties(ctx, fileName, props);
    }
    default boolean saveProperties(Context ctx, String fileName, Properties props) {
        try {
            props.keySet().forEach(k -> {
                Object val = props.get(k);
                if(!(val instanceof String))  {
                    System.out.println("Ky -> "+k);
                }
            });
            FileOutputStream fos = new FileOutputStream(fileName);
            props.store(fos, "Saved on "+System.currentTimeMillis());
            fos.close();
            return true;
        } catch(Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    default String expandKeyWords(String name) {
        if(name == null || name.isEmpty()) {
            return name;
        }
        int idx = name.indexOf("$resources");
        if(idx != -1) {
            return name.replace("$resources", resourcesDirectory());
        }
        idx = name.indexOf("$java");
        if(idx != -1) {
            return name.replace("$java", javaDirectory());
        }
        return name;
    }

    default String javaDirectory() {
        return mainDirectory()+File.separator+"java";
    }
    default String sourceDirectory() {
        String userDirectory = Paths.get("")
                .toAbsolutePath()
                .toString();
        return userDirectory+File.separator+"src";
    }

    default String mainDirectory() {
        String src = sourceDirectory();
        return src+File.separator+"main";
    }

    default String resourcesDirectory() {
        String main = mainDirectory();
        return main+File.separator+"resources";
    }

    default String property(Context ctx, Tag name) {
      return property(ctx, name.name());
    }

    default String property(Context ctx, Tag name, String def) {
        String ret = property(ctx, name.name());
        if(ret == null) {
            ret = def;
        }
        return ret;
    }

    default String property(Context ctx, String name) {
        Properties properties = properties(ctx);
        if (properties != null) {
            String value;
            if(isWindows() && properties.containsKey(name+".Windows")) {
                value = expandKeyWords(properties.getProperty(name+".Windows"));
            } else {
               value = expandKeyWords(properties.getProperty(name));
            }
            return value;
        }
        return null;
    }

    default Long property(Context ctx, Tag name, Long defaultValue) {
        return propertyLong(ctx,name,defaultValue);
    }
    default Long property(Context ctx, String name, Long defaultValue) {
        return propertyLong(ctx, name, defaultValue);
    }

    default Long propertyLong(Context ctx, Tag name, Long defaultValue) {
        return propertyLong(ctx, name.name(), defaultValue);
    }
    default Long propertyLong(Context ctx, String name, Long defaultValue) {
        Properties properties = properties(ctx);
        if (properties != null) {
            String ret = properties.getProperty(name);
            if(ret != null) {
                try {
                    return Long.parseLong(ret);
                } catch(Exception e) {
                    return defaultValue;
                }
            }
        }
        return defaultValue;
    }

    default Double propertyDouble(Context ctx, Tag name, Double defaultValue) {
        return propertyDouble(ctx, name.name(), defaultValue);
    }
    default Double propertyDouble(Context ctx, String name, Double defaultValue) {
        Properties properties = properties(ctx);
        if (properties != null) {
            String ret = properties.getProperty(name);
            if(ret != null) {
                try {
                    return Double.parseDouble(ret);
                } catch(Exception e) {
                    return defaultValue;
                }
            }
        }
        return defaultValue;
    }

    default Integer property(Context ctx, Tag name, int defaultValue) {
        return propertyInt(ctx, name.name(), defaultValue);
    }
    default Integer property(Context ctx, String name, int defaultValue) {
        return propertyInt(ctx, name, defaultValue);
    }

    default Integer propertyInt(Context ctx, Tag name, Integer defaultValue) {
        return propertyInt(ctx, name.name(), defaultValue);
    }

    default List<String> propertyList(Context ctx, Tag name, String delim, String[] def) {
       String str = property(ctx, name);
       if(str == null) {
           return Arrays.asList(def);
       }
       return Arrays.asList(str.split(delim));
    }

    default Integer propertyInt(Context ctx, String name, Integer defaultValue) {
        Properties properties = properties(ctx);
        if (properties != null) {
            String ret = properties.getProperty(name);
            if(ret != null) {
                try {
                    return Integer.parseInt(ret);
                } catch(Exception e) {
                    return defaultValue;
                }
            }
        }
        return defaultValue;
    }

    default Boolean property(Context ctx, Tag name, Boolean defValue) {
        return propertyBoolean(ctx, name.name(), defValue);
    }
    default Boolean property(Context ctx, String name, Boolean defValue) {
        return propertyBoolean(ctx,name,defValue);
    }

    default Boolean propertyBoolean(Context ctx, Tag name, Boolean defaultValue) {
        return propertyBoolean(ctx, name.name(), defaultValue);
    }
    default Boolean propertyBoolean(Context ctx, String name, Boolean defaultValue) {
        Properties properties = properties(ctx);
        if (properties != null) {
            String ret = properties.getProperty(name);
            if(ret != null) {
                try {
                    return Boolean.parseBoolean(ret);
                } catch(Exception e) {
                    return defaultValue;
                }
            }
        }
        return defaultValue;
    }
    default void setProperty(Context ctx, Tag name, String value) {
        setProperty(ctx, name.name(), value);
    }
    default void setProperty(Context ctx, String name, String value) {
        if(value != null) {
            Properties properties = properties(ctx);
            if (properties != null) {
                properties.setProperty(name, value);
            }
        }
    }

    default void setProperty(Context ctx, Tag name, Double value) {
        setProperty(ctx, name.name(), Double.toString(value));
    }
}
