package weblogic.ejb.container.cmp11.rdbms;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import weblogic.ejb.container.compliance.EJBComplianceTextFormatter;
import weblogic.ejb.spi.EjbDescriptorBean;
import weblogic.j2ee.descriptor.wl60.WeblogicRdbmsJarBean;
import weblogic.xml.process.XMLParsingException;
import weblogic.xml.process.XMLProcessingException;

public abstract class CMPDDParser {
   private WeblogicRdbmsJarBean cmpDescriptor;
   private String currentEJBName;
   private String fileName = null;
   protected String encoding = null;
   private CompatibilitySettings compat;
   protected EjbDescriptorBean ejbDescriptor;
   protected static EJBComplianceTextFormatter fmt;

   public CMPDDParser() {
      fmt = new EJBComplianceTextFormatter();
   }

   public void setEJBDescriptor(EjbDescriptorBean var1) {
      this.ejbDescriptor = var1;
   }

   public void setFileName(String var1) {
      this.fileName = var1;
   }

   protected String getFileName() {
      return this.fileName;
   }

   public void setEncoding(String var1) {
      this.encoding = var1;
   }

   public void setCurrentEJBName(String var1) {
      this.currentEJBName = var1;
   }

   protected String getCurrentEJBName() {
      return this.currentEJBName;
   }

   public void setDescriptorMBean(WeblogicRdbmsJarBean var1) {
      this.cmpDescriptor = var1;
   }

   public WeblogicRdbmsJarBean getDescriptorMBean() {
      return this.cmpDescriptor;
   }

   public CompatibilitySettings getCompatibilitySettings() {
      return this.compat;
   }

   private static void p(String var0) {
      System.out.println("***<CMPDDParser> " + var0);
   }

   public abstract void process(Reader var1) throws IOException, XMLParsingException, XMLProcessingException;

   public abstract void process(InputStream var1) throws IOException, XMLParsingException, XMLProcessingException;

   public void setUseQuotedNames(boolean var1) {
      if (this.compat == null) {
         this.compat = new CompatibilitySettings();
      }

      this.compat.useQuotedNames = var1;
   }

   public void setIsolationLevel(Integer var1) {
      if (this.compat == null) {
         this.compat = new CompatibilitySettings();
      }

      this.compat.isolationLevel = var1;
   }

   protected void addFinderExpression(String var1, FinderExpression var2) {
      if (this.compat == null) {
         this.compat = new CompatibilitySettings();
      }

      Object var3 = (List)this.compat.finderExpressionMap.get(var1);
      if (var3 == null) {
         var3 = new ArrayList();
         this.compat.finderExpressionMap.put(var1, var3);
      }

      ((List)var3).add(var2);
   }

   public static class CompatibilitySettings {
      public boolean useQuotedNames = false;
      public Integer isolationLevel = null;
      public Map finderExpressionMap = new HashMap();
   }

   public static class FinderExpression {
      public int expressionNumber;
      public String expressionText;
      public String expressionType;
   }
}
