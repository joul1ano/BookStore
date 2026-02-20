import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Login from "./pages/Login";
import BooksPage from "./pages/users/BooksPage";
import BookDetails from "./pages/users/BookDetails";
import Register from "./pages/Register"
import MainLayout from "./Layouts/MainLayout";
import { CartProvider } from "./context/CartContext.jsx";
import PreviewCart from "./pages/users/PreviewCart";
import Checkout from "./pages/users/Checkout.jsx";
import { FavouriteProvider } from "./context/FavouriteContext.jsx";
import FavouriteBooks from "./pages/users/FavouriteBooks.jsx";
import Profile from "./pages/users/Profile.jsx"
import AdminLayout from "./Layouts/AdminLayout.jsx"
import AdminProducts from "./pages/admin/AdminProducts.jsx";
import OrdersPage from "./pages/users/OrdersPage";
import OrderDetailsPage from "./pages/users/OrderDetails";
import CreateBook from "./pages/admin/CreateBook";
import EditBook from "./pages/admin/EditBook";
import UsersPage from "./pages/admin/UsersPage.jsx";
import AdminOrders from "./pages/admin/AdminOrders";
import AdminOrderDetail from "./pages/admin/AdminOrderDetails";

function App() {
  return (
    <CartProvider>        {/* ← wraps everything, Login can use useCart() */}
      <Router>
        <Routes>

          {/* No navbar */}
          <Route path="/" element={<Login />} />
          <Route path="/register" element={<Register />} />

          {/* User routes — FavouriteProvider only mounts here */}
          <Route element={
            <FavouriteProvider>
              <MainLayout />
            </FavouriteProvider>
          }>
            <Route path="/books" element={<BooksPage />} />
            <Route path="/books/:id" element={<BookDetails />} />
            <Route path="me/cart" element={<PreviewCart />} />
            <Route path="me/cart/checkout" element={<Checkout />} />
            <Route path="me/favourites" element={<FavouriteBooks />} />
            <Route path="/me" element={<Profile />} />
            <Route path="/me/orders" element={<OrdersPage />} />
            <Route path="/me/orders/:orderId" element={<OrderDetailsPage />} />
          </Route>

          {/* Admin routes — FavouriteProvider never mounts here */}
          <Route element={<AdminLayout />}>
            <Route path="/admin/products" element={<AdminProducts />} />
            <Route path="/admin/products/new" element={<CreateBook />} />
            <Route path="/admin/products/:bookId" element={<EditBook />} />
            <Route path="/admin/users" element={<UsersPage />} />
            <Route path="/admin/orders" element={<AdminOrders />} />
            <Route path="/admin/orders/:orderId" element={<AdminOrderDetail />} />
          </Route>

        </Routes>
      </Router>
    </CartProvider>
  );
}
export default App;
