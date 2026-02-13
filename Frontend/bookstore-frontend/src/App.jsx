import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Login from "./pages/Login";
import BooksPage from "./pages/BooksPage";
import BookDetails from "./pages/BookDetails";
import Register from "./pages/Register"
import MainLayout from "./Layouts/MainLayout";

function App() {
  return (
    <Router>
      <Routes>

        {/* No navbar */}
        <Route path="/" element={<Login/>} />
        <Route path="/register" element={<Register />} />

        {/* With navbar */}
        <Route element={<MainLayout />}>
          <Route path="/books" element={<BooksPage />} />
          <Route path="/books/:id" element={<BookDetails />} />
        </Route>

      </Routes>
    </Router>
  );
}

export default App;
