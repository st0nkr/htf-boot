package com.teto.command;


import com.teto.IOptional;
import com.teto.domain.exception.ContextException;
import com.teto.domain.meta.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;

/*
     No logic specific stuff resides here. Add it and you die!!!
 */
public class Context implements IOptional {

    final Map<String, Object> map = new ConcurrentHashMap<>();
    final Semaphore lock = new Semaphore(1);
    private static final Logger log = LoggerFactory.getLogger(Context.class);
    protected Map<String, Object> getMap() {
        return map;
    }

    public <X> X fetch(Class<X> x) {
        if (x == null) {
            return null;
        }
        String tag = getClassTagName(x);
        try {
            lock.acquire();
            return (X) getMap().get(tag);
        } catch (InterruptedException e) {
            log.error(e.getMessage());
        } finally {
            lock.release();
        }
        return null;
    }

    public <X> X fetch(Tag m) {
        return fetch(m.name());
    }

    public <X> X fetch(String x) {
        if (x == null) {
            return null;
        }
        try {
            lock.acquire();
            return (X) getMap().get(x);
        } catch (InterruptedException e) {
            log.error(e.getMessage());
        } finally {
            lock.release();
        }
        return null;
    }

    public void unstash(Tag tag) {
        unstash(tag.name());
    }

    public <X> void unstash(Class<X> x) {
        if (x == null) {
            return;
        }
        String tag = getClassTagName(x);
        unstash(tag);
    }

    static <X> String getClassTagName(Class<X> x) {
        return x.getSimpleName();
    }

    @SafeVarargs
    public final <X> void stash(X... objects) {
        try {
            lock.acquire();
            for (X object : objects) {
                String tag = getTagName(object);

                getMap().put(tag, object);
            }
        } catch (InterruptedException e) {
            log.error(e.getMessage());
        } finally {
            lock.release();
        }
    }

    public final void stash(Tag m, Object o) {
        try {
            lock.acquire();
            getMap().put(m.name(), o);
        } catch (InterruptedException e) {
            log.error(e.getMessage());
        } finally {
            lock.release();
        }
    }

    public void unstash(String name) {
        try {
            lock.acquire();
            getMap().remove(name);
        } catch (InterruptedException e) {
            log.error(e.getMessage());
        } finally {
            lock.release();
        }
    }

    public void stash(String tag, Object obj) {
        if (tag == null) {
            log.error(tag + " is null");
            throw new ContextException("Key value is null");
        }
        if (obj == null) {
            log.error(obj + " is null");
            throw new ContextException("Attempting to store null value for key " + tag);
        }
        if (obj instanceof Optional) {
            log.error(obj + " is Optional");
            throw new ContextException("Attempting to store optional for key " + tag);
        }
        try {
            lock.acquire();
            getMap().put(tag, obj);
        } catch (InterruptedException e) {
            log.error(e.getMessage());
            throw new ContextException(e.getMessage());
        } finally {
            lock.release();
        }
    }

    public String getTagName(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof String) {
            return (String) obj;
        }
        return obj.getClass().getSimpleName();
    }

    private int tabCount = 0;

    public <X> Optional<X> apply(Command<X> cmd) {
        if (cmd == null) {
            return empty();
        }
        long startTime = System.currentTimeMillis();
        try {
            tabCount++;
            Optional<X> retVal = cmd.apply(this);
            long elapsedTime = System.currentTimeMillis() - startTime;
            //log.info(tabbed(cmd.getClass().getSimpleName() + " took "+elapsedTime+"ms"));

            tabCount--;
            return retVal;
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
        return empty();
    }

    public Context  clone() {
        final Context clone = new Context();
        map.keySet().forEach(key -> {
            Object val = map.get(key);
            clone.stash(key, val);
        });
        return clone;
    }
    private String tabbed(String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < tabCount; i++) {
            sb.append("\t");
        }
        sb.append(s);
        return sb.toString();
    }

    public void clear() {
        try {
            lock.acquire();
            getMap().clear();
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            lock.release();
        }
    }

    public boolean contains(Class clazz) {
        Object obj = fetch(clazz);
        return obj != null;
    }
}
