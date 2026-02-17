import { Outlet } from "react-router-dom";
import AdminSidebar from "./AdminSidebar";

function AdminLayout() {
    return (
        <div className="d-flex vh-100">
            <AdminSidebar />
            <main className="flex-grow-1 overflow-auto bg-light">
                <Outlet />
            </main>
        </div>
    );
}

export default AdminLayout;