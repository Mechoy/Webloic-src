package weblogic.ejb.container.dd;

import java.util.Collections;
import java.util.Iterator;
import weblogic.ejb.container.dd.xml.DDUtils;
import weblogic.ejb.container.deployer.IncomprehensibleMethodSignatureException;

public final class QueryDescriptor extends BaseDescriptor {
   private static boolean debug = System.getProperty("weblogic.ejb.deployment.debug") != null;
   private String description;
   private String methodName;
   private String[] methodParams;
   private String queryText;

   public QueryDescriptor() {
      super((String)null);
   }

   public void setDescription(String var1) {
      if (debug) {
         System.err.println("setDescription(" + var1 + ")");
      }

      this.description = var1;
   }

   public String getDescription() {
      return this.description;
   }

   public String getMethodSignature() {
      return DDUtils.getMethodSignature(this.methodName, this.methodParams);
   }

   public String getMethodName() {
      return this.methodName;
   }

   public String[] getMethodParams() {
      return this.methodParams;
   }

   public void setMethodSignature(String var1) throws IncomprehensibleMethodSignatureException {
      if (debug) {
         System.err.println("setMethodSignature(" + var1 + ")");
      }

      this.methodName = DDUtils.getMethodName(var1);
      this.methodParams = DDUtils.getMethodParams(var1);
   }

   public void setMethodSignature(String var1, String[] var2) {
      if (debug) {
         System.err.println("setMethodSignature(" + var1 + "," + var2 + ")");
      }

      this.methodName = var1;
      this.methodParams = var2;
   }

   public void setQueryText(String var1) {
      if (debug) {
         System.err.println("setQueryText(" + var1 + ")");
      }

      this.queryText = var1;
   }

   public String getQueryText() {
      return this.queryText;
   }

   public void validateSelf() {
   }

   public Iterator getSubObjectsIterator() {
      return Collections.EMPTY_SET.iterator();
   }
}
