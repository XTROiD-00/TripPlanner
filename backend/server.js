
const express = require("express");
const cors = require("cors");
const Database = require("better-sqlite3");

const app = express();

app.use(cors());
app.use(express.json());

const db = new Database("tripplanner.db");

// Create database tables
db.exec(`
    CREATE TABLE IF NOT EXISTS trips (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        name TEXT NOT NULL,
        destination TEXT NOT NULL,
        start_date TEXT,
        end_date TEXT,
        notes TEXT,
        client_id INTEGER
    );

    CREATE TABLE IF NOT EXISTS activities (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        trip_id INTEGER NOT NULL,
        client_id INTEGER NOT NULL,
        title TEXT NOT NULL,
        description TEXT,
        date TEXT NOT NULL,
        start_time TEXT,
        end_time TEXT,
        location TEXT,
        completed INTEGER DEFAULT 0,
        FOREIGN KEY (trip_id) REFERENCES trips(id) ON DELETE CASCADE,
        UNIQUE(trip_id, client_id)
    );

    CREATE TABLE IF NOT EXISTS expenses (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        trip_id INTEGER NOT NULL,
        client_id INTEGER NOT NULL,
        amount REAL NOT NULL,
        category TEXT NOT NULL,
        description TEXT,
        date TEXT NOT NULL,
        photo_path TEXT,
        FOREIGN KEY (trip_id) REFERENCES trips(id) ON DELETE CASCADE,
        UNIQUE(trip_id, client_id)
    );

    CREATE TABLE IF NOT EXISTS reminders (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        trip_id INTEGER NOT NULL,
        client_id INTEGER NOT NULL,
        activity_id INTEGER,
        title TEXT NOT NULL,
        reminder_date_time TEXT NOT NULL,
        enabled INTEGER DEFAULT 1,
        FOREIGN KEY (trip_id) REFERENCES trips(id) ON DELETE CASCADE,
        FOREIGN KEY (activity_id) REFERENCES activities(id),
        UNIQUE(trip_id, client_id)
    );

    CREATE TABLE IF NOT EXISTS saved_places (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        trip_id INTEGER NOT NULL,
        client_id INTEGER NOT NULL,
        name TEXT NOT NULL,
        address TEXT,
        latitude REAL,
        longitude REAL,
        notes TEXT,
        FOREIGN KEY (trip_id) REFERENCES trips(id) ON DELETE CASCADE,
        UNIQUE(trip_id, client_id)
    );
`);

// Upgrade existing trips table
const tripColumns = db.prepare("PRAGMA table_info(trips)").all();
const existingColumns = tripColumns.map(column => column.name);

if (!existingColumns.includes("notes")) {
    db.exec("ALTER TABLE trips ADD COLUMN notes TEXT");
}

if (!existingColumns.includes("client_id")) {
    db.exec("ALTER TABLE trips ADD COLUMN client_id INTEGER");
}

db.exec(`
    CREATE UNIQUE INDEX IF NOT EXISTS idx_trips_client_id
    ON trips(client_id)
    WHERE client_id IS NOT NULL;
`);

// Test endpoints
app.get("/", (req, res) => {
    res.json({ message: "TripPlanner REST API is running!" });
});

app.get("/api/test", (req, res) => {
    res.json({
        success: true,
        message: "The API is working correctly."
    });
});

// =========================
// TRIPS
// =========================

app.get("/api/trips", (req, res) => {
    const trips = db.prepare(
        "SELECT * FROM trips ORDER BY id DESC"
    ).all();

    res.json(trips);
});

app.post("/api/trips", (req, res) => {
    const {
        name, destination, start_date, end_date, notes, client_id
    } = req.body;

    if (!name || !destination) {
        return res.status(400).json({
            error: "Name and destination are required"
        });
    }

    let trip;

    if (client_id != null) {
        const existing = db.prepare(
            "SELECT * FROM trips WHERE client_id = ?"
        ).get(client_id);

        if (existing) {
            db.prepare(`
                UPDATE trips
                SET name = ?, destination = ?, start_date = ?,
                    end_date = ?, notes = ?
                WHERE id = ?
            `).run(name, destination, start_date || null,
                end_date || null, notes || null, existing.id);

            trip = db.prepare(
                "SELECT * FROM trips WHERE id = ?"
            ).get(existing.id);
        }
    }

    if (!trip) {
        const result = db.prepare(`
            INSERT INTO trips
            (name, destination, start_date, end_date, notes, client_id)
            VALUES (?, ?, ?, ?, ?, ?)
        `).run(name, destination, start_date || null,
            end_date || null, notes || null, client_id ?? null);

        trip = db.prepare(
            "SELECT * FROM trips WHERE id = ?"
        ).get(result.lastInsertRowid);
    }

    res.status(201).json(trip);
});

// Check that a backend trip exists
function validTrip(tripId) {
    return db.prepare(
        "SELECT id FROM trips WHERE id = ?"
    ).get(tripId);
}

// =========================
// ACTIVITIES
// =========================

app.get("/api/trips/:tripId/activities", (req, res) => {
    const activities = db.prepare(
        "SELECT * FROM activities WHERE trip_id = ? ORDER BY date"
    ).all(req.params.tripId);

    res.json(activities);
});

app.post("/api/activities", (req, res) => {
    const {
        trip_id, client_id, title, description, date,
        start_time, end_time, location, completed
    } = req.body;

    if (!validTrip(trip_id) || client_id == null || !title || !date) {
        return res.status(400).json({
            error: "Valid trip_id, client_id, title and date are required"
        });
    }

    db.prepare(`
        INSERT INTO activities
        (trip_id, client_id, title, description, date,
         start_time, end_time, location, completed)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
        ON CONFLICT(trip_id, client_id) DO UPDATE SET
            title = excluded.title,
            description = excluded.description,
            date = excluded.date,
            start_time = excluded.start_time,
            end_time = excluded.end_time,
            location = excluded.location,
            completed = excluded.completed
    `).run(
        trip_id, client_id, title, description || null, date,
        start_time || null, end_time || null, location || null,
        completed ? 1 : 0
    );

    const activity = db.prepare(`
        SELECT * FROM activities WHERE trip_id = ? AND client_id = ?
    `).get(trip_id, client_id);

    res.status(201).json(activity);
});

// =========================
// EXPENSES
// =========================

app.get("/api/trips/:tripId/expenses", (req, res) => {
    const expenses = db.prepare(
        "SELECT * FROM expenses WHERE trip_id = ? ORDER BY date DESC"
    ).all(req.params.tripId);

    res.json(expenses);
});

app.post("/api/expenses", (req, res) => {
    const {
        trip_id, client_id, amount, category,
        description, date, photo_path
    } = req.body;

    if (!validTrip(trip_id) || client_id == null ||
        amount == null || !category || !date) {
        return res.status(400).json({
            error: "Required expense fields are missing"
        });
    }

    db.prepare(`
        INSERT INTO expenses
        (trip_id, client_id, amount, category, description, date, photo_path)
        VALUES (?, ?, ?, ?, ?, ?, ?)
        ON CONFLICT(trip_id, client_id) DO UPDATE SET
            amount = excluded.amount,
            category = excluded.category,
            description = excluded.description,
            date = excluded.date,
            photo_path = excluded.photo_path
    `).run(
        trip_id, client_id, amount, category,
        description || null, date, photo_path || null
    );

    const expense = db.prepare(`
        SELECT * FROM expenses WHERE trip_id = ? AND client_id = ?
    `).get(trip_id, client_id);

    res.status(201).json(expense);
});

// =========================
// REMINDERS
// =========================

app.get("/api/trips/:tripId/reminders", (req, res) => {
    const reminders = db.prepare(
        "SELECT * FROM reminders WHERE trip_id = ?"
    ).all(req.params.tripId);

    res.json(reminders);
});

app.post("/api/reminders", (req, res) => {
    const {
        trip_id, client_id, activity_client_id,
        title, reminder_date_time, enabled
    } = req.body;

    if (!validTrip(trip_id) || client_id == null ||
        !title || !reminder_date_time) {
        return res.status(400).json({
            error: "Required reminder fields are missing"
        });
    }

    let activityId = null;

    if (activity_client_id != null) {
        const activity = db.prepare(`
            SELECT id FROM activities
            WHERE trip_id = ? AND client_id = ?
        `).get(trip_id, activity_client_id);

        if (!activity) {
            return res.status(400).json({
                error: "Linked activity has not been synced yet"
            });
        }

        activityId = activity.id;
    }

    db.prepare(`
        INSERT INTO reminders
        (trip_id, client_id, activity_id, title, reminder_date_time, enabled)
        VALUES (?, ?, ?, ?, ?, ?)
        ON CONFLICT(trip_id, client_id) DO UPDATE SET
            activity_id = excluded.activity_id,
            title = excluded.title,
            reminder_date_time = excluded.reminder_date_time,
            enabled = excluded.enabled
    `).run(
        trip_id, client_id, activityId, title,
        reminder_date_time, enabled === false ? 0 : 1
    );

    const reminder = db.prepare(`
        SELECT * FROM reminders WHERE trip_id = ? AND client_id = ?
    `).get(trip_id, client_id);

    res.status(201).json(reminder);
});

// =========================
// SAVED PLACES
// =========================

app.get("/api/trips/:tripId/saved-places", (req, res) => {
    const places = db.prepare(
        "SELECT * FROM saved_places WHERE trip_id = ? ORDER BY name"
    ).all(req.params.tripId);

    res.json(places);
});

app.post("/api/saved-places", (req, res) => {
    const {
        trip_id, client_id, name, address,
        latitude, longitude, notes
    } = req.body;

    if (!validTrip(trip_id) || client_id == null || !name) {
        return res.status(400).json({
            error: "Valid trip_id, client_id and name are required"
        });
    }

    db.prepare(`
        INSERT INTO saved_places
        (trip_id, client_id, name, address, latitude, longitude, notes)
        VALUES (?, ?, ?, ?, ?, ?, ?)
        ON CONFLICT(trip_id, client_id) DO UPDATE SET
            name = excluded.name,
            address = excluded.address,
            latitude = excluded.latitude,
            longitude = excluded.longitude,
            notes = excluded.notes
    `).run(
        trip_id, client_id, name, address || null,
        latitude ?? null, longitude ?? null, notes || null
    );

    const place = db.prepare(`
        SELECT * FROM saved_places WHERE trip_id = ? AND client_id = ?
    `).get(trip_id, client_id);

    res.status(201).json(place);
});

// Start server
const PORT = process.env.PORT || 3000;

app.listen(PORT, "0.0.0.0", () => {
    console.log(`TripPlanner API running on port ${PORT}`);
});
