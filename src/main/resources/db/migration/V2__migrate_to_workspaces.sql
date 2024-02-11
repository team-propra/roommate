CREATE TABLE item_to_workspace
(
    id        UUID PRIMARY KEY,
    item_name VARCHAR(255) NOT NULL REFERENCES item (item_name),
    workspace_id   UUID         NOT NULL REFERENCES workspace (id)
);

CREATE TABLE workspace
(
    id          UUID PRIMARY KEY,
    room_number INT  NOT NULL,
    room_id     UUID NOT NULL REFERENCES room (id)

);

DROP TABLE item_to_room;

