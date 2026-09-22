const express = require("express");
const cors = require("cors");
const Database = require("better-sqlite3");

const app = express();

app.use(cors());
app.use(express.json());

// Create/connect to the SQLite database
const db = new Database("tripplanner.db");

// Create the trips table if it doesn't exist
db.prepare(`
    CREATE TABLE IF NOT EXISTS trips (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        name TEXT NOT NULL,
        destination TEXT NOT NULL,
        start_date TEXT,
        end_date TEXT
    )
`).run();

// Test endpoint
app.get("/", (req, res) => {
    res.json({
        message: "TripPlanner REST API is running!"
    });
});

// API test endpoint
app.get("/api/test", (req, res) => {
    res.json({
        success: true,
        message: "The API is working correctly."
    });
});

// Get all trips
app.get("/api/trips", (req, res) => {
    const trips = db.prepare("SELECT * FROM trips ORDER BY id DESC").all();

    res.json(trips);
});

// Create a trip
app.post("/api/trips", (req, res) => {
    const { name, destination, start_date, end_date } = req.body;

    if (!name || !destination) {
        return res.status(400).json({
            error: "Name and destination are required"
        });
    }

    const result = db.prepare(`
        INSERT INTO trips (name, destination, start_date, end_date)
        VALUES (?, ?, ?, ?)
    `).run(name, destination, start_date || null, end_date || null);

    const trip = db
        .prepare("SELECT * FROM trips WHERE id = ?")
        .get(result.lastInsertRowid);

    res.status(201).json(trip);
});

const PORT = 3000;

app.listen(PORT, () => {
    console.log(`TripPlanner API running on http://localhost:${PORT}`);
});