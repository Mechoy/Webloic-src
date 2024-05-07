package weblogic.ejb.container.compliance;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import weblogic.ejb.container.interfaces.EntityBeanInfo;
import weblogic.ejb20.dd.DescriptorErrorInfo;
import weblogic.utils.AssertionError;

final class PKClassChecker extends BaseComplianceChecker {
   private static final boolean debug = false;
   private static final boolean verbose = false;
   private EntityBeanInfo ebi;
   private Class pkClass;
   private String ejbName;
   private boolean isCMP;
   private boolean isCompoundCMPPK;
   private Class[] wrapperClasses = new Class[]{Boolean.class, Byte.class, Character.class, Double.class, Float.class, Integer.class, Long.class, Short.class, String.class};

   PKClassChecker(EntityBeanInfo var1) throws ClassNotFoundException {
      this.ebi = var1;
      this.pkClass = var1.getPrimaryKeyClass();
      this.ejbName = var1.getEJBName();
      this.isCMP = !var1.getIsBeanManagedPersistence();
      if (this.isCMP) {
         this.isCompoundCMPPK = !var1.isUnknownPrimaryKey() && var1.getCMPInfo().getCMPrimaryKeyFieldName() == null;
      }

   }

   private boolean isWrapperClass(Class var1) {
      for(int var2 = 0; var2 < this.wrapperClasses.length; ++var2) {
         if (var1.equals(this.wrapperClasses[var2])) {
            return true;
         }
      }

      return false;
   }

   public void checkPrimitivePKHasFieldSet() throws ComplianceException {
      if (this.isCMP && this.isWrapperClass(this.pkClass) && this.ebi.getCMPInfo().getCMPrimaryKeyFieldName() == null) {
         throw new ComplianceException(this.fmt.PRIMARY_KEY_WITHOUT_PRIMKEY_FIELD(this.pkClass.getName(), this.ejbName), new DescriptorErrorInfo("<primkey-field>", this.ejbName, this.pkClass.getName()));
      }
   }

   public void checkPKImplementsHashCode() throws ComplianceException {
      if (!Object.class.equals(this.pkClass)) {
         try {
            Method var1 = this.pkClass.getMethod("hashCode", (Class[])null);
            if (Object.class.equals(var1.getDeclaringClass())) {
               throw new ComplianceException(this.fmt.PK_MUST_IMPLEMENT_HASHCODE(this.ejbName));
            }
         } catch (NoSuchMethodException var2) {
            throw new AssertionError("hashCode() not found?!");
         }
      }
   }

   public void checkPKImplementsEquals() throws ComplianceException {
      if (!Object.class.equals(this.pkClass)) {
         try {
            Method var1 = this.pkClass.getMethod("equals", Object.class);
            if (Object.class.equals(var1.getDeclaringClass())) {
               throw new ComplianceException(this.fmt.PK_MUST_IMPLEMENT_EQUALS(this.ejbName));
            }
         } catch (NoSuchMethodException var2) {
            throw new AssertionError("equals(Object) not found?!");
         }
      }
   }

   public void checkPKImplementsSerializable() throws ComplianceException {
      if (!Object.class.equals(this.pkClass)) {
         if (!Serializable.class.isAssignableFrom(this.pkClass)) {
            throw new ComplianceException(this.fmt.CMP_PK_MUST_IMPLEMENT_SERIALIZABLE(this.ejbName));
         }
      }
   }

   public void checkPKClassNotObject() throws ComplianceException {
      if (this.isCMP && !this.ebi.getCMPInfo().uses20CMP() && Object.class.equals(this.pkClass)) {
         throw new ComplianceException(this.fmt.CMP_PK_CANNOT_BE_JAVA_LANG_OBJECT(this.ejbName));
      }
   }

   public void checkCMPPublicPK() throws ComplianceException {
      if (this.isCMP && this.isCompoundCMPPK) {
         int var1 = this.pkClass.getModifiers();
         if (!Modifier.isPublic(var1)) {
            throw new ComplianceException(this.fmt.CMP_PK_MUST_BE_PUBLIC(this.ejbName, this.pkClass.getName()));
         }
      }

   }

   public void checkCMPPKDefaultNoArgConstructor() throws ComplianceException {
      if (this.isCMP && this.isCompoundCMPPK) {
         Constructor var1 = null;

         try {
            var1 = this.pkClass.getConstructor();
         } catch (NoSuchMethodException var3) {
            throw new ComplianceException(this.fmt.CMP_PK_MUST_HAVE_NOARG_CONSTRUCTOR(this.ejbName, this.pkClass.getName()));
         }

         int var2 = var1.getModifiers();
         if (!Modifier.isPublic(var2)) {
            throw new ComplianceException(this.fmt.CMP_PK_MUST_HAVE_NOARG_CONSTRUCTOR(this.ejbName, this.pkClass.getName()));
         }
      }

   }

   public void checkCMPFieldsModifiers() throws ComplianceException {
      if (this.isCMP && this.isCompoundCMPPK) {
         Collection var1 = this.ebi.getCMPInfo().getAllContainerManagedFieldNames();
         Field[] var2 = this.pkClass.getFields();
         if (var2 == null || var2.length == 0) {
            throw new ComplianceException(this.fmt.PK_FIELD_CLASS_MUST_HAVE_ATLEAST_ONE_CMP_FIELD(this.ejbName, this.pkClass.getName()));
         }

         boolean var3 = false;

         for(int var4 = 0; var4 < var2.length; ++var4) {
            Field var5 = var2[var4];
            if (!var5.getName().equals("serialVersionUID") && var1.contains(var2[var4].getName())) {
               var3 = true;
               int var6 = var5.getModifiers();
               if (!Modifier.isPublic(var6)) {
                  throw new ComplianceException(this.fmt.PK_FIELDS_MUST_BE_PUBLIC(this.ejbName, this.pkClass.getName(), var5.getName()));
               }

               if (Modifier.isStatic(var6) && !Modifier.isFinal(var6)) {
                  throw new ComplianceException(this.fmt.PK_FIELDS_MUST_NOT_BE_STATIC(this.ejbName, this.pkClass.getName(), var5.getName()));
               }
            }
         }

         if (!var3) {
            if (var2.length > 0) {
               throw new ComplianceException(this.fmt.FIELDS_IN_PK_CLASS_SHOULD_BE_CMP_FIELDS(this.ejbName, this.pkClass.getName()));
            }

            throw new ComplianceException(this.fmt.PK_FIELD_CLASS_MUST_HAVE_ATLEAST_ONE_CMP_FIELD(this.ejbName, this.pkClass.getName()));
         }
      }

   }
}
