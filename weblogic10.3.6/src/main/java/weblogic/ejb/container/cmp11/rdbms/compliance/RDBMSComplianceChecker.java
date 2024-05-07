package weblogic.ejb.container.cmp11.rdbms.compliance;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import weblogic.ejb.container.cmp11.rdbms.codegen.TypeUtils;
import weblogic.ejb.container.compliance.BaseComplianceChecker;
import weblogic.ejb.container.compliance.EJBComplianceChecker;
import weblogic.ejb.container.ejbc.EJBCException;
import weblogic.ejb.container.persistence.spi.CMPBeanDescriptor;
import weblogic.j2ee.validation.ComplianceException;
import weblogic.utils.ErrorCollectionException;

public final class RDBMSComplianceChecker extends BaseComplianceChecker {
   private CMPBeanDescriptor bd = null;
   private Class ejbClass = null;
   private String ejbName = null;
   private List fieldList = null;
   private ErrorCollectionException errors = null;

   public RDBMSComplianceChecker(CMPBeanDescriptor var1, Class var2, List var3) {
      if (!EJBComplianceChecker.isNeedCheck) {
         this.bd = var1;
         this.ejbClass = var2;
         this.fieldList = var3;
         this.ejbName = var1.getEJBName();
         this.errors = new ErrorCollectionException();
      }
   }

   public void checkCompliance() throws ErrorCollectionException {
      this.checkCMPFields();
      if (!this.errors.isEmpty()) {
         throw this.errors;
      }
   }

   private void checkCMPFields() {
      Iterator var1 = this.fieldList.iterator();

      while(var1.hasNext()) {
         String var2 = (String)var1.next();
         Field var3 = null;

         try {
            var3 = this.ejbClass.getField(var2);
         } catch (NoSuchFieldException var7) {
            this.errors.add(new ComplianceException(this.fmt.CMP_FIELDS_MUST_BE_BEAN_FIELDS(this.ejbName, var2)));
         }

         Class var4 = var3.getType();

         try {
            int var5 = TypeUtils.getSQLTypeForClass(var4);
         } catch (EJBCException var6) {
            this.errors.add(new ComplianceException(this.fmt.CMP_FIELD_CLASS_NOT_SUPPORTED_IN_CMP11(this.ejbName, var3.getName(), var4.getName())));
         }
      }

   }
}
