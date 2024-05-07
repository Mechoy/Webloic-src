package weblogic.management.provider.internal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import weblogic.descriptor.Descriptor;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.internal.DescriptorImpl;
import weblogic.diagnostics.debug.DebugLogger;

public final class DescriptorInfoUtils {
   private static DebugLogger debugLogger = DebugLogger.getDebugLogger("DebugConfigurationEdit");

   public static void addDescriptorInfo(DescriptorInfo var0, DescriptorImpl var1) {
      Map var2 = var1.getContext();
      Object var3 = (List)var2.get("DescriptorInfo");
      if (var3 == null) {
         var3 = new ArrayList();
         var2.put("DescriptorInfo", var3);
      }

      ((List)var3).add(var0);
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("Added descriptor info " + var0.getDescriptorBean());
      }

   }

   public static DescriptorInfo removeDescriptorInfo(DescriptorBean var0, Descriptor var1) {
      Map var2 = ((DescriptorImpl)var1).getContext();
      List var3 = (List)var2.get("DescriptorInfo");
      if (var3 == null) {
         throw new AssertionError("No DescriptorInfo list");
      } else {
         Iterator var4 = var3.iterator();

         DescriptorInfo var5;
         Descriptor var6;
         do {
            if (!var4.hasNext()) {
               return null;
            }

            var5 = (DescriptorInfo)var4.next();
            var6 = var5.getDescriptor();
         } while(var6 == null || var0 == null || !var6.equals(var0.getDescriptor()));

         var4.remove();
         addDeletedDescriptorInfo(var5, (DescriptorImpl)var1);
         if (debugLogger.isDebugEnabled()) {
            debugLogger.debug("Removed descriptor info " + var5.getDescriptorBean());
         }

         return var5;
      }
   }

   public static Iterator getDescriptorInfos(Descriptor var0) {
      Map var1 = ((DescriptorImpl)var0).getContext();
      List var2 = (List)var1.get("DescriptorInfo");
      return var2 == null ? null : var2.iterator();
   }

   public static void addDeletedDescriptorInfo(DescriptorInfo var0, DescriptorImpl var1) {
      Map var2 = var1.getContext();
      Object var3 = (List)var2.get("DeletedDescriptorInfo");
      if (var3 == null) {
         var3 = new ArrayList();
         var2.put("DeletedDescriptorInfo", var3);
      }

      ((List)var3).add(var0);
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("Added deleted descriptor info " + var0.getDescriptorBean());
      }

   }

   public static void removeAllDeletedDescriptorInfos(Descriptor var0) {
      Map var1 = ((DescriptorImpl)var0).getContext();
      var1.put("DeletedDescriptorInfo", (Object)null);
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("Clear deleted deleted descriptors");
      }

   }

   public static Iterator getDeletedDescriptorInfos(Descriptor var0) {
      Map var1 = ((DescriptorImpl)var0).getContext();
      List var2 = (List)var1.get("DeletedDescriptorInfo");
      return var2 == null ? null : var2.iterator();
   }

   public static void addNotFoundDescriptorInfo(DescriptorInfo var0, DescriptorImpl var1) {
      Map var2 = var1.getContext();
      Object var3 = (List)var2.get("NotFoundDescriptorInfo");
      if (var3 == null) {
         var3 = new ArrayList();
         var2.put("NotFoundDescriptorInfo", var3);
      }

      ((List)var3).add(var0);
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("Added not found descriptor info " + var0.getDescriptorBean());
      }

   }

   public static Iterator getNotFoundDescriptorInfos(Descriptor var0) {
      Map var1 = ((DescriptorImpl)var0).getContext();
      List var2 = (List)var1.get("NotFoundDescriptorInfo");
      return var2 == null ? null : var2.iterator();
   }

   public static boolean getDescriptorLoadExtensions(Descriptor var0) {
      Map var1 = ((DescriptorImpl)var0).getContext();
      Boolean var2 = (Boolean)var1.get("DescriptorExtensionLoad");
      return var2 == null ? true : var2;
   }

   public static void setDescriptorLoadExtensions(Descriptor var0, boolean var1) {
      Map var2 = ((DescriptorImpl)var0).getContext();
      Boolean var3 = new Boolean(var1);
      var2.put("DescriptorExtensionLoad", var3);
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("Set descriptor load extension " + var1);
      }

   }

   public static Object getDescriptorConfigExtension(Descriptor var0) {
      Map var1 = ((DescriptorImpl)var0).getContext();
      Object var2 = var1.get("DescriptorConfigExtension");
      return var2;
   }

   public static void setDescriptorConfigExtension(Descriptor var0, Object var1, String var2) {
      Map var3 = ((DescriptorImpl)var0).getContext();
      var3.put("DescriptorConfigExtension", var1);
      var3.put("DescriptorConfigExtensionAttribute", var2);
   }
}
