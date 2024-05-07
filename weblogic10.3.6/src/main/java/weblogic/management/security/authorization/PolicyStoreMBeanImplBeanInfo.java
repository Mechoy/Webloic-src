package weblogic.management.security.authorization;

import com.bea.common.security.xacml.policy.Policy;
import com.bea.common.security.xacml.policy.PolicySet;
import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;
import weblogic.management.utils.PropertiesListerMBeanImplBeanInfo;

public class PolicyStoreMBeanImplBeanInfo extends PropertiesListerMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = PolicyStoreMBean.class;

   public PolicyStoreMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public PolicyStoreMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = PolicyStoreMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("abstract", Boolean.TRUE);
      var2.setValue("since", "9.2.0.0");
      var2.setValue("package", "weblogic.management.security.authorization");
      String var3 = (new String("Provides a set of methods for managing policies in a policy store. An Authorization-provider or Role Mapping-provider MBean can optionally implement this MBean interface.  Policies are expressed as XACML 2.0 Policy or PolicySet documents.  Authorization-providers should expect standard <code>Policy</code> or <code>PolicySet</code> documents as described in the XACML 2.0 Core Specification.  Role Mapping-providers should expect <code>Policy</code> or <code>PolicySet</code> documents consistent with role assignment policies described by the Role Based Access Control Profile, specifically the <code>Target</code> must contain:<p> <ol> <li>An ActionAttributeDesignator with the id, urn:oasis:names:tc:xacml:1.0:action:action-id, and the value, urn:oasis:names:tc:xacml:2.0:actions:enableRole, according to anyURI-equal</li> <li>A ResourceAttributeDesignator with the id, urn:oasis:names:tc:xacml:2.0:subject:role, and a value naming the role being assigned, according to string-equal</li> </ol>  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">In addition to being used as a base class that provides functionality to security provider MBeans, JMX applications can use this class directly as a type-safe interface. When used as a type-safe interface, a JMX application imports this class and accesses it through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, JMX applications that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. </p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.security.authorization.PolicyStoreMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      Object var2 = null;
      super.buildPropertyDescriptors(var1);
   }

   private void fillinFactoryMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
   }

   private void fillinCollectionMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
   }

   private void fillinFinderMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
   }

   private void fillinOperationMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      Method var3 = PolicyStoreMBean.class.getMethod("listAllPolicies");
      String var4 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      String[] var5;
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var5 = new String[]{BeanInfoHelper.encodeEntities("NotFoundException Thrown if no policies are found")};
         var2.setValue("throws", var5);
         var1.put(var4, var2);
         var2.setValue("description", "Returns cursor listing all policies ");
         var2.setValue("role", "operation");
      }

      var3 = PolicyStoreMBean.class.getMethod("listAllPoliciesAsString");
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var5 = new String[]{BeanInfoHelper.encodeEntities("NotFoundException Thrown if no policies are found")};
         var2.setValue("throws", var5);
         var1.put(var4, var2);
         var2.setValue("description", "Returns cursor listing all policies.  Policies are returned as java.lang.String. ");
         var2.setValue("role", "operation");
      }

      var3 = PolicyStoreMBean.class.getMethod("listAllPolicySets");
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var5 = new String[]{BeanInfoHelper.encodeEntities("NotFoundException Thrown if no policy sets are found")};
         var2.setValue("throws", var5);
         var1.put(var4, var2);
         var2.setValue("description", "Returns cursor listing all policy sets ");
         var2.setValue("role", "operation");
      }

      var3 = PolicyStoreMBean.class.getMethod("listAllPolicySetsAsString");
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var5 = new String[]{BeanInfoHelper.encodeEntities("NotFoundException Thrown if no policy sets are found")};
         var2.setValue("throws", var5);
         var1.put(var4, var2);
         var2.setValue("description", "Returns cursor listing all policy sets.  Policy sets are returned as java.lang.String. ");
         var2.setValue("role", "operation");
      }

      var3 = PolicyStoreMBean.class.getMethod("readPolicy", String.class, String.class);
      ParameterDescriptor[] var7 = new ParameterDescriptor[]{createParameterDescriptor("identifier", "Policy identifier "), createParameterDescriptor("version", "Policy version ")};
      String var8 = BeanInfoHelper.buildMethodKey(var3);
      String[] var6;
      if (!var1.containsKey(var8)) {
         var2 = new MethodDescriptor(var3, var7);
         var6 = new String[]{BeanInfoHelper.encodeEntities("NotFoundException Thrown if matching policy is not found")};
         var2.setValue("throws", var6);
         var1.put(var8, var2);
         var2.setValue("description", "Reads policy with specified identifier and version ");
         var2.setValue("role", "operation");
      }

      var3 = PolicyStoreMBean.class.getMethod("readPolicyAsString", String.class, String.class);
      var7 = new ParameterDescriptor[]{createParameterDescriptor("identifier", "Policy identifier "), createParameterDescriptor("version", "Policy version ")};
      var8 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var8)) {
         var2 = new MethodDescriptor(var3, var7);
         var6 = new String[]{BeanInfoHelper.encodeEntities("NotFoundException Thrown if matching policy is not found")};
         var2.setValue("throws", var6);
         var1.put(var8, var2);
         var2.setValue("description", "Reads policy with specified identifier and version ");
         var2.setValue("role", "operation");
      }

      var3 = PolicyStoreMBean.class.getMethod("readPolicySet", String.class, String.class);
      var7 = new ParameterDescriptor[]{createParameterDescriptor("identifier", "Policy set identifier "), createParameterDescriptor("version", "Policy set version ")};
      var8 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var8)) {
         var2 = new MethodDescriptor(var3, var7);
         var6 = new String[]{BeanInfoHelper.encodeEntities("NotFoundException Thrown if matching policy set is not found")};
         var2.setValue("throws", var6);
         var1.put(var8, var2);
         var2.setValue("description", "Reads policy set with specified identifier and version ");
         var2.setValue("role", "operation");
      }

      var3 = PolicyStoreMBean.class.getMethod("readPolicySetAsString", String.class, String.class);
      var7 = new ParameterDescriptor[]{createParameterDescriptor("identifier", "Policy set identifier "), createParameterDescriptor("version", "Policy set version ")};
      var8 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var8)) {
         var2 = new MethodDescriptor(var3, var7);
         var6 = new String[]{BeanInfoHelper.encodeEntities("NotFoundException Thrown if matching policy set is not found")};
         var2.setValue("throws", var6);
         var1.put(var8, var2);
         var2.setValue("description", "Reads policy set with specified identifier and version ");
         var2.setValue("role", "operation");
      }

      var3 = PolicyStoreMBean.class.getMethod("addPolicy", Policy.class);
      var7 = new ParameterDescriptor[]{createParameterDescriptor("policy", "Policy document ")};
      var8 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var8)) {
         var2 = new MethodDescriptor(var3, var7);
         var6 = new String[]{BeanInfoHelper.encodeEntities("CreateException Thrown if policy is invalid or store cannot be updated"), BeanInfoHelper.encodeEntities("AlreadyExistsException Thrown if matching policy id and version already present in store")};
         var2.setValue("throws", var6);
         var1.put(var8, var2);
         var2.setValue("description", "Adds policy.  Policy is set to ACTIVE status. ");
         var2.setValue("role", "operation");
      }

      var3 = PolicyStoreMBean.class.getMethod("addPolicy", String.class);
      var7 = new ParameterDescriptor[]{createParameterDescriptor("policy", "Policy document ")};
      var8 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var8)) {
         var2 = new MethodDescriptor(var3, var7);
         var6 = new String[]{BeanInfoHelper.encodeEntities("CreateException Thrown if policy is invalid or store cannot be updated"), BeanInfoHelper.encodeEntities("AlreadyExistsException Thrown if matching policy id and version already present in store")};
         var2.setValue("throws", var6);
         var1.put(var8, var2);
         var2.setValue("description", "Adds policy.  Policy is set to ACTIVE status. ");
         var2.setValue("role", "operation");
      }

      var3 = PolicyStoreMBean.class.getMethod("addPolicy", Policy.class, String.class);
      var7 = new ParameterDescriptor[]{createParameterDescriptor("policy", "Policy document "), createParameterDescriptor("status", "Policy status ")};
      var8 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var8)) {
         var2 = new MethodDescriptor(var3, var7);
         var6 = new String[]{BeanInfoHelper.encodeEntities("CreateException Thrown if policy is invalid or store cannot be updated"), BeanInfoHelper.encodeEntities("AlreadyExistsException Thrown if matching policy id and version already present in store")};
         var2.setValue("throws", var6);
         var1.put(var8, var2);
         var2.setValue("description", "Adds policy ");
         var2.setValue("role", "operation");
      }

      var3 = PolicyStoreMBean.class.getMethod("addPolicy", String.class, String.class);
      var7 = new ParameterDescriptor[]{createParameterDescriptor("policy", "Policy document "), createParameterDescriptor("status", "Policy status ")};
      var8 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var8)) {
         var2 = new MethodDescriptor(var3, var7);
         var6 = new String[]{BeanInfoHelper.encodeEntities("CreateException Thrown if policy is invalid or store cannot be updated"), BeanInfoHelper.encodeEntities("AlreadyExistsException Thrown if matching policy id and version already present in store")};
         var2.setValue("throws", var6);
         var1.put(var8, var2);
         var2.setValue("description", "Adds policy ");
         var2.setValue("role", "operation");
      }

      var3 = PolicyStoreMBean.class.getMethod("addPolicySet", PolicySet.class);
      var7 = new ParameterDescriptor[]{createParameterDescriptor("set", "Policy set document ")};
      var8 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var8)) {
         var2 = new MethodDescriptor(var3, var7);
         var6 = new String[]{BeanInfoHelper.encodeEntities("CreateException Thrown if policy set is invalid or store cannot be updated"), BeanInfoHelper.encodeEntities("AlreadyExistsException Thrown if matching policy set id and version already present in store")};
         var2.setValue("throws", var6);
         var1.put(var8, var2);
         var2.setValue("description", "Adds policy set.  Policy set is set to ACTIVE status. ");
         var2.setValue("role", "operation");
      }

      var3 = PolicyStoreMBean.class.getMethod("addPolicySet", String.class);
      var7 = new ParameterDescriptor[]{createParameterDescriptor("set", "Policy set document ")};
      var8 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var8)) {
         var2 = new MethodDescriptor(var3, var7);
         var6 = new String[]{BeanInfoHelper.encodeEntities("CreateException Thrown if policy set is invalid or store cannot be updated"), BeanInfoHelper.encodeEntities("AlreadyExistsException Thrown if matching policy set id and version already present in store")};
         var2.setValue("throws", var6);
         var1.put(var8, var2);
         var2.setValue("description", "Adds policy set.  Policy set is set to ACTIVE status. ");
         var2.setValue("role", "operation");
      }

      var3 = PolicyStoreMBean.class.getMethod("addPolicySet", PolicySet.class, String.class);
      var7 = new ParameterDescriptor[]{createParameterDescriptor("set", "Policy set document "), createParameterDescriptor("status", "Policy status enumeration ")};
      var8 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var8)) {
         var2 = new MethodDescriptor(var3, var7);
         var6 = new String[]{BeanInfoHelper.encodeEntities("CreateException Thrown if policy set is invalid or store cannot be updated"), BeanInfoHelper.encodeEntities("AlreadyExistsException Thrown if matching policy set id and version already present in store")};
         var2.setValue("throws", var6);
         var1.put(var8, var2);
         var2.setValue("description", "Adds policy set. ");
         var2.setValue("role", "operation");
      }

      var3 = PolicyStoreMBean.class.getMethod("addPolicySet", String.class, String.class);
      var7 = new ParameterDescriptor[]{createParameterDescriptor("set", "Policy set document "), createParameterDescriptor("status", "Policy status enumeration ")};
      var8 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var8)) {
         var2 = new MethodDescriptor(var3, var7);
         var6 = new String[]{BeanInfoHelper.encodeEntities("CreateException Thrown if policy set is invalid or store cannot be updated"), BeanInfoHelper.encodeEntities("AlreadyExistsException Thrown if matching policy set id and version already present in store")};
         var2.setValue("throws", var6);
         var1.put(var8, var2);
         var2.setValue("description", "Adds policy set. ");
         var2.setValue("role", "operation");
      }

      var3 = PolicyStoreMBean.class.getMethod("modifyPolicy", Policy.class);
      var7 = new ParameterDescriptor[]{createParameterDescriptor("policy", "Policy document ")};
      var8 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var8)) {
         var2 = new MethodDescriptor(var3, var7);
         var6 = new String[]{BeanInfoHelper.encodeEntities("CreateException Thrown if policy is invalid or store cannot be updated"), BeanInfoHelper.encodeEntities("NotFoundException Thrown if matching policy id and version not present in store")};
         var2.setValue("throws", var6);
         var1.put(var8, var2);
         var2.setValue("description", "Updates policy.  This operation does not change the status of policy. already present in the store. ");
         var2.setValue("role", "operation");
      }

      var3 = PolicyStoreMBean.class.getMethod("modifyPolicy", String.class);
      var7 = new ParameterDescriptor[]{createParameterDescriptor("policy", "Policy document ")};
      var8 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var8)) {
         var2 = new MethodDescriptor(var3, var7);
         var6 = new String[]{BeanInfoHelper.encodeEntities("CreateException Thrown if policy is invalid or store cannot be updated"), BeanInfoHelper.encodeEntities("NotFoundException Thrown if matching policy id and version not present in store")};
         var2.setValue("throws", var6);
         var1.put(var8, var2);
         var2.setValue("description", "Updates policy.  This operation does not change the status of policy. already present in the store. ");
         var2.setValue("role", "operation");
      }

      var3 = PolicyStoreMBean.class.getMethod("modifyPolicy", Policy.class, String.class);
      var7 = new ParameterDescriptor[]{createParameterDescriptor("policy", "Policy document "), createParameterDescriptor("status", "Policy status enumeration ")};
      var8 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var8)) {
         var2 = new MethodDescriptor(var3, var7);
         var6 = new String[]{BeanInfoHelper.encodeEntities("CreateException Thrown if policy is invalid or store cannot be updated"), BeanInfoHelper.encodeEntities("NotFoundException Thrown if matching policy id and version not present in store")};
         var2.setValue("throws", var6);
         var1.put(var8, var2);
         var2.setValue("description", "Updates policy and status. ");
         var2.setValue("role", "operation");
      }

      var3 = PolicyStoreMBean.class.getMethod("modifyPolicy", String.class, String.class);
      var7 = new ParameterDescriptor[]{createParameterDescriptor("policy", "Policy document "), createParameterDescriptor("status", "Policy status enumeration ")};
      var8 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var8)) {
         var2 = new MethodDescriptor(var3, var7);
         var6 = new String[]{BeanInfoHelper.encodeEntities("CreateException Thrown if policy is invalid or store cannot be updated"), BeanInfoHelper.encodeEntities("NotFoundException Thrown if matching policy id and version not present in store")};
         var2.setValue("throws", var6);
         var1.put(var8, var2);
         var2.setValue("description", "Updates policy and status. ");
         var2.setValue("role", "operation");
      }

      var3 = PolicyStoreMBean.class.getMethod("modifyPolicySet", PolicySet.class);
      var7 = new ParameterDescriptor[]{createParameterDescriptor("set", "Policy set document "), createParameterDescriptor("status", "Policy status enumeration ")};
      var8 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var8)) {
         var2 = new MethodDescriptor(var3, var7);
         var6 = new String[]{BeanInfoHelper.encodeEntities("CreateException Thrown if policy set is invalid or store cannot be updated"), BeanInfoHelper.encodeEntities("NotFoundException Thrown if matching policy set id and version not present in store")};
         var2.setValue("throws", var6);
         var1.put(var8, var2);
         var2.setValue("description", "Updates policy set.  This operation does not change the status of policy set. already present in the store. ");
         var2.setValue("role", "operation");
      }

      var3 = PolicyStoreMBean.class.getMethod("modifyPolicySet", String.class);
      var7 = new ParameterDescriptor[]{createParameterDescriptor("set", "Policy set document "), createParameterDescriptor("status", "Policy status enumeration ")};
      var8 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var8)) {
         var2 = new MethodDescriptor(var3, var7);
         var6 = new String[]{BeanInfoHelper.encodeEntities("CreateException Thrown if policy set is invalid or store cannot be updated"), BeanInfoHelper.encodeEntities("NotFoundException Thrown if matching policy set id and version not present in store")};
         var2.setValue("throws", var6);
         var1.put(var8, var2);
         var2.setValue("description", "Updates policy set.  This operation does not change the status of policy set. already present in the store. ");
         var2.setValue("role", "operation");
      }

      var3 = PolicyStoreMBean.class.getMethod("modifyPolicySet", PolicySet.class, String.class);
      var7 = new ParameterDescriptor[]{createParameterDescriptor("set", "Policy set document "), createParameterDescriptor("status", "Policy status enumeration ")};
      var8 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var8)) {
         var2 = new MethodDescriptor(var3, var7);
         var6 = new String[]{BeanInfoHelper.encodeEntities("CreateException Thrown if policy set is invalid or store cannot be updated"), BeanInfoHelper.encodeEntities("NotFoundException Thrown if matching policy set id and version not present in store")};
         var2.setValue("throws", var6);
         var1.put(var8, var2);
         var2.setValue("description", "Updates policy set and status. ");
         var2.setValue("role", "operation");
      }

      var3 = PolicyStoreMBean.class.getMethod("modifyPolicySet", String.class, String.class);
      var7 = new ParameterDescriptor[]{createParameterDescriptor("set", "Policy set document "), createParameterDescriptor("status", "Policy status enumeration ")};
      var8 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var8)) {
         var2 = new MethodDescriptor(var3, var7);
         var6 = new String[]{BeanInfoHelper.encodeEntities("CreateException Thrown if policy set is invalid or store cannot be updated"), BeanInfoHelper.encodeEntities("NotFoundException Thrown if matching policy set id and version not present in store")};
         var2.setValue("throws", var6);
         var1.put(var8, var2);
         var2.setValue("description", "Updates policy set and status. ");
         var2.setValue("role", "operation");
      }

      var3 = PolicyStoreMBean.class.getMethod("modifyPolicyStatus", String.class, String.class, String.class);
      var7 = new ParameterDescriptor[]{createParameterDescriptor("identifier", "Policy identifier "), createParameterDescriptor("version", "Policy version "), createParameterDescriptor("status", "Policy status enumeration ")};
      var8 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var8)) {
         var2 = new MethodDescriptor(var3, var7);
         var6 = new String[]{BeanInfoHelper.encodeEntities("CreateException Thrown if store cannot be updated"), BeanInfoHelper.encodeEntities("NotFoundException Thrown if matching policy id and version not present in store")};
         var2.setValue("throws", var6);
         var1.put(var8, var2);
         var2.setValue("description", "Sets status for policy identified by identifier and version ");
         var2.setValue("role", "operation");
      }

      var3 = PolicyStoreMBean.class.getMethod("getPolicyStatus", String.class, String.class);
      var7 = new ParameterDescriptor[]{createParameterDescriptor("identifier", "Policy identifier "), createParameterDescriptor("version", "Policy version ")};
      var8 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var8)) {
         var2 = new MethodDescriptor(var3, var7);
         var6 = new String[]{BeanInfoHelper.encodeEntities("NotFoundExeption Thrown if matching policy id and version not present in store")};
         var2.setValue("throws", var6);
         var1.put(var8, var2);
         var2.setValue("description", "Returns status for policy identified by identifier and version ");
         var2.setValue("role", "operation");
      }

      var3 = PolicyStoreMBean.class.getMethod("modifyPolicySetStatus", String.class, String.class, String.class);
      var7 = new ParameterDescriptor[]{createParameterDescriptor("identifier", "Policy set identifier "), createParameterDescriptor("version", "Policy set version "), createParameterDescriptor("status", "Policy status enumeration ")};
      var8 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var8)) {
         var2 = new MethodDescriptor(var3, var7);
         var6 = new String[]{BeanInfoHelper.encodeEntities("CreateException Thrown if store cannot be updated"), BeanInfoHelper.encodeEntities("NotFoundException Thrown if matching policy set id and version not present in store")};
         var2.setValue("throws", var6);
         var1.put(var8, var2);
         var2.setValue("description", "Sets status for policy set identified by identifier and version ");
         var2.setValue("role", "operation");
      }

      var3 = PolicyStoreMBean.class.getMethod("getPolicySetStatus", String.class, String.class);
      var7 = new ParameterDescriptor[]{createParameterDescriptor("identifier", "Policy set identifier "), createParameterDescriptor("version", "Policy set version ")};
      var8 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var8)) {
         var2 = new MethodDescriptor(var3, var7);
         var6 = new String[]{BeanInfoHelper.encodeEntities("NotFoundExeption Thrown if matching policy set id and version not present in store")};
         var2.setValue("throws", var6);
         var1.put(var8, var2);
         var2.setValue("description", "Returns status for policy set identified by identifier and version ");
         var2.setValue("role", "operation");
      }

      var3 = PolicyStoreMBean.class.getMethod("deletePolicy", String.class, String.class);
      var7 = new ParameterDescriptor[]{createParameterDescriptor("identifier", "Policy identifier "), createParameterDescriptor("version", "Policy version ")};
      var8 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var8)) {
         var2 = new MethodDescriptor(var3, var7);
         var6 = new String[]{BeanInfoHelper.encodeEntities("NotFoundException Thrown if matching policy id and version not present in store"), BeanInfoHelper.encodeEntities("RemoveException Thrown if store cannot be updated")};
         var2.setValue("throws", var6);
         var1.put(var8, var2);
         var2.setValue("description", "Deletes policy with given identifier and version. ");
         var2.setValue("role", "operation");
      }

      var3 = PolicyStoreMBean.class.getMethod("deletePolicySet", String.class, String.class);
      var7 = new ParameterDescriptor[]{createParameterDescriptor("identifier", "Policy set identifier "), createParameterDescriptor("version", "Policy set version ")};
      var8 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var8)) {
         var2 = new MethodDescriptor(var3, var7);
         var6 = new String[]{BeanInfoHelper.encodeEntities("NotFoundException Thrown if matching policy set id and version not present in store"), BeanInfoHelper.encodeEntities("RemoveException Thrown if store cannot be updated")};
         var2.setValue("throws", var6);
         var1.put(var8, var2);
         var2.setValue("description", "Deletes policy set with given identifier and version. ");
         var2.setValue("role", "operation");
      }

   }

   protected void buildMethodDescriptors(Map var1) throws IntrospectionException, NoSuchMethodException {
      this.fillinFinderMethodInfos(var1);
      if (!this.readOnly) {
         this.fillinCollectionMethodInfos(var1);
         this.fillinFactoryMethodInfos(var1);
      }

      this.fillinOperationMethodInfos(var1);
      super.buildMethodDescriptors(var1);
   }

   protected void buildEventSetDescriptors(Map var1) throws IntrospectionException {
   }
}
