package weblogic.management.security.authorization;

import com.bea.common.security.xacml.policy.Policy;
import com.bea.common.security.xacml.policy.PolicySet;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.zip.CRC32;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBeanHelper;
import weblogic.descriptor.internal.AbstractSchemaHelper2;
import weblogic.descriptor.internal.Munger;
import weblogic.descriptor.internal.SchemaHelper;
import weblogic.management.utils.AlreadyExistsException;
import weblogic.management.utils.CreateException;
import weblogic.management.utils.NotFoundException;
import weblogic.management.utils.PropertiesListerMBeanImpl;
import weblogic.management.utils.RemoveException;
import weblogic.utils.collections.CombinedIterator;

public class PolicyStoreMBeanImpl extends PropertiesListerMBeanImpl implements PolicyStoreMBean, Serializable {
   private static SchemaHelper2 _schemaHelper;

   public PolicyStoreMBeanImpl() {
      this._initializeProperty(-1);
   }

   public PolicyStoreMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);
      this._initializeProperty(-1);
   }

   public String listAllPolicies() throws NotFoundException {
      throw new AssertionError("Method not implemented");
   }

   public String listAllPoliciesAsString() throws NotFoundException {
      throw new AssertionError("Method not implemented");
   }

   public String listAllPolicySets() throws NotFoundException {
      throw new AssertionError("Method not implemented");
   }

   public String listAllPolicySetsAsString() throws NotFoundException {
      throw new AssertionError("Method not implemented");
   }

   public Policy readPolicy(String var1, String var2) throws NotFoundException {
      throw new AssertionError("Method not implemented");
   }

   public String readPolicyAsString(String var1, String var2) throws NotFoundException {
      throw new AssertionError("Method not implemented");
   }

   public PolicySet readPolicySet(String var1, String var2) throws NotFoundException {
      throw new AssertionError("Method not implemented");
   }

   public String readPolicySetAsString(String var1, String var2) throws NotFoundException {
      throw new AssertionError("Method not implemented");
   }

   public void addPolicy(Policy var1) throws CreateException, AlreadyExistsException {
      throw new AssertionError("Method not implemented");
   }

   public void addPolicy(String var1) throws CreateException, AlreadyExistsException {
      throw new AssertionError("Method not implemented");
   }

   public void addPolicy(Policy var1, String var2) throws CreateException, AlreadyExistsException {
      throw new AssertionError("Method not implemented");
   }

   public void addPolicy(String var1, String var2) throws CreateException, AlreadyExistsException {
      throw new AssertionError("Method not implemented");
   }

   public void addPolicySet(PolicySet var1) throws CreateException, AlreadyExistsException {
      throw new AssertionError("Method not implemented");
   }

   public void addPolicySet(String var1) throws CreateException, AlreadyExistsException {
      throw new AssertionError("Method not implemented");
   }

   public void addPolicySet(PolicySet var1, String var2) throws CreateException, AlreadyExistsException {
      throw new AssertionError("Method not implemented");
   }

   public void addPolicySet(String var1, String var2) throws CreateException, AlreadyExistsException {
      throw new AssertionError("Method not implemented");
   }

   public void modifyPolicy(Policy var1) throws CreateException, NotFoundException {
      throw new AssertionError("Method not implemented");
   }

   public void modifyPolicy(String var1) throws CreateException, NotFoundException {
      throw new AssertionError("Method not implemented");
   }

   public void modifyPolicy(Policy var1, String var2) throws CreateException, NotFoundException {
      throw new AssertionError("Method not implemented");
   }

   public void modifyPolicy(String var1, String var2) throws CreateException, NotFoundException {
      throw new AssertionError("Method not implemented");
   }

   public void modifyPolicySet(PolicySet var1) throws CreateException, NotFoundException {
      throw new AssertionError("Method not implemented");
   }

   public void modifyPolicySet(String var1) throws CreateException, NotFoundException {
      throw new AssertionError("Method not implemented");
   }

   public void modifyPolicySet(PolicySet var1, String var2) throws CreateException, NotFoundException {
      throw new AssertionError("Method not implemented");
   }

   public void modifyPolicySet(String var1, String var2) throws CreateException, NotFoundException {
      throw new AssertionError("Method not implemented");
   }

   public void modifyPolicyStatus(String var1, String var2, String var3) throws CreateException, NotFoundException {
      throw new AssertionError("Method not implemented");
   }

   public String getPolicyStatus(String var1, String var2) throws NotFoundException {
      throw new AssertionError("Method not implemented");
   }

   public void modifyPolicySetStatus(String var1, String var2, String var3) throws CreateException, NotFoundException {
      throw new AssertionError("Method not implemented");
   }

   public String getPolicySetStatus(String var1, String var2) throws NotFoundException {
      throw new AssertionError("Method not implemented");
   }

   public void deletePolicy(String var1, String var2) throws NotFoundException, RemoveException {
      throw new AssertionError("Method not implemented");
   }

   public void deletePolicySet(String var1, String var2) throws NotFoundException, RemoveException {
      throw new AssertionError("Method not implemented");
   }

   public Object _getKey() {
      return super._getKey();
   }

   public void _validate() throws IllegalArgumentException {
      super._validate();
   }

   protected void _unSet(int var1) {
      if (!this._initializeProperty(var1)) {
         super._unSet(var1);
      } else {
         this._markSet(var1, false);
      }

   }

   protected AbstractDescriptorBeanHelper _createHelper() {
      return new Helper(this);
   }

   public boolean _isAnythingSet() {
      return super._isAnythingSet();
   }

   private boolean _initializeProperty(int var1) {
      boolean var2 = var1 > -1;
      if (!var2) {
         var1 = -1;
      }

      try {
         switch (var1) {
            default:
               return !var2;
         }
      } catch (RuntimeException var4) {
         throw var4;
      } catch (Exception var5) {
         throw (Error)(new AssertionError("Impossible Exception")).initCause(var5);
      }
   }

   public Munger.SchemaHelper _getSchemaHelper() {
      return null;
   }

   public String _getElementName(int var1) {
      return this._getSchemaHelper2().getElementName(var1);
   }

   protected String getSchemaLocation() {
      return "http://xmlns.oracle.com/weblogic/1.0/security.xsd";
   }

   protected String getTargetNamespace() {
      return "http://xmlns.oracle.com/weblogic/security";
   }

   public SchemaHelper _getSchemaHelper2() {
      if (_schemaHelper == null) {
         _schemaHelper = new SchemaHelper2();
      }

      return _schemaHelper;
   }

   public String wls_getInterfaceClassName() {
      return "weblogic.management.security.authorization.PolicyStoreMBean";
   }

   public static class SchemaHelper2 extends AbstractSchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            default:
               return super.getPropertyIndex(var1);
         }
      }

      public SchemaHelper getSchemaHelper(int var1) {
         switch (var1) {
            default:
               return super.getSchemaHelper(var1);
         }
      }

      public String getElementName(int var1) {
         switch (var1) {
            default:
               return super.getElementName(var1);
         }
      }
   }

   protected static class Helper extends PropertiesListerMBeanImpl.Helper {
      private PolicyStoreMBeanImpl bean;

      protected Helper(PolicyStoreMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         return super.getPropertyIndex(var1);
      }

      public Iterator getChildren() {
         ArrayList var1 = new ArrayList();
         return new CombinedIterator(var1);
      }

      protected long computeHashValue(CRC32 var1) {
         try {
            StringBuffer var2 = new StringBuffer();
            long var3 = super.computeHashValue(var1);
            if (var3 != 0L) {
               var2.append(String.valueOf(var3));
            }

            long var5 = 0L;
            var1.update(var2.toString().getBytes());
            return var1.getValue();
         } catch (Exception var7) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var7);
         }
      }

      protected void computeDiff(AbstractDescriptorBean var1) {
         try {
            super.computeDiff(var1);
            PolicyStoreMBeanImpl var2 = (PolicyStoreMBeanImpl)var1;
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            PolicyStoreMBeanImpl var3 = (PolicyStoreMBeanImpl)var1.getSourceBean();
            PolicyStoreMBeanImpl var4 = (PolicyStoreMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               super.applyPropertyUpdate(var1, var2);
            }
         } catch (RuntimeException var7) {
            throw var7;
         } catch (Exception var8) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var8);
         }
      }

      protected AbstractDescriptorBean finishCopy(AbstractDescriptorBean var1, boolean var2, List var3) {
         try {
            PolicyStoreMBeanImpl var5 = (PolicyStoreMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            return var5;
         } catch (RuntimeException var6) {
            throw var6;
         } catch (Exception var7) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var7);
         }
      }

      protected void inferSubTree(Class var1, Object var2) {
         super.inferSubTree(var1, var2);
         Object var3 = null;
      }
   }
}
