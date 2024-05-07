package weblogic.ejb.container.utils.ddconverter;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;

public final class EJB10MethodDescriptorStructure {
   public static final boolean debug = false;
   private String methodName;
   private String methodSig;
   private Vector methodParams;
   private Vector accessControlEntries;
   private Hashtable controlDescriptors;

   public EJB10MethodDescriptorStructure(String var1, Vector var2, Hashtable var3) {
      this.methodSig = var1;
      StringTokenizer var4 = new StringTokenizer(var1, "( ),", false);
      boolean var5 = false;
      if (var1.endsWith("()")) {
         var5 = true;
      }

      Vector var6 = new Vector();

      while(var4.hasMoreTokens()) {
         var6.addElement(var4.nextToken());
      }

      this.methodName = (String)var6.elementAt(0);
      if (var6.size() <= 1 && !var5) {
         this.methodParams = null;
      } else {
         this.methodParams = new Vector();

         for(int var7 = 1; var7 < var6.size(); ++var7) {
            this.methodParams.addElement(var6.elementAt(var7));
         }
      }

      if (var2 != null && !var2.isEmpty()) {
         this.accessControlEntries = var2;
      }

      if (var3 != null && !var3.isEmpty()) {
         this.controlDescriptors = var3;
      }

   }

   public void setMethodName(String var1) {
      this.methodName = var1;
   }

   public void setMethodParams(Vector var1) {
      this.methodParams = var1;
   }

   public void setAccessControlEntries(Vector var1) {
      this.accessControlEntries = var1;
   }

   public void setControlDescriptors(Hashtable var1) {
      this.controlDescriptors = var1;
   }

   public String getMethodSig() {
      return this.methodSig;
   }

   public String getMethodName() {
      return this.methodName;
   }

   public Vector getMethodParams() {
      return this.methodParams;
   }

   public Vector getAccessControlEntries() {
      return this.accessControlEntries;
   }

   public Hashtable getControlDescriptors() {
      return this.controlDescriptors;
   }

   public String getTransactionAttribute() {
      String var1 = "transactionAttribute";
      String var2 = null;
      if (this.controlDescriptors != null && this.controlDescriptors.containsKey(var1)) {
         var2 = (String)this.controlDescriptors.get(var1);
      }

      return var2;
   }

   public String getIsolationLevel() {
      String var1 = null;
      if (this.controlDescriptors != null && this.controlDescriptors.containsKey("isolationLevel")) {
         var1 = (String)this.controlDescriptors.get("isolationLevel");
      }

      return var1;
   }

   public String getRunAsMode() {
      String var1 = null;
      this.controlDescriptors = this.getControlDescriptors();
      if (this.controlDescriptors != null && this.controlDescriptors.containsKey("runAsMode")) {
         var1 = (String)this.controlDescriptors.get("runAsMode");
      }

      return var1;
   }

   public String getRunAsIdentity() {
      String var1 = null;
      this.controlDescriptors = this.getControlDescriptors();
      if (this.controlDescriptors != null && this.controlDescriptors.containsKey("runAsIdentity")) {
         var1 = (String)this.controlDescriptors.get("runAsIdentity");
      }

      return var1;
   }

   public String toString() {
      String var1 = new String();
      var1 = var1 + "Method Name: " + this.methodName + ": ";
      var1 = var1 + "\n" + " Method Params: ";
      int var2;
      if (this.methodParams != null) {
         for(var2 = 0; var2 < this.methodParams.size(); ++var2) {
            var1 = var1 + "(" + this.methodParams.elementAt(var2) + ") ";
         }
      } else {
         var1 = var1 + "null";
      }

      var1 = var1 + "\n" + " Access Control Entries: ";
      if (this.accessControlEntries != null) {
         for(var2 = 0; var2 < this.accessControlEntries.size(); ++var2) {
            var1 = var1 + "{" + this.accessControlEntries.elementAt(var2) + "} ";
         }
      } else {
         var1 = var1 + "null";
      }

      var1 = var1 + "\n" + " Control Descriptors: ";
      if (this.controlDescriptors != null) {
         Enumeration var5 = this.controlDescriptors.keys();

         for(int var3 = 0; var3 < this.controlDescriptors.size(); ++var3) {
            String var4 = (String)var5.nextElement();
            var1 = var1 + "[" + var4 + ": " + this.controlDescriptors.get(var4) + "]";
         }
      } else {
         var1 = var1 + "null";
      }

      return var1;
   }
}
