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
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        :root {
            --primary: #3498db;
            --secondary: #2c3e50;
            --success: #27ae60;
            --warning: #f39c12;
            --danger: #e74c3c;
            --light: #ecf0f1;
            --lighter: #f8f9fa;
            --dark: #2c3e50;
            --text-muted: #7f8c8d;
            --border: #e0e0e0;
            --sidebar-width: 260px;
            --transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
        }

        body {
            background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
            font-family: 'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI', sans-serif;
            color: var(--dark);
            min-height: 100vh;
        }

        /* ===== SIDEBAR ===== */
        .sidebar {
            width: var(--sidebar-width);
            height: 100vh;
            position: fixed;
            top: 0;
            left: 0;
            background: linear-gradient(180deg, var(--secondary) 0%, #1a252f 100%);
            color: white;
            padding: 25px 0;
            z-index: 1000;
            box-shadow: 2px 0 15px rgba(0, 0, 0, 0.1);
            overflow-y: auto;
        }

        .sidebar::-webkit-scrollbar {
            width: 6px;
        }

        .sidebar::-webkit-scrollbar-track {
            background: rgba(255, 255, 255, 0.05);
        }

        .sidebar::-webkit-scrollbar-thumb {
            background: rgba(255, 255, 255, 0.2);
            border-radius: 3px;
        }

        .sidebar-brand {
            text-align: center;
            font-size: 1.5rem;
            font-weight: 700;
            margin-bottom: 40px;
            color: white;
            text-decoration: none;
            display: flex;
            align-items: center;
            justify-content: center;
            gap: 10px;
            transition: var(--transition);
        }

        .sidebar-brand:hover {
            transform: scale(1.05);
        }

        .sidebar-brand i {
            font-size: 1.8rem;
        }

        .nav-category {
            font-size: 0.7rem;
            text-transform: uppercase;
            letter-spacing: 1px;
            color: rgba(255, 255, 255, 0.5);
            padding: 15px 20px 8px;
            font-weight: 600;
        }

        .nav-link {
            color: rgba(255, 255, 255, 0.75);
            padding: 12px 20px;
            display: flex;
            align-items: center;
            text-decoration: none;
            transition: var(--transition);
            position: relative;
            margin: 4px 10px;
            border-radius: 8px;
            font-weight: 500;
        }

        .nav-link i {
            width: 24px;
            text-align: center;
            margin-right: 12px;
            font-size: 1rem;
        }

        .nav-link:hover {
            background-color: rgba(255, 255, 255, 0.1);
            color: white;
            transform: translateX(5px);
        }

        .nav-link.active {
            background: linear-gradient(90deg, var(--primary), #2980b9);
            color: white;
            box-shadow: 0 4px 12px rgba(52, 152, 219, 0.3);
        }

        .nav-link.active::before {
            content: '';
            position: absolute;
            left: 0;
            top: 0;
            bottom: 0;
            width: 4px;
            background: white;
            border-radius: 0 2px 2px 0;
        }

        .logout-section {
            margin-top: 40px;
            padding-top: 20px;
            border-top: 1px solid rgba(255, 255, 255, 0.1);
        }

        /* ===== MAIN CONTENT ===== */
        .main-content {
            margin-left: var(--sidebar-width);
            padding: 30px;
            min-height: 100vh;
        }

        /* ===== TOP BAR ===== */
        .top-bar {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 35px;
            background: white;
            padding: 20px 25px;
            border-radius: 12px;
            box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
        }

        .top-bar h3 {
            margin: 0;
            font-weight: 700;
            font-size: 1.8rem;
            color: var(--dark);
        }

        .user-pill {
            background: white;
            padding: 8px 16px;
            border-radius: 25px;
            box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
            display: flex;
            align-items: center;
            gap: 12px;
            border: 2px solid var(--light);
        }

        .user-pill i {
            font-size: 1.5rem;
            color: var(--primary);
        }

        .user-info div:first-child {
            font-weight: 600;
            color: var(--dark);
            font-size: 0.95rem;
        }

        .user-info div:last-child {
            font-size: 0.75rem;
            color: var(--text-muted);
            text-transform: uppercase;
            letter-spacing: 0.5px;
        }

        /* ===== STAT CARDS ===== */
        .stats-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 20px;
            margin-bottom: 30px;
        }

        .stat-card {
            background: white;
            border-radius: 12px;
            padding: 25px;
            box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
            position: relative;
            overflow: hidden;
            transition: var(--transition);
            border-left: 5px solid var(--primary);
        }

        .stat-card::before {
            content: '';
            position: absolute;
            top: 0;
            right: 0;
            width: 100px;
            height: 100px;
            background: radial-gradient(circle, rgba(52, 152, 219, 0.1) 0%, transparent 70%);
            border-radius: 50%;
        }

        .stat-card:hover {
            transform: translateY(-8px);
            box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
        }

        .stat-card.primary { border-left-color: var(--primary); }
        .stat-card.success { border-left-color: var(--success); }
        .stat-card.warning { border-left-color: var(--warning); }
        .stat-card.danger { border-left-color: var(--danger); }

        .stat-label {
            color: var(--text-muted);
            font-size: 0.85rem;
            text-transform: uppercase;
            letter-spacing: 0.5px;
            font-weight: 600;
            margin-bottom: 10px;
        }

        .stat-value {
            font-size: 2.5rem;
            font-weight: 700;
            color: var(--dark);
            position: relative;
            z-index: 1;
        }

        .stat-icon {
            position: absolute;
            right: 20px;
            top: 50%;
            transform: translateY(-50%);
            font-size: 3rem;
            opacity: 0.08;
            z-index: 0;
        }

        /* ===== ACTIVITY FEED ===== */
        .activity-feed {
            background: white;
            border-radius: 12px;
            overflow: hidden;
            box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
        }

        .feed-header {
            padding: 20px 25px;
            border-bottom: 1px solid var(--border);
            font-weight: 700;
            font-size: 1.1rem;
            color: var(--dark);
            display: flex;
            align-items: center;
            gap: 10px;
        }

        .feed-header i {
            color: var(--primary);
            font-size: 1.2rem;
        }

        .feed-item {
            padding: 20px 25px;
            border-bottom: 1px solid var(--lighter);
            display: flex;
            justify-content: space-between;
            align-items: center;
            transition: var(--transition);
            position: relative;
        }

        .feed-item:last-child {
            border-bottom: none;
        }

        .feed-item:hover {
            background-color: var(--lighter);
            padding-left: 30px;
        }

        .feed-item::before {
            content: '';
            position: absolute;
            left: 0;
            top: 0;
            bottom: 0;
            width: 4px;
            background: var(--primary);
            transform: scaleY(0);
            transition: var(--transition);
        }

        .feed-item:hover::before {
            transform: scaleY(1);
        }

        .feed-content strong {
            color: var(--dark);
            font-size: 1rem;
            display: block;
            margin-bottom: 5px;
        }

        .feed-content span {
            color: var(--text-muted);
            font-size: 0.9rem;
        }

        .feed-time {
            color: var(--text-muted);
            font-size: 0.85rem;
            text-align: right;
            white-space: nowrap;
        }

        .empty-state {
            padding: 60px 25px;
            text-align: center;
            color: var(--text-muted);
        }

        .empty-state i {
            font-size: 3rem;
            margin-bottom: 15px;
            opacity: 0.3;
        }

        /* ===== RESPONSIVE ===== */
        @media (max-width: 768px) {
            .sidebar {
                width: 200px;
            }

            .main-content {
                margin-left: 200px;
                padding: 20px;
            }

            .top-bar {
                flex-direction: column;
                gap: 15px;
                text-align: center;
            }

            .stats-grid {
                grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
                gap: 15px;
            }

            .stat-card {
                padding: 20px;
            }

            .stat-value {
                font-size: 2rem;
            }
        }

        @media (max-width: 576px) {
            .sidebar {
                width: 0;
                transform: translateX(-100%);
                transition: var(--transition);
            }

            .sidebar.active {
                width: 250px;
                transform: translateX(0);
            }

            .main-content {
                margin-left: 0;
            }

            .stats-grid {
                grid-template-columns: 1fr;
            }

            .feed-item {
                flex-direction: column;
                align-items: flex-start;
            }

            .feed-time {
                text-align: left;
                margin-top: 10px;
            }
        }
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

    <s:if test="user.role.canManageCompanies() || user.role.canManageUsers()">
        <div class="nav-category">Management</div>

        <s:if test="user.role.canManageCompanies()">
            <a href="company.action" class="nav-link">
                <i class="fa-solid fa-building"></i> Companies
            </a>
        </s:if>

        <s:if test="user.role.canManageUsers()">
            <a href="user.action" class="nav-link">
                <i class="fa-solid fa-users-gear"></i> Users & Roles
            </a>
        </s:if>
    </s:if>

    <div class="logout-section">
        <a href="logout.action" class="nav-link">
            <i class="fa-solid fa-right-from-bracket"></i> Logout
        </a>
    </div>
</div>

<div class="main-content">
    <div class="top-bar">
        <h3>Dashboard</h3>
        <div class="user-pill">
            <i class="fa-solid fa-circle-user"></i>
            <div class="user-info">
                <div><s:property value="user.firstName"/> <s:property value="user.lastName"/></div>
                <div><s:property value="user.role.name"/></div>
            </div>
        </div>
    </div>

    <div class="stats-grid">
        <div class="stat-card primary">
            <div class="stat-label">Total Deals</div>
            <div class="stat-value"><s:property value="summary.totalDeals"/></div>
            <i class="fa-solid fa-briefcase stat-icon"></i>
        </div>
        <div class="stat-card success">
            <div class="stat-label">Total Contacts</div>
            <div class="stat-value"><s:property value="summary.totalContacts"/></div>
            <i class="fa-solid fa-address-card stat-icon"></i>
        </div>
        <div class="stat-card warning">
            <div class="stat-label">Open Deals</div>
            <div class="stat-value"><s:property value="summary.dealsInProgress + summary.dealsNew"/></div>
            <i class="fa-solid fa-folder-open stat-icon"></i>
        </div>
        <div class="stat-card danger">
            <div class="stat-label">Pending Activities</div>
            <div class="stat-value"><s:property value="summary.pendingActivities"/></div>
            <i class="fa-solid fa-bell stat-icon"></i>
        </div>
    </div>

    <div class="activity-feed">
        <div class="feed-header">
            <i class="fa-solid fa-clock-rotate-left"></i> Recent Activities
        </div>

        <s:if test="recentItems != null && !recentItems.isEmpty()">
            <s:iterator value="recentItems">
                <div class="feed-item">
                    <div class="feed-content">
                        <strong><s:property value="title"/></strong>
                        <span><s:property value="type"/></span>
                    </div>
                    <div class="feed-time">
                        <s:date name="date" format="MMM dd, HH:mm"/>
                    </div>
                </div>
            </s:iterator>
        </s:if>
        <s:else>
            <div class="empty-state">
                <i class="fa-solid fa-inbox"></i>
                <p>No recent activities found.</p>
            </div>
        </s:else>
    </div>

</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
