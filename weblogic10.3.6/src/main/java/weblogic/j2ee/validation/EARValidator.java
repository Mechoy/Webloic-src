package weblogic.j2ee.validation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import weblogic.ejb.spi.EJBValidationInfo;
import weblogic.j2ee.J2EELogger;
import weblogic.j2ee.J2EEUtils;
import weblogic.j2ee.descriptor.ApplicationBean;
import weblogic.j2ee.descriptor.wl.ApplicationEntityCacheBean;
import weblogic.j2ee.descriptor.wl.EjbBean;
import weblogic.j2ee.descriptor.wl.JDBCDataSourceBean;
import weblogic.j2ee.descriptor.wl.JMSBean;
import weblogic.j2ee.descriptor.wl.WeblogicApplicationBean;
import weblogic.logging.Loggable;
import weblogic.utils.ErrorCollectionException;

public final class EARValidator {
   private static final boolean debug = false;
   ApplicationBean dd = null;
   WeblogicApplicationBean wldd = null;
   Map moduleInfos = new HashMap();
   Map name2ModuleInfos = new HashMap();

   public EARValidator(ApplicationBean var1, WeblogicApplicationBean var2) {
      this.dd = var1;
      this.wldd = var2;
   }

   public void addModuleValidationInfo(ModuleValidationInfo var1) {
      this.moduleInfos.put(var1.getURI(), var1);
      if (var1.getModuleName() != null) {
         this.name2ModuleInfos.put(var1.getModuleName(), var1);
      }

   }

   public void validate() throws ErrorCollectionException {
      HashSet var1 = null;
      if (this.wldd != null) {
         var1 = new HashSet();
         EjbBean var2 = this.wldd.getEjb();
         if (var2 != null) {
            ApplicationEntityCacheBean[] var3 = var2.getEntityCaches();

            for(int var4 = 0; var4 < var3.length; ++var4) {
               var1.add(var3[var4].getEntityCacheName());
            }
         }
      }

      ErrorCollectionException var5 = new ErrorCollectionException("");
      Iterator var6 = this.moduleInfos.values().iterator();

      while(var6.hasNext()) {
         ModuleValidationInfo var7 = (ModuleValidationInfo)var6.next();
         this.validateEJBLinks(var7, var5);
         this.ValidateAppScopedCacheRefs(var1, var7, var5);
         this.validateJMSLinkRefs(var7, var5);
         this.validateJDBCLinkRefs(var7, var5);
      }

      if (!var5.isEmpty()) {
         throw var5;
      }
   }

   private void validateEJBLinks(ModuleValidationInfo var1, ErrorCollectionException var2) {
      Collection var3 = var1.getEJBRefs();
      Iterator var4 = var3.iterator();

      while(var4.hasNext()) {
         ModuleValidationInfo.EJBRef var5 = (ModuleValidationInfo.EJBRef)var4.next();

         try {
            String var6 = var5.getEJBLink();
            if (var6.indexOf(35) < 0) {
               this.validateUnqualifiedEJBLink(var1, var5, var6);
            } else {
               if (var6.startsWith("../")) {
                  var6 = J2EEUtils.makePathAbsolute(var6, var1.getURI());
               }

               int var7 = var6.indexOf(35);
               String var8 = var6.substring(0, var7);
               String var9 = var6.substring(var7 + 1, var6.length());
               ModuleValidationInfo var10 = (ModuleValidationInfo)this.moduleInfos.get(var8);
               if (var10 == null) {
                  Loggable var14;
                  if (var5.getEJBName() != null) {
                     var14 = J2EELogger.logInvalidEJBLinkQualificationInEJBDescriptorLoggable(var5.getEJBLink(), var5.getEJBRefName(), var5.getEJBName(), var1.getURI(), var8);
                     throw new ComplianceException(var14.getMessage());
                  }

                  var14 = J2EELogger.logInvalidEJBLinkQualificationLoggable(var5.getEJBLink(), var5.getEJBRefName(), var1.getURI(), var8);
                  throw new ComplianceException(var14.getMessage());
               }

               EJBValidationInfo var11 = var10.getEJBValidationInfo(var9);
               Loggable var12;
               if (var11 == null) {
                  if (var5.getEJBName() != null) {
                     var12 = J2EELogger.logInvalidQualifiedEJBLinkInEJBDescriptorLoggable(var5.getEJBLink(), var5.getEJBRefName(), var5.getEJBName(), var1.getURI(), var8, var9);
                     throw new ComplianceException(var12.getMessage());
                  }

                  var12 = J2EELogger.logInvalidQualifiedEJBLinkLoggable(var5.getEJBLink(), var5.getEJBRefName(), var1.getURI(), var8, var9);
                  throw new ComplianceException(var12.getMessage());
               }

               if (!var11.isClientDriven()) {
                  if (var5.getEJBName() != null) {
                     var12 = J2EELogger.logEJBLinkInEJBDescriptorPointsToInvalidBeanLoggable(var5.getEJBLink(), var5.getEJBRefName(), var5.getEJBName(), var1.getURI());
                     throw new ComplianceException(var12.getMessage());
                  }

                  var12 = J2EELogger.logEJBLinkPointsToInvalidBeanLoggable(var5.getEJBLink(), var5.getEJBRefName(), var1.getURI());
                  throw new ComplianceException(var12.getMessage());
               }

               this.validateResolvedEJBLink(var1, var5, var11);
            }
         } catch (ComplianceException var13) {
            var2.add(var13);
         }
      }

   }

   private void validateUnqualifiedEJBLink(ModuleValidationInfo var1, ModuleValidationInfo.EJBRef var2, String var3) throws ComplianceException {
      ArrayList var4 = new ArrayList();
      Iterator var5 = this.moduleInfos.values().iterator();

      ModuleValidationInfo var6;
      EJBValidationInfo var7;
      while(var5.hasNext()) {
         var6 = (ModuleValidationInfo)var5.next();
         var7 = var6.getEJBValidationInfo(var3);
         if (var7 != null && var7.isClientDriven()) {
            var4.add(var6);
         }
      }

      if (var4.size() == 1) {
         var6 = (ModuleValidationInfo)var4.get(0);
         var7 = var6.getEJBValidationInfo(var3);
         this.validateResolvedEJBLink(var1, var2, var7);
      } else if (var4.size() == 0) {
         Loggable var10;
         if (var2.getEJBName() != null) {
            var10 = J2EELogger.logInvalidUnqualifiedEJBLinkInEJBDescriptorLoggable(var2.getEJBLink(), var2.getEJBRefName(), var2.getEJBName(), var1.getURI());
            throw new ComplianceException(var10.getMessage());
         } else {
            var10 = J2EELogger.logInvalidUnqualifiedEJBLinkLoggable(var2.getEJBLink(), var2.getEJBRefName(), var1.getURI());
            throw new ComplianceException(var10.getMessage());
         }
      } else {
         StringBuffer var9 = new StringBuffer();

         for(Iterator var11 = var4.iterator(); var11.hasNext(); var9.append(((ModuleValidationInfo)var11.next()).getURI())) {
            if (var9.length() > 0) {
               var9.append(", ");
            }
         }

         Loggable var8;
         if (var2.getEJBName() != null) {
            var8 = J2EELogger.logAmbiguousEJBLinkInEJBDescriptorLoggable(var2.getEJBLink(), var2.getEJBRefName(), var2.getEJBName(), var1.getURI(), var9.toString());
            throw new ComplianceException(var8.getMessage());
         } else {
            var8 = J2EELogger.logAmbiguousEJBLinkLoggable(var2.getEJBLink(), var2.getEJBRefName(), var1.getURI(), var9.toString());
            throw new ComplianceException(var8.getMessage());
         }
      }
   }

   private boolean ejbImplementsBusinessInterface(String var1, boolean var2, EJBValidationInfo var3) {
      Set var4 = null;
      if (var2) {
         var4 = var3.getBusinessLocals();
      } else {
         var4 = var3.getBusinessRemotes();
      }

      Iterator var5 = var4.iterator();

      Class var6;
      do {
         if (!var5.hasNext()) {
            return false;
         }

         var6 = (Class)var5.next();
      } while(!var1.equals(var6.getName()));

      return true;
   }

   private void validateResolvedEJBLink(ModuleValidationInfo var1, ModuleValidationInfo.EJBRef var2, EJBValidationInfo var3) throws ComplianceException {
      this.validateEJBRefType(var1, var2, var3);
      Loggable var6;
      if (var2.getHomeInterfaceName() == null) {
         if (!this.ejbImplementsBusinessInterface(var2.getComponentInterfaceName(), true, var3) && !this.ejbImplementsBusinessInterface(var2.getComponentInterfaceName(), false, var3)) {
            var6 = J2EELogger.logIncorrectInterfaceForEJBAnnotationTargetLoggable(var1.getURI(), var2.getEJBLink(), var2.getComponentInterfaceName(), var2.getEJBRefName());
            throw new ComplianceException(var6.getMessage());
         }
      } else {
         String var4;
         boolean var5;
         String var7;
         if (var2.isLocal()) {
            if (!var3.hasLocalClientView()) {
               if (var2.getEJBName() != null) {
                  var6 = J2EELogger.logIncorrectInterfacesForEJBRefTypeInEJBDescriptorLoggable(var2.getEJBLink(), "ejb-local-ref", var2.getEJBRefName(), var2.getEJBName(), var1.getURI(), "local");
                  throw new ComplianceException(var6.getMessage());
               }

               var6 = J2EELogger.logIncorrectInterfacesForEJBRefTypeLoggable(var2.getEJBLink(), "ejb-local-ref", var2.getEJBRefName(), var1.getURI(), "local");
               throw new ComplianceException(var6.getMessage());
            }

            var4 = var2.getComponentInterfaceName();
            if (var4 != null) {
               var5 = false;
               var5 = var4.equals(var3.getLocalInterfaceName());
               if (!var5) {
                  var5 = this.ejbImplementsBusinessInterface(var4, true, var3);
               }

               if (!var5) {
                  if (var2.getEJBName() != null) {
                     J2EELogger.logIncorrectInterfaceNameForEJBRefInEJBDescriptor("ejb-local-ref", var2.getEJBRefName(), var2.getEJBName(), var1.getURI(), "local", var4);
                  } else {
                     J2EELogger.logIncorrectInterfaceNameForEJBRef("ejb-local-ref", var2.getEJBRefName(), var1.getURI(), "local", var4);
                  }
               }
            }

            var7 = var2.getHomeInterfaceName();
            if (var7 != null && !var7.equals(var3.getLocalHomeInterfaceName())) {
               if (var2.getEJBName() != null) {
                  J2EELogger.logIncorrectInterfaceNameForEJBRefInEJBDescriptor("ejb-local-ref", var2.getEJBRefName(), var2.getEJBName(), var1.getURI(), "local-home", var2.getHomeInterfaceName());
               } else {
                  J2EELogger.logIncorrectInterfaceNameForEJBRef("ejb-local-ref", var2.getEJBRefName(), var1.getURI(), "local-home", var2.getHomeInterfaceName());
               }
            }
         } else {
            if (!var3.hasRemoteClientView()) {
               if (var2.getEJBName() != null) {
                  var6 = J2EELogger.logIncorrectInterfacesForEJBRefTypeInEJBDescriptorLoggable(var2.getEJBLink(), "ejb-ref", var2.getEJBRefName(), var2.getEJBName(), var1.getURI(), "remote");
                  throw new ComplianceException(var6.getMessage());
               }

               var6 = J2EELogger.logIncorrectInterfacesForEJBRefTypeLoggable(var2.getEJBLink(), "ejb-ref", var2.getEJBRefName(), var1.getURI(), "remote");
               throw new ComplianceException(var6.getMessage());
            }

            var4 = var2.getComponentInterfaceName();
            if (var4 != null) {
               var5 = false;
               var5 = var4.equals(var3.getRemoteInterfaceName());
               if (!var5) {
                  var5 = this.ejbImplementsBusinessInterface(var4, false, var3);
               }

               if (!var5) {
                  if (var2.getEJBName() != null) {
                     J2EELogger.logIncorrectInterfaceNameForEJBRefInEJBDescriptor("ejb-ref", var2.getEJBRefName(), var2.getEJBName(), var1.getURI(), "remote", var2.getComponentInterfaceName());
                  } else {
                     J2EELogger.logIncorrectInterfaceNameForEJBRef("ejb-ref", var2.getEJBRefName(), var1.getURI(), "remote", var2.getComponentInterfaceName());
                  }
               }
            }

            var7 = var2.getHomeInterfaceName();
            if (var7 != null && !var7.equals(var3.getHomeInterfaceName())) {
               if (var2.getEJBName() != null) {
                  J2EELogger.logIncorrectInterfaceNameForEJBRefInEJBDescriptor("ejb-ref", var2.getEJBRefName(), var2.getEJBName(), var1.getURI(), "home", var2.getHomeInterfaceName());
               } else {
                  J2EELogger.logIncorrectInterfaceNameForEJBRef("ejb-ref", var2.getEJBRefName(), var1.getURI(), "home", var2.getHomeInterfaceName());
               }
            }
         }

      }
   }

   private void validateEJBRefType(ModuleValidationInfo var1, ModuleValidationInfo.EJBRef var2, EJBValidationInfo var3) {
      String var4 = var2.getRefType();
      String var5;
      if ("Session".equalsIgnoreCase(var4)) {
         if (!var3.isSessionBean()) {
            var5 = "ejb-local-ref";
            if (!var2.isLocal()) {
               var5 = "ejb-ref";
            }

            if (var2.getEJBName() != null) {
               J2EELogger.logIncorrectRefTypeForEJBRefInEJBDescriptor(var5, var2.getEJBRefName(), var2.getEJBName(), var1.getURI(), "Session");
            } else {
               J2EELogger.logIncorrectRefTypeForEJBRef(var5, var2.getEJBRefName(), var1.getURI(), "Session");
            }
         }
      } else if ("Entity".equalsIgnoreCase(var4) && !var3.isEntityBean()) {
         var5 = "ejb-local-ref";
         if (!var2.isLocal()) {
            var5 = "ejb-ref";
         }

         if (var2.getEJBName() != null) {
            J2EELogger.logIncorrectRefTypeForEJBRefInEJBDescriptor(var5, var2.getEJBRefName(), var2.getEJBName(), var1.getURI(), "Entity");
         } else {
            J2EELogger.logIncorrectRefTypeForEJBRef(var5, var2.getEJBRefName(), var1.getURI(), "Entity");
         }
      }

   }

   private void ValidateAppScopedCacheRefs(Collection var1, ModuleValidationInfo var2, ErrorCollectionException var3) {
      Map var4 = var2.getAppScopedCacheReferences();
      if (var4 != null) {
         Iterator var5 = var4.keySet().iterator();

         while(var5.hasNext()) {
            String var6 = (String)var5.next();
            Loggable var7;
            if (var1 != null) {
               if (!var1.contains(var6)) {
                  var7 = J2EELogger.logInvalidEntityCacheRefDeclaredLoggable((String)var4.get(var6), var2.getURI(), var6);
                  var3.add(new ComplianceException(var7.getMessage()));
               }
            } else {
               var7 = J2EELogger.logInvalidEntityCacheRefDeclaredLoggable((String)var4.get(var6), var2.getURI(), var6);
               var3.add(new ComplianceException(var7.getMessage()));
            }
         }
      }

   }

   private void validateJMSLinkRefs(ModuleValidationInfo var1, ErrorCollectionException var2) {
      Collection var3 = var1.getJMSLinkRefs();
      Iterator var4 = var3.iterator();

      while(true) {
         while(true) {
            String var6;
            String var7;
            String var9;
            String var10;
            do {
               if (!var4.hasNext()) {
                  return;
               }

               ModuleValidationInfo.JLinkRef var5 = (ModuleValidationInfo.JLinkRef)var4.next();
               var6 = var5.getAppComponentName();
               var7 = var5.getAppComponentType();
               String var8 = var5.getResRefName();
               var9 = var5.getResRefType();
               var10 = var5.getResLinkName();
            } while(var10.indexOf(35) <= 0);

            int var11 = var10.indexOf(35);
            String var12 = var10.substring(0, var11);
            String var13 = var10.substring(var11 + 1, var10.length());
            ModuleValidationInfo var14 = (ModuleValidationInfo)this.name2ModuleInfos.get(var12);
            if (var14 == null) {
               Loggable var17 = J2EELogger.logInvalidJMSResourceLinkInJ2EEComponentLoggable(var6, var7, var12, var10);
               var2.add(new ComplianceException(var17.getMessage()));
            } else {
               JMSBean var15 = var14.getJMSBean();
               Loggable var16;
               if (var15 == null) {
                  var16 = J2EELogger.logInvalidJMSResourceLinkInJ2EEComponentLoggable(var6, var7, var12, var10);
                  var2.add(new ComplianceException(var16.getMessage()));
               } else if (var9.equals("javax.jms.Queue")) {
                  if (var15.lookupQueue(var13) == null && var15.lookupDistributedQueue(var13) == null && var15.lookupUniformDistributedQueue(var13) == null) {
                     var16 = J2EELogger.logJMSResourceSpecifiedInResourceLinkNotFoundLoggable(var6, var7, var12, var10, var13, "javax.jms.Queue");
                     var2.add(new ComplianceException(var16.getMessage()));
                  }
               } else if (var9.equals("javax.jms.Topic")) {
                  if (var15.lookupTopic(var13) == null && var15.lookupDistributedTopic(var13) == null && var15.lookupUniformDistributedTopic(var13) == null) {
                     var16 = J2EELogger.logJMSResourceSpecifiedInResourceLinkNotFoundLoggable(var6, var7, var12, var10, var13, "javax.jms.Topic");
                     var2.add(new ComplianceException(var16.getMessage()));
                  }
               } else if ((var9.equals("javax.jms.ConnectionFactory") || var9.equals("javax.jms.QueueConnectionFactory") || var9.equals("javax.jms.TopicConnectionFactory")) && var15.lookupConnectionFactory(var13) == null) {
                  var16 = J2EELogger.logJMSResourceSpecifiedInResourceLinkNotFoundLoggable(var6, var7, var12, var10, var13, "javax.jms.ConnectionFactory");
                  var2.add(new ComplianceException(var16.getMessage()));
               }
            }
         }
      }
   }

   private void validateJDBCLinkRefs(ModuleValidationInfo var1, ErrorCollectionException var2) {
      Collection var3 = var1.getJDBCLinkRefs();
      Iterator var4 = var3.iterator();

      while(true) {
         while(true) {
            String var6;
            String var7;
            String var9;
            String var10;
            do {
               if (!var4.hasNext()) {
                  return;
               }

               ModuleValidationInfo.JLinkRef var5 = (ModuleValidationInfo.JLinkRef)var4.next();
               var6 = var5.getAppComponentName();
               var7 = var5.getAppComponentType();
               String var8 = var5.getResRefName();
               var9 = var5.getResRefType();
               var10 = var5.getResLinkName();
            } while(var10.indexOf(35) <= 0);

            int var11 = var10.indexOf(35);
            String var12 = var10.substring(0, var11);
            String var13 = var10.substring(var11 + 1, var10.length());
            ModuleValidationInfo var14 = (ModuleValidationInfo)this.name2ModuleInfos.get(var12);
            if (var14 == null) {
               Loggable var17 = J2EELogger.logInvalidJDBCResourceLinkInJ2EEComponentLoggable(var6, var7, var12, var10);
               var2.add(new ComplianceException(var17.getMessage()));
            } else {
               JDBCDataSourceBean var15 = var14.getJDBCDataSourceBean();
               Loggable var16;
               if (var15 == null) {
                  var16 = J2EELogger.logInvalidJDBCResourceLinkInJ2EEComponentLoggable(var6, var7, var12, var10);
                  var2.add(new ComplianceException(var16.getMessage()));
               } else if (var9.equals("javax.sql.DataSource") && (var15.getName() == null || !var15.getName().equals(var13))) {
                  var16 = J2EELogger.logJDBCResourceSpecifiedInResourceLinkNotFoundLoggable(var6, var7, var12, var10, var13, "javax.sql.DataSource");
                  var2.add(new ComplianceException(var16.getMessage()));
               }
            }
         }
      }
   }
}
