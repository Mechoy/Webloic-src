package weblogic.ejb.container.internal;

import javax.ejb.EnterpriseBean;
import javax.security.jacc.PolicyContextException;
import javax.xml.soap.SOAPMessage;
import weblogic.security.jacc.PolicyContextHandlerData;
import weblogic.security.service.ContextElement;
import weblogic.security.service.ContextHandler;

public class EJBContextHandler implements ContextHandler, PolicyContextHandlerData {
   public static final EJBContextHandler EMPTY = new EJBContextHandler(new String[0], new Object[0]);
   public static final String JACC_SOAP_MSG_KEY = "javax.xml.soap.SOAPMessage";
   public static final String JACC_EJB_KEY = "javax.ejb.EnterpriseBean";
   public static final String JACC_ARGUMENTS_KEY = "javax.ejb.arguments";
   private static final String[] jacc_keys = new String[]{"javax.xml.soap.SOAPMessage", "javax.ejb.EnterpriseBean", "javax.ejb.arguments"};
   private static final Object[] EMPTY_ARGS = new Object[0];
   private String[] names;
   private MethodDescriptor methodDescriptor;
   private Object[] values;
   private EnterpriseBean ejb;
   private SOAPMessage msg;

   public EJBContextHandler(EnterpriseBean var1, Object[] var2, SOAPMessage var3) {
      this.ejb = var1;
      this.values = var2 != null ? var2 : EMPTY_ARGS;
      this.msg = var3;
   }

   public EJBContextHandler(MethodDescriptor var1, Object[] var2) {
      this.methodDescriptor = var1;
      this.values = var2 != null ? var2 : EMPTY_ARGS;
   }

   private EJBContextHandler(String[] var1, Object[] var2) {
      this.names = var1;
      this.values = var2;
   }

   void setSOAPMessage(SOAPMessage var1) {
      this.msg = var1;
   }

   void setEjb(EnterpriseBean var1) {
      this.ejb = var1;
   }

   public int size() {
      return this.values.length;
   }

   public String[] getNames() {
      if (this.names == null) {
         this.names = md2paramNames(this.methodDescriptor);
      }

      return this.names;
   }

   public Object getValue(String var1) {
      try {
         return this.values[this.indexOf(var1)];
      } catch (ArrayIndexOutOfBoundsException var3) {
         return null;
      }
   }

   private int indexOf(String var1) {
      if (this.names == null) {
         this.names = md2paramNames(this.methodDescriptor);
      }

      int var2 = this.names.length / 2;

      for(int var3 = 0; var3 < this.names.length; ++var3) {
         if (var1.equals(this.names[var3])) {
            if (var3 < var2) {
               return var3;
            }

            return var3 - var2;
         }
      }

      return -1;
   }

   public ContextElement[] getValues(String[] var1) {
      ContextElement[] var2 = new ContextElement[var1.length];
      int var3 = 0;

      for(int var4 = 0; var4 < var1.length; ++var4) {
         int var5 = this.indexOf(var1[var4]);
         if (var5 != -1) {
            var2[var3++] = new ContextElement(var1[var4], this.values[var5]);
         }
      }

      if (var3 < var1.length) {
         ContextElement[] var6 = var2;
         var2 = new ContextElement[var3];
         System.arraycopy(var6, 0, var2, 0, var3);
      }

      return var2;
   }

   public Object getContext(String var1) throws PolicyContextException {
      if (var1 == null) {
         return null;
      } else if (var1.equals("javax.ejb.arguments")) {
         return this.values;
      } else if (var1.equals("javax.xml.soap.SOAPMessage")) {
         return this.msg;
      } else {
         return var1.equals("javax.ejb.EnterpriseBean") ? this.ejb : null;
      }
   }

   static String[] md2paramNames(MethodDescriptor var0) {
      int var1 = var0.getMethodInfo().getMethodParams().length;
      int var2 = var1 * 2;
      if (var2 == 0) {
         return new String[0];
      } else {
         String[] var3 = new String[var2];

         for(int var4 = 0; var4 < var1; ++var4) {
            var3[var4] = "Parameter" + (var4 + 1);
            var3[var4 + var1] = "com.bea.contextelement.ejb20.Parameter" + (var4 + 1);
         }

         return var3;
      }
   }

   public static String[] getKeys() {
      return jacc_keys;
   }
}
