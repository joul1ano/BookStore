import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Login from "./pages/Login";
import BooksPage from "./pages/BooksPage";
import BookDetails from "./pages/BookDetails";
import Register from "./pages/Register"
import MainLayout from "./Layouts/MainLayout";
import { CartProvider } from "./context/CartContext.jsx";
import PreviewCart from "./pages/PreviewCart";
import Checkout from "./pages/Checkout.jsx";
import { FavouriteProvider } from "./context/FavouriteContext.jsx";
import FavouriteBooks from "./pages/FavouriteBooks.jsx";
import Profile from "./pages/Profile.jsx"
import AdminLayout from "./Layouts/AdminLayout.jsx"
import AdminProducts from "./pages/AdminProducts.jsx";
import OrdersPage from "./pages/OrdersPage";
import OrderDetailsPage from "./pages/OrderDetails";
import CreateBook from "./pages/CreateBook";
import EditBook from "./pages/EditBook";

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
          </Route>

        </Routes>
      </Router>
    </CartProvider>
  );
}
export default App;
