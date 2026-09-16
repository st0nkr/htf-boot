package com.teto.domain.jsoup;

import com.teto.domain.meta.ConfigProperty;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Element;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class NodeLocatorFactory {

    private static String removeNameSpace(String nodeName) {
        if (nodeName.contains(":")) {
            return nodeName.split(":")[1];
        }
        return nodeName;
    }

    private static String cleanText(String text) {
        if(text == null) return text;
        return text.replace("\n","").replace("\r","");
    }

    public static NodeLocator normalize(Element e) {
        NodeLocator nl = new NodeLocator();
        nl.setJsoupElement(e);

        SortedSet<String> classNames = new TreeSet<>(e.classNames());
        if(!classNames.isEmpty()) {
            nl.put(ConfigProperty.CLASS, setToString(classNames));
        }
        String text = cleanText(e.text());
        if(!text.strip().isEmpty()) {
            var txt = text.strip();
            nl.put(ConfigProperty.TEXT, txt);
        }

        if(e.hasParent()) {
            nl.put(ConfigProperty.PARENT, e.parent().toString());
        }

        try {
            String sel = e.cssSelector();
            if(!sel.isEmpty()) {
                nl.put(ConfigProperty.CSS_SELECTOR, sel);
            }
        } catch(Exception ex) {

        }

        try {
            nl.put(ConfigProperty.BASE_URI, e.baseUri());
            nl.put(ConfigProperty.TAG,e.tagName());
            nl.put(ConfigProperty.OUTER_HTML, e.outerHtml());
            nl.put(ConfigProperty.HAS_PARENT, Boolean.valueOf(e.hasParent()).toString());
            nl.put(ConfigProperty.OWN_TEXT, e.ownText());
            nl.put(ConfigProperty.WHOLE_TEXT, e.wholeText());
            nl.put(ConfigProperty.IS_BLOCK, e.isBlock() + "");
            nl.put(ConfigProperty.ID, e.id());
            nl.put(ConfigProperty.DATA, e.data());
            nl.put(ConfigProperty.VALUE, e.val());
            nl.put(ConfigProperty.HTML, e.html());
            nl.put(ConfigProperty.TYPE, e.attr(ConfigProperty.TYPE));
            for (Attribute att : e.attributes()) {
                String key = mapKey(att.getKey());
                if (key != null) {
                    switch (key) {
                        case ConfigProperty.HIDDEN: {
                            nl.put(ConfigProperty.HIDDEN, ConfigProperty.TRUE);
                        }
                        break;
                        case ConfigProperty.MULTIPLE: {
                            nl.put(ConfigProperty.MULTIPLE, ConfigProperty.TRUE);
                        }
                        break;
                        case ConfigProperty.NO_VALIDATE: {
                            nl.put(ConfigProperty.NO_VALIDATE, ConfigProperty.TRUE);
                        }
                        break;
                        case ConfigProperty.AUTOFOCUS: {
                            nl.put(ConfigProperty.AUTOFOCUS, ConfigProperty.TRUE);
                        }
                        break;
                        case ConfigProperty.DISABLED: {
                            nl.put(ConfigProperty.DISPLAYED, ConfigProperty.TRUE);
                        }
                        break;
                        case ConfigProperty.REQUIRED: {
                            nl.put(ConfigProperty.REQUIRED, ConfigProperty.TRUE);
                        }

                        default: {
                            nl.put(key, att.getValue());
                        }
                    }
                } else {
                    nl.put(key, att.getValue());
                }
            }
            nl.put(ConfigProperty.HAS_CHILDREN, (e.childrenSize() > 0) ? ConfigProperty.TRUE : ConfigProperty.FALSE);
            nl.remove(ConfigProperty.PARENT);
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        return nl;
    }

    private static String mapKey(String key) {

        if(false) {
            switch (key.toLowerCase()) {
                case "aria-busy":
                    return ConfigProperty.ARIA_BUSY;
                case "data-fgtcs-blocklinks":
                    return ConfigProperty.DATA_FGTCS_BLOCKLINKS;
                case "data-component":
                    return ConfigProperty.DATA_COMPONENT;
                case "data-src":
                    return ConfigProperty.DATA_SRC;
                case "data-srcset":
                    return ConfigProperty.DATA_SRCSET;
                case "fetchpriority":
                    return ConfigProperty.FETCHPRIORITY;
                case "onerror":
                    return ConfigProperty.ONERROR;
                case "onload":
                    return ConfigProperty.ONLOAD;
                case "loading":
                    return ConfigProperty.LOADING;
                case "src":
                    return ConfigProperty.SRC;
                case "srcset":
                    return ConfigProperty.SRCSET;
                case "sizes":
                    return ConfigProperty.SIZES;
                case "data-form-action-uri":
                    return ConfigProperty.DATA_FORM_ACTION_URI;
                case "data-view-id":
                    return ConfigProperty.DATA_VIEW_ID;
                case "data-locale":
                    return ConfigProperty.DATA_LOCALE;
                case "data-allow-sign-up-types":
                    return ConfigProperty.DATA_ALLOW_SIGN_UP_TYPES;
                case "view":
                    return ConfigProperty.VIEW;
                case "data-allow-at-sign":
                    return ConfigProperty.DATA_ALLOW_AT_SIGN;
                case "data-is-rendered":
                    return ConfigProperty.DATA_IS_RENDERED;
                case "data-initial-dir":
                    return ConfigProperty.DATA_INITIAL_DIR;
                case "data-initial-value":
                    return ConfigProperty.DATA_INITIAL_VALUE;
                case "data-is-touch-wrapper":
                    return ConfigProperty.DATA_IS_TOUCH_WRAPPER;
                case "data-idom-class":
                    return ConfigProperty.DATA_IDOM_CLASS;
                case "jsrenderer":
                    return ConfigProperty.JSRENDERER;
                case "jsdata":
                    return ConfigProperty.JSDATA;
                case "data-p":
                    return ConfigProperty.DATA_P;
                case "data-node-index":
                    return ConfigProperty.DATA_NODE_INDEX;
                case "jsmodel":
                    return ConfigProperty.JSMODEL;
                case "c-wiz":
                    return ConfigProperty.C_WIZ;
                case "jsaction":
                    return ConfigProperty.JSACTION;
                case "aria-controls":
                    return ConfigProperty.ARIA_CONTROLS;
                case "aria-live":
                    return ConfigProperty.ARIA_LIVE;
                case "jscontroller":
                    return ConfigProperty.JSCONTRLLER;
                case "jsshadow":
                    return ConfigProperty.JSSHADOW;
                case "aria-atomic":
                    return ConfigProperty.ARIA_ATOMIC;
                case "alt":
                    return ConfigProperty.ALT;
                case "jsname":
                    return ConfigProperty.JSNAME;
                case "jsslot":
                    return ConfigProperty.JSSLOT;
                case "multiple":
                    return ConfigProperty.MULTIPLE;
                case "aria-labelledby":
                    return ConfigProperty.ARIA_LABELLED_BY;
                case "aria-invalid":
                    return ConfigProperty.ARIA_INVALID;
                case "aria-required":
                    return ConfigProperty.ARIA_REQUIRED;
                case "data-bbc-container":
                    return ConfigProperty.DATA_BBC_CONTAINER;
                case "data-bbc-title":
                    return ConfigProperty.DATA_BBC_TITLE;
                case "data-bbc-source":
                    return ConfigProperty.DATA_BBC_SOURCE;
                case "data-bbc-ignore-views":
                    return ConfigProperty.DATA_BBC_IGNORE_VIEWS;
                case "d":
                    return ConfigProperty.D;
                case "maxlength":
                    return ConfigProperty.MAX_LENGTH;
                case "spellcheck":
                    return ConfigProperty.SPELLCHECK;
                case "title":
                    return ConfigProperty.TITLE;
                case "focusable":
                    return ConfigProperty.FOCUSABLE;
                case "fill":
                    return ConfigProperty.FILL;
                case "disabled":
                    return ConfigProperty.DISABLED;
                case "name":
                    return ConfigProperty.name;
                case "nodeName":
                    return ConfigProperty.NODE_NAME;
                case "data-context":
                    return ConfigProperty.DATA_CONTEXT;
                case "method":
                    return ConfigProperty.METHOD;
                case "ownText":
                    return ConfigProperty.OWN_TEXT;
                case "tagName":
                    return ConfigProperty.TAG;
                case "outerHtml":
                    return ConfigProperty.OUTER_HTML;
                case "data-module":
                    return ConfigProperty.DATA_MODULE;
                case "cssSelector":
                    return ConfigProperty.CSS_SELECTOR;
                case "baseUri":
                    return ConfigProperty.BASE_URI;
                case "html":
                    return ConfigProperty.HTML;
                case "text":
                    return ConfigProperty.TEXT;
                case "id":
                    return ConfigProperty.ID;
                case "class":
                    return ConfigProperty.CLASS;
                case "value":
                    return ConfigProperty.VALUE;
                case "hasParent":
                    return ConfigProperty.HAS_PARENT;
                case "action":
                    return ConfigProperty.ACTION;
                case "for":
                    return ConfigProperty.FOR;
                case "aria-checked":
                    return ConfigProperty.ARIA_CHECKED;
                case "data-checked":
                    return ConfigProperty.DATA_CHECKED;
                case "stroke":
                    return ConfigProperty.STROKE;
                case "stroke-width":
                    return ConfigProperty.STROKE_WIDTH;
                case "stroke-linecap":
                    return ConfigProperty.STOKE_LINECAP;
                case "stroke-linejoin":
                    return ConfigProperty.STOKE_LINEJOIN;
                case "color":
                    return ConfigProperty.COLOR;
                case "target":
                    return ConfigProperty.TARGET;
                case "aria-describedby":
                    return ConfigProperty.ARIA_DESCRIBED_BY;
                case "placeholder":
                    return ConfigProperty.PLACEHOLDER;
                case "autocomplete":
                    return ConfigProperty.AUTOCOMPLETE;
                case "type":
                    return ConfigProperty.TYPE;
                case "xmlns":
                    return ConfigProperty.XMLNS;
                case "width":
                    return ConfigProperty.WIDTH;
                case "height":
                    return ConfigProperty.HEIGHT;
                case "viewbox":
                    return ConfigProperty.VIEWBOX;
                case "aria-labelledBy":
                    return ConfigProperty.ARIA_LABELLED_BY;
                case "role":
                    return ConfigProperty.ROLE;
                case "href":
                    return ConfigProperty.HREF;
                case "data-test-id":
                    return ConfigProperty.DATA_TEST_ID;
                case "aria-label":
                    return ConfigProperty.ARIA_LABEL;
                case "xmlns:xlink":
                    return ConfigProperty.XLINK;
                case "xlink:href":
                    return ConfigProperty.XLINK_HREF;
                //case "accept-charset" : return MetaTags.ACCEPT_CHARSET;
                case "data-stats-interaction":
                    return ConfigProperty.DATA_STATS_INTERACTION;
                case "data-stats-interaction-variant":
                    return ConfigProperty.DATA_STATS_INTERACTION_VARIANT;
                case "data-stats-interaction-action":
                    return ConfigProperty.DATA_STATS_INTERACTION_ACTION;
                case "aria-hidden":
                    return ConfigProperty.ARIA_HIDDEN;
                case "required":
                    return ConfigProperty.REQUIRED;
                case "tabindex":
                    return ConfigProperty.TAB_INDEX;
                case "data-type":
                    return ConfigProperty.DATA_TYPE;
                case "data-value":
                    return ConfigProperty.DATA_VALUE;
                case "aria-autocomplete":
                    return ConfigProperty.ARIA_AUTOCOMPLETE;
                case "aria-haspopup":
                    return ConfigProperty.ARIA_HAS_POPUP;
                case "aria-owns":
                    return ConfigProperty.ARIA_OWNS;
                case "aria-expanded":
                    return ConfigProperty.ARIA_EXPANDED;
                case "autofocus":
                    return ConfigProperty.AUTOFOCUS;
                case "autocorrect":
                    return ConfigProperty.AUTOCORRECT;
                case "autocapitalize":
                    return ConfigProperty.AUTO_CAPITALIZE;
                case "data-key":
                    return ConfigProperty.DATA_KEY;
                case "style":
                    return ConfigProperty.STYLE;
                case "dir":
                    return ConfigProperty.DIR;
                case "data-testid":
                    return ConfigProperty.DATA_TESTID;
                case "aria-disabled":
                    return ConfigProperty.ARIA_DISABLED;
                case "novalidate":
                    return ConfigProperty.NO_VALIDATE;
                case "data-blog":
                    return ConfigProperty.DATA_BLOG;
                case "data-post_access_level":
                    return ConfigProperty.DATA_POST_ACCESS_LEVEL;
                case "data-ng-model":
                    return ConfigProperty.DATA_NG_MODEL;
                case "data-ng-click":
                    return ConfigProperty.DATA_NG_CLICK;
                case "ng-if":
                    return ConfigProperty.NG_IF;
                case "data-header_menu_search":
                    return ConfigProperty.DATA_HEADER_MENU_SEARCH;
                case "data-header_menu_search_start":
                    return ConfigProperty.DATA_HEADER_MENU_SEARCH_START;
                case "data-drupal-selector":
                    return ConfigProperty.DATA_DRUPAL_SELECTOR;
                case "size":
                    return ConfigProperty.SIZE;
                case "fill-rule":
                    return ConfigProperty.FILL_RULE;
                case "clip-rule":
                    return ConfigProperty.CLIP_RULE;
                case "data-sitekey":
                    return ConfigProperty.DATA_SITE_KEY;
                case "rel":
                    return ConfigProperty.REL;
                case "data-nav-assist-menu-item-index":
                case "data-behavior":
                case "data-actuators":
                case "data-target":
            }
        }
        return key;
    }

    private static String setToString(Set<String> classNames) {
        final StringBuilder sb = new StringBuilder();
        if (classNames != null) {
            classNames.forEach(name -> sb.append(name).append(" "));
        }
        return sb.toString().trim();
    }
}
