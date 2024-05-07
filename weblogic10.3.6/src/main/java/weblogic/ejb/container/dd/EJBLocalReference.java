package weblogic.ejb.container.dd;

import java.util.Collections;
import java.util.Iterator;

public class EJBLocalReference extends BaseDescriptor {
   private static boolean debug = System.getProperty("weblogic.ejb.deployment.debug") != null;
   private String refDesc;
   private String refName;
   private String refType;
   private String refLocalHome;
   private String refLocal;
   private String refEjbName;
   private String refJndiName;

   public EJBLocalReference() {
      super((String)null);
   }

   public EJBLocalReference(String var1, String var2, String var3, String var4, String var5, String var6) {
      super((String)null);
      this.setDescription(var1);
      this.setName(var2);
      this.setRefType(var3);
      this.setLocalHomeInterfaceName(var4);
      this.setLocalInterfaceName(var5);
      this.setLinkedEjbName(var6);
   }

   public void setDescription(String var1) {
      if (debug) {
         System.err.println("EJBLocalReference[" + System.identityHashCode(this) + "].setDescription(" + var1 + ")");
      }

      this.refDesc = var1;
   }

   public String getDescription() {
      return this.refDesc;
   }

   public void setName(String var1) {
      if (debug) {
         System.err.println("EJBLocalReference[" + System.identityHashCode(this) + "].setName(" + var1 + ")");
      }

      this.refName = var1;
   }

   public String getName() {
      return this.refName;
   }

   public void setRefType(String var1) {
      if (debug) {
         System.err.println("EJBLocalReference[" + System.identityHashCode(this) + "].setRefType(" + var1 + ")");
      }

      this.refType = var1;
   }

   public String getRefType() {
      return this.refType;
   }

   public void setLocalHomeInterfaceName(String var1) {
      if (debug) {
         System.err.println("EJBLocalReference[" + System.identityHashCode(this) + "].setLocalHomeInterfaceName(" + var1 + ")");
      }

      this.refLocalHome = var1;
   }

   public String getLocalHomeInterfaceName() {
      return this.refLocalHome;
   }

   public void setLocalInterfaceName(String var1) {
      if (debug) {
         System.err.println("EJBLocalReference[" + System.identityHashCode(this) + "].setLocalInterfaceName(" + var1 + ")");
      }

      this.refLocal = var1;
   }

   public String getLocalInterfaceName() {
      return this.refLocal;
   }

   public void setLinkedEjbName(String var1) {
      if (debug) {
         System.err.println("EJBLocalReference[" + System.identityHashCode(this) + "].setLinkedEjbName(" + var1 + ")");
      }

      this.refEjbName = var1;
   }

   public String getLinkedEjbName() {
      return this.refEjbName;
   }

   public void setJNDIName(String var1) {
      if (debug) {
         System.err.println("EJBLocalReference[" + System.identityHashCode(this) + "].setJNDIName(" + var1 + ")");
      }

      this.refJndiName = var1;
   }

   public String getJNDIName() {
      return this.refJndiName;
   }

   public void validateSelf() {
   }

   public Iterator getSubObjectsIterator() {
      return Collections.EMPTY_SET.iterator();
   }
}
