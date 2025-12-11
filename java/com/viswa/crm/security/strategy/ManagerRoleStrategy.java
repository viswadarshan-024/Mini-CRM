package com.viswa.crm.security.strategy;

import org.springframework.stereotype.Component;

@Component("MANAGER")
public class ManagerRoleStrategy implements RoleStrategy {

    public boolean canManageUsers() { return false; }

    public boolean canCreateCompany() { return true; }
    public boolean canEditCompany() { return true; }
    public boolean canDeleteCompany() { return false; }

    public boolean canCreateContact() { return true; }
    public boolean canEditContact() { return true; }
    public boolean canDeleteContact() { return true; }

    public boolean canCreateDeal() { return true; }
    public boolean canEditDeal() { return true; }
    public boolean canDeleteDeal() { return false; }
    public boolean canViewAllDeals() { return true; }
    public boolean canAssignDeal() { return true; }

    public boolean canCreateActivity() { return true; }
    public boolean canEditActivity() { return true; }
    public boolean canDeleteActivity() { return true; }

    public boolean canViewDashboard() { return true; }
    public boolean canViewDeals() { return true; }
    public boolean canViewContacts() { return true; }
    public boolean canViewCompanies()  { return true; }
    public boolean canManageCompanies()  { return true; }
}
