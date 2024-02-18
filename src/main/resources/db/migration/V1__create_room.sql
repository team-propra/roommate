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
    duration CHARACTER VARYING NOT NULL,
    room_id UUID NOT NULL REFERENCES room(id),
    user_handle VARCHAR(255) REFERENCES users(handle)
);

CREATE TABLE item_to_room (
    id UUID PRIMARY KEY,
    item_name VARCHAR(255) NOT NULL REFERENCES item(item_name),
    room_id UUID NOT NULL REFERENCES room(id)
)