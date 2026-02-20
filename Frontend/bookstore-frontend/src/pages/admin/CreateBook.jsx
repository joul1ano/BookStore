
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { createBook } from "../../services/bookService";

function CreateBook() {
    const navigate = useNavigate();

    const [formData, setFormData] = useState({
        title: "",
        author: "",
        description: "",
        genre: "",
        numberOfPages: "",
        price: "",
        availability: "",
        publisherId: ""
    });

    const requiredFields = ["title", "author", "genre", "numberOfPages", "price", "availability", "publisherId"];

    const [error, setError] = useState(null);
    const [success, setSuccess] = useState("");

    const handleChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
        setError(null);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        for (const field of requiredFields) {
            if (!formData[field] || formData[field].toString().trim() === "") {
                setError("Please fill in all required fields.");
                return;
            }
        }

        setError(null);

        try {
            await createBook({
                ...formData,
                numberOfPages: Number(formData.numberOfPages),
                price: Number(formData.price),
                availability: Number(formData.availability),
                publisherId: formData.publisherId ? Number(formData.publisherId) : null,
            });

            setSuccess("Book created successfully!");
            setFormData({
                title: "",
                author: "",
                description: "",
                genre: "",
                numberOfPages: "",
                price: "",
                availability: "",
                publisherId: "",
            });
        } catch (err) {
            setError(err.response?.data?.message || "Failed to create book.");
            setSuccess("");
        }
    };

    return (
        <div className="p-4">
            {/* Breadcrumb */}
            <div className="text-muted mb-1">
                Dashboard <span className="mx-1">›</span>
                <span
                    className="text-success fw-semibold"
                    style={{ cursor: "pointer" }}
                    onClick={() => navigate("/admin/products")}
                >
                    Products
                </span>
                <span className="mx-1">›</span>
                <span className="fw-semibold">Add New Book</span>
            </div>

            {/* Title row */}
            <div className="d-flex justify-content-between align-items-center mb-4">
                <h2 className="fw-bold">Add New Book</h2>
                <button
                    className="btn btn-outline-secondary"
                    onClick={() => navigate("/admin/products")}
                >
                    ← Back to Products
                </button>
            </div>

            {/* Alerts */}
            {error && (
                <div className="alert alert-danger">{error}</div>
            )}
            {success && (
                <div className="alert alert-success">{success}</div>
            )}

            {/* Form card */}
            <div className="card shadow-sm">
                <div className="card-body p-4">
                    <form onSubmit={handleSubmit}>
                        <div className="row g-3">

                            {/* Title */}
                            <div className="col-md-6">
                                <label className="form-label fw-semibold">
                                    Title <span className="text-danger">*</span>
                                </label>
                                <input
                                    className="form-control"
                                    name="title"
                                    placeholder="Enter book title"
                                    onChange={handleChange}
                                    value={formData.title}
                                />
                            </div>

                            {/* Author */}
                            <div className="col-md-6">
                                <label className="form-label fw-semibold">
                                    Author <span className="text-danger">*</span>
                                </label>
                                <input
                                    className="form-control"
                                    name="author"
                                    placeholder="Enter author name"
                                    onChange={handleChange}
                                    value={formData.author}
                                />
                            </div>

                            {/* Genre */}
                            <div className="col-md-6">
                                <label className="form-label fw-semibold">
                                    Genre <span className="text-danger">*</span>
                                </label>
                                <select
                                    className="form-select"
                                    name="genre"
                                    value={formData.genre}
                                    onChange={handleChange}
                                >
                                    <option value="">Select genre</option>
                                    <option value="FICTION">Fiction</option>
                                    <option value="NON_FICTION">Non-fiction</option>
                                    <option value="MYSTERY">Mystery</option>
                                    <option value="THRILLER">Thriller</option>
                                    <option value="FANTASY">Fantasy</option>
                                    <option value="SCIENCE_FICTION">Science Fiction</option>
                                    <option value="ROMANCE">Romance</option>
                                    <option value="HORROR">Horror</option>
                                    <option value="BIOGRAPHY">Biography</option>
                                    <option value="HISTORY">History</option>
                                    <option value="SELF_HELP">Self-help</option>
                                    <option value="POETRY">Poetry</option>
                                    <option value="CLASSICS">Classics</option>
                                    <option value="ADVENTURE">Adventure</option>
                                    <option value="CHILDREN">Children</option>
                                    <option value="YOUNG_ADULT">Young Adult</option>
                                    <option value="GRAPHIC_NOVEL">Graphic Novel</option>
                                    <option value="TRAVEL">Travel</option>
                                    <option value="RELIGION">Religion</option>
                                    <option value="COOKBOOK">Cookbook</option>
                                </select>
                            </div>

                            {/* Pages */}
                            <div className="col-md-6">
                                <label className="form-label fw-semibold">
                                    Number of Pages <span className="text-danger">*</span>
                                </label>
                                <input
                                    type="number"
                                    className="form-control"
                                    name="numberOfPages"
                                    placeholder="e.g. 320"
                                    onChange={handleChange}
                                    value={formData.numberOfPages}
                                />
                            </div>

                            {/* Price */}
                            <div className="col-md-4">
                                <label className="form-label fw-semibold">
                                    Price (€) <span className="text-danger">*</span>
                                </label>
                                <input
                                    type="number"
                                    step="0.01"
                                    className="form-control"
                                    name="price"
                                    placeholder="e.g. 12.99"
                                    onChange={handleChange}
                                    value={formData.price}
                                />
                            </div>

                            {/* Availability */}
                            <div className="col-md-4">
                                <label className="form-label fw-semibold">
                                    Availability (stock) <span className="text-danger">*</span>
                                </label>
                                <input
                                    type="number"
                                    className="form-control"
                                    name="availability"
                                    placeholder="e.g. 50"
                                    onChange={handleChange}
                                    value={formData.availability}
                                />
                            </div>

                            {/* Publisher ID */}
                            <div className="col-md-4">
                                <label className="form-label fw-semibold">
                                    Publisher ID <span className="text-danger">*</span>
                                </label>
                                <input
                                    type="number"
                                    className="form-control"
                                    name="publisherId"
                                    placeholder="Enter publisher ID"  
                                    onChange={handleChange}
                                    value={formData.publisherId}
                                />
                            </div>

                            {/* Description */}
                            <div className="col-12">
                                <label className="form-label fw-semibold">Description</label>
                                <textarea
                                    className="form-control"
                                    name="description"
                                    rows={4}
                                    placeholder="Enter a short description of the book (optional)"
                                    onChange={handleChange}
                                    value={formData.description}
                                />
                            </div>

                            {/* Submit */}
                            <div className="col-12 d-flex justify-content-end gap-2 mt-2">
                                <button
                                    type="button"
                                    className="btn btn-outline-secondary"
                                    onClick={() => navigate("/admin/products")}
                                >
                                    Cancel
                                </button>
                                <button type="submit" className="btn btn-success">
                                    <i className="bi bi-plus-circle me-1"></i> Create Book
                                </button>
                            </div>

                        </div>
                    </form>
                </div>
            </div>
        </div>
    );
}

export default CreateBook;