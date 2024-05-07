package weblogic.ant.taskdefs.management;

import java.io.Serializable;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.QueryExp;
import javax.management.ReflectionException;

public class MBeanCommoQuery implements QueryExp, Serializable {
   MBeanServer mbs;
   public ObjectName targetType;

   public void setTargetType(ObjectName var1) {
      this.targetType = var1;
   }

   public void setMBeanServer(MBeanServer var1) {
      this.mbs = var1;
   }

   public boolean apply(ObjectName var1) {
      try {
         if (this.mbs.isInstanceOf(var1, "weblogic.management.commo.CommoModelMBean") && !"CustomMBeanType".equals(var1.getKeyProperty("Type"))) {
            if (this.targetType == null) {
               return true;
            }

            ObjectName var2 = (ObjectName)this.mbs.invoke(var1, "getTypeObjectName", new Object[0], new String[0]);
            if (var2 != null && var2.equals(this.targetType)) {
               return true;
            }
         }
      } catch (InstanceNotFoundException var3) {
      } catch (MBeanException var4) {
      } catch (ReflectionException var5) {
      }

      return false;
   }
}
