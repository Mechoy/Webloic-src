package weblogic.management.mbeanservers.internal;

import javax.management.ObjectName;
import weblogic.security.service.ContextElement;
import weblogic.security.service.ContextHandler;

public class JMXContextHandler implements ContextHandler {
   private static final String[] common_keys = new String[]{"com.bea.contextelement.jmx.ObjectName", "com.bea.contextelement.jmx.ShortName"};
   private static final String[] invoke_keys = new String[]{"com.bea.contextelement.jmx.ObjectName", "com.bea.contextelement.jmx.ShortName", "com.bea.contextelement.jmx.Parameters", "com.bea.contextelement.jmx.Signature", "com.bea.contextelement.jmx.AuditProtectedArgInfo", "com.bea.contextelement.jmx.OldAttributeValue"};
   private final ObjectName objectName;
   private final String[] keys;
   private final Object[] parameters;
   private final String[] signature;
   private final String auditProtectedArgInfo;
   private final Object oldAttributeValue;

   public JMXContextHandler(ObjectName var1) {
      this.objectName = var1;
      this.keys = common_keys;
      this.parameters = null;
      this.signature = null;
      this.auditProtectedArgInfo = null;
      this.oldAttributeValue = null;
   }

   public JMXContextHandler(ObjectName var1, Object[] var2, String[] var3, String var4, Object var5) {
      this.objectName = var1;
      this.parameters = var2;
      this.signature = var3;
      this.auditProtectedArgInfo = var4;
      this.keys = invoke_keys;
      this.oldAttributeValue = var5;
   }

   public int size() {
      return this.keys.length;
   }

   public String[] getNames() {
      return this.keys;
   }

   public Object getValue(String var1) {
      if ("com.bea.contextelement.jmx.ObjectName".equals(var1)) {
         return this.objectName;
      } else if ("com.bea.contextelement.jmx.ShortName".equals(var1) && this.objectName != null) {
         return this.objectName.getKeyProperty("Name");
      } else if ("com.bea.contextelement.jmx.Parameters".equals(var1)) {
         return this.parameters;
      } else if ("com.bea.contextelement.jmx.Signature".equals(var1)) {
         return this.signature;
      } else if ("com.bea.contextelement.jmx.AuditProtectedArgInfo".equals(var1)) {
         return this.auditProtectedArgInfo;
      } else {
         return "com.bea.contextelement.jmx.OldAttributeValue".equals(var1) ? this.oldAttributeValue : null;
      }
   }

   public ContextElement[] getValues(String[] var1) {
      ContextElement[] var2 = new ContextElement[var1.length];
      int var3 = 0;

      for(int var4 = 0; var4 < var1.length; ++var4) {
         Object var5 = this.getValue(var1[var4]);
         if (var5 != null) {
            var2[var3++] = new ContextElement(var1[var4], var5);
         }
      }

      if (var3 < var1.length) {
         ContextElement[] var6 = var2;
         var2 = new ContextElement[var3];
         System.arraycopy(var6, 0, var2, 0, var3);
      }

      return var2;
   }
}
