package weblogic.ejb.container.compliance;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Vector;
import javax.ejb.FinderException;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.dd.xml.DDUtils;
import weblogic.ejb.container.interfaces.CMPInfo;
import weblogic.ejb.container.interfaces.ClientDrivenBeanInfo;
import weblogic.ejb.container.interfaces.EntityBeanInfo;
import weblogic.ejb.container.utils.ClassUtils;
import weblogic.i18n.Localizer;
import weblogic.i18ntools.L10nLookup;
import weblogic.utils.Debug;
import weblogic.utils.ErrorCollectionException;

class EntityHomeInterfaceChecker extends HomeInterfaceChecker {
   static final String EJB_HOME = "ejbHome";
   private Class pkClass;
   private EntityBeanInfo ebi;
   private boolean isCMP = false;
   private boolean isCMP20 = false;
   private static Localizer l10n = L10nLookup.getLocalizer(Locale.getDefault(), "weblogic.ejb.container.compliance.EJBComplianceTextLocalizer");

   EntityHomeInterfaceChecker(Class var1, Class var2, Class var3, ClientDrivenBeanInfo var4, Class var5) throws ClassNotFoundException {
      super(var1, var2, var3, var4, var5);
      this.ebi = (EntityBeanInfo)var4;
      this.pkClass = this.ebi.getPrimaryKeyClass();
      CMPInfo var6 = this.ebi.getCMPInfo();
      if (var6 != null) {
         this.isCMP = true;
         this.isCMP20 = var6.uses20CMP();
      }

   }

   public void checkHomeContainsFindByPK() throws ComplianceException {
      Method[] var1 = this.homeInterface.getMethods();
      Method var2 = null;

      for(int var3 = 0; var3 < var1.length; ++var3) {
         if ("findByPrimaryKey".equals(var1[var3].getName())) {
            var2 = var1[var3];
            break;
         }
      }

      if (var2 == null) {
         if (this.checkingRemoteClientView()) {
            throw new ComplianceException(this.fmt.HOME_MUST_HAVE_FIND_PK(this.ejbName));
         } else {
            throw new ComplianceException(this.fmt.LOCAL_HOME_MUST_HAVE_FIND_PK(this.ejbName));
         }
      } else {
         Class var5 = var2.getReturnType();
         if (!var5.equals(this.compInterface)) {
            if (this.checkingRemoteClientView()) {
               throw new ComplianceException(this.fmt.FIND_BY_PK_RETURNS_REMOTE_INTF(this.ejbName));
            } else {
               throw new ComplianceException(this.fmt.FIND_BY_PK_RETURNS_LOCAL_INTF(this.ejbName));
            }
         } else {
            Class[] var4 = var2.getParameterTypes();
            if (var4.length != 1 || !var4[0].equals(Object.class) && !var4[0].equals(this.pkClass)) {
               if (this.checkingRemoteClientView()) {
                  throw new ComplianceException(this.fmt.HOME_FIND_PK_CORRECT_PARAMETERS(this.ejbName, this.methodSig(var2)));
               } else {
                  throw new ComplianceException(this.fmt.LOCAL_HOME_FIND_PK_CORRECT_PARAMETERS(this.ejbName, this.methodSig(var2)));
               }
            }
         }
      }
   }

   public void checkFindThrowsFinderException() throws ErrorCollectionException {
      Iterator var1 = ClassUtils.getFinderMethods(this.homeInterface);
      ErrorCollectionException var2 = new ErrorCollectionException();

      while(var1.hasNext()) {
         Method var3 = (Method)var1.next();
         if (!ComplianceUtils.methodThrowsException(var3, FinderException.class)) {
            if (this.checkingRemoteClientView()) {
               var2.add(new ComplianceException(this.fmt.FINDER_MUST_THROW_FE(this.ejbName, this.methodSig(var3))));
            } else {
               var2.add(new ComplianceException(this.fmt.LOCAL_FINDER_MUST_THROW_FE(this.ejbName, this.methodSig(var3))));
            }
         }
      }

      if (!var2.isEmpty()) {
         throw var2;
      }
   }

   private boolean goodFinderReturn(Class var1) {
      if (var1.equals(this.compInterface)) {
         return true;
      } else if (!this.isCMP20 && Enumeration.class.isAssignableFrom(var1)) {
         return true;
      } else {
         Class var2 = Collection.class;
         return var2.equals(var1);
      }
   }

   public void checkFinderReturnsRemoteOrCollection() throws ErrorCollectionException {
      ErrorCollectionException var1 = new ErrorCollectionException();
      Iterator var2 = ClassUtils.getFinderMethods(this.homeInterface);

      while(var2.hasNext()) {
         Method var3 = (Method)var2.next();
         Class var4 = var3.getReturnType();
         if (!this.goodFinderReturn(var4)) {
            if (this.checkingRemoteClientView()) {
               var1.add(new ComplianceException(this.fmt.FINDER_RETURNS_BAD_TYPE(this.ejbName, this.methodSig(var3))));
            } else {
               var1.add(new ComplianceException(this.fmt.LOCAL_FINDER_RETURNS_BAD_TYPE(this.ejbName, this.methodSig(var3))));
            }
         }
      }

      if (!var1.isEmpty()) {
         throw var1;
      }
   }

   private String beanFindName(String var1) {
      Debug.assertion(var1.startsWith("find"));
      return "ejbF" + var1.substring(1);
   }

   public void checkFindMethodsMatchBeanMethods() throws ErrorCollectionException {
      if (this.ebi.getIsBeanManagedPersistence()) {
         Iterator var1 = ClassUtils.getFinderMethods(this.homeInterface);
         ErrorCollectionException var2 = new ErrorCollectionException();

         while(var1.hasNext()) {
            Method var3 = (Method)var1.next();

            try {
               Method var4 = this.beanClass.getMethod(this.beanFindName(var3.getName()), var3.getParameterTypes());
               ComplianceUtils.exceptionTypesMatch(var3, var4);
               Class var5 = var4.getReturnType();
               Class var6 = var3.getReturnType();
               if (var5.equals(this.pkClass)) {
                  if (!var6.equals(this.compInterface)) {
                     if (this.checkingRemoteClientView()) {
                        var2.add(new ComplianceException(this.fmt.SCALAR_FINDER_DOESNT_RETURN_REMOTE_INTF(this.ejbName, this.methodSig(var3))));
                     } else {
                        var2.add(new ComplianceException(this.fmt.SCALAR_FINDER_DOESNT_RETURN_LOCAL_INTF(this.ejbName, this.methodSig(var3))));
                     }
                  }
               } else if (var5.equals(Enumeration.class)) {
                  if (!var6.equals(Enumeration.class)) {
                     var2.add(new ComplianceException(this.fmt.ENUM_FINDER_DOESNT_RETURN_REMOTE_INTF(this.ejbName, this.methodSig(var3))));
                  }
               } else if (var5.equals(Collection.class)) {
                  if (!var6.equals(Collection.class)) {
                     if (this.checkingRemoteClientView()) {
                        var2.add(new ComplianceException(this.fmt.COLL_FINDER_DOESNT_RETURN_COLL(this.ejbName, this.methodSig(var3))));
                     } else {
                        var2.add(new ComplianceException(this.fmt.LOCAL_COLL_FINDER_DOESNT_RETURN_COLL(this.ejbName, this.methodSig(var3))));
                     }
                  }
               } else {
                  var2.add(new ComplianceException(this.fmt.UNEXPECTED_FINDER_RETURN_TYPE(this.ejbName, this.methodSig(var4))));
               }
            } catch (NoSuchMethodException var7) {
               var2.add(new ComplianceException(this.fmt.FIND_METHOD_DOESNT_EXIST_IN_BEAN(this.ejbName, this.methodSig(var3))));
            } catch (ExceptionTypeMismatchException var8) {
               if (this.checkingRemoteClientView()) {
                  var2.add(new ComplianceException(this.fmt.FIND_EXCEPTION_TYPE_DOESNT_MATCH_BEAN(this.ejbName, this.methodSig(var3))));
               } else {
                  var2.add(new ComplianceException(this.fmt.LOCAL_FIND_EXCEPTION_TYPE_DOESNT_MATCH_BEAN(this.ejbName, this.methodSig(var3))));
               }
            }
         }

         if (!var2.isEmpty()) {
            throw var2;
         }
      }
   }

   public void checkCreateMethodsMatchBeanPostCreateMethods() throws ErrorCollectionException {
      List var1 = this.getCreateMethods();
      ErrorCollectionException var2 = new ErrorCollectionException();
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         Method var4 = (Method)var3.next();

         try {
            String var5 = "ejbPostC" + var4.getName().substring(1);
            Method var6 = this.beanClass.getMethod(var5, var4.getParameterTypes());
            ComplianceUtils.exceptionTypesMatch(var4, var6);
         } catch (NoSuchMethodException var7) {
            var2.add(new ComplianceException(this.fmt.POST_CREATE_METHOD_DOESNT_EXIST_IN_BEAN(this.ejbName, this.methodSig(var4))));
         } catch (ExceptionTypeMismatchException var8) {
            var2.add(new ComplianceException(this.fmt.POST_CREATE_EXCEPTION_TYPE_DOESNT_MATCH_BEAN(this.ejbName, this.methodSig(var4))));
         }
      }

      if (!var2.isEmpty()) {
         throw var2;
      }
   }

   public void checkForExtraMethodsInHomeInterface() throws ErrorCollectionException {
      Vector var1 = new Vector();
      Vector var2 = new Vector();
      ErrorCollectionException var3 = new ErrorCollectionException();
      Method[] var4 = this.homeInterface.getMethods();
      this.computeBeanHomeMethods(var3, var1);

      Method var6;
      for(int var5 = 0; var5 < var4.length; ++var5) {
         var6 = var4[var5];
         String var7 = var6.getName();

         try {
            this.ejbHomeInterface.getMethod(var7, var6.getParameterTypes());
         } catch (NoSuchMethodException var10) {
            boolean var9 = var7.startsWith("create") || var7.startsWith("find");
            if (!var9 && !var7.equals("<clinit>")) {
               var2.addElement(var6);
            }
         }
      }

      Enumeration var11 = var2.elements();

      while(var11.hasMoreElements()) {
         var6 = (Method)var11.nextElement();
         if (!var1.contains(var6)) {
            if (this.checkingRemoteClientView()) {
               var3.add(new ComplianceException(this.fmt.EXTRA_HOME_METHOD_20(this.ejbName, this.methodSig(var6), DDUtils.getEjbHomeMethodSignature(var6))));
            } else {
               var3.add(new ComplianceException(this.fmt.EXTRA_LOCAL_HOME_METHOD_20(this.ejbName, this.methodSig(var6), DDUtils.getEjbHomeMethodSignature(var6))));
            }
         }
      }

      if (!var3.isEmpty()) {
         throw var3;
      }
   }

   public void computeBeanHomeMethods(ErrorCollectionException var1, Vector var2) throws ErrorCollectionException {
      Method[] var3 = this.beanClass.getMethods();

      for(int var4 = 0; var4 < var3.length; ++var4) {
         String var5 = var3[var4].getName();
         if (var5.startsWith("ejbHome")) {
            if (var5.length() <= "ejbHome".length()) {
               var1.add(new ComplianceException(this.fmt.HOME_METHOD_NAME_IN_BEAN_CLASS_INCOMPLETE_20(this.beanClass.getName())));
            } else {
               this.beanClassHomeMethodNameCheck(this.beanClass, var5, var1);
               boolean var6 = false;

               try {
                  var6 = this.isValidHomeMethod(var5, var3[var4].getParameterTypes(), var3[var4].getReturnType(), this.homeInterface, this.beanClass);
               } catch (Exception var10) {
                  var6 = true;
                  var1.add(var10);
               }

               if (var6) {
                  try {
                     String var7 = var5.substring("ejbHome".length());
                     var7 = Character.toLowerCase(var7.charAt(0)) + var7.substring(1);
                     Method var8 = this.homeInterface.getMethod(var7, var3[var4].getParameterTypes());
                     var2.addElement(var8);
                  } catch (NoSuchMethodException var9) {
                     EJBLogger.logStackTrace(var9);
                  }
               }
            }
         }
      }

   }

   public boolean isValidHomeMethod(String var1, Class[] var2, Class var3, Class var4, Class var5) throws Exception {
      boolean var6 = false;

      try {
         String var7 = var1.substring("ejbHome".length());
         var7 = Character.toLowerCase(var7.charAt(0)) + var7.substring(1);
         Method var8 = var4.getMethod(var7, var2);
         Class var9 = var8.getReturnType();
         if (!var3.equals(var9)) {
            EJBComplianceTextFormatter var10 = new EJBComplianceTextFormatter();
            if (this.checkingRemoteClientView()) {
               throw new Exception(var10.EJB_HOME_METHOD_RETURN_TYPE_SHOULD_MATCH(this.ejbName, var4.getName(), var7, var9.getName(), var5.getName(), var3.getName(), "remote"));
            }

            throw new Exception(var10.EJB_HOME_METHOD_RETURN_TYPE_SHOULD_MATCH(this.ejbName, var4.getName(), var7, var9.getName(), var5.getName(), var3.getName(), "local"));
         }

         var6 = true;
      } catch (NoSuchMethodException var11) {
      }

      return var6;
   }

   public void beanClassHomeMethodNameCheck(Class var1, String var2, ErrorCollectionException var3) {
      String var4 = var2.substring("ejbHome".length());
      if (Character.isLowerCase(var4.charAt(0))) {
         StringBuffer var5 = new StringBuffer("ejbHome");
         var5.append(Character.toUpperCase(var4.charAt(0)));
         if (var4.length() > 1) {
            var5.append(var4.substring(1));
         }

         var3.add(new ComplianceException(this.fmt.HOME_METHOD_NAME_IN_BEAN_CLASS_LOWER_CASE_20(var1.getName(), var2, var5.toString())));
      }

   }

   private static void p(String var0) {
      System.out.println(l10n.get("entityhomeInterfaceCheckerMessage") + var0);
   }
}
