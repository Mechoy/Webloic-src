package weblogic.servlet.ejb2jsp.dd;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.w3c.dom.Element;
import weblogic.servlet.ejb2jsp.SourceMethodInfo;
import weblogic.servlet.internal.dd.ToXML;
import weblogic.utils.io.XMLWriter;
import weblogic.xml.dom.DOMProcessingException;
import weblogic.xml.dom.DOMUtils;

public class BeanDescriptor implements ToXML, Serializable {
   private EJBMethodDescriptor[] ejbMethods;
   private EJBMethodDescriptor[] homeMethods;
   private String ejbName;
   private String remoteType;
   private String homeType;
   private String ejbType;
   private String jndiName;
   private boolean enabled;
   private static final BaseEJB[] baseEJBMethods = new BaseEJB[]{new BaseEJB("javax.ejb.EJBHome", "getEJBHome", (String)null), new BaseEJB("javax.ejb.Handle", "getHandle", (String)null), new BaseEJB("java.lang.Object", "getPrimaryKey", (String)null), new BaseEJB("boolean", "isIdentical", "javax.ejb.EJBObject"), new BaseEJB("void", "remove", (String)null)};
   private static final BaseEJB[] baseHomeMethods = new BaseEJB[]{new BaseEJB("javax.ejb.EJBMetaData", "getEJBMetaData", (String)null), new BaseEJB("javax.ejb.HomeHandle", "getHomeHandle", (String)null), new BaseEJB("void", "remove", "java.lang.Object"), new BaseEJB("void", "remove", "javax.ejb.Handle")};

   static void p(String var0) {
      System.err.println("[EJBTagDesc]: " + var0);
   }

   public BeanDescriptor() {
      this.ejbMethods = this.homeMethods = new EJBMethodDescriptor[0];
      this.remoteType = this.homeType = this.ejbType = this.ejbName = this.jndiName = "";
      this.enabled = true;
   }

   public BeanDescriptor(Element var1) throws DOMProcessingException {
      this.initFromRoot(var1);
   }

   private void initFromRoot(Element var1) throws DOMProcessingException {
      Element var2 = null;
      String var3 = null;
      this.ejbName = DOMUtils.getValueByTagName(var1, "ejb-name");
      this.remoteType = DOMUtils.getValueByTagName(var1, "remote-type");
      this.homeType = DOMUtils.getValueByTagName(var1, "home-type");
      this.jndiName = DOMUtils.getValueByTagName(var1, "jndi-name");
      this.ejbType = DOMUtils.getValueByTagName(var1, "ejb-type");
      var3 = DOMUtils.getValueByTagName(var1, "enabled");
      if (!"false".equalsIgnoreCase(var3) && !"no".equalsIgnoreCase(var3)) {
         this.enabled = true;
      } else {
         this.enabled = false;
      }

      var2 = DOMUtils.getElementByTagName(var1, "ejb-methods");
      List var4 = DOMUtils.getOptionalElementsByTagName(var2, "method");
      ArrayList var5 = new ArrayList();
      Iterator var6 = var4.iterator();

      while(var6.hasNext()) {
         var5.add(new EJBMethodDescriptor((Element)var6.next()));
      }

      this.ejbMethods = new EJBMethodDescriptor[var5.size()];
      var5.toArray(this.ejbMethods);
      var5.clear();
      var2 = DOMUtils.getElementByTagName(var1, "home-methods");
      var4 = DOMUtils.getOptionalElementsByTagName(var2, "method");
      var6 = var4.iterator();

      while(var6.hasNext()) {
         var5.add(new EJBMethodDescriptor((Element)var6.next()));
      }

      this.homeMethods = new EJBMethodDescriptor[var5.size()];
      var5.toArray(this.homeMethods);
      this.resolveBaseMethods();
   }

   public String toString() {
      return "EJB: " + this.getEJBName();
   }

   public String getRemoteType() {
      return this.remoteType;
   }

   public void setRemoteType(String var1) {
      this.remoteType = var1;
   }

   public String getEJBName() {
      return this.ejbName;
   }

   public void setEJBName(String var1) {
      this.ejbName = var1;
   }

   public String getHomeType() {
      return this.homeType;
   }

   public void setHomeType(String var1) {
      this.homeType = var1;
   }

   public String getEJBType() {
      return this.ejbType;
   }

   public void setEJBType(String var1) {
      this.ejbType = var1;
   }

   public boolean isEnabled() {
      return this.enabled;
   }

   public void setEnabled(boolean var1) {
      this.enabled = var1;
   }

   public String getJNDIName() {
      return this.jndiName;
   }

   public void setJNDIName(String var1) {
      this.jndiName = var1;
   }

   public EJBMethodDescriptor[] getEJBMethods() {
      return this.ejbMethods != null ? this.ejbMethods : new EJBMethodDescriptor[0];
   }

   public void setEJBMethods(EJBMethodDescriptor[] var1) {
      if (var1 == null) {
         this.ejbMethods = new EJBMethodDescriptor[0];
      } else {
         this.ejbMethods = (EJBMethodDescriptor[])((EJBMethodDescriptor[])var1.clone());
      }
   }

   public EJBMethodDescriptor[] getHomeMethods() {
      return this.homeMethods != null ? this.homeMethods : new EJBMethodDescriptor[0];
   }

   public void setHomeMethods(EJBMethodDescriptor[] var1) {
      if (var1 == null) {
         this.homeMethods = new EJBMethodDescriptor[0];
      } else {
         this.homeMethods = (EJBMethodDescriptor[])((EJBMethodDescriptor[])var1.clone());
      }
   }

   public EJBMethodDescriptor[] getUnresolvedMethods() {
      ArrayList var1 = new ArrayList();
      EJBMethodDescriptor[] var2 = this.getEJBMethods();

      int var3;
      for(var3 = 0; var3 < var2.length; ++var3) {
         if (var2[var3].isEnabled() && !var2[var3].isResolved()) {
            var1.add(var2[var3]);
         }
      }

      var2 = this.getHomeMethods();

      for(var3 = 0; var3 < var2.length; ++var3) {
         if (var2[var3].isEnabled() && !var2[var3].isResolved()) {
            var1.add(var2[var3]);
         }
      }

      EJBMethodDescriptor[] var4 = new EJBMethodDescriptor[var1.size()];
      var1.toArray(var4);
      return var4;
   }

   public void resolveSource(SourceMethodInfo var1) {
      EJBMethodDescriptor[] var2 = this.getUnresolvedMethods();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         if (var1.equalsMethod(var2[var3])) {
            var2[var3].resolveParamNames(var1);
         }
      }

   }

   public boolean isStatefulBean() {
      return "ENTITY".equalsIgnoreCase(this.getEJBType()) || "STATEFUL".equalsIgnoreCase(this.getEJBType());
   }

   public void setEnableBaseEJB(boolean var1) {
      EJBMethodDescriptor[] var2 = this.getEJBMethods();

      int var3;
      int var4;
      for(var3 = 0; var3 < var2.length; ++var3) {
         for(var4 = 0; var4 < baseEJBMethods.length; ++var4) {
            if (baseEJBMethods[var4].matchesDescriptor(var2[var3])) {
               var2[var3].setEnabled(var1);
            }
         }
      }

      var2 = this.getHomeMethods();

      for(var3 = 0; var3 < var2.length; ++var3) {
         for(var4 = 0; var4 < baseHomeMethods.length; ++var4) {
            if (baseHomeMethods[var4].matchesDescriptor(var2[var3])) {
               var2[var3].setEnabled(var1);
            }
         }
      }

   }

   public void resolveBaseMethods() {
      SourceMethodInfo var1 = null;
      ArrayList var2 = new ArrayList();
      ArrayList var3 = new ArrayList();
      var1 = new SourceMethodInfo("getEJBHome", "javax.ejb.EJBHome", var2, var3);
      this.resolveSource(var1);
      var1 = new SourceMethodInfo("getHandle", "javax.ejb.Handle", var2, var3);
      this.resolveSource(var1);
      var1 = new SourceMethodInfo("getPrimaryKey", "java.lang.Object", var2, var3);
      this.resolveSource(var1);
      var1 = new SourceMethodInfo("remove", "void", var2, var3);
      this.resolveSource(var1);
      var2.add("javax.ejb.EJBObject");
      var3.add("other");
      var1 = new SourceMethodInfo("isIdentical", "boolean", var2, var3);
      this.resolveSource(var1);
      if (this.isStatefulBean()) {
         var2.clear();
         var3.clear();
         var1 = new SourceMethodInfo("getEJBMetaData", "javax.ejb.EJBMetaData", var2, var3);
         this.resolveSource(var1);
         var1 = new SourceMethodInfo("getHomeHandle", "javax.ejb.HomeHandle", var2, var3);
         this.resolveSource(var1);
         var2.add("java.lang.Object");
         var3.add("object");
         var1 = new SourceMethodInfo("remove", "void", var2, var3);
         this.resolveSource(var1);
         var2.clear();
         var3.clear();
         var2.add("javax.ejb.Handle");
         var3.add("handle");
         var1 = new SourceMethodInfo("remove", "void", var2, var3);
         this.resolveSource(var1);
         EJBMethodDescriptor[] var4 = this.getHomeMethods();
         boolean var5 = false;

         for(int var6 = 0; var6 < var4.length; ++var6) {
            EJBMethodDescriptor var7 = var4[var6];
            if ("remove".equals(var7.getName()) && this.getHomeType().equals(var7.getTargetType()) && "home-remove".equals(var7.getTagName())) {
               MethodParamDescriptor[] var8 = var7.getParams();
               if (var8.length == 1 && var8[0].getType().equals("javax.ejb.Handle")) {
                  var7.setTagName("home-remove-handle");
                  var5 = true;
               }
            }
         }

      }
   }

   public String[] getErrors() {
      if (!this.isEnabled()) {
         return new String[0];
      } else {
         ArrayList var1 = new ArrayList();
         this.getDuplicateTagNames(var1);
         this.getDuplicateAttributeNames(var1);
         EJBMethodDescriptor[] var2 = this.getUnresolvedMethods();

         for(int var3 = 0; var2 != null && var3 < var2.length; ++var3) {
            String var4 = "tag " + var2[var3].getTagName() + " for method " + var2[var3].getName() + " on " + var2[var3].getTargetType() + " appears to " + "have meaningless parameter names (arg0,arg1,....)";
            var1.add(var4);
         }

         String[] var5 = new String[var1.size()];
         var1.toArray(var5);
         return var5;
      }
   }

   void getDuplicateAttributeNames(List var1) {
      this.getDuplicateAttributeNames(var1, this.getEJBMethods());
      this.getDuplicateAttributeNames(var1, this.getHomeMethods());
   }

   private void getDuplicateAttributeNames(List var1, EJBMethodDescriptor[] var2) {
      for(int var3 = 0; var3 < var2.length; ++var3) {
         EJBMethodDescriptor var4 = var2[var3];
         if (var4.isEnabled()) {
            MethodParamDescriptor[] var5 = var4.getParams();
            int var6 = var5.length;
            if (var6 >= 2) {
               for(int var7 = 0; var7 < var6 - 1; ++var7) {
                  String var8 = var5[var7].getName();

                  for(int var9 = var7 + 1; var9 < var6; ++var9) {
                     if (var8.equals(var5[var9].getName())) {
                        String var10 = "duplicate attribute name \"" + var8 + "\" for tag: " + var4.getTagName() + " signature: " + var4.getSignature();
                        var1.add(var10);
                     }
                  }
               }
            }
         }
      }

   }

   private void getDuplicateTagNames(List var1) {
      this.getDuplicateTagNames(var1, this.getEJBMethods());
      this.getDuplicateTagNames(var1, this.getHomeMethods());
   }

   private void getDuplicateTagNames(List var1, EJBMethodDescriptor[] var2) {
      if (var2 != null) {
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3 - 1; ++var4) {
            if (var2[var4].isEnabled()) {
               String var5 = var2[var4].getTagName();

               for(int var6 = var4 + 1; var6 < var3; ++var6) {
                  if (var2[var6].isEnabled() && var5 != null && var5.equals(var2[var6].getTagName())) {
                     String var7 = "duplicate tag names \"" + var5 + "\" refer to methods \"" + var2[var4].getSignature() + "\" and \"" + var2[var6].getSignature() + "\"";
                     var1.add(var7);
                  }
               }
            }
         }

      }
   }

   public void toXML(XMLWriter var1) {
      var1.println("<ejb>");
      var1.incrIndent();
      var1.println("<ejb-name>" + this.ejbName + "</ejb-name>");
      var1.println("<remote-type>" + this.remoteType + "</remote-type>");
      var1.println("<home-type>" + this.homeType + "</home-type>");
      var1.println("<jndi-name>" + this.jndiName + "</jndi-name>");
      var1.println("<ejb-type>" + this.ejbType + "</ejb-type>");
      var1.println("<enabled>" + this.isEnabled() + "</enabled>");
      var1.println("<ejb-methods>");
      var1.incrIndent();

      int var2;
      for(var2 = 0; this.ejbMethods != null && var2 < this.ejbMethods.length; ++var2) {
         this.ejbMethods[var2].toXML(var1);
      }

      var1.decrIndent();
      var1.println("</ejb-methods>");
      var1.println("<home-methods>");
      var1.incrIndent();

      for(var2 = 0; this.homeMethods != null && var2 < this.homeMethods.length; ++var2) {
         this.homeMethods[var2].toXML(var1);
      }

      var1.decrIndent();
      var1.println("</home-methods>");
      var1.decrIndent();
      var1.println("</ejb>");
   }
}
