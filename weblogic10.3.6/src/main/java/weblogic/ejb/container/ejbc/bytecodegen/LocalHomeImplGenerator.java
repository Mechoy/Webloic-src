package weblogic.ejb.container.ejbc.bytecodegen;

import com.bea.objectweb.asm.ClassWriter;
import java.lang.reflect.Method;
import javax.ejb.EJBException;
import javax.ejb.RemoveException;
import weblogic.ejb.container.deployer.NamingConvention;
import weblogic.ejb.container.ejbc.EJBMethodsUtil;
import weblogic.ejb.container.interfaces.BaseEJBLocalObjectIntf;
import weblogic.ejb.container.interfaces.Ejb3SessionBeanInfo;
import weblogic.ejb.container.interfaces.SessionBeanInfo;
import weblogic.ejb.container.internal.StatefulEJBLocalHome;
import weblogic.ejb.container.internal.StatefulEJBLocalHomeImpl;
import weblogic.ejb.container.internal.StatelessEJBLocalHome;
import weblogic.ejb.container.internal.StatelessEJBLocalHomeImpl;
import weblogic.ejb20.interfaces.LocalHomeHandle;

class LocalHomeImplGenerator extends AbstractHomeImplGenerator {
   private static final MethInfo REMOVE_MI = MethInfo.of("remove", "md_ejbRemove_O").args(Object.class).exceps(RemoveException.class).create();
   private static final MethInfo GET_LOCAL_HOME_HANDLE_MI = MethInfo.of("getLocalHomeHandle", "md_getLocalHomeHandle").returns(LocalHomeHandle.class).exceps(EJBException.class).create();

   LocalHomeImplGenerator(NamingConvention var1, SessionBeanInfo var2) {
      super(var1, var2, BCUtil.binName(var1.getLocalHomeClassName()), getSuperClsName(var2));
   }

   String[] getInterfaces() {
      return new String[]{BCUtil.binName(this.sbi.getLocalHomeInterfaceClass())};
   }

   String getComponentImplName() {
      return BCUtil.binName(this.nc.getEJBLocalObjectClassName());
   }

   Class<?> getComponentInterface() {
      return this.sbi.getLocalInterfaceClass();
   }

   Class<? extends Exception> getDefExceptionForCreate() {
      return EJBException.class;
   }

   String getSupersCreateReturnType() {
      return BCUtil.fieldDesc(BaseEJBLocalObjectIntf.class);
   }

   Method[] getCreateMethods() {
      return EJBMethodsUtil.getLocalCreateMethods(this.sbi.getLocalHomeInterfaceClass());
   }

   void addCustomMembers(ClassWriter var1) {
      BCUtil.addHomeMembers(var1, this.clsName, this.superClsName, REMOVE_MI, GET_LOCAL_HOME_HANDLE_MI);
   }

   private static String getSuperClsName(SessionBeanInfo var0) {
      if (var0.isStateful()) {
         return var0.isEJB30() && ((Ejb3SessionBeanInfo)var0).hasBusinessLocals() ? BCUtil.binName(StatefulEJBLocalHomeImpl.class) : BCUtil.binName(StatefulEJBLocalHome.class);
      } else {
         return var0.isEJB30() && ((Ejb3SessionBeanInfo)var0).hasBusinessLocals() ? BCUtil.binName(StatelessEJBLocalHomeImpl.class) : BCUtil.binName(StatelessEJBLocalHome.class);
      }
   }
}
