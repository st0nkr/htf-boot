package com.teto;

import com.teto.domain.annotation.Meta;
import com.teto.domain.target.Target;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class MergeTest implements IMerge {

    private Merge merger;

    @BeforeEach
    void setUp() {
        merger = new Merge();
    }

    public static class SimplePojo {
        private String name;
        private Integer age;
        private Double score;
        private Boolean active;

        public SimplePojo() {}

        public SimplePojo(String name, Integer age, Double score, Boolean active) {
            this.name = name;
            this.age = age;
            this.score = score;
            this.active = active;
        }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public Integer getAge() { return age; }
        public void setAge(Integer age) { this.age = age; }
        public Double getScore() { return score; }
        public void setScore(Double score) { this.score = score; }
        public Boolean getActive() { return active; }
        public void setActive(Boolean active) { this.active = active; }
    }

    public static class ParentPojo {
        private String parentField;

        public String getParentField() { return parentField; }
        public void setParentField(String parentField) { this.parentField = parentField; }
    }

    public static class ChildPojo extends ParentPojo {
        private String childField;

        public String getChildField() { return childField; }
        public void setChildField(String childField) { this.childField = childField; }
    }

    public static class ComplexPojo {
        private String id;
        private SimplePojo details;
        private List<String> tags = new ArrayList<>();
        private Map<String, String> attributes = new HashMap<>();

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public SimplePojo getDetails() { return details; }
        public void setDetails(SimplePojo details) { this.details = details; }
        public List<String> getTags() { return tags; }
        public void setTags(List<String> tags) { this.tags = tags; }
        public Map<String, String> getAttributes() { return attributes; }
        public void setAttributes(Map<String, String> attributes) { this.attributes = attributes; }
    }

    public static class IgnorableFieldsPojo {
        public static String staticField = "static_default";
        private final String finalField = "final_default";
        private transient String transientField;
        @Meta(ignore = true)
        private String ignoredMetaField;
        private String normalField;

        public String getTransientField() { return transientField; }
        public void setTransientField(String transientField) { this.transientField = transientField; }
        public String getIgnoredMetaField() { return ignoredMetaField; }
        public void setIgnoredMetaField(String ignoredMetaField) { this.ignoredMetaField = ignoredMetaField; }
        public String getNormalField() { return normalField; }
        public void setNormalField(String normalField) { this.normalField = normalField; }
        public String getFinalField() { return finalField; }
    }

    public static class CircularNode {
        private String name;
        private CircularNode next;

        public CircularNode(String name) { this.name = name; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public CircularNode getNext() { return next; }
        public void setNext(CircularNode next) { this.next = next; }
    }

    @Test
    void testMergeSimplePojos() {
        SimplePojo target = new SimplePojo("Alice", null, 85.5, null);
        SimplePojo source = new SimplePojo(null, 30, null, true);

        SimplePojo result = merge(target, source);

        assertSame(target, result);
        assertEquals("Alice", result.getName());
        assertEquals(30, result.getAge());
        assertEquals(85.5, result.getScore());
        assertTrue(result.getActive());
    }

    @Test
    void testMergeOverwritesTargetWhenSourceHasValue() {
        SimplePojo target = new SimplePojo("Alice", 25, 80.0, false);
        SimplePojo source = new SimplePojo("Bob", 30, 95.0, true);

        SimplePojo result = merge(target, source);

        assertEquals("Bob", result.getName());
        assertEquals(30, result.getAge());
        assertEquals(95.0, result.getScore());
        assertTrue(result.getActive());
    }

    @Test
    void testMergePreservesTargetWhenSourceHasBlankString() {
        SimplePojo target = new SimplePojo("Alice", 25, null, null);
        SimplePojo source = new SimplePojo("   ", 30, null, null);

        SimplePojo result = merge(target, source);

        assertEquals("Alice", result.getName());
        assertEquals(30, result.getAge());
    }

    @Test
    void testMergeNulls() {
        SimplePojo target = new SimplePojo("Alice", 25, null, null);
        SimplePojo source = new SimplePojo("Bob", 30, null, null);

        assertNull(merge(null, null));
        assertSame(source, merge(null, source));
        assertSame(target, merge(target, null));
    }

    @Test
    void testMergeInheritedFields() {
        ChildPojo target = new ChildPojo();
        target.setParentField("targetParent");

        ChildPojo source = new ChildPojo();
        source.setChildField("sourceChild");

        ChildPojo result = merge(target, source);

        assertEquals("targetParent", result.getParentField());
        assertEquals("sourceChild", result.getChildField());
    }

    @Test
    void testMergeNestedPojos() {
        ComplexPojo target = new ComplexPojo();
        target.setId("T1");
        target.setDetails(new SimplePojo("TargetName", null, 70.0, null));

        ComplexPojo source = new ComplexPojo();
        source.setDetails(new SimplePojo(null, 28, null, true));

        ComplexPojo result = merge(target, source);

        assertEquals("T1", result.getId());
        assertNotNull(result.getDetails());
        assertEquals("TargetName", result.getDetails().getName());
        assertEquals(28, result.getDetails().getAge());
        assertEquals(70.0, result.getDetails().getScore());
        assertTrue(result.getDetails().getActive());
    }

    @Test
    void testMergeCollectionsAndMaps() {
        ComplexPojo target = new ComplexPojo();
        target.getTags().add("java");
        target.getAttributes().put("env", "prod");

        ComplexPojo source = new ComplexPojo();
        source.getTags().add("spring");
        source.getAttributes().put("version", "1.0");

        ComplexPojo result = merge(target, source);

        assertEquals(List.of("java", "spring"), result.getTags());
        assertEquals("prod", result.getAttributes().get("env"));
        assertEquals("1.0", result.getAttributes().get("version"));
    }

    @Test
    void testIgnorableFieldsAreNotMerged() {
        IgnorableFieldsPojo.staticField = "static_default";
        IgnorableFieldsPojo target = new IgnorableFieldsPojo();
        target.setTransientField("targetTransient");
        target.setIgnoredMetaField("targetIgnored");
        target.setNormalField("targetNormal");

        IgnorableFieldsPojo source = new IgnorableFieldsPojo();
        source.setTransientField("sourceTransient");
        source.setIgnoredMetaField("sourceIgnored");
        source.setNormalField("sourceNormal");

        IgnorableFieldsPojo result = merge(target, source);

        assertEquals("targetTransient", result.getTransientField());
        assertEquals("targetIgnored", result.getIgnoredMetaField());
        assertEquals("sourceNormal", result.getNormalField());
        assertEquals("final_default", result.getFinalField());
    }

    @Test
    void testCircularReferencesHandling() {
        CircularNode node1 = new CircularNode("Node1");
        CircularNode node2 = new CircularNode("Node2");
        node1.setNext(node2);
        node2.setNext(node1);

        CircularNode target = new CircularNode("Target");
        target.setNext(new CircularNode("TargetNext"));

        assertDoesNotThrow(() -> merge(target, node1));
        assertEquals("Node1", target.getName());
    }

    @Test
    void testMergeClassConvenienceMethods() {
        SimplePojo target = new SimplePojo("Alice", null, null, null);
        SimplePojo source = new SimplePojo(null, 35, null, null);

        SimplePojo res1 = Merge.mergePojos(target, source);
        assertEquals("Alice", res1.getName());
        assertEquals(35, res1.getAge());

        SimplePojo res2 = merger.merge(target, new SimplePojo("Alice2", null, 99.0, null));
        assertEquals("Alice2", res2.getName());
        assertEquals(99.0, res2.getScore());
    }

    @Test
    void testMergeTargetDomainEntity() {
        Target target = new Target();
        target.setName("test-target");
        target.setIpAddress("192.168.1.10");

        Target source = new Target();
        source.setPortNumber(8080L);
        source.setVendor("Apache");

        Target result = Merge.mergePojos(target, source);
        assertEquals("test-target", result.getName());
        assertEquals("192.168.1.10", result.getIpAddress());
        assertEquals(8080L, result.getPortNumber());
        assertEquals("Apache", result.getVendor());
    }
}
