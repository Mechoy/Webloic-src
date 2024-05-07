package weblogic.j2ee.dd.xml;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import weblogic.j2ee.dd.ModuleDescriptor;
import weblogic.management.descriptors.ApplicationDescriptorMBean;
import weblogic.management.descriptors.application.weblogic.EntityCacheMBean;
import weblogic.xml.process.SAXValidationException;
import weblogic.xml.process.XMLParsingException;
import weblogic.xml.process.XMLProcessingException;

public abstract class WADDLoader {
   private static final boolean debug = System.getProperty("weblogic.j2ee.dd.xml") != null;
   private static boolean verbose = System.getProperty("weblogic.j2ee.dd.xml") != null;
   protected ApplicationDescriptorMBean applicationDescriptor = null;
   private ModuleDescriptor currentModuleContext;
   protected boolean validate = true;

   public void setApplicationDescriptor(ApplicationDescriptorMBean var1) {
      this.applicationDescriptor = var1;
   }

   public ApplicationDescriptorMBean getApplicationDescriptor() {
      return this.applicationDescriptor;
   }

   public void setValidate(boolean var1) {
      this.validate = var1;
   }

   public abstract void process(String var1) throws IOException, XMLParsingException, XMLProcessingException;

   public abstract void process(File var1) throws IOException, XMLParsingException, XMLProcessingException;

   public abstract void process(InputStream var1) throws IOException, XMLParsingException, XMLProcessingException;

   protected void validatePositiveInteger(String var1) throws Exception {
      int var2;
      try {
         var2 = Integer.parseInt(var1);
      } catch (NumberFormatException var4) {
         throw new Exception("Parameter must be an integer");
      }

      if (var2 < 0) {
         throw new Exception("Parameter must be a positive integer");
      }
   }

   protected void validateIntegerGreaterThanNegativeOne(String var1) throws Exception {
      int var2;
      try {
         var2 = Integer.parseInt(var1);
      } catch (NumberFormatException var4) {
         throw new Exception("Parameter must be an integer");
      }

      if (var2 < -1) {
         throw new Exception("Parameter must be an integer greater than -1");
      }
   }

   protected void validateIntegerGreaterThanZero(String var1) throws Exception {
      int var2;
      try {
         var2 = Integer.parseInt(var1);
      } catch (NumberFormatException var4) {
         throw new Exception("Parameter must be an integer");
      }

      if (var2 <= 0) {
         throw new Exception("Parameter must be an integer greater 0");
      }
   }

   protected void validateStmtCacheSize(String var1) throws Exception {
      int var2;
      try {
         var2 = Integer.parseInt(var1);
      } catch (NumberFormatException var4) {
         throw new Exception("Parameter must be an integer");
      }

      if (var2 < 0 || var2 > 1024) {
         throw new Exception("Parameter must be an integer greater than (0) and lesser than (1024)");
      }
   }

   protected void validateStmtCacheType(String var1) throws Exception {
      if (!var1.equals("LRU") && !var1.equals("FIXED")) {
         throw new Exception("Statement cache type must either be (LRU) or (FIXED)");
      }
   }

   protected void validateCachingStrategy(EntityCacheMBean var1, String var2) throws SAXValidationException {
      SAXValidationException var3 = null;
      if (!"Exclusive".equalsIgnoreCase(var2) && !"MultiVersion".equalsIgnoreCase(var2)) {
         var3 = new SAXValidationException("Invalid caching-strategy of '" + var2 + "' specified for entity-cache '" + var1.getEntityCacheName() + "'.  Value must be 'Exclusive' or 'MultiVersion'.");
         throw var3;
      }
   }

   protected void setCurrentModuleContext(ModuleDescriptor var1) {
      this.currentModuleContext = var1;
   }

   protected ModuleDescriptor getCurrentModuleContext() {
      return this.currentModuleContext;
   }
}
