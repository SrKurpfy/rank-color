CREATE TABLE IF NOT EXISTS `color_users` (
    id CHAR(36) NOT NULL PRIMARY KEY,
    name VARCHAR(16) UNIQUE NOT NULL,
    rank_color VARCHAR(36)
);