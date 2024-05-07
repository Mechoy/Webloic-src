package weblogic.kernel;

import java.beans.PropertyChangeListener;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.MBeanInfo;
import javax.management.MBeanNotificationInfo;
import javax.management.MBeanServer;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;
import javax.management.ObjectName;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import weblogic.descriptor.BeanUpdateListener;
import weblogic.descriptor.Descriptor;
import weblogic.descriptor.DescriptorBean;
import weblogic.management.WebLogicMBean;
import weblogic.management.WebLogicObjectName;
import weblogic.utils.StackTraceUtils;
import weblogic.utils.collections.PropertiesHelper;

public abstract class MBeanStub {
   private static final Class[] STRING_PARAM = new Class[]{String.class};
   private static final Map primitivePromotionMap = new HashMap();
   private boolean isPersisted = true;
   private boolean isDefaulted = false;

   private static Class promote(Class var0) {
      Class var1 = (Class)primitivePromotionMap.get(var0);
      return var1 == null ? var0 : var1;
   }

   protected final void initializeFromSystemProperties(String var1) {
      if (!KernelEnvironment.getKernelEnvironment().isInitializeFromSystemPropertiesAllowed(var1)) {
         throw new UnsupportedOperationException("Initialize WebLogic system properties with prefix " + var1 + " is not allowed");
      } else {
         Class var2 = this.getClass();
         Method[] var3 = var2.getMethods();

         for(int var4 = 0; var4 < var3.length; ++var4) {
            Method var5 = var3[var4];
            Class[] var6 = var5.getParameterTypes();
            if (var6.length == 1) {
               String var7 = var5.getName();
               if (var7.startsWith("set") && var7.length() != 3) {
                  String var8 = var7.substring(3);
                  String var9 = var1 + var8;
                  String var10 = null;

                  try {
                     var10 = System.getProperty(var9);
                     if (var10 == null) {
                        continue;
                     }
                  } catch (SecurityException var19) {
                     if (Kernel.isApplet()) {
                        return;
                     }

                     KernelLogger.logErrorInitialingFromSystemProperties(var2.getName(), var8, "", StackTraceUtils.throwable2StackTrace(var19));
                  }

                  if (var6[0] == Properties.class) {
                     Properties var11 = PropertiesHelper.parse(var10);

                     try {
                        var5.invoke(this, var11);
                     } catch (InvocationTargetException var13) {
                        KernelLogger.logErrorInitialingFromSystemProperties(var2.getName(), var8, var10, StackTraceUtils.throwable2StackTrace(var13.getTargetException()));
                     } catch (Exception var14) {
                        KernelLogger.logErrorInitialingFromSystemProperties(var2.getName(), var8, var10, StackTraceUtils.throwable2StackTrace(var14));
                     }
                  } else {
                     Constructor var20 = null;

                     try {
                        var20 = promote(var6[0]).getConstructor(STRING_PARAM);
                     } catch (NoSuchMethodException var17) {
                        KernelLogger.logNoConstructorWithStringParam(var2.getName(), var5.getName(), var8, var10);
                     } catch (Exception var18) {
                        KernelLogger.logErrorInitialingFromSystemProperties(var2.getName(), var8, var10, StackTraceUtils.throwable2StackTrace(var18));
                     }

                     if (var20 != null) {
                        try {
                           var5.invoke(this, var20.newInstance(var10));
                        } catch (InvocationTargetException var15) {
                           KernelLogger.logErrorInitialingFromSystemProperties(var2.getName(), var8, var10, StackTraceUtils.throwable2StackTrace(var15.getTargetException()));
                        } catch (Exception var16) {
                           KernelLogger.logErrorInitialingFromSystemProperties(var2.getName(), var8, var10, StackTraceUtils.throwable2StackTrace(var16));
                        }
                     }
                  }
               }
            }
         }

      }
   }

   public final Object getKey() {
      return this.getName();
   }

   public final String getAttributeStringValue(String var1) {
      return null;
   }

   public final Set getSetFields() {
      return null;
   }

   public final String getNotes() {
      return null;
   }

   public final void setNotes(String var1) {
   }

   public final Element getXml(Document var1) {
      return null;
   }

   public final Element getXmlConverter(Document var1) {
      return null;
   }

   public void freezeCurrentValue(String var1) {
   }

   public void restoreDefaultValue(String var1) {
   }

   public void addPropertyChangeListener(PropertyChangeListener var1) {
      throw new UnsupportedOperationException();
   }

   public void removePropertyChangeListener(PropertyChangeListener var1) {
      throw new UnsupportedOperationException();
   }

   public boolean isPersistenceEnabled() {
      return this.isPersisted;
   }

   public void setPersistenceEnabled(boolean var1) {
      this.isPersisted = var1;
   }

   public boolean isDefaultedMBean() {
      return this.isDefaulted;
   }

   public void setDefaultedMBean(boolean var1) {
      this.isDefaulted = var1;
   }

   public String getComments() {
      return null;
   }

   public void setComments(String var1) {
   }

   public void addLinkMbeanAttribute(String var1, ObjectName var2) {
   }

   public final void registerConfigMBean(String var1, MBeanServer var2) {
   }

   public final void unRegisterConfigMBean(String var1) {
   }

   public final void touch() {
   }

   public void linkToRepository(Object var1) {
   }

   public String getName() {
      return null;
   }

   public final void setName(String var1) {
   }

   public final String getType() {
      return null;
   }

   public final WebLogicObjectName getObjectName() {
      return null;
   }

   public final boolean isCachingDisabled() {
      return false;
   }

   public final WebLogicMBean getParent() {
      return null;
   }

   public final void setParent(WebLogicMBean var1) {
   }

   public final boolean isRegistered() {
      return true;
   }

   public final void postDeregister() {
   }

   public final void preDeregister() {
   }

   public final void postRegister(Boolean var1) {
   }

   public final ObjectName preRegister(MBeanServer var1, ObjectName var2) {
      return null;
   }

   public final void addNotificationListener(NotificationListener var1, NotificationFilter var2, Object var3) {
   }

   public final void removeNotificationListener(NotificationListener var1) {
   }

   public final MBeanNotificationInfo[] getNotificationInfo() {
      return null;
   }

   public final Object getAttribute(String var1) {
      return null;
   }

   public final void setAttribute(Attribute var1) {
   }

   public final AttributeList getAttributes(String[] var1) {
      return null;
   }

   public final AttributeList setAttributes(AttributeList var1) {
      return null;
   }

   public final MBeanInfo getMBeanInfo() {
      return null;
   }

   public final Object invoke(String var1, Object[] var2, String[] var3) {
      return null;
   }

   public Descriptor getDescriptor() {
      return null;
   }

   public DescriptorBean getParentBean() {
      return null;
   }

   public boolean isSet(String var1) {
      return false;
   }

   public void unSet(String var1) {
   }

   public void addBeanUpdateListener(BeanUpdateListener var1) {
   }

   public void removeBeanUpdateListener(BeanUpdateListener var1) {
   }

   public boolean isEditable() {
      return false;
   }

   public Object clone() {
      return null;
   }

   public DescriptorBean createChildCopy(String var1, DescriptorBean var2) {
      throw new UnsupportedOperationException();
   }

   public DescriptorBean createChildCopyIncludingObsolete(String var1, DescriptorBean var2) {
      throw new UnsupportedOperationException();
   }

   static {
      primitivePromotionMap.put(Boolean.TYPE, Boolean.class);
      primitivePromotionMap.put(Character.TYPE, Character.class);
      primitivePromotionMap.put(Byte.TYPE, Byte.class);
      primitivePromotionMap.put(Short.TYPE, Short.class);
      primitivePromotionMap.put(Integer.TYPE, Integer.class);
      primitivePromotionMap.put(Long.TYPE, Long.class);
      primitivePromotionMap.put(Float.TYPE, Float.class);
      primitivePromotionMap.put(Double.TYPE, Double.class);
   }
}
