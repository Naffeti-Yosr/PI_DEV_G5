-- SQL script to create the Reclamation table
CREATE TABLE Reclamation (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    report TEXT NOT NULL,
    status VARCHAR(50) DEFAULT 'Pending',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_user
        FOREIGN KEY (user_id) REFERENCES User(id)
        ON DELETE CASCADE
);
