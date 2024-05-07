package weblogic.wsee.wstx.wsat.config;

import java.lang.reflect.Method;
import javax.xml.ws.WebServiceException;
import weblogic.j2ee.descriptor.wl.OperationComponentBean;
import weblogic.j2ee.descriptor.wl.PortComponentBean;
import weblogic.j2ee.descriptor.wl.WSATConfigBean;
import weblogic.wsee.jws.JWSVisitor;
import weblogic.wsee.wstx.wsat.Transactional;
import weblogic.wsee.wstx.wsat.Transactional.TransactionFlowType;
import weblogic.wsee.wstx.wsat.Transactional.Version;

public class WSATBeanVisitor implements JWSVisitor {
   private PortComponentBean portComponentBean;
   WSATConfigBean portTransactionConfigBean;
   private boolean createOperationLevelBean;

   public WSATBeanVisitor(PortComponentBean var1) {
      this.portComponentBean = var1;
   }

   public WSATBeanVisitor(PortComponentBean var1, boolean var2) {
      this.portComponentBean = var1;
      this.createOperationLevelBean = var2;
   }

   public void visitClass(JWSVisitor.JWSClass var1) {
      Transactional var2 = (Transactional)var1.getServiceImpl().getAnnotation(Transactional.class);
      if (var1.isProviderBased()) {
         Method var3 = var1.getInvokeMethod();
         if (var3 != null) {
            Transactional var4 = (Transactional)var3.getAnnotation(Transactional.class);
            if (var4 != null) {
               var2 = var4;
            }
         }
      }

      this.portTransactionConfigBean = this.portComponentBean.getWSATConfig();
      if (var2 != null && this.portTransactionConfigBean == null) {
         this.portTransactionConfigBean = this.portComponentBean.createWSATConfig();
         this.fromAnnotationToBean(var2, this.portTransactionConfigBean, (WSATConfigBean)null);
      }

   }

   public void visitMethod(JWSVisitor.WsMethod var1) {
      Method var2 = var1.getImplMethod();
      OperationComponentBean var3 = this.portComponentBean.lookupOperation(var1.getOperationName());
      if (var3 == null) {
         var3 = this.portComponentBean.createOperation();
         var3.setName(var1.getOperationName());
      }

      WSATConfigBean var4 = var3.getWSATConfig();
      Transactional var5 = (Transactional)var2.getAnnotation(Transactional.class);
      if (var1.isOneway()) {
         this.handleOnewayOperation(var2, var3, var4, var5);
      } else if (var4 == null) {
         if (var5 != null) {
            var4 = var3.createWSATConfig();
            this.fromAnnotationToBean(var5, var4, this.portComponentBean.getWSATConfig());
         } else if (this.createOperationLevelBean && this.portTransactionConfigBean != null) {
            WSATConfigBean var6 = var3.createWSATConfig();
            var6.setEnabled(this.portTransactionConfigBean.isEnabled());
            var6.setFlowType(this.portTransactionConfigBean.getFlowType());
            var6.setVersion(this.portTransactionConfigBean.getVersion());
         }

      }
   }

   private void handleOnewayOperation(Method var1, OperationComponentBean var2, WSATConfigBean var3, Transactional var4) {
      if (var3 != null) {
         if (var3.isEnabled() && !TransactionFlowType.NEVER.toString().equals(var3.getFlowType())) {
            throw new WebServiceException("WSAT-Config can't be enabled on Oneway operation - " + var2.getName());
         }
      } else if (var4 != null) {
         if (var4.enabled() && var4.value() != TransactionFlowType.NEVER) {
            throw new WebServiceException(Transactional.class.getName() + " can't be enabled on Oneway operation - " + var1.toString());
         }

         this.buildOperationLevelWSATConfigBean(var2, var4.enabled(), var4.value().toString(), var4.version().toString());
      } else if (this.createOperationLevelBean) {
         this.buildOperationLevelWSATConfigBean(var2, false, TransactionFlowType.NEVER.toString(), Version.DEFAULT.toString());
      }

   }

   private void buildOperationLevelWSATConfigBean(OperationComponentBean var1, boolean var2, String var3, String var4) {
      WSATConfigBean var5 = var1.createWSATConfig();
      var5.setEnabled(var2);
      var5.setFlowType(var3);
      var5.setVersion(var4);
   }

   private void fromAnnotationToBean(Transactional var1, WSATConfigBean var2, WSATConfigBean var3) {
      var2.setEnabled(var1.enabled());
      var2.setFlowType(var1.value().toString());
      if (var3 == null) {
         var2.setVersion(var1.version().toString());
      } else {
         if (var1.version() != Version.DEFAULT && !var1.version().toString().equals(var3.getVersion())) {
            throw new WebServiceException("operation level version can't be different from the port level version!");
         }

         var2.setVersion(var3.getVersion());
      }

   }
}
