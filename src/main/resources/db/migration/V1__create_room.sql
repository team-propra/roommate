CREATE TABLE room (
    id UUID PRIMARY KEY,
    room_number VARCHAR(255) NOT NULL
);

CREATE TABLE item (
    item_name VARCHAR(255) PRIMARY KEY
);

CREATE TABLE booked_timeframe (
    id UUID PRIMARY KEY,
    day_of_week VARCHAR(255) NOT NULL,
    local_time TIME NOT NULL,
    duration INTERVAL NOT NULL,
    room_id UUID NOT NULL,
    FOREIGN KEY room_id REFERENCES room(id)
);

CREATE TABLE item_to_room (
    id UUID PRIMARY KEY,
    item_name VARCHAR(255) NOT NULL,
    room_id UUID NOT NULL,
    FOREIGN KEY room_id REFERENCES room(id),
    FOREIGN KEY item_name REFERENCES item(item_name)
)