<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard | Mini CRM</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">

    <style>
        :root {
            --sidebar-width: 250px;
            --primary-bg: #2c3e50;
            --hover-bg: #34495e;
            --active-accent: #3498db;
        }
        body { background-color: #f4f6f9; font-family: 'Segoe UI', sans-serif; }

        /* Sidebar */
        .sidebar {
            width: var(--sidebar-width);
            height: 100vh;
            position: fixed;
            top: 0; left: 0;
            background-color: var(--primary-bg);
            color: white;
            padding-top: 20px;
            z-index: 1000;
        }
        .sidebar-brand {
            text-align: center; font-size: 1.4rem; font-weight: bold; margin-bottom: 20px; color: white; text-decoration: none; display: block;
        }
        .nav-link {
            color: #bdc3c7; padding: 12px 20px; display: flex; align-items: center; text-decoration: none; transition: 0.3s;
        }
        .nav-link:hover { background-color: var(--hover-bg); color: white; }
        .nav-link.active { background-color: var(--active-accent); color: white; }
        .nav-link i { width: 30px; text-align: center; }
        .nav-category {
            font-size: 0.75rem; text-transform: uppercase; color: #7f8c8d; padding: 15px 20px 5px; font-weight: bold;
        }

        /* Main Content */
        .main-content { margin-left: var(--sidebar-width); padding: 30px; }

        /* Header */
        .top-bar { display: flex; justify-content: space-between; align-items: center; margin-bottom: 30px; }
        .user-pill { background: white; padding: 5px 15px; border-radius: 20px; box-shadow: 0 2px 5px rgba(0,0,0,0.05); display: flex; align-items: center; gap: 10px; }

        /* Cards */
        .stat-card {
            background: white; border-radius: 8px; padding: 20px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.05); position: relative; overflow: hidden;
        }
        .stat-value { font-size: 2rem; font-weight: bold; color: #2c3e50; }
        .stat-label { color: #7f8c8d; font-size: 0.9rem; text-transform: uppercase; }
        .stat-icon { position: absolute; right: 20px; top: 20px; font-size: 2.5rem; opacity: 0.1; }

        /* Activity Feed */
        .activity-feed { background: white; border-radius: 8px; padding: 0; overflow: hidden; box-shadow: 0 2px 10px rgba(0,0,0,0.05); }
        .feed-header { padding: 15px 20px; border-bottom: 1px solid #eee; font-weight: bold; }
        .feed-item { padding: 15px 20px; border-bottom: 1px solid #f8f9fa; display: flex; justify-content: space-between; align-items: center; }
        .feed-item:last-child { border-bottom: none; }
    </style>
</head>
<body>

<div class="sidebar">
    <a href="dashboard.action" class="sidebar-brand">
        <i class="fa-solid fa-cube"></i> Mini CRM
    </a>

    <div class="nav-category">Main Menu</div>
    <a href="dashboard.action" class="nav-link active">
        <i class="fa-solid fa-gauge-high"></i> Dashboard
    </a>
    <a href="deal.action" class="nav-link">
        <i class="fa-solid fa-handshake"></i> Deals
    </a>
    <a href="contact.action" class="nav-link">
        <i class="fa-solid fa-address-book"></i> Contacts
    </a>
    <a href="activity.action" class="nav-link">
        <i class="fa-solid fa-calendar-check"></i> Activities
    </a>

    <s:if test="role().canManageCompanies() || role().canManageUsers()">
        <div class="nav-category">Management</div>

        <s:if test="role().canManageCompanies()">
            <a href="company.action" class="nav-link">
                <i class="fa-solid fa-building"></i> Companies
            </a>
        </s:if>

        <s:if test="role().canManageUsers()">
            <a href="user.action" class="nav-link">
                <i class="fa-solid fa-users-gear"></i> Users & Roles
            </a>
        </s:if>
    </s:if>

    <div class="mt-5">
        <a href="logout.action" class="nav-link text-danger">
            <i class="fa-solid fa-right-from-bracket"></i> Logout
        </a>
    </div>
</div>

<div class="main-content">

    <div class="top-bar">
        <h3>Overview</h3>
        <div class="user-pill">
            <i class="fa-solid fa-circle-user fa-lg text-secondary"></i>
            <div>
                <div class="fw-bold"><s:property value="user.fullName"/></div>
                <div class="small text-muted" style="font-size: 0.75rem;"><s:property value="user.roleName"/></div>
            </div>
        </div>
    </div>
    <div class="row g-4 mb-4">
        <div class="col-md-3">
            <div class="stat-card border-start border-4 border-primary">
                <div class="stat-label">Total Deals</div>
                <div class="stat-value"><s:property value="summary.totalDeals"/></div>
                <i class="fa-solid fa-briefcase stat-icon text-primary"></i>
            </div>
        </div>
        <div class="col-md-3">
            <div class="stat-card border-start border-4 border-success">
                <div class="stat-label">Contacts</div>
                <div class="stat-value"><s:property value="summary.totalContacts"/></div>
                <i class="fa-solid fa-address-card stat-icon text-success"></i>
            </div>
        </div>
        <div class="col-md-3">
            <div class="stat-card border-start border-4 border-warning">
                <div class="stat-label">Open Deals</div>
                <div class="stat-value"><s:property value="summary.dealsInProgress + summary.dealsNew"/></div>
                <i class="fa-solid fa-folder-open stat-icon text-warning"></i>
            </div>
        </div>
        <div class="col-md-3">
            <div class="stat-card border-start border-4 border-danger">
                <div class="stat-label">Pending Actions</div>
                <div class="stat-value"><s:property value="summary.pendingActivities"/></div>
                <i class="fa-solid fa-bell stat-icon text-danger"></i>
            </div>
        </div>
    </div>

    <div class="activity-feed">
        <div class="feed-header">
            <i class="fa-solid fa-clock-rotate-left me-2 text-secondary"></i> Recent Activities
        </div>

        <s:if test="recentItems != null && !recentItems.isEmpty()">
            <s:iterator value="recentItems">
                <div class="feed-item">
                    <div>
                        <strong><s:property value="title"/></strong><br/>
                        <span class="text-muted small"><s:property value="type"/></span>
                    </div>
                    <div class="text-end text-muted small">
                        <s:date name="date" format="MMM dd, HH:mm"/>
                    </div>
                </div>
            </s:iterator>
        </s:if>
        <s:else>
            <div class="p-4 text-center text-muted">No recent activities found.</div>
        </s:else>
    </div>

</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>