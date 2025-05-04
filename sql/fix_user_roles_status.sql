-- Update user roles to match enum values
UPDATE users
SET role = UPPER(role)
WHERE role IS NOT NULL;

-- Fix specific role values that do not match enum
UPDATE users
SET role = 'CLIENT'
WHERE LOWER(role) = 'user';

-- Update user statuses to match enum values
UPDATE users
SET status = LOWER(status)
WHERE status IS NOT NULL;

-- Fix specific status values that do not match enum
UPDATE users
SET status = 'non_confirmed'
WHERE LOWER(status) = 'active';

-- Optional: Verify changes
SELECT id, role, status FROM users;
