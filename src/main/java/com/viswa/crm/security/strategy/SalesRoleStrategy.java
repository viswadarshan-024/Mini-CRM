package com.viswa.crm.security.strategy;

import org.springframework.stereotype.Component;

@Component("SALES")
public class SalesRoleStrategy implements RoleStrategy {

    public boolean canManageUsers() { return false; }

    public boolean canCreateCompany() { return true; }
    public boolean canEditCompany() { return true; }
    public boolean canDeleteCompany() { return false; }

    public boolean canCreateContact() { return true; }
    public boolean canEditContact() { return true; }
    public boolean canDeleteContact() { return false; }

    public boolean canCreateDeal() { return true; }
    public boolean canEditDeal() { return true; }
    public boolean canDeleteDeal() { return false; }
    public boolean canViewAllDeals() { return false; }
    public boolean canAssignDeal() { return false; }

    public boolean canCreateActivity() { return true; }
    public boolean canEditActivity() { return true; }
    public boolean canDeleteActivity() { return false; }

    public boolean canViewDashboard() { return true; }
    public boolean canViewDeals() { return true; }
    public boolean canViewContacts() { return true; }
    public boolean canViewCompanies()  { return false; }
    public boolean canManageCompanies()  { return false; }
}
