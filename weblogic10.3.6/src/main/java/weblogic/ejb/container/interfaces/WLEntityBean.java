package weblogic.ejb.container.interfaces;

public interface WLEntityBean extends WLEnterpriseBean {
   void __WL_setOperationsComplete(boolean var1);

   boolean __WL_getOperationsComplete();

   void __WL_setBeanStateValid(boolean var1);

   boolean __WL_isBeanStateValid();

   void __WL_setLastLoadTime(long var1);

   long __WL_getLastLoadTime();
}
