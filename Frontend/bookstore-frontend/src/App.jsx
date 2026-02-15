import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Login from "./pages/Login";
import BooksPage from "./pages/BooksPage";
import BookDetails from "./pages/BookDetails";
import Register from "./pages/Register"
import MainLayout from "./Layouts/MainLayout";
import { CartProvider } from "./context/CartContext.jsx";
import PreviewCart from "./pages/PreviewCart";

function App() {
  return (
    <CartProvider>
      <Router>
      <Routes>

        {/* No navbar */}
        <Route path="/" element={<Login/>} />
        <Route path="/register" element={<Register />} />

        {/* With navbar */}
        <Route element={<MainLayout />}>
          <Route path="/books" element={<BooksPage />} />
          <Route path="/books/:id" element={<BookDetails />} />
          <Route path="/cart" element={<PreviewCart />} />
        </Route>

      </Routes>
    </Router>

    </CartProvider>
  );
}

export default App;
