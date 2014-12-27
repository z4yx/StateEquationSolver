CREATE TABLE `substance` (
  `_id`  INTEGER PRIMARY KEY AUTOINCREMENT,
  `name`  varchar(16) NOT NULL,
  `Tc`  REAL NOT NULL,
  `Pc`  REAL NOT NULL,
  `Vc`  REAL NOT NULL,
  `Zc`  REAL NOT NULL,
  `w`  REAL NOT NULL
);

CREATE TABLE `antoine`(
  `_id`  INTEGER PRIMARY KEY AUTOINCREMENT,
  `name`  varchar(16) NOT NULL,
  `A`  REAL NOT NULL,
  `B`  REAL NOT NULL,
  `C`  REAL NOT NULL
);
CREATE INDEX a_n on antoine(name);