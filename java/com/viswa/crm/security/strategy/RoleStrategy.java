package com.viswa.crm.security.strategy;

// Strategy Pattern implementation for Role Based Access Control
public interface RoleStrategy {

    // User
    boolean canManageUsers();

    // Company
    boolean canCreateCompany();
    boolean canEditCompany();
    boolean canDeleteCompany();

    boolean canCreateContact();
    boolean canEditContact();
    boolean canDeleteContact();

    // Deal module
    boolean canCreateDeal();
    boolean canEditDeal();
    boolean canDeleteDeal();
    boolean canViewAllDeals();
    boolean canAssignDeal();

    // Activity
    boolean canCreateActivity();
    boolean canEditActivity();
    boolean canDeleteActivity();


    // for jsp
    boolean canViewDashboard();
    boolean canViewDeals();
    boolean canViewContacts();
    boolean canViewCompanies();
    boolean canManageCompanies();
}
