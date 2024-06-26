CREATE TABLE workspace
(
    id          UUID PRIMARY KEY,
    workspace_number INT  NOT NULL,
    room_id     UUID NOT NULL REFERENCES room (id) ON DELETE CASCADE

);

CREATE TABLE item_to_workspace
(
    id        UUID PRIMARY KEY,
    item_name VARCHAR(255) NOT NULL REFERENCES item (item_name) ON DELETE CASCADE,
    workspace_id   UUID         NOT NULL REFERENCES workspace (id) ON DELETE CASCADE
);

DROP TABLE item_to_room;

ALTER TABLE booked_timeframe DROP room_id;
ALTER TABLE booked_timeframe ADD workspace_id UUID NOT NULL REFERENCES workspace(id) ON DELETE CASCADE

