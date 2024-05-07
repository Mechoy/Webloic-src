package weblogic.ejb.container.ejbc.bytecodegen;

import com.bea.objectweb.asm.ClassWriter;
import java.lang.reflect.Method;
import java.rmi.RemoteException;
import javax.ejb.EJBMetaData;
import javax.ejb.EJBObject;
import javax.ejb.Handle;
import javax.ejb.HomeHandle;
import javax.ejb.RemoveException;
import weblogic.ejb.container.deployer.NamingConvention;
import weblogic.ejb.container.ejbc.EJBMethodsUtil;
import weblogic.ejb.container.interfaces.Ejb3SessionBeanInfo;
import weblogic.ejb.container.interfaces.SessionBeanInfo;
import weblogic.ejb.container.internal.StatefulEJBHome;
import weblogic.ejb.container.internal.StatefulEJBHomeImpl;
import weblogic.ejb.container.internal.StatelessEJBHome;
import weblogic.ejb.container.internal.StatelessEJBHomeImpl;

class HomeImplGenerator extends AbstractHomeImplGenerator {
   private static final MethInfo GET_EJB_METADATA_MI = MethInfo.of("getEJBMetaData", "md_getEJBMetaData").returns(EJBMetaData.class).exceps(RemoteException.class).create();
   private static final MethInfo GET_HOME_HANDLE_MI = MethInfo.of("getHomeHandle", "md_getHomeHandle").returns(HomeHandle.class).exceps(RemoteException.class).create();
   private static final MethInfo REMOVE_OBJ_MI = MethInfo.of("remove", "md_ejbRemove_O").args(Object.class).exceps(RemoteException.class, RemoveException.class).create();
   private static final MethInfo REMOVE_HANDLE_MI = MethInfo.of("remove", "md_ejbRemove_javax_ejb_Handle").args(Handle.class).exceps(RemoteException.class, RemoveException.class).create();

   HomeImplGenerator(NamingConvention var1, SessionBeanInfo var2) {
      super(var1, var2, BCUtil.binName(var1.getHomeClassName()), getSuperClsName(var2));
   }

   String[] getInterfaces() {
      return new String[]{BCUtil.binName(this.sbi.getHomeInterfaceClass())};
   }

   String getComponentImplName() {
      return BCUtil.binName(this.nc.getEJBObjectClassName());
   }

   Class<?> getComponentInterface() {
      return this.sbi.getRemoteInterfaceClass();
   }

   Class<? extends Exception> getDefExceptionForCreate() {
      return RemoteException.class;
   }

   String getSupersCreateReturnType() {
      return BCUtil.fieldDesc(EJBObject.class);
   }

   Method[] getCreateMethods() {
      return EJBMethodsUtil.getCreateMethods(this.sbi.getHomeInterfaceClass());
   }

   void addCustomMembers(ClassWriter var1) {
      BCUtil.addHomeMembers(var1, this.clsName, this.superClsName, GET_EJB_METADATA_MI, GET_HOME_HANDLE_MI, REMOVE_OBJ_MI, REMOVE_HANDLE_MI);
   }

   private static String getSuperClsName(SessionBeanInfo var0) {
      if (var0.isStateful()) {
         return var0.isEJB30() && ((Ejb3SessionBeanInfo)var0).hasBusinessRemotes() ? BCUtil.binName(StatefulEJBHomeImpl.class) : BCUtil.binName(StatefulEJBHome.class);
      } else {
         return var0.isEJB30() && ((Ejb3SessionBeanInfo)var0).hasBusinessRemotes() ? BCUtil.binName(StatelessEJBHomeImpl.class) : BCUtil.binName(StatelessEJBHome.class);
      }
   }
}
