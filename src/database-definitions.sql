CREATE TABLE Party (
    pid INTEGER,
    name TEXT,
    mid INTEGER,
    vid INTEGER,
    eid INTEGER,
    price NUMERIC NOT NULL,
    timing  TIMESTAMP NOT NULL,
    numberofguests INT NOT NULL,

    FOREIGN KEY (mid) REFERENCES Menu(mid),
    FOREIGN KEY (vid) REFERENCES Venue(vid),
    FOREIGN KEY (eid) REFERENCES Entertainment(eid),

    CONSTRAINT PartyKey PRIMARY KEY (pid),
    CONSTRAINT MinGuests CHECK (numberofguests > 0)
)
;

CREATE TABLE Venue (
    vid INTEGER,
    name TEXT NOT NULL,
    costprice NUMERIC NOT NULL,

    CONSTRAINT  VenueKey PRIMARY KEY (vid),
    CONSTRAINT MinVenueCost CHECK (venuecost > 0)
)
;
CREATE TABLE Menu (
    mid INTEGER,
    description TEXT NOT NULL,
    costprice   NUMERIC NOT NULL,

    CONSTRAINT MenuKey PRIMARY KEY (mid),
    CONSTRAINT MinMenuCost CHECK (costprice > 0)
)
;
CREATE TABLE Entertainment (
    eid INTEGER,
    description TEXT NOT NULL,
    costprice   NUMERIC NOT NULL,
    CONSTRAINT  EntertainmentKey PRIMARY KEY (eid),
    CONSTRAINT MinEntertainmentCost CHECK (costprice > 0)
)
;
