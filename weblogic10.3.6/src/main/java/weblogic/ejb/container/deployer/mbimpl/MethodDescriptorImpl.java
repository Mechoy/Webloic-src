package weblogic.ejb.container.deployer.mbimpl;

import weblogic.ejb.container.dd.xml.DDUtils;
import weblogic.ejb.container.interfaces.MethodDescriptor;
import weblogic.j2ee.descriptor.MethodBean;
import weblogic.j2ee.descriptor.MethodParamsBean;

public final class MethodDescriptorImpl implements MethodDescriptor {
   private String m_methodName;
   private String[] m_methodParams;
   private String m_ejbName;
   private String m_methodIntf;

   public MethodDescriptorImpl(MethodBean var1) {
      this.m_methodName = var1.getMethodName();
      this.m_ejbName = var1.getEjbName();
      this.m_methodIntf = var1.getMethodIntf();
      MethodParamsBean var2 = var1.getMethodParams();
      if (null != var2) {
         this.m_methodParams = var2.getMethodParams();
      } else {
         this.m_methodParams = null;
      }

   }

   public MethodDescriptorImpl(weblogic.j2ee.descriptor.wl.MethodBean var1) {
      this.m_methodName = var1.getMethodName();
      this.m_ejbName = var1.getEjbName();
      this.m_methodIntf = var1.getMethodIntf();
      weblogic.j2ee.descriptor.wl.MethodParamsBean var2 = var1.getMethodParams();
      if (null != var2) {
         this.m_methodParams = var2.getMethodParams();
      } else {
         this.m_methodParams = null;
      }

   }

   public String toString() {
      String var1 = "";
      var1 = var1 + "[MethodDescriptorImpl " + this.m_methodName + "(";
      if (this.m_methodParams != null) {
         for(int var2 = 0; var2 < this.m_methodParams.length; ++var2) {
            var1 = var1 + this.m_methodParams[var2] + " ";
         }
      }

      var1 = var1 + ") " + this.m_ejbName + ", " + this.m_methodIntf + " type= " + this.getMethodType() + "]";
      return var1;
   }

   public short getMethodType() {
      byte var1 = 3;
      if (this.m_methodName.equals("*")) {
         var1 = 1;
      } else if (this.m_methodParams == null) {
         var1 = 2;
      }

      return var1;
   }

   public String getMethodSignature() {
      return DDUtils.getMethodSignature(this.m_methodName, this.m_methodParams);
   }

   public String getEjbName() {
      return this.m_ejbName;
   }

   public String getMethodIntf() {
      return this.m_methodIntf;
   }
}
