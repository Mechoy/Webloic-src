package weblogic.connector.common;

import java.security.AccessController;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import weblogic.connector.deploy.DeployerUtil;
import weblogic.connector.exception.RAException;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.utils.classloaders.GenericClassLoader;

public class LinkrefManager {
   private static Hashtable baseRAs = new Hashtable();
   private static Hashtable linkrefRAs = new Hashtable();
   private static final String CLASS_NAME = "weblogic.connector.common.LinkrefManager";

   public static synchronized void addBaseRA(RAInstanceManager var0) {
      Debug.enter("weblogic.connector.common.LinkrefManager", "addBaseRA(...)");

      try {
         String var1 = var0.getRAInfo().getConnectionFactoryName();
         Debug.println("weblogic.connector.common.LinkrefManager", ".addBaseRA() Connection factory name : " + var1);
         if (var1 != null && var1.length() > 0) {
            Debug.println("weblogic.connector.common.LinkrefManager", ".addBaseRA() Adding the base RA to the hashtable");
            baseRAs.put(var1, var0);
         }
      } finally {
         Debug.exit("weblogic.connector.common.LinkrefManager", "addBaseRA(...)");
      }

   }

   public static synchronized void removeBaseRA(RAInstanceManager var0, RAException var1) {
      Debug.enter("weblogic.connector.common.LinkrefManager", "removeBaseRA(...)");

      try {
         String var2 = var0.getRAInfo().getConnectionFactoryName();
         if (var2 != null && var2.length() > 0) {
            Debug.println("weblogic.connector.common.LinkrefManager", ".removeBaseRA() Removing base RA with connection factory name : " + var2);
            baseRAs.remove(var2);
         }
      } catch (Throwable var7) {
         var1.addError(var7);
      } finally {
         Debug.exit("weblogic.connector.common.LinkrefManager", "removeBaseRA(...)");
      }

   }

   public static synchronized void addLinkrefRA(RAInstanceManager var0) {
      Debug.enter("weblogic.connector.common.LinkrefManager", "addLinkrefRA(...)");

      try {
         String var1 = var0.getRAInfo().getLinkref();
         Debug.println("weblogic.connector.common.LinkrefManager", ".addLinkrefRA() Adding linkref with connectionFactory : " + var1);
         if (var1 != null && var1.length() > 0) {
            Object var2 = (List)linkrefRAs.get(var1);
            if (var2 == null) {
               Debug.println("weblogic.connector.common.LinkrefManager", ".addLinkrefRA() This is the first linkref RA being added under the connection factory name");
               var2 = new Vector(10);
               linkrefRAs.put(var1, var2);
            }

            if (!((List)var2).contains(var0)) {
               Debug.println("weblogic.connector.common.LinkrefManager", ".addLinkrefRA() Adding to the list");
               ((List)var2).add(var0);
            }
         } else {
            Debug.throwAssertionError("The linkref does not have a connection factory defined.");
         }
      } finally {
         Debug.exit("weblogic.connector.common.LinkrefManager", "addLinkrefRA(...)");
      }

   }

   public static synchronized void removeLinkrefRA(RAInstanceManager var0, RAException var1) {
      Debug.enter("weblogic.connector.common.LinkrefManager", "removeLinkrefRA(...)");

      try {
         String var2 = var0.getRAInfo().getLinkref();
         if (var2 != null && var2.length() > 0) {
            List var3 = (List)linkrefRAs.get(var2);
            if (var3 != null) {
               var3.remove(var0);
               if (var3.size() == 0) {
                  linkrefRAs.remove(var2);
               }
            }
         } else {
            Debug.throwAssertionError("The connection factory for the linkref has not been specified.");
         }
      } catch (Throwable var8) {
         var1.addError(var8);
      } finally {
         Debug.exit("weblogic.connector.common.LinkrefManager", "removeLinkrefRA(...)");
      }

   }

   public static void deployDependentLinkrefs(RAInstanceManager var0) {
      Debug.enter("weblogic.connector.common.LinkrefManager", "deployDependentLinkrefs(...)");

      try {
         String var1 = var0.getRAInfo().getConnectionFactoryName();
         Debug.println("Connection factory name : " + var1);
         if (var1 != null && var1.length() > 0) {
            Debug.println("Get the list of linkrefs waiting to be deployed");
            List var2 = (List)linkrefRAs.get(var1);
            Vector var3 = new Vector(10);
            Vector var4 = new Vector(10);
            if (var2 != null) {
               Debug.println("Number of linkref to be deployed : " + var2.size());
               Iterator var6 = var2.iterator();

               while(var6.hasNext()) {
                  RAInstanceManager var5 = (RAInstanceManager)var6.next();

                  try {
                     Debug.println("Update the classloader with the base jar");
                     DeployerUtil.updateClassFinder((GenericClassLoader)var5.getClassloader(), var0.getJarFile(), var5.getRAInfo().isEnableGlobalAccessToClasses(), var5.getClassFinders());
                     Debug.println("Update the RAInfo of this linkref with the base raInfo");
                     var5.getRAInfo().setBaseRA(var0.getRAInfo());
                     Debug.println("Set the late deploy flag to false");
                     var5.setLateDeploy(false);
                     Debug.println("Initialize the ra");
                     if (Debug.isDeploymentEnabled()) {
                        Debug.deployment("Starting to deploy the link-ref RA module '" + var5.getModuleName() + "' with base RA module '" + var0.getModuleName() + "'");
                     }

                     AuthenticatedSubject var7 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
                     var5.initialize((RAInstanceManager)null, var7);
                     Debug.println("prepare");
                     var5.prepare();
                     Debug.println("activate");
                     var5.activate();
                     var3.add(var5);
                     if (Debug.isDeploymentEnabled()) {
                        Debug.deployment("Succeeded in deploying the link-ref RA module '" + var5.getModuleName() + "' with base RA module '" + var0.getModuleName() + "'");
                     }
                  } catch (Throwable var14) {
                     var4.add(var5);
                     String var8 = Debug.logFailedToDeployLinkRef(var5.getModuleName(), var0.getModuleName(), var14.toString());
                     Debug.logStackTrace(var8, var14);
                     if (Debug.isDeploymentEnabled()) {
                        AuthenticatedSubject var9 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
                        Debug.deployment("Failed in deploying the link-ref RA module '" + var5.getModuleName() + "' with base RA module '" + var0.getModuleName() + "'. Caught exception with stack trace:\n" + var5.getAdapterLayer().throwable2StackTrace(var14, var9));
                     }
                  }
               }

               linkrefRAs.remove(var1);
            } else {
               Debug.println("No linkrefs to deploy");
            }
         } else {
            Debug.println("Connection factory name is null or empty. No dependent linkrefs to deploy");
         }
      } finally {
         Debug.exit("weblogic.connector.common.LinkrefManager", "deployDependentLinkrefs(...)");
      }

   }

   public static synchronized RAInstanceManager getBaseRA(String var0) {
      Debug.enter("weblogic.connector.common.LinkrefManager", "getBaseRA(...)");
      RAInstanceManager var1 = null;
      if (var0 != null && var0.length() > 0) {
         Debug.println("Trying to get the base RA for connection factory : " + var0);
         var1 = (RAInstanceManager)baseRAs.get(var0);
      }

      Debug.exit("weblogic.connector.common.LinkrefManager", "getBaseRA(...)");
      return var1;
   }
}
