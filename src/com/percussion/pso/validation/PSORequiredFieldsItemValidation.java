/*
 * COPYRIGHT (c) 1999 - 2008 by Percussion Software, Inc., Woburn, MA USA.
 * All rights reserved. This material contains unpublished, copyrighted
 * work including confidential and proprietary information of Percussion.
 *
 * com.percussion.pso.validation PSORequiredFieldsItemValidation.java
 *
 */
package com.percussion.pso.validation;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.percussion.extension.IPSItemValidator;
import com.percussion.server.IPSRequestContext;
import com.percussion.util.IPSHtmlParameters;
import com.percussion.util.PSItemErrorDoc;

/**
 * An item validation exit that checks for required fields.
 * There are 2 required parameters: 
 * <ul>
 * <li>The required fields. </li>
 * <li>Destination workflow states. </li>
 * </ul>
 * Both of these parameters are comma separated lists of values.  
 * If the item is transitioning into one of the listed states, then the required fields will be checked.
 * <p>
 * Note that some field types (particularly rich text fields) may appear to be blank when they actually 
 * contain data.  This extension will treat these fields as if they are <b>not</b> empty.  
 *
 * @author DavidBenua
 *
 */
public class PSORequiredFieldsItemValidation
      extends
         PSOAbstractItemValidationExit implements IPSItemValidator
{
   private static Log log = LogFactory.getLog(PSORequiredFieldsItemValidation.class);
   /**
    * 
    */
   public PSORequiredFieldsItemValidation()
   {
      super(); 
   }
   /**
    * @see com.percussion.pso.validation.PSOAbstractItemValidationExit#validateDocs(org.w3c.dom.Document, org.w3c.dom.Document, com.percussion.server.IPSRequestContext, java.lang.Object[])
    */
   @Override
   protected void validateDocs(Document inputDoc, Document errorDoc,
         IPSRequestContext req, Object[] params) throws Exception
   {
      String contentid = req.getParameter(IPSHtmlParameters.SYS_CONTENTID);
      Validate.notEmpty(contentid);
      String transitionid = req.getParameter(IPSHtmlParameters.SYS_TRANSITIONID);      
      
      if(transitionid == null || transitionid == ""){
    	  transitionid = "123455";
      }
      
      Validate.notEmpty(transitionid);
      String fields = params[0].toString();
      Validate.notEmpty(fields);
      String states = params[1].toString();
    

      
      if(super.matchDestinationState(contentid, transitionid, states))
      {
         log.debug("Validating item " + contentid);
         List<String> flds = splitAndTrim(fields);  
         for(String f : flds)
         {
            log.debug("validating field " + f);
            Element e = super.getFieldElement(inputDoc, f);
            if(e == null)
            {
               log.debug("field missing - " + f);
               PSItemErrorDoc.addError(errorDoc, f, f, "Required field not found ", new Object[]{f});
               continue;
            }
            if (isMultiValue(e))
            {
               List<String> vals = getFieldValues(e);
               if (vals == null || vals.size() == 0)
               {
                  log.debug("multivalue field is empty - " + f);
                  String label = getFieldLabel(e);
                  PSItemErrorDoc.addError(errorDoc, f, label,
                        "Required field {0} has no values ",
                        new Object[]{label});
               }
            } else
            { // single value field
               String val = getFieldValue(e);
               log.debug("found value " + val);
               if (StringUtils.isBlank(val))
               {
                  log.debug("field blank - " + f);
                  String label = getFieldLabel(e);
                  PSItemErrorDoc.addError(errorDoc, f, label,
                        "Required field is blank ", new Object[]{label});
               }
            }
         }
         log.debug("validation complete "); 
      }
   }
}
