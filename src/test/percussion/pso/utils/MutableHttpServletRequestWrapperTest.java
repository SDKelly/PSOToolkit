/*
 * COPYRIGHT (c) 1999 - 2008 by Percussion Software, Inc., Woburn, MA USA.
 * All rights reserved. This material contains unpublished, copyrighted
 * work including confidential and proprietary information of Percussion.
 *
 * test.percussion.pso.utils MutableHttpServletRequestWrapperTest.java
 *
 */
package test.percussion.pso.utils;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import com.percussion.pso.utils.MutableHttpServletRequestWrapper;

public class MutableHttpServletRequestWrapperTest
{
   
   MutableHttpServletRequestWrapper cut;
   
   @Before
   public void setUp() throws Exception
   {
      MockHttpServletRequest request = new MockHttpServletRequest();
      request.setParameter("foo", "foo");
      request.setParameter("bar", "bar");
      request.setParameter("baz", new String[]{"baz","baz"});
      request.addHeader("foo", "bar");
      
      
      cut = new MutableHttpServletRequestWrapper(request);
   }
   @Test
   public final void testGetParameterString()
   {
      cut.setParameter("bar", "barbar"); 
      assertEquals("foo", cut.getParameter("foo")); 
      assertEquals("barbar", cut.getParameter("bar")); 
      assertNull(cut.getParameter("fizz")); 
      cut.setParameter("fizz", "fizzfizz");
      assertEquals("fizzfizz", cut.getParameter("fizz"));
      cut.setParameter("bar", new String[]{"bat","ball"});
      assertEquals("bat", cut.getParameter("bar")); 
      String [] b = cut.getParameterValues("bar"); 
      assertNotNull(b);
      assertEquals(2,b.length); 
      assertEquals("ball",b[1]); 
      
   }
   @Test
   @SuppressWarnings("unchecked")
   public final void testGetParameterNames()
   {
      cut.setParameter("biz", "bzzzz");
      Set<String> v = new HashSet<String>();
      v.add("foo");
      v.add("bar");
      v.add("baz");
      v.add("biz"); 
      int i = 0; 
      Enumeration<String> nms = cut.getParameterNames();
      while(nms.hasMoreElements())
      {
         String n = nms.nextElement(); 
         assertTrue(v.contains(n));
         i++; 
      }
      assertEquals(4,i); 
   }
   
   @Test
   public final void testGetHeader() 
   {
      cut.setHeader("baz", "bat");
      String h1 = cut.getHeader("FOO");
      assertNotNull(h1);
      assertEquals("bar",h1);
      
      h1 = cut.getHeader("baz");
      assertNotNull(h1);
      assertEquals("bat",h1);
      
      h1 = cut.getHeader("xyzzy");
      assertNull(h1); 
   }
   
   @Test
   @SuppressWarnings("unchecked")
   public final void testGetHeaders()
   {
      cut.setHeader("baz", new String[]{"bat","ball"});
      String h1 = cut.getHeader("BAZ"); 
      assertNotNull(h1);
      assertEquals("bat",h1); 
      
      Enumeration e = cut.getHeaders("baz");
      List<String> s = new ArrayList<String>();
      while(e.hasMoreElements())
      {
         s.add((String)e.nextElement());
      }
      assertEquals(2,s.size());
      assertTrue(s.contains("bat"));
      assertTrue(s.contains("ball")); 
      
      
      e = cut.getHeaders("foo");
      s = new ArrayList<String>(); 
      while(e.hasMoreElements())
      {
         s.add((String)e.nextElement());
      }
      assertEquals(1,s.size());
      assertTrue(s.contains("bar")); 
      
   }
   
   @Test
   @SuppressWarnings("unchecked")
   public final void testGetHeaderNames()
   {
      cut.setHeader("baz", "bat");
      
      Enumeration e = cut.getHeaderNames();
      List<String> s = new ArrayList<String>();
      while(e.hasMoreElements())
      {
         s.add((String)e.nextElement());
      }
      assertEquals(2,s.size());
      assertTrue(s.contains("foo"));
      assertTrue(s.contains("baz")); 
   }
}
