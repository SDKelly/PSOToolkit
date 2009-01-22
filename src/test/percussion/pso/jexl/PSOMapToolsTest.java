package test.percussion.pso.jexl;

import static java.util.Arrays.*;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.percussion.pso.jexl.PSOMapTools;

public class PSOMapToolsTest {

    PSOMapTools mapTools;
    Map<String, Object> customM;
    Map<String, Object> defaultM;
    Map<String, Object> actual;
    Map<String, Object> expected;
    
    @Before
    public void setUp() {
        mapTools = new PSOMapTools();
        expected = new HashMap<String, Object>();
        
        customM = new HashMap<String, Object>();
        defaultM = new HashMap<String, Object>();
        customM.put("a", 1L);
        customM.put("b", 0L);
        customM.put("e", 5L);
        
        defaultM.put("a", 1L);
        defaultM.put("b", 2L);
        defaultM.put("c", 3L);
        defaultM.put("d", 4L);
    }
    
    @Test
    public void shouldExtend() {
        expected.put("a", 1L);
        expected.put("b", 0L);
        expected.put("c", 3L);
        expected.put("d", 4L);
        expected.put("e", 5L);
        
        actual = mapTools.overlay(defaultM, customM);
        assertEquals(expected, actual);
    }
    
    @Test
    public void overlayShouldReturnEqualsToDefaultOnNullForCustom() {
        actual = mapTools.overlay(defaultM, null);
        assertEquals(defaultM, actual);
        assertNotSame(defaultM, actual);
    }
    
    @Test
    public void overlayShouldReturnEqualsToCustomOnNullForDefault() {
        actual = mapTools.overlay(null, customM);
        assertEquals(customM, actual);
        assertNotSame(customM, actual);
    }
    
    @Test
    public void shouldCreateMapFromLists() {
        List<String> keys = asList("a","b","e");
        List<Long> values = asList(1L,0L,5L, 100000L);
        // The last value should be ignored
        actual = mapTools.create(keys, values);
        assertEquals(customM, actual);
    }
    
    @Test
    public void shouldCreateMap() {
        assertNotNull(mapTools.create());
    }
    
    @Test
    public void shouldGetFirstDefined() {
        Object actual = mapTools.getFirstDefined(customM, "d,r,e,b", null);
        assertEquals(5L, actual);
    }
}
