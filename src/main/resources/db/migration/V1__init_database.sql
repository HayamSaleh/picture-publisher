
CREATE TABLE users (
  id SERIAL PRIMARY KEY,
  username VARCHAR(255) UNIQUE NOT NULL,
  email VARCHAR(255) UNIQUE NOT NULL,
  password VARCHAR(255) NOT NULL,
  role VARCHAR(255) default 'USER_ROLE'
);

CREATE TABLE pictures (
    id SERIAL PRIMARY KEY,
    owner_id BIGINT NOT NULL,
    name VARCHAR(255),
    description VARCHAR(255),
    category VARCHAR(255) NOT NULL,
    status VARCHAR(255) default 'PENDING',
    width DOUBLE PRECISION,
    height DOUBLE PRECISION,
    size DOUBLE PRECISION,
    picture_file bytea,
    url VARCHAR(255),
    FOREIGN KEY (owner_id) REFERENCES users(id)
);