package weblogic.entitlement.data.ldap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import netscape.ldap.LDAPAttribute;
import netscape.ldap.LDAPAttributeSet;
import netscape.ldap.LDAPConnection;
import netscape.ldap.LDAPEntry;
import netscape.ldap.LDAPException;
import netscape.ldap.LDAPModification;
import netscape.ldap.LDAPSearchResults;
import weblogic.entitlement.data.EPolicyCollectionInfo;
import weblogic.entitlement.data.EResource;
import weblogic.entitlement.data.ERole;
import weblogic.entitlement.data.ERoleCollectionInfo;
import weblogic.entitlement.data.ERoleId;
import weblogic.entitlement.data.EnConflictException;
import weblogic.entitlement.data.EnCreateException;
import weblogic.entitlement.data.EnCursorResourceFilter;
import weblogic.entitlement.data.EnCursorRoleFilter;
import weblogic.entitlement.data.EnData;
import weblogic.entitlement.data.EnDataChangeListener;
import weblogic.entitlement.data.EnDuplicateKeyException;
import weblogic.entitlement.data.EnFinderException;
import weblogic.entitlement.data.EnRemoveException;
import weblogic.entitlement.data.EnResourceCursor;
import weblogic.entitlement.data.EnRoleCursor;
import weblogic.entitlement.data.EnStorageException;
import weblogic.entitlement.expression.EAuxiliary;
import weblogic.entitlement.expression.EExprRep;
import weblogic.entitlement.expression.EExpression;
import weblogic.entitlement.util.Escaping;
import weblogic.entitlement.util.TextFilter;
import weblogic.ldap.EmbeddedLDAP;
import weblogic.ldap.EmbeddedLDAPChange;
import weblogic.ldap.EmbeddedLDAPChangeListener;
import weblogic.security.utils.ProviderUtils;

public class EData extends EnLDAP implements EnData {
   private String roleBaseDN = null;
   private String resourceBaseDN = null;
   private String predicateBaseDN = null;
   private String policyCollectionBaseDN = null;
   private String roleCollectionBaseDN = null;
   private static final String eexprAttribute = "EExpr";
   private static final String auxAttribute = "EAux";
   private static final String[] ROLE_OBJ_CLASSES = new String[]{"top", "ERole"};
   private static final String[] RESOURCE_OBJ_CLASSES = new String[]{"top", "EResource"};
   private static final String[] PREDICATE_OBJ_CLASSES = new String[]{"top", "EPredicate"};
   private static final String[] POLICY_COL_OBJ_CLASSES = new String[]{"top", "wlsPolicyCollectionInfo"};
   private static final String[] ROLE_COL_OBJ_CLASSES = new String[]{"top", "wlsRoleCollectionInfo"};
   private static final String roleBaseName = "ERole";
   private static final String resourceBaseName = "EResource";
   private static final String predicateBaseName = "EPredicate";
   private static final String policyCollectionBaseName = "EPolicyCollectionInfo";
   private static final String roleCollectionBaseName = "ERoleCollectionInfo";
   private static final String WLSCREATORINFO = "wlsCreatorInfo";
   private static final String wlsCollectionName = "wlsCollectionName";
   private static final String wlsCollectionVersion = "wlsCollectionVersion";
   private static final String wlsCollectionTimestamp = "wlsCollectionTimestamp";
   private static final String[] eexprAttrList = new String[]{"EExpr", "EAux", "wlsCreatorInfo", "wlsCollectionName"};
   private static final String[] conflictAttrList = new String[]{"wlsCreatorInfo", "wlsCollectionName"};
   private static final String[] collectionAttrList = new String[]{"wlsCollectionName", "wlsCollectionVersion", "wlsCollectionTimestamp"};
   private static final char[] SPECIAL_CHARS = new char[]{'@', '|', '&', '!', '=', '<', '>', '~', '(', ')', '*', ':', ',', ';', ' ', '"', '\'', '\t', '\\', '+', '/'};
   public static final Escaping escaper;

   public EData(Properties var1) {
      super(var1);
      if (traceLogger != null && traceLogger.isDebugEnabled()) {
         traceLogger.debug("Initializing EData.");
      }

      this.roleBaseDN = "ou=ERole," + this.realmDN;
      this.resourceBaseDN = "ou=EResource," + this.realmDN;
      this.predicateBaseDN = "ou=EPredicate," + this.realmDN;
      this.policyCollectionBaseDN = "ou=EPolicyCollectionInfo," + this.realmDN;
      this.roleCollectionBaseDN = "ou=ERoleCollectionInfo," + this.realmDN;
      LDAPConnection var2 = null;

      try {
         var2 = getConnection();
         this.createHierachy(var2);
      } catch (LDAPException var7) {
         var2 = null;
         throw new EnStorageException(var7.getMessage());
      } finally {
         if (var2 != null) {
            releaseConnection(var2);
         }

      }

   }

   private void createHierachy(LDAPConnection var1) throws LDAPException {
      addStructuralEntry(var1, this.roleBaseDN, false, "ERole");
      addStructuralEntry(var1, this.resourceBaseDN, false, "EResource");
      addStructuralEntry(var1, this.predicateBaseDN, false, "EPredicate");
      addStructuralEntry(var1, this.policyCollectionBaseDN, false, "EPolicyCollectionInfo");
      addStructuralEntry(var1, this.roleCollectionBaseDN, false, "ERoleCollectionInfo");
   }

   private static void checkDuplicateException(LDAPException var0) throws EnDuplicateKeyException {
      checkStorageException(var0);
      if (var0.getLDAPResultCode() == 68) {
         throw new EnDuplicateKeyException(var0.toString());
      }
   }

   private static void checkFinderException(LDAPException var0) throws EnFinderException {
      checkStorageException(var0);
      if (var0.getLDAPResultCode() == 32) {
         throw new EnFinderException(var0.toString());
      }
   }

   public Collection fetchRoleIds(String var1, TextFilter var2) {
      String var3 = var2 == null ? "*" : var2.toString(escaper, "*");
      var1 = escaper.escapeString(var1);
      if (traceLogger != null && traceLogger.isDebugEnabled()) {
         traceLogger.debug("fetchRoleIds: ");
      }

      return this.fetchERoleIds(PK2Name(var1, var3), this.roleBaseDN, "cn");
   }

   private Collection fetchERoleIds(String var1, String var2, String var3) {
      ArrayList var4 = new ArrayList();
      LDAPConnection var5 = null;

      try {
         if (traceLogger != null && traceLogger.isDebugEnabled()) {
            traceLogger.debug(var1);
         }

         var5 = getConnection();
         LDAPSearchResults var6 = var5.search(var2, 1, var3 + "=" + var1, noAttrs, false);

         while(var6.hasMoreElements()) {
            LDAPEntry var7 = var6.next();
            String var8 = getEntryName(var7);
            ERoleId var9 = name2PK(var8);
            var4.add(var9);
         }
      } catch (LDAPException var13) {
         checkStorageException(var13);
      } finally {
         if (var5 != null) {
            releaseConnection(var5);
         }

      }

      return var4;
   }

   public Collection fetchResourceRoleIds(TextFilter var1) {
      String var2 = var1 == null ? "*" : var1.toString(escaper, "*");
      if (traceLogger != null && traceLogger.isDebugEnabled()) {
         traceLogger.debug("fetchResourceRoleIds: ");
      }

      return this.fetchERoleIds(PK2Name(var2, "*"), this.roleBaseDN, "cn");
   }

   public Collection fetchGlobalRoles() {
      return this.fetchRoles("", (TextFilter)null);
   }

   public Collection fetchRoles(String var1) {
      return this.fetchRoles(var1, (TextFilter)null);
   }

   public Collection fetchRoles(String var1, TextFilter var2) {
      ArrayList var3 = new ArrayList();
      LDAPConnection var4 = null;

      try {
         var1 = escaper.escapeString(var1);
         String var5 = var2 == null ? "*" : var2.toString(escaper, "*");
         if (traceLogger != null && traceLogger.isDebugEnabled()) {
            traceLogger.debug("fetchRoles(" + var1 + "," + var5 + ") ==> ");
         }

         var4 = getConnection();
         LDAPSearchResults var6 = var4.search(this.roleBaseDN, 1, "cn=" + PK2Name(var1, var5), eexprAttrList, false);

         while(var6.hasMoreElements()) {
            var3.add(this.getRoleFromEntry(var6.next()));
         }
      } catch (LDAPException var10) {
         checkStorageException(var10);
      } finally {
         if (var4 != null) {
            releaseConnection(var4);
         }

      }

      return var3;
   }

   private Collection fetchNames(TextFilter var1, String var2, String var3) {
      ArrayList var4 = new ArrayList();
      LDAPConnection var5 = null;

      try {
         String var6 = var1 == null ? "*" : var1.toString(escaper, "*");
         if (traceLogger != null && traceLogger.isDebugEnabled()) {
            traceLogger.debug(var6);
         }

         var5 = getConnection();
         LDAPSearchResults var7 = var5.search(var2, 1, var3 + "=" + var6, noAttrs, false);

         while(var7.hasMoreElements()) {
            LDAPEntry var8 = var7.next();
            String var9 = getEntryName(var8);
            var9 = escaper.unescapeString(var9);
            var4.add(var9);
         }
      } catch (LDAPException var13) {
         checkStorageException(var13);
      } finally {
         if (var5 != null) {
            releaseConnection(var5);
         }

      }

      return var4;
   }

   public Collection fetchResourceNames(TextFilter var1) {
      if (traceLogger != null && traceLogger.isDebugEnabled()) {
         traceLogger.debug("fetchResourceNames: ");
      }

      return this.fetchNames(var1, this.resourceBaseDN, "cn");
   }

   public Collection fetchResources(TextFilter var1) {
      ArrayList var2 = new ArrayList();
      LDAPConnection var3 = null;

      try {
         String var4 = var1 == null ? "*" : var1.toString(escaper, "*");
         if (traceLogger != null && traceLogger.isDebugEnabled()) {
            traceLogger.debug("fetchResources(" + var4 + ")");
         }

         var3 = getConnection();
         LDAPSearchResults var5 = var3.search(this.resourceBaseDN, 1, "cn=" + var4, eexprAttrList, false);

         while(var5.hasMoreElements()) {
            var2.add(this.getResourceFromEntry(var5.next()));
         }
      } catch (LDAPException var9) {
         checkStorageException(var9);
      } finally {
         if (var3 != null) {
            releaseConnection(var3);
         }

      }

      return var2;
   }

   public ERole[] fetchRoles(ERoleId[] var1, boolean var2) throws EnFinderException {
      if (traceLogger != null && traceLogger.isDebugEnabled()) {
         traceLogger.debug("fetch roles");
      }

      ERole[] var3 = new ERole[var1.length];
      LDAPConnection var4 = null;

      try {
         String[] var5 = getRoleNames(var1);
         var4 = getConnection();
         LDAPSearchResults var6 = var4.search(this.roleBaseDN, 1, makeNameFilter(var5), eexprAttrList, false);
         HashMap var7 = new HashMap(var1.length);

         while(var6.hasMoreElements()) {
            ERole var8 = this.getRoleFromEntry(var6.next());
            var7.put(var8.getPrimaryKey(), var8);
         }

         for(int var14 = 0; var14 < var1.length; ++var14) {
            var3[var14] = (ERole)var7.get(var1[var14]);
            if (traceLogger != null && traceLogger.isDebugEnabled()) {
               traceLogger.debug("role[" + var14 + "]=" + var1[var14] + (var3[var14] == null ? "Not Found" : var3[var14].getEntitlement()));
            }

            if (var3[var14] == null && !var2) {
               throw new EnFinderException("Role '" + var1[var14].getRoleName() + "' not found.");
            }
         }
      } catch (LDAPException var12) {
         checkStorageException(var12);
      } finally {
         if (var4 != null) {
            releaseConnection(var4);
         }

      }

      return var3;
   }

   public EResource[] fetchResources(String[] var1, boolean var2) throws EnFinderException {
      if (traceLogger != null && traceLogger.isDebugEnabled()) {
         traceLogger.debug("fetch resources");
      }

      EResource[] var3 = new EResource[var1.length];
      LDAPConnection var4 = null;

      try {
         String[] var5 = new String[var1.length];

         for(int var6 = 0; var6 < var5.length; ++var6) {
            var5[var6] = escaper.escapeString(var1[var6]);
         }

         var4 = getConnection();
         LDAPSearchResults var14 = var4.search(this.resourceBaseDN, 1, makeNameFilter(var5), eexprAttrList, false);
         HashMap var7 = new HashMap(var1.length);

         while(var14.hasMoreElements()) {
            EResource var8 = this.getResourceFromEntry(var14.next());
            var7.put(var8.getName(), var8);
         }

         for(int var15 = 0; var15 < var1.length; ++var15) {
            var3[var15] = (EResource)var7.get(var1[var15]);
            if (traceLogger != null && traceLogger.isDebugEnabled()) {
               traceLogger.debug("resource[" + var15 + "]=" + var1[var15] + " : " + (var3[var15] == null ? "Not Found" : var3[var15].getEntitlement()));
            }

            if (var3[var15] == null && !var2) {
               throw new EnFinderException("Resource '" + var1[var15] + "' not found.");
            }
         }
      } catch (LDAPException var12) {
         checkStorageException(var12);
      } finally {
         if (var4 != null) {
            releaseConnection(var4);
         }

      }

      return var3;
   }

   public void update(ERole[] var1, boolean var2) throws EnFinderException {
      if (traceLogger != null && traceLogger.isDebugEnabled()) {
         traceLogger.debug("update roles");
      }

      if (var1.length != 0) {
         LDAPConnection var3 = null;

         try {
            String[] var4 = getRoleNames(var1);
            var3 = getConnection();
            LDAPSearchResults var5 = var3.search(this.roleBaseDN, 1, makeNameFilter(var4), noAttrs, true);
            if (countEntries(var5) != var1.length) {
               throw new EnFinderException("Attempt to modify unknown role");
            }

            for(int var6 = 0; var6 < var1.length; ++var6) {
               EExpression var7 = var1[var6].getExpression();
               LDAPAttribute var8 = new LDAPAttribute("EExpr", var7 == null ? null : var7.serialize());
               LDAPModification var9 = new LDAPModification(2, var8);
               LDAPAttribute var10 = new LDAPAttribute("wlsCreatorInfo", var2 ? "deploy" : "mbean");
               LDAPModification var11 = new LDAPModification(2, var10);
               String var13 = var1[var6].getCollectionName();
               LDAPModification[] var12;
               if (var13 != null) {
                  LDAPAttribute var14 = new LDAPAttribute("wlsCollectionName", escaper.escapeString(var13));
                  LDAPModification var15 = new LDAPModification(2, var14);
                  var12 = new LDAPModification[]{var9, var11, var15};
               } else {
                  var12 = new LDAPModification[]{var9, var11};
               }

               var3.modify("cn=" + var4[var6] + "," + this.roleBaseDN, var12);
               if (traceLogger != null && traceLogger.isDebugEnabled()) {
                  traceLogger.debug("role[" + var6 + "]=" + var1[var6].getPrimaryKey() + " : " + var1[var6].getEntitlement());
               }
            }
         } catch (LDAPException var19) {
            checkStorageException(var19);
         } finally {
            if (var3 != null) {
               releaseConnection(var3);
            }

         }

      }
   }

   public void updateAuxiliary(ERole[] var1, boolean var2) throws EnFinderException {
      if (traceLogger != null && traceLogger.isDebugEnabled()) {
         traceLogger.debug("update roles auxiliary");
      }

      if (var1.length != 0) {
         LDAPConnection var3 = null;

         try {
            String[] var4 = getRoleNames(var1);
            var3 = getConnection();
            LDAPSearchResults var5 = var3.search(this.roleBaseDN, 1, makeNameFilter(var4), noAttrs, true);
            if (countEntries(var5) != var1.length) {
               throw new EnFinderException("Attempt to modify unknown role");
            }

            for(int var6 = 0; var6 < var1.length; ++var6) {
               EAuxiliary var7 = var1[var6].getAuxiliary();
               LDAPAttribute var8 = new LDAPAttribute("EAux", var7 == null ? null : var7.toString());
               LDAPModification var9 = new LDAPModification(2, var8);
               LDAPAttribute var10 = new LDAPAttribute("wlsCreatorInfo", var2 ? "deploy" : "mbean");
               LDAPModification var11 = new LDAPModification(2, var10);
               LDAPModification[] var12 = new LDAPModification[]{var9, var11};
               var3.modify("cn=" + var4[var6] + "," + this.roleBaseDN, var12);
               if (traceLogger != null && traceLogger.isDebugEnabled()) {
                  traceLogger.debug("role[" + var6 + "]=" + var1[var6].getPrimaryKey() + " : " + var1[var6].getAuxiliary());
               }
            }
         } catch (LDAPException var16) {
            checkStorageException(var16);
         } finally {
            if (var3 != null) {
               releaseConnection(var3);
            }

         }

      }
   }

   public void update(EResource[] var1, boolean var2) throws EnFinderException {
      if (traceLogger != null && traceLogger.isDebugEnabled()) {
         traceLogger.debug("update resources");
      }

      if (var1.length != 0) {
         LDAPConnection var3 = null;

         try {
            String[] var4 = getResourceNames(var1);
            var3 = getConnection();
            LDAPSearchResults var5 = var3.search(this.resourceBaseDN, 1, makeNameFilter(var4), noAttrs, true);
            if (countEntries(var5) != var1.length) {
               throw new EnFinderException("Attempt to modify unknown resource.");
            }

            for(int var6 = 0; var6 < var1.length; ++var6) {
               EExpression var7 = var1[var6].getExpression();
               LDAPAttribute var8 = new LDAPAttribute("EExpr", var7 == null ? null : var7.serialize());
               LDAPModification var9 = new LDAPModification(2, var8);
               LDAPAttribute var10 = new LDAPAttribute("wlsCreatorInfo", var2 ? "deploy" : "mbean");
               LDAPModification var11 = new LDAPModification(2, var10);
               String var13 = var1[var6].getCollectionName();
               LDAPModification[] var12;
               if (var13 != null) {
                  LDAPAttribute var14 = new LDAPAttribute("wlsCollectionName", escaper.escapeString(var13));
                  LDAPModification var15 = new LDAPModification(2, var14);
                  var12 = new LDAPModification[]{var9, var11, var15};
               } else {
                  var12 = new LDAPModification[]{var9, var11};
               }

               var3.modify("cn=" + var4[var6] + "," + this.resourceBaseDN, var12);
               if (traceLogger != null && traceLogger.isDebugEnabled()) {
                  traceLogger.debug("resource[" + var6 + "]=" + var1[var6].getName() + " : " + var1[var6].getEntitlement());
               }
            }
         } catch (LDAPException var19) {
            checkStorageException(var19);
         } finally {
            if (var3 != null) {
               releaseConnection(var3);
            }

         }

      }
   }

   public void create(ERole[] var1, boolean var2) throws EnDuplicateKeyException, EnCreateException {
      if (traceLogger != null && traceLogger.isDebugEnabled()) {
         traceLogger.debug("create roles");
      }

      LDAPConnection var3 = null;

      try {
         String[] var4 = getRoleNames(var1);
         var3 = getConnection();
         LDAPSearchResults var5 = var3.search(this.roleBaseDN, 1, makeNameFilter(var4), noAttrs, false);
         if (var5.hasMoreElements()) {
            LDAPEntry var19 = var5.next();
            String var20 = getEntryName(var19);
            String var21 = escaper.unescapeString(var20.substring(var20.indexOf("::") + "::".length()));
            throw new EnDuplicateKeyException("Role policy definition for '" + var21 + "' already exist.");
         }

         for(int var6 = 0; var6 < var1.length; ++var6) {
            LDAPAttributeSet var7 = new LDAPAttributeSet();
            var7.add(new LDAPAttribute("objectclass", ROLE_OBJ_CLASSES));
            var7.add(new LDAPAttribute("cn", var4[var6]));
            EExpression var8 = var1[var6].getExpression();
            if (var8 != null) {
               var7.add(new LDAPAttribute("EExpr", var8.serialize()));
            }

            EAuxiliary var9 = var1[var6].getAuxiliary();
            if (var9 != null) {
               var7.add(new LDAPAttribute("EAux", var9.toString()));
            }

            String var10 = "cn=" + var4[var6] + "," + this.roleBaseDN;
            LDAPAttribute var11 = new LDAPAttribute("wlsCreatorInfo", var2 ? "deploy" : "mbean");
            var7.add(var11);
            String var12 = var1[var6].getCollectionName();
            if (var12 != null) {
               var7.add(new LDAPAttribute("wlsCollectionName", escaper.escapeString(var12)));
            }

            LDAPEntry var13 = new LDAPEntry(var10, var7);
            var3.add(var13);
         }
      } catch (LDAPException var17) {
         checkDuplicateException(var17);
      } finally {
         if (var3 != null) {
            releaseConnection(var3);
         }

      }

   }

   public void createForCollection(ERole[] var1) throws EnConflictException, EnDuplicateKeyException, EnCreateException {
      if (traceLogger != null && traceLogger.isDebugEnabled()) {
         traceLogger.debug("create roles for collection");
      }

      LDAPConnection var2 = null;

      try {
         String[] var3 = getRoleNames(var1);
         var2 = getConnection();
         LDAPSearchResults var4 = var2.search(this.roleBaseDN, 1, makeNameFilter(var3), conflictAttrList, false);
         String var9;
         if (var4.hasMoreElements()) {
            LDAPEntry var19 = var4.next();
            String var20 = getEntryName(var19);
            boolean var21 = getEntryDeployData(var19);
            String var22 = "Entitlement role definition for '" + escaper.unescapeString(var20) + "' ";
            if (var21) {
               throw new EnDuplicateKeyException(var22 + " already exist.");
            }

            if (traceLogger != null && traceLogger.isDebugEnabled()) {
               var9 = getEntryAttribute(var19, "wlsCollectionName");
               traceLogger.debug("conflict: " + var9 + " -- " + var20);
            }

            throw new EnConflictException(var22 + " is customized.");
         }

         for(int var5 = 0; var5 < var1.length; ++var5) {
            LDAPAttributeSet var6 = new LDAPAttributeSet();
            var6.add(new LDAPAttribute("objectclass", ROLE_OBJ_CLASSES));
            var6.add(new LDAPAttribute("cn", var3[var5]));
            EExpression var7 = var1[var5].getExpression();
            if (var7 != null) {
               var6.add(new LDAPAttribute("EExpr", var7.serialize()));
            }

            EAuxiliary var8 = var1[var5].getAuxiliary();
            if (var8 != null) {
               var6.add(new LDAPAttribute("EAux", var8.toString()));
            }

            var9 = "cn=" + var3[var5] + "," + this.roleBaseDN;
            boolean var10 = var1[var5].isDeployData();
            LDAPAttribute var11 = new LDAPAttribute("wlsCreatorInfo", var10 ? "deploy" : "mbean");
            var6.add(var11);
            String var12 = var1[var5].getCollectionName();
            if (var12 != null) {
               var6.add(new LDAPAttribute("wlsCollectionName", escaper.escapeString(var12)));
            }

            LDAPEntry var13 = new LDAPEntry(var9, var6);
            var2.add(var13);
         }
      } catch (LDAPException var17) {
         checkDuplicateException(var17);
      } finally {
         if (var2 != null) {
            releaseConnection(var2);
         }

      }

   }

   public void create(EResource[] var1, boolean var2) throws EnDuplicateKeyException, EnCreateException {
      if (traceLogger != null && traceLogger.isDebugEnabled()) {
         traceLogger.debug("create resources");
      }

      LDAPConnection var3 = null;

      try {
         String[] var4 = getResourceNames(var1);
         var3 = getConnection();
         LDAPSearchResults var5 = var3.search(this.resourceBaseDN, 1, makeNameFilter(var4), noAttrs, false);
         if (var5.hasMoreElements()) {
            LDAPEntry var18 = var5.next();
            String var19 = getEntryName(var18);
            throw new EnDuplicateKeyException("Entitlement policy definition for '" + escaper.unescapeString(var19) + "' already exist.");
         }

         for(int var6 = 0; var6 < var1.length; ++var6) {
            LDAPAttributeSet var7 = new LDAPAttributeSet();
            var7.add(new LDAPAttribute("objectclass", RESOURCE_OBJ_CLASSES));
            var7.add(new LDAPAttribute("cn", var4[var6]));
            EExpression var8 = var1[var6].getExpression();
            if (var8 != null) {
               var7.add(new LDAPAttribute("EExpr", var8.serialize()));
            }

            String var9 = "cn=" + var4[var6] + "," + this.resourceBaseDN;
            LDAPAttribute var10 = new LDAPAttribute("wlsCreatorInfo", var2 ? "deploy" : "mbean");
            var7.add(var10);
            String var11 = var1[var6].getCollectionName();
            if (var11 != null) {
               var7.add(new LDAPAttribute("wlsCollectionName", escaper.escapeString(var11)));
            }

            LDAPEntry var12 = new LDAPEntry(var9, var7);
            var3.add(var12);
         }
      } catch (LDAPException var16) {
         checkDuplicateException(var16);
      } finally {
         if (var3 != null) {
            releaseConnection(var3);
         }

      }

   }

   public void removeRoles(ERoleId[] var1) throws EnFinderException {
      if (traceLogger != null && traceLogger.isDebugEnabled()) {
         traceLogger.debug("remove roles");
      }

      LDAPConnection var2 = null;

      try {
         String[] var3 = getRoleNames(var1);
         var2 = getConnection();
         LDAPSearchResults var4 = var2.search(this.roleBaseDN, 1, makeNameFilter(var3), noAttrs, true);
         if (countEntries(var4) != var1.length) {
            throw new EnFinderException("Attempt to remove unknown role");
         }

         for(int var5 = 0; var5 < var1.length; ++var5) {
            if (traceLogger != null && traceLogger.isDebugEnabled()) {
               traceLogger.debug("role[" + var5 + "]=" + var1[var5]);
            }

            String var6 = "cn=" + var3[var5] + "," + this.roleBaseDN;
            var2.delete(var6);
         }
      } catch (LDAPException var10) {
         checkStorageException(var10);
      } finally {
         if (var2 != null) {
            releaseConnection(var2);
         }

      }

   }

   public void removeResources(String[] var1) throws EnFinderException {
      if (traceLogger != null && traceLogger.isDebugEnabled()) {
         traceLogger.debug("remove resources");
      }

      LDAPConnection var2 = null;

      try {
         String[] var3 = new String[var1.length];

         for(int var4 = 0; var4 < var1.length; ++var4) {
            var3[var4] = escaper.escapeString(var1[var4]);
         }

         var2 = getConnection();
         LDAPSearchResults var12 = var2.search(this.resourceBaseDN, 1, makeNameFilter(var3), noAttrs, true);
         if (countEntries(var12) != var1.length) {
            throw new EnFinderException("Attempt to remove unknown resource.");
         }

         for(int var5 = 0; var5 < var1.length; ++var5) {
            if (traceLogger != null && traceLogger.isDebugEnabled()) {
               traceLogger.debug("resource[" + var5 + "]=" + var1[var5]);
            }

            String var6 = "cn=" + var3[var5] + "," + this.resourceBaseDN;
            var2.delete(var6);
         }
      } catch (LDAPException var10) {
         checkStorageException(var10);
      } finally {
         if (var2 != null) {
            releaseConnection(var2);
         }

      }

   }

   public void createPredicate(String var1) throws EnDuplicateKeyException {
      if (traceLogger != null && traceLogger.isDebugEnabled()) {
         traceLogger.debug("create predicate: " + var1);
      }

      LDAPConnection var2 = null;

      try {
         var2 = getConnection();
         LDAPAttributeSet var3 = new LDAPAttributeSet();
         var3.add(new LDAPAttribute("objectclass", PREDICATE_OBJ_CLASSES));
         var3.add(new LDAPAttribute("cn", var1));
         String var4 = "cn=" + var1 + "," + this.predicateBaseDN;
         LDAPEntry var5 = new LDAPEntry(var4, var3);
         var2.add(var5);
      } catch (LDAPException var9) {
         checkDuplicateException(var9);
      } finally {
         if (var2 != null) {
            releaseConnection(var2);
         }

      }

   }

   public void removePredicate(String var1) throws EnFinderException {
      if (traceLogger != null && traceLogger.isDebugEnabled()) {
         traceLogger.debug("remove predicate: " + var1);
      }

      LDAPConnection var2 = null;

      try {
         var2 = getConnection();
         String var3 = "cn=" + var1 + "," + this.predicateBaseDN;
         var2.delete(var3);
      } catch (LDAPException var7) {
         checkFinderException(var7);
      } finally {
         if (var2 != null) {
            releaseConnection(var2);
         }

      }

   }

   public boolean predicateExists(String var1) {
      boolean var2 = false;
      if (traceLogger != null && traceLogger.isDebugEnabled()) {
         traceLogger.debug("predicate exists: " + var1);
      }

      LDAPConnection var3 = null;

      try {
         var3 = getConnection();
         LDAPSearchResults var4 = var3.search(this.predicateBaseDN, 1, "cn=" + var1, noAttrs, false);
         var2 = var4.hasMoreElements();
      } catch (LDAPException var8) {
         checkStorageException(var8);
      } finally {
         if (var3 != null) {
            releaseConnection(var3);
         }

      }

      return var2;
   }

   public Collection fetchPredicates(TextFilter var1) {
      if (traceLogger != null && traceLogger.isDebugEnabled()) {
         traceLogger.debug("fetch predicates: ");
      }

      return this.fetchNames(var1, this.predicateBaseDN, "cn");
   }

   public void setDataChangeListener(EnDataChangeListener var1) {
      if (traceLogger != null && traceLogger.isDebugEnabled()) {
         traceLogger.debug("setDataChangeListener()");
      }

      EmbeddedLDAP var2 = EmbeddedLDAP.getEmbeddedLDAP();
      if (var2 != null) {
         LDAPChangeListener var3 = new LDAPChangeListener(var1);
         var2.registerChangeListener(this.roleBaseDN, var3);
         var2.registerChangeListener(this.resourceBaseDN, var3);
         var2.registerChangeListener(this.predicateBaseDN, var3);
      }

   }

   public void applicationDeletedResources(String var1, int var2, String var3) throws EnFinderException, EnRemoveException {
      if (traceLogger != null && traceLogger.isDebugEnabled()) {
         traceLogger.debug("application delete resources");
      }

      LDAPConnection var4 = null;

      try {
         var4 = getConnection();
         ProviderUtils.applicationDeleted(var4, this.resourceBaseDN, var1, var2, var3, traceLogger);
      } catch (LDAPException var9) {
         checkStorageException(var9);
      } finally {
         if (var4 != null) {
            releaseConnection(var4);
         }

      }

   }

   public void cleanupAfterCollectionResources(String var1, long var2, List var4) throws EnFinderException, EnRemoveException {
      if (traceLogger != null && traceLogger.isDebugEnabled()) {
         traceLogger.debug("cleanup after collection resources");
      }

      LDAPConnection var5 = null;

      try {
         var5 = getConnection();
         ProviderUtils.cleanupAfterCollection(var5, this.resourceBaseDN, var1, var2, var4, traceLogger);
      } catch (LDAPException var10) {
         checkStorageException(var10);
      } finally {
         if (var5 != null) {
            releaseConnection(var5);
         }

      }

   }

   public void cleanupAfterCollectionRoles(String var1, long var2, List var4) throws EnFinderException, EnRemoveException {
      if (traceLogger != null && traceLogger.isDebugEnabled()) {
         traceLogger.debug("cleanup after collection roles");
      }

      LDAPConnection var5 = null;

      try {
         var5 = getConnection();
         ProviderUtils.cleanupAfterCollection(var5, this.roleBaseDN, var1, var2, var4, traceLogger);
      } catch (LDAPException var10) {
         checkStorageException(var10);
      } finally {
         if (var5 != null) {
            releaseConnection(var5);
         }

      }

   }

   public void cleanupAfterDeployResources(String var1, int var2, String var3, long var4) throws EnFinderException, EnRemoveException {
      if (traceLogger != null && traceLogger.isDebugEnabled()) {
         traceLogger.debug("cleanup after deploy resources");
      }

      LDAPConnection var6 = null;

      try {
         var6 = getConnection();
         ProviderUtils.cleanupAfterAppDeploy(var6, this.resourceBaseDN, var1, var2, var3, var4, traceLogger);
      } catch (LDAPException var11) {
         checkStorageException(var11);
      } finally {
         if (var6 != null) {
            releaseConnection(var6);
         }

      }

   }

   public void applicationCopyResources(String var1, String var2) throws EnCreateException {
      if (traceLogger != null && traceLogger.isDebugEnabled()) {
         traceLogger.debug("application copy resources");
      }

      LDAPConnection var3 = null;

      try {
         var3 = getConnection();
         ProviderUtils.applicationCopy(var3, this.resourceBaseDN, var1, var2, nameAttributeList, ProviderUtils.EXCLUDED_ON_COPY_ATTRS, traceLogger);
      } catch (LDAPException var8) {
         if (traceLogger != null && traceLogger.isDebugEnabled()) {
            traceLogger.debug("application copy resources exception: " + var8.toString(), var8);
         }

         throw new EnCreateException(var8.toString());
      } finally {
         if (var3 != null) {
            releaseConnection(var3);
         }

      }

   }

   public void applicationDeletedRoles(String var1, int var2, String var3) throws EnFinderException, EnRemoveException {
      if (traceLogger != null && traceLogger.isDebugEnabled()) {
         traceLogger.debug("application delete roles");
      }

      LDAPConnection var4 = null;

      try {
         var4 = getConnection();
         ProviderUtils.applicationDeleted(var4, this.roleBaseDN, var1, var2, var3, traceLogger);
      } catch (LDAPException var9) {
         checkStorageException(var9);
      } finally {
         if (var4 != null) {
            releaseConnection(var4);
         }

      }

   }

   public void cleanupAfterDeployRoles(String var1, int var2, String var3, long var4) throws EnFinderException, EnRemoveException {
      if (traceLogger != null && traceLogger.isDebugEnabled()) {
         traceLogger.debug("cleanup after deploy roles");
      }

      LDAPConnection var6 = null;

      try {
         var6 = getConnection();
         ProviderUtils.cleanupAfterAppDeploy(var6, this.roleBaseDN, var1, var2, var3, var4, traceLogger);
      } catch (LDAPException var11) {
         checkStorageException(var11);
      } finally {
         if (var6 != null) {
            releaseConnection(var6);
         }

      }

   }

   public void applicationCopyRoles(String var1, String var2) throws EnCreateException {
      if (traceLogger != null && traceLogger.isDebugEnabled()) {
         traceLogger.debug("application copy roles");
      }

      LDAPConnection var3 = null;

      try {
         var3 = getConnection();
         ProviderUtils.applicationCopy(var3, this.roleBaseDN, var1, var2, nameAttributeList, ProviderUtils.EXCLUDED_ON_COPY_ATTRS, traceLogger);
      } catch (LDAPException var8) {
         if (traceLogger != null && traceLogger.isDebugEnabled()) {
            traceLogger.debug("application copy roles exception: " + var8.toString(), var8);
         }

         throw new EnCreateException(var8.toString());
      } finally {
         if (var3 != null) {
            releaseConnection(var3);
         }

      }

   }

   public static String PK2Name(ERoleId var0) {
      return PK2Name(escaper.escapeString(var0.getResourceName()), escaper.escapeString(var0.getRoleName()));
   }

   protected static String unescapeName(String var0) {
      return escaper.unescapeString(var0);
   }

   protected static ERoleId name2PK(String var0) {
      int var1 = var0.indexOf("::");
      String var2 = var1 == 0 ? null : escaper.unescapeString(var0.substring(0, var1));
      String var3 = escaper.unescapeString(var0.substring(var1 + "::".length()));
      return new ERoleId(var2, var3);
   }

   protected static String getEntryName(LDAPEntry var0) {
      String var1 = var0.getDN();
      var1 = var1.substring(var1.indexOf(61) + 1, var1.indexOf(44));
      return var1;
   }

   private EExpression getEntryExpression(LDAPEntry var1) {
      LDAPAttribute var2 = var1.getAttribute("EExpr");
      String var3 = null;
      if (var2 != null) {
         String[] var4 = var2.getStringValueArray();
         if (var4 != null && var4.length > 0) {
            var3 = var4[0];
         }
      }

      return var3 == null ? null : EExprRep.deserialize(var3);
   }

   protected static String getEntryAuxiliary(LDAPEntry var0) {
      LDAPAttribute var1 = var0.getAttribute("EAux");
      String var2 = null;
      if (var1 != null) {
         String[] var3 = var1.getStringValueArray();
         if (var3 != null && var3.length > 0) {
            var2 = var3[0];
         }
      }

      return var2;
   }

   protected static boolean getEntryDeployData(LDAPEntry var0) {
      LDAPAttribute var1 = var0.getAttribute("wlsCreatorInfo");
      boolean var2 = false;
      if (var1 != null) {
         String var3 = null;
         String[] var4 = var1.getStringValueArray();
         if (var4 != null && var4.length > 0) {
            var3 = var4[0];
         }

         if ("deploy".equals(var3)) {
            var2 = true;
         }
      }

      return var2;
   }

   private static int countEntries(LDAPSearchResults var0) throws LDAPException {
      int var1;
      for(var1 = 0; var0.hasMoreElements(); ++var1) {
         var0.next();
      }

      return var1;
   }

   private static String[] getRoleNames(ERoleId[] var0) {
      String[] var1 = new String[var0.length];

      for(int var2 = 0; var2 < var0.length; ++var2) {
         var1[var2] = PK2Name(var0[var2]);
      }

      return var1;
   }

   private static String[] getRoleNames(ERole[] var0) {
      String[] var1 = new String[var0.length];

      for(int var2 = 0; var2 < var0.length; ++var2) {
         var1[var2] = PK2Name((ERoleId)var0[var2].getPrimaryKey());
      }

      return var1;
   }

   private static String[] getResourceNames(EResource[] var0) {
      String[] var1 = new String[var0.length];

      for(int var2 = 0; var2 < var0.length; ++var2) {
         var1[var2] = escaper.escapeString(var0[var2].getName());
      }

      return var1;
   }

   private static String makeNameFilter(String[] var0) {
      StringBuffer var1 = new StringBuffer("(|");

      for(int var2 = 0; var2 < var0.length; ++var2) {
         if (var0[var2] != null) {
            var1.append("(");
            var1.append("cn").append("=").append(var0[var2]);
            var1.append(')');
         }
      }

      var1.append(')');
      return var1.toString();
   }

   public EnResourceCursor findResources(TextFilter var1, int var2, EnCursorResourceFilter var3) {
      String var4 = var1 == null ? "*" : var1.toString(escaper, "*");
      if (traceLogger != null && traceLogger.isDebugEnabled()) {
         traceLogger.debug("findResources: " + var4);
      }

      EResourceCursor var5 = null;
      LDAPConnection var6 = null;

      try {
         var6 = getConnection();
         LDAPSearchResults var7 = var6.search(this.resourceBaseDN, 1, "cn=" + var4, eexprAttrList, false);
         if (var3 == null) {
            var5 = new EResourceCursor(var6, var7, var2, this, traceLogger);
         } else {
            var5 = new EResourceCursor(var3, var6, var7, var2, this, traceLogger);
         }
      } catch (LDAPException var8) {
         if (traceLogger != null && traceLogger.isDebugEnabled()) {
            traceLogger.debug("LDAPException while trying to search for resources");
         }

         if (var6 != null) {
            releaseConnection(var6);
         }

         checkStorageException(var8);
      }

      return var5;
   }

   public EnRoleCursor findRoles(TextFilter var1, TextFilter var2, int var3, EnCursorRoleFilter var4) {
      String var5 = var1 == null ? "*" : var1.toString(escaper, "*");
      String var6 = var5;
      if (var2 != null) {
         var6 = PK2Name(var5, var2.toString(escaper, "*"));
      } else if (!var5.endsWith("*")) {
         var6 = var5 + "*";
      }

      if (traceLogger != null && traceLogger.isDebugEnabled()) {
         traceLogger.debug("findRoles: " + var6);
      }

      ERoleCursor var7 = null;
      LDAPConnection var8 = null;

      try {
         var8 = getConnection();
         LDAPSearchResults var9 = var8.search(this.roleBaseDN, 1, "cn=" + var6, eexprAttrList, false);
         if (var4 == null) {
            var7 = new ERoleCursor(var8, var9, var3, this, traceLogger);
         } else {
            var7 = new ERoleCursor(var4, var8, var9, var3, this, traceLogger);
         }
      } catch (LDAPException var10) {
         if (traceLogger != null && traceLogger.isDebugEnabled()) {
            traceLogger.debug("LDAPException while trying to search for roles");
         }

         if (var8 != null) {
            releaseConnection(var8);
         }

         checkStorageException(var10);
      }

      return var7;
   }

   public EResource getResourceFromEntry(LDAPEntry var1) {
      if (traceLogger != null && traceLogger.isDebugEnabled()) {
         traceLogger.debug("getResourceFromEntry");
      }

      EResource var2 = null;
      if (var1 != null) {
         String var3 = getEntryName(var1);
         EExpression var4 = this.getEntryExpression(var1);
         boolean var5 = getEntryDeployData(var1);
         String var6 = getEntryAttribute(var1, "wlsCollectionName");
         String var7 = unescapeName(var3);
         if (var6 != null) {
            var6 = unescapeName(var6);
         }

         var2 = new EResource(var7, var4, var5, var6);
         if (traceLogger != null && traceLogger.isDebugEnabled()) {
            traceLogger.debug("  name: " + var7);
            traceLogger.debug(" eexpr: " + var2.getEntitlement());
            traceLogger.debug("deploy: " + var5);
            if (var6 != null) {
               traceLogger.debug(" cname: " + var6);
            }
         }
      }

      return var2;
   }

   public ERole getRoleFromEntry(LDAPEntry var1) {
      if (traceLogger != null && traceLogger.isDebugEnabled()) {
         traceLogger.debug("getRoleFromEntry");
      }

      ERole var2 = null;
      if (var1 != null) {
         String var3 = getEntryName(var1);
         EExpression var4 = this.getEntryExpression(var1);
         String var5 = getEntryAuxiliary(var1);
         boolean var6 = getEntryDeployData(var1);
         String var7 = getEntryAttribute(var1, "wlsCollectionName");
         ERoleId var8 = name2PK(var3);
         EAuxiliary var9 = null;
         if (var5 != null) {
            var9 = new EAuxiliary(var5);
         }

         var2 = new ERole(var8, var4, var9, var6, var7);
         if (traceLogger != null && traceLogger.isDebugEnabled()) {
            traceLogger.debug("  name: " + var8.toString());
            traceLogger.debug(" eexpr: " + var2.getEntitlement());
            traceLogger.debug("deploy: " + var6);
            traceLogger.debug("   aux: " + var9);
            if (var7 != null) {
               traceLogger.debug(" cname: " + var7);
            }
         }
      }

      return var2;
   }

   public void createForCollection(EResource[] var1) throws EnConflictException, EnDuplicateKeyException, EnCreateException {
      if (traceLogger != null && traceLogger.isDebugEnabled()) {
         traceLogger.debug("create resources for collection");
      }

      LDAPConnection var2 = null;

      try {
         String[] var3 = getResourceNames(var1);
         var2 = getConnection();
         LDAPSearchResults var4 = var2.search(this.resourceBaseDN, 1, makeNameFilter(var3), conflictAttrList, false);
         String var8;
         if (var4.hasMoreElements()) {
            LDAPEntry var18 = var4.next();
            String var19 = getEntryName(var18);
            boolean var20 = getEntryDeployData(var18);
            var8 = "Entitlement policy definition for '" + escaper.unescapeString(var19) + "' ";
            if (var20) {
               throw new EnDuplicateKeyException(var8 + "already exist.");
            }

            if (traceLogger != null && traceLogger.isDebugEnabled()) {
               String var21 = getEntryAttribute(var18, "wlsCollectionName");
               traceLogger.debug("conflict: " + var21 + " -- " + var19);
            }

            throw new EnConflictException(var8 + "is customized.");
         }

         for(int var5 = 0; var5 < var1.length; ++var5) {
            LDAPAttributeSet var6 = new LDAPAttributeSet();
            var6.add(new LDAPAttribute("objectclass", RESOURCE_OBJ_CLASSES));
            var6.add(new LDAPAttribute("cn", var3[var5]));
            EExpression var7 = var1[var5].getExpression();
            if (var7 != null) {
               var6.add(new LDAPAttribute("EExpr", var7.serialize()));
            }

            var8 = "cn=" + var3[var5] + "," + this.resourceBaseDN;
            boolean var9 = var1[var5].isDeployData();
            LDAPAttribute var10 = new LDAPAttribute("wlsCreatorInfo", var9 ? "deploy" : "mbean");
            var6.add(var10);
            String var11 = var1[var5].getCollectionName();
            if (var11 != null) {
               var6.add(new LDAPAttribute("wlsCollectionName", escaper.escapeString(var11)));
            }

            LDAPEntry var12 = new LDAPEntry(var8, var6);
            var2.add(var12);
         }
      } catch (LDAPException var16) {
         checkDuplicateException(var16);
      } finally {
         if (var2 != null) {
            releaseConnection(var2);
         }

      }

   }

   public void createPolicyCollectionInfo(String var1, String var2, String var3) throws EnCreateException, EnConflictException {
      LDAPConnection var4 = null;

      try {
         String var5 = escaper.escapeString(var1);
         if (traceLogger != null && traceLogger.isDebugEnabled()) {
            traceLogger.debug("createPolicyCollectionInfo(" + var5 + ")");
         }

         String var6 = "wlsCollectionName=" + var5 + "," + this.policyCollectionBaseDN;
         LDAPAttributeSet var7 = new LDAPAttributeSet();
         var7.add(new LDAPAttribute("objectclass", POLICY_COL_OBJ_CLASSES));
         var7.add(new LDAPAttribute("wlsCollectionName", var5));
         if (var2 != null) {
            var7.add(new LDAPAttribute("wlsCollectionVersion", var2));
         }

         if (var3 != null) {
            var7.add(new LDAPAttribute("wlsCollectionTimestamp", var3));
         }

         LDAPEntry var8 = new LDAPEntry(var6, var7);
         var4 = getConnection();

         try {
            var4.add(var8);
         } catch (LDAPException var17) {
            if (var17.getLDAPResultCode() != 68) {
               throw new EnCreateException(var17.toString());
            }

            try {
               var4.delete(var8.getDN());
            } catch (LDAPException var16) {
               if (var16.getLDAPResultCode() == 32) {
                  if (traceLogger != null && traceLogger.isDebugEnabled()) {
                     traceLogger.debug("createPolicyCollectionInfo(): conflict for " + var5);
                  }

                  throw new EnConflictException(var5 + " conflict.");
               }

               throw new EnCreateException(var16.toString());
            }

            var4.add(var8);
         }
      } catch (LDAPException var18) {
         if (var18.getLDAPResultCode() == 68) {
            if (traceLogger != null && traceLogger.isDebugEnabled()) {
               traceLogger.debug("createPolicyCollectionInfo(): conflict for " + var1);
            }

            throw new EnConflictException(var1 + " conflict.");
         }

         throw new EnCreateException(var18.toString());
      } finally {
         if (var4 != null) {
            releaseConnection(var4);
         }

      }

   }

   public void createRoleCollectionInfo(String var1, String var2, String var3) throws EnCreateException, EnConflictException {
      LDAPConnection var4 = null;

      try {
         String var5 = escaper.escapeString(var1);
         if (traceLogger != null && traceLogger.isDebugEnabled()) {
            traceLogger.debug("createRoleCollectionInfo(" + var5 + ")");
         }

         String var6 = "wlsCollectionName=" + var5 + "," + this.roleCollectionBaseDN;
         LDAPAttributeSet var7 = new LDAPAttributeSet();
         var7.add(new LDAPAttribute("objectclass", ROLE_COL_OBJ_CLASSES));
         var7.add(new LDAPAttribute("wlsCollectionName", var5));
         if (var2 != null) {
            var7.add(new LDAPAttribute("wlsCollectionVersion", var2));
         }

         if (var3 != null) {
            var7.add(new LDAPAttribute("wlsCollectionTimestamp", var3));
         }

         LDAPEntry var8 = new LDAPEntry(var6, var7);
         var4 = getConnection();

         try {
            var4.add(var8);
         } catch (LDAPException var17) {
            if (var17.getLDAPResultCode() != 68) {
               throw new EnCreateException(var17.toString());
            }

            try {
               var4.delete(var8.getDN());
            } catch (LDAPException var16) {
               if (var16.getLDAPResultCode() == 32) {
                  if (traceLogger != null && traceLogger.isDebugEnabled()) {
                     traceLogger.debug("createRoleCollectionInfo(): conflict for " + var5);
                  }

                  throw new EnConflictException(var5 + " conflict.");
               }

               throw new EnCreateException(var16.toString());
            }

            var4.add(var8);
         }
      } catch (LDAPException var18) {
         if (var18.getLDAPResultCode() == 68) {
            if (traceLogger != null && traceLogger.isDebugEnabled()) {
               traceLogger.debug("createRoleCollectionInfo(): conflict for " + var1);
            }

            throw new EnConflictException(var1 + " conflict.");
         }

         throw new EnCreateException(var18.toString());
      } finally {
         if (var4 != null) {
            releaseConnection(var4);
         }

      }

   }

   public EPolicyCollectionInfo fetchPolicyCollectionInfo(String var1) {
      LDAPConnection var2 = null;

      try {
         String var3 = escaper.escapeString(var1);
         if (traceLogger != null && traceLogger.isDebugEnabled()) {
            traceLogger.debug("fetchPolicyCollectionInfo(" + var3 + ")");
         }

         var2 = getConnection();
         LDAPSearchResults var4 = var2.search(this.policyCollectionBaseDN, 1, "wlsCollectionName=" + var3, collectionAttrList, false);

         while(var4.hasMoreElements()) {
            LDAPEntry var5 = var4.next();
            if (var5 != null) {
               String var6 = getEntryName(var5);
               String var7 = unescapeName(var6);
               String var8 = getEntryAttribute(var5, "wlsCollectionVersion");
               String var9 = getEntryAttribute(var5, "wlsCollectionTimestamp");
               if (traceLogger != null && traceLogger.isDebugEnabled()) {
                  traceLogger.debug("     name: " + var7);
                  traceLogger.debug("  version: " + var8);
                  traceLogger.debug("timestamp: " + var9);
               }

               EPolicyCollectionInfo var10 = new EPolicyCollectionInfo(var7, var8, var9);
               return var10;
            }
         }
      } catch (LDAPException var14) {
         checkStorageException(var14);
      } finally {
         if (var2 != null) {
            releaseConnection(var2);
         }

      }

      return null;
   }

   public ERoleCollectionInfo fetchRoleCollectionInfo(String var1) {
      LDAPConnection var2 = null;

      try {
         String var3 = escaper.escapeString(var1);
         if (traceLogger != null && traceLogger.isDebugEnabled()) {
            traceLogger.debug("fetchRoleCollectionInfo(" + var3 + ")");
         }

         var2 = getConnection();
         LDAPSearchResults var4 = var2.search(this.roleCollectionBaseDN, 1, "wlsCollectionName=" + var3, collectionAttrList, false);

         while(var4.hasMoreElements()) {
            LDAPEntry var5 = var4.next();
            if (var5 != null) {
               String var6 = getEntryName(var5);
               String var7 = unescapeName(var6);
               String var8 = getEntryAttribute(var5, "wlsCollectionVersion");
               String var9 = getEntryAttribute(var5, "wlsCollectionTimestamp");
               if (traceLogger != null && traceLogger.isDebugEnabled()) {
                  traceLogger.debug("     name: " + var7);
                  traceLogger.debug("  version: " + var8);
                  traceLogger.debug("timestamp: " + var9);
               }

               ERoleCollectionInfo var10 = new ERoleCollectionInfo(var7, var8, var9);
               return var10;
            }
         }
      } catch (LDAPException var14) {
         checkStorageException(var14);
      } finally {
         if (var2 != null) {
            releaseConnection(var2);
         }

      }

      return null;
   }

   protected static String getEntryAttribute(LDAPEntry var0, String var1) {
      LDAPAttribute var2 = var0.getAttribute(var1);
      String var3 = null;
      if (var2 != null) {
         String[] var4 = var2.getStringValueArray();
         if (var4 != null && var4.length > 0) {
            var3 = var4[0];
         }
      }

      return var3;
   }

   static {
      escaper = new Escaping(SPECIAL_CHARS);
   }

   private class LDAPChangeListener implements EmbeddedLDAPChangeListener {
      private EnDataChangeListener listener;

      public LDAPChangeListener(EnDataChangeListener var2) {
         this.listener = var2;
      }

      public void entryChanged(EmbeddedLDAPChange var1) {
         String var2 = var1.getEntryName();
         if (EnLDAP.traceLogger != null && EnLDAP.traceLogger.isDebugEnabled()) {
            EnLDAP.traceLogger.debug("entryChanged: " + var2);
         }

         if (var2 != null && var2.startsWith("cn=")) {
            int var3 = var2.indexOf(44, 3);
            if (var3 > 0) {
               String var4 = var2.substring(3, var3);
               if (var2.endsWith(EData.this.roleBaseDN)) {
                  ERoleId var5 = EData.name2PK(var4);
                  this.listener.roleChanged(var5);
               } else {
                  String var6;
                  if (var2.endsWith(EData.this.resourceBaseDN)) {
                     var6 = EData.escaper.unescapeString(var4);
                     this.listener.resourceChanged(var6);
                  } else if (var2.endsWith(EData.this.predicateBaseDN)) {
                     var6 = EData.escaper.unescapeString(var4);
                     this.listener.predicateChanged(var6);
                  }
               }
            }
         }

      }
   }
}
