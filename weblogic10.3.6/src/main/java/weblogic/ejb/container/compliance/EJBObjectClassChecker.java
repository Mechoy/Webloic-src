package weblogic.ejb.container.compliance;

import java.lang.reflect.Method;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.ejb.EJBException;
import javax.ejb.EJBObject;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.cmp.rdbms.RDBMSUtils;
import weblogic.ejb.container.dd.xml.DDUtils;
import weblogic.ejb.container.interfaces.CMPInfo;
import weblogic.ejb.container.interfaces.ClientDrivenBeanInfo;
import weblogic.ejb.container.interfaces.EntityBeanInfo;
import weblogic.ejb.container.persistence.PersistenceUtils;
import weblogic.ejb.container.persistence.spi.CmrField;
import weblogic.ejb.container.persistence.spi.EjbRelation;
import weblogic.ejb.container.persistence.spi.EjbRelationshipRole;
import weblogic.ejb.container.persistence.spi.Relationships;
import weblogic.ejb.container.persistence.spi.RoleSource;
import weblogic.utils.ErrorCollectionException;

final class EJBObjectClassChecker extends BaseComplianceChecker {
   private final Class eoClass;
   private final Class beanClass;
   private final Class ejbObjectInterface;
   private final String ejbName;
   private final boolean isEntityBean;
   private final List businessMethods;
   private CMPInfo cmpInfo;
   private boolean uses20CMP = false;
   private Relationships relationships;
   private Class remoteInterfaceClass;

   EJBObjectClassChecker(Class var1, ClientDrivenBeanInfo var2, Class var3) throws ClassNotFoundException {
      this.eoClass = var1;
      this.beanClass = var2.getBeanClass();
      this.isEntityBean = var2 instanceof EntityBeanInfo;
      this.ejbName = var2.getEJBName();
      this.ejbObjectInterface = var3;
      this.businessMethods = this.getBusinessMethods();
      if (this.isEntityBean) {
         this.cmpInfo = ((EntityBeanInfo)var2).getCMPInfo();
         if (this.cmpInfo != null && this.cmpInfo.uses20CMP()) {
            this.uses20CMP = true;
            this.relationships = this.cmpInfo.getRelationships();
            if (this.checkingRemoteClientView()) {
               try {
                  this.remoteInterfaceClass = var2.getRemoteInterfaceClass();
               } catch (Exception var5) {
               }
            }
         }
      }

   }

   protected boolean checkingRemoteClientView() {
      return this.ejbObjectInterface.equals(EJBObject.class);
   }

   private List getBusinessMethods() {
      HashMap var1 = new HashMap();
      Method[] var2 = this.eoClass.getMethods();
      ArrayList var3 = new ArrayList();

      for(int var4 = 0; var4 < var2.length; ++var4) {
         Method var5 = var2[var4];
         String var6 = var5.getName();

         try {
            this.ejbObjectInterface.getMethod(var6, var5.getParameterTypes());
         } catch (NoSuchMethodException var16) {
            String var8 = var5.getName();
            String var9 = DDUtils.getMethodSignature(var5);
            Method var10 = (Method)var1.get(var9);
            if (var10 != null) {
               Class var11 = var10.getDeclaringClass();
               Class var12 = var5.getDeclaringClass();
               if (var11.isAssignableFrom(var12)) {
                  try {
                     var11.getMethod(var8, var5.getParameterTypes());
                     var3.remove(var10);
                  } catch (NoSuchMethodException var14) {
                  }
               } else if (var12.isAssignableFrom(var11)) {
                  try {
                     var11.getMethod(var8, var5.getParameterTypes());
                     if (var11 == var12) {
                        continue;
                     }
                  } catch (NoSuchMethodException var15) {
                  }
               }
            }

            if (!var8.equals("<clinit>")) {
               var3.add(var5);
               var1.put(var9, var5);
            }
         }
      }

      return var3;
   }

   public void checkEoExtendsEJBObject() throws ComplianceException {
      if (!this.ejbObjectInterface.isAssignableFrom(this.eoClass)) {
         if (this.checkingRemoteClientView()) {
            throw new ComplianceException(this.fmt.EO_IMPLEMENTS_EJBOBJECT(this.ejbName));
         } else {
            throw new ComplianceException(this.fmt.ELO_IMPLEMENTS_EJB_LOCAL_OBJECT(this.ejbName));
         }
      }
   }

   public void checkMethodsThrowRemoteException() throws ErrorCollectionException {
      ErrorCollectionException var1 = new ErrorCollectionException();
      Iterator var2 = this.businessMethods.iterator();

      while(var2.hasNext()) {
         Method var3 = (Method)var2.next();
         boolean var4;
         if (this.checkingRemoteClientView()) {
            var4 = ComplianceUtils.methodThrowsException(var3, RemoteException.class);
            if (!var4) {
               var1.add(new ComplianceException(this.fmt.EO_THROWS_REMOTE_EXCEPTION(this.ejbName, this.methodSig(var3))));
            }
         } else {
            var4 = ComplianceUtils.methodThrowsExactlyException(var3, RemoteException.class);
            if (var4) {
               var1.add(new ComplianceException(this.fmt.ELO_THROWS_REMOTE_EXCEPTION(this.ejbName, this.methodSig(var3))));
            }
         }
      }

      if (!var1.isEmpty()) {
         throw var1;
      }
   }

   public void checkApplicationExceptions() throws ErrorCollectionException {
      ErrorCollectionException var1 = new ErrorCollectionException();
      Iterator var2 = this.businessMethods.iterator();

      while(var2.hasNext()) {
         Method var3 = (Method)var2.next();
         Class[] var4 = var3.getExceptionTypes();

         for(int var5 = 0; var5 < var4.length; ++var5) {
            Class var6 = var4[var5];
            boolean var7 = false;
            if (this.checkingRemoteClientView()) {
               var7 = ComplianceUtils.checkApplicationException(var6, RemoteException.class);
            } else {
               var7 = ComplianceUtils.checkApplicationException(var6, EJBException.class);
            }

            if (!var7) {
               var1.add(new ComplianceException(this.fmt.INVALID_APPLICATION_EXCEPTION(this.ejbName, this.methodSig(var3), var3.getDeclaringClass().getName(), var6.getName())));
            }
         }
      }

      if (!var1.isEmpty()) {
         throw var1;
      }
   }

   private List getMatchingBeanMethodsWithName(String var1) {
      Method[] var2 = this.beanClass.getMethods();
      ArrayList var3 = new ArrayList();

      for(int var4 = 0; var4 < var2.length; ++var4) {
         Method var5 = var2[var4];
         String var6 = var5.getName();
         if (var6.equals(var1)) {
            var3.add(var5);
         }
      }

      return var3;
   }

   public void checkInterfaceBusinessMethodsMatchBeanMethods() throws ErrorCollectionException {
      ErrorCollectionException var1 = new ErrorCollectionException();
      Iterator var2 = this.businessMethods.iterator();

      while(var2.hasNext()) {
         Method var3 = (Method)var2.next();
         String var4 = var3.getName();
         Method var5 = null;

         try {
            boolean var6 = true;
            boolean var17 = false;
            List var18 = this.getMatchingBeanMethodsWithName(var3.getName());
            Iterator var9 = var18.iterator();

            while(var9.hasNext()) {
               var17 = true;
               boolean var10 = true;
               var5 = (Method)var9.next();
               Class[] var11 = var5.getParameterTypes();
               Class[] var12 = var3.getParameterTypes();
               if (var11.length != var12.length) {
                  var10 = false;
               } else {
                  for(int var13 = 0; var13 < var12.length; ++var13) {
                     if (!var11[var13].equals(var12[var13])) {
                        var10 = false;
                        break;
                     }
                  }
               }

               if (var10) {
                  var6 = false;
                  break;
               }
            }

            if (!var17) {
               var5 = this.beanClass.getMethod(var4, var3.getParameterTypes());
            }

            if (var17 && var6) {
               if (this.checkingRemoteClientView()) {
                  var1.add(new ComplianceException(this.fmt.EO_METHOD_SIGNATURE_DOES_NOT_MATCH_BEAN(this.ejbName, this.methodSig(var3))));
               } else {
                  var1.add(new ComplianceException(this.fmt.ELO_METHOD_SIGNATURE_DOES_NOT_MATCH_BEAN(this.ejbName, this.methodSig(var3))));
               }
            }

            if (!ComplianceUtils.returnTypesMatch(var3, var5)) {
               if (this.checkingRemoteClientView()) {
                  var1.add(new ComplianceException(this.fmt.EO_RETURN_TYPE_DOESNT_MATCH_BEAN(this.ejbName, this.methodSig(var3))));
               } else {
                  var1.add(new ComplianceException(this.fmt.ELO_RETURN_TYPE_DOESNT_MATCH_BEAN(this.ejbName, this.methodSig(var3))));
               }
            }

            try {
               ComplianceUtils.exceptionTypesMatch(var3, var5);
            } catch (ExceptionTypeMismatchException var14) {
               throw var14;
            }
         } catch (NoSuchMethodException var15) {
            if (this.checkingRemoteClientView()) {
               var1.add(new ComplianceException(this.fmt.EO_METHOD_DOESNT_EXIST_IN_BEAN(this.ejbName, this.methodSig(var3))));
            } else {
               var1.add(new ComplianceException(this.fmt.ELO_METHOD_DOESNT_EXIST_IN_BEAN(this.ejbName, this.methodSig(var3))));
            }
         } catch (ExceptionTypeMismatchException var16) {
            String var7 = this.methodSig(var3);
            String var8 = this.checkingRemoteClientView() ? this.fmt.EO_EXCEPTION_TYPE_DOESNT_MATCH_BEAN(this.ejbName, var7) : this.fmt.ELO_EXCEPTION_TYPE_DOESNT_MATCH_BEAN(this.ejbName, var7);
            EJBLogger.logComplianceWarning(this.ejbName, var7, this.checkingRemoteClientView() ? "remote" : "local", new ComplianceException(var8));
         }
      }

      if (!var1.isEmpty()) {
         throw var1;
      }
   }

   public void checkLocalExposeThroughRemote() throws ErrorCollectionException {
      if (this.checkingRemoteClientView()) {
         ErrorCollectionException var1 = new ErrorCollectionException();
         Method[] var2 = this.eoClass.getMethods();

         for(int var3 = 0; var3 < var2.length; ++var3) {
            Method var4 = var2[var3];
            if (ComplianceUtils.localExposeThroughRemote(var4)) {
               var1.add(new ComplianceException(this.fmt.LOCAL_INTERFACE_TYPES_EXPOSE_THROUGH_REMOTE_INTERFACE(this.ejbName, this.methodSig(var4))));
            }
         }

         if (!var1.isEmpty()) {
            throw var1;
         }
      }
   }

   public void checkRelationExposedThroughRemote() throws ErrorCollectionException {
      if (this.checkingRemoteClientView()) {
         if (this.uses20CMP) {
            if (this.relationships != null) {
               Map var1 = this.relationships.getAllEjbRelations();
               if (var1.size() > 0) {
                  HashMap var2 = new HashMap();
                  Iterator var3 = var1.keySet().iterator();

                  Iterator var6;
                  String var11;
                  while(var3.hasNext()) {
                     Object var4 = var3.next();
                     EjbRelation var5 = (EjbRelation)var1.get(var4);
                     var6 = var5.getAllEjbRelationshipRoles().iterator();

                     while(var6.hasNext()) {
                        EjbRelationshipRole var7 = (EjbRelationshipRole)var6.next();
                        RoleSource var8 = var7.getRoleSource();
                        if (var8.getEjbName().equals(this.ejbName)) {
                           CmrField var9 = var7.getCmrField();
                           if (var9 != null) {
                              String var10 = var9.getName();
                              var11 = var9.getType();
                              String var12 = RDBMSUtils.getterMethodName(var10);
                              String var13 = RDBMSUtils.setterMethodName(var10);
                              var2.put(var12, var11);
                              var2.put(var13, var11);
                           }
                        }
                     }
                  }

                  ErrorCollectionException var14 = new ErrorCollectionException();
                  Map var15 = PersistenceUtils.getAccessorMethodMap(this.beanClass);
                  Map var16 = PersistenceUtils.getAccessorMethodMap(this.remoteInterfaceClass);
                  var6 = var16.keySet().iterator();

                  while(var6.hasNext()) {
                     String var17 = (String)var6.next();
                     if (var2.get(var17) != null) {
                        Method var18 = (Method)var15.get(var17);
                        String var19 = DDUtils.getMethodSignature(var18);
                        Method var20 = (Method)var16.get(var17);
                        var11 = DDUtils.getMethodSignature(var20);
                        if (var11.equals(var19)) {
                           var14.add(new ComplianceException(this.fmt.CANNOT_EXPOSE_RELATIONSHIP_ACCESSOR_IN_REMOTE(this.ejbName, this.remoteInterfaceClass.getName(), var17)));
                        }
                     }
                  }

                  if (!var14.isEmpty()) {
                     throw var14;
                  }
               }
            }
         }
      }
   }
}
