package com.teto.command.mac;

import com.teto.IMAC;
import com.teto.domain.mac.MacDetails;
import com.teto.domain.target.Target;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class LookupMacAddressTest implements IMAC {

    @Test
    void testLookupCiscoMac() {
        Optional<MacDetails> result = LookupMacAddress.lookup("00:00:0C:12:34:56");
        assertTrue(result.isPresent());
        MacDetails details = result.get();
        assertEquals("00:00:0C:12:34:56", details.getNormalizedMac());
        assertEquals("00:00:0C", details.getOui());
        assertNotNull(details.getManufacturer());
        assertTrue(details.getManufacturer().toLowerCase().contains("cisco"));
        assertTrue(details.isValid());
        assertTrue(details.isUnicast());
        assertFalse(details.isMulticast());
        assertTrue(details.isUniversallyAdministered());
    }

    @Test
    void testLookupVmwareMacDashFormat() {
        Optional<MacDetails> result = LookupMacAddress.lookup("00-50-56-AB-CD-EF");
        assertTrue(result.isPresent());
        MacDetails details = result.get();
        assertEquals("00:50:56:AB:CD:EF", details.getNormalizedMac());
        assertEquals("00:50:56", details.getOui());
        assertNotNull(details.getManufacturer());
        assertTrue(details.getManufacturer().toLowerCase().contains("vmware"));
    }

    @Test
    void testLookupDotFormat() {
        Optional<MacDetails> result = LookupMacAddress.lookup("0000.0c12.3456");
        assertTrue(result.isPresent());
        MacDetails details = result.get();
        assertEquals("00:00:0C:12:34:56", details.getNormalizedMac());
        assertNotNull(details.getManufacturer());
        assertTrue(details.getManufacturer().toLowerCase().contains("cisco"));
    }

    @Test
    void testLookupRawHexFormat() {
        Optional<MacDetails> result = LookupMacAddress.lookup("005056abcdef");
        assertTrue(result.isPresent());
        MacDetails details = result.get();
        assertEquals("00:50:56:AB:CD:EF", details.getNormalizedMac());
        assertNotNull(details.getManufacturer());
        assertTrue(details.getManufacturer().toLowerCase().contains("vmware"));
    }

    @Test
    void testLookupOuiOnly() {
        Optional<MacDetails> result = LookupMacAddress.lookup("00:50:56");
        assertTrue(result.isPresent());
        MacDetails details = result.get();
        assertEquals("00:50:56", details.getOui());
        assertNotNull(details.getManufacturer());
        assertTrue(details.getManufacturer().toLowerCase().contains("vmware"));
    }

    @Test
    void testLookupManufacturerHelper() {
        Optional<String> manufacturer = LookupMacAddress.lookupManufacturer("08:00:27:11:22:33");
        assertTrue(manufacturer.isPresent());
        assertTrue(manufacturer.get().toLowerCase().contains("cadmus") || manufacturer.get().toLowerCase().contains("virtualbox") || manufacturer.get().toLowerCase().contains("oracle"));
    }

    @Test
    void testLookupTarget() {
        Target target = Target.create();
        target.setMacAddress("00:50:56:11:22:33");

        Optional<MacDetails> details = LookupMacAddress.lookup(target);
        assertTrue(details.isPresent());
        assertNotNull(target.getManufacturer());
        assertTrue(target.getManufacturer().toLowerCase().contains("vmware"));
        assertEquals(target.getManufacturer(), target.getVendor());
    }

    @Test
    void testInterfaceDefaultMethods() {
        Optional<MacDetails> details = lookupMac("00:00:0C:44:55:66");
        assertTrue(details.isPresent());
        assertTrue(details.get().getManufacturer().toLowerCase().contains("cisco"));

        Optional<String> mfg = lookupMacManufacturer("00:50:56:00:11:22");
        assertTrue(mfg.isPresent());
        assertTrue(mfg.get().toLowerCase().contains("vmware"));
    }

    @Test
    void testInvalidMacAddress() {
        Optional<MacDetails> empty = LookupMacAddress.lookup("");
        assertTrue(empty.isEmpty());

        Optional<MacDetails> nullLookup = LookupMacAddress.lookup((String) null);
        assertTrue(nullLookup.isEmpty());

        Optional<MacDetails> tooShort = LookupMacAddress.lookup("12:34");
        assertTrue(tooShort.isEmpty());
    }

    @Test
    void testMulticastAndLocallyAdministeredBits() {
        // 01:00:5E:xx:xx:xx -> IPv4 multicast (multicast bit set)
        Optional<MacDetails> multicast = LookupMacAddress.lookup("01:00:5E:00:00:01");
        assertTrue(multicast.isPresent());
        assertTrue(multicast.get().isMulticast());
        assertFalse(multicast.get().isUnicast());

        // 02:xx:xx:xx:xx:xx -> Locally administered (bit 1 of first byte set)
        Optional<MacDetails> local = LookupMacAddress.lookup("02:00:00:00:00:01");
        assertTrue(local.isPresent());
        assertTrue(local.get().isLocallyAdministered());
        assertFalse(local.get().isUniversallyAdministered());
    }
}
