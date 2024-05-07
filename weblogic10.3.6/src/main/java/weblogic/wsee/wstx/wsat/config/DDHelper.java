package weblogic.wsee.wstx.wsat.config;

import com.sun.xml.ws.binding.WebServiceFeatureList;
import javax.xml.ws.WebServiceException;
import weblogic.descriptor.EditableDescriptorManager;
import weblogic.j2ee.descriptor.wl.OperationComponentBean;
import weblogic.j2ee.descriptor.wl.OperationInfoBean;
import weblogic.j2ee.descriptor.wl.PortComponentBean;
import weblogic.j2ee.descriptor.wl.PortInfoBean;
import weblogic.j2ee.descriptor.wl.WSATConfigBean;
import weblogic.j2ee.descriptor.wl.WeblogicWebservicesBean;
import weblogic.wsee.jws.VisitableJWS;
import weblogic.wsee.jws.VisitableJWSBuilder;
import weblogic.wsee.wstx.wsat.Transactional;
import weblogic.wsee.wstx.wsat.TransactionalFeature;
import weblogic.wsee.wstx.wsat.Transactional.TransactionFlowType;
import weblogic.wsee.wstx.wsat.Transactional.Version;

public class DDHelper {
   static final String NEVER;

   public static void updateFeatureFromServiceDD(TransactionalFeature var0, PortComponentBean var1) {
      if (var0 == null) {
         throw new IllegalArgumentException("TransactionalFeature can't be null");
      } else if (var1 != null) {
         WSATConfigBean var2 = var1.getWSATConfig();
         updateFeatureFromPortLevelConfig(var0, var2);
         OperationComponentBean[] var3 = var1.getOperations();
         OperationComponentBean[] var4 = var3;
         int var5 = var3.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            OperationComponentBean var7 = var4[var6];
            WSATConfigBean var8 = var7.getWSATConfig();
            if (var8 == null) {
               var8 = var2;
            }

            String var9 = var7.getName();
            updateFeatureFromOperationLevelConfig(var0, var8, var9);
         }

      }
   }

   private static void updateFeatureFromPortLevelConfig(TransactionalFeature var0, WSATConfigBean var1) {
      if (var1 == null) {
         var0.setExplicitMode(true);
         var0.setVersion((Transactional.Version)null);
      } else {
         var0.setEnabled(var1.isEnabled());
         var0.setFlowType(TransactionFlowType.valueOf(var1.getFlowType()));
         var0.setVersion(Version.valueOf(var1.getVersion()));
      }
   }

   private static void updateFeatureFromOperationLevelConfig(TransactionalFeature var0, WSATConfigBean var1, String var2) {
      if (var1 != null) {
         var0.setEnabled(var2, var1.isEnabled());
         Transactional.TransactionFlowType var3 = TransactionFlowType.valueOf(var1.getFlowType());
         var0.setFlowType(var2, var3);
         Transactional.Version var4 = Version.valueOf(var1.getVersion());
         boolean var5 = var1.isEnabled() && var3 != TransactionFlowType.NEVER;
         if (var5) {
            if (var0.isExplicitMode() && var5) {
               var0.setEnabled(true);
               if (var0.getVersion() == null) {
                  var0.setVersion(var4);
               }
            }

            if (var4 != var0.getVersion()) {
               if (var0.isExplicitMode()) {
                  throw new WebServiceException("WS-AT version on operation(" + var4 + ") '" + var2 + "' is different from the one on other operations!");
               }

               if (var4 != Version.DEFAULT) {
                  throw new WebServiceException("WS-AT version(" + var4 + ")  on operation '" + var2 + "' is different from the one(" + var0.getVersion() + ") on port!");
               }
            }

         }
      }
   }

   public static TransactionalFeature buildFeatureFromServiceDD(PortComponentBean var0) {
      TransactionalFeature var1 = null;
      if (var0 != null) {
         if (!isEffectivelyEnabled(var0.getWSATConfig())) {
            OperationComponentBean[] var2 = var0.getOperations();

            for(int var3 = 0; var3 < var2.length; ++var3) {
               OperationComponentBean var4 = var2[var3];
               if (isEffectivelyEnabled(var4.getWSATConfig())) {
                  var1 = new TransactionalFeature();
                  var1.setExplicitMode(true);
                  break;
               }
            }
         } else {
            var1 = new TransactionalFeature();
         }
      }

      if (var1 != null) {
         updateFeatureFromServiceDD(var1, var0);
      }

      return var1;
   }

   public static TransactionalFeature buildFeatureFromServiceRefDD(PortInfoBean var0) {
      if (var0 == null) {
         return null;
      } else {
         WSATConfigBean var1 = var0.getWSATConfig();
         TransactionalFeature var2 = null;
         if (var1 != null) {
            var2 = new TransactionalFeature();
            updateFeatureFromPortLevelConfig(var2, var1);
         }

         OperationInfoBean[] var3 = var0.getOperations();
         OperationInfoBean[] var4 = var3;
         int var5 = var3.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            OperationInfoBean var7 = var4[var6];
            WSATConfigBean var8 = var7.getWSATConfig();
            if (var8 != null) {
               if (var2 == null) {
                  var2 = new TransactionalFeature();
               }

               String var9 = var7.getName();
               updateFeatureFromOperationLevelConfig(var2, var8, var9);
            }
         }

         return var2;
      }
   }

   public static void populateServiceDDFromJWS(Class var0, Class var1, PortComponentBean var2) {
      populateServiceDDFromJWS(var0, var1, var2, false);
   }

   public static void populateServiceDDFromJWS(Class var0, Class var1, PortComponentBean var2, boolean var3) {
      VisitableJWS var4 = VisitableJWSBuilder.jaxws().impl(var1).sei(var0).build();
      var4.accept(new WSATBeanVisitor(var2, var3));
   }

   public static void populateServiceRefDDFromAnnotation(Transactional var0, PortInfoBean var1) {
      if (var0 != null) {
         if (var1 == null) {
            throw new IllegalArgumentException("portInfoBean can't be null");
         } else if (var1.getWSATConfig() == null) {
            WSATConfigBean var2 = var1.createWSATConfig();
            var2.setEnabled(var0.enabled());
            var2.setVersion(var0.version().toString());
            var2.setFlowType(var0.value().toString());
         }
      }
   }

   public static TransactionalFeature buildFeatureFromAnnotation(Transactional var0) {
      TransactionalFeature var1 = new TransactionalFeature();
      var1.setEnabled(var0.enabled());
      var1.setVersion(var0.version());
      var1.setFlowType(var0.value());
      return var1;
   }

   public static void updateFeatureFromJWS(Class var0, Class var1, WebServiceFeatureList var2) {
      EditableDescriptorManager var3 = new EditableDescriptorManager();
      WeblogicWebservicesBean var4 = (WeblogicWebservicesBean)var3.createDescriptorRoot(WeblogicWebservicesBean.class).getRootBean();
      var4.setVersion("1.2");
      PortComponentBean var5 = var4.createWebserviceDescription().createPortComponent();
      populateServiceDDFromJWS(var0, var1, var5, true);
      TransactionalFeature var6 = (TransactionalFeature)var2.get(TransactionalFeature.class);
      if (var6 == null) {
         var6 = buildFeatureFromServiceDD(var5);
         if (var6 != null) {
            var2.add(var6);
         }
      } else {
         updateFeatureFromServiceDD(var6, var5);
      }

   }

   public static boolean isEffectivelyEnabled(WSATConfigBean var0) {
      return var0 != null && var0.isEnabled() && !NEVER.equals(var0.getFlowType());
   }

   static {
      NEVER = TransactionFlowType.NEVER.toString();
   }
}
