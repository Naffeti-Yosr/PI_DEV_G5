-- Insert sample users into 'users' table
INSERT INTO `users`(`id`, `nom`, `prenom`, `birth_date`, `email`, `adresse`, `telephone`, `password`, `role`, `status`) VALUES
(1, 'Doe', 'John', '1980-01-01', 'john.doe@example.com', '123 Main St', '1234567890', 'password123', 'user', 'active'),
(2, 'Smith', 'Jane', '1990-05-15', 'jane.smith@example.com', '456 Elm St', '0987654321', 'password123', 'user', 'active');

-- Insert sample reclamations linked to users
INSERT INTO Reclamation (user_id, report, status, created_at) VALUES
(1, 'Example claim report: The product arrived damaged.', 'Pending', CURRENT_TIMESTAMP),
(2, 'Late delivery of the order.', 'Pending', CURRENT_TIMESTAMP);
