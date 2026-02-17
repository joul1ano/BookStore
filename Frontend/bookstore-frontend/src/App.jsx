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

function App() {
  return (
    <CartProvider>
      <FavouriteProvider>
        <Router>
          <Routes>

            {/* No navbar */}
            <Route path="/" element={<Login />} />
            <Route path="/register" element={<Register />} />

            {/* With navbar */}
            <Route element={<MainLayout />}>
              <Route path="/books" element={<BooksPage />} />
              <Route path="/books/:id" element={<BookDetails />} />
              <Route path="/cart" element={<PreviewCart />} />
              <Route path="/checkout" element={<Checkout />} />
              <Route path="/favourites" element={<FavouriteBooks />} />
              <Route path="/me" element={<Profile />} />
            </Route>

            <Route element={<AdminLayout />}>
              <Route path="/admin/products" element={<AdminProducts />} />
            </Route>

          </Routes>
        </Router>
      </FavouriteProvider>
    </CartProvider>
  );
}

export default App;
