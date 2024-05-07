package weblogic.management.security.internal.compatibility;

import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import javax.management.ObjectName;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import weblogic.management.ManagementRuntimeException;
import weblogic.management.commo.Commo;
import weblogic.management.commo.SecurityMBeanData;
import weblogic.utils.Debug;
import weblogic.utils.StringUtils;

public class ProviderMigrationDriver {
   String configFileLocation = null;
   List secMBeanData = null;
   static boolean debugInit = false;
   static String _debugInit = System.getProperty("weblogic.DebugUserConfigLoad", "false");

   public ProviderMigrationDriver(String var1, List var2) {
      this.configFileLocation = var1;
      this.secMBeanData = var2;
      if (_debugInit.equals("true")) {
         debugInit = true;
      }

   }

   void printDebug(String var1) {
      if (debugInit) {
         Debug.say(var1);
      }

   }

   private Element setAttributes(Element var1, Vector var2) {
      Iterator var3 = var2.iterator();

      while(true) {
         while(var3.hasNext()) {
            Commo.Pair var4 = (Commo.Pair)var3.next();
            String var5 = var4.getName();
            String var6 = "";
            Object var7 = var4.getValue();
            if (var7 instanceof Object[]) {
               Object[] var8 = (Object[])((Object[])var7);
               String[] var9 = new String[var8.length];

               for(int var10 = 0; var10 < var8.length; ++var10) {
                  Object var11 = var8[var10];
                  if (var11 instanceof ObjectName) {
                     var9[var10] = ((ObjectName)var11).toString();
                  } else {
                     var9[var10] = var11.toString();
                  }
               }

               var6 = StringUtils.join(var9, "|");
               var1.setAttribute(var5, var6);
            } else {
               var1.setAttribute(var5, var7.toString());
            }
         }

         return var1;
      }
   }

   private String getDisplayNameFromData(SecurityMBeanData var1) {
      try {
         Commo.Pair[] var2 = var1.getAdditionalDescriptorElements();
         if (var2 == null) {
            return null;
         } else {
            for(int var3 = 0; var3 < var2.length; ++var3) {
               Commo.Pair var4 = var2[var3];
               if (var4.getName().equals("displayname")) {
                  return (String)var4.getValue();
               }
            }

            return null;
         }
      } catch (Exception var5) {
         throw new ManagementRuntimeException(var5);
      }
   }

   public InputStream convert(InputStream var1) {
      ConfigXMLFile var2 = null;

      try {
         var2 = new ConfigXMLFile(var1);
         var2.parse(new InsertSecurityMBeansHandler(this.secMBeanData));
         return var2.getInputStream();
      } catch (Exception var4) {
         throw new ManagementRuntimeException(var4);
      }
   }

   class InsertSecurityMBeansHandler extends ConfigFileHandler {
      List secData = null;

      protected InsertSecurityMBeansHandler(List var2) {
         this.secData = var2;
      }

      protected void startElement(String var1, XMLAttributeList var2, Node var3) {
         if (var1.equals("Security")) {
            try {
               Node var4 = var3;
               ProviderMigrationDriver.this.printDebug("Found security node.  Adding providers nodes as children.");
               Iterator var5 = this.secData.iterator();

               while(var5.hasNext()) {
                  SecurityMBeanData var6 = (SecurityMBeanData)var5.next();
                  String var7 = var6.getInstanceName();
                  String var8 = ProviderMigrationDriver.this.getDisplayNameFromData(var6);
                  ObjectName var9 = new ObjectName(var6.getTypeName());
                  String var10 = Commo.getTypeShortName(var9);
                  ProviderMigrationDriver.this.printDebug("Type : " + var10);
                  ProviderMigrationDriver.this.printDebug("Name : " + var7);
                  Vector var11 = var6.getAttrValues();

                  try {
                     Element var12 = this.getDocument().createElement(var10);
                     if (var8 != null) {
                        var12.setAttribute("DisplayName", var8);
                     }

                     if (var7 != null) {
                        var12.setAttribute("Name", var7);
                     }

                     var12 = ProviderMigrationDriver.this.setAttributes(var12, var11);
                     var4.appendChild(var12);
                     ProviderMigrationDriver.this.printDebug("Provider node that is added. " + var10);
                  } catch (Exception var13) {
                     throw new ManagementRuntimeException(var13);
                  }
               }

            } catch (Exception var14) {
               throw new ManagementRuntimeException(var14);
            }
         }
      }
   }
}
