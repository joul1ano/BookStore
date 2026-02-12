import { useState } from "react";
import { createBook } from "../services/bookService";

function CreateBook() {
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

    const requiredFields = [
        "title",
        "author",
        "genre",
        "numberOfPages",
        "price",
        "availability"
    ];

    const [error, setError] = useState(null);
    const [success, setSuccess] = useState("");

    const handleChange = (e) => {
        setFormData({
            ...formData,
            [e.target.name]: e.target.value,
        });
        setError(null);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        // Frontend validation
        for (const field of requiredFields) {
            if (!formData[field] || formData[field].toString().trim() === "") {
                setError("Please fill in all required fields");
                return;
            }
        }

        setError("");

        try {
            await createBook({
                ...formData,
                numberOfPages: Number(formData.numberOfPages),
                price: Number(formData.price),
                availability: Number(formData.availability),
                publisherId: Number(formData.publisherId),
            });

            setSuccess("Book created successfully!");
            setError(null);

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
            setError(err.response?.data?.message || { general: "Failed to create book" });
            setSuccess("");
        }
    };

    return (
        <div className="container mt-5">
            <div className="row justify-content-center">
                <div className="col-md-6">
                    <div className="card shadow">
                        <div className="card-body">
                            <h3 className="card-title text-center mb-4">Create Book</h3>

                            {error && (
                                <div className="alert alert-danger mb-3" role="alert">
                                    {typeof error === "string" ? (
                                        error
                                    ) : (
                                        <ul className="mb-0">
                                            {Object.entries(error).map(([field, msg]) => (
                                                <li key={field}>
                                                    <strong>{msg}.</strong> 
                                                </li>
                                            ))}
                                        </ul>

                                    )}
                                </div>
                            )}

                            {success && (
                                <div className="alert alert-success">
                                    {success}
                                </div>
                            )}

                            <form onSubmit={handleSubmit}>
                                <div className="mb-3">
                                    <label className="form-label">Title</label>
                                    <input className="form-control" name="title" onChange={handleChange} value={formData.title} />
                                </div>

                                <div className="mb-3">
                                    <label className="form-label">Author</label>
                                    <input className="form-control" name="author" onChange={handleChange} value={formData.author} />
                                </div>

                                <div className="mb-3">
                                    <label className="form-label">Description</label>
                                    <textarea className="form-control" name="description" onChange={handleChange} value={formData.description} />
                                </div>

                                <div className="mb-3">
                                    <label htmlFor="genre" className="form-label">
                                        Genre
                                    </label>

                                    <select
                                        id="genre"
                                        name="genre"
                                        className="form-select"
                                        value={formData.genre}
                                        onChange={handleChange}
                                        required
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


                                <div className="mb-3">
                                    <label className="form-label">Pages</label>
                                    <input type="number" className="form-control" name="numberOfPages" onChange={handleChange} value={formData.numberOfPages} />
                                </div>

                                <div className="mb-3">
                                    <label className="form-label">Price</label>
                                    <input type="number" step="0.01" className="form-control" name="price" onChange={handleChange} value={formData.price} />
                                </div>

                                <div className="mb-3">
                                    <label className="form-label">Availability</label>
                                    <input type="number" className="form-control" name="availability" onChange={handleChange} value={formData.availability} />
                                </div>

                                <div className="mb-3">
                                    <label className="form-label">Publisher ID</label>
                                    <input type="number" className="form-control" name="publisherId" onChange={handleChange} value={formData.publisherId} />
                                </div>

                                <button type="submit" className="btn btn-primary w-100">
                                    Create Book
                                </button>
                            </form>

                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default CreateBook;